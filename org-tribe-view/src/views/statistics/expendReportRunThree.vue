<template>
    <a-card :bordered="false" class="card-area">
        <a-button v-if="dataSource.length>0" @click="handleExport">导出</a-button>
        <a-button style="margin-left: 8px" @click="$router.go(-1)">返回上层</a-button>
        <h1 style="text-align:center;">{{title1}}{{title2}}支出账户信息表</h1>
        <!-- <a-icon v-if="dataSource.length>0" type="download" style="font-size: 25px;position:absolute;right: 40px;top: 40px;cursor: pointer;"
                      @click="handleExport" title="下载"/> -->
        <!-- table区域-begin -->
        <a-spin :spinning="loading">
            <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}" style="width: 100%"
                      :span-method="arraySpanMethod" border>
                <el-table-column label="银行信息" width="400"
                                 align="center" prop="ORG_FLAG_NAME">
                </el-table-column>
                <el-table-column label="金额" header-align="center" align="right" prop="ORG_FLAG_F_AMT">
                </el-table-column>
                <el-table-column label="银行网点" width="300" header-align="center" align="left" prop="BANK_NAME">
                </el-table-column>
                <el-table-column label="金额" header-align="center" align="right" prop="BANK_F_AMT">
                </el-table-column>
                <el-table-column label="账户信息" header-align="center" align="left" prop="S_PAYERACCT">
                </el-table-column>
                <el-table-column label="金额" header-align="center" align="right" prop="F_AMT">
                </el-table-column>
            </el-table>
        </a-spin>
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
        getReportAll, getGuokuTree, queryJurisdictionData, queryExpenseDetails, queryExpenseAccount
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
                title2: '',
                rowMergeArrs: {}, // 包含需要一个或多个合并项信息的对象
                needMergeArr: ['ORG_FLAG_NAME', 'BANK_NAME'], // 有合并项的列
                dataSource: [
                    /* {
                        BANK_INFO:'中国工商银行',
                        ORG_MONEY:'1,000,000.00',
                        BANK_NET:'中国工商银行昆明正义支行',
                        ORG_MONEY_1:'1,000,000.00',
                        ACCOUNT_INFO:'10101',
                        ORG_MONEY_2:'1,000,000.00',
                    },
                    {
                        BANK_INFO:'中国工商银行',
                        ORG_MONEY:'1,000,000.00',
                        BANK_NET:'中国工商银行昆明正义支行',
                        ORG_MONEY_1:'1,000,000.00',
                        ACCOUNT_INFO:'10102',
                        ORG_MONEY_2:'1,000,000.00',
                    },
                    {
                        BANK_INFO:'中国工商银行',
                        ORG_MONEY:'1,000,000.00',
                        BANK_NET:'中国工商银行昆明正义支行',
                        ORG_MONEY_1:'1,000,000.00',
                        ACCOUNT_INFO:'10103',
                        ORG_MONEY_2:'1,000,000.00',
                    },
                    {
                        BANK_INFO:'中国工商银行',
                        ORG_MONEY:'1,000,000.00',
                        BANK_NET:'中国工商银行昆明北京路支行',
                        ORG_MONEY_1:'1,000,000.00',
                        ACCOUNT_INFO:'20101',
                        ORG_MONEY_2:'1,000,000.00',
                    } */
                ],

            }
        },
        watch: {
            $route(val) {
                if (val.name == "statistics-expendReportRunThree") {
                    let obj = JSON.parse(this.$route.query.query);
                    this.title1 = obj.S_FUNCSBTNAME;
                    this.title2 = obj.MECHA_NAME;
                    this.loading = true;
                    let param = {
                        D_ACCT: obj.D_ACCT,
                        D_TYPE: obj.D_TYPE,
                        S_TRECODE: obj.S_TRECODE,
                        JURISDICTION: obj.JURISDICTION,
                        AMT_ATTR: obj.AMT_ATTR,
                        UNIT: obj.UNIT,
                        S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                        MECHA_TYPE: obj.MECHA_TYPE,
                        MECHA_CODE: obj.MECHA_CODE,
                        pageNo: this.pageNo,
                        pageSize: this.pageSize,
                    }
                    this.spanArr = [];
                    queryExpenseAccount(param).then(res => {
                        this.loading = false;
                        if (res.result === 'success') {
                            this.dataSource = res.rows
                            this.total = res.total;
                            this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                        }
                    }).catch(err => {
                        this.loading = false
                    })
                }
            }
        },
        mounted() {
            this.loading = true;
            let obj = JSON.parse(this.$route.query.query);
            this.title1 = obj.S_FUNCSBTNAME;
            this.title2 = obj.MECHA_NAME;
            let param = {
                D_ACCT: obj.D_ACCT,
                D_TYPE: obj.D_TYPE,
                S_TRECODE: obj.S_TRECODE,
                AMT_ATTR: obj.AMT_ATTR,
                JURISDICTION: obj.JURISDICTION,
                UNIT: obj.UNIT,
                S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                MECHA_TYPE: obj.MECHA_TYPE,
                MECHA_CODE: obj.MECHA_CODE,
                pageNo: this.pageNo,
                pageSize: this.pageSize,
            }
            this.spanArr = [];
            queryExpenseAccount(param).then(res => {
                this.loading = false;
                if (res.result === 'success') {
                    this.dataSource = res.rows
                    this.total = res.total;
                    this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                }
            }).catch(err => {
                this.loading = false;
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
                    AMT_ATTR: obj.AMT_ATTR,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    MECHA_TYPE: obj.MECHA_TYPE,
                    MECHA_CODE: obj.MECHA_CODE,
                    pageNo: this.pageNo,
                    pageSize: this.pageSize,
                }
                queryExpenseAccount(param).then(res => {
                    this.loading = false;
                    if (res.result === 'success') {
                        this.dataSource = res.rows
                        this.total = res.total;
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                }).catch(err => {
                    this.loading = false;
                })
            },
            handleExport() {
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    DATE_DSCR: obj.DATE_DSCR,
                    D_TYPE: obj.D_TYPE,
                    S_TRECODE: obj.S_TRECODE,
                    AMT_ATTR: obj.AMT_ATTR,
                    JURISDICTION: obj.JURISDICTION,
                    JURISDICTION_DSCR: obj.JURISDICTION === '0' ? '全辖' : '本级',
                    UNIT: obj.UNIT,
                    UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                    EXPORT_FLAG: '3',
                    S_TRENAME: obj.S_TRENAME,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    S_FUNCSBTNAME: obj.S_FUNCSBTNAME,
                    MECHA_NAME: obj.MECHA_NAME,
                    MECHA_CODE: obj.MECHA_CODE,
                }
                console.log(param);
                window.location.href = `${
                    window._CONFIG['domianURL']
                    }/reportcenter/expenseSubject/exportXls?params=${encodeURIComponent(
                    JSON.stringify(param)
                )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
            },
            //合并
            arraySpanMethod({row, column, rowIndex, columnIndex}) {
                if (column.property === 'ORG_FLAG_NAME') return this.mergeAction('ORG_FLAG_NAME', rowIndex, column)
                if (column.property === 'ORG_FLAG_F_AMT') return this.mergeAction('ORG_FLAG_NAME', rowIndex, column)
                if (column.property === 'BANK_NAME') return this.mergeAction('BANK_NAME', rowIndex, column)
                if (column.property === 'BANK_F_AMT') return this.mergeAction('BANK_NAME', rowIndex, column)
                //if (column.property === 'S_BANKDSCR') return this.mergeAction('S_BANKDSCR_1', rowIndex, column);
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