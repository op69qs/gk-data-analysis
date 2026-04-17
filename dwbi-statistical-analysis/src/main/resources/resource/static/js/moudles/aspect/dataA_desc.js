/**
 * 描述处理方法
 */
layui.define(["dataA_dataService"], function (exports) {
    var dataService = layui.dataA_dataService;
    /**
     * 设置option  数据
     */
    function setDesc(obj) {
        //获取数据
        //-----
        var multi_data = dataService.getTypeData(obj)['desc'];
        if (multi_data) {
            $.each(multi_data, function (k, o) {
                //对数字进行
                if(o){
                    $("#"+k).text(toThousands(o));
                }else{
                    $("#"+k).text("");
                }
            })
        }
        //
    }
    //数字千分位处理
    function toThousands(num) {
        if($.isNumeric(num)){
            var result = [ ], counter = 0;
            num = (num || 0).toString().split('');
            for (var i = num.length - 1; i >= 0; i--) {
                counter++;
                result.unshift(num[i]);
                if (!(counter % 3) && i != 0) { result.unshift(','); }
            }
            return result.join('');
        }else{
            return num;
        }
    }
    function change(obj) {
        setDesc(obj);
        console.log("描述重绘成功");
    }
    function init(obj) {
        //设置option
        setDesc(obj);
    }
    exports('dataA_desc', {
        init: init,
        change: change
    }); 
});