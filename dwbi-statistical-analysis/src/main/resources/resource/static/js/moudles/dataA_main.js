//设置jquery 异步请求 header 添加Authorization: localStorage.token
$.ajaxSetup({
    beforeSend: function (request) {
        request.setRequestHeader('Authorization', localStorage.token);
    },
});
//初始化全局条件
window.dataA_queryParams = {
    date: "201801",                  //日期
    code: "",                      //政区编码
    zqName: "",                 //政区名称
    org: "C1080552000033",           //默认贵阳银行 机构编码
    ref: "dataA_sqsp"                //业务模块
}

$(function(){
	$.ajax({
        type: "GET",
        url: "/analysis/dataA/getArea",
        success: function(data){
        			dataA_queryParams.code = data.areaCode;
        			startLoad()
                 }
    });
})

function startLoad() {
    //初始化各个组件
    layui.form.render();
    //layui 自定义模块加载
    layui.extend({
        //基础扩展 treeSelect
        treeSelect: "/analysis/static/js/moudles/treeSelect",
        //依赖基础 可同名
        bar: "/analysis/static/js/moudles/baseChart/bar",
        pie: "/analysis/static/js/moudles/baseChart/pie",
        square: "/analysis/static/js/moudles/baseChart/square",
        map: "/analysis/static/js/moudles/baseChart/map",
        Dtree: "/analysis/static/js/moudles/baseChart/Dtree",
        //自身有的 需要加项目名
        dataA_dataService: "/analysis/static/js/moudles/dataA_dataService",
        dataA_dataHandle: "/analysis/static/js/moudles/dataA_dataHandle",
        //业务 图
        dataA_map: "/analysis/static/js/moudles/aspect/dataA_map",   //申请审批的 map图
        dataA_line: "/analysis/static/js/moudles/aspect/dataA_line", //申请审批通过率折线图
        dataA_bar: "/analysis/static/js/moudles/aspect/dataA_bar",   //申请审批通过率直方图
        dataA_top: "/analysis/static/js/moudles/aspect/dataA_top",   //
        dataA_pie: "/analysis/static/js/moudles/aspect/dataA_pie",   //
        dataA_desc: "/analysis/static/js/moudles/aspect/dataA_desc",   //
        dataA_Dtree: "/analysis/static/js/moudles/aspect/dataA_Dtree",   //
    });
    //提前加载基础组件
    var load = ['treeSelect', 'bar', 'pie', 'square', 'map', 'Dtree', 'dataA_dataService','dataA_dataHandle'];
    var load_dataE = ['dataA_Dtree', 'dataA_map', 'dataA_line', 'dataA_bar', 'dataA_top', 'dataA_pie', 'dataA_desc'];
    //
    layui.use(load.concat(load_dataE), function () {
        //监听页面监听  图联动 
        var active = {
            dataA_sqsp: {  //申请审批页面
                init: function () {
                    layui.dataA_map.init(dataA_queryParams);
                    layui.dataA_line.init(dataA_queryParams);
                    layui.dataA_bar.init(dataA_queryParams);
                    layui.dataA_top.init(dataA_queryParams);
                   // layui.dataA_pie.init(dataA_queryParams);
                    layui.dataA_desc.init(dataA_queryParams);
                }
            },
            dataA_ffxx: {   //发放信息页面
                init: function () {
                    layui.dataA_desc.init(dataA_queryParams);
                    layui.dataA_pie.init(dataA_queryParams);
                    layui.dataA_top.init(dataA_queryParams);
                    layui.dataA_bar.init(dataA_queryParams);
                }
            },
            dataA_dbxx: {   //担保信息页面
                init: function () {
                    layui.dataA_desc.init(dataA_queryParams);
                    layui.dataA_top.init(dataA_queryParams);
                    layui.dataA_Dtree.init(dataA_queryParams);
                }
            },
            dataA_ckxx: {   //存款信息页面
                init: function () {
                    layui.dataA_map.init(dataA_queryParams);
                    //layui.dataA_line.init(dataA_queryParams);
                    layui.dataA_pie.init(dataA_queryParams);
                    layui.dataA_Dtree.init(dataA_queryParams);
                }
            }
        }
        var cache_html = {}; //一次回话缓存页面
        function changeOrInit() {
        	$.ajax({
                type: "GET",
                url: "/analysis/dataA/getAreaSelect",
                data: {username:$("#username").val(), content:$("#content").val()},
                //dataType: "json",
                success: function(data){
		                	 initZqTree(data.data);
                         }
            });
            //
            var ref = dataA_queryParams.ref;
            //切换页面
            var $target = $("#" + ref);
            if (cache_html[ref]) {
                $(".dataA_content").html(cache_html[ref]);
                active[ref] ? active[ref]['init'].call(this) : '';
            } else {
                $.get("/analysis/static/html/dataA/" + ref.split("_")[1] + ".html").done(function (res) {
                    cache_html[ref] = res; //缓存
                    $(".dataA_content").html(res);
                    active[ref] ? active[ref]['init'].call(this) : '';
                }).fail(function (error) {
                    console.error("加载模板错误" + error);
                })
            }
        }
        //不同业务切换 
        $(".dataA-nav-header li").on("click", function () {
            $(".dataA-nav-header li").removeClass("dataA-this")
            var $t = $(this);
            $t.addClass("dataA-this");
            var ref = $t.attr("ref");
            if (ref) {
                dataA_queryParams.ref = ref;
                changeOrInit();
            }
        })
        //公共时间 监听
        layui.laydate.render({
            elem: '#dataA-date-input',
            type: 'month',
            theme: 'molv',
            value: new Date(),
            done: function (value) {
                console.log("选择时间：" + value);
                dataA_queryParams.date = value.replace(/-/g,"");
                changeOrInit();
            }
        });
        ////////////////////////////////页面初始化//////////////////////////////
        function initZqTree(data) {
        	var areaData = [
                {
                    name: "贵州省",
                    value: "520000",
                    children: []
                }
            ];
        	$.each(data, function (i, item) {
                if (item.AREA_DSCR_2 != '贵州省') {
                	areaData[0].children.push({name:item.AREA_DSCR_2,value:item.AREA_NO_ID_2})
                }
            })
            layui.treeSelect({
                elem: "#dataA-zq-input",
                default: dataA_queryParams.zqName,
                click: function (obj) {
                	if(obj.value=="520000" && dataA_queryParams.code!="520000") {
                		console.log("你没有省级权限");
                		return;
                	}
                    dataA_queryParams.code = obj.value;
                    dataA_queryParams.zqName=obj.name;
                    //改变
                    changeOrInit();
                },
                data:areaData
            })
        }
        //机构
        layui.form.on("select(dataA-jg-select)", function (obj) {
            var org = obj.value;
            if (org) {
                dataA_queryParams.org = org;
                changeOrInit();
            } else {

            }
        })
        //页面初始化
        changeOrInit();
        //暴露  调用方法
        window.dataA_chanage = changeOrInit;
    })
}

