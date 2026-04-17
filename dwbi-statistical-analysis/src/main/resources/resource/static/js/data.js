var ctx = 'http://' + window.location.host;
$(document).ready(function () {
    initCycle();
    initType();
});

/**
 * 上报周期
 */
function initCycle() {
    $('#cycleId').combobox({
        url: '../../../../guoku/gk/getCycle',
        required: false,
        method: 'get',
        queryParams: {
            Authorization: localStorage.token
        },
        width: 80,
        editable: false,
        valueField: 'id',
        textField: 'text',
        onSelect: function (rec) {
            // 初始化日期
            initWdatePicker(rec.id);
        },
        onLoadSuccess: function (data) {
            $('#cycleId').combobox('setValue', data[0].id);
        }
    });
}

/**
 * 日期
 * @param bizTypeId
 * @param areaId
 * @param orgId
 * @param cycleId
 */
function initWdatePicker(cycleId) {
    var fiscalTerm = '';
    if ('01' == cycleId) {
        // 日
        $("#fiscalTermDay").css("display", "inline");
        $("#fiscalTermDay1").css("display", "inline");
        $("#fiscalTermDecad").css("display", "none");
        $("#fiscalTermDecad1").css("display", "none");
        $("#fiscalTermMon").css("display", "none");
        $("#fiscalTermMon1").css("display", "none");
        $("#fiscalTermQuarter").css("display", "none");
        $("#fiscalTermQuarter1").css("display", "none");
        $("#fiscalTermYear").css("display", "none");
        $("#fiscalTermYear1").css("display", "none");
        // 设置默认日期
        $('#fiscalTermDay').val(today());
        $('#fiscalTermDay1').val(today());
        fiscalTerm = today();
    } else if ('05' == cycleId) {
        // 月
        $("#fiscalTermDay").css("display", "none");
        $("#fiscalTermDay1").css("display", "none");
        $("#fiscalTermDecad").css("display", "none");
        $("#fiscalTermDecad1").css("display", "none");
        $("#fiscalTermMon").css("display", "inline");
        $("#fiscalTermMon1").css("display", "inline");
        $("#fiscalTermQuarter").css("display", "none");
        $("#fiscalTermQuarter1").css("display", "none");
        $("#fiscalTermYear").css("display", "none");
        $("#fiscalTermYear1").css("display", "none");
        // 设置默认日期
        $('#fiscalTermMon').val(toMon());
        $('#fiscalTermMon1').val(toMon());
    } else if ('08' == cycleId) {
        // 年
        $("#fiscalTermDay").css("display", "none");
        $("#fiscalTermDay1").css("display", "none");
        $("#fiscalTermDecad").css("display", "none");
        $("#fiscalTermDecad1").css("display", "none");
        $("#fiscalTermMon").css("display", "none");
        $("#fiscalTermMon1").css("display", "none");
        $("#fiscalTermQuarter").css("display", "none");
        $("#fiscalTermQuarter1").css("display", "none");
        $("#fiscalTermYear").css("display", "inline");
        $("#fiscalTermYear1").css("display", "inline");
        // 设置默认日期
        $('#fiscalTermYear').val(toYear());
        $('#fiscalTermYear1').val(toYear());
    }
    if ('' == fiscalTerm) {
        $('#dataList').datagrid('loadData', {total: 0, rows: []});
    } else {
        // 初始化列表
        //  initDataReport(bizTypeId,areaId,orgId,cycleId,fiscalTerm);
    }
}

/**
 * 设置默认日期是今天
 * @returns {string}
 */
function today() {
    var today = new Date();
    var h = today.getFullYear();
    var m = today.getMonth() + 1;
    var d = today.getDate();
    m = m < 10 ? "0" + m : m;   //  这里判断月份是否<10,如果是在月份前面加'0'
    d = d < 10 ? "0" + d : d;        //  这里判断日期是否<10,如果是在日期前面加'0'
    return h + "-" + m + "-" + d;
}

/**
 * 设置默认日期是本月
 * @returns {string}
 */
function toMon() {
    var today = new Date();
    var h = today.getFullYear();
    var m = today.getMonth() + 1;
    m = m < 10 ? "0" + m : m;   //  这里判断月份是否<10,如果是在月份前面加'0'
    return h + "-" + m;
}

/**
 * 设置默认日期是今年
 * @returns {string}
 */
function toYear() {
    var today = new Date();
    var h = today.getFullYear();
    return h;
}

/**
 * 日期-天
 */
function openWdatePickerDay() {
    WdatePicker({
        readOnly: false,
        dateFmt: 'yyyy-MM-dd',
        isShowWeek: true,
        isShowClear: false
    });
}

/**
 * 日期-月
 */
function openWdatePickerMon() {
    WdatePicker({
        readOnly: false,
        dateFmt: 'yyyy-MM',
        isShowWeek: true,
        isShowClear: false,
        maxDate: '%y-%M-%d'
    });
}

/**
 * 日期-季
 */
function openWdatePickerQuarter() {
    WdatePicker({
        readOnly: false,
        dateFmt: 'yyyy-MM',
        isQuarter: true,
        disabledDates: ['20[0-1][0-9]-0[1-2]', '20[0-1][0-9]-0[4-5]', '20[0-1][0-9]-0[7-8]', '20[0-1][0-9]-1[0-1]'],
        isShowWeek: true,
        isShowClear: false,
        maxDate: '%y-%M-%d'
    });
}

/**
 * 日期-年
 */
function openWdatePickerYear() {
    WdatePicker({
        readOnly: false,
        dateFmt: 'yyyy',
        isShowWeek: true,
        isShowClear: false,
        maxDate: '%y-%M-%d'
    });
}

function initType() {
    $('#type_1').combobox({
        url: '../../../../guoku/gk/getTpye',
        queryParams: {
            Authorization: localStorage.token
        },
        width: 123,
        editable: false,
        method: 'get',
        valueField: 'id',
        textField: 'TEXT',
        onLoadSuccess: function (data) {
            $('#type_1').combobox('setValue', data[0].id);
        }
    });
}

/**
 * 封装数据，将list结果集封装为树形结果集
 * @param data
 * @param parent
 * @returns {*}
 */
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