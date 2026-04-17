/**
 * 饼图类
 * --依赖 echarts 和 layui的模块写法
 * --可迁移 require.js
 */
layui.define( function (exports) {
    //do something
    function pie(divId,opt){
        this.chart=echarts.init(document.getElementById(divId));
        this.chart.setOption(opt);
    }
    pie.prototype.change=function(opt){
        this.chart.setOption(opt,true);
    }
    pie.prototype.getInstance=function(){
        return this.chart;
    }
    exports('pie',pie);
});