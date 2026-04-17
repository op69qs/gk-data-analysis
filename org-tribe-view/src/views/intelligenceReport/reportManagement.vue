<template>
  <a-card :bordered="false" class="card-area">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <!-- 搜索区域 -->
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :md="6" :sm="8">
            <a-form-item label="报告名称" :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
              <a-input placeholder="请输入报告名称" v-model="queryParam.REPORT_NAME"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item
              label="报告模板类型"
              :labelCol="{ span: 5 }"
              :wrapperCol="{ span: 18, offset: 1 }"
            >
              <a-select placeholder="请选择报告模板类型" v-model="queryParam.REPORT_TYPE_ID" allowClear>
                <a-select-option value="1">月报模板</a-select-option>
                <a-select-option value="2">月度快报模板</a-select-option>
                <a-select-option value="3">季度模板</a-select-option>
                <a-select-option value="4">季度快报模板</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="状态" :labelCol="{ span: 5 }" :wrapperCol="{ span: 18, offset: 1 }">
              <a-select placeholder="请选择状态" v-model="queryParam.STATUS" allowClear>
                <a-select-option value="0">生成中</a-select-option>
                <a-select-option value="1">已生成</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
            <a-col :md="6" :sm="24">
              <a-button type="primary" @click="searchQuery()">查询</a-button>
              <a-button style="margin-left: 8px" @click="onSearchReset">重置</a-button>
            </a-col>
          </span>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" style="margin-top: 5px">
      <a-button @click="handleAdd" type="primary">新增</a-button>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="(record, i) => i"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template slot="STATUS" slot-scope="text, record">
          <span v-if="text === '0'" style="color:red;">生成中</span>
          <span v-else-if="text === '1'">已生成</span>
        </template>
        <template slot="action" slot-scope="text, record">
          <template>
            <a @click="handleView(record)">查看</a>
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定删除吗?"
              @confirm="() => handleDelete({REPORT_ID:record.REPORT_ID})"
            >
              <a>删除</a>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </div>
    <!-- table区域-end -->
    <!--生成报告-->
    <edit-report ref="modalForm" @ok="loadData(1)"></edit-report>
    <!--查看月度报告-->
    <details-report ref="detailsReport" @ok="loadData(1)"></details-report>
    <!--查看月度快报--->
    <month-quick-report ref="monthQuickReport" @ok="loadData(1)"></month-quick-report>
    <!-- 查看季度报告 -->
    <quarterly-report ref="quarterlyReport" @ok="loadData(1)"></quarterly-report>
    <!--查看季度快报--->
    <quarterly-quick-report ref="quarterlyQuickReport" @ok="loadData(1)"></quarterly-quick-report>
  </a-card>
</template>

<script>
import { ListMixin } from '@/mixins/ListMixin'
import editReport from './modules/editReport'
import detailsReport from './modules/detailsReport'
import monthQuickReport from './modules/monthQuickReport'
import quarterlyReport from './modules/quarterlyReport'
import quarterlyQuickReport from './modules/quarterlyQuickReport'

export default {
  name: 'reportManagement',
  mixins: [ListMixin],
  components: { editReport, detailsReport, monthQuickReport, quarterlyReport, quarterlyQuickReport },
  data() {
    return {
      // 表头
      columns: [
        {
          title: '报告名称',
          width: '10%',
          dataIndex: 'REPORT_NAME'
        },
        {
          title: '报告模板类型',
          width: '4%',
          dataIndex: 'REPORT_TYPE_DESC'
        },
        {
          title: '维度',
          align: 'center',
          dataIndex: 'DIM_DESC',
          width: '4%'
        },
        {
          title: '账期',
          dataIndex: 'ACCOUNT_PERIOD',
          width: '4%'
        },
        {
          title: '状态',
          dataIndex: 'STATUS',
          align: 'center',
          width: '3%',
          scopedSlots: { customRender: 'STATUS' }
        },
        {
          title: '生成时间',
          dataIndex: 'MODIFY_DATE',
          align: 'center',
          width: '5%'
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          width: '4%',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/fixedReport/newsFlash/getReportAll',
        delete: '/fixedReport/newsFlash/delEntityReport'
      }
    }
  },
  created() {
    this.queryParam.USERID = this.$sessionStorage.ls.get('Login_Userinfo').id
  },
  methods: {
    //重置
    onSearchReset() {
      this.queryParam.USERID = this.$sessionStorage.ls.get('Login_Userinfo').id
      this.searchReset()
    },
    //查看
    handleView(record) {
      console.log(record, 'rrrrrrrrrr')
      if (record.REPORT_TYPE_ID === '1') {
        this.$refs.detailsReport.visibleModal = true
        this.$refs.detailsReport.edit(record)
      } else if (record.REPORT_TYPE_ID === '2') {
        // console.log('2222')
        this.$refs.monthQuickReport.visibleModal = true
        this.$refs.monthQuickReport.edit(record)
      } else if (record.REPORT_TYPE_ID === '3') {
        this.$refs.quarterlyReport.visibleModal = true
        this.$refs.quarterlyReport.edit(record)
      } else if (record.REPORT_TYPE_ID === '4') {
        this.$refs.quarterlyQuickReport.visibleModal = true
        this.$refs.quarterlyQuickReport.edit(record)
      }
    },
    //导出
    handleExport(record) {
      this.loadData()
      // window.open(`${exportReportEntity}?ER_ID=${record.ER_ID}&X-Access-Token=${this.$sessionStorage.ls.get('Access-Token')}`);
    }
  }
}
</script>

<style scoped lang="less">
@import '~@assets/less/common.less';
</style>