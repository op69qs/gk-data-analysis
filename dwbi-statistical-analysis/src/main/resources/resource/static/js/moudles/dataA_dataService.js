/**
 * 数据服务接口
 */
layui.define(function (exports) {
    'use strict';
    //同步获取地图数据
    function getGeoJson(code) {
        var geojson = JSON.parse(localStorage.getItem(code));
        if (!geojson) {
            var url = "/analysis/assets/echart/map/" + code + ".json";
            $.ajax({
                url: url,
                async: false,
                success: function (res) {
                    geojson = res;
                },
                error: function () {
                    console.error("同步加载geojson失败：政区code[" + code + "]");
                }
            })
            localStorage.setItem(code, JSON.stringify(geojson));
        }
        return geojson;
    }
    //合并sql 进行数据获取  定义为同步代码块
    var cache_data = {};
    function getData() {
        var key = dataA_queryParams.date + dataA_queryParams.code + dataA_queryParams.org + dataA_queryParams.ref;
        var data;
        if (!cache_data[key]) {
            $.ajax({
                url: "/analysis/dataA/" + dataA_queryParams.ref,
                data: window.dataA_queryParams,
                async: false,
                success: function (res) {
                    if (res.code == '404') {
                        //暂无数据
                    } else {
                        //data=res.data;
                        cache_data[key] = res.data;
                    }
                },
                error: function () {
                    console.error("同步加载数据分析页面数据失败：政区code[" + dataA_queryParams.code + "][" + dataA_queryParams.ref + "]");
                }
            })
        }
        //return data;
        return cache_data[key];
    }
    //获取不同业务数据
    function getTypeData(obj) {
        var result = getData();
        var route = {
            dataA_sqsp: function () { //申请审批页面
                return {
                    map: {
                        dataA_sq_map: {
                            title: "贷款申请地区情况(单位：笔)",
                            series: {
                                person: result['dataA_sqsp_map']['sq_map_p'],
                                unit: result['dataA_sqsp_map']['sq_map_u']
                            }
                        },
                        dataA_sp_map: {
                            title: "贷款审批地区情况(单位：笔)",
                            series: {
                                person: result['dataA_sqsp_map']['sp_map_p'],
                                unit: result['dataA_sqsp_map']['sp_map_u']
                            }
                        }
                    },
                    line: {
                        dataA_sqsp_line: {
                            title: "贷款审批通过率时序分析",
                            category: result['dataA_sqsp_line']['category'],
                            person: result['dataA_sqsp_line']['person'],
                            unit: result['dataA_sqsp_line']['unit']
                        }
                    },
                    bar: {
                        dataA_sqsp_p_sq_bar: {
                            name: "个人",
                            title: "个人贷款申请金额直方图(单位：笔)",
                            category:result['dataA_sqsp_bar']['sq_bar_category_psq'],
                            data: result['dataA_sqsp_bar']['sq_bar_data_psq']
                        },
                        dataA_sqsp_p_sp_bar: {
                            name: "个人",
                            title: "个人贷款审批金额直方图(单位：笔)",
                            category:result['dataA_sqsp_bar']['sq_bar_category_psp'],
                            data: result['dataA_sqsp_bar']['sq_bar_data_psp']
                        },
                        dataA_sqsp_u_sq_bar: {
                            name: "单位",
                            title: "单位贷款申请金额直方图(单位：笔)",
                            category:result['dataA_sqsp_bar']['sq_bar_category_usq'],
                            data: result['dataA_sqsp_bar']['sq_bar_data_usq']
                        },
                        dataA_sqsp_u_sp_bar: {
                            name: "单位",
                            title: "单位贷款审批金额直方图(单位：笔)",
                            category:result['dataA_sqsp_bar']['sq_bar_category_usp'],
                            data: result['dataA_sqsp_bar']['sq_bar_data_usp']
                        }
                    },
                    top: {
                        dataA_sq_u_type_top: {
                            name: "单位行业",
                            title: "单位贷款行业申请通过率top5",
                            category: result['dataA_sqsp_top']['category_sq'],
                            data: result['dataA_sqsp_top']['data_sq']
                        },
                        dataA_sp_u_type_top: {
                            name: "单位行业",
                            title: "单位贷款行业审批通过率top5",
                            category: result['dataA_sqsp_top']['category_sp'],
                            data: result['dataA_sqsp_top']['data_sp']
                        }
                    },
                    desc: {
                        dataA_sqsp_p_sqbs: result['dataA_sqsp_desc']['dataA_sqsp_p_sqbs']||"-",
                        dataA_sqsp_p_sqtgbs: result['dataA_sqsp_desc']['dataA_sqsp_p_sqtgbs']||"-",
                        dataA_sqsp_p_sqtgl: result['dataA_sqsp_desc']['dataA_sqsp_p_sqtgl']||"-",

                        dataA_sqsp_p_spbs: result['dataA_sqsp_desc']['dataA_sqsp_p_spbs']||"-",
                        dataA_sqsp_p_sptgbs: result['dataA_sqsp_desc']['dataA_sqsp_p_sptgbs']||"-",
                        dataA_sqsp_p_sptgl: result['dataA_sqsp_desc']['dataA_sqsp_p_sptgl']||"-",

                        dataA_sqsp_u_sqbs: result['dataA_sqsp_desc']['dataA_sqsp_u_sqbs']||"-",
                        dataA_sqsp_u_sqtgbs: result['dataA_sqsp_desc']['dataA_sqsp_u_sqbs']||"-",
                        dataA_sqsp_u_sqtgl: result['dataA_sqsp_desc']['dataA_sqsp_u_sqtgl']||"-",

                        dataA_sqsp_u_spbs: result['dataA_sqsp_desc']['dataA_sqsp_u_spbs']||"-",
                        dataA_sqsp_u_sptgbs: result['dataA_sqsp_desc']['dataA_sqsp_u_sptgbs']||"-",
                        dataA_sqsp_u_sptgl: result['dataA_sqsp_desc']['dataA_sqsp_u_sptgl']||"-",
                        //平均数、众数、中位数
                        dataA_sqsp_p_sq_bar_pjs:result['dataA_sqsp_desc']['dataA_sqsp_p_sq_bar_pjs']||"-",
                        dataA_sqsp_p_sq_bar_zs:result['dataA_sqsp_desc']['dataA_sqsp_p_sq_bar_zs']||"-",
                        dataA_sqsp_p_sq_bar_zws:result['dataA_sqsp_desc']['dataA_sqsp_p_sq_bar_zws']||"-",
                        
                        dataA_sqsp_p_sp_bar_pjs:result['dataA_sqsp_desc']['dataA_sqsp_p_sp_bar_pjs']||"-",
                        dataA_sqsp_p_sp_bar_zs:result['dataA_sqsp_desc']['dataA_sqsp_p_sp_bar_zs']||"-",
                        dataA_sqsp_p_sp_bar_zws:result['dataA_sqsp_desc']['dataA_sqsp_p_sp_bar_zws']||"-",
                        
                        dataA_sqsp_u_sq_bar_pjs:result['dataA_sqsp_desc']['dataA_sqsp_u_sq_bar_pjs']||"-",
                        dataA_sqsp_u_sq_bar_zs:result['dataA_sqsp_desc']['dataA_sqsp_u_sq_bar_zs']||"-",
                        dataA_sqsp_u_sq_bar_zws:result['dataA_sqsp_desc']['dataA_sqsp_u_sq_bar_zws']||"-",
                        
                        dataA_sqsp_u_sp_bar_pjs:result['dataA_sqsp_desc']['dataA_sqsp_u_sp_bar_pjs']||"-",
                        dataA_sqsp_u_sp_bar_zs:result['dataA_sqsp_desc']['dataA_sqsp_u_sp_bar_zs']||"-",
                        dataA_sqsp_u_sp_bar_zws:result['dataA_sqsp_desc']['dataA_sqsp_u_sp_bar_zws']||"-",
                    },
                    pie: {
                        dataA_sqsp_p_type_pie: {
                            title: "个人贷款类型饼图",
                            data: [{ name: '个人消费', value: 50 }, { name: "个人经营", value: 80 }]
                        }
                    }
                }
            },
            dataA_ffxx: function () {   //发放信息页面
                return {
                    desc: {
                        dataA_ffxx_p_htbs: result['dataA_ffxx_desc']['dataA_ffxx_p_htbs']||"-", //合同笔数
                        dataA_ffxx_p_jjbs: result['dataA_ffxx_desc']['dataA_ffxx_p_jjbs']||"-", //借据笔数
                        dataA_ffxx_u_htbs: result['dataA_ffxx_desc']['dataA_ffxx_u_htbs']||"-",
                        dataA_ffxx_u_jjbs: result['dataA_ffxx_desc']['dataA_ffxx_u_jjbs']||"-",
                        //普惠金融八个指标贷款余额
                        dataA_ffxx_zb_nh: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_nh']),
                        dataA_ffxx_zb_gt: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_gt']),
                        dataA_ffxx_zb_jd: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_jd']),
                        dataA_ffxx_zb_xw: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_xw']),
                        dataA_ffxx_zb_xg: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_xg']),
                        dataA_ffxx_zb_zx: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_zx']),
                        dataA_ffxx_zb_xt: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_xt']),
                        dataA_ffxx_zb_wd: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_zb_wd']),
                        //
                        dataA_ffxx_p_jxje:layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_p_jxje']),
                        //直方图 平均数 众数  中位数
                        dataA_ffxx_p_je_bar_pjs: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_p_je_bar_pjs']),
                        dataA_ffxx_p_je_bar_zs: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_p_je_bar_zs']),
                        dataA_ffxx_p_je_bar_zws: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_p_je_bar_zws']),
                        
                        dataA_ffxx_u_je_bar_pjs:layui.dataA_dataHandle.handleNum( result['dataA_ffxx_desc']['dataA_ffxx_u_je_bar_pjs']),
                        dataA_ffxx_u_je_bar_zs: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_u_je_bar_zs']),
                        dataA_ffxx_u_je_bar_zws: layui.dataA_dataHandle.handleNum(result['dataA_ffxx_desc']['dataA_ffxx_u_je_bar_zws']),
                       
                        dataA_ffxx_p_lv_bar_pjs: result['dataA_ffxx_desc']['dataA_ffxx_p_lv_bar_pjs']||"-",
                        dataA_ffxx_p_lv_bar_zs: result['dataA_ffxx_desc']['dataA_ffxx_p_lv_bar_zs']||"-",
                        dataA_ffxx_p_lv_bar_zws: result['dataA_ffxx_desc']['dataA_ffxx_p_lv_bar_zws']||"-",
                        
                        dataA_ffxx_u_lv_bar_pjs: result['dataA_ffxx_desc']['dataA_ffxx_u_lv_bar_pjs']||"-",
                        dataA_ffxx_u_lv_bar_zs: result['dataA_ffxx_desc']['dataA_ffxx_u_lv_bar_zs']||"-",
                        dataA_ffxx_u_lv_bar_zws: result['dataA_ffxx_desc']['dataA_ffxx_u_lv_bar_zws']||"-",
                    },
                    top: {
                        dataA_ffxx_u_type_top: {
                            name: "单位行业",
                            title: "单位贷款行业top5(单位：万元)",
                            category: result['dataA_ffxx_top']['category'],
                            data: result['dataA_ffxx_top']['data']
                        }
                    },
                    pie: {
                        dataA_ffxx_ywpz_ht_pie: {
                            title: "合同业务品种",
                            data: result['dataA_ffxx_pie']['dataA_ffxx_ywpz_ht_pie']
                        },
                        dataA_ffxx_dbfs_ht_pie: {
                            title: "合同贷款担保方式",
                            data:result['dataA_ffxx_pie']['dataA_ffxx_dbfs_ht_pie']
                        },
                        dataA_ffxx_dklx_ht_pie: {
                            title: "合同贷款发生类型",
                            data: result['dataA_ffxx_pie']['dataA_ffxx_dklx_ht_pie']
                        },
                        dataA_ffxx_qxjg_ht_pie: {
                            title: "合同贷款期限结构",
                            data:result['dataA_ffxx_pie']['dataA_ffxx_qxjg_ht_pie']
                        },
                        dataA_ffxx_ywpz_jj_pie: {
                            title: "借据业务品种",
                            data: result['dataA_ffxx_pie']['dataA_ffxx_ywpz_jj_pie']
                        },
                        dataA_ffxx_dbfs_jj_pie: {
                            title: "借据贷款担保方式",
                            data:result['dataA_ffxx_pie']['dataA_ffxx_dbfs_jj_pie']
                        },
                        dataA_ffxx_dklx_jj_pie: {
                            title: "借据贷款发生类型",
                            data: result['dataA_ffxx_pie']['dataA_ffxx_dklx_jj_pie']
                        },
                        dataA_ffxx_qxjg_jj_pie: {
                            title: "借据贷款期限结构",
                            data:result['dataA_ffxx_pie']['dataA_ffxx_qxjg_jj_pie']
                        }
                    },
                    bar: {
                        dataA_ffxx_p_je_bar: {
                            name: "个人",
                            title: "个人合同金额直方图",
                            category: result['dataA_ffxx_bar']['bar_category_p_htje'],
                            data: result['dataA_ffxx_bar']['bar_data_p_htje']
                        },
                        dataA_ffxx_u_je_bar: {
                            name: "单位",
                            title: "单位合同金额直方图",
                            category: result['dataA_ffxx_bar']['bar_category_u_htje'],
                            data: result['dataA_ffxx_bar']['bar_data_u_htje'],
                        },
                        dataA_ffxx_p_lv_bar: {
                            name: "个人",
                            title: "个人合同利率直方图",
                            category:  result['dataA_ffxx_bar']['bar_category_p_htlv'],
                            data: result['dataA_ffxx_bar']['bar_data_p_htlv'],
                        },
                        dataA_ffxx_u_lv_bar: {
                            name: "单位",
                            title: "单位合同利率直方图",
                            category: result['dataA_ffxx_bar']['bar_category_u_htlv'],
                            data: result['dataA_ffxx_bar']['bar_data_u_htlv'],
                        }
                    },
                }

            },
            dataA_dbxx: function () {    //担保信息页面
                return {
                    desc: {
                        dataA_dbxx_scgz: layui.dataA_dataHandle.handleNum(result['dataA_dbxx_desc']['dataA_dbxx_scgz']),
                        dataA_dbxx_zxgz: layui.dataA_dataHandle.handleNum(result['dataA_dbxx_desc']['dataA_dbxx_zxgz'])
                    },
                    top: {
                        dataA_dbxx_u_djb_top: {
                            name: "单位贷款行业抵借比",
                            title: "单位贷款行业抵借比top5",
                            category: result['dataA_dbxx_top']['category'],
                            data: result['dataA_dbxx_top']['data']
                        }
                    },
                    tree: {
                        dataA_dbxx_dbjg_tree: {
                            title: "担保物结构图(单位：万元)",
                            data: layui.dataA_dataHandle.handle_dbw_tree(result['dataA_dbxx_tree'],"担保物")
                        }
                    }
                }
            },
            dataA_ckxx: function () {    //存款信息页面
                return {
                    map: {
                        dataA_ckxx_map: {
                            title: '存款余额地区情况(单位：万元)',
                            series: {
                                person:result['dataA_ckxx_map']['person'],
                                unit: result['dataA_ckxx_map']['unit']
                            }
                        }
                    },
                    // line: {
                    //     dataA_ckxx_line: {
                    //         title: "存款余额时序图",
                    //         category: result['dataA_ckxx_line']['category'],
                    //         person: result['dataA_ckxx_line']['person'],
                    //         unit: result['dataA_ckxx_line']['unit']
                    //     }
                    // },
                    pie: {
                        dataA_ckxx_ckqx_pie: {
                            title: "存款期限结构(单位：万元)",
                            data: result['dataA_ckxx_pie']
                        }
                    },
                    tree: {
                        dataA_ckxx_cpjg_tree: {
                            title: "存款产品结构(单位：万元)",
                            data: layui.dataA_dataHandle.handle_ckxx_tree(result['dataA_ckxx_tree'],"存款产品",3)
                        }

                    }
                }
            }
        }
        var data = route[obj.ref] ? route[obj.ref].call(this) : "";
        //针对map数据进行处理   根据code进行数据映射
        layui.dataA_dataHandle.handleMapData(data, obj.code);
        return data;
    }
    exports("dataA_dataService", {
        getGeoJson: getGeoJson,
        getTypeData: getTypeData
    })
});