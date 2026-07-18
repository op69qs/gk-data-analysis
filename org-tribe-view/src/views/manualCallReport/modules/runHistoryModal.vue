<template>
  <a-modal
    :title="title"
    :visible="visible"
    :maskClosable="false"
    width="920px"
    @cancel="close">
    <a-table
      rowKey="id"
      size="middle"
      tableLayout="fixed"
      :columns="columns"
      :dataSource="dataSource"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange">
      <a-tag slot="status" slot-scope="text" :color="statusMeta(text).color">
        {{ statusMeta(text).text }}
      </a-tag>
      <pre slot="result" slot-scope="text" class="result-message">{{ text || '-' }}</pre>
    </a-table>
    <template slot="footer">
      <a-button @click="close">关闭</a-button>
    </template>
  </a-modal>
</template>

<script>
import { postAction } from '@/api/manage'

export default {
  name: 'RunHistoryModal',
  data() {
    return {
      visible: false,
      loading: false,
      task: {},
      dataSource: [],
      pagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '50'],
        showSizeChanger: true,
        showTotal: total => `共${total}条`,
        total: 0
      },
      columns: [
        { title: '启动时间', dataIndex: 'start_time', width: 165 },
        { title: '结束时间', dataIndex: 'end_time', width: 165, customRender: text => text || '-' },
        { title: '运行状态', dataIndex: 'status', width: 100, scopedSlots: { customRender: 'status' } },
        { title: '执行结果', dataIndex: 'result_message', scopedSlots: { customRender: 'result' } }
      ]
    }
  },
  computed: {
    title() {
      return `运行记录 - ${this.task.task_name || ''}`
    }
  },
  methods: {
    show(record) {
      this.task = Object.assign({}, record)
      this.dataSource = []
      this.pagination.current = 1
      this.pagination.total = 0
      this.visible = true
      this.loadData()
    },
    loadData() {
      if (!this.task.id) return
      this.loading = true
      postAction('/errorLogController/getRunRecords', {
        task_id: this.task.id,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }).then(res => {
        if (res && res.result === 'success') {
          this.dataSource = Array.isArray(res.rows) ? res.rows : []
          this.pagination.total = Number(res.total) || 0
        } else {
          this.dataSource = []
          this.pagination.total = 0
          this.$message.warning((res && res.msg) || '运行记录加载失败')
        }
      }).catch(() => {
        this.dataSource = []
        this.pagination.total = 0
        this.$message.error('运行记录加载失败，请稍后重试')
      }).finally(() => {
        this.loading = false
      })
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current
      this.pagination.pageSize = pagination.pageSize
      this.loadData()
    },
    statusMeta(status) {
      return {
        '1': { text: '正在执行', color: 'processing' },
        '200': { text: '成功', color: 'green' },
        '500': { text: '失败', color: 'red' }
      }[String(status)] || { text: status || '未知', color: 'default' }
    },
    close() {
      this.visible = false
    }
  }
}
</script>

<style scoped lang="less">
.result-message {
  max-height: 240px;
  margin: 0;
  overflow: auto;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: Consolas, Monaco, monospace;
  font-size: 12px;
  line-height: 1.6;
}
</style>
