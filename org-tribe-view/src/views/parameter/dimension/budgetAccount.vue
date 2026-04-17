<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @submit.prevent="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="12">
                        <a-form-item label="年份">
                            <el-date-picker
                                    style="width: 100%"
                                    v-model="queryParam.S_BDGSBTVSION"
                                    type="year"
                                    format="yyyy"
                                    value-format="yyyy"
                                    placeholder="选择年份">
                            </el-date-picker>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="8">
                        <a-form-item label="预算科目名称">
                            <a-input placeholder="请输入预算科目名称" v-model="queryParam.SUBJECT_DSCR_4"/>
                        </a-form-item>
                    </a-col>


                    <a-col :md="5" :sm="7">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery">查询</a-button>
              <a-button @click="searchReset" style="margin-left: 8px">重置</a-button>
            </span>
                    </a-col>

                </a-row>
            </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator">
            <a-button @click="handleAdd" type="primary">新增</a-button>
            <a-button @click="handleImport" style="margin-left: 8px">导入</a-button>
            <a-button @click="handleExportXls2('预算科目')" style="margin-left: 8px">导出</a-button>
        </div>

        <!-- table区域-begin -->
        <div>
            <a-table
                    :columns="columns"
                    bordered
                    size="middle"
                    :pagination="ipagination"
                    :dataSource="dataSource"
                    :rowKey="(record, i) => i"
                    :loading="loading">
                <template slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>

                    <a-divider type="vertical"/>
                    <a-popconfirm title="是否要删除此行？"
                                  @confirm=" () => handleDelete({SUBJECT_CODE_4:record.SUBJECT_CODE_4,S_BDGSBTVSION:record.S_BDGSBTVSION})">
                        <a>删除</a>
                    </a-popconfirm>
                    <!--<a-dropdown>
                        <a class="ant-dropdown-link">
                            更多
                            <a-icon type="down"/>
                        </a>
                        <a-menu slot="overlay">
                            <a-menu-item>
                                <a href="javascript:;" @click="handleDetail(record)">详情</a>
                            </a-menu-item>

                            <a-menu-item>
                                <a href="javascript:;" @click="handleDelete(record)">删除</a>
                            </a-menu-item>
                        </a-menu>
                    </a-dropdown>-->
                </template>
                <!-- 字符串超长截取省略号显示 -->
                <template slot="url" slot-scope="text">
                    <j-ellipsis :value="text" :length="25"/>
                </template>
                <!-- 字符串超长截取省略号显示 -->
                <template slot="statedesc" slot-scope="text">
                    <span :style="{color:text === '启用'?'#000000a6':'#ff6633'}">{{text}}</span>
                </template>
                <!-- 字符串超长截取省略号显示-->
                <template slot="component" slot-scope="text">
                    <j-ellipsis :value="text"/>
                </template>
            </a-table>
        </div>
        <!-- table区域-end -->
        <!--新增-->
        <budget-account-model ref="modalForm" @ok="searchQuery"></budget-account-model>
        <!--导入-->
        <budget-account-import-modal ref="budgetAccountImportModal" @ok="searchQuery"></budget-account-import-modal>

    </a-card>
</template>

<script>
    import budgetAccountImportModal from './modules/budgetAccountImportModal'
    import budgetAccountModel from './modules/budgetAccountModel'
    import {ListMixin} from '@/mixins/ListMixin'
    import JEllipsis from '@/components/jeecg/JEllipsis'

    const columns = [{
        title: '年份',
        dataIndex: 'S_BDGSBTVSION',
        align: 'center'
    }, {
        title: '预算科目代码',
        dataIndex: 'SUBJECT_CODE_4'
    }, {
        title: '预算科目名称',
        dataIndex: 'SUBJECT_DSCR_4'
    }, {
        title: '预算种类',
        dataIndex: 'BUDGET_TYPE'
    }, {
        title: '调拨标志',
        dataIndex: 'FLITTING_FLAG'
    }, {
        title: '统计科目代码',
        dataIndex: 'STAT_CODE_4'
    }, {
        title: 'T科目分类编码',
        dataIndex: 'T_SUBJECT_CODE_3'
    }, {
        title: 'T科目类别',
        dataIndex: 'T_SUBJECT_TYPE'
    }, {
        title: '操作',
        dataIndex: 'action',
        scopedSlots: {customRender: 'action'},
        align: 'center',
        width: 150
    }
    ];

    export default {
        name: 'budgetAccount',
        mixins: [ListMixin],
        components: {
            budgetAccountImportModal,
            budgetAccountModel,
            JEllipsis
        },
        data() {
            return {
                description: '这是菜单管理页面',
                // 表头
                columns: columns,
                loading: false,
                url: {
                    list: '/subjectImport/getPage',
                    delete: '/subjectImport/del',
                    exportXlsUrl: 'subjectImport/exportXls',
                },
            }
        },
        methods: {
            handleImport() {
                this.$refs.budgetAccountImportModal.add();
            }
        }
    }
</script>

<style scoped lang="less">
    @import '~@assets/less/common.less';
</style>