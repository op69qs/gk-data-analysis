<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="loadData(true)">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="类型名称">
              <a-input v-model="queryParam.business_name" placeholder="请输入类型名称" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="状态">
              <a-select v-model="queryParam.business_state" placeholder="请选择状态" allow-clear>
                <a-select-option value="0">启用</a-select-option>
                <a-select-option value="1">停用</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <div class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="loadData(true)">查询</a-button>
              <a-button style="margin-left: 8px" icon="reload" @click="resetQuery">重置</a-button>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <a-table
      rowKey="business_id"
      bordered
      size="middle"
      :columns="columns"
      :data-source="dataSource"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange">
      <template slot="business_state" slot-scope="text">
        <a-tag :color="text === '0' ? 'green' : 'orange'">{{ text === '0' ? '启用' : '停用' }}</a-tag>
      </template>
    </a-table>
  </a-card>
</template>

<script>
import { getBusinessTypePageList } from '@/api/visScreen'

export default {
  name: 'VisSystemBusinessTypeList',
  data() {
    return {
      queryParam: {
        business_name: '',
        business_state: undefined
      },
      loading: false,
      dataSource: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showTotal: total => `共 ${total} 条`
      },
      columns: [
        { title: '类型名称', dataIndex: 'business_name' },
        { title: '类型编码', dataIndex: 'business_id', align: 'center' },
        { title: '状态', dataIndex: 'business_state', align: 'center', scopedSlots: { customRender: 'business_state' } }
      ]
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    resetQuery() {
      this.queryParam = {
        business_name: '',
        business_state: undefined
      }
      this.loadData(true)
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current
      this.pagination.pageSize = pagination.pageSize
      this.loadData()
    },
    loadData(reset) {
      if (reset) {
        this.pagination.current = 1
      }
      this.loading = true
      const params = {
        ...this.queryParam,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      getBusinessTypePageList(params).then((res) => {
        if (res && res.result === 'success') {
          this.dataSource = res.rows || []
          this.pagination.total = res.total || 0
        } else {
          this.dataSource = []
          this.pagination.total = 0
        }
      }).catch(() => {
        this.dataSource = []
        this.pagination.total = 0
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>