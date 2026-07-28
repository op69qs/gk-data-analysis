<template>
  <div class="task-timeline">
    <a-empty v-if="!tasks.length" description="暂无执行记录" />
    <a-timeline v-else>
      <a-timeline-item v-for="task in tasks" :key="task.id" :color="statusColor(task.status)">
        <div class="task-title">
          <span>{{ stageLabel(task.taskType) }}</span>
          <a-tag :color="tagColor(task.status)">{{ statusLabel(task.status) }}</a-tag>
          <span v-if="task.attemptNo > 1" class="attempt">第 {{ task.attemptNo }} 次</span>
        </div>
        <div class="task-meta">开始：{{ task.startedTime || '尚未开始' }}　结束：{{ task.endedTime || '—' }}　耗时：{{ duration(task) }}</div>
        <div v-if="task.resultSummary" class="task-result">{{ task.resultSummary }}</div>
        <a-alert v-if="task.errorMessage" class="task-error" type="error" showIcon :message="task.errorMessage" />
        <ul v-if="taskLogs(task.id).length" class="task-logs">
          <li v-for="log in taskLogs(task.id)" :key="log.id">
            <span class="log-time">{{ log.eventTime }}</span>
            <a-tag :color="tagColor(log.toStatus)">{{ statusLabel(log.toStatus) }}</a-tag>
            <span v-if="log.fileId" class="log-file">{{ fileName(log.fileId) }}</span>
            <span>{{ log.message }}</span>
            <span v-if="log.processedRowCount || log.errorRowCount" class="log-counts">
              已处理 {{ log.processedRowCount || 0 }}，成功 {{ log.successRowCount || 0 }}，异常 {{ log.errorRowCount || 0 }}
            </span>
          </li>
        </ul>
      </a-timeline-item>
    </a-timeline>
  </div>
</template>

<script>
export default {
  name: 'ReportTaskTimeline',
  props: {
    tasks: { type: Array, default: () => [] },
    logs: { type: Array, default: () => [] },
    files: { type: Array, default: () => [] }
  },
  methods: {
    taskLogs(taskId) { return this.logs.filter(item => item.taskId === taskId) },
    fileName(fileId) {
      const file = this.files.find(item => item.id === fileId)
      return file ? file.originalName : fileId
    },
    stageLabel(value) {
      return { ARCHIVE: '原件归档', EXTRACT: '安全解压', PARSE: '文件解析', LOAD: '数据入库', PROCESS: '加工' }[value] || value
    },
    statusLabel(value) {
      return { QUEUED: '等待执行', PROCESSING: '执行中', PREPARED: '已解析待提交', COMMITTING: '入库中', SUCCEEDED: '已提交', FAILED: '执行失败', PARTIALLY_SUCCEEDED: '部分成功', CANCELLED: '已取消' }[value] || value
    },
    statusColor(value) {
      if (value === 'SUCCEEDED') return 'green'
      if (value === 'FAILED') return 'red'
      if (value === 'PROCESSING') return 'blue'
      return 'gray'
    },
    tagColor(value) {
      return { SUCCEEDED: 'green', PARTIALLY_SUCCEEDED: 'orange', FAILED: 'red', PROCESSING: 'blue', PREPARED: 'cyan', COMMITTING: 'blue', QUEUED: 'orange' }[value] || 'default'
    },
    duration(task) {
      return task.durationMs == null ? '—' : `${task.durationMs} ms`
    }
  }
}
</script>

<style scoped>
.task-title { display: flex; align-items: center; gap: 8px; font-weight: 600; }
.task-meta { margin-top: 5px; color: #777; font-size: 12px; }
.task-result { margin-top: 5px; color: #444; }
.task-error { margin-top: 8px; }
.attempt { color: #999; font-size: 12px; }
.task-logs { margin: 8px 0 0; padding: 8px 12px 8px 28px; background: #fafafa; color: #555; font-size: 12px; }
.task-logs li { margin: 4px 0; }
.log-time { margin-right: 8px; color: #888; }
.log-file { margin-right: 8px; color: #1890ff; }
.log-counts { display: block; color: #777; }
</style>
