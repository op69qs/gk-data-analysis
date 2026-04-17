/**
 * Created by samer on 2017/12/12.
 */

/**
 * 一个页签多表本查询页导出
 * @param gridid
 * @param txtNameDsrc
 * @constructor
 */
function ExproterAll(gridid, txtNameDsrc, dscrArr, textArr, colspanNo) {
    // appendLoadingGif();
    $.messager.progress({"text": "正在导出请稍后..."});
    //  meta charset='utf-8' 用来设置导出文本格式
    var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
    var conditionTable = setTableContent(dscrArr, textArr, colspanNo);
    htmlTemp += conditionTable;
    for (var i = 0; i < gridid.length; i++) {
        var contentTable = ChangeToTable(gridid[i]);
        if (txtNameDsrc[i] == "附表：库存影响分析") {
            var contentTableName =
                "    <div style=\"margin: 20px auto 20px auto;\">\n" +
                "        <p style=\"text-align:left;font-size: 20px;\">附表：库存影响分析</p>\n" +
                "    </div>";
        } else {
            var contentTableName = "<div style='font: 24px; text-align:center'>" + txtNameDsrc[i] + "</div>";
        }
        htmlTemp += contentTableName;
        if (i == 0) {
            if (document.getElementById("tableHeaderTable")) {
                var tableHeaderTable = document.getElementById("tableHeaderTable").outerHTML;
                htmlTemp += tableHeaderTable;
            }
        }
        htmlTemp += contentTable;
        if (i == (gridid.length - 1)) {
            if (document.getElementById("tableFooterTable")) {
                var tableFooterTable = document.getElementById("tableFooterTable").outerHTML;
                htmlTemp += tableFooterTable;
            }
        }
    }
    htmlTemp += "</body></html>";
    var blob = new Blob(
        [htmlTemp],
        {
            type: "application/vnd.ms-excel"
        }
    );
    saveAs(blob, txtNameDsrc[1] + ".xls");
    // removeLoadingGif();
    $.messager.progress('close');
}

/**
 * UTF-8全表导出
 * @param gridid      数据表格ID
 * @param txtNameDsrc 表名中文描述
 * @param url         请求地址
 * @param params      查询参数，包涵页面中所有条件参数。
 * @param col         表格表头需要取出的列数，从后向前依次去除。
 * @param dscrArr     导出条件的中文描述
 * @param textArr     导出条件的当前值
 * @param colspanNo   导出条件需要跨列个数
 * @param formatCols  导出内容中需要进行数字格式化的field
 * @param colOrder    导出列的对齐规则
 */
function exporter_UTF8(gridid, txtNameDsrc, url, params, col, dscrArr, textArr, colspanNo, formatCols, colOrder) {
    // appendLoadingGif();
    $.messager.progress({"text": "正在导出请稍后..."});
    //  meta charset='utf-8' 用来设置导出文本格式
    var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
    var conditionTable = setTableContent(dscrArr, textArr, colspanNo);
    htmlTemp += conditionTable;
    var contentTable = ChangeToTable1(gridid, url, params, col, formatCols, colOrder);
    var contentTableName = "<div style='font-size: 24px; text-align:center'>" + txtNameDsrc + "</div>";
    htmlTemp += contentTableName;
    if (gridid === 'queryDataSub') {
        if (document.getElementById("drillDown")) {
            var drillDownTable = document.getElementById("drillDown").outerHTML;
            htmlTemp += drillDownTable;
        }
    } else {
        if (document.getElementById("tableHeaderTable")) {
            var tableHeaderTable = document.getElementById("tableHeaderTable").outerHTML;
            htmlTemp += tableHeaderTable;
        }
    }
    htmlTemp += contentTable;
    if (gridid !== 'queryDataSub') {
        if (document.getElementById("tableFooterTable")) {
            var tableFooterTable = document.getElementById("tableFooterTable").outerHTML;
            htmlTemp += tableFooterTable;
        }
    }
    htmlTemp += "</body></html>";
    var blob = new Blob(
        [htmlTemp],
        {
            type: "application/vnd.ms-excel"
        }
    );
    saveAs(blob, txtNameDsrc + ".xls");
    $.messager.progress('close');
    // removeLoadingGif();
}
function exportera_UTF8(gridid, txtNameDsrc, url, params, col, dscrArr, textArr, colspanNo, formatCols, colOrder) {
    // appendLoadingGif();
    $.messager.progress({"text": "正在导出请稍后..."});
    //  meta charset='utf-8' 用来设置导出文本格式
    var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
    var conditionTable = setTableContent(dscrArr, textArr, colspanNo);
    htmlTemp += conditionTable;
    var contentTable = ChangeToTable1(gridid, url, params, col, formatCols, colOrder);
    // var contentTableName = "<div style='font-size: 24px; text-align:center'>" + txtNameDsrc + "</div>";
    // htmlTemp += contentTableName;
    if (gridid === 'queryDataSub') {
        if (document.getElementById("drillDown")) {
            var drillDownTable = document.getElementById("drillDown").outerHTML;
            htmlTemp += drillDownTable;
        }
    } else {
        if (document.getElementById("tableHeaderTable")) {
            var tableHeaderTable = document.getElementById("tableHeaderTable").outerHTML;
            htmlTemp += tableHeaderTable;
        }
    }
    htmlTemp += contentTable;
    if (gridid !== 'queryDataSub') {
        if (document.getElementById("tableFooterTable")) {
            var tableFooterTable = document.getElementById("tableFooterTable").outerHTML;
            htmlTemp += tableFooterTable;
        }
    }
    htmlTemp += "</body></html>";
    var blob = new Blob(
        [htmlTemp],
        {
            type: "application/vnd.ms-excel"
        }
    );
    saveAs(blob, txtNameDsrc + ".xls");
    $.messager.progress('close');
    // removeLoadingGif();
}
/**
 * UTF-8当前页导出
 * @param gridid
 * @param txtNameDsrc
 * @param url
 * @param params 查询参数，包涵页面中所有条件参数。
 * @param col 表格表头需要取出的列数，从后向前依次去除。
 */
function currentPageExporter_UTF8(gridid, txtNameDsrc, dscrArr, textArr, colspanNo) {
    //  meta charset='utf-8' 用来设置导出文本格式
    var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
    var conditionTable = setTableContent(dscrArr, textArr, colspanNo);
    htmlTemp += conditionTable;
    var contentTable = ChangeToTable(gridid);
    var contentTableName = "<div style='font-size:24px;text-align:center'>" + txtNameDsrc + "</div>";
    htmlTemp += contentTableName;
    var tableHeaderTable = document.getElementById("tableHeaderTable").outerHTML;
    htmlTemp += tableHeaderTable;
    htmlTemp += contentTable;
    var tableFooterTable = document.getElementById("tableFooterTable").outerHTML;
    htmlTemp += tableFooterTable;
    if (document.getElementById("drillDown")) {
        var drillDownTable = document.getElementById("drillDown").outerHTML;
        htmlTemp += drillDownTable;
    }
    htmlTemp += "</body></html>";
    var blob = new Blob(
        [htmlTemp],
        {
            type: "application/vnd.ms-excel"
        }
    );
    saveAs(blob, txtNameDsrc + ".xls");
}
/**
 * UTF-8当前页导出无表头
 * @param gridid 数据表格ID
 * @param txtNameDsrc 表名中文描述
 * @param url
 * @param params
 * @param col
 */
// function currentPageExportera_UTF8(gridid, txtNameDsrc, dscrArr, textArr, colspanNo) {
//     //  meta charset='utf-8' 用来设置导出文本格式
//     var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
//     // var conditionTable = setTableContent(dscrArr, textArr, colspanNo);
//     // htmlTemp += conditionTable;
//     var contentTable = ChangeToTable(gridid);
//     // var contentTableName = "<div style='font-size:24px;text-align:center'>" + txtNameDsrc + "</div>";
//     // htmlTemp += contentTableName;
//     // var tableHeaderTable = document.getElementById("tableHeaderTable").outerHTML;
//     // htmlTemp += tableHeaderTable;
//     // htmlTemp += contentTable;
//     // var tableFooterTable = document.getElementById("tableFooterTable").outerHTML;
//     // htmlTemp += tableFooterTable;
//     if (document.getElementById("drillDown")) {
//         var drillDownTable = document.getElementById("drillDown").outerHTML;
//         htmlTemp += drillDownTable;
//     }
//     htmlTemp += "</body></html>";
//     var blob = new Blob(
//         [htmlTemp],
//         {
//             type: "application/vnd.ms-excel"
//         }
//     );
//     saveAs(blob, txtNameDsrc + ".xls");
// }

/**
 * 转换grid为table标签
 * @param printDatagrid
 * @returns {string}
 * @constructor
 */
function ChangeToTable(printDatagrid) {
    var printData = $("#" + printDatagrid);
    var tableString = "<style>td{mso-number-format:\"\\@\";}</style>";
    tableString += '<table border="1" class="exceltable" style="border-collapse: collapse;border:0px;">';
    var columns = printData.datagrid("options").columns; // 得到columns对象
    var nameList = new Array();
// 载入title
    if (typeof columns != 'undefined' && columns != '') {
        $(columns).each(function (index) {
            tableString += '\n<tr>';
            for (var i = 0; i < columns[index].length; ++i) {
                if (!columns[index][i].hidden) {
                    var width = columns[index][i].width;
                    // debugger;
                    if (width&&width.indexOf("%") > 0) {
                        width = (window.screen.availWidth) * parseInt(width) / 100;
                    }
                    tableString += '\n<th width="' + width + '"';
                    if (typeof columns[index][i].rowspan != 'undefined' && columns[index][i].rowspan > 1) {
                        tableString += ' rowspan="' + columns[index][i].rowspan + '"';
                    }
                    if (typeof columns[index][i].colspan != 'undefined' && columns[index][i].colspan > 1) {
                        tableString += ' colspan="' + columns[index][i].colspan + '"';
                    }
                    if (typeof columns[index][i].field != 'undefined' && columns[index][i].field != '' && columns[index][i].field != '占位') {
                        nameList.push(columns[index][i]);
                    }
                    tableString += ' style="background-color: #BFE1FF;text-align:center;" ';
                    tableString += '>' + columns[index][i].title + '</th>';
                }
            }
            tableString += '\n</tr>';
        });
    }
// 载入内容
    var rows = printData.datagrid("getRows"); // 这段代码是获取当前页的所有行
    for (var i = 0; i < rows.length; ++i) {
        tableString += '\n<tr >';
        for (var j = 0; j < nameList.length; ++j) {
            var e = nameList[j].field.lastIndexOf('_0');
            tableString += '\n<td style="padding:3px;"';
            if (nameList[j].align != undefined && nameList[j].align != '') {
                tableString += ' text-align:' + nameList[j].align + ';';
            }
            tableString += '" >';

            if ($('#dateTable h3').html() == '地方债发行情况统计表（汇总表）') {
                if (nameList[j].field === 'A2') {
                    if (String(rows[i].A2).indexOf(".") == -1) {
                        rows[i].A2 = amtValueFormat(rows[i].A2);
                    }
                }
            }
            if (e + 2 == nameList[j].field.length) {
                tableString += rows[i][nameList[j].field.substring(0, e)] == null ? "" : rows[i][nameList[j].field.substring(0, e)];
            } else
                tableString += rows[i][nameList[j].field] == null ? "" : rows[i][nameList[j].field];
            tableString += '</td>';
        }
        tableString += '\n</tr>';
    }
    tableString += '\n</table>';
    return tableString;
}

/**
 * 转换grid为table标签,全表查询
 * @param printDatagrid  数据表格ID
 * @param url            请求地址
 * @param paramsdata     查询参数，包涵页面中所有条件参数。
 * @param col            表格表头需要取出的列数，从后向前依次去除。
 * @param formatCols     导出内容中需要进行数字格式化的field
 * @param colOrder       导出列的对齐规则
 * @returns {string}     返回组装好的table元素
 * @constructor
 */
function ChangeToTable1(printDatagrid, url, paramsdata, col, formatCols, colOrder) { //转换grid为table标签
    var printData = $("#" + printDatagrid);
    var tableString = "<style>td{mso-number-format:\"\\@\";}</style>";
    tableString += '<table border="1" class="exceltable" style="border-collapse: collapse;border:0px;">';
    var columns = printData.datagrid("options").columns, // 得到columns对象
        frozenColumns = printData.datagrid("options").frozenColumns; // 得到frozenColumns对象冻结列
    var nameList = new Array();
    if (typeof frozenColumns != 'undefined' && frozenColumns != '') {
        $.each(frozenColumns[0], function (i, v) {
            columns[0].splice(0, 0, v);
        })
    }
// 载入title
    if (typeof columns != 'undefined' && columns != '') {
        $(columns).each(function (index) {
            tableString += '\n<tr>';
            for (var i = 0; i < columns[index].length - col; ++i) {
                if (!columns[index][i].hidden) {
                    var width = columns[index][i].width;
                    if (typeof width === 'string' && width.indexOf("%") > 0) {
                        width = (window.screen.availWidth) * parseInt(width) / 100;
                    }
                    tableString += '\n<th width="' + width + '"';
                    if (typeof columns[index][i].rowspan != 'undefined' && columns[index][i].rowspan > 1) {
                        tableString += ' rowspan="' + columns[index][i].rowspan + '"';
                    }
                    if (typeof columns[index][i].colspan != 'undefined' && columns[index][i].colspan > 1) {
                        tableString += ' colspan="' + columns[index][i].colspan + '"';
                    }
                    if (typeof columns[index][i].field != 'undefined' && columns[index][i].field != '' && columns[index][i].field != '占位') {
                        nameList.push(columns[index][i]);
                    }
                    tableString += ' style="background-color: #BFE1FF;text-align:center;" ';
                    tableString += '>' + columns[index][i].title + '</th>';
                }
            }
            tableString += '\n</tr>';
        });
    }
    if (colOrder != undefined && colOrder != '') {
        nameList = colOrder;
    }
    $.ajax({
        type: 'POST',
        url: url,
        traditional: true,
        dataType: "json",
        async: false,
        contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
        data: paramsdata,
        success: function (data) {
            data = data.rows;
            if (formatCols != null && formatCols != '' && formatCols != undefined) {
                for (var d_index = 0; d_index < data.length; d_index++) {
                    for (var f_index = 0; f_index < formatCols.length; f_index++) {
                        var fCol = formatCols[f_index];
                        if (data[d_index][fCol] != undefined) {
                            if ((columns[0][3].title == '笔数')
                                & (url == '../../../bank_guoku/toBankCombinatorial' || url == '../../../bank_guoku/toGuokuCombinatorial')
                                & (fCol == 'AMT')) {
                                data[d_index][fCol] = "<span style='mso-number-format:\"\\@\"'>" + amtValueFormatInt(data[d_index][fCol]) + "</span>";
                            } else {
                                data[d_index][fCol] = "<span style='mso-number-format:\"\\#\\,\\#\\#0\\.00\"'>" + amtValueFormat(data[d_index][fCol]) + "</span>";
                            }
                        }
                    }
                }
            }
            //console.log(JSON.stringify(data));
            // alert(JSON.stringify(data));
            for (var i = 0; i < data.length; ++i) {
                tableString += '\n<tr >';
                // debugger;
                for (var j = 0; j < nameList.length; ++j) {
                    var e = nameList[j].field.lastIndexOf('_0');
                    tableString += '\n<td style="padding:3px;' + (i % 2 == 1 ? 'background-color:#efefef;' : '');
                    if (nameList[j].align != undefined && nameList[j].align != '') {
                        tableString += ' text-align:' + nameList[j].align + ';';
                    }
                    tableString += '" >';
                    // alert(data[2][nameList[2].field]);
                    console.log(data[i][nameList[j].field.substring(0, e)]);
                    if (e + 2 == nameList[j].field.length) {
                        tableString += data[i][nameList[j].field.substring(0, e)] == null ? "" : data[i][nameList[j].field.substring(0, e)];
                    } else if (
                        nameList[j].field == 'D_ACCEPT' || nameList[j].field == 'D_ENTRUST' || nameList[j].field == 'TS_SYSUPDATE' || nameList[j].field == 'D_RECK'
                        || nameList[j].field == 'C_CREATETIME' || nameList[j].field == 'DATA_DATE' || nameList[j].field == 'ADD_DATE' || nameList[j].field == 'D_ACCT'
                        || nameList[j].field == 'D_CURACCEPT' || nameList[j].field == 'D_CURACCT' || nameList[j].field == 'D_CURBILL' || nameList[j].field == 'D_DATE'
                        || nameList[j].field == 'D_IMPORTENTRUST' || nameList[j].field == 'D_LIMIT' || nameList[j].field == 'D_ORIACCEPT' || nameList[j].field == 'D_ORIACCT'
                        || nameList[j].field == 'D_ORIBILL' || nameList[j].field == 'D_PKGENTRUST' || nameList[j].field == 'D_TAXENDDATE' || nameList[j].field == 'D_TAXSTARTDATE'
                        || nameList[j].field == 'D_VOUCHER' || nameList[j].field == 'MOD_DATE') {
                        // if( /[0-9]{4}-[0-9]{2}-[0-9]{2}]/.test(data[i][nameList[j].field]) || /[0-9]{4}-[0-9]{2}]/.test(data[i][nameList[j].field]) || /[0-9]{4}/.test(data[i][nameList[j].field]) ){
                        if (data[i][nameList[j].field].length <= 10) {
                            tableString += data[i][nameList[j].field];
                        } else {
                            // tableString += data[i][nameList[j].field] == null ? "" :  JSON.stringify(data[i][nameList[j].field]);
                            tableString += data[i][nameList[j].field] == null ? "" : datetime(data[i][nameList[j].field]);
                        }
                    } else {
                        tableString += data[i][nameList[j].field] == null ? "" : data[i][nameList[j].field];
                    }
                    tableString += '</td>';
                }
                tableString += '\n</tr>';

            }
            tableString += '\n</table>';
        }
    });
    return tableString;
}

/**
 * 时间转化方法
 * @param dataVar DATETIME类型 或 TIMESTAMP类型
 * @returns {*}
 */
function datetime(dataVar) {
    if (dataVar.time) {
        var date = new Date(Number(dataVar.time));
    } else {
        var date = new Date(dataVar);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    }
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    // D = date.getDate() + ' ';
    D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    // h = date.getHours() + ':';
    h = (date.getHours() < 10 ? '0' + (date.getHours()) : date.getHours()) + ':';
    // m = date.getMinutes() + ':';
    m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes()) + ':';
    s = (date.getSeconds() < 10 ? '0' + (date.getSeconds()) : date.getSeconds());
    // s = date.getSeconds();
    if (dataVar.time) {
        return Y + M + D;
    } else {
        return Y + M + D + h + m + s;
    }
}

/**
 * 依照传入的数组、列数，组装报表选择条件
 * @param dscrArr     导出条件的中文描述
 * @param textArr     导出条件的当前值
 * @param colspanNo   导出条件需要跨列个数
 * @returns {string}  返回组装好的table元素
 */
function setTableContent(dscrArr, textArr, colspanNo) {
    var tableTemp =
        "<table width='100%;'>";
    for (var i = 0; i < dscrArr.length; i++) {
        var trTemp =
            "<tr>" +
            "   <td width='6%'>" +
            dscrArr[i] +
            "   </td>" +
            "   <td   width='94%' colspan='" + (colspanNo - 1) + "' style='text-align: left;vnd.ms-excel.numberformat:@;'>" +
            textArr[i] +
            "   </td>" +
            "</tr>";
        tableTemp += trTemp;
    }
    tableTemp +=
        "</table>";
    return tableTemp;
}

/**
 * 添加导出进度条
 */
function appendLoadingGif() {
    $("#divFrame").append(
        "   <div id='cover' style='" +
        "           position:absolute;" +
        "           text-align: center;" +
        "           z-index: 99;" +
        "           top:35%;" +
        "           width: 100%;" +
        "           height: 100%;" +
        "           padding: 5px 15px;" +
        "           border: solid 0px #1E9FFF;'>" +
        "        <img src='../../../../guoku/static/img/loading_cross_bar.gif' alt='Loading...' style='width: 18%; height: 1.8%;'/>" +
        "        <br/><span style='font-size: 24px; color:#1E9FFF'>正在导出请稍后……</span>" +
        "    </div>"
    );
}

/**
 * 移除导出进度条
 */
function removeLoadingGif() {
    $("#divFrame")[0].removeChild($("#cover")[0]);
}

/**
 * 表格导出
 */
function tableExport(domId, name) {
    var htmlTemp = "<html><head><meta charset='utf-8' /></head><body>";
    htmlTemp += document.getElementById(domId).outerHTML;
    htmlTemp += "</body></html>";
    var blob = new Blob(
        [htmlTemp],
        {
            type: "application/vnd.ms-excel"
        }
    );
    saveAs(blob, name + ".xls");
}