<template>
  <a-card :bordered="false" class="vis-gallery-list">
    <a-page-header title="图库" sub-title="已恢复到 GK 主前端的 vis 图库列表" />
    <a-tabs :active-key="activeBusinessId" @change="handleBusinessChange">
      <a-tab-pane key="" tab="全部" />
      <a-tab-pane v-for="item in businessTypes" :key="String(item.business_id)" :tab="item.business_name" />
    </a-tabs>

    <a-spin :spinning="loading">
      <a-row v-if="galleryItems.length" :gutter="[24, 24]">
        <a-col v-for="item in galleryItems" :key="item.id" :xl="8" :lg="12" :md="12" :sm="24">
          <a-card hoverable>
            <div class="vis-gallery-list__cover">
              <img v-if="showImage(item)" :src="resolveMediaUrl(item.content)" :alt="item.title" />
              <div v-else class="vis-gallery-list__html" v-html="item.content"></div>
            </div>
            <template slot="actions">
              <div class="vis-gallery-list__action-row">
                <span class="vis-gallery-list__title">{{ item.title }}</span>
                <span class="vis-gallery-list__edit">编辑</span>
              </div>
            </template>
          </a-card>
        </a-col>
      </a-row>
      <a-empty v-else description="暂无图库数据" />
    </a-spin>
  </a-card>
</template>

<script>
import { getBusinessTypeList, getGalleryList } from '@/api/visScreen'

export default {
  name: 'VisGalleryList',
  data() {
    return {
      activeBusinessId: '',
      businessTypes: [],
      galleryItems: [],
      loading: false,
      pageNo: 1,
      pageSize: 12
    }
  },
  created() {
    this.loadBusinessTypes()
    this.loadGalleryItems()
  },
  methods: {
    resolveMediaUrl(value) {
      if (!value) {
        return ''
      }
      if (/^(data:|https?:)?\/\//i.test(value)) {
        return value
      }
      const normalizedPath = value.replace(/^\.?\//, '')
      return `${window._CONFIG['domianURL']}/${normalizedPath}`
    },
    showImage(item) {
      return item && (item.type === 'b' || item.type === 't')
    },
    handleBusinessChange(key) {
      this.activeBusinessId = key
      this.pageNo = 1
      this.loadGalleryItems()
    },
    loadBusinessTypes() {
      getBusinessTypeList().then((res) => {
        if (res && res.result === 'success') {
          this.businessTypes = res.rows || []
        }
      }).catch(() => {
        this.businessTypes = []
      })
    },
    loadGalleryItems() {
      this.loading = true
      const params = {
        pageNo: this.pageNo,
        pageSize: this.pageSize,
        state: 0
      }
      if (this.activeBusinessId) {
        params.business_id = this.activeBusinessId
      }
      getGalleryList(params).then((res) => {
        if (res && res.result === 'success') {
          this.galleryItems = res.rows || []
        } else {
          this.galleryItems = []
        }
      }).catch(() => {
        this.galleryItems = []
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="less" scoped>
.vis-gallery-list__cover {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 260px;
  overflow: hidden;
  background: linear-gradient(180deg, #f6f9fc 0%, #edf3f8 100%);
}

.vis-gallery-list__cover img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.vis-gallery-list__html {
  width: 100%;
  height: 100%;
  padding: 20px;
  overflow: hidden;
}

.vis-gallery-list__action-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 0 12px;
}

.vis-gallery-list__title {
  flex: 1;
  text-align: left;
  white-space: normal;
  word-break: break-word;
}

.vis-gallery-list__edit {
  margin-left: 16px;
  color: #1890ff;
}
</style>