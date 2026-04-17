<template>
    <a-spin :spinning="spinning">
        <div id="integratedQuery">
            <transition name="fade">
                <div v-show="advanced">
                    <div class="inquiryBottom" :style="{height:treeHeight}">
                        <div>
                            <a-input-search style="padding:4px 6px 4px 0;" placeholder="请输入维度表关键字查询"
                                            @search="onSearch"></a-input-search>
                        </div>
                        <div style="margin-bottom: 10px">
                          <a-button type="primary" @click="onNewDimension('add')">新增</a-button>
                        </div>
                        <a-table
                          ref="dimensionTable"
                          size="middle"
                          bordered
                          :rowKey="(record, i) => i"
                          :dataSource="dimensionSource"
                          :pagination="false"
                          :loading="loading1"
                          :scroll="{ x: scrollX,y:300}"
                          :columns="dimensionColumns"
                          @change="handleTableChange"
                          v-loadmore="dimensionLoadMore">
                           <span slot="action" slot-scope="text, record, index">
                              <a @click="handleEdit(record,index)">编辑</a>
                              <a @click="handleDeail(record,index)" style="padding: 0 10px">详情</a>
                              <a @click="handleDel(record,index)">删除</a>
                            </span>
                        </a-table>
                    </div>
                </div>
            </transition>
            <div class="model-middle">
                <button @click="advanced = !advanced">
                    <icon-font
                            :type="advanced===false?'icon-xiangyoushuangjiantou':'icon-xiangzuoshuangjiantou'"
                            style="font-size:18px;margin-right:5px;"/>
                </button>
            </div>
            <div :style="{width:advanced?'73%':'98%'}">
                <div v-if="dimensionShow">
                  <div>{{dimensionParams.name}}-明细</div>
                  <a-divider style="margin:0 0 15px 0;"/>
                  <div>
                    <div class="table-page-search-wrapper search2" style="margin-bottom: 20px">
                      <!-- 搜索区域 -->
                      <a-form layout="inline">
                        <a-row :gutter="24">
                          <a-col :md="8" :sm="8">
                            <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="名称">
                              <a-input v-model="queryParam.name" placeholder="请选择维度详情名称查询"></a-input>
                            </a-form-item>

                          </a-col>
                          <a-col :md="8" :sm="8">
                            <a-form-item :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}" label="code">
                              <a-input v-model="queryParam.code" placeholder="请选择编码查询"></a-input>
                            </a-form-item>
                          </a-col>
                          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                            <a-col :md="6" :sm="24">
                              <a-button type="primary" @click="searchQuery()">查询</a-button>
                              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                            </a-col>
                          </span>
                          <a-col :md="24" :sm="24">
                            <a-button @click="handleAdd" type="primary">新增</a-button>
                            <a-upload name="file" :showUploadList="false" :multiple="false" :customRequest="uploadFile">
                              <a-button type="primary">导入</a-button>
                            </a-upload>
                            <!--<a-button type="primary" @click="importVisible = true">导入</a-button>-->
                          </a-col>
                        </a-row>
                      </a-form>
                    </div>
                    <div>
                      <a-table
                        :columns="columns"
                        :row-key="record => record.id"
                        :data-source="data"
                        :pagination="pagination"
                        :loading="loading"
                        bordered
                        @change="handleTableChangeTwo"
                      >
                        <template slot="action" slot-scope="text, record, index">
                          <a @click="handleSubEdit(record,index)" style="padding-right:20px;">编辑</a>
                          <a @click="handleSubDel(record,index)">删除</a>
                        </template>
                      </a-table>
                    </div>
                  </div>
                </div>

            </div>
        </div>
        <!--新增、修改维度弹框-->
        <a-modal v-model="addVisible" :title="typeStatus=='add'?'新增维度':'编辑'" @ok="handleAddOk">
          <a-input v-model="dimensionName" placeholder="请输入维度名称" />
        </a-modal>
        <!--新增维度详情-->
      <dimension-detail-modal ref="DataSourceModal" @ok="modalFormOk" :dataSource="dataSource"></dimension-detail-modal>
    </a-spin>
</template>

<script>
  // import {ListMixin} from '@/mixins/ListMixin'
  import {
    JeecgListMixin
  } from '@/mixins/JeecgListMixin'
    import {
      getMainPage,
      addMain,
      editMain,
      delMain,
      getSubPage,
      delSub,
      readExcel
    } from '@/api/dimensionTableApi'
    import dimensionDetailModal from './modules/dimensionDetailModal'
    export default {
        name: "dimensionTable",
        mixins: [JeecgListMixin],
        data() {
            return {
              scrollX: '',//table x
              treeHeight: (document.body.clientHeight - 201) + 'px',
              advanced: true,
              typeStatus: 'add',
              dimensionId:'', // 维度id
              dimensionName: '', // 新增维度name
              dimensionSource: [], // 维度table数据
              dimensionColumns: [
                  {
                    title: '维度名称',
                    dataIndex: 'name',
                    align: 'center',
                    width:"38%",
                  },
                  {
                    title: '操作',
                    dataIndex: 'action',
                    align: 'center',
                    width: '50%',
                    scopedSlots: {customRender: 'action'}
                  }
                ], //维度table 列
              dimensionPagination: {
                  current: 1,
                  pageSize: 100,
                  pageSizeOptions: ['100', '200', '300'],
                  showTotal: (total, range) => {
                    return range[0] + "-" + range[1] + " 共" + total + "条"
                  },
                  showQuickJumper: true,
                  showSizeChanger: true,
                  total: 0
                },//维度 分页字段
              loading1: false, //维度table loading
              spinning: false, //维度tableloading 遮罩
              addVisible: false, // 维度新增弹框
              // params: {},
              searchValue: '',//搜索值
              dimensionShow: false,
              dimensionParams: {}, // 维度明细参数
              queryParam:{},// 维度明细搜索框
              fileList: [],
              data: [], // 维度详情table列表
              pagination: { // 维度详情分页字段
                current: 1,
                pageSize: 100,
                pageSizeOptions: ['100', '200', '300'],
                showTotal: (total, range) => {
                  return range[0] + "-" + range[1] + " 共" + total + "条"
                },
                showQuickJumper: true,
                showSizeChanger: true,
                total: 0
              },
              loading: false,// 维度详情加载
              columns:[
                {
                  title: '编码',
                  dataIndex: 'code',
                  align: 'center',
                  width:"25%",
                },
                {
                  title: '维度详情名称',
                  dataIndex: 'name',
                  align: 'center',
                  width:"50%",
                },
                {
                  title: '操作',
                  dataIndex: 'action',
                  align: 'center',
                  width: '30%',
                  scopedSlots: {customRender: 'action'}
                }
              ],// 维度详情列
            }
        },
        components: {
          dimensionDetailModal
        },
        computed: {
          importExcelUrl: function () {
            return `${window._CONFIG['domianURL']}${this.url.importExcelUrl}`
          }
        },
        mounted() {
          this.onDimensionSearch(1)
        },
        /*//组件销毁
        destoryed() {
            delRedis({userId: this.$sessionStorage.ls.get('Login_Userinfo').id}).then(res => {
            });
        },*/
        methods: {
            /*查询*/
            onSearch(value){
              if(this.dimensionShow == true) this.dimensionShow =  false;
              this.onDimensionSearch(1,value)
            },
          // 维度表搜索
            onDimensionSearch(arg,name) {
              this.spinning = true;
              let params = {};
              //加载数据 若传入参数1则加载第一页的内容
              if (arg === 1) {
                this.dimensionPagination.current = 1;
                this.dimensionSource = [];
                this.dimensionPagination.total = 0;
              }
              if(name) params.name = name
              params.pageNo=this.dimensionPagination.current;
              params.pageSize=this.dimensionPagination.pageSize;
              getMainPage(params).then(res => {
                if (res.result === 'success') {
                  this.dimensionSource = res.rows;
                  this.dimensionPagination.total = res.total;
                } else {
                  this.$message.error(res.msg);
                }
                this.spinning = false;
              }).catch(err => {
                this.spinning = false;
              })
            },
            // 新增维度表
            handleAddOk(){
              if(!this.dimensionName) {
                this.$message.error('维度名不能为空');
                this.addVisible = true;
              }else {
                if(this.typeStatus == 'add'){
                  addMain({'name':this.dimensionName}).then(res =>{
                    if(res.result == 'success'){
                      this.$message.success(res.msg);
                      this.dimensionName = '';
                      this.onDimensionSearch(1)
                      this.addVisible = false;
                    }else {
                      this.$message.error(res.msg);
                      this.dimensionName = ''
                      this.addVisible = false;
                    }
                  }).catch(err => {
                    this.addVisible = false;
                  })
                } else {
                  editMain({'name':this.dimensionName,'id':this.dimensionId}).then(res =>{
                    if(res.result == 'success'){
                      this.$message.success(res.msg);
                      this.dimensionName = '';
                      this.onDimensionSearch(1)
                      this.addVisible = false;
                    }else {
                      this.$message.error(res.msg);
                      this.dimensionName = ''
                      this.addVisible = false;
                    }
                  }).catch(err => {
                    this.addVisible = false;
                  })
                }
              }
            },
            // 滚动加载维度信息
            dimensionLoadMore() {
              if (!this.spinning) {
                if (this.dimensionSource.length >= this.dimensionPagination.total && this.dimensionSource.length > 0) return;
                this.dimensionPagination.current++;
                // 方式 多次加载
                this.spinning = true;
                this.onDimensionSearch();
              }
            },
            // 维度表-弹框
            onNewDimension(type,record) {
              this.typeStatus = type;
              this.addVisible = true;
              if(type=='add'){
                this.dimensionName = ''
                this.dimensionId = ''
                this.reset()
                this.dimensionShow =  false;
              }else {
                this.dimensionId = record.id
                this.dimensionName = record.name
              }
            },
            /*删除*/
            handleDel(record, index) {
              delMain({'name':record.name,'id':record.id}).then(res =>{
                if(res.result == 'success'){
                  this.$message.success(res.msg);
                  this.onDimensionSearch(1)
                  this.dimensionShow =  false;
                }else {
                  this.$message.error(res.msg);
                }
              }).catch(err => {
                console.log(err)
              })
            },
            /*编辑*/
            handleEdit(record, index) {
              this.onNewDimension('edit',record)
            },
            /*维度详情*/
            handleDeail(record, index) {
              let data = record;
              this.dimensionParams = {...data};
              this.queryParam.main_id = data.id;
              this.dimensionShow =  true;
              this.loadData()
              // this.onNewDimension('edit',record)
            },
            handleTableChange(pagination){ // 维度表格点击事件
              this.dimensionPagination = pagination;
              this.onDimensionSearch();
            },
            //维度明细查询
            searchQuery() {
              this.loading = true;
              this.loadData();
            },
            //维度明细查询条件重置
            searchReset() {
              delete this.queryParam.name
              delete this.queryParam.code
              this.loadData();
            },
            // 维度明细查询
            loadData(){
              if(!this.queryParam.main_id){
                return
              }
              this.queryParam.pageNo = this.pagination.current;
              this.queryParam.pageSize = this.pagination.pageSize;
              this.loading = true;
              getSubPage(this.queryParam).then( res => {
                if (res.result === 'success') {
                  this.data = res.rows;
                  this.pagination.total = res.total;
                  this.loading = false;
                } else {
                  this.loading = false;
                  this.$message.error(res.msg);
                }
              });
            },
            handleTableChangeTwo(pagination){ // 维度表格点击事件
              this.pagination = pagination;
              this.loadData();
            },
            // 新增维度明细
            handleAdd: function () {
              let ref = this.$refs.DataSourceModal;
              ref.main_id = this.dimensionParams.id;
              ref.title = '新增';
              ref.add();
            },
            handleSubDel(record) {
              delSub({'id':record.id}).then(res =>{
                if(res.result == 'success'){
                  this.$message.success(res.msg);
                  this.loadData(1)
                }else {
                  this.$message.error(res.msg);
                }
              }).catch(err => {
                console.log(err)
              })
            },
            //编辑
            handleSubEdit: function (record) {
              let ref = this.$refs.DataSourceModal;
              ref.title = '编辑';
              ref.edit(record);
            },

            beforeUpload(file) {
              this.fileList = [file];
              return false;
            },
            /* 导入 */
            uploadFile(file){
              const formData = new FormData();
              formData.append('file',file.file);
              formData.append('main_id', this.dimensionParams.id);
              readExcel(formData).then(res =>{
                if(res.result == 'success'){
                  this.$message.success(res.msg);
                  this.loadData()
                }else {
                  this.$message.error(res.msg);
                }
              }).catch(err => {
                console.log(err)
              })

            },
            // 清空dimensionParams的值
            reset(){
              this.dimensionParams.id = {}
            }
        }
    }
</script>

<style scoped>
    #integratedQuery {
        display: flex;
        background: #fff;
    }

    #integratedQuery > div {
        display: inline-block;
        /*vertical-align: top;*/
    }

    #integratedQuery > div:nth-child(1) {
        width: 25%;
        border-right: 1px solid #e4e4e4;
    }

    #integratedQuery > div:nth-child(3) {
        width: 73%;
        padding: 15px;
    }

    #integratedQuery > div:nth-child(3) > div:nth-child(1),
    #integratedQuery > div:nth-child(3) > div:nth-child(3) {
        padding-bottom: 15px;
    }

    #integratedQuery > div:nth-child(3) > div:nth-child(3) {
        display: flex;
        justify-content: space-between;
    }

    .inquiryBottom {
        padding: 15px;
        /*border: 1px solid #e4e4e4;*/
        border-right: 0;
    }

    .inquiryBottom > div:nth-child(1) {
        padding-bottom: 5px
    }

    .ant-tree {
        max-height: 547px;
        overflow-y: auto;
    }

    .ant-btn {
        margin-right: 8px;
    }

    .ant-radio-group >>> span.ant-radio + * {
        color: rgb(24, 144, 255) !important;
    }

    .ant-table-wrapper >>> .ant-spin-nested-loading.ant-table td {
        white-space: nowrap;
    }

    .ant-table-wrapper >>> .ant-spin-nested-loading .ant-table-tbody td {
        word-break: break-all;
    }

    li.ant-tree-treenode-disabled > span:not(.ant-tree-switcher),
    li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper,
    li.ant-tree-treenode-disabled > .ant-tree-node-content-wrapper span {
        color: black;
    }

    /*图标样式*/
    .charts {
        font-size: 28px;
        margin-right: 10px;
        cursor: pointer;
    }

    .ant-col-8 {
        line-height: 30px;


    }

    /*.ant-col-8 span {
      display: inline-block;
      width: 40%;
      text-align: right;
    }*/
    .model-middle {
        /* display: flex;
  flex-direction: column;
  align-items: flex-end; */
        float: left;
        margin-top: 240px;
        margin-left: -1px;
        z-index: 100;
    }

    .model-middle button {
        margin-top: 116px;
        border-left: #000 !important;
        background: #fff;
        border: 1px solid #ccc;
        padding-top: 10px;
        padding-left: 0px;
        padding-bottom: 10px;
        cursor: pointer;
    }

    /*动画效果*/
    .fade-enter-active {
        animation: bounce-in .5s;
    }

    .fade-leave-active {
        animation: bounce-in .5s reverse;
    }

    @keyframes bounce-in {
        0% {
            opacity: 0;
        }
        10% {
            opacity: 0.1;
        }
        20% {
            opacity: 0.2;
        }
        30% {
            opacity: 0.3;
        }
        40% {
            opacity: 0.4;
        }
        50% {
            opacity: 0.5;
        }
        60% {
            opacity: 0.6;
        }
        70% {
            opacity: 0.7;
        }
        80% {
            opacity: 0.8;
        }
        90% {
            opacity: 0.9;
        }
        100% {
            opacity: 1;
        }
    }
</style>
