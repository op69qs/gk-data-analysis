<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="loadData">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="国库名称">
              <a-input v-model="queryParam.guoku_dscr" placeholder="请输入名称查询" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="状态">
              <a-select v-model="queryParam.state" placeholder="请选择状态" allow-clear>
                <a-select-option value="0">启用</a-select-option>
                <a-select-option value="1">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <div class="table-page-search-submitButtons">
              <a-button type="primary" @click="loadData">查询</a-button>
              <a-button style="margin-left: 8px" @click="resetQuery">重置</a-button>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-table
      rowKey="id"
      bordered
      size="middle"
      :columns="columns"
      :data-source="dataSource"
      :pagination="false"
      :loading="loading"
      :default-expand-all-rows="true">
      <template slot="state" slot-scope="text">
        <a-tag :color="text === '0' ? 'green' : 'orange'">{{ text === '0' ? '启用' : '停用' }}</a-tag>
      </template>
    </a-table>
  </a-card>
</template>

<script>
import { getTreasuryTreeList } from '@/api/visScreen'

export default {
  name: 'VisSystemTreasuryList',
  data() {
    return {
      queryParam: {
        guoku_dscr: '',
        state: undefined
      },
      loading: false,
      dataSource: [],
      columns: [
        { title: '国库名称', dataIndex: 'guoku_dscr', align: 'left' },
        { title: '国库编码', dataIndex: 'guoku_id', align: 'left' },
        { title: '国库级次', dataIndex: 'level_dscr', align: 'left' },
        { title: '国库属性', dataIndex: 'guoku_shuxing_dscr', align: 'left' },
        { title: '地区', dataIndex: 'area_dscr', align: 'left' },
        { title: '核算主体名称', dataIndex: 'bookorgname', align: 'left' },
        { title: '状态', dataIndex: 'state', align: 'center', scopedSlots: { customRender: 'state' } }
      ]
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    resetQuery() {
      this.queryParam = {
        guoku_dscr: '',
        state: undefined
      }
      this.loadData()
    },
    loadData() {
      this.loading = true
      getTreasuryTreeList({ ...this.queryParam }).then((res) => {
        if (res && res.result === 'success') {
          this.dataSource = res.rows || []
        } else {
          this.dataSource = []
        }
      }).catch(() => {
        this.dataSource = []
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>