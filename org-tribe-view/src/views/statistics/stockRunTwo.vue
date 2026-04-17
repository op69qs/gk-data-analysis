<template>
    <a-card :bordered="false" class="card-area">
        <a-button v-if="dataSource.length>0" @click="handleExport">导出</a-button>
        <a-button style="margin-left: 8px" @click="$router.go(-1)">返回上层</a-button>
        <h1 style="text-align:center;">{{title1}}科目{{title2}}库存情况表</h1>
        <!-- <a-icon v-if="dataSource.length>0" type="download" style="font-size: 25px;position:absolute;right: 40px;top: 40px;cursor: pointer;"
                      @click="handleExport" title="下载"/> -->
        <!-- table区域-begin -->
        <div style="width: 70%;margin: 0 auto">
            <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}" style="width: 100%"
                      :span-method="objectSpanMethod" border>
                <el-table-column label="账期" width="200"
                                 align="center" prop="D_ACCT">
                </el-table-column>
                <el-table-column label="国库名称" width="240" header-align="center" align="left" prop="S_TRENAME">
                </el-table-column>
                <el-table-column label="流入" width="240" header-align="center" align="right" prop="F_AMT_INCM">
                </el-table-column>
                <el-table-column prop="F_AMT_OUT" label="流出" width="240" header-align="center" align="right"/>
                <el-table-column prop="BALANCE" label="库存余额" header-align="center" align="right"/>
            </el-table>
        </div>
        <!--  <el-pagination class="page"
         @current-change="handleCurrentChange"
         :page-size="pageSize"
         layout="total, prev, pager, next, jumper"
         :total="total"
         :current-page.sync="currentPage">
       </el-pagination> -->
        <!-- table区域-end -->
    </a-card>
</template>

<script>
    /* import {ListMixin} from '@/mixins/ListMixin' */
    import {
        getReportAll, getGuokuTree, queryAreaData
    } from '@/api/nationalTreasury'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'

    export default {
        name: 'stockRun',
        data() {
            return {
                loading: false,
                treeData: [],
                /* 分页参数 */
                pageSize: 10,
                pageNo: 1,
                total: 0,
                currentPage: 1,
                spanArr: [],
                title1: '',
                title2: '',
                dataSource: [],

            }
        },
        watch: {
            $route(val) {
                if (val.name == "statistics-stockRunTwo") {
                    let obj = JSON.parse(this.$route.query.query);
                    this.title1 = obj.ACOUNT_SUBJECT;
                    this.title2 = obj.LEVEL_NAME;
                    this.loading = true;
                    let param = {
                        D_ACCT: obj.D_ACCT,
                        ACOUNT_SUBJECT: obj.ACOUNT_SUBJECT,
                        GUOKU_DSCR: obj.GUOKU_DSCR,
                        GUOKU_ID: obj.GUOKU_ID,
                        UNIT: obj.UNIT,
                        UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                        LEVEL: obj.LEVEL,
                    }
                    this.spanArr = [];
                    queryAreaData(param).then(res => {
                        if (res.result === 'success') {
                            this.loading = false;
                            this.dataSource = res.rows
                            this.getSpanArr(this.dataSource);
                        }
                    })
                }
            }
        },
        mounted() {
            let obj = JSON.parse(this.$route.query.query);
            this.title1 = obj.ACOUNT_SUBJECT;
            this.title2 = obj.LEVEL_NAME;
            let param = {
                D_ACCT: obj.D_ACCT,
                ACOUNT_SUBJECT: obj.ACOUNT_SUBJECT,
                GUOKU_DSCR: obj.GUOKU_DSCR,
                GUOKU_ID: obj.GUOKU_ID,
                UNIT: obj.UNIT,
                UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                LEVEL: obj.LEVEL,
            }
            this.spanArr = [];
            queryAreaData(param).then(res => {
                if (res.result === 'success') {
                    this.loading = false;
                    this.dataSource = res.rows
                    this.getSpanArr(this.dataSource);
                }
            })
        },
        methods: {

            /* handleCurrentChange(pagination, filters, sorter) {
              this.loading = true;
              this.queryParam.pageNo = pagination.current;
              this.queryParam.pageSize = pagination.pageSize;
              getReportAll({
                pageNo: this.queryParam.pageNo, pageSize: this.queryParam.pageSize,
                schemeDescr: this.queryParam.name == undefined ? '' : this.queryParam.name.replace(/，/g, "%"),
                CENTER_TYPE_ID: this.$route.name === 'report-dataReport' ? '001' : '002'//模块类型
              }).then(res => {
                if (res.result === 'success') {
                  this.loading = false;
                  this.dataSource = res.rows;
                }
              })
            }, */
            handleExport() {
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    ACOUNT_SUBJECT: obj.ACOUNT_SUBJECT,
                    GUOKU_DSCR: obj.GUOKU_DSCR,
                    GUOKU_ID: obj.GUOKU_ID,
                    UNIT: obj.UNIT,
                    UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                    LEVEL: obj.LEVEL,
                    LEVEL_NAME: obj.LEVEL_NAME,
                    EXPORT_FLAG: '2'
                }
                console.log(param);
                window.location.href = `${
                    window._CONFIG['domianURL']
                    }/reportcenter/InventoryReport/exportXls?params=${encodeURIComponent(
                    JSON.stringify(param)
                )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
            },
            getSpanArr(data) {
                for (var i = 0; i < data.length; i++) {
                    if (i === 0) {
                        this.spanArr.push(1);
                        this.pos = 0
                    } else {
                        // 判断当前元素与上一个元素是否相同
                        if (data[i].D_ACCT === data[i - 1].D_ACCT) {
                            this.spanArr[this.pos] += 1;
                            this.spanArr.push(0);
                        } else {
                            this.spanArr.push(1);
                            this.pos = i;
                        }
                    }
                }
            },
            objectSpanMethod({row, column, rowIndex, columnIndex}) {
                if (columnIndex === 0) {
                    const _row = this.spanArr[rowIndex];
                    const _col = _row > 0 ? 1 : 0;
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                }
            }

        }
    }
</script>

<style scoped>
    .page {
        float: right;
        margin-top: 10px;
    }
</style>