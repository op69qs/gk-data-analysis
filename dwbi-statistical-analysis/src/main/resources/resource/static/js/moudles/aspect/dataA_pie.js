/**
 * 批量处理直方图
 */
layui.define(['pie', "dataA_dataService"], function (exports) {
    //
    var person_color = ['#8B4513'], unit_color = ['#FFD700'];
    //
    var instance;
    var chart;
    var pie = layui.pie;
    var dataService = layui.dataA_dataService;
    //
    var option = {
        title: {
            text: '',
            left: 'center',
            top:5
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'horizontal',
            bottom: 2,
            show:false,
            data: []
        },
        series: [
            {
                name: '',
                type: 'pie',
                radius: ['35%','50%'],
                center: ['50%', '55%'],
                data: [],
                color: ['#c487ee', '#deb140', '#49dff0', '#034079', '#6f81da', '#00ffb4'],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        //获取数据
        //-----
        var multi_data = dataService.getTypeData(obj)['pie'];
        //设置option
        if (multi_data) {
            $.each(multi_data, function (k, d) {
                option.title.text = d.title;
                option.series[0].data = d.data;
                option.series[0].name = d.title;
                var l_data = [];
                $.each(d.data, function (l, lt) {
                    l_data.push(lt['name']);
                })
                option.legend.data = l_data;
                new pie(k, option);
            })
        }
        //
    }
    //
    function change(obj) {
        setOption(obj);
        console.log("饼图重绘成功");
    }
    function init(obj) {
        //设置option
        setOption(obj);
    }
    exports('dataA_pie', {
        init: init,
        change: change
    }); 
});