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

//获取监测器平台下拉选
function getPlatformMonitor() {
    getservice('./statisticalAnalysis/monitorAlarm/getPlatformMonitor', {system_code: 'ucloud',is_monitor: '1'}).done(function (res) {
        $("#platform1,#platform2").combobox({
            valueField: 'value',
            textField: 'label',
            data: res.data,
            onLoadSuccess: function () { //设置默认值
                $('#platform1,#platform2').combobox('setValue', res.data[0].value);
            }
        });
    });
}

//选择平台监测器重新渲染[分平台监测器告警分析]图表
function resetPlatformEcharts(value) {
    var periodVal = $("#period").combobox("getValue"); //周期
    var params = {
        platform: value,
        period: periodVal,
        startTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#startTime1").combobox("getValue") : $("#startTime2").val(),
        endTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#endTime1").combobox("getValue") : $("#endTime2").val()
    }
    //校验：如果无平台、周期、起始时间或者结束时间参数，则不进行查询
    if (value.length == 0 || periodVal.length == 0 || params.startTime.length == 0 && params.endTime.length == 0) {
        // 销毁实例,清空画布
        echarts.init(document.getElementById("echarts3")).dispose();
        return;
    }
    getservice('./statisticalAnalysis/monitorAlarm/getPlatformAlarmAnalysisData', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            for (var i = 0; i < res.series.length; i++) {
                series.push({
                    name: res.series[i].name,
                    type: 'line',
                    lineStyle: {width: 3},
                    data: res.series[i].value
                });
            }
            alarmOption3.legend.data = res.legend;
            alarmOption3.xAxis[0].data = res.xAxis;
            alarmOption3.series = series;
            echart_data('echarts3', alarmOption3);
        }
    });
}

//选择平台监测器重新渲染[分平台分设备监测器告警分析]图表
function resetResourceEcharts(value) {
    var periodVal = $("#period").combobox("getValue"); //周期
    var params = {
        platform: value,
        period: periodVal,
        startTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#startTime1").combobox("getValue") : $("#startTime2").val(),
        endTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#endTime1").combobox("getValue") : $("#endTime2").val()
    }
    //校验：如果无平台、周期、起始时间或者结束时间参数，则不进行查询
    if (value.length == 0 || periodVal.length == 0 || params.startTime.length == 0 && params.endTime.length == 0) {
        // 销毁实例,清空画布
        echarts.init(document.getElementById("echarts4")).dispose();
        return;
    }
    getservice('./statisticalAnalysis/monitorAlarm/getDeviceAlarmAnalysisData', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            for (var i = 0; i < res.series.length; i++) {
                series.push({
                    name: res.series[i].name,
                    type: 'bar',
                    barWidth: 20,
                    data: res.series[i].value
                });
            }
            alarmOption4.legend.data = res.legend;
            alarmOption4.xAxis[0].data = res.xAxis;
            alarmOption4.series = series;
            echart_data('echarts4', alarmOption4);
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
    //监测器告警构成分析
    getservice('./statisticalAnalysis/monitorAlarm/getAlarmCompositionAnalysisData', params).done(function (res) {
        if (res) {
            alarmOption1.series[0].data = res.innerData;
            alarmOption1.series[1].data = res.outerData;
            echart_data('echarts1', alarmOption1);
        }
    });

    //监测器告警序时分析
    getservice('./statisticalAnalysis/monitorAlarm/getAlarmChronologyAnalysisData', params).done(function (res) {
        if (res) {
            var series = []; //定义空数组,存放展示数据
            for (var i = 0; i < res.series.length; i++) {
                series.push({
                    name: res.series[i].name,
                    type: 'bar',
                    barWidth: 20,
                    stack: res.series[0].name,
                    data: res.series[i].value
                });
            }
            alarmOption2.legend.data = res.legend;
            alarmOption2.xAxis[0].data = res.xAxis;
            alarmOption2.series = series;
            echart_data('echarts2', alarmOption2);
        }
    });

    //分平台监测器告警分析
    resetPlatformEcharts($("#platform1").combobox("getValue"));

    //分平台分设备监测器告警分析
    resetResourceEcharts($("#platform2").combobox("getValue"));
}

//运维-监测告警构成
var alarmOption1 = {
    tooltip: {
        trigger: 'item',
        formatter: function (param) {
            return param.marker + param.name + "：" + param.value + " (" + param.percent + "%)";
        }
    },
    backgroundColor: '#fff',
    series: [
        {
            type: 'pie',
            selectedMode: 'single',
            radius: [0, '33%'],
            "color": [
                "#969EE3",
                "#3fb1e3",
                "#61E2BB",
                "#42C8C8",
                "#FDEA9B",
                "#ccccff"
            ],

            label: {
                position: 'inner'
            },
            labelLine: {
                show: false
            },
            data: []
        },
        {
            type: 'pie',
            radius: ['40%', '55%'],
            "color": [
                "#B6BBE6",
                "#6EC6EC",
                "#6EC6EC",
                "#83EECE",
                "#42C8C8",
                "#42C8C8",
                "#42C8C8",
                "#FDEA9B",
                "#FDEA9B",
                "#ccccff",
                "#ccccff"
            ],
            label: {},
            data: []
        }
    ]
};

//运维-告警序时分析
var alarmOption2 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        },
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
        top: '2%',
        data: []
    },
    color: [
        "#969EE3",
        "#3fb1e3",
        "#61E2BB",
        "#42C8C8",
        "#66ccff",
        "#FDEA9B",
        "#ccccff",
        "#FF99CC",
        "#83EECE",
        "#FBD437",
        "#ffccff",
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
            type: 'value'
        }
    ],
    series: []
};

//分平台监测器告警分析
var alarmOption3 = {
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
        top: '2%',
        data: []
    },
    color: [
        "#969EE3",
        "#3fb1e3",
        "#61E2BB",
        "#42C8C8",
        "#66ccff",
        "#FDEA9B",
        "#ccccff",
        "#FF99CC",
        "#83EECE",
        "#FBD437",
        "#ffccff",
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
            type: 'value'
        }
    ],
    series: []
};

//分平台分设备监测器告警分析
var alarmOption4 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    backgroundColor: '#fff',
    legend: {
        top: '2%',
        data: []
    },
    color: [
        "#969EE3",
        "#3fb1e3",
        "#61E2BB",
        "#42C8C8",
        "#66ccff",
        "#FDEA9B",
        "#ccccff",
        "#FF99CC",
        "#83EECE",
        "#FBD437",
        "#ffccff"
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
            type: 'value'
        }
    ],
    series: []
};