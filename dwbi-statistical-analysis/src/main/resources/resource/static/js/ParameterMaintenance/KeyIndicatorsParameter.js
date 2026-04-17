$(document).ready(function () {
    //初始化模板列表
    var params = {is_keyPoint: '0'};
    initTemplateFileList(params);
});

//可编辑模糊查询
function initTemplateFileList(params) {
    $('#viewData').datagrid({
        url: '/statisticalAnalysis/keyIndicatorsParameter/getKeyIndicatorsParameter',
        toolbar: '#toolbar',
        singleSelect: false,
        pagination: true,
        pageIndex:1,//页索引
        pageList: [10, 20, 50, 100],
        pageSize: 10,
        maxHeight: 600,
        queryParams: params,
        columns: [[
            {title: '全选', field: 'checkedAll', checkbox: true, sortable: false},  //添加checkbox
            {field: "index_name", title: "指标名称", width: 180},
            {field: "platfrom_name", title: "平台", width: 150},
            {field: "index_desc", title: "指标描述", width: 350},
            {
                field: "is_monitor", title: "是否监测器", width: 120, align: 'center',
                formatter: function (value) {
                    if (value == 0) {
                        return '否';
                    } else if (value == 1) {
                        return "是";
                    } else {
                        return "";
                    }
                }
            }
        ]]
    });
}

//查询
function queryAddData() {
    var params = {
        is_keyPoint: '0',
        indicatorkeyWord: $('#indicatorsAdd').textbox('getValue'),
        platformId: $('#platformAdd').combobox('getValue'),
        userName: localStorage.userName
    }
    initTemplateFileList(params);
}

//重置
function resetAddData() {
    $("#indicatorsAdd").textbox("setValue", "");
    $("#platformAdd").combobox("setValue", "");
    var params = {
        is_keyPoint: '0',
        userName: localStorage.userName
    }
    initTemplateFileList(params);
}