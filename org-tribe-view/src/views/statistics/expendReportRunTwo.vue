<template>
    <a-card :bordered="false" class="card-area">
        <div class="table-page-search-wrapper search2 cashflowSearch">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="8" :sm="8">
                        <a-form-item
                                label="法人机构名称"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                        >
                            <a-input v-model="queryParam.keyWord" placeholder="请输入关键字" style="width:100%;"/>
                        </a-form-item>
                    </a-col>
                    <a-col :md="8" :sm="8">
                        <a-form-item
                                label="法人机构类别"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.ORG_TYPES" placeholder="请选择法人机构类别" style="width:100%;">
                                <a-select-option value="0">全部</a-select-option>
                                <a-select-option value="1">特别关注</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a-button style="margin-left: 8px" @click="handleExport">导出</a-button>
              <a-button style="margin-left: 8px" @click="$router.go(-1)">返回上层</a-button>
            </a-col>
          </span>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align:center;">{{title1}}{{title2}}支出明细表</h1>
        <!-- table区域-begin -->
        <div style="width: 70%;margin: 0 auto;">
            <a-spin :spinning="loading">
                <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}"
                          style="width: 100%"
                          border>
                    <el-table-column label="法人机构编码" width="300"
                                     header-align="center" align="left" prop="MECHA_CODE">
                    </el-table-column>
                    <el-table-column label="法人机构名称"
                                     align="center">
                        <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
                    @click="openInfoModal(scope.row)"
            >{{scope.row.MECHA_NAME}}</a>
          </span>
                        </template>
                    </el-table-column>
                    <el-table-column label="金额" header-align="center" align="right" width="300">
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
            </a-spin>
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
        getReportAll, getGuokuTree, queryJurisdictionData, queryExpenseDetails
    } from '@/api/nationalTreasury'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'

    export default {
        name: 'expendReportRunTwo',
        data() {
            return {
                loading: false,
                treeData: [],
                queryParam: {
                    ORG_TYPES: '0',
                },
                /* 分页参数 */
                pageSize: 10,
                pageNo: 1,
                total: 0,
                currentPage: 1,
                spanArr: [],
                title1: '',
                title2: '',
                dataSource: [
                    {
                        ORG_TYPE_CODE: '990754',
                        ORG_TYPE_DSCR: 'xxxx财政局xx专户',
                        SUBJECT_DSCR_4: '一般公共服务支出',
                        ORG_MONEY: '1,000,000.00',
                    },
                    {
                        ORG_TYPE_CODE: '99999',
                        ORG_TYPE_DSCR: 'xxxxxxxx户',
                        SUBJECT_DSCR_4: '一般公共服务支出',
                        ORG_MONEY: '1,000,000.00',
                    },
                ],

            }
        },
        watch: {
            $route(val) {
                if (val.name == "statistics-expendReportRunTwo") {
                    let obj = JSON.parse(this.$route.query.query);
                    this.title1 = obj.S_FUNCSBTNAME;
                    this.title2 = obj.MECHA_TYPE;
                    this.loading = true;
                    let param = {
                        D_ACCT: obj.D_ACCT,
                        D_TYPE: obj.D_TYPE,
                        S_TRECODE: obj.S_TRECODE,
                        JURISDICTION: obj.JURISDICTION,
                        UNIT: obj.UNIT,
                        S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                        AMT_ATTR: obj.AMT_ATTR,
                        MECHA_NAME: obj.MECHA_NAME,
                        MECHA_CLASSFY: obj.MECHA_CLASSFY,
                        MECHA_TYPE: obj.MECHA_TYPE,
                        pageNo: this.pageNo,
                        pageSize: this.pageSize
                    }
                    this.spanArr = [];
                    queryExpenseDetails(param).then(res => {
                        this.loading = false;
                        if (res.result === 'success') {
                            this.dataSource = res.rows
                        }
                    }).catch(err => {
                        this.loading = false;
                    })
                }
            }
        },
        mounted() {
            this.loading = true;
            let obj = JSON.parse(this.$route.query.query);
            this.title1 = obj.S_FUNCSBTNAME;
            this.title2 = obj.MECHA_TYPE;
            let param = {
                D_ACCT: obj.D_ACCT,
                D_TYPE: obj.D_TYPE,
                S_TRECODE: obj.S_TRECODE,
                JURISDICTION: obj.JURISDICTION,
                UNIT: obj.UNIT,
                S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                AMT_ATTR: obj.AMT_ATTR,
                //S_FUNCSBTCODE: typeof(obj.S_FUNCSBTCODE) === "string"?new Array(obj.S_FUNCSBTCODE):obj.S_FUNCSBTCODE,
                MECHA_NAME: obj.MECHA_NAME,
                MECHA_CLASSFY: obj.MECHA_CLASSFY,
                MECHA_TYPE: obj.MECHA_TYPE,
                pageNo: this.pageNo,
                pageSize: this.pageSize
            }
            this.spanArr = [];
            queryExpenseDetails(param).then(res => {
                this.loading = false;
                if (res.result === 'success') {
                    this.dataSource = res.rows
                }
            }).catch(err => {
                this.loading = false;
            })
        },
        methods: {
            searchQuery() {
                this.loading = true;
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    D_TYPE: obj.D_TYPE,
                    S_TRECODE: obj.S_TRECODE,
                    JURISDICTION: obj.JURISDICTION,
                    MECHA_TYPE: obj.MECHA_TYPE,
                    AMT_ATTR: obj.AMT_ATTR,
                    UNIT: obj.UNIT,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    MECHA_NAME: this.queryParam.keyWord ? this.queryParam.keyWord : obj.MECHA_NAME,
                    MECHA_CLASSFY: this.queryParam.ORG_TYPES ? this.queryParam.ORG_TYPES : obj.MECHA_CLASSFY,
                    pageNo: this.pageNo,
                    pageSize: this.pageSize
                }
                queryExpenseDetails(param).then(res => {
                    this.loading = false;
                    if (res.result === 'success') {
                        this.dataSource = res.rows
                    }
                }).catch(err => {
                    this.loading = false;
                })
            },
            handleCurrentChange(page) {
                this.loading = true;
                this.queryParam.pageNo = page;
                /* getReportAll({
                  pageNo: this.queryParam.pageNo, pageSize: this.queryParam.pageSize,
                  schemeDescr: this.queryParam.name == undefined ? '' : this.queryParam.name.replace(/，/g, "%"),
                  CENTER_TYPE_ID: this.$route.name === 'report-dataReport' ? '001' : '002'//模块类型
                }).then(res => {
                  if (res.result === 'success') {
                    this.loading = false;
                    this.dataSource = res.rows;
                  }
                }) */
            },
            searchReset() {
                this.queryParam = {
                    ORG_TYPES: '0',
                }
            },
            //行点击
            openInfoModal(record) {
                this.$router.push({
                    path: '/statistics/expendReportRunThree',
                    query: {name: record.S_FUNCSBTNAME + record.MECHA_NAME + '支出账户信息表', query: JSON.stringify(record)}
                })
            },
            handleExport() {
                let obj = JSON.parse(this.$route.query.query);
                let param = {
                    D_ACCT: obj.D_ACCT,
                    JURISDICTION: obj.JURISDICTION,
                    JURISDICTION_DSCR: obj.JURISDICTION === '0' ? '全辖' : '本级',
                    UNIT: obj.UNIT,
                    AMT_ATTR: obj.AMT_ATTR,
                    UNIT_DSCR: obj.UNIT === '1' ? '元' : (obj.UNIT === '10000' ? '万元' : '亿元'),
                    D_TYPE: obj.D_TYPE,
                    DATE_DSCR: obj.DATE_DSCR,
                    S_TRECODE: obj.S_TRECODE,
                    S_TRENAME: obj.S_TRENAME,
                    S_FUNCSBTCODE: obj.S_FUNCSBTCODE,
                    S_FUNCSBTNAME: obj.S_FUNCSBTNAME,
                    MECHA_NAME: this.queryParam.keyWord,
                    MECHA_TYPE: obj.MECHA_TYPE,
                    MECHA_TYPE_NAME: obj.MECHA_TYPE_NAME,
                    MECHA_CLASSFY: this.queryParam.ORG_TYPES,
                    EXPORT_FLAG: '2'
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