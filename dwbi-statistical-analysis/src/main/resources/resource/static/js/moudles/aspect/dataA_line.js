/**
 * 数据分析基础折线图
 * 
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
        title: {
            text: '',
            left: "center",
            textStyle: {
                color: "#008acd"
            },
            top:5
        },
        tooltip: {
            trigger: 'axis'
        },
        grid: {
            left: '1%',
            right: '1%',
            bottom: '15%',
            containLabel: true
        },
        legend: {
            x: '10%',
            y: "10%",
            orient: 'horizontal',
            data: [{ name: '个人' },
            { name: '单位' },
            ]
        },
        // dataZoom: [{
        //     type: 'inside',
        //     start: 0,
        //     end: 10
        // }, {
        //     start: 0,
        //     end: 10,
        //     handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
        //     handleSize: '80%',
        //     handleStyle: {
        //         color: '#fff',
        //         shadowBlur: 3,
        //         shadowColor: 'rgba(0, 0, 0, 0.6)',
        //         shadowOffsetX: 2,
        //         shadowOffsetY: 2
        //     }
        // }],
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: []
        },
        yAxis: {
            type: 'value'
        },
        series: [
            {
                name: '个人',
                type: 'line',
                sign: "person",
                color: person_color,
                data: []
            },
            {
                name: '单位',
                type: 'line',
                sign: "unit",
                color: unit_color,
                data: []
            },

        ]
    };
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        //获取数据
        var multi_data = dataService.getTypeData(obj)['line'];
        //-----
        $.each(multi_data,function(divId,data){
            option.xAxis.data = data.category;
            option.title.text=data.title;
            $.each(option.series, function (k, o) {
                o.data = data[o.sign];
            })
            instanceMap = new bar(divId, option);
        })
    }
    //
    function change(obj) {
        //给句条件查询
        setOption(obj);
        console.log("折现图重绘成功");
    }
    function init(obj) {
        //设置option
        setOption(obj);
    }
    exports('dataA_line', {
        init: init,
        change: change
    });
});