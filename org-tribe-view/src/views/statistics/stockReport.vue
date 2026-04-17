<template>
    <a-card :bordered="false" class="card-area">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item required :validate-status="validateStatus1" :label="label.name1"
                                     :labelCol="{span: 5}"
                                     :wrapperCol="{span: 18, offset: 1}">
                            <span slot="help">{{ validateStatus1=='error'?'请选择国库':'&nbsp;&nbsp;' }}</span>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    labelInValue
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="treeData"
                                    v-model="queryParam.GUOKU_ID"
                                    placeholder="请选择国库"
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item :validate-status="validateStatus" :label="label.name" :labelCol="{span: 5}"
                                     :wrapperCol="{span: 18, offset: 1}" required>
                            <span slot="help">{{ validateStatus=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <a-range-picker
                                    style="width: 100%"
                                    v-model="queryParam.CHECK_DATE"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item :validate-status="validateStatus2" label="金额单位" :labelCol="{ span: 5 }"
                                     :wrapperCol="{ span: 18, offset: 1 }">
                            <span slot="help">{{ validateStatus2=='error'?'请选择金额单位':'&nbsp;&nbsp;' }}</span>
                            <a-select
                                    v-model="queryParam.unit"
                                    placeholder="请选择金额单位"
                                    style="width:100%;"
                            >
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
              <a-button style="margin-left:8px;" @click="handleExport">导出</a-button>
            </a-col>
          </span>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align:center;">库存报表</h1>
        <!-- table区域-begin -->
        <div style="width: 70%;margin:0 auto">
            <el-table :data="dataSource"
                      :header-cell-style="{background:'#fafafa',color:'#606266'}" @row-click="rowClick"
                      style="width: 100%"
                      border :span-method="objectSpanMethod">
                <el-table-column label="账期" width="200"
                                 align="center" prop="D_ACCT">
                </el-table-column>
                <el-table-column label="会计科目" width="200" align="center" prop="ACOUNT_SUBJECT">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.ACOUNT_SUBJECT}}</a>
          </span>
                    </template>
                </el-table-column>
                <el-table-column label="流入" width="200" header-align="center" align="right" prop="F_AMT_INCM">
                    <template slot-scope="scope">
          <span>
            <a
                    href="javascript:;"
                    class="link"
            >{{scope.row.F_AMT_INCM}}</a>
          </span>
                    </template>
                </el-table-column>
                <el-table-column prop="F_AMT_OUT" label="流出" width="200" header-align="center" align="right">
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
        <!-- <el-pagination class="page"
        @current-change="handleCurrentChange"
        :page-size="pageSize"
        :current-page="pageNo"
        layout="total, prev, pager, next, jumper"
        :total="total"
        >
      </el-pagination> -->
        <div class="page">
            <!-- <el-pagination
              @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      :current-page="pageNo"
              :page-sizes="[12, 60, 120]"
              :page-size="pageSize"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="total">
                  </el-pagination> -->
            <a-pagination v-if="dataSource.length>0" size="small" :current="current" :pageSize="pageSize"
                          :page-size-options="pageSizeOptions" :total="total" :show-total="showTotal" show-size-changer
                          show-quick-jumper @change="onChange" @showSizeChange="onShowSizeChange"/>


        </div>
        <!-- table区域-end -->
    </a-card>
</template>

<script>
    /* import {ListMixin} from '@/mixins/ListMixin' */
    import {
        queryInventoryReport, getGuokuTree
    } from '@/api/nationalTreasury'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'

    export default {
        name: 'stockReport',
        data() {
            return {
                label: {
                    name: '账' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '期',
                    name1: '国' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '库'
                },
                // 查询条件
                queryParam: {
                    unit: '10000'
                },
                loading: false,
                treeData: [],
                /* 分页参数 */
                //pageNo: 1,
                //pageSize:12,
                //total: 0,
                //currentPage: 1,
                pageSizeOptions: ['12', '60', '120'],
                current: 1,
                pageSize: 12,
                showTotal: (total, range) => {
                    return ' 共' + total + '条'
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
                spanArr: [],
                validateStatus: '',
                validateStatus1: '',
                validateStatus2: '',
                dataSource: [
                    /* {
                  D_ACCT: '2020-01-01',
                  ACOUNT_SUBJECT: '271',
                  F_AMT_INCM: '100,000.00',
                  F_AMT_OUT: '100,000.00',
                  BALANCE: '100,000.00'
                }, {
                  D_ACCT: '2020-01-01',
                  ACOUNT_SUBJECT: '272',
                  F_AMT_INCM: '200,000.00',
                  F_AMT_OUT: '200,000.00',
                  BALANCE: '200,000.00'
                }, {
                  D_ACCT: '2020-01-01',
                  ACOUNT_SUBJECT: '合计',
                  F_AMT_INCM: '300,000.00',
                  F_AMT_OUT: '300,000.00',
                  BALANCE: '300,000.00'
                },{
                  D_ACCT: '2020-01-02',
                  ACOUNT_SUBJECT: '271',
                  F_AMT_INCM: '100,000.00',
                  F_AMT_OUT: '100,000.00',
                  BALANCE: '100,000.00'
                }, {
                  D_ACCT: '2020-01-02',
                  ACOUNT_SUBJECT: '272',
                  F_AMT_INCM: '200,000.00',
                  F_AMT_OUT: '200,000.00',
                  BALANCE: '200,000.00'
                }, {
                  D_ACCT: '2020-01-02',
                  ACOUNT_SUBJECT: '合计',
                  F_AMT_INCM: '300,000.00',
                  F_AMT_OUT: '300,000.00',
                  BALANCE: '300,000.00'
                } */
                ],
            }
        },
        created() {
            getGuokuTree({
                guoku_id: this.$sessionStorage.ls.get('Login_Userinfo').guokuId
            }).then(res => {
                if (res.result === 'success') {
                    this.treeData = res.rows
                }
            })
            //this.searchQuery();
        },
        mounted() {
            //this.getSpanArr(this.dataSource);
        },
        methods: {
            //查询
            searchQuery() {
                if (!this.queryParam.CHECK_DATE) {
                    this.validateStatus = 'error';
                    return
                } else {

                    this.validateStatus = 'success'
                }
                if (!this.queryParam.GUOKU_ID) {
                    this.validateStatus1 = 'error';
                    return
                } else {

                    this.validateStatus1 = 'success'
                }
                if (!this.queryParam.unit) {
                    this.validateStatus2 = 'error';
                    return
                } else {

                    this.validateStatus2 = 'success'
                }
                this.loading = true;
                if (this.queryParam.CHECK_DATE) {
                    this.queryParam.START_DATE = this.queryParam.CHECK_DATE[0].format('YYYY-MM-DD')
                    this.queryParam.END_DATE = this.queryParam.CHECK_DATE[1].format('YYYY-MM-DD')
                }
                this.spanArr = [];
                queryInventoryReport({
                    START_DATE: this.queryParam.START_DATE,
                    END_DATE: this.queryParam.END_DATE,
                    GUOKU_DSCR: this.queryParam.GUOKU_ID.label,
                    GUOKU_ID: this.queryParam.GUOKU_ID.value,
                    UNIT: this.queryParam.unit,
                    UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                    pageNo: 1, pageSize: this.pageSize,
                    EXPORT_FLAG: '0'
                }).then(res => {
                    if (res.result === 'success') {
                        this.loading = false;
                        this.dataSource = res.rows
                        this.total = res.total;
                        this.getSpanArr(this.dataSource);
                    }
                })
            },
            /* handleCurrentChange(page) {
              this.loading = true;
              this.pageNo = page;
              this.spanArr = [];
              queryInventoryReport({
                pageNo: this.pageNo,pageSize: this.pageSize,
                START_DATE: this.queryParam.START_DATE,
                END_DATE: this.queryParam.END_DATE,
                GUOKU_DSCR:this.queryParam.GUOKU_ID.label,
                GUOKU_ID: this.queryParam.GUOKU_ID.value,
                UNIT: this.queryParam.unit,
                UNIT_DSCR:this.queryParam.unit==='1'?'元':(this.queryParam.unit==='10000'?'万元':'亿元'),
                UNIT: this.queryParam.unit,
                EXPORT_FLAG:'0'
              }).then(res => {
                if (res.result === 'success') {
                  this.loading = false;
                  this.dataSource = res.rows;
                  this.total = res.total;
                  this.getSpanArr(this.dataSource);
                }
              })
            },
            handleSizeChange(pageSize){
            this.loading = true;
            this.pageSize = pageSize;
            this.spanArr = [];
            queryInventoryReport({
                pageNo: this.pageNo,pageSize: this.pageSize,
                START_DATE: this.queryParam.START_DATE,
                END_DATE: this.queryParam.END_DATE,
                GUOKU_DSCR:this.queryParam.GUOKU_ID.label,
                GUOKU_ID: this.queryParam.GUOKU_ID.value,
                UNIT: this.queryParam.unit,
                UNIT_DSCR:this.queryParam.unit==='1'?'元':(this.queryParam.unit==='10000'?'万元':'亿元'),
                EXPORT_FLAG:'0'
              }).then(res => {
                if (res.result === 'success') {
                  this.loading = false;
                  this.dataSource = res.rows;
                  this.total = res.total;
                  this.getSpanArr(this.dataSource);
                }
              })
            }, */
            onChange(page, pageSize) {
                this.loading = true;
                this.current = page;
                this.spanArr = [];
                queryInventoryReport({
                    pageNo: this.current, pageSize: this.pageSize,
                    START_DATE: this.queryParam.START_DATE,
                    END_DATE: this.queryParam.END_DATE,
                    GUOKU_DSCR: this.queryParam.GUOKU_ID.label,
                    GUOKU_ID: this.queryParam.GUOKU_ID.value,
                    UNIT: this.queryParam.unit,
                    UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                    UNIT: this.queryParam.unit,
                    EXPORT_FLAG: '0'
                }).then(res => {
                    if (res.result === 'success') {
                        this.loading = false;
                        this.dataSource = res.rows;
                        this.total = res.total;
                        this.getSpanArr(this.dataSource);
                    }
                })
            },
            onShowSizeChange(current, pageSize) {
                this.loading = true;
                this.pageSize = pageSize;
                this.current = 1
                this.spanArr = [];
                queryInventoryReport({
                    pageNo: 1, pageSize: this.pageSize,
                    START_DATE: this.queryParam.START_DATE,
                    END_DATE: this.queryParam.END_DATE,
                    GUOKU_DSCR: this.queryParam.GUOKU_ID.label,
                    GUOKU_ID: this.queryParam.GUOKU_ID.value,
                    UNIT: this.queryParam.unit,
                    UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                    EXPORT_FLAG: '0'
                }).then(res => {
                    if (res.result === 'success') {
                        this.loading = false;
                        this.dataSource = res.rows;
                        this.total = res.total;
                        this.getSpanArr(this.dataSource);
                    }
                })
            },
            //行点击
            rowClick(record, index) {
                //if(record.ACOUNT_SUBJECT!=='合计'){
                this.$router.push({
                    path: '/statistics/stockRun',
                    query: {
                        name: record.ACOUNT_SUBJECT + '科目分级次库存情况表',
                        query: record,
                        ACOUNT_SUBJECT: record.ACOUNT_SUBJECT,
                        D_ACCT: record.D_ACCT,
                        guoku: this.queryParam.GUOKU_ID,
                        unit: this.queryParam.unit,
                        querys: JSON.stringify(record)
                    }
                })
                //}
            },
            searchReset() {
                this.queryParam = {
                    unit: '10000',
                };
                this.current = 1;
                this.pageSize = 12;
                this.total = 0;
                this.dataSource = [];
                //this.searchQuery();
            },
            handleExport() {
                if (this.dataSource.length > 0) {
                    let param = {
                        pageNo: this.pageNo, pageSize: this.pageSize,
                        START_DATE: this.queryParam.START_DATE,
                        END_DATE: this.queryParam.END_DATE,
                        GUOKU_DSCR: this.queryParam.GUOKU_ID.label,
                        GUOKU_ID: this.queryParam.GUOKU_ID.value,
                        UNIT: this.queryParam.unit,
                        UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                        EXPORT_FLAG: '0'
                    }
                    console.log(param);
                    window.location.href = `${
                        window._CONFIG['domianURL']
                        }/reportcenter/InventoryReport/exportXls?params=${encodeURIComponent(
                        JSON.stringify(param)
                    )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
                }
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