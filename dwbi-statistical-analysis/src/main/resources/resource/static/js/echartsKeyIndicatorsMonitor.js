//动态切换时间插件、下拉选
function changeTimeElement(value) {
    //情况时间控件值
    $("#startTime2,#endTime2").val("");
    $("#startTime1,#endTime1").combobox('setValue', '');
    if (value == '1' || value == '2') { //半小时、一小时
        $("#startTime1,#endTime1").parent().removeAttr("style");
        $("#startTime2,#endTime2").parent().attr("style", "display:none");
        getservice('/statisticalAnalysis/forecastAnalysis/getTimePoints', {
            "period": value,
            "sampleFlag": "02"
        }).done(function (res) {
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

//动态切换重点指标下拉选
function changePlatformElement(value) {
    if (value != '') {
        getservice('./statisticalAnalysis/keyIndicatorsMonitor/getKeyIndicators', {type: "devOps", platform: value }).done(function (res) {
            $("#indicators").combobox({
                valueField: 'value',
                textField: 'label',
                data: res
            });
        });
    } else { //无值
        $("#indicators").combobox({
            valueField: 'value',
            textField: 'label',
            data: []
        });
    }
}

//获取重点状态指标echars
function getKeyStateIndicators() {
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getKeyIndicators', { type: "devOps", indexType: '2'}).done(function (res) {
        $("#keyStateIndicators").combobox({
            valueField: 'value',
            textField: 'label',
            data: res,
            onChange: function (current, old) {
                if (current != old) {
                    resetKeyStateEcharts(current);
                }
            }
        });
    });
}

//选择重点指标状态重新渲染[重点状态指标分析]图表
function resetKeyStateEcharts(value) {
    var periodVal = $("#period").combobox("getValue"); //周期
    var params = {
        keyState: value,
        period: periodVal,
        platform: $("#platform").combobox("getValue"),
        indicatorsId: $("#keyStateIndicators").combobox("getValue"),
        indicatorsName: $("#keyStateIndicators").combobox("getText"),
        startTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#startTime1").combobox("getValue") : $("#startTime2").val(),
        endTime: ("3,4,5".indexOf(periodVal) == -1) ? $("#endTime1").combobox("getValue") : $("#endTime2").val()
    };
    //校验：如果无状态、周期、起始时间或者结束时间参数，则不进行查询
    if (value.length == 0 || periodVal.length == 0 || params.indicatorsId.length == 0 || params.startTime.length == 0 && params.endTime.length == 0) {
        // 销毁实例,清空画布
        echarts.init(document.getElementById("echarts2")).dispose();
        return;
    }
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getkeyIndicatorsStatusAnalysis', params).done(function (res) {
        if (res) {
            indicatorOption2.legend.data = res.legend;
            indicatorOption2.series[0].data = res.series;
            echart_data('echarts2', indicatorOption2);
        }
    });
}

//初始化/重置echarts图数据
function echart_data(id, param) {
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(param, true);
    window.addEventListener("resize", function () {
        myChart.resize();
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
    //重点指标峰值资源序时分析
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getkeyIndicatorsPeakValueAnalysis', params).done(function (res) {
        if (res) {
            var series = []; //新建数组
            series.push({
                name: params.indicatorsName,
                type: 'bar',
                barWidth: 30,
                data: res.series
            });
            indicatorOption1.legend.data = params.indicatorsName.split(",");
            indicatorOption1.xAxis[0].data = res.xAxis;
            indicatorOption1.xAxis[0].axisLabel.rotate = res.xAxis.length > 5 ? 45 : 0;
            indicatorOption1.series = series;
            echart_data('echarts1', indicatorOption1);
        }
    });

    //重点状态指标分析
    resetKeyStateEcharts($("#keyStateIndicators").combobox("getValue"));

    //重点指标资源排名TOP15分析
    getservice('./statisticalAnalysis/keyIndicatorsMonitor/getkeyIndicatorsResourceRankAnalysis', params).done(function (res) {
        if (res) {
            var series = [];
            // indicatorOption3.legend.data = params.indicatorsName.split(",");
            indicatorOption3.yAxis.data = res.yAxis;
            indicatorOption3.series[0].name = params.indicatorsName;
            indicatorOption3.series[0].data = res.series;
            echart_data('echarts3', indicatorOption3);
        }
    });
}

//重点指标峰值资源序时分析
var indicatorOption1 = {
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            type: 'shadow'
        }
    },
    backgroundColor: '#fff',
    legend: {
        data: [],
        top: '2%'
    },
    color: [
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
            data: [],
            axisLabel: {
                interval:0,
                rotate: 0
            }
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

//重点状态指标分析
var indicatorOption2 = {
    tooltip: {
        trigger: 'item',
        formatter: function (param) {
            return param.marker + param.name + "(数量)：" + param.value + " (" + param.percent + "%)";
        }
    },
    backgroundColor: '#fff',
    legend: {
        orient: 'vertical',
        top: '20%',
        left: '10%',
        data: []
    },
    color: [
        "#969EE3",
        "#3fb1e3",
        "#61E2BB",
        "#42C8C8",
        "#FDEA9B",
        "#ccccff",
        "#FF99CC",
        "#83EECE",
        "#FBD437",
        "#ffccff"
    ],
    series: [
        {
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '30',
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: []
        }
    ]
};

//重点指标资源排名TOP15分析
var indicatorOption3 = {
    backgroundColor: '#fff',
    color: '#969EE3',
    legend: {
        data: [],
        top: '3%'
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis: {
        name: '%',
        type: 'value',
        position: 'top'
    },
    yAxis: {
        type: 'category',
        data: []
    },
    series: [
        {
            name: '',
            type: 'bar',
            barMaxWidth: 20,
            barMinWidth: 15,
            label: {
                show: true,
                position: 'right'
            },
            data: []
        }
    ]
};





