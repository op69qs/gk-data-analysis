/**
 * 定义 数据分析页面的map
 * 业务：申请审批 页面
 */
layui.define(['map', "dataA_dataService"], function (exports) {
    //
    var TOP = "40",BOTTOM='5', LEFT = "15%",RIGHT='15%', LEVEL = 2;
    var person_color = ['#8B4513'], unit_color = ['#FFD700'];
    //
    var instanceMap = [];
    var map = layui.map;
    var dataService = layui.dataA_dataService;
    //
    function getTip(series, code) {
        var tip = {};
        $.each(series, function (i, o) {
            var v;
            for (var j = 0; i < o.data.length; j++) {
                if (o.data[j]['code'] == code) {
                    v = o.data[j]['value'];
                    break;
                }
            }
            if('undefined'==typeof v){
                v="-";
            }
            var res = {
                name: o.name,
                value: v
            }
            tip[o.sign] = res;
        })
        return tip;
    }
    var option = {
        title: {
            text: '',
            left: 'center',
            top:5
        },
        tooltip: {
            trigger: 'item',
            formatter: function (params,obj) {
                if(!params.data){
                    return "<div>暂无数据</div>"
                }
                var d = params.data['code'];
                var divId= params.data['divId'];
                var series = echarts.getInstanceByDom(document.getElementById(divId)).getOption().series;
                //getTip
                var tip = getTip(series, d);
                var personC = tip.person.value;
                var unitC = tip.unit.value;
                var tatol = personC + unitC;
                var colo = params.color;
                //
                var personC_marker = params.marker.replace(colo, person_color[0]);
                var unitC_marker = params.marker.replace(colo, unit_color[0])
                //
                return "<div>"+params.data['name']+"共(" + tatol + ")<br>" + personC_marker + " 个人: " + personC + "<br>" + unitC_marker + " 单位: " + unitC + "</div>"
            }
        },
        legend: {
            orient: 'horizontal',
            left: 'left',
            top:"10",
            data: ['个人', '单位']
        },
        visualMap: {
            min: 800,
            max: 50000,
            text: ['High', 'Low'],
            realtime: true,
            show: false,
            calculable: true,
            inRange: {
                color: ['#e6f7ff', '#bae7ff', '#91d5ff', '#69c0ff', '#40a9ff', '#1890ff', '#096dd9', '#0050b3', '#003a8c']
            }
        },
        series: [
            {
                name: '个人',
                type: 'map',
                sign: "person",
                map: 'china',
                roam: false,
                left: LEFT,
                right: RIGHT,
                top: TOP,
                bottom: BOTTOM,
                color: person_color,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data: []
            }, {
                name: '单位',
                type: 'map',
                map: 'china',
                sign: "unit",
                roam: false,
                left: LEFT,
                right: RIGHT,
                top: TOP,
                bottom: BOTTOM,
                color: unit_color,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data: []
            }
        ]
    };
    /**
     * 获取地图数据
     * @param {*} obj 
     */
    function getGeoJson(obj) {
        var code = obj.code;
        var geo = echarts.getMap(code);
        if (!geo) {
            var geojson = dataService.getGeoJson(code);
            echarts.registerMap(code, geojson);
        }
    }
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        instanceMap=[];
        //获取数据
        var multi_data = dataService.getTypeData(obj)['map'];
        //设置option最大和最小
        $.each(multi_data, function (divId, data) {
            var minMax = getMaxMin(data.series);
            option.visualMap.min = minMax['min'];
            option.visualMap.max = minMax['max'];
            option.title.text=data.title;
            //-----
            $.each(option.series, function (k, o) {
                o.map = obj.code;
                o.data = data.series[o.sign];
            })
            var chart=new map(divId, option);
            //注册观察者
            chart.registerObserver(data['observers']);
            instanceMap.push(chart);
        })
    }
    function getMaxMin(mapD) {
        var res = {};
        var res_ = [];
        for (var key in mapD) {
            $.each(mapD[key], function (i, o) {
                if (res[o.code]) {
                    res[o.code] += o.value;
                } else {
                    res[o.code] = o.value;
                }
            })
        }
        for (var k in res) {
            res_.push(res[k]);
        }
        res_.sort(function (a, b) {
            return a - b;
        })
        return {
            min: res_[0],
            max: res_[res_.length - 1]
        }
    }
    //
    function change(obj) {
        getGeoJson(obj);
        //给句条件查询
        setOption(obj);
    }
    function init(obj) {
        getGeoJson(obj);
        //设置option
        setOption(obj);
        //压地图
        //初始化事件
        initEvent();
    }
    //map的点击事件
    function initEvent() {
        $.each(instanceMap, function (i, instance) {
            var chart=instance.getInstance();
            chart.on("click", function (param) {
                if(!param.data){
                    layui.layer.msg("暂无数据", { icon: 5 });   
                    return;
                }
                var code=param.data['code'];
                var name=param.data['name'];
                var suffix=code.substr(code.length-2,2);
                if(suffix=='00'&&code.length==6){  //市级
                    dataA_queryParams.code = code;
                    dataA_queryParams.zqName=name;
                }else{                             //返回省级
                    dataA_queryParams.code = '520000';
                    dataA_queryParams.zqName="贵州省";
                }
                if(window.dataA_chanage){
                    window.dataA_chanage();
                }
                //change(dataA_queryParams);
                //触发观察者
            })
        })
    }
    exports('dataA_map', {
        init: init,
        change: change
    });
});