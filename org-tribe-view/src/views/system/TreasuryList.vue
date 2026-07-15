<template>
    <a-card :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
            <a-form layout="inline" @submit.prevent="searchQuery">
                <a-row :gutter="24">
                    <a-col :md="6" :sm="12">
                        <a-form-item label="国库名称">
                            <a-input placeholder="请输入名称查询" v-model="queryParam.guoku_dscr"></a-input>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="8">
                        <a-form-item label="状态">
                            <j-dict-select-tag v-model="queryParam.state" placeholder="请选择状态查询" dictCode="state"/>
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
            <a-button @click="handleAdd" type="primary" v-show="!dataSource.length > 0">新增</a-button>
        </div>

        <!-- table区域-begin -->
        <div>

            <a-table
                    :columns="columns"
                    size="middle"
                    :pagination="false"
                    :dataSource="dataSource"
                    rowKey="id"
                    :expandedRowKeys="expandedRowKeys"
                    @expand="handleExpand"
                    :loading="loading">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical"/>
          <a-dropdown>
            <a class="ant-dropdown-link">
              更多 <a-icon type="down"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;" @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item v-if="record.state === '0'">
                <a href="javascript:;" @click="handleAddSub(record)">添加下级</a>
              </a-menu-item>

              <a-menu-item v-show="record.state === '0'">
                <a href="javascript:;" @click="handleStopEnabled(record,{
                  id: record.id,
                  state: '1',
                  guoku_pid: record.guoku_pid
                })">禁用</a>
              </a-menu-item>

              <a-menu-item v-show="record.state === '1'">
                      <a href="javascript:;" @click="handleStopEnabled(record,{
                  id: record.id,
                  state: '0',
                  guoku_pid: record.guoku_pid
                })">启用</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
                <!-- 字符串超长截取省略号显示 -->
                <span slot="url" slot-scope="text">
          <j-ellipsis :value="text" :length="25"/>
        </span>
                <!-- 字符串超长截取省略号显示 -->
                <span slot="statedesc" slot-scope="text">
          <span :style="{color:text === '启用'?'#000000a6':'#ff6633'}">{{text}}</span>
        </span>
                <!-- 字符串超长截取省略号显示-->
                <span slot="component" slot-scope="text">
          <j-ellipsis :value="text"/>
        </span>
            </a-table>

        </div>
        <!-- table区域-end -->

        <treasury-modal ref="modalForm" @ok="modalFormOk"></treasury-modal>

    </a-card>
</template>

<script>
    import TreasuryModal from './modules/TreasuryModal'
    import {postAction} from '@/api/manage'
    import {JeecgListMixin} from '@/mixins/JeecgListMixin'
    import JEllipsis from '@/components/jeecg/JEllipsis'

    const columns = [
        {
            title: '国库名称',
            dataIndex: 'guoku_dscr'
        }, {
            title: '国库编码',
            dataIndex: 'guoku_id'
        }, {
            title: '国库级次',
            dataIndex: 'level_dscr'
        }, {
            title: '国库属性',
            dataIndex: 'guoku_shuxing_dscr'
        }, {
            title: '地区',
            dataIndex: 'area_dscr'
        }, {
            title: '核算主体名称',
            dataIndex: 'bookorgname'
        }, {
            title: '支付行号',
            dataIndex: 'pay_bnk_no'
        }, {
            title: '支付行号名称',
            dataIndex: 'rcvbnk_name'
        }, {
            title: '状态',
            dataIndex: 'statedesc',
            scopedSlots: {customRender: 'statedesc'}
        }, {
            title: '操作',
            dataIndex: 'action',
            scopedSlots: {customRender: 'action'},
            align: 'center',
            width: 150
        }
    ];

    export default {
        name: 'treasury',
        mixins: [JeecgListMixin],
        components: {
            TreasuryModal,
            JEllipsis
        },
        data() {
            return {
                description: '这是菜单管理页面',
                // 表头
                columns: columns,
                loading: false,
                url: {
                    list: '/vis/api/GuokuController/getGuoKuTreeList',
                    stopEnabled: '/vis/api/GuokuController/delGuoku',
                    isDelBookOrg: '/vis/api/GuokuController/isDelGuoku'
                },
                expandedRowKeys: []//展开的行，控制属性
            }
        },
        methods: {
            loadData() {
                this.loading = true;
                let params = Object.assign({}, this.queryParam);
                this.expandedRowKeys = [];
                return new Promise((resolve) => {
                    postAction(this.url.list, params).then(res => {
                        if (res.result === 'success') {
                            this.dataSource = this.getDataByResult(res.rows);
                            resolve();
                        } else {
                            this.$message.warning(res.message)
                        }
                        this.loading = false;
                    }).catch(err => {
                        this.loading = false;
                    })
                })
            },
            handleAddSub(record) {
                this.$refs.modalForm.title = "添加下级";
                this.$refs.modalForm.disableSubmit = false;
                this.$refs.modalForm.edit({'guoku_pid': record.guoku_id});
            },
            //点击展开图标时触发
            handleExpand(expanded, record) {
                // 判断是否是展开状态
                if (expanded) {
                    this.expandedRowKeys.push(record.id)
                    if (record.children.length > 0 && record.children[0].isLoading === true) {
                        let params = Object.assign({}, this.queryParam) //查询条件
                        params['parentId'] = record.id
                        postAction(this.url.list, params).then(res => {
                            if (res.result === 'success') {
                                if (res.rows && res.rows.length > 0) {
                                    record.children = this.getDataByResult(res.rows)
                                    this.dataSource = [...this.dataSource]
                                } else {
                                    record.children = ''
                                    record.isleaf = '0'
                                }
                            } else {
                                this.$message.warning(res.message)
                            }
                        })
                    }
                } else {
                    let keyIndex = this.expandedRowKeys.indexOf(record.id)
                    if (keyIndex >= 0) {
                        this.expandedRowKeys.splice(keyIndex, 1)
                    }
                }
            }
        }
    }
</script>

<style scoped lang="less">
    @import '~@assets/less/common.less';
</style>
