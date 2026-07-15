<template>
    <a-card :bordered="false">

        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :md="6" :sm="12">
                        <a-form-item label="类型名称">
                            <a-input placeholder="请输入类型名称" v-model="queryParam.business_name"></a-input>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="8">
                        <a-form-item label="状态">
                            <j-dict-select-tag v-model="queryParam.business_state" placeholder="请选择状态"
                                               dictCode="enable_status" :allowClear="true"/>
                        </a-form-item>
                    </a-col>
                    <a-col :md="6" :sm="8">
                        <div style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                            <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置
                            </a-button>
                        </div>
                    </a-col>

                </a-row>
            </a-form>
        </div>

        <!-- 操作按钮区域 -->
        <div class="table-operator" style="border-top: 5px">
            <a-button @click="handleAdd" type="primary" icon="plus">添加</a-button>
        </div>

        <!-- table区域-begin -->
        <div>
            <a-table
                    ref="table"
                    bordered
                    size="middle"
                    rowKey="business_id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange">

                <template slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>

                    <a-divider type="vertical"/>

                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete({business_id:record.business_id})">
                        <a>删除</a>
                    </a-popconfirm>
                </template>


            </a-table>
        </div>
        <!-- table区域-end -->

        <business-type-list-modal ref="modalForm" @ok="searchQuery"></business-type-list-modal>
    </a-card>
</template>

<script>
    import {ListMixin} from '@/mixins/ListMixin'
    import BusinessTypeListModal from './modules/BusinessTypeListModal'

    export default {
        name: "BusinessTypeList",
        mixins: [ListMixin],
        components: {BusinessTypeListModal},
        data() {
            return {
                description: '这是用户管理页面',
                queryParam: {},
                columns: [
                    {
                        title: '类型名称',
                        dataIndex: 'business_name',
                    },
                    {
                        title: '类型编码',
                        align: "center",
                        dataIndex: 'business_id',
                    },
                    {
                        title: '状态',
                        align: "center",
                        dataIndex: 'business_state',
                        customRender(text) {
                            if (text === '0') {
                                return '启用'
                            } else {
                                return '停用'
                            }
                        }
                    },
                    {
                        title: '操作',
                        dataIndex: 'action',
                        scopedSlots: {customRender: 'action'},
                        align: "center",
                        width: 150
                    }

                ],
                url: {
                    list: "/vis/api/bussType/getPage",
                    delete: "/vis/api/bussType/del"
                },
            }
        },
        mounted() {
        },
        methods: {}

    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
