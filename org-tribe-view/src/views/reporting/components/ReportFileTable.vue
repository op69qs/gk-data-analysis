<template>
  <a-table rowKey="id" size="small" bordered :pagination="false" :columns="columns" :dataSource="files">
    <template slot="status" slot-scope="value">
      <a-tag :color="value === 'SUCCEEDED' ? 'green' : value === 'FAILED' ? 'red' : 'orange'">{{ value || '—' }}</a-tag>
    </template>
    <template slot="counts" slot-scope="text, record">
      {{ record.successRowCount || 0 }} / {{ record.errorRowCount || 0 }}
    </template>
    <template slot="action" slot-scope="text, record">
      <a @click="$emit('download', record)">下载</a>
    </template>
  </a-table>
</template>

<script>
export default {
  name: 'ReportFileTable',
  props: { files: { type: Array, default: () => [] } },
  data() {
    return {
      columns: [
        { title: '文件', dataIndex: 'originalName', ellipsis: true },
        { title: '角色', dataIndex: 'fileRole', width: 90 },
        { title: '大小（字节）', dataIndex: 'fileSize', width: 120 },
        { title: '解析状态', dataIndex: 'parseStatus', width: 110, scopedSlots: { customRender: 'status' } },
        { title: '成功 / 异常行', width: 130, scopedSlots: { customRender: 'counts' } },
        { title: '操作', width: 70, scopedSlots: { customRender: 'action' } }
      ]
    }
  }
}
</script>
