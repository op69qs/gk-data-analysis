<template>
    <a-card :bordered="false" class="card-area">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper searchCenter">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    
                    <a-col :md="6" :sm="10">
                        <a-form-item required :labelCol="{ span: 7 }" :wrapperCol="{ span: 16, offset: 1 }">
                            <template slot="label">国&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    allowClear
                                    labelInValue
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="guoKuTree"
                                    v-model="queryParam.S_TRECODE"
                                    placeholder="请选择国库"
                                    @change="nameChange"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 7 }" :wrapperCol="{ span: 16, offset: 1 }">
                            <template slot="label">起始时间</template>
                            <a-date-picker
                                    style="width: 100%"
                                    placeholder="请选择起始时间"
                                    v-model="queryParam.S_STARTTIME"
                                    :disabled-date="disabledDate"
                                    format="YYYY-MM-DD"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 7 }" :wrapperCol="{ span: 16, offset: 1 }">
                            <template slot="label">结束时间</template>
                            <a-date-picker
                                    style="width: 100%"
                                    placeholder="请选择结束时间"
                                    v-model="queryParam.S_ENDTIME"
                                    :disabled-date="disabledDate"
                                    format="YYYY-MM-DD"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原支付方式</template>
                            <a-select v-model="queryParam.S_PAYMODE" placeholder="请选择支付方式">
                                <a-select-option value="1">直接支付</a-select-option>
                                <a-select-option value="2">授权转账</a-select-option>
                                <a-select-option value="3">授权现金</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原代理银行</template>
                            <a-select  v-model="queryParam.S_AGENTBANKCLASS" placeholder="请选择代理银行">
                                <a-select-option
                                  :value="d.id"
                                  v-for="d in POST_BANK_OPTION"
                                  :key="d.id"
                                >{{d.name}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">支付凭证编码</template>
                            <a-input  v-model="queryParam.S_VOUCHERNO" placeholder="请输入支付凭证编码" />                              
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原预算单位代码</template>
                            <a-input v-model="queryParam.S_BDGORGCODE" placeholder="请输入预算单位代码"/>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原预算单位</template>
                            <a-input v-model="queryParam.S_BDGORGNAME" placeholder="请输入预算单位"/>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原收款人账号</template>
                            <a-input  v-model="queryParam.S_PAYEEACCTNO" placeholder="请输入收款人账号" />                                
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                        <template slot="label">原收款人名称</template>
                           <a-input  v-model="queryParam.S_PAYEEACCTNAME" placeholder="请输入收款人名称" />                                                               
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 7 }" :wrapperCol="{ span: 16, offset: 1 }">
                            <template slot="label">功能科目代码</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    @change="selectCode"
                                    :maxTagCount="1"
                                    
                                    tree-checkable
                                    :show-checked-strategy="SHOW_PARENT"
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="SUBJECTNAME_OPTION"                                 
                                    v-model="queryParam.S_EXPFUNCCODE"
                                    placeholder="请选择科目代码"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 7 }" :wrapperCol="{ span: 16, offset: 1 }">
                            <template slot="label">功能科目名称</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    multiple
                                    @change="selectName"
                                    
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="SUBJECTCODE_OPTION"
                                    :showCheckedStrategy="SHOW_PARENT"
                                    treeCheckable
                                    :selectedKeys="selectedKeys"
                                    v-model="queryParam.S_EXPFUNCNAME"
                                    placeholder="请选择功能科目名称"
                                    :maxTagCount="1"
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="退回类型"
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                            <a-select v-model="queryParam.backReason" placeholder="请选择退回类型">
                                <a-select-option value="1">全部</a-select-option>
                                <a-select-option value="10000">万元</a-select-option>
                                <a-select-option value="100000000">亿元</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="摘要"
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                            <a-input v-model="queryParam.ENTERPRISE_NAME" placeholder="请输入摘要"/>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="金额单位"
                                :labelCol="{ span: 7 }"
                                :wrapperCol="{ span: 16, offset: 1 }"
                        >
                            <a-select v-model="queryParam.S_AMTUNIT" placeholder="请选择金额单位">
                                <a-select-option value="1">元</a-select-option>
                                <a-select-option value="10000">万元</a-select-option>
                                <a-select-option value="100000000">亿元</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    
                    
                    <a-col :md="6" :sm="10"></a-col>
                    <a-col :md="6" :sm="10"></a-col>
                    <a-col :md="6" :sm="10"></a-col>

                    <a-col :md="6" :sm="24" style="text-align: right">
                        <a-button type="primary" @click="searchQuery()">查询</a-button>
                        <!-- <a-button style="margin-left: 8px" @click="handleSearchReset">重置</a-button> -->
                        <!-- <a-button style="margin-left: 8px" @click="handleExportXls('税收收入分行业分税种统计月报总表')">导出Excel</a-button> -->
                        <a-button style="margin-left: 8px" @click="handleExportXls('税收收入分行业分税种统计月报总表')">导出Txt</a-button>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align: center">集中支付退款业务明细查询表</h1>
        <a-row style="padding:10px 0;" v-if="dataSource.length>0">
        <a-col :md="8" style="z-index:100;position:absolute;left:0px;text-align:left;">
          填报摊位：
          
        </a-col>
        <a-col :md="24" style="position:absolute;text-align:center;" v-if="queryParam.D_ACCT">
          业务期间：
          <span>{{queryParam.D_ACCT.format('YYYY-MM')}}</span>
        </a-col>
        <a-col
          :md="8"
          style="position:absolute;right:0px;text-align:right;"
        >
          金额单位：{{queryParam.UNIT === '1' ? '元' : (queryParam.UNIT === '10000' ? '万元' : '亿元'),}}
          
        </a-col>
      </a-row>
        <!-- 表格区域 -->
        <div class="table" style="position:relative;width:100%;margin:20px 0">
            <a-table
                    bordered
                    :rowKey="(record, i) => i"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange"
                    :scroll="{ x: 3000}"
            ></a-table>
        </div>
    </a-card>
</template>

<script>
    import {dailyStockListMixin} from '@/mixins/dailyStockListMixin'
    import {getGuokuTree,getIndexJurisdiction,getEnumTypeAll} from '@/api/nationalTreasury'
    import {getBudgetUnit,getKeMuTreeCode,getKeMuTreeName} from '@/api/report'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'
    const SHOW_PARENT = SHOW_PARENT;
    const sub = "A01,A02,A03,A04,A05,B06,B07,B08,B09,B10,B11,B12,C13,C14,C15,C16,C17,C18,C19,C20,C21,C22,C23,C24,C25,C26,C27,C28,C29,C30,C31,C32,C33,C34,C35,C36,C37,C38,C39,C40,C41,C42,C43,D44,D45,D46,E47,E48,E49,E50,F51,F52,G53,G54,G55,G56,G57,G58,G59,G60,H61,H62,I63,I64,I65,J66,J67,J68,J69,K70,L71,L72,M73,M74,M75,N76,N77,N78,O79,O80,O81,P82,Q83,Q84,R85,R86,R87,R88,R89,S90,S91,S92,S93,S94,S95,T96,ZZ9,ZQT11111111,ZQT22222222,ZQT33333333,ZQT44444444,ZQTZQT"

    export default {
        name: 'centerlizedReturnDetail',
        mixins: [dailyStockListMixin],
        data() {
            return {
                 guoKuTree: [],
                dataSource: [],
                POST_BANK_OPTION:[
               
                ],
                S_BDGORG_OPTION:[],
                JURISDICTION_OPTION:[],
                SUBJECTCODE_OPTION:[],
                SUBJECTNAME_OPTION:[],
                TAX_OPTION:[],
                sortTree:[],
                selectedKeys: '',
                SHOW_PARENT,
                columns: [{
                        title: '编号',
                        align: 'center',
                        dataIndex: 'S_SEQNO',
                        width:100,
                    },{
                        title: '日期',
                        align: 'center',
                        dataIndex: 'S_ENTRUSTDATE_1',
                        width:200,
                    },{
                        title: '国库编码',
                        align: 'center',
                        dataIndex: 'S_TRECODE',
                        width:200,
                    },{
                        title: '原代理银行',
                        align: 'center',
                        dataIndex: 'S_AGENTBANKCLASS_1',
                        width:200
                    },{
                        title: '原支付方式',
                        align: 'center',
                        dataIndex: 'S_PAYMODENAME',
                        width:200
                    },{
                        title: '原支付凭证日期',
                        align: 'center',
                        dataIndex: 'D_PAYVOUDATE',
                        width:300,
                    },{
                        title: '原支付凭证编号',
                        align: 'center',
                        dataIndex: 'F_PROJECT8',
                        width:300
                    },{
                        title: '凭证编号',
                        align: 'center',
                        dataIndex: 'S_VOUCHERNO_1',
                        width:300
                    },{
                        title: '预算种类',
                        align: 'center',
                        dataIndex: 'S_FUNDTYPENAME',
                        width:200
                    },{
                        title: '原预算单位代码',
                        align: 'center',
                        dataIndex: 'S_BDGORGCODE_1',
                        width:300
                    },{
                        title: '原预算单位名称',
                        align: 'center',
                        dataIndex: 'S_BDGORGNAME_1',
                        width:300
                    },{
                        title: '功能科目代码',
                        align: 'center',
                        dataIndex: 'S_EXPFUNCCODE_1',
                        width:300
                    },{
                        title: '功能科目名称',
                        align: 'center',
                        dataIndex: 'S_EXPFUNCNAME_1',
                        width:300
                    },{
                        title: '原零余额账户账号',
                        align: 'center',
                        dataIndex: 'S_ZEROACCTNO',
                        width:300
                    },{
                        title: '原零余额账户名称',
                        align: 'center',
                        dataIndex: 'S_ZEROACCTNAME',
                        width:300
                    },{
                        title: '原收款人账户',
                        align: 'center',
                        dataIndex: 'S_PAYEEACCTNO',
                        width:300
                    },{
                        title: '原收款人开户行名称',
                        align: 'center',
                        dataIndex: 'S_PAYEEOPNBNKNAME',
                        width:300
                    },{
                        title: '退回类型',
                        align: 'center',
                        dataIndex: 'F_PROJECT_12',
                        width:200
                    },{
                        title: '退回金额',
                        align: 'center',
                        dataIndex: 'F_PROJECT_10',
                        width:200
                    },{
                        title: '摘要',
                        align: 'center',
                        dataIndex: 'S_REMARK',
                        width:300
                    }
                    ],
                url: {
                    list: '/fixedReport/reportTndustry/getTndustryTaxData',
                    exportXlsUrl: '/fixedReport/reportTndustry/exportXls'
                },
                guokuName: ''
            }
        },

        created() {
            // 查询条件设置
            this.setQuery()
            // 国库
           // 国库
            getGuokuTree(
               
            ).then(res => {
                if (res.result === 'success') {
                    this.guoKuTree = res.rows
                }
            })
            getEnumTypeAll(37).then((res)=>{
                if (res.result === 'success') {
                        this.POST_BANK_OPTION = res.rows;
                    }
            })
            //获取预算单位
            getBudgetUnit().then(res=>{
               //if (res.result === 'success') {
                    this.S_BDGORG_OPTION = res.rows
                //} 
            })
            //科目数
            getKeMuTreeName().then(res=>{
                if(res.result === 'success'){
                    this.SUBJECTCODE_OPTION = res.rows;
                }
            }) 
            //科目数
            getKeMuTreeCode().then(res=>{
               if(res.result === 'success'){
                    this.SUBJECTNAME_OPTION = res.rows;
                }
            })
        },
        methods: {
            //  限制月的方法
            disabledDate(current) {
                // console.log(current, 'ccccc') //current是月份的最后一个月
                // console.log(this.moment().endOf('month'), 'mmm') // moment是当前月
                return current && current >= this.moment().startOf('day')
            },

            sortChange(){},
            handleSearchReset() {
                this.setQuery()
                this.dataSource = []
            },
            // 查询条件设置 默认值
           setQuery() {
                this.queryParam = {
                    //DISPLAY_ENTERPRISE: '2', //日期
                    S_AMTUNIT:'1',
                    report_type: 'centerlizedPaymentDetail', // 报表类型,
                    S_STARTTIME:moment(new Date(), 'YYYY-MM-DD'),
                    S_ENDTIME:moment(new Date(), 'YYYY-MM-DD')
                    //PROJECT:sub.split(','),
                }
            },
            selectName(val,label,extra){
              /* this.queryParam = {
                  S_EXPFUNCCODE:val,
                  S_EXPFUNCNAME:val
              } */
              this.queryParam.S_EXPFUNCCODE = val
              this.queryParam.S_EXPFUNCNAME = val
            },
            selectCode(val,label,extra){
              /* this.queryParam = {
                  S_EXPFUNCNAME:label,
                  S_EXPFUNCCODE:val
              } */
              this.queryParam.S_EXPFUNCNAME = label;
              this.queryParam.S_EXPFUNCCODE = val;
            },
            nameChange(value) {
                console.log(value, 'vvvvvv')
                if (value === undefined) {
                    this.guokuName = ''
                } else {
                    this.guokuName = value.label
                }
            }
        }
    }
</script>

<style>
    .searchCenter .ant-col-7{
     width: 29.16666667% !important;
    }
    .search .ant-form-item-label{
        overflow:inherit
    }
</style>
