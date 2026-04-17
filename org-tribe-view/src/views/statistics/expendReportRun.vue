<template>
    <a-card :bordered="false" class="card-area">
        <a-button v-if="dataSource.length>0" @click="handleExport">导出</a-button>
        <a-button style="margin-left: 8px" @click="$router.go(-1)">返回上层</a-button>
        <h1 style="text-align:center;">{{title1}}法人机构汇总支出表</h1>
        <!-- <a-icon v-if="dataSource.length>0" type="download" style="font-size: 25px;position:absolute;right: 40px;top: 40px;cursor: pointer;"
                      @click="handleExport" title="下载"/> -->
        <!-- table区域-begin -->
        <div style="width: 70%;margin: 0 auto;">
            <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}" style="width: 100%"
                      border>
                <el-table-column label="法人机构种类" width="500"
                                 align="center" prop="MECHA_TYPE_NAME">
                </el-table-column>
                <el-table-column label="金额" header-align="center" align="right">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
                    @click="openInfoModal(scope.row)"
            >{{scope.row.F_AMT}}</a>
          </span>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- <el-pagination class="page"
        @current-change="handleCurrentChange"
        :page-size="pageSize"
        layout="total, prev, pager, next, jumper"
        :total="total"
        :current-page="pageNo">
      </el-pagination> -->
        <!-- table区域-end -->
    </a-card>
</template>

<script>
    /* import {ListMixin} from '@/mixins/ListMixin' */
    import {
        getReportAll, getGuokuTree, queryJurisdictionData, queryCorporationExpense
    } from '@/api/nationalTreasury'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'

    export default {
        name: 'expendReportRun',
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
                dataSource: [
                    /* {
                        ORG_TYPE:'专户',
                        SUBJECT_DSCR_4: '一般公共服务支出',
                        ORG_MONEY:'1,000,000.00',
                    },
                    {
                        ORG_TYPE:'财政局',
                        SUBJECT_DSCR_4: '一般公共服务支出',
                        ORG_MONEY:'1,000,000.00',
                    }, */
                ],

            }
        },
        watch: {
            $route(val) {
                if (val.name == "statistics-expendReportRun") {
                    console.log(this.$route.query.AMT_ATTR)
                    let obj = JSON.parse(this.$route.query.query);
                    this.title1 = obj.S_FUNCSBTNAME;
                    this.loading = true;
                    let param = {
                        D_ACCT: obj.D_ACCT,
                        D_TYPE: obj.D_TYPE,
                        S_TRECODE: obj.S_TRECODE,
                        JURISDICTION: obj.JURISDICTION,
                        UNIT: obj.UNIT,
                        S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                        AMT_ATTR: this.$route.query.AMT_ATTR,
                        pageNo: this.pageNo,
                        pageSize: this.pageSize
                    }
                    this.spanArr = [];
                    queryCorporationExpense(param).then(res => {
                        if (res.result === 'success') {
                            this.loading = false;
                            this.dataSource = res.rows
                            this.total = res.total
                        }
                    })
                }
            }
        },
        mounted() {
            let obj = JSON.parse(this.$route.query.query);
            this.title1 = obj.S_FUNCSBTNAME;
            console.log(this.$route.query.AMT_ATTR)
            let param = {
                D_ACCT: obj.D_ACCT,
                D_TYPE: obj.D_TYPE,
                S_TRECODE: obj.S_TRECODE,
                JURISDICTION: obj.JURISDICTION,
                UNIT: obj.UNIT,
                S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                AMT_ATTR: this.$route.query.AMT_ATTR,
                pageNo: this.pageNo,
                pageSize: this.pageSize
            }
            this.spanArr = [];
            queryCorporationExpense(param).then(res => {
                if (res.result === 'success') {
                    this.loading = false;
                    this.dataSource = res.rows
                    this.total = res.total
                }
            })
        },
        methods: {

            handleCurrentChange(page) {
                this.loading = true;
                this.pageNo = page;
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    D_TYPE: obj.D_TYPE,
                    S_TRECODE: obj.S_TRECODE,
                    JURISDICTION: obj.JURISDICTION,
                    UNIT: obj.UNIT,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    AMT_ATTR: this.$route.query.AMT_ATTR,
                    pageNo: this.pageNo,
                    pageSize: this.pageSize
                }
                this.spanArr = [];
                queryCorporationExpense(param).then(res => {
                    if (res.result === 'success') {
                        this.loading = false;
                        this.dataSource = res.rows
                        this.total = res.total
                    }
                })
            },
            //行点击
            openInfoModal(record) {
                this.$router.push({
                    path: '/statistics/expendReportRunTwo',
                    query: {
                        name: record.S_FUNCSBTNAME + record.MECHA_TYPE_NAME + '库存情况表',
                        query: JSON.stringify(record)
                    }
                })
            },
            handleExport() {
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    JURISDICTION: obj.JURISDICTION,
                    JURISDICTION_DSCR: obj.JURISDICTION === '0' ? '全辖' : '本级',
                    UNIT: obj.UNIT,
                    UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                    D_TYPE: obj.D_TYPE,
                    DATE_DSCR: obj.DATE_DSCR,
                    S_TRECODE: obj.S_TRECODE,
                    S_TRENAME: obj.S_TRENAME,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    S_FUNCSBTNAME: obj.S_FUNCSBTNAME,
                    EXPORT_FLAG: '1'
                }
                console.log(param);
                window.location.href = `${
                    window._CONFIG['domianURL']
                    }/reportcenter/expenseSubject/exportXls?params=${encodeURIComponent(
                    JSON.stringify(param)
                )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
            },


        }
    }
</script>

<style scoped>
    .page {
        float: right;
        margin-top: 10px;
    }
</style>