<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="loadPages(true)">
        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="页面名称">
              <a-input v-model="queryParam.name" placeholder="请输入关键字" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="状态">
              <a-select v-model="queryParam.state" placeholder="请选择状态" allow-clear>
                <a-select-option value="0">发布</a-select-option>
                <a-select-option value="1">未发布</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <div class="table-page-search-submitButtons">
              <a-button type="primary" icon="search" @click="loadPages(true)">查询</a-button>
              <a-button style="margin-left: 8px" icon="reload" @click="resetQuery">重置</a-button>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="openEditor('add')">新增页面</a-button>
    </div>

    <a-spin :spinning="loading" tip="加载中...">
      <a-row v-if="dataSource.length" :gutter="[24, 24]">
        <a-col v-for="item in dataSource" :key="item.id" :xl="8" :lg="12" :md="12" :sm="24">
          <a-card hoverable>
            <img :src="resolveMediaUrl(item.thumbnail)" :alt="item.name" class="vis-page-list__poster" />
            <template slot="actions">
              <div class="vis-page-list__actions">
                <span @click="openEditor('edit', item)"><a-icon type="edit" /> 编辑</span>
                <span @click="deletePage(item)"><a-icon type="delete" /> 删除</span>
                <span v-if="item.state === '1'" @click="publishPage(item)">发布</span>
                <span v-else class="vis-page-list__action-disabled">已发布</span>
              </div>
            </template>
            <a-card-meta :title="item.name">
              <template slot="description">
                <span>更新时间：{{ item.update_time || '--' }}</span>
              </template>
            </a-card-meta>
          </a-card>
        </a-col>
      </a-row>
      <a-empty v-else description="暂无页面数据" />
    </a-spin>
    <div class="vis-page-list__pagination" v-if="pagination.total > pagination.pageSize">
      <a-pagination
        :current="pagination.current"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        show-size-changer
        :show-total="pagination.showTotal"
        @change="handlePageChange"
        @showSizeChange="handlePageChange" />
    </div>
  </a-card>
</template>

<script>
import { deletePageInfo, editPageState, getPageList } from '@/api/visScreen'
import { resolveVisMediaUrl } from '@/utils/visMedia'

export default {
  name: 'VisPageList',
  data() {
    return {
      queryParam: {
        name: '',
        state: undefined
      },
      loading: false,
      dataSource: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showTotal: total => `共 ${total} 条`
      }
    }
  },
  created() {
    this.loadPages()
  },
  methods: {
    resolveMediaUrl(value) {
      if (!value) {
        return require('@/assets/noData.png')
      }
      return resolveVisMediaUrl(value) || require('@/assets/noData.png')
    },
    resetQuery() {
      this.queryParam = {
        name: '',
        state: undefined
      }
      this.loadPages(true)
    },
    handlePageChange(page, pageSize) {
      this.pagination.current = page
      this.pagination.pageSize = pageSize
      this.loadPages()
    },
    openEditor(type, record) {
      const query = {
        type
      }
      if (record) {
        query.id = record.id
        query.name = record.name
        query.template = record.template
        query.state = record.state
        query.thumbnail = record.thumbnail
      }
      this.$router.push({
        path: '/vis/bigscreen/pages/editor',
        query
      })
    },
    publishPage(record) {
      this.loading = true
      editPageState({ id: record.id, state: '0' }).then((res) => {
        if (res && res.result === 'success') {
          this.$message.success(res.msg || '发布成功')
          this.loadPages()
        } else {
          this.$message.warning((res && res.msg) || '发布失败')
        }
      }).catch(() => {
        this.$message.error('发布失败')
      }).finally(() => {
        this.loading = false
      })
    },
    deletePage(record) {
      this.$confirm({
        title: '确定删除这个页面吗？',
        okText: '确定',
        cancelText: '取消',
        onOk: () => {
          this.loading = true
          deletePageInfo({ id: record.id }).then((res) => {
            if (res && res.result === 'success') {
              this.$message.success(res.msg || '删除成功')
              this.loadPages(true)
            } else {
              this.$message.warning((res && res.msg) || '删除失败')
            }
          }).catch(() => {
            this.$message.error('删除失败')
          }).finally(() => {
            this.loading = false
          })
        }
      })
    },
    loadPages(reset) {
      if (reset) {
        this.pagination.current = 1
      }
      this.loading = true
      const params = {
        ...this.queryParam,
        pageNo: this.pagination.current,
        pageSize: this.pagination.pageSize
      }
      getPageList(params).then((res) => {
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

<style scoped>
.table-operator {
  margin-bottom: 16px;
}

.vis-page-list__poster {
  width: 100%;
  height: 300px;
  object-fit: contain;
  background: #f5f7fa;
}

.vis-page-list__actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 12px;
  color: rgba(0, 0, 0, 0.85);
}

.vis-page-list__actions span {
  cursor: pointer;
}

.vis-page-list__action-disabled {
  color: rgba(0, 0, 0, 0.35);
  cursor: default;
}

.vis-page-list__pagination {
  margin-top: 24px;
  text-align: right;
}
</style>