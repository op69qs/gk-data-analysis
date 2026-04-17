/**
 * 条形图 单例
 */
layui.define( function (exports) {
    //do something
    //
    function bar(divId,opt){
        this.chart=echarts.init(document.getElementById(divId));
        this.chart.setOption(opt);
    }
    bar.prototype.change=function(opt){
        this.chart.setOption(opt,true);
    }
    bar.prototype.getInstance = function(){
        return this.chart;
    }
    exports('bar',bar);
});
