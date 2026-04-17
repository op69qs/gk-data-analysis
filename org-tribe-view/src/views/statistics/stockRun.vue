<template>
    <a-card :bordered="false" class="card-area">
        <a-button v-if="dataSource.length>0" @click="handleExport">导出</a-button>
        <a-button style="margin-left: 8px" @click="$router.go(-1)">返回上层</a-button>
        <h1 style="text-align:center;">{{title1}}科目分级次库存情况表</h1>
        <!-- <a-icon v-if="dataSource.length>0" type="download" style="font-size: 25px;position:absolute;right: 40px;top: 40px;cursor: pointer;"
                      @click="handleExport" title="下载"/> -->
        <!-- table区域-begin -->
        <div style="width: 70%;margin: 0 auto">
            <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}"
                      @row-click="rowClick" style="width: 100%" :span-method="objectSpanMethod" border>
                <el-table-column label="账期" width="200"
                                 align="center" prop="D_ACCT">
                </el-table-column>
                <el-table-column label="级次" width="240" align="center" prop="LEVEL_NAME">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.LEVEL_NAME}}</a>
          </span>
                    </template>
                </el-table-column>
                <el-table-column label="流入" width="240" header-align="center" align="right" prop="F_AMT_INCM">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.F_AMT_INCM}}</a>
          </span>
                    </template>
                </el-table-column>
                <el-table-column prop="F_AMT_OUT" label="流出" width="240" header-align="center" align="right">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.F_AMT_OUT}}</a>
          </span>
                    </template>
                </el-table-column>
                <el-table-column prop="BALANCE" label="库存余额" header-align="center" align="right">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.BALANCE}}</a>
          </span>
                    </template>
                </el-table-column>
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
        getReportAll, getGuokuTree, queryJurisdictionData
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
                dataSource: [],

            }
        },
        watch: {
            $route(val) {
                if (val.name == "statistics-stockRun") {
                    let obj = JSON.parse(this.$route.query.querys);
                    this.title1 = val.query.ACOUNT_SUBJECT;
                    this.loading = true;
                    let param = {
                        D_ACCT: val.query.D_ACCT,
                        ACOUNT_SUBJECT: val.query.ACOUNT_SUBJECT,
                        GUOKU_DSCR: obj.GUOKU_DSCR,
                        GUOKU_ID: obj.GUOKU_ID,
                        UNIT: val.query.unit,
                        UNIT_DSCR: val.query.unit === '1' ? '元' : (val.query.unit === '10000' ? '万元' : '亿元'),
                        EXPORT_FLAG: '1'
                    }
                    this.spanArr = [];
                    queryJurisdictionData(param).then(res => {
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
            this.title1 = this.$route.query.ACOUNT_SUBJECT;
            let obj = JSON.parse(this.$route.query.querys);
            let param = {
                D_ACCT: this.$route.query.D_ACCT,
                ACOUNT_SUBJECT: this.$route.query.ACOUNT_SUBJECT,
                GUOKU_DSCR: obj.GUOKU_DSCR,
                GUOKU_ID: obj.GUOKU_ID,
                UNIT: this.$route.query.unit,
                UNIT_DSCR: this.$route.query.unit === '1' ? '元' : (this.$route.query.unit === '10000' ? '万元' : '亿元'),
                EXPORT_FLAG: '1'
            }
            this.spanArr = [];
            queryJurisdictionData(param).then(res => {
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
            //行点击
            rowClick(record, index) {
                //if(record.LEVEL_NAME!=='合计'){
                this.$router.push({
                    path: '/statistics/stockRunTwo',
                    query: {name: record.ACOUNT_SUBJECT + record.LEVEL_NAME + '库存情况表', query: JSON.stringify(record)}
                })
                // }
            },
            handleExport() {
                let obj = JSON.parse(this.$route.query.querys);
                let param = {
                    D_ACCT: this.$route.query.D_ACCT,
                    ACOUNT_SUBJECT: this.$route.query.ACOUNT_SUBJECT,
                    GUOKU_DSCR: obj.GUOKU_DSCR,
                    GUOKU_ID: obj.GUOKU_ID,
                    UNIT_DSCR: this.$route.query.unit === '1' ? '元' : (this.$route.query.unit === '10000' ? '万元' : '亿元'),
                    UNIT: this.$route.query.unit,
                    EXPORT_FLAG: '1'
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