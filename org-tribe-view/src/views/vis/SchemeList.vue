<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="loadSchemes(true)">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="方案名称">
              <a-input v-model="queryParam.name" placeholder="请输入关键字" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <div class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="loadSchemes(true)">查询</a-button>
              <a-button style="margin-left: 8px" icon="reload" @click="resetQuery">重置</a-button>
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
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange">
      <template slot="action" slot-scope="text, record">
        <a @click="handlePreview(record)">预览</a>
      </template>
    </a-table>
  </a-card>
</template>

<script>
import { getSchemePageList } from '@/api/visScreen'

export default {
  name: 'VisSchemeList',
  data() {
    return {
      queryParam: {
        name: ''
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
        { title: '方案名称', dataIndex: 'name' },
        { title: '添加人', dataIndex: 'username', align: 'center' },
        { title: '添加时间', dataIndex: 'add_time', align: 'center' },
        { title: '操作', dataIndex: 'action', align: 'center', width: 120, scopedSlots: { customRender: 'action' } }
      ]
    }
  },
  created() {
    this.loadSchemes()
  },
  methods: {
    resetQuery() {
      this.queryParam = { name: '' }
      this.loadSchemes(true)
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current
      this.pagination.pageSize = pagination.pageSize
      this.loadSchemes()
    },
    handlePreview(record) {
      this.$confirm({
        title: '大屏自动轮播设置',
        content: '是否设置大屏自动轮播',
        okText: '是',
        cancelText: '否',
        onOk: () => {
          this.$router.push({
            path: '/vis/preview',
            query: {
              schemeId: record.id,
              info: record.id,
              interval: record.rotation_interval,
              autoSetting: '1'
            }
          })
        },
        onCancel: () => {
          this.$router.push({
            path: '/vis/preview',
            query: {
              schemeId: record.id,
              info: record.id,
              interval: record.rotation_interval,
              autoSetting: '0'
            }
          })
        }
      })
    },
    loadSchemes(reset) {
      if (reset) {
        this.pagination.current = 1
      }
      this.loading = true
      const params = {
        ...this.queryParam,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      getSchemePageList(params).then((res) => {
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