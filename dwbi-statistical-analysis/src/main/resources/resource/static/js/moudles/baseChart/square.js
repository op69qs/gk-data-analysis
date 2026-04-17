/**
 * treeMap 单例()
 */
layui.define( function (exports) {
    //do something
    var chart;
    // chart.showLoading('default', {text:'统计中，请稍候...',maskColor: '#404a59',textColor:
    // '#fff',});
    function init(divId,opt){
        chart=echarts.init(document.getElementById(divId));
        chart.setOption(opt);
    }
    function change(opt){
        chart.setOption(opt,true);
    }
    function getInstance(){
        return chart;
    }
    exports('square',{
        init:init,
        change:change,
        getInstance:getInstance
    });
});