<template>
  <a-card :bordered="false">
    <div class="monitor-heading"><h2>上报监控</h2><span>以代理国库配置为基线检查文件完整性、处理状态与异常</span></div>
    <a-tabs v-model="activeTab" @change="search">
      <a-tab-pane key="KEY" tab="KEY 监控" />
      <a-tab-pane key="TIMS" tab="TIMS 收入/支出/库存监控" />
    </a-tabs>
    <a-form layout="inline" class="search-form">
      <a-form-item label="账期"><a-month-picker v-model="query.period" format="YYYY-MM" /></a-form-item>
      <a-form-item label="国库代码"><a-input v-model.trim="query.treCode" allowClear /></a-form-item>
      <a-form-item label="国库名称"><a-input v-model.trim="query.treName" allowClear /></a-form-item>
      <a-form-item :label="activeTab === 'KEY' ? 'ZIP 文件' : '文件名'"><a-input v-model.trim="query.fileName" allowClear /></a-form-item>
      <a-form-item label="执行结果"><a-select v-model="query.exeState" allowClear style="width: 120px"><a-select-option value="0">无异常</a-select-option><a-select-option value="1">有异常</a-select-option></a-select></a-form-item>
      <a-form-item v-if="activeTab === 'TIMS'" label="三类齐全"><a-select v-model="query.reported" allowClear style="width: 120px"><a-select-option value="0">已齐全</a-select-option><a-select-option value="1">未齐全</a-select-option></a-select></a-form-item>
      <a-form-item><a-button type="primary" @click="search">查询</a-button><a-button class="reset-button" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-table rowKey="rowKey" bordered :loading="loading" :columns="activeTab === 'KEY' ? keyColumns : timsColumns"
             :dataSource="rowsWithKey" :pagination="pagination" @change="tableChanged">
      <template slot="fileState" slot-scope="text, record">
        <a-tag :color="record.state === '0' ? 'green' : 'orange'">{{ record.state_dscr || '未上报' }}</a-tag>
      </template>
      <template slot="keyComplete" slot-scope="text, record">
        <a-tooltip :title="keyTooltip(record)"><a-tag :color="keyComplete(record) ? 'green' : 'red'">{{ keyComplete(record) ? '四类齐全' : '文件不全' }}</a-tag></a-tooltip>
      </template>
      <template slot="timsComplete" slot-scope="text, record">
        <a-tag :color="record.sort === 2 ? 'green' : record.sort === 1 ? 'red' : 'orange'">{{ record.sort === 2 ? '三类齐全' : record.sort === 1 ? '存在异常' : '文件不全' }}</a-tag>
      </template>
      <template slot="exception" slot-scope="value"><span :class="value ? 'exception-text' : ''">{{ value || '—' }}</span></template>
    </a-table>
  </a-card>
</template>

<script>
import { queryKeyMonitoring, queryTimsMonitoring } from '@/api/reporting'

export default {
  name: 'ReportMonitoring',
  data() {
    return {
      activeTab: 'KEY', loading: false, rows: [],
      query: { period: null, treCode: '', treName: '', fileName: '', exeState: undefined, reported: undefined },
      pagination: { current: 1, pageSize: 10, total: 0, showSizeChanger: true },
      keyColumns: [
        { title: '国库代码', dataIndex: 'tre_code', width: 130 }, { title: '国库名称', dataIndex: 'tre_name' },
        { title: '账期', dataIndex: 'biz_date', width: 110 }, { title: 'ZIP', dataIndex: 'zip_name', ellipsis: true },
        { title: '文件完整性', width: 105, scopedSlots: { customRender: 'keyComplete' } },
        { title: '收入/支出/库存/退库行数', customRender: (text, row) => `${row.sr_count || 0} / ${row.zc_count || 0} / ${row.kc_count || 0} / ${row.tk_count || 0}` },
        { title: '状态', width: 100, scopedSlots: { customRender: 'fileState' } }
      ],
      timsColumns: [
        { title: '国库代码', dataIndex: 'tre_code', width: 130 }, { title: '国库名称', dataIndex: 'tre_name' },
        { title: '账期', dataIndex: 'biz_date', width: 110 }, { title: '类型', dataIndex: 'biz_type_desc', width: 80 },
        { title: '文件', dataIndex: 'file_name', ellipsis: true }, { title: '行数', dataIndex: 'data_count', width: 80 },
        { title: '完整性', width: 100, scopedSlots: { customRender: 'timsComplete' } },
        { title: '异常', dataIndex: 'file_exception', ellipsis: true, scopedSlots: { customRender: 'exception' } },
        { title: '状态', width: 100, scopedSlots: { customRender: 'fileState' } }
      ]
    }
  },
  computed: {
    rowsWithKey() { return this.rows.map((item, index) => Object.assign({ rowKey: `${item.tre_code || 'none'}-${item.biz_type || 'key'}-${item.id || index}` }, item)) }
  },
  created() { this.load() },
  methods: {
    async load() {
      this.loading = true
      try {
        const params = {
          pageNo: this.pagination.current, pageSize: this.pagination.pageSize,
          bizDate: this.query.period ? this.query.period.clone().endOf('month').format('YYYY-MM-DD') : undefined,
          treCode: this.query.treCode || undefined, treName: this.query.treName || undefined,
          exeState: this.query.exeState, reported: this.query.reported
        }
        if (this.activeTab === 'KEY') params.zipName = this.query.fileName || undefined
        else params.fileName = this.query.fileName || undefined
        const response = await (this.activeTab === 'KEY' ? queryKeyMonitoring(params) : queryTimsMonitoring(params))
        if (!response.success) throw new Error(response.message)
        this.rows = response.result.records || []
        this.pagination.total = Number(response.result.total || 0)
      } catch (error) { this.$message.error(error.message || '监控数据加载失败') }
      finally { this.loading = false }
    },
    search() { this.pagination.current = 1; this.load() },
    reset() { this.query = { period: null, treCode: '', treName: '', fileName: '', exeState: undefined, reported: undefined }; this.search() },
    tableChanged(page) { this.pagination.current = page.current; this.pagination.pageSize = page.pageSize; this.load() },
    keyComplete(row) { return Boolean(row.sr_name && row.zc_name && row.kc_name && row.tk_name) },
    keyTooltip(row) { return `收入：${row.sr_name || '缺少'}；支出：${row.zc_name || '缺少'}；库存：${row.kc_name || '缺少'}；退库：${row.tk_name || '缺少'}` }
  }
}
</script>

<style scoped>
.monitor-heading { display: flex; align-items: baseline; gap: 12px; }
.monitor-heading h2 { margin-bottom: 0; }
.monitor-heading span { color: #888; }
.search-form { margin: 8px 0 16px; }
.reset-button { margin-left: 8px; }
.exception-text { color: #f5222d; }
</style>
