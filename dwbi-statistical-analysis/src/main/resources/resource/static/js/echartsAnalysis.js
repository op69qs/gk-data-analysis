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

//初始化/重置echarts图数据
function echart_data(id, param) {
    var myChart = echarts.init(document.getElementById(id));
    myChart.setOption(param, true);
    window.addEventListener("resize", function () {
        myChart.resize();
    });
}

//获取接口返回数据
function getServiceData(id, params) {
    //样本预测分析
    getservice('./statisticalAnalysis/forecastAnalysis/getForecastEChartsData', params).done(function (res) {
        if (res) {
            dataList = res.data;
            if (sampleFlag == '02') { //样本外预测
                analysisOption1.xAxis.data = res.date;
                analysisOption1.series[0].data = res.forecastData;
                echart_data(echartsId, analysisOption1);
            } else { //样本内预测
                analysisOption2.xAxis.data = res.date;
                analysisOption2.series[0].data = res.forecastData;
                analysisOption2.series[1].data = res.actualData;
                analysisOption2.series[2].data = res.erroData;
                echart_data(echartsId, analysisOption2);
            }
            if (res.code == 500) { //分析数据不足报错
                layer.msg(res.errorInfo);
            }
        }
    })
}

//样本外预测
var analysisOption1 = {
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
        data: ['预测值'],
        top: '2%'
    },
    grid: {
        containLabel: true
    },
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
            name: '预测值',
            type: 'line',
            data: []
        }
    ]
};

//样本内预测
var analysisOption2 = {
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
        data: ['预测值', '实际值', '误差值'],
        top: '2%'
    },
    grid: {
        containLabel: true
    },
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
            name: '预测值',
            type: 'line',
            data: []
        },
        {
            name: '实际值',
            type: 'line',
            data: []
        },
        {
            name: '误差值',
            type: 'line',
            data: []
        }
    ]
};
