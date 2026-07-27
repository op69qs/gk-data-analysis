<template>
  <a-drawer title="上报执行详情" :visible="visible" :width="drawerWidth" @close="$emit('close')">
    <a-spin :spinning="loading">
      <template v-if="detail.batch">
        <a-alert v-if="detail.batch.errorSummary" class="detail-alert" type="error" showIcon
                 :message="detail.batch.errorSummary" />
        <a-alert v-else-if="detail.batch.resultSummary" class="detail-alert" type="info" showIcon
                 :message="detail.batch.resultSummary" />
        <a-row class="summary-grid" :gutter="0">
          <a-col v-for="item in summaryItems" :key="item.label" :span="12" class="summary-item">
            <span class="summary-label">{{ item.label }}</span><span class="summary-value">{{ item.value }}</span>
          </a-col>
        </a-row>

        <div class="progress-block">
          <span>整体进度</span><a-progress :percent="detail.batch.progressPercent || 0" :status="progressStatus" />
        </div>

        <a-tabs>
          <a-tab-pane key="timeline" tab="执行过程">
            <ReportTaskTimeline :tasks="detail.tasks || []" :logs="detail.timeline || []" :files="detail.files || []" />
          </a-tab-pane>
          <a-tab-pane key="files" tab="文件与行数">
            <ReportFileTable :files="detail.files || []" @download="download" />
          </a-tab-pane>
          <a-tab-pane key="errors" :tab="`行级异常（${(detail.parseErrors || []).length}）`">
            <a-table rowKey="id" size="small" bordered :columns="errorColumns" :dataSource="detail.parseErrors || []"
                     :pagination="{ pageSize: 10 }">
              <template slot="errorFile" slot-scope="fileId">{{ fileName(fileId) }}</template>
            </a-table>
          </a-tab-pane>
          <a-tab-pane key="calls" :tab="`加工调用（${(detail.processCalls || []).length}）`">
            <a-table rowKey="id" size="small" bordered :columns="callColumns" :dataSource="detail.processCalls || []"
                     :pagination="false" />
          </a-tab-pane>
        </a-tabs>

        <div class="detail-actions">
          <a-button @click="retry('PARSE')">重新解析</a-button>
          <a-button @click="retry('LOAD')">重新入库</a-button>
          <a-tooltip v-if="showProcessAction" :title="processActionTip">
            <span>
              <a-button type="primary" :disabled="!canProcess" @click="retry('PROCESS')">
                {{ detail.batch.processCallStatus === 'WAITING_MANUAL' ? '调用下游加工' : '再次调用' }}
              </a-button>
            </span>
          </a-tooltip>
        </div>
      </template>
    </a-spin>
  </a-drawer>
</template>

<script>
import { getReportBatchDetail, retryReportTask, downloadReportFile } from '@/api/reporting'
import ReportTaskTimeline from './components/ReportTaskTimeline'
import ReportFileTable from './components/ReportFileTable'

export default {
  name: 'ReportBatchDetail',
  components: { ReportTaskTimeline, ReportFileTable },
  props: {
    visible: { type: Boolean, default: false },
    batchId: { type: String, default: '' }
  },
  data() {
    return {
      loading: false,
      pollTimer: null,
      detail: { batch: null, files: [], tasks: [], timeline: [], parseErrors: [], processCalls: [] },
      errorColumns: [
        { title: '文件', dataIndex: 'fileId', width: 150, scopedSlots: { customRender: 'errorFile' } }, { title: '工作表', dataIndex: 'sheetName', width: 100 },
        { title: '行', dataIndex: 'rowNumber', width: 65 }, { title: '列', dataIndex: 'columnName', width: 100 },
        { title: '原值', dataIndex: 'rawValue', ellipsis: true }, { title: '原因', dataIndex: 'errorMessage', ellipsis: true }
      ],
      callColumns: [
        { title: '账期参数', dataIndex: 'procedureArgument', width: 120 }, { title: '过程', dataIndex: 'procedureName' },
        { title: '状态', dataIndex: 'status', width: 100 }, { title: '开始', dataIndex: 'startedTime', width: 160 },
        { title: '结束', dataIndex: 'endedTime', width: 160 }, { title: '异常', dataIndex: 'errorMessage' }
      ]
    }
  },
  computed: {
    drawerWidth() { return Math.min(1100, Math.max(720, window.innerWidth * 0.82)) },
    progressStatus() { return this.detail.batch && this.detail.batch.status === 'FAILED' ? 'exception' : 'active' },
    showProcessAction() {
      const batch = this.detail.batch || {}
      return batch.sourceDomain === 'TIMS' && ['WAITING_MANUAL', 'DEPENDENCY_UNVERIFIED', 'FAILED', 'SUCCEEDED'].indexOf(batch.processCallStatus) >= 0
    },
    canProcess() {
      const batch = this.detail.batch || {}
      const active = (this.detail.tasks || []).some(item => item.status === 'QUEUED' || item.status === 'PROCESSING')
      return this.showProcessAction && batch.processCallStatus !== 'DEPENDENCY_UNVERIFIED' && !active
    },
    processActionTip() {
      const status = this.detail.batch && this.detail.batch.processCallStatus
      if (status === 'DEPENDENCY_UNVERIFIED') return 'ETL/ADM 表和存储过程尚未通过内网核验，当前禁止调用'
      if (!this.canProcess) return '当前批次已有任务正在排队或执行'
      return '严格按本批次账期调用 ADM 加工'
    },
    summaryItems() {
      const batch = this.detail.batch || {}
      return [
        { label: '批次号', value: batch.batchNo || '—' }, { label: '状态', value: this.statusLabel(batch.status) },
        { label: '来源 / 类型', value: `${batch.sourceDomain || '—'} / ${batch.businessType || '—'}` },
        { label: '账期', value: this.dateOnly(batch.accountingPeriod) },
        { label: '国库', value: `${batch.treasuryCode || '—'} ${batch.treasuryName || ''}` },
        { label: '当前阶段', value: this.stageLabel(batch.currentStage) },
        { label: '成功行', value: batch.successRowCount || 0 }, { label: '异常行', value: batch.errorRowCount || 0 },
        { label: '下游加工', value: this.processStatusLabel(batch.processCallStatus) }
      ]
    }
  },
  watch: {
    visible: { immediate: true, handler(value) { if (value && this.batchId) this.load() } },
    batchId(value) { if (this.visible && value) this.load() }
  },
  created() {
    this.pollTimer = window.setInterval(() => {
      if (this.visible && this.detail.batch && ['QUEUED', 'PROCESSING'].indexOf(this.detail.batch.status) >= 0) this.load(true)
    }, 3000)
  },
  beforeDestroy() { if (this.pollTimer) window.clearInterval(this.pollTimer) },
  methods: {
    async load(silent) {
      if (!silent) this.loading = true
      try {
        const response = await getReportBatchDetail(this.batchId)
        if (!response.success) throw new Error(response.message)
        this.detail = response.result
      } catch (error) {
        this.$message.error(error.message || '详情加载失败')
      } finally { if (!silent) this.loading = false }
    },
    async retry(type) {
      try {
        const response = await retryReportTask(this.batchId, type)
        if (!response.success) throw new Error(response.message)
        this.$message.success(response.message || '任务已排队')
        this.load()
        this.$emit('changed')
      } catch (error) { this.$message.error(error.message || '重试失败') }
    },
    async download(file) {
      try {
        const blob = await downloadReportFile(file.id)
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = file.originalName || 'report-file'
        link.click()
        window.URL.revokeObjectURL(url)
      } catch (error) { this.$message.error('文件下载失败') }
    },
    fileName(fileId) {
      const file = (this.detail.files || []).find(item => item.id === fileId)
      return file ? file.originalName : (fileId || '—')
    },
    stageLabel(value) { return { ARCHIVE: '归档', EXTRACT: '解压', PARSE: '解析', LOAD: '入库', PROCESS: '加工' }[value] || value },
    statusLabel(value) { return { QUEUED: '等待', PROCESSING: '执行中', SUCCEEDED: '成功', PARTIALLY_SUCCEEDED: '部分完成/等待加工', FAILED: '失败', LOGICALLY_DELETED: '已删除' }[value] || value },
    processStatusLabel(value) { return { NOT_REQUIRED: '无需加工', WAITING_MANUAL: '待人工加工', DEPENDENCY_UNVERIFIED: '依赖未核验', QUEUED: '已排队', PROCESSING: '加工中', SUCCEEDED: '加工成功', FAILED: '加工失败' }[value] || value || '未开始' },
    statusColor(value) { return { SUCCEEDED: 'green', PARTIALLY_SUCCEEDED: 'orange', FAILED: 'red', PROCESSING: 'blue', QUEUED: 'orange' }[value] || 'default' },
    dateOnly(value) { return value ? String(value).slice(0, 10) : '—' }
  }
}
</script>

<style scoped>
.detail-alert { margin-bottom: 16px; }
.summary-grid { border-top: 1px solid #e8e8e8; border-left: 1px solid #e8e8e8; }
.summary-item { display: flex; min-height: 42px; border-right: 1px solid #e8e8e8; border-bottom: 1px solid #e8e8e8; }
.summary-label { width: 108px; padding: 10px; background: #fafafa; color: #666; }
.summary-value { flex: 1; padding: 10px; }
.progress-block { margin: 18px 0; }
.detail-actions { display: flex; justify-content: flex-end; gap: 8px; padding: 18px 0; }
</style>
