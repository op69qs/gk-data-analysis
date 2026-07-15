<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="方案名称">
              <a-input placeholder="请输入关键字" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="8" :sm="16">
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
          <a-col :md="6" :sm="8">
            <span class="table-page-search-submitButtons" style="float: left; overflow: hidden;">
              <a-button type="primary" icon="search" @click="searchQuery">查询</a-button>
              <a-button type="primary" icon="reload" style="margin-left: 8px" @click="handleReset">重置</a-button>
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
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
        <template slot="serial" slot-scope="text, record, index">
          {{ (ipagination.current - 1) * ipagination.pageSize + index + 1 }}
        </template>
        <template slot="action" slot-scope="text, record">
          <a @click="handleConvert(record)">转图</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete({ id: record.id })">
            <a>删除</a>
          </a-popconfirm>
        </template>
      </a-table>
    </div>

    <index-library-convert-modal ref="convertModal" @ok="searchQuery"></index-library-convert-modal>
  </a-card>
</template>

<script>
import { ListMixin } from '@/mixins/ListMixin'
import IndexLibraryConvertModal from './modules/IndexLibraryConvertModal'

export default {
  name: 'IndexLibraryList',
  mixins: [ListMixin],
  components: { IndexLibraryConvertModal },
  data() {
    return {
      createDateRange: [],
      queryParam: {},
      columns: [
        {
          title: '序号',
          dataIndex: 'serial',
          align: 'center',
          width: 80,
          scopedSlots: { customRender: 'serial' }
        },
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'create_time'
        },
        {
          title: '创建人',
          align: 'center',
          dataIndex: 'username'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' },
          align: 'center',
          width: 160
        }
      ],
      url: {
        list: '/vis/api/indexLibraryScheme/getPage',
        delete: '/vis/api/indexLibraryScheme/del'
      }
    }
  },
  methods: {
    onDateChange(dates, dateStrings) {
      this.queryParam.startDate = dateStrings[0] || undefined
      this.queryParam.endDate = dateStrings[1] || undefined
    },
    handleReset() {
      this.createDateRange = []
      this.queryParam = {}
      this.searchQuery()
    },
    handleConvert(record) {
      this.$refs.convertModal.open(record)
    }
  }
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
