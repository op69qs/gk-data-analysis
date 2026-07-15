<template>
  <a-card :bordered="false" :class="['vis-preview-entry', { 'vis-preview-entry--fullscreen': isFullscreenMode }]">
    <a-page-header v-if="!isFullscreenMode" title="可视化大屏预览" sub-title="方案预览链路已恢复到主前端">
      <template slot="extra">
        <a-button icon="fullscreen" @click="openFullscreenMode">全屏</a-button>
        <a-button icon="reload" :loading="loading" @click="loadPreview">刷新</a-button>
      </template>
    </a-page-header>

    <div v-else class="vis-preview-entry__toolbar">
      <span>全屏预览</span>
      <div>
        <a-button size="small" icon="left" @click="prevPage">上一页</a-button>
        <a-button size="small" icon="right" style="margin-left: 8px" @click="nextPage">下一页</a-button>
        <a-button size="small" icon="fullscreen-exit" style="margin-left: 8px" @click="exitFullscreenMode">退出</a-button>
      </div>
    </div>

    <a-alert
      v-if="!schemeId"
      type="warning"
      show-icon
      message="未收到方案参数"
      description="请从展示方案管理页进入预览，或在地址中携带 schemeId/info 参数。"
    />

    <template v-else>
      <a-row v-if="!isFullscreenMode" :gutter="16" class="vis-preview-entry__stats">
        <a-col :xl="6" :md="12" :sm="24">
          <a-card>
            <a-statistic title="方案编号" :value="schemeId" />
          </a-card>
        </a-col>
        <a-col :xl="6" :md="12" :sm="24">
          <a-card>
            <a-statistic title="页面数量" :value="pages.length" />
          </a-card>
        </a-col>
        <a-col :xl="6" :md="12" :sm="24">
          <a-card>
            <a-statistic title="轮播间隔" :value="intervalDisplay" suffix="ms" />
          </a-card>
        </a-col>
        <a-col :xl="6" :md="12" :sm="24">
          <a-card>
            <a-statistic title="自动轮播" :value="autoSettingText" />
          </a-card>
        </a-col>
      </a-row>

      <a-spin :spinning="loading">
        <a-alert
          v-if="errorMessage"
          class="vis-preview-entry__alert"
          type="error"
          show-icon
          :message="errorMessage"
        />

        <a-empty v-if="!pages.length && !errorMessage" description="当前方案暂无可预览页面" />

        <div v-else>
          <a-carousel ref="previewCarousel" class="vis-preview-entry__carousel" :autoplay="autoSetting" :autoplaySpeed="intervalDisplay">
            <div v-for="page in pages" :key="page.id || page.name || page.page_name">
              <div :class="['vis-preview-entry__hero', { 'vis-preview-entry__hero--fullscreen': isFullscreenMode }]" :style="buildHeroStyle(page)">
                <div class="vis-preview-entry__mask">
                  <div class="vis-preview-entry__hero-text">
                    <p class="vis-preview-entry__eyebrow">方案预览</p>
                    <h2>{{ getPageTitle(page) }}</h2>
                    <p>{{ getPageDescription(page) }}</p>
                  </div>
                </div>
              </div>
            </div>
          </a-carousel>

          <a-row :gutter="[16, 16]" class="vis-preview-entry__page-grid">
            <a-col v-for="(page, index) in pages" :key="page.id || index" :xl="8" :lg="12" :sm="24">
              <a-card :title="getPageTitle(page)">
                <p class="vis-preview-entry__meta">组件数：{{ getPageBlockCount(page) }}</p>
                <p class="vis-preview-entry__meta">背景类型：{{ getBackgroundLabel(page) }}</p>
                <p class="vis-preview-entry__meta">背景值：{{ getBackgroundValue(page) }}</p>
                <div v-if="getPageHighlights(page).length" class="vis-preview-entry__tags">
                  <a-tag v-for="item in getPageHighlights(page)" :key="item">{{ item }}</a-tag>
                </div>
                <a-empty v-else description="当前页未返回可展示的组件标题" :image="simpleImage" />
              </a-card>
            </a-col>
          </a-row>
        </div>
      </a-spin>
    </template>

    <div v-if="!schemeId" class="vis-preview-entry__content">
      <p>已恢复的前置能力：</p>
      <ul>
        <li>主系统开发态 vis 网关路由</li>
        <li>vis-screen-backend 聚合与装配骨架</li>
        <li>旧 BigScreen 菜单组件名兼容映射</li>
        <li>/vis/api 前缀下的基础接口封装</li>
      </ul>
    </div>
  </a-card>
</template>

<script>
import { Empty } from 'ant-design-vue'
import { getSchemeAllPage } from '@/api/visScreen'
import { resolveVisCarouselInterval } from '@/utils/visCarousel'

export default {
  name: 'VisPreviewEntry',
  data() {
    return {
      loading: false,
      pages: [],
      errorMessage: ''
    }
  },
  computed: {
    simpleImage() {
      return Empty.PRESENTED_IMAGE_SIMPLE
    },
    schemeId() {
      return this.$route.query.schemeId || this.$route.query.info || ''
    },
    intervalDisplay() {
      return resolveVisCarouselInterval(this.$route.query.interval)
    },
    autoSetting() {
      return String(this.$route.query.autoSetting || '0') === '1'
    },
    autoSettingText() {
      return this.autoSetting ? '开启' : '关闭'
    },
    isFullscreenMode() {
      return String(this.$route.query.fullscreen || '0') === '1'
    }
  },
  watch: {
    '$route.fullPath'() {
      this.syncFullscreenState()
      this.loadPreview()
    }
  },
  created() {
    this.syncFullscreenState()
    this.loadPreview()
  },
  beforeDestroy() {
    document.removeEventListener('keyup', this.handleKeyup, false)
  },
  methods: {
    syncFullscreenState() {
      document.removeEventListener('keyup', this.handleKeyup, false)
      if (this.isFullscreenMode) {
        document.addEventListener('keyup', this.handleKeyup, false)
        this.$nextTick(() => {
          this.requestDocumentFullscreen()
        })
      }
    },
    loadPreview() {
      if (!this.schemeId) {
        this.pages = []
        this.errorMessage = ''
        return
      }
      this.loading = true
      this.errorMessage = ''
      getSchemeAllPage({ scheme_id: this.schemeId }).then((res) => {
        if (res && res.result === 'success') {
          this.pages = res.rows || res.data || []
        } else {
          this.pages = []
          this.errorMessage = (res && res.message) || '方案预览数据加载失败'
        }
      }).catch(() => {
        this.pages = []
        this.errorMessage = '方案预览数据加载失败'
      }).finally(() => {
        this.loading = false
      })
    },
    handleKeyup(event) {
      if (!this.isFullscreenMode) {
        return
      }
      if (event.keyCode === 27) {
        this.exitFullscreenMode()
      } else if (event.keyCode === 37) {
        this.prevPage()
      } else if (event.keyCode === 39) {
        this.nextPage()
      }
    },
    requestDocumentFullscreen() {
      const element = document.documentElement
      const requestMethod =
        element.requestFullscreen ||
        element.webkitRequestFullscreen ||
        element.mozRequestFullScreen ||
        element.msRequestFullscreen
      if (requestMethod) {
        requestMethod.call(element)
      }
    },
    openFullscreenMode() {
      this.$router.push({
        path: '/vis/preview',
        query: {
          ...this.$route.query,
          fullscreen: '1'
        }
      })
    },
    exitFullscreenMode() {
      const nextQuery = { ...this.$route.query }
      delete nextQuery.fullscreen
      this.$router.push({
        path: '/vis/preview',
        query: nextQuery
      })
      const exitMethod =
        document.exitFullscreen ||
        document.webkitExitFullscreen ||
        document.mozCancelFullScreen ||
        document.msExitFullscreen
      if (exitMethod) {
        exitMethod.call(document)
      }
    },
    prevPage() {
      if (this.$refs.previewCarousel && this.$refs.previewCarousel.prev) {
        this.$refs.previewCarousel.prev()
      }
    },
    nextPage() {
      if (this.$refs.previewCarousel && this.$refs.previewCarousel.next) {
        this.$refs.previewCarousel.next()
      }
    },
    getPageTitle(page) {
      return page.name || page.page_name || page.title || '未命名页面'
    },
    getPageDescription(page) {
      const blocks = this.getPageBlockCount(page)
      const background = this.getBackgroundLabel(page)
      return `当前页包含 ${blocks} 个组件，背景模式为${background}。`
    },
    getPageBlockCount(page) {
      return Array.isArray(page.page_sub) ? page.page_sub.length : 0
    },
    getPageHighlights(page) {
      if (!Array.isArray(page.page_sub)) {
        return []
      }
      return page.page_sub
        .map(item => item.title || item.name)
        .filter(Boolean)
        .slice(0, 6)
    },
    getBackgroundLabel(page) {
      return String(page.background_type) === '0' ? '图片背景' : '纯色背景'
    },
    getBackgroundValue(page) {
      return page.content || page.colour || '-'
    },
    buildHeroStyle(page) {
      const isImageBackground = String(page.background_type) === '0' && page.content
      if (isImageBackground) {
        return {
          backgroundImage: `url(${page.content})`
        }
      }
      return {
        background: page.colour || 'linear-gradient(135deg, #12324a 0%, #215a6d 100%)'
      }
    }
  }
}
</script>

<style lang="less" scoped>
.vis-preview-entry__stats {
  margin-bottom: 16px;
}

.vis-preview-entry__toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  padding: 8px 12px;
  color: #fff;
  background: rgba(4, 18, 27, 0.68);
  border-radius: 10px;
}

.vis-preview-entry__alert {
  margin-bottom: 16px;
}

.vis-preview-entry__carousel {
  margin-bottom: 16px;
}

.vis-preview-entry__hero {
  height: 360px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: cover;
  border-radius: 12px;
  overflow: hidden;
}

.vis-preview-entry__hero--fullscreen {
  height: calc(100vh - 96px);
  border-radius: 16px;
}

.vis-preview-entry__mask {
  display: flex;
  align-items: flex-end;
  width: 100%;
  height: 100%;
  padding: 24px;
  background: linear-gradient(180deg, rgba(9, 24, 36, 0.15) 0%, rgba(9, 24, 36, 0.82) 100%);
}

.vis-preview-entry__hero-text {
  color: #fff;
}

.vis-preview-entry__hero-text h2 {
  margin: 0 0 8px;
  color: #fff;
  font-size: 28px;
}

.vis-preview-entry__hero-text p {
  margin: 0;
  color: rgba(255, 255, 255, 0.85);
}

.vis-preview-entry__eyebrow {
  margin-bottom: 8px;
  font-size: 12px;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.vis-preview-entry__page-grid {
  margin-top: 16px;
}

.vis-preview-entry__meta {
  margin-bottom: 8px;
  color: rgba(0, 0, 0, 0.65);
}

.vis-preview-entry__tags {
  margin-top: 12px;
}

.vis-preview-entry__content {
  margin-top: 16px;
  line-height: 1.8;
}

.vis-preview-entry--fullscreen {
  min-height: 100vh;
  background: #081a24;
}
</style>