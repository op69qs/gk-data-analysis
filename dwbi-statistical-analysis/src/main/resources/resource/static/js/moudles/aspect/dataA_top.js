/**
 * 条形图 top排序
 */
layui.define(['bar', "dataA_dataService"], function (exports) {
    var divId = "sqspLine";
    //
    var person_color = ['#8B4513'], unit_color = ['#FFD700'];
    //
    var instance;
    var chart;
    var bar = layui.bar;
    var dataService = layui.dataA_dataService;
    //
    var option = {
        color: ['#3398DB'],
        title: {
            text: '',
            left: 'center',
            top:5
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '15%',
            bottom: '3%',
            containLabel: true
        },
        yAxis: [
            {
                type: 'category',
                data: [],
                axisLine: {
                    show: false
                },
                axisTick: {
                    show: false
                }
            }
        ],
        xAxis: [
            {
                show: false,
                type: 'value'
            }
        ],
        series: [
            {
                name: '',
                label: {
                    show: true,
                    position: "right"
                },
                type: 'bar',
                barWidth: '60%',
                data: []
            }
        ]
    };
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        //获取数据
        //-----
        var multi_data = dataService.getTypeData(obj)['top'];
        if (multi_data) {
            $.each(multi_data, function (k, o) {
                option.title.text = o.title;
                option.series[0].name = o.name;
                //处理类目组过长  和 数值排序问题
                var re = handlerData(o.category, o.data);
                //
                option.yAxis[0].data = re.category;
                option.series[0].data = re.data;
                new bar(k, option);
            })
        }
        //
    }
    function handlerData(category, data) {
        var sort = [];
        $.each(category, function (i, v) {
            var item = {
                name: v,
                value: data[i]
            }
            sort.push(item);
        })
        sort.sort(function (a, b) {
            return a.value - b.value;
        })
        var category_ = [], data_ = [];
        $.each(sort, function (i, o) {
            category_.push(o.name);
            data_.push(o.value);
        })
        return {
            category: category_,
            data: data_
        }
    }
    //
    function change(obj) {
        setOption(obj);
        console.log("top排序图重绘成功");
    }
    function init(obj) {
        //设置option
        setOption(obj);
    }
    exports('dataA_top', {
        init: init,
        change: change
    });
});