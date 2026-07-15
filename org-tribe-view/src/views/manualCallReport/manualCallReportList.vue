<template>
  <a-card :bordered="false">
    <a-tabs v-model="activeTab">
      <a-tab-pane key="refresh" tab="动态刷数">
        <div class="table-page-search-wrapper">
          <a-form layout="inline" @submit.prevent="searchQuery">
            <a-row :gutter="24">
              <a-col :md="8" :sm="12">
                <a-form-item label="任务名称">
                  <a-input v-model="queryParam.task_name" placeholder="请输入任务名称" allowClear />
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="12">
                <a-form-item label="执行状态">
                  <a-select v-model="queryParam.status" placeholder="请选择执行状态" allowClear>
                    <a-select-option value="0">未执行</a-select-option>
                    <a-select-option value="1">正在执行</a-select-option>
                    <a-select-option value="200">执行成功</a-select-option>
                    <a-select-option value="500">执行失败</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <span class="table-page-search-submitButtons">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button type="primary" ghost class="action-button" @click="handleAdd">新增</a-button>
                  <a-button class="action-button" @click="searchReset">重置</a-button>
                </span>
              </a-col>
            </a-row>
          </a-form>
        </div>

        <a-table
          rowKey="id"
          size="middle"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          :scroll="{ x: 1050 }"
          @change="handleTableChange">
          <span slot="taskType" slot-scope="text">{{ taskTypeText(text) }}</span>
          <a-tag slot="status" slot-scope="text" :color="statusMeta(text).color">
            {{ statusMeta(text).text }}
          </a-tag>
          <span slot="action" slot-scope="text, record">
            <a :disabled="isRunning(record)" @click="!isRunning(record) && handleEdit(record)">修改</a>
            <a-divider type="vertical" />
            <a :disabled="isRunning(record)" @click="!isRunning(record) && confirmStart(record)">启动</a>
            <a-divider type="vertical" />
            <a-popconfirm v-if="!isRunning(record)" title="确定删除该任务吗？" @confirm="handleDelete({ id: record.id })">
              <a>删除</a>
            </a-popconfirm>
            <a v-else disabled>删除</a>
          </span>
        </a-table>

        <call-modal ref="modalForm" @ok="modalFormOk" />
      </a-tab-pane>
      <a-tab-pane key="report" tab="报告管理">
        <report-management />
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import { postAction } from '@/api/manage'
import { ListMixin } from '@/mixins/ListMixin'
import ReportManagement from '@/views/intelligenceReport/reportManagement'
import CallModal from './modules/callModal'

export default {
  name: 'ManualCallReportList',
  mixins: [ListMixin],
  components: { CallModal, ReportManagement },
  data() {
    return {
      activeTab: 'refresh',
      queryParam: { task_name: '', status: undefined },
      url: {
        list: '/errorLogController/getData',
        delete: '/errorLogController/del',
        call: '/errorLogController/callProc'
      },
      columns: [
        { title: '序号', width: 70, customRender: (text, record, index) => index + 1 },
        { title: '任务名称', dataIndex: 'task_name', width: 150 },
        { title: '类型', dataIndex: 'task_type', width: 100, scopedSlots: { customRender: 'taskType' } },
        { title: '执行状态', dataIndex: 'status', width: 110, scopedSlots: { customRender: 'status' } },
        { title: '脚本路径', dataIndex: 'shell_path', width: 210 },
        { title: '脚本名称', dataIndex: 'shell_name', width: 210 },
        { title: '参数', dataIndex: 'shell_param', width: 130 },
        { title: '操作', key: 'action', fixed: 'right', width: 170, scopedSlots: { customRender: 'action' } }
      ]
    }
  },
  methods: {
    loadData(arg) {
      if (arg === 1) {
        this.ipagination.current = 1
      }
      this.loading = true
      return postAction(this.url.list, this.getQueryParams())
        .then(res => {
          if (res && res.result === 'success') {
            this.dataSource = Array.isArray(res.rows) ? res.rows : []
            this.ipagination.total = Number(res.total) || 0
          } else {
            this.dataSource = []
            this.ipagination.total = 0
            this.$message.warning((res && res.msg) || '动态刷数任务加载失败')
          }
        })
        .catch(() => {
          this.dataSource = []
          this.ipagination.total = 0
          this.$message.error('动态刷数任务加载失败，请稍后重试')
        })
        .finally(() => {
          this.loading = false
        })
    },
    taskTypeText(type) {
      return String(type) === '1' ? '存储过程' : '脚本'
    },
    isRunning(record) {
      return String(record.status) === '1'
    },
    statusMeta(status) {
      return {
        '0': { text: '未执行', color: 'default' },
        '1': { text: '正在执行', color: 'processing' },
        '200': { text: '成功', color: 'success' },
        '500': { text: '失败', color: 'error' }
      }[String(status)] || { text: status || '未执行', color: 'default' }
    },
    confirmStart(record) {
      const that = this
      this.$confirm({
        title: '确定启动吗？',
        onOk() {
          return postAction(that.url.call, { id: record.id }).then(res => {
            if (res.result === 'success') {
              that.$message.success(res.msg)
              that.loadData()
            } else {
              that.$message.warning(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped lang="less">
@import '~@assets/less/common.less';

.action-button {
  margin-left: 8px;
}
</style>
