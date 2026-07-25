<template>
  <a-card :bordered="false">
    <div class="page-heading"><h2>收入 / 支出调整记录</h2><p>查询 EDW 报送明细，并通过追加记录覆盖展示金额；不直接修改基线明细。</p></div>
    <a-form layout="inline" class="search-form">
      <a-form-item label="类型"><a-radio-group v-model="query.type" buttonStyle="solid" @change="search"><a-radio-button value="income">收入</a-radio-button><a-radio-button value="payout">支出</a-radio-button></a-radio-group></a-form-item>
      <a-form-item label="账期"><a-month-picker v-model="query.period" format="YYYY-MM" /></a-form-item>
      <a-form-item label="国库代码"><a-input v-model.trim="query.treCode" allowClear /></a-form-item>
      <a-form-item label="统计代码"><a-input v-model.trim="query.statisticsCode" allowClear /></a-form-item>
      <a-form-item label="预算级次"><a-input v-model.trim="query.budgetLevel" allowClear /></a-form-item>
      <a-form-item><a-button type="primary" @click="search">查询</a-button><a-button class="reset-button" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-tabs v-model="activeTab" @change="search">
      <a-tab-pane key="source" tab="报送明细（含最新调整）" />
      <a-tab-pane key="history" tab="调整历史" />
    </a-tabs>
    <a-table rowKey="rowKey" bordered :loading="loading" :columns="activeTab === 'source' ? sourceColumns : historyColumns"
             :dataSource="rowsWithKey" :pagination="pagination" @change="tableChanged">
      <template slot="amount" slot-scope="value"><span class="amount">{{ amount(value) }}</span></template>
      <template slot="difference" slot-scope="value"><span :class="Number(value) >= 0 ? 'positive' : 'negative'">{{ amount(value) }}</span></template>
      <template slot="action" slot-scope="text, record"><a @click="openChange(record)">新增调整</a></template>
    </a-table>

    <a-modal title="新增金额调整" :visible="modalVisible" :confirmLoading="saving" @ok="save" @cancel="modalVisible = false">
      <a-alert type="warning" showIcon class="change-tip" message="系统会重新读取 EDW 原金额并计算差额，页面传入的原金额不作为入库依据。" />
      <div class="change-summary">
        <div class="change-row"><span>类型</span><b>{{ query.type === 'income' ? '收入' : '支出' }}</b></div>
        <div class="change-row"><span>国库</span><b>{{ changeForm.treasuryCode }}</b></div>
        <div class="change-row"><span>统计代码</span><b>{{ changeForm.statisticsCode }}</b></div>
        <div class="change-row"><span>预算级次</span><b>{{ changeForm.budgetLevel }}</b></div>
        <div class="change-row"><span>当前显示金额</span><b>{{ amount(changeForm.oldAmount) }}</b></div>
      </div>
      <a-form-item class="new-amount" label="新金额" required><a-input-number v-model="changeForm.newAmount" :precision="2" style="width: 100%" /></a-form-item>
    </a-modal>
  </a-card>
</template>

<script>
import { queryChangeSource, queryChangeHistory, addReportChange } from '@/api/reporting'

export default {
  name: 'ReportChangeRecord',
  data() {
    return {
      activeTab: 'source', loading: false, saving: false, modalVisible: false, rows: [],
      query: { type: 'income', period: null, treCode: '', statisticsCode: '', budgetLevel: '' },
      changeForm: { accountingDate: '', treasuryCode: '', statisticsCode: '', budgetLevel: '', oldAmount: 0, newAmount: null },
      pagination: { current: 1, pageSize: 10, total: 0, showSizeChanger: true },
      sourceColumns: [
        { title: '账期', dataIndex: 'd_acct', width: 110 }, { title: '国库', dataIndex: 'treasury', width: 130, customRender: (v, r) => r.s_tratrecode || r.s_trecode },
        { title: '统计代码', dataIndex: 'statistics_code' }, { title: '预算级次', dataIndex: 'level', width: 100, customRender: (v, r) => r.c_bdglevel || r.guoku_lvl },
        { title: '本月金额', dataIndex: 'month_f_amt', width: 140, scopedSlots: { customRender: 'amount' } },
        { title: '本年金额', dataIndex: 'year_f_amt', width: 140, scopedSlots: { customRender: 'amount' } },
        { title: '操作', width: 90, scopedSlots: { customRender: 'action' } }
      ],
      historyColumns: [
        { title: '调整时间', dataIndex: 'update_date', width: 170 }, { title: '类型', dataIndex: 'type', width: 80 },
        { title: '账期', dataIndex: 'd_acct', width: 110 }, { title: '国库', dataIndex: 's_trecode', width: 130 },
        { title: '统计代码', dataIndex: 'statistics_code' }, { title: '级次', dataIndex: 'c_bdglevel', width: 80 },
        { title: '原金额', dataIndex: 'old_f_amt', width: 120, scopedSlots: { customRender: 'amount' } },
        { title: '新金额', dataIndex: 'new_f_amt', width: 120, scopedSlots: { customRender: 'amount' } },
        { title: '差额', dataIndex: 'diff_f_amt', width: 120, scopedSlots: { customRender: 'difference' } },
        { title: '操作人', dataIndex: 'update_user', width: 100 }
      ]
    }
  },
  computed: { rowsWithKey() { return this.rows.map((item, index) => Object.assign({ rowKey: `${item.type || this.query.type}-${item.d_acct}-${item.statistics_code}-${index}` }, item)) } },
  created() { this.load() },
  methods: {
    params() {
      return {
        pageNo: this.pagination.current, pageSize: this.pagination.pageSize, type: this.query.type,
        bizDate: this.query.period ? this.query.period.clone().endOf('month').format('YYYY-MM-DD') : undefined,
        treCode: this.query.treCode || undefined, statisticsCode: this.query.statisticsCode || undefined,
        budgetLevel: this.query.budgetLevel || undefined
      }
    },
    async load() {
      this.loading = true
      try {
        const response = await (this.activeTab === 'source' ? queryChangeSource(this.params()) : queryChangeHistory(this.params()))
        if (!response.success) throw new Error(response.message)
        this.rows = response.result.records || []; this.pagination.total = Number(response.result.total || 0)
      } catch (error) { this.$message.error(error.message || '数据加载失败') }
      finally { this.loading = false }
    },
    search() { this.pagination.current = 1; this.load() },
    reset() { this.query = { type: 'income', period: null, treCode: '', statisticsCode: '', budgetLevel: '' }; this.search() },
    tableChanged(page) { this.pagination.current = page.current; this.pagination.pageSize = page.pageSize; this.load() },
    openChange(row) {
      this.changeForm = {
        accountingDate: String(row.d_acct).slice(0, 10), treasuryCode: row.s_tratrecode || row.s_trecode,
        statisticsCode: row.statistics_code, budgetLevel: row.c_bdglevel || row.guoku_lvl,
        oldAmount: row.month_f_amt, newAmount: null
      }
      this.modalVisible = true
    },
    async save() {
      if (this.changeForm.newAmount == null) return this.$message.warning('请输入新金额')
      this.saving = true
      try {
        const response = await addReportChange(Object.assign({}, this.changeForm, { type: this.query.type }))
        if (!response.success) throw new Error(response.message)
        this.$message.success(response.message); this.modalVisible = false; this.load()
      } catch (error) { this.$message.error(error.message || '调整记录保存失败') }
      finally { this.saving = false }
    },
    amount(value) { return value == null || value === '' ? '—' : Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 }) }
  }
}
</script>

<style scoped>
.page-heading h2 { margin-bottom: 2px; }
.page-heading p { color: #888; }
.search-form { margin-top: 16px; }
.reset-button { margin-left: 8px; }
.amount { font-variant-numeric: tabular-nums; }
.positive { color: #52c41a; }
.negative { color: #f5222d; }
.change-tip { margin-bottom: 16px; }
.change-summary { border: 1px solid #e8e8e8; }
.change-row { display: flex; border-bottom: 1px solid #e8e8e8; }
.change-row:last-child { border-bottom: 0; }
.change-row span { width: 120px; padding: 9px; background: #fafafa; color: #666; }
.change-row b { flex: 1; padding: 9px; font-weight: 500; }
.new-amount { margin-top: 16px; }
</style>
