$(function () {
    $('#content').css('background', '#fff');
});

function definedTreeFilter(data, parent) {
    var opt = $(this).data().tree.options;
    var idFiled,
        textFiled,
        parentField;
    if (opt.parentField) {
        idFiled = opt.idFiled || 'id';
        textFiled = opt.textFiled || 'text';
        parentField = opt.parentField;

        var i,
            l,
            treeData = [],
            tmpMap = [];

        for (i = 0, l = data.length; i < l; i++) {
            tmpMap[data[i][idFiled]] = data[i];
        }

        for (i = 0, l = data.length; i < l; i++) {
            if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                if (!tmpMap[data[i][parentField]]['children'])
                    tmpMap[data[i][parentField]]['children'] = [];
                data[i]['text'] = data[i][textFiled];
                tmpMap[data[i][parentField]]['children'].push(data[i]);
                if (tmpMap[data[i][parentField]]['children']) {
                    tmpMap[data[i][parentField]].checked = false;
                }
            } else {
                data[i]['text'] = data[i][textFiled];
                treeData.push(data[i]);
            }
        }
        return treeData;
    }
    return data;
}

function ajaxLoading() {
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height()
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2
    });
}

function ajaxLoading1() {
    $("<div class=\"datagrid-mask\"></div>").css({
        display: "block",
        width: "100%",
        height: $(window).height(),
        zIndex: 9003,
        opacity: 0
    }).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在压缩，请稍候。。。").appendTo("body").css({
        display: "block",
        left: ($(document.body).outerWidth(true) - 190) / 2,
        top: ($(window).height() - 45) / 2,
        zIndex: 9003
    });
}

function ajaxLoadEnd() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
}

/**
 * input 框校验
 */
$.extend($.fn.validatebox.defaults.rules, {
    //验证大小写英文
    UALL: {
        validator: function (value) {
            return /^[a-zA-Z]*$/.test(value);
        },
        message: '请输入输入英文字母'
    },//数字
    Number: {
        validator: function (value) {
            var reg = /^[0-9]*$/;
            return reg.test(value);
        },
        message: '请输入数字'
    }
});

/**
 * 四舍五入保留两位小数
 * @param x
 * @returns {string|boolean}
 */
function toDecimal(x) {
    var num = Math.round(x * 100) / 100;//此处x在做乘法是会进行隐士类型转换数值型，如果转换失败最终num=NaN
    if (isNaN(num)) {
        return false;
    }
    var str = num.toString();
    var rs = str.indexOf('.');
    if (rs < 0) {
        str += '.';
    }
    while (str.length <= 3) {
        str += '0';
    }
    return str;
}

/**
 * datagrid合并方法
 */
function mergeMethod(ID, index, field, rowspan) {
    field.forEach(function (r) {
        $(ID).datagrid('mergeCells', {
            index: index,
            field: r,
            rowspan: rowspan
        });
    })
}

/**
 * 数组去重
 * @param array
 * @returns {Array}
 */
function uniq(array){
    var temp = []; //一个新的临时数组
    for(var i = 0; i < array.length; i++){
        if(temp.indexOf(array[i]) == -1){
            temp.push(array[i]);
        }
    }
    return temp;
}