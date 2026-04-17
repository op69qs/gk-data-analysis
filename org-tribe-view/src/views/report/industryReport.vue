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
                    <!-- <a-col :md="6" :sm="10">
                        <a-form-item
                                label="行业名称"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-input v-model="queryParam.PRONAME" />
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10">
                        <a-form-item
                                label="行业编码"
                                :labelCol="{ span: 5 }"
                                :wrapperCol="{ span: 18, offset: 1 }"
                        >
                            <a-input v-model="queryParam.PROCODE" />
                        </a-form-item>
                    </a-col> -->
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
                    <a-col :md="6" :sm="10">
                        <a-form-item label="当期累计" :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
                            <a-select v-model="queryParam.mark" @change="setColumns()" placeholder="请选择金额属性">
                                <a-select-option value="0">本期值</a-select-option>
                                <a-select-option value="1">累计值</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="10"></a-col>
                    <a-col :md="6" :sm="10"></a-col>
                    <a-col :md="6" :sm="10"></a-col>

                    <a-col :md="6" :sm="24" style="text-align: right">
                        <a-button type="primary" @click="searchQuery()">查询</a-button>
                        <a-button style="margin-left: 8px" @click="handleSearchReset">重置</a-button>
                        <a-button style="margin-left: 8px" @click="handleExportXls(queryParam.mark == '0' ? '税收收入本期分行业分税种统计表' : '税收收入累计分行业分税种统计表')">导出</a-button>
                    </a-col>
                </a-row>
            </a-form>
        </div>
        <h1 style="text-align: center">{{queryParam.mark == '0' ? '本期' : '累计'}}分行业</h1>
        <!-- 表格区域 -->
        <div class="table" style="position:relative;width:100%;margin:20px 0">
            <a-table
                    bordered
                    :rowKey="(record, i) => i"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    :scroll="{ x: 4300 }"
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
    export default {
        name: 'guokuStockTab',
        mixins: [dailyStockListMixin],
        data() {
            return {
                guoKuTree: [],
                dataSource: [],
                columns: [],
                url: {
                    list: '/fixedReport/kydReportController/getIndustry ',
                    exportXlsUrl: '/fixedReport/kydReportController/excelIndustry'
                },
                guokuName: '',
                report_type:'industryReport'
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
        },
        methods: {
            setColumns() {
                this.dataSource = []
                this.columns = [{
                        title: '项目',
                        width: 200,
                        dataIndex: 'PRONAME'
                    },{
                        title: '税收收入合计',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_101',
                            scopedSlots: {customRender: 'F_AMT_101'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_101_year'
                        }]
                    },{
                        title: '国内增值税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_1010101',
                            scopedSlots: {customRender: 'F_AMT_1010101'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_1010101_year'
                        }]
                    },{
                        title: '国内消费税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_1010201',
                            scopedSlots: {customRender: 'F_AMT_1010201'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_1010201_year'
                        }]
                    },{
                        title: '企业所得税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10104',
                            scopedSlots: {customRender: 'F_AMT_10104'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10104_year'
                        }]
                    },{
                        title: '个人所得税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10106',
                            scopedSlots: {customRender: 'F_AMT_10106'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10106_year'
                        }]
                    },{
                        title: '资源税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10107',
                            scopedSlots: {customRender: 'F_AMT_10107'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10107_year'
                        }]
                    },{
                        title: '城市维护建设税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10109',
                            scopedSlots: {customRender: 'F_AMT_10109'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10109_year'
                        }]
                    },{
                        title: '房产税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10110',
                            scopedSlots: {customRender: 'F_AMT_10110'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10110_year'
                        }]
                    },{
                        title: '印花税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10111',
                            scopedSlots: {customRender: 'F_AMT_10111'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10111_year'
                        }]
                    },{
                        title: '城镇土地使用税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10112',
                            scopedSlots: {customRender: 'F_AMT_10112'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10112_year'
                        }]
                    },{
                        title: '土地增值税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10113',
                            scopedSlots: {customRender: 'F_AMT_10113'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10113_year'
                        }]
                    },{
                        title: '车辆购置税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10116',
                            scopedSlots: {customRender: 'F_AMT_10116'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10116_year'
                        }]
                    },{
                        title: '车船税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10114',
                            scopedSlots: {customRender: 'F_AMT_10114'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10114_year'
                        }]
                    },{
                        title: '耕地占用税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10118',
                            scopedSlots: {customRender: 'F_AMT_10118'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10118_year'
                        }]
                    },{
                        title: '契税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10119',
                            scopedSlots: {customRender: 'F_AMT_10119'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10119_year'
                        }]
                    },{
                        title: '烟叶税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10120',
                            scopedSlots: {customRender: 'F_AMT_10120'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10120_year'
                        }]
                    },{
                        title: '环境保护税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_10121',
                            scopedSlots: {customRender: 'F_AMT_10121'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_10121_year'
                        }]
                    },{
                        title: '其他各税',
                        children: [{
                            title: this.queryParam.mark === '0' ? '本期' : '累计',
                            align: 'center',
                            dataIndex: 'F_AMT_99999',
                            scopedSlots: {customRender: 'F_AMT_99999'}
                        }, {
                            title: '同比(%)',
                            align: 'center',
                            dataIndex: 'F_AMT_99999_year'
                        }]
                    }
                ]
            },
            //  限制月的方法
            disabledDate(current) {
                // console.log(current, 'ccccc') //current是月份的最后一个月
                // console.log(this.moment().endOf('month'), 'mmm') // moment是当前月
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
                    mark: '0',
                    report_type: 'industryReport', // 报表类型,
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
