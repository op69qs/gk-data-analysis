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
                        <a-form-item :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <template slot="label">国&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;库</template>
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    allowClear
                                    :maxTagCount="1"
                                    multiple
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
                                label="所属行业"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <!-- <a-select v-model="queryParam.industry" placeholder="请选择所属行业" showSearch optionFilterProp="children" allowClear>
                                <a-select-option
                                            :value="d.industryId"
                                            v-for="d in INDUSTRY_OPTIONS"
                                            :key="d.industryId"
                                    >{{d.industryName}}
                                    </a-select-option>
                            </a-select> -->
                            <a-tree-select
                                    style="width:100%"
                                    showSearch
                                    allowClear
                                    labelInValue
                                    treeNodeFilterProp="label"
                                    :dropdownStyle="{ maxHeight: '200px', overflow: 'auto' }"
                                    :treeData="INDUSTRY_OPTIONS"
                                    v-model="queryParam.industryId"
                                    placeholder="请选择行业"
                            />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="金额单位"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-select v-model="queryParam.amtUnit" placeholder="请选择金额单位">
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
                        <a-button style="margin-left: 8px" @click="handleExportXls('税收收入企业排名统计表')">导出</a-button>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align: center">企业排名</h1>
        <!-- <a-row style="padding:10px 0;" v-if="dataSource.length>0">
        <a-col :md="8" style="z-index:100;position:absolute;left:0px;text-align:left;">
          编报机关：
          
        </a-col>
        <a-col :md="24" style="position:absolute;text-align:center;" v-if="queryParam.D_ACCT">
          报表期：
          <span>{{queryParam.D_ACCT.format('YYYY-MM')}}</span>
        </a-col>
      </a-row> -->
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
    import {getSubjectAll,getTndustryTaxData,getIndustryTree,getIndustryDrop} from '@/api/report'
    import {getAction, postAction} from '@/api/manage'
    import {GetUrlParam} from '@/utils/request'
    export default {
        name: 'guokuStockTab',
        mixins: [dailyStockListMixin],
        data() {
            return {
                guoKuTree: [],
                dataSource: [],
                INDUSTRY_OPTIONS:[],
                columns: [],
                url: {
                    list: '/fixedReport/kydReportController/getEnterpriseRanking',
                    exportXlsUrl: '/fixedReport/kydReportController/excelEnterpriseRanking'
                },
                guokuName: '',
                report_type:'enterpriseRankReport'
            }
        },
        watch:{
        },
        created() {
            // 查询条件设置
            this.setQuery()
            this.setColumns()
            // 国库
            getGuokuTree(
               
            ).then(res => {
                if (res.result === 'success') {
                    this.guoKuTree = res.rows
                }
            })
            //行业下拉
            getIndustryDrop(
               
            ).then(res => {
                if (res.result === 'success') {
                    this.INDUSTRY_OPTIONS = res.rows
                }
            })
        },
        methods: {
            setColumns() {
                this.dataSource = []
                this.columns = [{
                        title: '排名',
                        align: 'center',
                        dataIndex: 'rank'
                    },{
                        title: '企业名称',
                        align: 'center',
                        dataIndex: 'PRONAME'
                    },{
                        title: '累计纳税金额',
                        align: 'center',
                        dataIndex: 'F_AMT_year'
                    },{
                        title: '同比',
                        align: 'center',
                        dataIndex: 'F_AMT_year_tb'
                    },{
                        title: '所属行业',
                        align: 'center',
                        dataIndex: 'industry'
                    },{
                        title: '所属国库',
                        align: 'center',
                        dataIndex: 's_tredscr'
                    }
                ]
            },
            //  限制月的方法
            disabledDate(current) {
                // console.log(current, 'ccccc') //current是月份的最后一个月
                // console.log(this.moment().endOf('month'), 'mmm') // moment是当前月
                // this.moment().startOf('month')
                return current && current >= this.moment().endOf('month')
            },

            sortChange(){},
            handleSearchReset() {
                this.setQuery()
                this.dataSource = []
            },
            // 查询条件设置 默认值
            setQuery() {
                this.queryParam = {
                    DISPLAY_ENTERPRISE: '2', //日期
                    amtUnit:'100000000',
                    report_type: 'enterpriseRankReport', // 报表类型,
                    // PROJECT:sub.split(','),
                }
            },
            nameChange(value) { // 选择国库
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
