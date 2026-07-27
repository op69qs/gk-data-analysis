<template>
  <a-card :bordered="false">
    <div class="page-heading">
      <div><h2 class="page-title">数据上报</h2><p class="page-subtitle">收入、支出、库存及 KEY 文件全流程跟踪</p></div>
      <a-button type="primary" icon="upload" @click="uploadVisible = true">新建上报</a-button>
    </div>
    <a-form class="search-form" layout="inline">
      <a-form-item label="来源"><a-select v-model="query.sourceDomain" allowClear style="width: 120px"><a-select-option value="KEY">KEY</a-select-option><a-select-option value="TIMS">TIMS</a-select-option></a-select></a-form-item>
      <a-form-item label="状态"><a-select v-model="query.status" allowClear style="width: 130px"><a-select-option v-for="item in statusOptions" :key="item.value" :value="item.value">{{ item.label }}</a-select-option></a-select></a-form-item>
      <a-form-item label="账期"><a-month-picker v-model="query.accountingPeriod" format="YYYY-MM" /></a-form-item>
      <a-form-item label="国库"><a-input v-model.trim="query.treasuryCode" allowClear placeholder="国库代码" /></a-form-item>
      <a-form-item label="文件"><a-input v-model.trim="query.fileName" allowClear placeholder="文件名" /></a-form-item>
      <a-form-item><a-button type="primary" @click="search">查询</a-button><a-button class="reset-button" @click="reset">重置</a-button></a-form-item>
    </a-form>

    <a-alert class="tracking-tip" type="info" showIcon message="执行中的批次每 3 秒自动刷新；进入详情可查看每个阶段、文件、行错误与加工调用。" />
    <a-table rowKey="id" bordered :loading="loading" :columns="columns" :dataSource="rows"
             :pagination="pagination" @change="tableChanged">
      <template slot="source" slot-scope="text, record"><a-tag color="blue">{{ record.sourceDomain }}</a-tag> {{ typeLabel(record.businessType) }}</template>
      <template slot="period" slot-scope="value">{{ dateOnly(value) }}</template>
      <template slot="progress" slot-scope="text, record"><a-progress :percent="record.progressPercent || 0" size="small" :status="record.status === 'FAILED' ? 'exception' : 'active'" /></template>
      <template slot="status" slot-scope="text, record"><a-tag :color="statusColor(record.status)">{{ stageLabel(record.currentStage) }} · {{ statusLabel(record.status) }}</a-tag></template>
      <template slot="processStatus" slot-scope="text, record">
        <a-tag v-if="record.sourceDomain === 'TIMS'" :color="processStatusColor(record.processCallStatus)">{{ processStatusLabel(record.processCallStatus) }}</a-tag>
        <span v-else>—</span>
      </template>
      <template slot="counts" slot-scope="text, record"><span class="success-count">{{ record.successRowCount || 0 }}</span> / <span class="error-count">{{ record.errorRowCount || 0 }}</span></template>
      <template slot="action" slot-scope="text, record">
        <a @click="openDetail(record)">详情</a><a-divider type="vertical" />
        <a-popconfirm title="仅逻辑删除，文件和审计记录继续保留。确定吗？" @confirm="remove(record)"><a>删除</a></a-popconfirm>
      </template>
    </a-table>

    <ReportUploadModal :visible="uploadVisible" @close="uploadVisible = false" @submitted="uploaded" />
    <ReportBatchDetail :visible="detailVisible" :batchId="selectedBatchId" @close="detailVisible = false" @changed="load" />
  </a-card>
</template>

<script>
import { queryReportBatches, deleteReportBatch } from '@/api/reporting'
import ReportUploadModal from './components/ReportUploadModal'
import ReportBatchDetail from './ReportBatchDetail'

export default {
  name: 'ReportBatchList',
  components: { ReportUploadModal, ReportBatchDetail },
  data() {
    return {
      loading: false,
      uploadVisible: false,
      detailVisible: false,
      selectedBatchId: '',
      pollTimer: null,
      rows: [],
      query: { sourceDomain: undefined, status: undefined, accountingPeriod: null, treasuryCode: '', fileName: '' },
      pagination: { current: 1, pageSize: 10, total: 0, showSizeChanger: true, showTotal: total => `共 ${total} 条` },
      statusOptions: [
        { value: 'QUEUED', label: '等待' }, { value: 'PROCESSING', label: '执行中' },
        { value: 'SUCCEEDED', label: '成功' }, { value: 'PARTIALLY_SUCCEEDED', label: '部分完成/等待加工' }, { value: 'FAILED', label: '失败' }
      ],
      columns: [
        { title: '批次号', dataIndex: 'batchNo', width: 210 },
        { title: '来源 / 类型', width: 150, scopedSlots: { customRender: 'source' } },
        { title: '账期', dataIndex: 'accountingPeriod', width: 110, scopedSlots: { customRender: 'period' } },
        { title: '国库', dataIndex: 'treasuryCode', width: 130 },
        { title: '文件', dataIndex: 'originalFileName', ellipsis: true },
        { title: '阶段状态', width: 150, scopedSlots: { customRender: 'status' } },
        { title: '下游加工', width: 130, scopedSlots: { customRender: 'processStatus' } },
        { title: '进度', width: 150, scopedSlots: { customRender: 'progress' } },
        { title: '成功 / 异常行', width: 120, scopedSlots: { customRender: 'counts' } },
        { title: '发起人', dataIndex: 'createBy', width: 100 },
        { title: '操作', width: 115, fixed: 'right', scopedSlots: { customRender: 'action' } }
      ]
    }
  },
  created() { this.load(); this.pollTimer = window.setInterval(this.poll, 3000) },
  beforeDestroy() { if (this.pollTimer) window.clearInterval(this.pollTimer) },
  methods: {
    async load(silent) {
      if (!silent) this.loading = true
      try {
        const params = Object.assign({}, this.query, {
          accountingPeriod: this.query.accountingPeriod ? this.query.accountingPeriod.format('YYYY-MM') : undefined,
          pageNo: this.pagination.current,
          pageSize: this.pagination.pageSize
        })
        const response = await queryReportBatches(params)
        if (!response.success) throw new Error(response.message)
        this.rows = response.result.records || []
        this.pagination.total = Number(response.result.total || 0)
      } catch (error) { if (!silent) this.$message.error(error.message || '批次加载失败') }
      finally { this.loading = false }
    },
    poll() {
      if (this.rows.some(item => item.status === 'PROCESSING' || item.status === 'QUEUED')) {
        this.load(true)
      }
    },
    search() { this.pagination.current = 1; this.load() },
    reset() { this.query = { sourceDomain: undefined, status: undefined, accountingPeriod: null, treasuryCode: '', fileName: '' }; this.search() },
    tableChanged(pagination) { this.pagination.current = pagination.current; this.pagination.pageSize = pagination.pageSize; this.load() },
    uploaded(result) { this.uploadVisible = false; this.selectedBatchId = result.batchId; this.detailVisible = true; this.search() },
    openDetail(record) { this.selectedBatchId = record.id; this.detailVisible = true },
    async remove(record) {
      try { const response = await deleteReportBatch(record.id); if (!response.success) throw new Error(response.message); this.$message.success(response.message); this.load() }
      catch (error) { this.$message.error(error.message || '删除失败') }
    },
    dateOnly(value) { return value ? String(value).slice(0, 10) : '—' },
    typeLabel(value) { return { ALL: '全部', INCOME: '收入', PAYOUT: '支出', STOCK: '库存', BACK: '退库' }[value] || value },
    stageLabel(value) { return { ARCHIVE: '归档', EXTRACT: '解压', PARSE: '解析', LOAD: '入库', PROCESS: '加工' }[value] || value },
    statusLabel(value) { return { QUEUED: '等待', PROCESSING: '执行中', SUCCEEDED: '成功', PARTIALLY_SUCCEEDED: '部分完成/等待加工', FAILED: '失败' }[value] || value },
    statusColor(value) { return { QUEUED: 'orange', PROCESSING: 'blue', SUCCEEDED: 'green', PARTIALLY_SUCCEEDED: 'orange', FAILED: 'red' }[value] || 'default' },
    processStatusLabel(value) {
      return { NOT_REQUIRED: '无需加工', WAITING_MANUAL: '待人工加工', DEPENDENCY_UNVERIFIED: '依赖未核验', QUEUED: '已排队', PROCESSING: '加工中', SUCCEEDED: '加工成功', FAILED: '加工失败' }[value] || value || '未开始'
    },
    processStatusColor(value) {
      return { WAITING_MANUAL: 'orange', DEPENDENCY_UNVERIFIED: 'red', QUEUED: 'blue', PROCESSING: 'blue', SUCCEEDED: 'green', FAILED: 'red' }[value] || 'default'
    }
  }
}
</script>

<style scoped>
.page-heading { display: flex; justify-content: space-between; align-items: center; margin-bottom: 18px; }
.page-title { margin: 0; font-size: 22px; }
.page-subtitle { margin: 4px 0 0; color: #888; }
.search-form { margin-bottom: 12px; }
.tracking-tip { margin-bottom: 16px; }
.reset-button { margin-left: 8px; }
.success-count { color: #52c41a; }
.error-count { color: #f5222d; }
</style>
