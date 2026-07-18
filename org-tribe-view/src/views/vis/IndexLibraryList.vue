<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="24">
            <a-form-item label="方案名称">
              <a-input placeholder="请输入关键字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="24">
            <a-form-item label="创建日期">
              <a-range-picker
                v-model="createDateRange"
                style="width: 100%"
                format="YYYY-MM-DD"
                :placeholder="['开始日期', '结束日期']"
                @change="onDateChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="24">
            <span class="table-page-search-submitButtons" style="float: left; overflow: hidden;">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button icon="reload" style="margin-left: 8px" @click="handleReset">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div>
      <a-table
        ref="table"
        bordered
        size="middle"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :scroll="{ x: 800 }"
        @change="handleTableChange"
      >
        <template slot="indexName" slot-scope="text, record">
          {{ record.raw && record.raw.INDEX_NAME }}
        </template>
        <template slot="creator" slot-scope="text, record">
          {{ record.raw && record.raw.realname }}
        </template>
        <template slot="action" slot-scope="text, record">
          <a href="#" @click.prevent="handleConvert(record)">转图</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">
            <a href="#" @click.prevent>删除</a>
          </a-popconfirm>
        </template>
      </a-table>
    </div>

    <index-library-convert-modal ref="convertModal" @ok="searchQuery"></index-library-convert-modal>
  </a-card>
</template>

<script>
import { listSchemes, deleteScheme } from '@/api/indexLibraryScheme'
import { normalizeSchemeRow } from '@/utils/indexLibraryScheme'
import IndexLibraryConvertModal from './modules/IndexLibraryConvertModal'

export default {
  name: 'IndexLibraryList',
  components: { IndexLibraryConvertModal },
  data() {
    return {
      createDateRange: [],
      queryParam: {
        name: '',
        begin_time: '',
        end_time: ''
      },
      loading: false,
      requestRevision: 0,
      dataSource: [],
      pagination: {
        current: 1,
        pageSize: 10,
        pageSizeOptions: ['10', '20', '50'],
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: total => `共${total}条`,
        total: 0
      },
      columns: [
        {
          title: '方案名称',
          dataIndex: 'name',
          width: 220
        },
        {
          title: '指标名称',
          dataIndex: 'indexName',
          scopedSlots: { customRender: 'indexName' }
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'username',
          width: 140,
          scopedSlots: { customRender: 'creator' }
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
          width: 180
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 160
        }
      ]
    }
  },
  created() {
    this.loadData()
  },
  beforeDestroy() {
    this.requestRevision += 1
  },
  methods: {
    onDateChange(dates, dateStrings) {
      this.queryParam.begin_time = this.formatDateValue(dates[0], dateStrings[0])
      this.queryParam.end_time = this.formatDateValue(dates[1], dateStrings[1])
    },
    formatDateValue(date, fallback) {
      if (date && typeof date.format === 'function') {
        return date.format('YYYY-MM-DD')
      }
      return fallback || ''
    },
    searchQuery() {
      this.pagination.current = 1
      return this.loadData()
    },
    handleReset() {
      this.createDateRange = []
      this.queryParam = {
        name: '',
        begin_time: '',
        end_time: ''
      }
      return this.searchQuery()
    },
    loadData() {
      const requestRevision = ++this.requestRevision
      const beginTime = this.queryParam.begin_time || ''
      const endTime = this.queryParam.end_time || ''
      if (beginTime && endTime && beginTime > endTime) {
        this.loading = false
        this.$message.error('开始日期不能大于结束日期')
        return Promise.resolve(false)
      }

      const params = {
        name: this.queryParam.name || '',
        begin_time: beginTime,
        end_time: endTime,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      this.loading = true
      return listSchemes(params).then(res => {
        if (requestRevision !== this.requestRevision) {
          return
        }
        if (res && res.result === 'success') {
          const rows = Array.isArray(res.rows) ? res.rows : []
          this.dataSource = rows.map(normalizeSchemeRow)
          this.pagination.total = Number(res.total) || 0
        } else {
          this.dataSource = []
          this.pagination.total = 0
          this.$message.error((res && res.msg) || '方案列表加载失败')
        }
      }).catch(() => {
        if (requestRevision !== this.requestRevision) {
          return
        }
        this.dataSource = []
        this.pagination.total = 0
        this.$message.error('方案列表加载失败，请稍后重试')
      }).finally(() => {
        if (requestRevision === this.requestRevision) {
          this.loading = false
        }
      })
    },
    handleTableChange(pagination) {
      this.pagination.current = pagination.current
      this.pagination.pageSize = pagination.pageSize
      return this.loadData()
    },
    handleDelete(record) {
      const schemeId = record.id || record.ID || (record.raw && record.raw.ID)
      if (!schemeId) {
        this.$message.error('方案ID缺失，无法删除')
        return Promise.resolve(false)
      }
      return deleteScheme({ schemeId }).then(res => {
        if (res && res.result === 'success') {
          this.$message.success(res.msg || '删除指标方案成功')
          if (this.dataSource.length === 1 && this.pagination.current > 1) {
            this.pagination.current -= 1
          }
          return this.loadData()
        }
        this.$message.error((res && res.msg) || '删除指标方案失败')
        return false
      }).catch(() => {
        this.$message.error('删除指标方案失败，请稍后重试')
        return false
      })
    },
    handleConvert(record) {
      this.$refs.convertModal.open(record.raw)
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
