//动态切换时间插件、下拉选
function changeTimeElement(value) {
    //情况时间控件值
    $("#startTime2,#endTime2").val("");
    $("#startTime1,#endTime1").combobox('setValue', '');
    if (value == '1' || value == '2') { //半小时、一小时
        $("#startTime1,#endTime1").parent().removeAttr("style");
        $("#startTime2,#endTime2").parent().attr("style", "display:none");
        getservice('/statisticalAnalysis/forecastAnalysis/getTimePoints', {"period": value}).done(function (res) {
            $("#startTime1,#endTime1").combobox({
                valueField: 'value',
                textField: 'label',
                data: res.data
            });
        });
    } else if ("3,4,5".indexOf(value) != -1) { //日、月、年
        $("#startTime2,#endTime2").parent().removeAttr("style");
        $("#startTime1,#endTime1").parent().attr("style", "display:none");
        var dateFormatter = ("3" == value) ? "yyyy-MM-dd" : (("4" == value) ? "yyyy-MM" : "yyyy");
        var maxDateFormatter = ("3" == value) ? "%y-%M-%d" : (("4" == value) ? "%y-%M" : "%y");
        $("#startTime2,#endTime2").attr("onfocus", "WdatePicker({readOnly : false,dateFmt : '" + dateFormatter + "',isShowWeek : true,isShowClear: false, maxDate : '" + maxDateFormatter + "'})");
    } else { //无值
        $("#startTime1,#endTime1").parent().removeAttr("style");
        $("#startTime2,#endTime2").parent().attr("style", "display:none");
        $("#startTime1,#endTime1").combobox({
            valueField: 'value',
            textField: 'label',
            data: []
        });
    }
}

//初始化/重置echarts图数据
function echart_data(id, param) {
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(param, true);
    window.addEventListener("resize", function () {
        myChart.resize();
    });
}

//选择构成要素重新渲染[建立连接情况构成分析]图表
function resetCompositionEcharts(value) {
    var periodVal = $("#period").combobox("getValue"); //周期
    var params = {
        compositionId: value,
        top: 3,
        period: periodVal,
        startTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#startTime1").combobox("getValue") : $("#startTime2").val(),
        endTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#endTime1").combobox("getValue") : $("#endTime2").val()
    };
    //校验：如果无平台、周期、起始时间或者结束时间参数，则不进行查询
    if (value.length == 0 || periodVal.length == 0 || params.startTime.length == 0 && params.endTime.length == 0) {
        // 销毁实例,清空画布
        echarts.init(document.getElementById("echarts3")).dispose();
        return;
    }
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getConnectionCompositionAnalysis', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            //top1链路的重置率、无响应率
            series.push({
                name: res.oneName1,
                type: 'bar',
                stack: res.oneName1,
                barWidth: 15,
                data: res.oneRestList
            });
            series.push({
                name: res.oneName2,
                type: 'bar',
                stack: res.oneName1,
                barWidth: 15,
                data: res.oneNoResponseList
            });

            //top2链路的重置率、无响应率
            series.push({
                name: res.twoName1,
                type: 'bar',
                stack: res.twoName1,
                barWidth: 15,
                data: res.twoRestList
            });
            series.push({
                name: res.twoName2,
                type: 'bar',
                stack: res.twoName1,
                barWidth: 15,
                data: res.twoNoResponseList
            });

            //top3链路的重置率、无响应率
            series.push({
                name: res.threeName1,
                type: 'bar',
                stack: res.threeName1,
                barWidth: 15,
                data: res.threeRestList
            });
            series.push({
                name: res.threeName2,
                type: 'bar',
                stack: res.threeName1,
                barWidth: 15,
                data: res.threeNoResponseList
            });
            alarmOption3.legend.data = res.legend;
            alarmOption3.xAxis[0].data = res.xAxis;
            alarmOption3.series = series;
            echart_data('echarts3', alarmOption3);
        }
    });
}

//调用后端接口方法
function getservice(url, param) {
    return $.ajax({
        type: 'POST',
        url: url,
        dataType: "json",
        headers: {Authorization: localStorage.token},
        data: param || {}
    });
}

//获取接口返回数据
function getServiceData(params) {
    //数据包发送接收情况分析
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getDataPacketSendAndReceiveAnalysis', params).done(function (res) {
        if (res) {
            alarmOption1.xAxis[0].data = res.xAxis;
            alarmOption1.series[0].data = res.sendPacketData;
            alarmOption1.series[1].data = res.receivePacketrData;
            echart_data('echarts1', alarmOption1);
        }
    });

    //网络(DNS)性能警报分析
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getNetworkPerformanceAnalysis', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            for (var i = 0; i < res.series.length; i++) {
                series.push({
                    name: res.series[i].name,
                    type: 'line',
                    lineStyle: {width: 3},
                    data: res.series[i].value,
                    smooth: true
                });
            }
            alarmOption2.legend.data = res.legend;
            alarmOption2.xAxis.data = res.xAxis;
            alarmOption2.series = series;
            echart_data('echarts2', alarmOption2);
        }
    });

    //建立连接情况构成分析
    resetCompositionEcharts($("#composition-analysis").combobox("getValue"));

    //异常行为警报分析
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getAbnormalBehaviorAnalysis', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            for (var i = 0; i < res.series.length; i++) {
                series.push({
                    name: res.series[i].name,
                    type: 'line',
                    lineStyle: {width: 3},
                    data: res.series[i].value,
                    smooth: true
                });
            }
            alarmOption4.legend.data = res.legend;
            alarmOption4.xAxis.data = res.xAxis;
            alarmOption4.series = series;
            echart_data('echarts4', alarmOption4);
        }
    });
}

//网络-数据包发送接收情况分析
var alarmOption1 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    backgroundColor: '#fff',
    legend: {
        data: [
            '发送数据包数',
            '接收数据包数'
        ],
        left: '3%'
    },
    color: [
        "#3fb1e3",
        "#61E2BB"
    ],
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            data: []
        }
    ],
    yAxis: [
        {
            name: 'B',
            type: 'value'
        }
    ],
    series: [
        {
            name: '发送数据包数',
            type: 'bar',
            barWidth: 20,
            data: [],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        },
        {
            name: '接收数据包数',
            type: 'bar',
            barWidth: 20,
            data: [],
            markPoint: {
                data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                ]
            },
            markLine: {
                data: [
                    {type: 'average', name: '平均值'}
                ]
            }
        }
    ]
};

//网络-网络(DNS)性能警报分析
var alarmOption2 = {
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            var str = '';
            params.forEach(function (param) {
                str += param.marker + param.seriesName + "：" + param.value + '<br/>';
            });
            return str;
        }
    },
    backgroundColor: '#fff',
    legend: {
        data: ["wqwqwd"],
        top: '2%'
    },
    xAxis: {
        type: 'category',
        data: []
    },
    yAxis: {
        name: '次数',
        type: 'value'
    },
    color: '#969EE3',
    series: []
};

//网络-建立连接情况构成分析
var alarmOption3 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    backgroundColor: '#fff',
    legend: {
        data: [
            '重置率',
            '无响应率'
        ],
        top: '2%'
    },
    color: [
        "#FBD437",
        "#FF99CC",
        "#61E2BB",
        "#ffccff",
        "#83EECE",
        "#FDEA9B"
    ],
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: [
        {
            type: 'category',
            data: []
        }
    ],
    yAxis: [
        {
            name: '%',
            type: 'value'
        }
    ],
    series: []
};

//网络-异常行为警报分析
var alarmOption4 = {
    tooltip: {
        trigger: 'axis',
        formatter: function (params) {
            var str = '';
            params.forEach(function (param) {
                str += param.marker + param.seriesName + "：" + param.value + '<br/>';
            });
            return str;
        }
    },
    backgroundColor: '#fff',
    legend: {
        data: [],
        top: '2%'
    },
    xAxis: {
        type: 'category',
        data: []
    },
    yAxis: {
        name: '次数',
        type: 'value'
    },
    color: '#66ccff',
    series: []
};