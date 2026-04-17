/**
 * 批量处理直方图
 */
layui.define(['bar', "dataA_dataService"], function (exports) {
    //
    var person_color = ['#8B4513'], unit_color = ['#FFD700'];
    //
    var instance;
    var chart;
    var bar = layui.bar;
    var dataService = layui.dataA_dataService;
    //
    var option = {
        title : {
            text: '',
            left: 'center',
            top:5
        },
        grid:{
            bottom:70,
            left:50,
        },
        tooltip : {
            trigger: 'axis'
        },
        calculable : true,
        xAxis : [
            {
                type : 'category',
                data : [], 
                axisTick: {
                    alignWithLabel: true
                },
                axisLabel: {
                    show: true,
                    interval: 0,
                    rotate: 40,
                    textStyle: {
                        color: '#682d19'
                    }
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
            }
        ],
        series : [
            {
                name:'',
                type:'bar',
                barMaxWidth:40,
                data:[],
               
            }
        ]
    };
    
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        //获取数据
        //-----
        var multi_data = dataService.getTypeData(obj)['bar'];
        if (multi_data) {
            $.each(multi_data, function (k, o) {
                option.title.text=o.title;
                option.series[0].name=o.name;
                option.xAxis[0].data=o.category;
                option.series[0].data=o.data;
                new bar(k,option);
            })
        }
        //
    }
    //
    function change(obj) {
        setOption(obj);
        console.log("条形图重绘成功");
    }
    function init(obj) {
        //设置option
        setOption(obj);
    }
    exports('dataA_bar', {
        init: init,
        change: change
    }); 
});