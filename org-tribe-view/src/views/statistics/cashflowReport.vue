<template>
    <a-card :bordered="false" class="card-area">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper cashflowSearch">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                required
                                :validate-status="validateStatus2"
                                label="核算主体"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                        >
                            <span slot="help">{{ validateStatus2=='error'?'请选择核算主体':'&nbsp;&nbsp;' }}</span>
                            <a-tree-select
                                    showSearch
                                    labelInValue
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="bookorgTreeData"
                                    v-model="queryParam.S_BOOKORGCODE"
                                    placeholder="请选择核算主体"
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                :validate-status="validateStatus"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                        >
                            <span slot="label">周&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期</span>
                            <a-select placeholder="请选择周期" @change="changType" v-model="queryParam.D_TYPE">
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
                        <!-- <a-form-item
                            :label="label.name"
                            required
                            :labelCol="{span: 3}"
                            :wrapperCol="{span: 19, offset: 1}"
                            v-if="queryParam.D_TYPE === '0'"
                            :validate-status="validateStatus1||validateStatus6"

                          >
                            <span slot="help">{{ validateStatus1=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <span style="color:red;" slot="help">{{ validateStatus6=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                            <a-date-picker style="width: 46%"
                              placeholder="请选择日期" v-model="queryParam.startDate" /> ~ <a-date-picker style="width: 46%"
                              placeholder="请选择日期" v-model="queryParam.endDate" />
                          </a-form-item>
                          <a-form-item
                            required
                            :label="label.name"
                            :labelCol="{span: 3}"
                            :wrapperCol="{span: 19, offset: 1}"
                            v-if="queryParam.D_TYPE === '1'"
                            :validate-status="validateStatus1||validateStatus7"

                          >
                            <span slot="help">{{ validateStatus1=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <span slot="help" style="color:red;">{{ validateStatus7=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                            <a-month-picker
                              style="width: 46%"
                              placeholder="请选择月份"
                              v-model="queryParam.startDate"
                            /> ~
                            <a-month-picker
                              style="width: 46%"
                              placeholder="请选择月份"
                              v-model="queryParam.endDate"
                            />
                          </a-form-item>
                          <a-form-item
                            required
                            :label="label.name"
                            :labelCol="{span: 3}"
                            :wrapperCol="{span: 19, offset: 1}"
                            v-if="queryParam.D_TYPE === '3'"
                            :validate-status="validateStatus1||validateStatus8"

                          >
                            <span slot="help">{{ validateStatus2=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <span slot="help" style="color:red;">{{ validateStatus8=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                            <a-date-picker placeholder="请输入年度查询" mode="year" style="width: 46%"
                                         @panelChange="e=>{queryParam.startDate = e;yearOpen = false;}" format="YYYY"
                                         v-model="queryParam.startDate" :open="yearOpen" @focus="yearOpen = true"></a-date-picker> ~
                            <a-date-picker placeholder="请输入年度查询" mode="year" style="width: 46%"
                                         @panelChange="e=>{queryParam.endDate = e;yearOpen1 = false;}" format="YYYY"
                                         v-model="queryParam.endDate" :open="yearOpen1" @focus="yearOpen1 = true"></a-date-picker>
                          </a-form-item>
                          <a-form-item
                            required
                            :label="label.name"
                            :labelCol="{span: 3}"
                            :wrapperCol="{span: 19, offset: 1}"
                            v-if="queryParam.D_TYPE === '2'"
                            :validate-status="validateStatus1||validateStatus9"

                          >
                            <span slot="help">{{ validateStatus1=='error'?'请选择账期':'&nbsp;&nbsp;' }}</span>
                            <span slot="help" style="color:red;">{{ validateStatus9=='error'?'请重新选择':'&nbsp;&nbsp;' }}</span>
                            <data-month
                              :choseQuarter="startquarter"
                              :choseQuarter1="endquarter"
                              @startquarter="startquarter1"
                              @endquarter="endquarter1"
                            ></data-month>
                          </a-form-item> -->
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                label="金额属性"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.unitType" placeholder="请选择金额属性" style="width:100%;">
                                <a-select-option value="0">本期值</a-select-option>
                                <a-select-option value="1">累计值</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                :validate-status="validateStatus4"
                                :labelCol="{span: 5}"
                                :wrapperCol="{span: 18, offset: 1}"
                        >
                            <template slot="label">&nbsp;&nbsp;&nbsp;银行信息</template>
                            <span slot="help">{{ validateStatus4=='error'?'请选择银行信息':'&nbsp;&nbsp;' }}</span>
                            <a-select allowClear labelInValue show-search option-filter-prop="children" mode="multiple"
                                      :maxTagCount="1" v-model="queryParam.BANKCODE" placeholder="请选择银行信息"
                                      style="width:100%;">
                                <a-select-option :value="d.id" v-for="d in bankreeData" :key="d.id">{{d.label}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <a-form-item
                                :validate-status="validateStatus3"
                                label="金额单位"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <span slot="help">{{ validateStatus3=='error'?'请选择金额单位':'&nbsp;&nbsp;' }}</span>
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
              <a-button style="margin-left:8px;" @click="handleExport">导出</a-button>
            </a-col>
          </span>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align:center;">国库与商业银行资金流动情况表</h1>
        <!-- table区域-begin -->
        <el-table :data="dataSource"
                  :header-cell-style="{background:'#fafafa',color:'#606266'}"
                  style="width: 100%" :span-method="arraySpanMethod" border>
            <el-table-column label="银行信息" width="200" align="center" prop="S_BANKDSCR_1"></el-table-column>
            <el-table-column
                    label="流入金额"
                    header-align="center"
                    align="right"
                    prop="COME_AMT_1"
            ></el-table-column>
            <el-table-column label="流出金额" header-align="center" align="right" prop="GO_AMT_1"></el-table-column>
            <el-table-column prop="F_AMT_1" label="净流入" header-align="center" align="right"/>
            <el-table-column prop="S_BANKDSCR" label="银行网点" align="center" width="200"/>
            <el-table-column label="流入金额" header-align="center" align="right" prop="COME_AMT"></el-table-column>
            <el-table-column label="流出金额" header-align="center" align="right" prop="GO_AMT"></el-table-column>
            <el-table-column prop="F_AMT" label="净流入" header-align="center" align="right"/>
        </el-table>
        <!-- <el-pagination class="cashflowPage"
         @current-change="handleCurrentChange"
         :page-size="pageSize"
         layout="total, prev, pager, next, jumper"
         :total="total"
         :current-page="pageNo">
       </el-pagination> -->
        <a-pagination v-if="dataSource.length>0" class="cashflowPage" size="small" :current="current"
                      :page-size="pageSize"
                      :total="total" :show-total="showTotal" show-size-changer show-quick-jumper @change="onChange"
                      @showSizeChange="onShowSizeChange"/>
        <!-- table区域-end -->
    </a-card>
</template>

<script>
    /* import {ListMixin} from '@/mixins/ListMixin' */
    import {queryInventoryReport, getOrgTree, queryCapitalFlowData, getBankOrgSelect} from '@/api/nationalTreasury'
    /* import { getAction, postAction } from '@/api/manage'
    import { GetUrlParam } from '@/utils/request' */
    import dataMonth from './component/dataMonth'
    import dateQuarter from '@/views/statistics/component/dateQuarter'

    export default {
        name: 'stockReport',
        components: {
            dateQuarter
        },
        data() {
            return {
                label: {
                    name: '周' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '期',
                    name1: '国' + '\xa0\xa0\xa0\xa0\xa0\xa0\xa0\xa0' + '库'
                },
                yearOpen: false,
                yearOpen1: false,
                startquarter: '',
                endquarter: '',
                // 查询条件
                queryParam: {
                    D_TYPE: '0',
                    unit: '10000',
                    unitType: '0'
                },
                loading: false,
                bookorgTreeData: [],
                bankreeData: [],
                rowMergeArrs: {}, // 包含需要一个或多个合并项信息的对象
                needMergeArr: ['S_BANKDSCR_1'], // 有合并项的列
                /* 分页参数 */
                //pageSize: 10,
                //pageNo: 1,
                //total: 0,
                obj: {},
                //currentPage: 1,
                //totals:[],
                current: 1,
                pageSize: 10,
                showTotal: (total, range) => {
                    return ' 共' + total + '条'
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0,
                showSummary: true,
                spanArr: [],
                validateStatus: '',
                validateStatus1: '',
                validateStatus2: '',
                validateStatus3: '',
                validateStatus4: '',
                validateStatus6: '',
                validateStatus7: '',
                validateStatus8: '',
                validateStatus9: '',
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
                ]
            }
        },
        created() {
            //核算主体
            getOrgTree({
                INSPECTION_GUOKU: this.$sessionStorage.ls.get('Login_Userinfo').guokuId
            }).then(res => {
                if (res.result === 'success') {
                    this.bookorgTreeData = []
                    this.bookorgTreeData = res.rows
                }
            })
            getBankOrgSelect({}).then(res => {
                if (res.result === 'success') {
                    this.bankreeData = []
                    this.bankreeData = res.rows
                }
            })
            //this.searchQuery();
        },
        mounted() {
            //this.getSpanArr(this.dataSource);
        },
        watch: {
            /* 'queryParam.endDate'(val){
              if(this.queryParam.D_TYPE === '1'){
                 let aa = new Date(this.queryParam.startDate).getTime()
                 let bb = new Date(this.queryParam.endDate).getTime()
                 if(this.queryParam.startDate!==''||this.queryParam.startDate!==''){
                 if(parseInt(aa)>parseInt(bb)){
                   this.queryParam.startDate = '';
                   this.queryParam.endDate = '';
                   this.validateStatus7 = 'error'
                 }else{
                   this.validateStatus7 = ''
                 }
              }
              }else if(this.queryParam.D_TYPE === '0'){
                 console.log(new Date(this.queryParam.startDate).getTime())
                 let aa = new Date(this.queryParam.startDate).getTime()
                 let bb = new Date(this.queryParam.endDate).getTime()
                 if(this.queryParam.startDate!==''||this.queryParam.startDate!==''){
                 if(parseInt(aa)>parseInt(bb)){
                   this.queryParam.startDate = '';
                   this.queryParam.endDate = '';
                   this.validateStatus6 = 'error'
                 }else{
                   this.validateStatus6 = ''
                 }
                 }
            }else if(this.queryParam.D_TYPE === '3'){
                 let aa = new Date(this.queryParam.startDate).getTime()
                 let bb = new Date(this.queryParam.endDate).getTime()
                 if(this.queryParam.startDate!==''||this.queryParam.startDate!==''){
                 if(parseInt(aa)>parseInt(bb)){
                   this.queryParam.startDate = '';
                   this.queryParam.endDate = '';
                   this.validateStatus8 = 'error'
                 }else{
                   this.validateStatus8 = ''
                 }
                 }
            }
            },
            'endquarter'(val){
                let aa = this.startquarter.replace('-', '').replace('Q', '')
                let bb = this.endquarter.replace('-', '').replace('Q', '')
                if(this.startquarter!==''||this.endquarter!==''){
                if(parseInt(aa)>parseInt(bb)){
                   this.startquarter = '';
                   this.endquarter = '';
                   this.validateStatus9 = 'error'
                 }else{
                   this.validateStatus9 = ''
                 }
                 }
            }, */
        },
        methods: {
            /* startquarter1(msg) {
              this.startquarter = msg
            },
            endquarter1(msg) {
              this.endquarter = msg
            }, */
            changType() {
                /* this.queryParam = {
                    D_ACCT:''
                } */
            },
            //查询
            searchQuery() {
                if (this.queryParam.D_TYPE === '0') {
                    if (!this.queryParam.D_ACCT) {
                        this.validateStatus1 = 'error';
                        return
                    } else {
                        this.validateStatus1 = 'success'
                    }
                }
                if (this.queryParam.D_TYPE === '1') {
                    if (!this.queryParam.D_ACCT) {
                        this.validateStatus1 = 'error';
                        return
                    } else {
                        this.validateStatus1 = 'success'
                    }
                } else if (this.queryParam.D_TYPE === '2') {
                    if (!this.queryParam.D_ACCT) {
                        this.validateStatus1 = 'error';
                        return
                    } else {
                        this.validateStatus1 = 'success'
                    }
                } else if (this.queryParam.D_TYPE === '3') {
                    if (!this.queryParam.D_ACCT) {
                        this.validateStatus1 = 'error';
                        return
                    } else {
                        this.validateStatus1 = 'success'
                    }
                }
                if (!this.queryParam.S_BOOKORGCODE) {
                    this.validateStatus2 = 'error'
                    return
                } else {
                    this.validateStatus2 = 'success'
                }
                if (this.queryParam.D_TYPE === '0') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM-DD'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        //BOOKORG_DSCR:this.queryParam.S_BOOKORGCODE.label,
                        //BANK_NAME:this.queryParam.BANKCODE.key,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        //UNIT_DSCR:this.queryParam.unit==='1'?'元':(this.queryParam.unit==='10000'?'万元':'亿元'),
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '1') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '3') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT, //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                }

                console.log(this.obj)
                this.loading = true
                //this.spanArr = []
                queryCapitalFlowData(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        /*  this.totals = [{'S_BANKDSCR_1':''},{'COME_AMT_1':'1'},{'GO_AMT_1':'2'},
                         {'F_AMT_1':'3'},{'S_BANKDSCR':''},{'COME_AMT':'4'},{'GO_AMT':'5'},{'F_AMT':'6'}] */
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1'}
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1',"GO_AMT_1":'2'}
                        this.total = res.total;
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            getSummaries(param) {
                /* let vm = this;
                let sums = [];
                if (this.showSummary) {
                  var selectedColm = param.columns;
                  let newArray=[]
                  selectedColm.forEach(a => {
                      if(vm.totals[a.property]){
                          newArray.push(vm.total[a.property])
                          }else{newArray.push('')}
                    });
                  sums=newArray;
                  sums[0] = "合计";
                  sums[4] = "合计";
                  return sums;
                } */
            },

            onChange(page, pageSize) {
                this.loading = true;
                this.current = page
                if (this.queryParam.D_TYPE === '0') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM-DD'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: this.current,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '1') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: this.current,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '3') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: this.current,
                        pageSize: this.pageSize
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT, //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: this.current,
                        pageSize: this.pageSize
                    }
                }

                console.log(this.obj)
                this.loading = true
                //this.spanArr = []
                queryCapitalFlowData(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        /*  this.totals = [{'S_BANKDSCR_1':''},{'COME_AMT_1':'1'},{'GO_AMT_1':'2'},
                         {'F_AMT_1':'3'},{'S_BANKDSCR':''},{'COME_AMT':'4'},{'GO_AMT':'5'},{'F_AMT':'6'}] */
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1'}
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1',"GO_AMT_1":'2'}
                        this.total = res.total;
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            onShowSizeChange(current, pageSize) {
                this.loading = true;
                this.pageSize = pageSize
                this.current = 1
                if (this.queryParam.D_TYPE === '0') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM-DD'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '1') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else if (this.queryParam.D_TYPE === '3') {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT.format('YYYY'), //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                } else {
                    this.obj = {
                        //方案查询条件
                        D_ACCT: this.queryParam.D_ACCT, //账期起始日
                        D_TYPE: this.queryParam.D_TYPE,
                        S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                        UNIT: this.queryParam.unit,
                        UNIT_TYPE: this.queryParam.unitType,
                        S_BANKNO_1: this.queryParam.BANKCODE,
                        pageNo: 1,
                        pageSize: this.pageSize
                    }
                }

                console.log(this.obj)
                this.loading = true
                //this.spanArr = []
                queryCapitalFlowData(this.obj).then(res => {
                    if (res.result === 'success') {
                        this.loading = false
                        this.dataSource = res.rows
                        /*  this.totals = [{'S_BANKDSCR_1':''},{'COME_AMT_1':'1'},{'GO_AMT_1':'2'},
                         {'F_AMT_1':'3'},{'S_BANKDSCR':''},{'COME_AMT':'4'},{'GO_AMT':'5'},{'F_AMT':'6'}] */
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1'}
                        //this.totals = {'S_BANKDSCR_1':'','COME_AMT_1':'1',"GO_AMT_1":'2'}
                        this.total = res.total;
                        this.rowMergeArrs = this.rowMergeHandle(this.needMergeArr, this.dataSource)
                    }
                })
            },
            searchReset() {
                this.queryParam = {
                    D_TYPE: '0',
                    unit: '10000',
                    unitType: '0',
                    //D_ACCT:'',
                }
                this.startquarter = '';
                this.endquarter = '';
                this.current = 1;
                this.total = 0;
                this.dataSource = [];
                //this.searchQuery();
            },
            handleExport() {
                if (this.dataSource.length > 0) {
                    if (this.queryParam.D_TYPE === '0') {
                        if (!this.queryParam.D_ACCT) {
                            this.validateStatus1 = 'error';
                            return
                        } else {
                            this.validateStatus1 = 'success'
                        }
                    }
                    if (this.queryParam.D_TYPE === '1') {
                        if (!this.queryParam.D_ACCT) {
                            this.validateStatus1 = 'error';
                            return
                        } else {
                            this.validateStatus1 = 'success'
                        }
                    } else if (this.queryParam.D_TYPE === '2') {
                        if (!this.queryParam.D_ACCT) {
                            this.validateStatus1 = 'error';
                            return
                        } else {
                            this.validateStatus1 = 'success'
                        }
                    } else if (this.queryParam.D_TYPE === '3') {
                        if (!this.queryParam.D_ACCT) {
                            this.validateStatus1 = 'error';
                            return
                        } else {
                            this.validateStatus1 = 'success'
                        }
                    }
                    if (!this.queryParam.S_BOOKORGCODE) {
                        this.validateStatus2 = 'error'
                        return
                    } else {
                        this.validateStatus2 = 'success'
                    }
                    if (this.queryParam.D_TYPE === '0') {
                        this.obj = {
                            //方案查询条件
                            D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM-DD'), //账期起始日
                            D_TYPE: this.queryParam.D_TYPE,
                            S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                            UNIT: this.queryParam.unit,
                            UNIT_TYPE: this.queryParam.unitType,
                            S_BANKNO_1: this.queryParam.BANKCODE,
                            BOOKORG_DSCR: this.queryParam.S_BOOKORGCODE.label,
                            UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                        }
                    } else if (this.queryParam.D_TYPE === '1') {
                        this.obj = {
                            //方案查询条件
                            D_ACCT: this.queryParam.D_ACCT.format('YYYY-MM'), //账期起始日
                            D_TYPE: this.queryParam.D_TYPE,
                            S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                            UNIT: this.queryParam.unit,
                            UNIT_TYPE: this.queryParam.unitType,
                            S_BANKNO_1: this.queryParam.BANKCODE,
                            BOOKORG_DSCR: this.queryParam.S_BOOKORGCODE.label,
                            UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                        }
                    } else if (this.queryParam.D_TYPE === '3') {
                        this.obj = {
                            //方案查询条件
                            D_ACCT: this.queryParam.D_ACCT.format('YYYY'), //账期起始日
                            D_TYPE: this.queryParam.D_TYPE,
                            S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                            UNIT: this.queryParam.unit,
                            UNIT_TYPE: this.queryParam.unitType,
                            S_BANKNO_1: this.queryParam.BANKCODE,
                            BOOKORG_DSCR: this.queryParam.S_BOOKORGCODE.label,
                            UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                        }
                    } else {
                        this.obj = {
                            //方案查询条件
                            D_ACCT: this.queryParam.D_ACCT, //账期起始日
                            D_TYPE: this.queryParam.D_TYPE,
                            S_BOOKORGCODE: this.queryParam.S_BOOKORGCODE.value,
                            UNIT: this.queryParam.unit,
                            UNIT_TYPE: this.queryParam.unitType,
                            S_BANKNO_1: this.queryParam.BANKCODE,
                            BOOKORG_DSCR: this.queryParam.S_BOOKORGCODE.label,
                            UNIT_DSCR: this.queryParam.unit === '1' ? '元' : (this.queryParam.unit === '10000' ? '万元' : '亿元'),
                        }
                    }
                    window.location.href = `${
                        window._CONFIG['domianURL']
                        }/reportcenter/capitalFlow/exportXls?params=${encodeURIComponent(
                        JSON.stringify(this.obj)
                    )}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`
                }
            },
            //合并
            arraySpanMethod({row, column, rowIndex, columnIndex}) {
                if (column.property === 'S_BANKDSCR_1') return this.mergeAction('S_BANKDSCR_1', rowIndex, column)
                if (column.property === 'COME_AMT_1') return this.mergeAction('S_BANKDSCR_1', rowIndex, column)
                if (column.property === 'GO_AMT_1') return this.mergeAction('S_BANKDSCR_1', rowIndex, column)
                if (column.property === 'F_AMT_1') return this.mergeAction('S_BANKDSCR_1', rowIndex, column)
                //if (column.property === 'S_BANKDSCR') return this.mergeAction('S_BANKDSCR_1', rowIndex, column);
            }
        }
    }
</script>

<style>
    /* .page {
      float: right;
      margin-top: 10px;
    } */
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