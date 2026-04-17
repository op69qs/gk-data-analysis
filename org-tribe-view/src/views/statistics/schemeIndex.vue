<template>
  <a-card :bordered="false" class="card-area">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper search2">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="8" :sm="8">
            <a-form-item label="关键字查询" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
              <a-input placeholder="请输入指标名称关键字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
         
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div id="tables" style="margin-top:20px;">
      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="SCHEME_DESCR"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <span slot="action" slot-scope="text, record">
          <a  @click="handleRun(record)">执行</a>
          <a-divider type="vertical" />
          <a v-if="record.STATE==='0'" @click="handleSubmit(record)">添加到查询页</a>
          <a style="color:rgba(0, 0, 0, 0.65);" v-if="record.STATE==='1'">已添加</a>
        </span>
        <span slot="INCLUDE_INDEX" slot-scope="text, record">
          <span>{{record.INDEX_NAME}}</span>
          <!-- <a  @click="handleProcess(record)">查看报告</a> -->
        </span>
      </a-table>
    </div>
    <!-- table区域-end -->
  </a-card>
</template>

<script>
/* import {ListMixin} from '@/mixins/ListMixin' */
import {
  selectPublicScheme,
  submitScheme,
  insertPublicScheme
} from '@/api/nationalTreasury'
import { getAction, postAction } from '@/api/manage'
import { GetUrlParam } from '@/utils/request'

export default {
  name: 'schemeIndex',
  data() {
    return {
      // 查询条件
      queryParam: {
        pageNo: 1, //第几页
        pageSize: 10 //每页中显示数据的条数,
      },
      model: {},
      loading: false,
      /* 分页参数 */
      pagination: {
        total: 0,
        pageSize: 10, //每页中显示10条数据
        showSizeChanger: true,
        pageSizeOptions: ['10', '20', '50', '100'], //每页中显示的数据
        showTotal: total => `共有 ${total} 条数据` //分页中显示总的数据
      },
      dataSource: [],
      // 表头
      columns: [{
          title: '方案描述',
          align: 'center',
          width: 500,
          dataIndex: 'SCHEME_DESCR'
        },{
          title: '包含指标',
          dataIndex: 'INCLUDE_INDEX',
          width: 400,
          align: 'center',
          scopedSlots: {customRender: 'INCLUDE_INDEX'}
        },{
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          /* width: 150, */
          scopedSlots: {customRender: 'action'}
        }] 
    }
  },
  created(){
    let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
    let userId = userInfo.id
    selectPublicScheme({pageNo: 1,pageSize: 10}).then(res=>{
        if (res.result == 'success') {
        this.loading = false
        this.dataSource = res.rows
        const pagination = { ...this.pagination }
        pagination.total = res.total
        this.pagination = pagination
      }
    })
  },
  methods:{
    //查询
    searchQuery() {
      this.loading = true
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      selectPublicScheme({pageNo: 1, pageSize: 10, 
     schemeDescr:this.queryParam.name==undefined?'':this.queryParam.name.replace(/，/g,"%")}).then(res=>{
          if (res.result == 'success') {
            this.loading = false
            this.dataSource = res.rows
          }
        })
    },
    handleTableChange(pagination, filters, sorter) {
      this.loading = true
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      this.queryParam.pageNo = pagination.current
      this.queryParam.pageSize = pagination.pageSize
      selectPublicScheme({pageNo: this.queryParam.pageNo, pageSize: this.queryParam.pageSize,
      schemeDescr:this.queryParam.name==undefined?'':this.queryParam.name.replace(/，/g,"%")
     }).then(res=>{
          if (res.result == 'success') {
            this.loading = false
            this.dataSource = res.rows
          }
        })
    },
    searchReset(){
        this.queryParam={};
        this.searchQuery();
      },
    handleRun(record){
        this.$router.push({ path: '/statistics/indexLibrary',query:record})
    },
    handleSubmit(record){
      let userInfo = JSON.parse(localStorage.pro__Login_Userinfo).value
      let userId = userInfo.id
      insertPublicScheme({schemeId:record.ID,userId:userId}).then(res=>{
         if(res.result == 'success'){
           this.$message.success(res.msg)
         }
      })
    }
  }
}
</script>

<style>
 .search2 .ant-col-5{
     width: 20.83333333% !important;
 }
 #tables .ant-table-tbody > tr td:first-child{
   text-align:left !important;
 }
  #tables .ant-table-tbody > tr td:nth-child(2){
   text-align:left !important;
 }
</style>