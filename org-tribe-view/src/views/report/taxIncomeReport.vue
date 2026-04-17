<template>
    <a-card :bordered="false" class="card-area">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <!-- 搜索区域 -->
            <a-form layout="inline">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="10">
                        <a-form-item required :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <template slot="label">账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期</template>
                            <a-month-picker
                                    style="width: 100%"
                                    placeholder="请选择账期"
                                    v-model="queryParam.D_ACCT"
                                    :disabled-date="disabledDate"
                                    format="YYYY-MM"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item required :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
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
                        <a-form-item
                                required
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                        <template slot="label">辖&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;属</template>
                            <a-select v-model="queryParam.S_BELONGFLAG" placeholder="请选择辖属">
                                <a-select-option value="1">全辖</a-select-option>
                                <a-select-option value="2">本级</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                required
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                        <template slot="label">科&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;目</template>
                            <a-select :maxTagCount="1" mode="multiple" labelInValue  v-model="queryParam.SUBJECTCODE" placeholder="请选择科目">
                                <a-select-option
                                  :value="d.id"
                                  v-for="d in SUBJECTCODE_OPTION"
                                  :key="d.id"
                                >{{d.name}}
                                </a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <template slot="label">排&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;序</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    allowClear
                                    :maxTagCount="1"
                                    labelInValue
                                    tree-checkable
                                    :show-checked-strategy="SHOW_PARENT"
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="sortTree"  
                                    tree-default-expand-all                                  
                                    v-model="queryParam.SORTCOL"
                                    placeholder="请选择排序"
                                    @change="sortChange"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <template slot="label">行&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;业</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    multiple
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="TAX_OPTION"
                                    :showCheckedStrategy="SHOW_PARENT"
                                    treeCheckable
                                    :selectedKeys="selectedKeys"
                                    v-model="queryParam.PROJECT"
                                    placeholder="请选择行业"
                                    :maxTagCount="1"
                            ></a-tree-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="是否显示企业"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select @select="selectValue" v-model="queryParam.DISPLAY_ENTERPRISE" placeholder="请选择是否显示企业">
                                <a-select-option value="1">是</a-select-option>
                                <a-select-option value="2">否</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="10" v-if="queryParam.DISPLAY_ENTERPRISE==='1'">
                        <a-form-item
                                label="企业数量"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-input-number style="width:100%;" v-model="queryParam.ENTERPRISESUM" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10" v-if="queryParam.DISPLAY_ENTERPRISE==='1'">
                        <a-form-item
                                label="企业名称"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-input v-model="queryParam.ENTERPRISE_NAME" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="金额单位"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.UNIT" placeholder="请选择金额单位">
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
                        <a-button style="margin-left: 8px" @click="handleSearchReset">重置</a-button>
                        <a-button style="margin-left: 8px" @click="handleExportXls('税收收入分行业分税种统计月报总表')">导出</a-button>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align: center">税收收入分行业分税种统计月报总表</h1>
        <h2 style="text-align: center;font-size:14px;">备注表号：01</h2>
        <a-row style="padding:10px 0;" v-if="dataSource.length>0">
        <a-col :md="8" style="z-index:100;position:absolute;left:0px;text-align:left;">
          编报机关：
          
        </a-col>
        <a-col :md="24" style="position:absolute;text-align:center;" v-if="queryParam.D_ACCT">
          报表期：
          <span>{{queryParam.D_ACCT.format('YYYY-MM')}}</span>
        </a-col>
        <a-col
          :md="8"
          style="position:absolute;right:0px;text-align:right;"
        >
          单位：{{queryParam.UNIT === '1' ? '元' : (queryParam.UNIT === '10000' ? '万元' : '亿元'),}}
          
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
            ></a-table>
        </div>
    </a-card>
</template>

<script>
    import {dailyStockListMixin} from '@/mixins/dailyStockListMixin'
    import {getGuokuTree,getIndexJurisdiction} from '@/api/nationalTreasury'
    import {getSubjectAll,getTndustryTaxData,getIndustryTree} from '@/api/report'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'
    const SHOW_PARENT = SHOW_PARENT;
    const sub = "A01,A02,A03,A04,A05,B06,B07,B08,B09,B10,B11,B12,C13,C14,C15,C16,C17,C18,C19,C20,C21,C22,C23,C24,C25,C26,C27,C28,C29,C30,C31,C32,C33,C34,C35,C36,C37,C38,C39,C40,C41,C42,C43,D44,D45,D46,E47,E48,E49,E50,F51,F52,G53,G54,G55,G56,G57,G58,G59,G60,H61,H62,I63,I64,I65,J66,J67,J68,J69,K70,L71,L72,M73,M74,M75,N76,N77,N78,O79,O80,O81,P82,Q83,Q84,R85,R86,R87,R88,R89,S90,S91,S92,S93,S94,S95,T96,ZZ9,ZQT11111111,ZQT22222222,ZQT33333333,ZQT44444444,ZQTZQT"

    export default {
        name: 'guokuStockTab',
        mixins: [dailyStockListMixin],
        data() {
            return {
                guoKuTree: [],
                dataSource: [],
                JURISDICTION_OPTION:[],
                SUBJECTCODE_OPTION:[],
                TAX_OPTION:[],
                sortTree:[],
                selectedKeys: '',
                SHOW_PARENT,
                columns: [{
                        title: '行业',
                        align: 'center',
                        dataIndex: 'F_PROJECT'
                    }
                    ],
                url: {
                    list: '/fixedReport/reportTndustry/getTndustryTaxData',
                    exportXlsUrl: '/fixedReport/reportTndustry/exportXls'
                },
                guokuName: ''
            }
        },
        watch:{
            'queryParam.SUBJECTCODE'(val){
                let oThis = this;
                oThis.sortTree = [];
                if(oThis.queryParam.DISPLAY_ENTERPRISE==='1'){
                oThis.columns = [{
                        title: '行业',
                        align: 'center',
                        dataIndex: 'PROJECT',
                    },
                    {
                        title: '企业名称',
                        align: 'center',
                        dataIndex: 'ENTERPRISE_NAME'
                    }];
                }else{
                  oThis.columns = [{
                        title: '行业',
                        align: 'center',
                        dataIndex: 'PROJECT',
                    }]
                    
                }
                val.forEach(function (e, i) {
                oThis.sortTree.push({
                    id: e.key,
                    value:e.key,
                    title: e.label,
                    disabled:true,
                    children: [{
                        id: 'F_AMT_'+e.key,
                        value:'F_AMT_'+e.key,
                        title: '金额',                       
                    }, {
                        id: 'F_AMT_'+e.key+'_YEAR',
                        value:'F_AMT_'+e.key+'_YEAR',
                        title: '同比增速',                    
                    }]
                });
                oThis.columns.push({
                        title: e.label,
                        width:400,
                        children: [{
                            title: '金额',
                            align: 'right',
                            dataIndex: 'F_AMT_'+e.key,
                            /* width:'50%'  */
                            //scopedSlots: {customRender: 'TAXINCM_TOTAL'}
                        }, {
                            title: '同比增速(%)',
                            align: 'right',
                            dataIndex: 'F_AMT_'+e.key+'_YEAR',
                            /* width:'50%'  */
                        }]
                        },)
            })
            }
        },
        created() {
            // 查询条件设置
            this.setQuery()
            // 国库
            getGuokuTree(
               
            ).then(res => {
                if (res.result === 'success') {
                    this.guoKuTree = res.rows
                }
            })
            //科目
            getSubjectAll().then(res=>{
               if (res.result === 'success') {
                    this.SUBJECTCODE_OPTION = res.rows
                } 
            })
            //行业
            getIndustryTree().then(res=>{
              if(res.result === 'success'){
                this.TAX_OPTION = res.rows;
              }
            }) 
        },
        methods: {
            //  限制月的方法
            disabledDate(current) {
                // console.log(current, 'ccccc') //current是月份的最后一个月
                // console.log(this.moment().endOf('month'), 'mmm') // moment是当前月
                return current && current >= this.moment().startOf('month')
            },

            sortChange(){},
            selectValue(value){
                if(value==='1'){
                    this.columns = [{
                        title: '行业',
                        align: 'center',
                        dataIndex: 'PROJECT'
                    },
                    {
                        title: '企业名称',
                        align: 'center',
                        dataIndex: 'ENTERPRISE_NAME'
                    }]
                }else{
                    this.columns = [{
                        title: '行业',
                        align: 'center',
                        dataIndex: 'PROJECT'
                    }] 
                }
            },
            handleSearchReset() {
                this.setQuery()
                this.dataSource = []
            },
            // 查询条件设置 默认值
            setQuery() {
                this.queryParam = {
                    DISPLAY_ENTERPRISE: '2', //日期
                    UNIT:'1',
                    report_type: 'taxIncomeReport', // 报表类型,
                    PROJECT:sub.split(','),
                }
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

<style lang="less" scoped>
    @import '~@assets/less/common.less';
</style>
