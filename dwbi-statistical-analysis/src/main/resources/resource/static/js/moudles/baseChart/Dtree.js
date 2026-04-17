/**
 * 树形结构图 
 */
layui.define( function (exports) {
    //do something
    //
    function Dtree(divId,opt){
        this.chart=echarts.init(document.getElementById(divId));
        this.chart.setOption(opt);
    }
    Dtree.prototype.change=function(opt){
        this.chart.setOption(opt,true);
    }
    Dtree.prototype.getInstance = function(){
        return this.chart;
    }
    exports('Dtree',Dtree);
});
