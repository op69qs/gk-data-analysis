<template>
    <a-card :bordered="false" ref="imageDom">

        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :md="6" :sm="12">
                        <a-form-item label="方案名称">
                            <a-input placeholder="请输入关键字" v-model="queryParam.name"></a-input>
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
                    rowKey="id"
                    :columns="columns"
                    :dataSource="dataSource"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange">

                <template slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>

                    <a-divider type="vertical"/>

                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete({id:record.id})">
                        <a>删除</a>
                    </a-popconfirm>
                    <a-divider type="vertical"/>
                    <a @click="handlePreview(record)">预览</a>
                </template>


            </a-table>
        </div>
        <!-- table区域-end -->
        <!--新增-->
        <exhibition-scheme-list-modal ref="modalForm" @ok="searchQuery()"></exhibition-scheme-list-modal>
    </a-card>
</template>

<script>
    import {ListMixin} from '@/mixins/ListMixin'
    import ExhibitionSchemeListModal from './modules/ExhibitionSchemeListModal'

    export default {
        name: "ExhibitionSchemeList",
        mixins: [ListMixin],
        components: {ExhibitionSchemeListModal},
        data() {
            return {
                queryParam: {},
                columns: [
                    {
                        title: '方案名称',
                        dataIndex: 'name',
                    }, {
                        title: '添加人',
                        align: "center",
                        dataIndex: 'username',
                    }, {
                        title: '添加时间',
                        align: "center",
                        dataIndex: 'add_time',
                    }, {
                        title: '操作',
                        dataIndex: 'action',
                        scopedSlots: {customRender: 'action'},
                        align: "center",
                        width: 200
                    }

                ],
                url: {
                    list: "/vis/api/schemeInfo/getPage",
                    delete: "/vis/api/schemeInfo/del"
                },
            }
        },
        mounted() {
            // this.dataSource = [
            //     {
            //         "name": "测试方案",
            //         "id": "admin",
            //         status: '2020-11-03 14:19:59'
            //     }]
        },
        methods: {
            showConfirm() {
            
            },
            //预览
            handlePreview(record) {
                let that = this
                this.$confirm({
                    title: '大屏自动轮播设置',
                    content: '是否设置大屏自动轮播',
                    okText: '是',
                    cancelText: '否',
                    onOk() {
                        that.$router.push({path: '/vis/preview', query: {info: record.id, interval: record.rotation_interval, autoSetting: 1}})
                    },
                    onCancel() {
                        that.$router.push({path: '/vis/preview', query: {info: record.id, interval: record.rotation_interval, autoSetting: 0}})
                    },
                });
            }
        }

    }
</script>
<style scoped>
    @import '~@assets/less/common.less'
</style>
