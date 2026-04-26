<template>
  <a-card :bordered="false" class="vis-page-editor-entry">
    <div class="vis-page-editor-entry__hero">
      <div>
        <p class="vis-page-editor-entry__eyebrow">页面编辑承接</p>
        <h2>旧 AddTemplate 路由已迁入主前端</h2>
        <p class="vis-page-editor-entry__summary">
          当前先恢复到可访问、可识别上下文、可返回列表的过渡页。完整拖拽式编辑器还没有迁回主仓。
        </p>
      </div>
      <div class="vis-page-editor-entry__actions">
        <a-button type="primary" icon="unordered-list" @click="goToPageList">返回页面管理</a-button>
        <a-button style="margin-left: 8px" icon="appstore" @click="goToTemplateList">查看模板管理</a-button>
      </div>
    </div>

    <a-row :gutter="16" class="vis-page-editor-entry__meta">
      <a-col :md="8" :sm="24">
        <a-card size="small" title="当前模式">
          <span>{{ editorModeText }}</span>
        </a-card>
      </a-col>
      <a-col :md="8" :sm="24">
        <a-card size="small" title="页面标识">
          <span>{{ currentPageId }}</span>
        </a-card>
      </a-col>
      <a-col :md="8" :sm="24">
        <a-card size="small" title="来源路由">
          <span>{{ sourcePath }}</span>
        </a-card>
      </a-col>
    </a-row>

    <a-card size="small" class="vis-page-editor-entry__context" title="页面上下文">
      <a-descriptions :column="2" size="small">
        <a-descriptions-item label="页面名称">{{ currentPageName }}</a-descriptions-item>
        <a-descriptions-item label="页面状态">{{ currentPageStateText }}</a-descriptions-item>
        <a-descriptions-item label="缩略图地址" :span="2">{{ currentThumbnail }}</a-descriptions-item>
      </a-descriptions>
    </a-card>

    <a-spin :spinning="loadingDetail || saving">
      <a-card size="small" title="基础信息编辑" class="vis-page-editor-entry__form-card">
        <a-form-model ref="editorForm" :model="formModel" :rules="rules" layout="vertical">
          <a-row :gutter="16">
            <a-col :md="12" :sm="24">
              <a-form-model-item label="页面名称" prop="name">
                <a-input v-model="formModel.name" placeholder="请输入页面名称" />
              </a-form-model-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-model-item label="模板编号" prop="template">
                <a-select v-model="formModel.template" placeholder="请选择模板" @change="handleTemplateChange">
                  <a-select-option v-for="item in templateOptions" :key="item.value" :value="item.value">
                    {{ item.label }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-model-item label="页面状态">
                <a-select v-model="formModel.state">
                  <a-select-option value="0">发布</a-select-option>
                  <a-select-option value="1">未发布</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-model-item label="标题背景">
                <a-select v-model="formModel.title_background">
                  <a-select-option value="0">显示</a-select-option>
                  <a-select-option value="1">隐藏</a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-model-item label="背景类型">
                <a-radio-group v-model="formModel.background_type">
                  <a-radio value="0">背景图片</a-radio>
                  <a-radio value="1">背景颜色</a-radio>
                </a-radio-group>
              </a-form-model-item>
            </a-col>
            <a-col :md="12" :sm="24">
              <a-form-model-item label="背景颜色">
                <a-input v-model="formModel.colour" placeholder="例如 #0d2941" />
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="背景图片 / 内容地址">
                <a-input v-model="formModel.content" placeholder="当前先保留为图片地址或背景内容字段" />
              </a-form-model-item>
            </a-col>
            <a-col :span="24">
              <a-form-model-item label="缩略图地址">
                <a-input v-model="formModel.thumbnail" placeholder="可直接复用现有缩略图地址" />
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>

        <div class="vis-page-editor-entry__form-actions">
          <a-button type="primary" :loading="saving" @click="submitForm">保存基础信息</a-button>
          <a-button style="margin-left: 8px" icon="layout" :disabled="!formModel.template" @click="applyTemplatePreset">按模板生成区块</a-button>
          <a-button style="margin-left: 8px" @click="restoreFromRoute">重置为当前上下文</a-button>
        </div>
      </a-card>
    </a-spin>

    <a-card size="small" title="页面块编辑" class="vis-page-editor-entry__blocks">
      <a-empty v-if="!pageSubList.length" description="当前没有已加载的页面块数据" />
      <div v-else class="vis-page-editor-entry__block-list">
        <a-card v-for="(item, index) in pageSubList" :key="`${item.i || index}-${index}`" size="small" class="vis-page-editor-entry__block-item">
          <div class="vis-page-editor-entry__block-header">
            <strong>{{ item.i === '0' ? '标题区块' : `区块 ${index + 1}` }}</strong>
            <span>{{ buildBlockDescription(item) }}</span>
          </div>
          <a-row :gutter="12">
            <a-col :md="12" :sm="24">
              <label class="vis-page-editor-entry__field-label">标题</label>
              <a-input v-model="item.title" placeholder="请输入区块标题" />
            </a-col>
            <a-col :md="12" :sm="24">
              <label class="vis-page-editor-entry__field-label">区块类型</label>
              <a-select v-model="item.type" placeholder="请选择区块类型">
                <a-select-option value="">图表/图库</a-select-option>
                <a-select-option value="h">HTML</a-select-option>
              </a-select>
            </a-col>
            <a-col :span="24">
              <label class="vis-page-editor-entry__field-label">内容</label>
              <a-textarea v-model="item.content" :rows="3" placeholder="HTML 类型可直接填写片段，其余类型可先保留为接口返回或图片地址" />
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">图库 ID</label>
              <a-input v-model="item.gallery_id" placeholder="可选" />
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">查询路径</label>
              <a-input v-model="item.query_path" placeholder="如 /queryData/getCurrentData" />
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">单位</label>
              <a-select v-model="item.unit" placeholder="可选">
                <a-select-option value="">不设置</a-select-option>
                <a-select-option value="1">元</a-select-option>
                <a-select-option value="10000">万元</a-select-option>
                <a-select-option value="100000000">亿元</a-select-option>
              </a-select>
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">时间类型</label>
              <a-input v-model="item.time_type" placeholder="如 d / m / j / y" />
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">时间区间</label>
              <a-input v-model="item.time_interval" placeholder="可选" />
            </a-col>
            <a-col :md="8" :sm="24">
              <label class="vis-page-editor-entry__field-label">附加配置</label>
              <a-input v-model="item.option" placeholder="可选 JSON / 文本配置" />
            </a-col>
            <a-col :md="6" :sm="12">
              <label class="vis-page-editor-entry__field-label">X</label>
              <a-input v-model="item.x" />
            </a-col>
            <a-col :md="6" :sm="12">
              <label class="vis-page-editor-entry__field-label">Y</label>
              <a-input v-model="item.y" />
            </a-col>
            <a-col :md="6" :sm="12">
              <label class="vis-page-editor-entry__field-label">W</label>
              <a-input v-model="item.w" />
            </a-col>
            <a-col :md="6" :sm="12">
              <label class="vis-page-editor-entry__field-label">H</label>
              <a-input v-model="item.h" />
            </a-col>
            <a-col :span="24">
              <div class="vis-page-editor-entry__where-header">
                <label class="vis-page-editor-entry__field-label">查询条件</label>
                <div>
                  <a-button size="small" @click="addWhereCondition(item)">添加条件</a-button>
                  <a-button size="small" style="margin-left: 8px" @click="addTimeWhereCondition(item)">添加时间条件</a-button>
                </div>
              </div>
              <a-empty v-if="!item.pageWhere || !item.pageWhere.length" description="当前没有查询条件" />
              <div v-else class="vis-page-editor-entry__where-list">
                <div v-for="(condition, conditionIndex) in item.pageWhere" :key="`${item.i}-${conditionIndex}`" class="vis-page-editor-entry__where-item">
                  <a-row :gutter="8">
                    <a-col :md="6" :sm="24">
                      <a-select v-model="condition.where_type" placeholder="类型">
                        <a-select-option value="b">核算主体</a-select-option>
                        <a-select-option value="g">国库</a-select-option>
                        <a-select-option value="a">地区</a-select-option>
                        <a-select-option value="s">科目</a-select-option>
                        <a-select-option value="ts">T科目</a-select-option>
                        <a-select-option value="t">时间</a-select-option>
                        <a-select-option value="custom">自定义</a-select-option>
                      </a-select>
                    </a-col>
                    <a-col :md="8" :sm="24">
                      <a-input v-model="condition.where_key" placeholder="字段名，如 guoku_id" />
                    </a-col>
                    <a-col :md="8" :sm="24">
                      <a-input v-model="condition.where_value" placeholder="字段值" />
                    </a-col>
                    <a-col :md="2" :sm="24">
                      <a-button icon="delete" @click="removeWhereCondition(item, conditionIndex)" />
                    </a-col>
                  </a-row>
                </div>
              </div>
            </a-col>
          </a-row>
        </a-card>
      </div>
    </a-card>

    <a-alert
      type="info"
      show-icon
      message="当前恢复状态"
      description="数据库菜单、旧组件名映射和新路由已经统一到这里。后续如果继续恢复真正编辑器，可以直接替换这个承接页，而不用再改菜单和旧路径兼容层。"
    />

    <a-descriptions bordered size="small" class="vis-page-editor-entry__detail" :column="1">
      <a-descriptions-item label="旧组件名">BigScreen/AddTemplate</a-descriptions-item>
      <a-descriptions-item label="旧路径">/bigScreen/AddTemplate</a-descriptions-item>
      <a-descriptions-item label="新路径">/vis/bigscreen/pages/editor</a-descriptions-item>
      <a-descriptions-item label="承接组件">vis/PageEditorEntry</a-descriptions-item>
    </a-descriptions>
  </a-card>
</template>

<script>
import { addPageInfo, editPageInfo, getPageSubAll } from '@/api/visScreen'

const TEMPLATE_PRESETS = {
  '1': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '4', h: '6', i: '1', content: '', title: '', type: '' },
    { x: '4', y: '2', w: '4', h: '8', i: '2', content: '', title: '', type: '' },
    { x: '8', y: '2', w: '4', h: '4', i: '3', content: '', title: '', type: '' },
    { x: '0', y: '8', w: '4', h: '6', i: '4', content: '', title: '', type: '' },
    { x: '4', y: '8', w: '4', h: '4', i: '5', content: '', title: '', type: '' },
    { x: '8', y: '6', w: '4', h: '4', i: '6', content: '', title: '', type: '' },
    { x: '8', y: '10', w: '4', h: '4', i: '7', content: '', title: '', type: '' }
  ],
  '2': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '6', h: '8', i: '1', content: '', title: '', type: '' },
    { x: '6', y: '2', w: '6', h: '4', i: '2', content: '', title: '', type: '' },
    { x: '6', y: '10', w: '6', h: '4', i: '3', content: '', title: '', type: '' },
    { x: '0', y: '6', w: '6', h: '4', i: '4', content: '', title: '', type: '' },
    { x: '6', y: '8', w: '6', h: '4', i: '5', content: '', title: '', type: '' }
  ],
  '3': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '3', h: '6', i: '1', content: '', title: '', type: '' },
    { x: '3', y: '2', w: '6', h: '12', i: '2', content: '', title: '', type: '' },
    { x: '9', y: '2', w: '3', h: '6', i: '3', content: '', title: '', type: '' },
    { x: '0', y: '8', w: '3', h: '6', i: '4', content: '', title: '', type: '' },
    { x: '9', y: '8', w: '3', h: '6', i: '5', content: '', title: '', type: '' }
  ],
  '4': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '4', h: '6', i: '1', content: '', title: '', type: '' },
    { x: '4', y: '2', w: '4', h: '6', i: '2', content: '', title: '', type: '' },
    { x: '8', y: '2', w: '4', h: '6', i: '3', content: '', title: '', type: '' },
    { x: '0', y: '8', w: '4', h: '6', i: '4', content: '', title: '', type: '' },
    { x: '4', y: '8', w: '4', h: '6', i: '5', content: '', title: '', type: '' },
    { x: '8', y: '8', w: '4', h: '6', i: '6', content: '', title: '', type: '' }
  ],
  '5': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '4', h: '4', i: '1', content: '', title: '', type: '' },
    { x: '4', y: '2', w: '4', h: '4', i: '2', content: '', title: '', type: '' },
    { x: '8', y: '2', w: '4', h: '4', i: '3', content: '', title: '', type: '' },
    { x: '0', y: '6', w: '4', h: '4', i: '4', content: '', title: '', type: '' },
    { x: '4', y: '6', w: '4', h: '4', i: '5', content: '', title: '', type: '' },
    { x: '8', y: '6', w: '4', h: '4', i: '6', content: '', title: '', type: '' },
    { x: '0', y: '10', w: '4', h: '4', i: '7', content: '', title: '', type: '' },
    { x: '4', y: '10', w: '4', h: '4', i: '8', content: '', title: '', type: '' },
    { x: '8', y: '10', w: '4', h: '4', i: '9', content: '', title: '', type: '' }
  ],
  '6': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '6', h: '6', i: '1', content: '', title: '', type: '' },
    { x: '6', y: '2', w: '6', h: '6', i: '2', content: '', title: '', type: '' },
    { x: '0', y: '8', w: '6', h: '6', i: '3', content: '', title: '', type: '' },
    { x: '6', y: '8', w: '6', h: '6', i: '4', content: '', title: '', type: '' }
  ],
  '7': [
    { x: '0', y: '0', w: '12', h: '2', i: '0', content: '', title: '', type: '' },
    { x: '0', y: '2', w: '12', h: '12', i: '1', content: '', title: '', type: '' }
  ]
}

export default {
  name: 'VisPageEditorEntry',
  data() {
    return {
      loadingDetail: false,
      saving: false,
      pageSubList: [],
      templateOptions: [
        { label: '模板1', value: '1' },
        { label: '模板2', value: '2' },
        { label: '模板3', value: '3' },
        { label: '模板4', value: '4' },
        { label: '模板5', value: '5' },
        { label: '模板6', value: '6' },
        { label: '模板7', value: '7' }
      ],
      formModel: {
        name: '',
        template: undefined,
        state: '1',
        title_background: '0',
        background_type: '1',
        colour: '#0d2941',
        content: '',
        thumbnail: ''
      },
      rules: {
        name: [{ required: true, message: '请输入页面名称', trigger: 'blur' }],
        template: [{ required: true, message: '请选择模板编号', trigger: 'change' }]
      }
    }
  },
  computed: {
    currentMode() {
      return this.$route.query.type || this.$route.params.type || 'add'
    },
    editorModeText() {
      const mode = this.currentMode
      if (mode === 'edit') {
        return '编辑已有页面'
      }
      if (mode === 'copy') {
        return '复制页面'
      }
      return '新增页面'
    },
    currentPageId() {
      return this.$route.query.id || this.$route.query.pageId || this.$route.params.id || '未传入'
    },
    currentPageName() {
      return this.$route.query.name || '未传入'
    },
    currentPageStateText() {
      const state = this.$route.query.state
      if (state === '0') {
        return '发布'
      }
      if (state === '1') {
        return '未发布'
      }
      return '未传入'
    },
    currentThumbnail() {
      return this.$route.query.thumbnail || '未传入'
    },
    sourcePath() {
      return this.$route.fullPath || '/vis/bigscreen/pages/editor'
    },
    currentUserId() {
      const userInfo = this.$store && this.$store.getters ? this.$store.getters.userInfo : null
      return userInfo && userInfo.id ? userInfo.id : ''
    }
  },
  created() {
    this.restoreFromRoute()
    this.loadPageBlocks()
  },
  watch: {
    '$route.fullPath'() {
      this.restoreFromRoute()
      this.loadPageBlocks()
    }
  },
  methods: {
    restoreFromRoute() {
      this.formModel = {
        name: this.$route.query.name || '',
        template: this.$route.query.template || undefined,
        state: this.$route.query.state || '1',
        title_background: this.$route.query.title_background || '0',
        background_type: this.$route.query.background_type || '1',
        colour: this.$route.query.colour || '#0d2941',
        content: this.$route.query.content || '',
        thumbnail: this.$route.query.thumbnail || ''
      }
      if (this.currentMode !== 'edit' && this.formModel.template && !this.pageSubList.length) {
        this.pageSubList = this.createTemplateBlocks(this.formModel.template)
      }
    },
    loadPageBlocks() {
      if (this.currentMode !== 'edit' || this.currentPageId === '未传入') {
        this.pageSubList = []
        return
      }
      this.loadingDetail = true
      getPageSubAll({ page_id: this.currentPageId }).then((res) => {
        if (res && res.result === 'success') {
          this.pageSubList = Array.isArray(res.rows) ? res.rows.map(this.normalizeBlock) : []
        } else {
          this.pageSubList = []
        }
      }).catch(() => {
        this.pageSubList = []
      }).finally(() => {
        this.loadingDetail = false
      })
    },
    buildPayload() {
      return {
        id: this.currentMode === 'edit' ? this.currentPageId : undefined,
        name: this.formModel.name,
        template: this.formModel.template,
        background_type: this.formModel.background_type,
        colour: this.formModel.colour,
        thumbnail: this.formModel.thumbnail,
        add_user: this.currentUserId,
        title_background: this.formModel.title_background,
        state: this.formModel.state,
        content: this.formModel.content,
        page_sub: this.pageSubList.map(this.normalizeBlock)
      }
    },
    handleTemplateChange(value) {
      if (!value) {
        return
      }
      if (!this.pageSubList.length || this.currentMode !== 'edit') {
        this.pageSubList = this.createTemplateBlocks(value)
        return
      }
      this.$confirm({
        title: '切换模板会按预设重置当前页面块，是否继续？',
        okText: '继续',
        cancelText: '取消',
        onOk: () => {
          this.pageSubList = this.createTemplateBlocks(value)
        }
      })
    },
    applyTemplatePreset() {
      if (!this.formModel.template) {
        this.$message.warning('请先选择模板')
        return
      }
      this.pageSubList = this.createTemplateBlocks(this.formModel.template)
    },
    createTemplateBlocks(templateValue) {
      const preset = TEMPLATE_PRESETS[String(templateValue)] || []
      return preset.map(this.normalizeBlock)
    },
    normalizeBlock(item, index) {
      return {
        id: item.id || '',
        gallery_id: item.gallery_id || '',
        page_id: item.page_id || (this.currentMode === 'edit' ? this.currentPageId : ''),
        time_type: item.time_type || '',
        time_interval: item.time_interval || '',
        content: item.content || '',
        x: `${item.x != null ? item.x : 0}`,
        y: `${item.y != null ? item.y : 0}`,
        w: `${item.w != null ? item.w : 0}`,
        h: `${item.h != null ? item.h : 0}`,
        i: `${item.i != null ? item.i : index}`,
        title: item.title || '',
        type: item.type || '',
        option: item.option || '',
        query_path: item.query_path || '',
        unit: item.unit || '',
        pageWhere: Array.isArray(item.pageWhere) ? item.pageWhere.map(this.normalizeWhereCondition) : []
      }
    },
    normalizeWhereCondition(condition) {
      return {
        where_type: condition.where_type || '',
        where_key: condition.where_key || '',
        where_value: condition.where_value || ''
      }
    },
    addWhereCondition(block) {
      if (!Array.isArray(block.pageWhere)) {
        this.$set(block, 'pageWhere', [])
      }
      block.pageWhere.push(this.normalizeWhereCondition({}))
    },
    addTimeWhereCondition(block) {
      if (!Array.isArray(block.pageWhere)) {
        this.$set(block, 'pageWhere', [])
      }
      block.pageWhere.push(this.normalizeWhereCondition({ where_type: 't', where_key: 'dacct', where_value: '' }))
    },
    removeWhereCondition(block, conditionIndex) {
      if (!Array.isArray(block.pageWhere)) {
        return
      }
      block.pageWhere.splice(conditionIndex, 1)
    },
    submitForm() {
      this.$refs.editorForm.validate((valid) => {
        if (!valid) {
          return
        }
        if (!this.pageSubList.length) {
          this.$message.warning('请先按模板生成页面块')
          return
        }
        this.saving = true
        const payload = this.buildPayload()
        const request = this.currentMode === 'edit' ? editPageInfo(payload) : addPageInfo(payload)
        request.then((res) => {
          if (res && res.result === 'success') {
            this.$message.success(res.msg || '保存成功')
            this.goToPageList()
          } else {
            this.$message.warning((res && res.msg) || '保存失败')
          }
        }).catch(() => {
          this.$message.error('保存失败')
        }).finally(() => {
          this.saving = false
        })
      })
    },
    buildBlockDescription(item) {
      return `类型: ${item.type || '未标记'} | 位置: (${item.x}, ${item.y}) | 尺寸: ${item.w} x ${item.h}`
    },
    goToPageList() {
      this.$router.push({ path: '/vis/bigscreen/pages' })
    },
    goToTemplateList() {
      this.$router.push({ path: '/vis/bigscreen/templates' })
    }
  }
}
</script>

<style scoped>
.vis-page-editor-entry__hero {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 24px;
  margin-bottom: 16px;
  padding: 24px;
  border-radius: 16px;
  background: linear-gradient(135deg, #0d2236 0%, #17496d 100%);
  color: #fff;
}

.vis-page-editor-entry__eyebrow {
  margin-bottom: 8px;
  font-size: 12px;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  opacity: 0.75;
}

.vis-page-editor-entry__hero h2 {
  margin-bottom: 8px;
  color: #fff;
}

.vis-page-editor-entry__summary {
  max-width: 560px;
  margin-bottom: 0;
  color: rgba(255, 255, 255, 0.85);
}

.vis-page-editor-entry__actions {
  flex-shrink: 0;
}

.vis-page-editor-entry__meta {
  margin-bottom: 16px;
}

.vis-page-editor-entry__detail {
  margin-top: 16px;
}

.vis-page-editor-entry__context {
  margin-bottom: 16px;
}

.vis-page-editor-entry__form-card {
  margin-bottom: 16px;
}

.vis-page-editor-entry__form-actions {
  margin-top: 8px;
}

.vis-page-editor-entry__blocks {
  margin-bottom: 16px;
}

.vis-page-editor-entry__block-list {
  display: grid;
  gap: 12px;
}

.vis-page-editor-entry__block-item {
  border-radius: 12px;
}

.vis-page-editor-entry__block-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 12px;
  color: rgba(0, 0, 0, 0.65);
}

.vis-page-editor-entry__field-label {
  display: inline-block;
  margin-bottom: 6px;
  color: rgba(0, 0, 0, 0.65);
}

.vis-page-editor-entry__where-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.vis-page-editor-entry__where-list {
  display: grid;
  gap: 8px;
}

.vis-page-editor-entry__where-item {
  padding: 10px;
  border: 1px solid #f0f0f0;
  border-radius: 10px;
  background: #fafafa;
}

@media (max-width: 768px) {
  .vis-page-editor-entry__hero {
    flex-direction: column;
  }

  .vis-page-editor-entry__actions {
    width: 100%;
  }

  .vis-page-editor-entry__block-header {
    flex-direction: column;
  }

  .vis-page-editor-entry__where-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>