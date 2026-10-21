<template>
    <a-card :bordered="false" class="pageView" >
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper" >
            <a-form layout="inline" @keyup.enter.native="searchQuery">
                <a-row :gutter="24">

                    <a-col :md="6" :sm="12">
                        <a-form-item label="页面名称">
                            <a-input placeholder="请输入关键字" v-model="queryParam.name"></a-input>
                        </a-form-item>
                    </a-col>

                    <a-col :md="6" :sm="12">
                        
                        <a-form-item label="状态">
                            <a-select v-model="queryParam.state" placeholder="请选择状态">
                                <a-select-option value="">
                                    请选择
                                </a-select-option>
                                <a-select-option value="0">
                                    发布
                                </a-select-option>
                                <a-select-option value="1">
                                    未发布
                                </a-select-option>
                            </a-select>
                            <!-- <a-input  v-model="queryParam.state"></a-input> -->
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
            <!-- <a-button @click="handleAdd" type="primary" icon="plus">添加</a-button> -->
            <a-button type="primary" icon="plus" @click="addChange">添加</a-button>

        </div>
        <!--内容区域-->
        <a-spin :spinning="loading" tip="加载中...">
            <a-row :gutter="[48,36]">
                <a-col :span="8" v-for="item in dataSource" :key="item.id">
                    <a-card hoverable>
                        <img slot="cover" :alt="item.name" :src="item.thumbnail" class="i_wh"/>
                        <template slot="actions">
                            <div class="editTitle">
                                <span @click="onTabChange('edit',item.id)"><a-icon key="edit" type="edit"/> 编辑</span>
                                <span @click="onTabChange('delete',item.id)"><a-icon key="delete" type="delete" />删除</span>
                                <span @click="onTabChange('release',item.id)" v-if="item.state=='1'">发布</span>
                                <!-- <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                                    <a-icon key="delete" type="delete"/>
                                </a-popconfirm> -->
                            </div>
                        </template>
                        <a-card-meta :title="item.name"></a-card-meta>
                    </a-card>
                </a-col>
            </a-row>
        </a-spin>
        <a-pagination :current="ipagination.current" :page-size="ipagination.pageSize"
                      :total="ipagination.total" @change="changePage" style="margin-top: 24px; text-align: right" />
    </a-card>
</template>

<script>
    import {ListMixin} from '@/mixins/ListMixin'
    import { postAction } from '@/api/manage'
    import { editPageState, getPageList } from '@/api/visScreen'
    export default {
        name: "PageList",
        mixins: [ListMixin],
        data() {
            return {
                description: '这是页面管理页面',
                url: {
                    list: "/vis/api/pageInfo/getPage",
                    delete: "/vis/api/pageInfo/del",
                },
                loading: false,
                dataSource:[],
                listRequest: 0
            }
        },
        activated() {
            if (!this.loading) this.loadData()
        },
        methods:{
            addChange(){
                this.$router.push({name: 'VisPageEditorEntry', query: {type: 'add'}})
            },
            onTabChange(type,id){
                if(type == "edit"){
                    this.$router.push({name: 'VisPageEditorEntry', query: {id, type}})
                } else if(type == 'delete'){
                    this.showDeleteConfirm(id)
                } else {
                    let params = {
                        id: id,
                        state: 0
                    }
                    return editPageState(params).then(res => {
                        // console.log(res)
                        if (res.result === 'success') {
                            this.$message.success(res.msg);
                            this.loadData(1);
                        }else{
                            this.$message.warning(res.msg);
                        }
                    }).catch(() => this.$message.error('发布失败，请重试'));
                }
            },
            showDeleteConfirm(id) { // 删除弹框
                let that = this;
                this.$confirm({
                    title: '确定要删除?',
                    okText: '确定',
                    okType: 'danger',
                    cancelText: '取消',
                    onOk() {
                        // that.handleDelete({id:id})
                        let params = {id:id}
                        return postAction(that.url.delete, params).then((res) => {
                            if (res.result === 'success') {
                                that.$message.success(res.msg);
                                that.loadData(1);
                            } else {
                                that.$message.warning(res.msg);
                            }
                        }).catch(() => that.$message.error('删除失败，请重试'));
                    },
                    onCancel() {
                        console.log('Cancel');
                    },
                });
            },
            changePage(page) {
                this.ipagination.current = page
                return this.loadData()
            },
            loadData(arg) {
                //加载数据 若传入参数1则加载第一页的内容
                if (arg === 1) {
                    this.ipagination.current = 1;
                }
                let params = this.getQueryParams();//查询条件
                const request = ++this.listRequest
                this.loading = true;
                return getPageList(params).then((res) => {
                    if (request !== this.listRequest) return
                    if (res.result === 'success' && Array.isArray(res.rows)) {
                        this.dataSource = res.rows;
                        this.ipagination.total = Number(res.total) || 0;
                    } else {
                        this.dataSource = []
                        this.ipagination.total = 0
                        this.$message.warning(res.msg || '页面加载失败')
                    }
                }).catch(() => {
                    if (request === this.listRequest) this.$message.error('页面加载失败，请重试')
                }).finally(() => {
                    if (request === this.listRequest) this.loading = false
                })
            },
        }
    }
</script>

<style scoped>
    .pageView{
        min-height: 87vh;
    }
    .noData{
        margin-top: 20px;
        text-align: center;
    }
    .ant-card >>> .ant-card-body {
        border-top: 1px solid #e8e8e8;

    }

    .i_wh {
        height: 300px;
    }
    .editTitle{
        display: flex;
        color: #000;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        padding: 0 1rem;
    }
</style>
