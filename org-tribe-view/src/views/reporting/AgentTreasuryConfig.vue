<template>
  <a-card :bordered="false">
    <div class="page-heading"><div><h2>代理国库配置</h2><p>配置 KEY/TIMS 监控基线、有效期和启停状态</p></div><a-button type="primary" icon="plus" @click="openAdd">新增配置</a-button></div>
    <a-form layout="inline" class="search-form">
      <a-form-item label="国库代码"><a-input v-model.trim="query.treCode" allowClear /></a-form-item>
      <a-form-item label="国库名称"><a-input v-model.trim="query.treName" allowClear /></a-form-item>
      <a-form-item label="状态"><a-select v-model="query.state" allowClear style="width: 110px"><a-select-option value="0">启用</a-select-option><a-select-option value="1">停用</a-select-option></a-select></a-form-item>
      <a-form-item><a-button type="primary" @click="search">查询</a-button><a-button class="reset-button" @click="reset">重置</a-button></a-form-item>
    </a-form>
    <a-table rowKey="treCode" bordered :loading="loading" :columns="columns" :dataSource="rows" :pagination="pagination" @change="tableChanged">
      <template slot="state" slot-scope="value"><a-tag :color="value === '0' ? 'green' : 'default'">{{ value === '0' ? '启用' : '停用' }}</a-tag></template>
      <template slot="action" slot-scope="text, record"><a @click="openEdit(record)">编辑</a></template>
    </a-table>

    <a-modal :title="editing ? '编辑代理国库' : '新增代理国库'" :visible="modalVisible" :confirmLoading="saving" @ok="save" @cancel="modalVisible = false">
      <a-form layout="vertical">
        <a-form-item label="国库代码" required><a-input v-model.trim="form.treCode" :disabled="editing" /></a-form-item>
        <a-form-item label="国库名称" required><a-input v-model.trim="form.treName" /></a-form-item>
        <a-form-item label="有效期" required><a-range-picker v-model="dateRange" format="YYYY-MM-DD" style="width: 100%" /></a-form-item>
        <a-form-item label="状态" required><a-radio-group v-model="form.state"><a-radio value="0">启用</a-radio><a-radio value="1">停用</a-radio></a-radio-group></a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script>
import moment from 'moment'
import { queryAgentTreasuries, addAgentTreasury, updateAgentTreasury } from '@/api/reporting'

export default {
  name: 'AgentTreasuryConfig',
  data() {
    return {
      loading: false, saving: false, modalVisible: false, editing: false, rows: [], dateRange: [],
      query: { treCode: '', treName: '', state: undefined },
      form: { treCode: '', treName: '', state: '0' },
      pagination: { current: 1, pageSize: 10, total: 0, showSizeChanger: true },
      columns: [
        { title: '国库代码', dataIndex: 'treCode', width: 150 }, { title: '国库名称', dataIndex: 'treName' },
        { title: '开始日期', dataIndex: 'startDate', width: 130 }, { title: '结束日期', dataIndex: 'endDate', width: 130 },
        { title: '状态', dataIndex: 'state', width: 90, scopedSlots: { customRender: 'state' } },
        { title: '创建人', dataIndex: 'addUserid', width: 100 }, { title: '修改时间', dataIndex: 'modTime', width: 170 },
        { title: '操作', width: 70, scopedSlots: { customRender: 'action' } }
      ]
    }
  },
  created() { this.load() },
  methods: {
    async load() {
      this.loading = true
      try {
        const response = await queryAgentTreasuries(Object.assign({}, this.query, { pageNo: this.pagination.current, pageSize: this.pagination.pageSize }))
        if (!response.success) throw new Error(response.message)
        this.rows = response.result.records || []; this.pagination.total = Number(response.result.total || 0)
      } catch (error) { this.$message.error(error.message || '配置加载失败') }
      finally { this.loading = false }
    },
    search() { this.pagination.current = 1; this.load() },
    reset() { this.query = { treCode: '', treName: '', state: undefined }; this.search() },
    tableChanged(page) { this.pagination.current = page.current; this.pagination.pageSize = page.pageSize; this.load() },
    openAdd() { this.editing = false; this.form = { treCode: '', treName: '', state: '0' }; this.dateRange = []; this.modalVisible = true },
    openEdit(row) {
      this.editing = true
      this.form = { treCode: row.treCode, treName: row.treName, state: row.state }
      this.dateRange = [moment(row.startDate), moment(row.endDate)]
      this.modalVisible = true
    },
    async save() {
      if (!this.form.treCode || !this.form.treName || this.dateRange.length !== 2) return this.$message.warning('请完整填写国库和有效期')
      const payload = Object.assign({}, this.form, { startDate: this.dateRange[0].format('YYYY-MM-DD'), endDate: this.dateRange[1].format('YYYY-MM-DD') })
      this.saving = true
      try {
        const response = await (this.editing ? updateAgentTreasury(this.form.treCode, payload) : addAgentTreasury(payload))
        if (!response.success) throw new Error(response.message)
        this.$message.success(response.message); this.modalVisible = false; this.load()
      } catch (error) { this.$message.error(error.message || '保存失败') }
      finally { this.saving = false }
    }
  }
}
</script>

<style scoped>
.page-heading { display: flex; justify-content: space-between; align-items: center; }
.page-heading h2 { margin-bottom: 2px; }
.page-heading p { color: #888; }
.search-form { margin: 14px 0; }
.reset-button { margin-left: 8px; }
</style>
