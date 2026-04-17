/**
 * 批量处理直方图
 */
layui.define(['Dtree', "dataA_dataService"], function (exports) {
    var instance;
    var chart;
    var Dtree = layui.Dtree;
    var dataService = layui.dataA_dataService;
    //
    var option = {
        title : {
            text: '',
            left: 'center',
            top:5
        },
        tooltip: {
            trigger: 'item',
            triggerOn: 'mousemove'
        },
        series: [
            {
                type: 'tree',
                name: 'tree1',
                data: [],
                top: '5%',
                left: '15%',
                bottom: '2%',
                right: '7%',
                symbolSize: 9,
                label: {
                    normal: {
                        position: 'left',
                        verticalAlign: 'middle',
                        align: 'right',
                        formatter:function(params){
                            var d=params.data;
                            var l=d.percent?d.name+":{per|"+d.percent+"%}":d.name;
                            return l;
                        },
                        rich: {
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            }
                        }
                    }
                },
                leaves: {
                    label: {
                        normal: {
                            position: 'right',
                            verticalAlign: 'middle',
                            align: 'left'
                        }
                    }
                },
                expandAndCollapse: true,
                animationDuration: 550,
                animationDurationUpdate: 750

            }
        ]
    };
    /**
     * 设置option  数据
     */
    function setOption(obj) {
        //获取数据
        //-----
        var multi_data = dataService.getTypeData(obj)['tree'];
        if (multi_data) {
            $.each(multi_data, function (k, o) {
                option.title.text = o.title;
                option.series[0].data[0] = o.data;
                new Dtree(k, option);
            })
        }
        //
    }
    //
    function change(obj) {
        setOption(obj);
    }
    function init(obj) {
        //设置option
        setOption(obj);
    }
    exports('dataA_Dtree', {
        init: init,
        change: change
    });
});