/**
 * 地图 类
 */
layui.define(function (exports) {
    //do something
    function map(divId, opt) {
        this.chart = echarts.init(document.getElementById(divId));
        this.chart.setOption(opt);
        this.observers = [];
    }
    map.prototype.change = function (opt) {
        this.chart.setOption(opt, true);
    }
    map.prototype.getInstance = function () {
        return this.chart;
    }
    //注册观察者
    map.prototype.registerObserver = function (obse) {
        if ($.isArray(obse)) {
            this.observers = this.observers.concat(obse);
        } else {
            this.observers.push(obse)
        }
    }
    //触发观察者
    map.prototype.notifyObservers = function (op) {
        for (var i = 0; i < this.observers.length; i++) {
            this.observers[i].change(op);
        }
    }
    exports('map', map);
});
