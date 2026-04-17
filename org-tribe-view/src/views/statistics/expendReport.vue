<template>
    <a-card :bordered="false" class="card-area">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                required
                                :validate-status="validateStatus"
                                :label="label.name1"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                        >
                            <span slot="help">{{ validateStatus=='error'?'请选择国库':'&nbsp;&nbsp;' }}</span>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="treeData"
                                    v-model="queryParam.GUOKU_ID"
                                    placeholder="请选择国库"
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                :label="label.name"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.D_TYPE" placeholder="请选择周期" style="width:100%;">
                                <a-select-option value="0">日</a-select-option>
                                <a-select-option value="1">月</a-select-option>
                                <a-select-option value="2">季</a-select-option>
                                <a-select-option value="3">年</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                :validate-status="validateStatus1"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                                required
                        >
                            <span slot="help">{{ validateStatus1=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <span slot="label">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期</span>
                            <a-date-picker
                                    v-if="queryParam.D_TYPE === '0'"
                                    style="width: 100%;"
                                    placeholder="请选择日"
                                    format="YYYY-MM-DD"
                                    v-model="queryParam.D_ACCT"
                            ></a-date-picker>
                            <a-month-picker
                                    v-else-if="queryParam.D_TYPE === '1'"
                                    style="width: 100%;"
                                    placeholder="请选择月"
                                    format="YYYY-MM"
                                    v-model="queryParam.D_ACCT"
                            ></a-month-picker>
                            <date-quarter
                                    v-else-if="queryParam.D_TYPE === '2'"
                                    @value="value => {queryParam.D_ACCT= value;}"
                            ></date-quarter>
                            <a-date-picker
                                    v-else-if="queryParam.D_TYPE === '3'"
                                    style="width: 100%;"
                                    placeholder="请选择年"
                                    mode="year"
                                    @panelChange="e=>{queryParam.D_ACCT = e;yearOpen = false;}"
                                    format="YYYY"
                                    v-model="queryParam.D_ACCT"
                                    :open="yearOpen"
                                    @focus="yearOpen = true"
                            ></a-date-picker>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                label="辖属标志"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.JURISDICTION" placeholder="请选择辖属标志" style="width:100%;">
                                <a-select-option value="0">本级</a-select-option>
                                <a-select-option value="1">全辖</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <template slot="label">&nbsp;&nbsp;&nbsp;预算科目</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    multiple
                                    :treeData="subjectData2"
                                    v-model="queryParam.S_BDGSBTCODE"
                                    placeholder="请选择预算科目"
                                    :maxTagCount="1"
                                    allowClear
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                label="金额单位"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.unit" placeholder="请选择金额单位" style="width:100%;">
                                <a-select-option value="1">元</a-select-option>
                                <a-select-option value="10000">万元</a-select-option>
                                <a-select-option value="100000000">亿元</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
              <a-button style="margin-left: 8px" @click="handleExport">导出</a-button>
            </a-col>
          </span>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align:center;">支出科目报表</h1>
        <!-- table区域-begin -->
        <el-table :data="dataSource" :header-cell-style="{background:'#fafafa',color:'#606266'}" style="width: 100%"
                  border>
            <el-table-column label="账期" width="200" align="center" prop="D_ACCT"></el-table-column>
            <el-table-column label="国库" width="240" align="center" prop="S_TRENAME"></el-table-column>
            <el-table-column
                    header-align="center"
                    align="left"
                    prop="S_FUNCSBTCODE"
                    label="科目代码"
                    width="180"
            ></el-table-column>
            <el-table-column header-align="center" align="left" prop="S_FUNCSBTNAME" label="科目名称"></el-table-column>
            <el-table-column label="本期发生额" header-align="center" align="right">
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
            <el-table-column
                    v-if="status"
                    label="本年累计"
                    header-align="center"
                    align="right"
            >
                <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
                    @click="openInfoModal1(scope.row)"
            >{{scope.row.YEAR_F_AMT}}</a>
          </span>
                </template>
            </el-table-column>
            <el-table-column prop="TCBS_F_AMT" label="发生额与TCBS系统差值" header-align="center" align="right"/>
            <el-table-column
                    v-if="status"
                    prop="TCBS_YAER_F_AMT"
                    label="年累计与TCBS系统差值"
                    header-align="center"
                    align="right"
            />
        </el-table>
        <!-- <el-pagination
          class="expendReportpage"
          @current-change="handleCurrentChange"
          :page-size="pageSize"
          layout="total, prev, pager, next, jumper"
          :total="total"
          :current-page="pageNo"
        ></el-pagination> -->
        <a-pagination v-if="dataSource.length>0" size="small" class="cashflowPage" :current="current"
                      :page-size="pageSize" :total="total" :show-total="showTotal" show-size-changer show-quick-jumper
                      @change="onChange" @showSizeChange="onShowSizeChange"/>
        <!-- table区域-end -->
    </a-card>
</template>

<script>
    /* import {ListMixin} from '@/mixins/ListMixin' */
    import {
        queryInventoryReport,
        getGuokuTree,
        getPayoutSubject,
        queryExpenseReport,
        getBudgetSubjectSelect
    } from '@/api/nationalTreasury'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'
    import dateQuarter from '@/views/statistics/component/dateQuarter'

    const SHOW_PARENT = SHOW_PARENT
    export default {
        name: 'expendReport',
        components: {
            dateQuarter
        },
        data() {
            return {
                label: {
                    name: '周' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '期',
                    name1: '国' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '库'
                },
                yearOpen: false,
                selectedKeys: '',
                SHOW_PARENT,
                // 查询条件
                queryParam: {
                    JURISDICTION: '0',
                    D_TYPE: '0',
                    unit: '1'
                },
                rowMergeArrs: {}, // 包含需要一个或多个合并项信息的对象
                needMergeArr: ['D_ACCT'], // 有合并项的列
                loading: false,
                treeData: [],
                subjectData2: [],
                status: true,
                /* 分页参数 */
                //pageSize: 10,
                //pageNo: 1,
                //total: 0,
                //currentPage: 1,
                current: 1,
                pageSize: 10,
                showTotal: (total, range) => {
                    return ' 共' + total + '条'
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
                obj: {},
                spanArr: [],
                changeTitle: '本期发生额',
                validateStatus: '',
                validateStatus1: '',
                validateStatus2: '',
                dataSource: [
                    /* {
                      D_ACCT: '2020-01-01',
                      GUOKU_DSCR: '昆明中支',
                      S_FUNCSBTCODE: '271',
                      SUBJECT_DSCR_4: '一般公共服务支出',
                      F_AMT: '100,000.00',
                      YEAR_F_AMT: '100,000.00',
                      TCBS_F_AMT:'0.00',
                      TCBS_YEAR_AMT:'0.00'
                    }, {
                      D_ACCT: '2020-01-02',
                      GUOKU_DSCR: '昆明中支',
                      S_FUNCSBTCODE: '272',
                      SUBJECT_DSCR_4: '人大事务',
                      F_AMT: '100,000.00',
                      YEAR_F_AMT: '100,000.00',
                      TCBS_F_AMT:'0.00',
                      TCBS_YEAR_AMT:'0.00'
                    } */
                ]
            }
        },
        created() {
            getGuokuTree({
                guoku_id: this.$sessionStorage.ls.get('Login_Userinfo').guokuId
            }).then(res => {
                if (res.result === 'success') {
                    this.treeData = res.rows
                }
            }),
                getBudgetSubjectSelect().then(res => {
                    if (res.result === 'success') {
                        this.subjectData2 = res.rows
                    }
                })
            //this.searchQuery();
        },
        methods: {
            //查询
            searchQuery() {
                if (!this.queryParam.GUOKU_ID) {
                    this.validateStatus = 'error'
                    return
                } else {
                    this.validateStatus = 'success'
                }
                if (this.queryParam.D_TYPE === '2') {
                    if (!this.queryParam.D_ACCT) {
                        this.validateStatus1 = 'error'
                        return
                    } else {
                        this.validateStatus1 = 'success'
                    }
                }
                /* if (!this.queryParam.S_BDGSBTCODE||this.queryParam.S_BDGSBTCODE.length===0) {
                  this.validateStatus2 = 'error'
                  return
                } else {
                  this.validateStatus2 = 'success'
                } */
                if (this.queryParam.D_TYPE === '0') {
                    this.changeTitle = '本期发生额（日发生额）'
                } else if (this.queryParam.D_TYPE === '1') {
                    this.changeTitle = '本期发生额（月发生额）'
                } else if (this.queryParam.D_TYPE === '2') {
                    this.changeTitle = '本期发生额（季发生额）'
                } else if (this.queryParam.D_TYPE === '3') {
                    this.changeTitle = '本期发生额（年发生额）'
                }
                this.loading = true
                if (this.queryParam.D_TYPE === '0') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM-DD'), //账期起始日
                        JURISDICTION: this.queryParam.JURISDICTION,
                        D_TYPE: this.queryParam.D_TYPE,
                        S_TRECODE: this.queryParam.GUOKU_ID,
                        UNIT: this.queryParam.unit,
                        S_FUNCSBTCODE: typeof (this.queryParam.S_BDGSBTCODE) === "string" ? this.queryParam.S_BDGSBTCODE.split('') : this.queryParam.S_BDGSBTCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '1') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM'), //账期起始日
                        JURISDICTION: this.queryParam.JURISDICTION,
                        D_TYPE: this.queryParam.D_TYPE,
                        S_TRECODE: this.queryParam.GUOKU_ID,
                        UNIT: this.queryParam.unit,
                        S_FUNCSBTCODE: this.queryParam.S_BDGSBTCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '3') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY'), //账期起始日
                        JURISDICTION: this.queryParam.JURISDICTION,
                        D_TYPE: this.queryParam.D_TYPE,
                        S_TRECODE: this.queryParam.GUOKU_ID,
                        UNIT: this.queryParam.unit,
                        S_FUNCSBTCODE: this.queryParam.S_BDGSBTCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT, //账期起始日
                        JURISDICTION: this.queryParam.JURISDICTION,
                        D_TYPE: this.queryParam.D_TYPE,
                        S_TRECODE: this.queryParam.GUOKU_ID,
                        UNIT: this.queryParam.unit,
                        S_FUNCSBTCODE: this.queryParam.S_BDGSBTCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                }
                if (this.queryParam.D_TYPE !== '3') {
                    this.status = true
                } else {
                    this.status = false
                }
                queryExpenseReport(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        this.total = res.total
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            /* handleCurrentChange(page) {
              this.loading = true
              this.obj.pageNo = page
              queryExpenseReport(this.obj).then(res => {
                if (res.result === 'success') {
                  this.loading = false
                  this.dataSource = res.rows
                  this.total = res.total
                }
              })
            }, */
            onChange(page, pageSize) {
                this.current = page
                this.obj.pageNo = this.current
                if (this.queryParam.D_TYPE !== '3') {
                    this.status = true
                } else {
                    this.status = false
                }
                queryExpenseReport(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        this.total = res.total
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            onShowSizeChange(current, pageSize) {
                this.obj.pageSize = pageSize
                this.obj.pageNo = 1
                this.current = 1
                if (this.queryParam.D_TYPE !== '3') {
                    this.status = true
                } else {
                    this.status = false
                }
                queryExpenseReport(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        this.total = res.total
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            openInfoModal(record) {
                this.$router.push({
                    path: '/statistics/expendReportRun',
                    query: {name: record.S_FUNCSBTNAME + '法人机构汇总支出表', query: JSON.stringify(record), AMT_ATTR: '0'}
                })
            },
            openInfoModal1(record) {
                this.$router.push({
                    path: '/statistics/expendReportRun',
                    query: {name: record.S_FUNCSBTNAME + '法人机构汇总支出表', query: JSON.stringify(record), AMT_ATTR: '1'}
                })
            },
            searchReset() {
                this.queryParam = {
                    JURISDICTION: '0',
                    D_TYPE: '0',
                    unit: '1'
                }
                this.current = 1;
                this.total = 0;
                this.dataSource = [];
                //this.searchQuery();
            },
            handleExport() {
                if (this.dataSource.length > 0) {
                    this.obj.UNIT_DSCR = this.obj.UNIT === '1' ? '元' : (this.obj.UNIT === '10000' ? '万元' : '亿元'),
                        this.obj.JURISDICTION_DSCR = this.obj.JURISDICTION === '0' ? '全辖' : '本级',
                        this.obj.EXPORT_FLAG = '0'
                    console.log(this.obj)
                    window.location.href = `${
                        window._CONFIG['domianURL']
                        }/reportcenter/expenseSubject/exportXls?params=${encodeURIComponent(
                        JSON.stringify(this.obj)
                    )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
                }
            },
            //合并
            arraySpanMethod({row, column, rowIndex, columnIndex}) {
                if (column.property === 'D_ACCT') return this.mergeAction('D_ACCT', rowIndex, column)
                if (column.property === 'S_TRENAME') return this.mergeAction('D_ACCT', rowIndex, column)
                //if (column.property === 'S_BANKDSCR') return this.mergeAction('S_BANKDSCR_1', rowIndex, column);
            }
        }
    }
</script>

<style>
    .cashflowSearch .el-input__inner {
        height: 32px;
        line-height: 32px;
    }

    .cashflowSearch .el-input__icon {
        line-height: 32px;
    }

    .cashflowPage {
        float: right;
        margin-top: 10px;
    }
</style>