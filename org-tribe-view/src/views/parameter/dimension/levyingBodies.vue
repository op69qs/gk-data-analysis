<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @submit.prevent="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="12">
                        <a-form-item label="征收机构名称">
                            <a-input placeholder="请输入名称查询" v-model="queryParam.TAX_ORG_DSCR"></a-input>
                        </a-form-item>
                    </a-col>

                    <!--<a-col :md="6" :sm="8">
                        <a-form-item label="状态">
                            <j-dict-select-tag v-model="queryParam.state" placeholder="请选择状态查询" dictCode="state"/>
                        </a-form-item>
                    </a-col>-->


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
            <a-button @click="handleExportXls2('征收机关')" style="margin-left: 8px" type="primary">导出</a-button>
        </div>

        <!-- table区域-begin -->
        <div>

            <a-table
                    :columns="columns"
                    size="middle"
                    bordered
                    :dataSource="dataSource"
                    rowKey="id"
                    :pagination="ipagination"
                    :loading="loading"
                    @change="handleTableChange">

                <template slot="action" slot-scope="text, record">
                    <a @click="handleEdit(record)">编辑</a>
                    <a-divider type="vertical"/>
                    <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete({TAX_ORG_ID:record.TAX_ORG_ID})">
                        <a>删除</a>
                    </a-popconfirm>
                    <a-divider type="vertical"/>
                    <a-popconfirm :title="`确定${record.STATE === '0' ?'停用':'启用'}吗?`"
                                  @confirm="() => handleStopAndStart({TAX_ORG_ID:record.TAX_ORG_ID,STATE:record.STATE === '0'?'1':'0 '})">
                        <a>{{record.STATE === '0' ?'停用':'启用'}}</a>
                    </a-popconfirm>
                </template>
                <!-- 字符串超长截取省略号显示 -->
                <template slot="STATE_DSCR" slot-scope="text">
                    <span :style="{color:text === '启用'?'#000000a6':'#ff6633'}">{{text}}</span>
                </template>
            </a-table>

        </div>
        <!-- table区域-end -->

        <levying-bodies-modal ref="modalForm" @ok="modalFormOk"></levying-bodies-modal>

    </a-card>
</template>

<script>
    import levyingBodiesModal from './modules/levyingBodiesModal'
    import {levyingBodiesEdit} from '@/api/nationalTreasury'
    import {postAction} from '@/api/manage'
    import {ListMixin} from '@/mixins/ListMixin'
    import JEllipsis from '@/components/jeecg/JEllipsis'

    const columns = [
        {
            title: '征收机构代码',
            dataIndex: 'TAX_ORG_ID'
        }, {
            title: '征收机构名称',
            dataIndex: 'TAX_ORG_DSCR'
        }, {
            title: '征收机关类型',
            dataIndex: 'TYPE_DSCR'
        }, {
            title: '核算主体代码',
            dataIndex: 'BOOK_ORG_CODE'
        }, {
            title: '国库代码',
            dataIndex: 'TRECODE',
        }, {
            title: '状态',
            dataIndex: 'STATE_DSCR',
            align: 'center',
            scopedSlots: {customRender: 'STATE_DSCR'}
        }, {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: 'center',
            width: 150
        }
    ];

    export default {
        name: 'levyingBodies',
        mixins: [ListMixin],
        components: {
            levyingBodiesModal,
            JEllipsis
        },
        data() {
            return {
                description: '这是菜单管理页面',
                // 表头
                columns: columns,
                loading: false,
                url: {
                    list: '/levyingBodies/getPage',
                    delete: '/levyingBodies/del',
                    exportXlsUrl: '/levyingBodies/exportXls'
                },
                expandedRowKeys: []//展开的行，控制属性
            }
        },
        methods: {
            //停启用
            handleStopAndStart(params) {
                levyingBodiesEdit(params).then(res => {
                    if (res.result === 'success') {
                        this.loadData()
                    }
                    this.$message[res.result === 'success' ? 'success' : 'warning'](res.msg)
                })
            }
        }
    }
</script>

<style scoped lang="less">
    @import '~@assets/less/common.less';
</style>