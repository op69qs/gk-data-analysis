<template>
  <a-modal
    :title="modalTitle"
    width="80%"
    :visible="visible"
    :maskClosable="false"
    :destroyOnClose="true"
    :confirmLoading="saving"
    :okButtonProps="{ props: { disabled: !previewReady || saving } }"
    cancelText="关闭"
    okText="确定"
    wrapClassName="index-library-convert-modal"
    @ok="handleOk"
    @cancel="handleCancel"
  >
    <index-library-convert-form
      ref="convertForm"
      :form="form"
      :index-options="indexOptions"
      :index-loading="indexLoading"
      :generated="hasGeneratedPreview"
      @generate="handleGenerate"
    />

    <section
      v-if="hasGeneratedPreview || previewLoading || previewState !== 'idle'"
      class="preview-panel"
      aria-live="polite"
    >
      <a-spin :spinning="previewLoading">
        <index-library-chart-preview
          v-if="previewState === 'ready'"
          ref="chartPreview"
          :type="frozenCondition.type"
          :response="previewResponse"
          :condition="frozenCondition"
        />
        <a-empty
          v-else-if="previewState === 'empty'"
          description="暂无可预览数据"
        />
        <a-empty
          v-else-if="previewState === 'error'"
          :description="previewMessage || '生成图片失败，请检查配置'"
        />
      </a-spin>
    </section>
  </a-modal>
</template>

<script>
import {
  getIndexInfo,
  previewBarLine,
  previewPie,
  saveBarLine,
  savePie
} from '@/api/indexLibraryScheme'
import { getAreaTree, getGuokuTree } from '@/api/visScreen'
import {
  parseSchemeCondition,
  createInitialForm,
  buildPreviewPayload,
  buildSavePayload,
  hasChartData,
  getDimensionCandidates,
  buildDimensionCandidateMatch
} from '@/utils/indexLibraryScheme'
import IndexLibraryChartPreview, {
  buildChartOption
} from './IndexLibraryChartPreview'
import IndexLibraryConvertForm from './IndexLibraryConvertForm'

const PERIOD_CODES = {
  1: 'd',
  2: 'm',
  3: 'q',
  4: 'y'
}

function clone(value) {
  return JSON.parse(JSON.stringify(value))
}

function defaultColumns(record, type) {
  const source = record && record.SCHEME_COLUMS
  if (!source) return []
  return String(source).split(',').filter(Boolean).map((chartId, index) => ({
    chartId,
    chartDirection: type === 'barAndLine' && index > 0 ? 'Line' : 'Columnar'
  }))
}

export default {
  name: 'IndexLibraryConvertModal',
  components: {
    IndexLibraryChartPreview,
    IndexLibraryConvertForm
  },
  data() {
    return {
      visible: false,
      record: {},
      condition: {},
      form: {},
      indexOptions: [],
      indexLoading: false,
      previewLoading: false,
      saving: false,
      hasGeneratedPreview: false,
      previewReady: false,
      previewState: 'idle',
      previewMessage: '',
      previewResponse: null,
      previewOption: null,
      frozenCondition: null,
      generatedFormSnapshot: '',
      indexInfoRevision: 0,
      dimensionTreeRevision: 0,
      previewRevision: 0,
      saveRevision: 0
    }
  },
  computed: {
    modalTitle() {
      const name = this.form && this.form.schemeName
      return name ? `${name} - 转图` : '转图'
    }
  },
  watch: {
    form: {
      deep: true,
      handler() {
        if (
          this.generatedFormSnapshot &&
          this.previewFormSignature() === this.generatedFormSnapshot
        ) {
          return
        }
        this.invalidatePreview()
      }
    }
  },
  methods: {
    open(record) {
      this.indexInfoRevision += 1
      this.dimensionTreeRevision += 1
      this.indexLoading = false
      this.saveRevision += 1
      this.saving = false
      this.resetPreview()
      try {
        this.record = record || {}
        this.condition = parseSchemeCondition(this.record.SCHEME_CONDITON)
        const initial = createInitialForm(this.record, this.condition)
        const type = initial.type || 'bar'
        this.form = {
          ...initial,
          title: initial.title || initial.schemeName || '',
          type,
          schemecolumns: Array.isArray(initial.schemecolumns)
            ? initial.schemecolumns
            : defaultColumns(this.record, type),
          xTurn: initial.xTurn === undefined ? '0' : String(initial.xTurn),
          unit: initial.unit,
          GK: initial.GK,
          indexName: initial.indexName
        }
        this.visible = true
        return Promise.all([
          this.loadIndexInfo(),
          this.loadDimensionCandidates()
        ]).then(results => results[0])
      } catch (error) {
        this.$message.error(error.message || '方案条件格式错误')
        return Promise.resolve(false)
      }
    },
    loadIndexInfo() {
      const revision = ++this.indexInfoRevision
      const conditionColumns = this.condition &&
        Array.isArray(this.condition.schemecolumns)
        ? this.condition.schemecolumns
          .map(item => item && item.chartId)
          .filter(id => id !== undefined && id !== null && id !== '')
          .join(',')
        : ''
      const columns = (this.record && this.record.SCHEME_COLUMS) || conditionColumns
      if (!columns) {
        this.indexOptions = []
        this.indexLoading = false
        return Promise.resolve(false)
      }
      this.indexLoading = true
      return getIndexInfo({ SCHEME_COLUMS: columns }).then(res => {
        if (revision !== this.indexInfoRevision) return false
        if (res && res.result === 'success') {
          this.indexOptions = Array.isArray(res.indexInfoList)
            ? res.indexInfoList
            : []
          return true
        }
        this.indexOptions = []
        this.$message.warning((res && res.msg) || '指标信息加载失败')
        return false
      }).catch(() => {
        if (revision !== this.indexInfoRevision) return false
        this.indexOptions = []
        this.$message.error('指标信息加载失败，请稍后重试')
        return false
      }).finally(() => {
        if (revision === this.indexInfoRevision) {
          this.indexLoading = false
        }
      })
    },
    loadDimensionCandidates() {
      const revision = ++this.dimensionTreeRevision
      const dimensionFlag = String(this.form.dimensionFlag || '')
      const request = dimensionFlag === '1'
        ? getGuokuTree
        : dimensionFlag === '2' ? getAreaTree : null
      if (!request) return Promise.resolve(false)
      const dimensionName = dimensionFlag === '1' ? '国库' : '地区'

      return request({}).then(res => {
        if (revision !== this.dimensionTreeRevision) return false
        if (!res || res.result !== 'success' || !Array.isArray(res.rows)) {
          this.$message.warning(`${dimensionName}名称加载失败，已保留方案候选`)
          return false
        }
        const match = buildDimensionCandidateMatch(
          res.rows,
          this.form.dimCode
        )
        if (match.matchedCount === 0) {
          this.$message.warning(`${dimensionName}编码未匹配到名称，已保留方案候选`)
          return false
        }
        const matchedValues = new Set(match.matchedValues)
        const originalByValue = new Map(
          getDimensionCandidates(this.form)
            .map(candidate => [candidate.value, candidate])
        )
        const candidates = match.candidates.map(candidate =>
          matchedValues.has(candidate.value)
            ? candidate
            : originalByValue.get(candidate.value) || candidate
        )
        if (
          match.requestedCount > 0 &&
          match.matchedCount < match.requestedCount
        ) {
          this.$message.warning(`${dimensionName}编码部分未匹配，已保留原候选名称`)
        }
        if (typeof this.$set === 'function') {
          this.$set(this.form, 'dimensionCandidates', candidates)
        } else {
          this.form.dimensionCandidates = candidates
        }
        return true
      }).catch(() => {
        if (revision === this.dimensionTreeRevision) {
          this.$message.warning(`${dimensionName}名称加载失败，已保留方案候选`)
        }
        return false
      })
    },
    invalidatePreview() {
      this.previewRevision += 1
      this.previewLoading = false
      this.previewReady = false
      this.generatedFormSnapshot = ''
      if (!this.hasGeneratedPreview) {
        this.previewOption = null
        this.previewResponse = null
        this.frozenCondition = null
        this.previewState = 'idle'
        this.previewMessage = ''
      }
    },
    resetPreview() {
      this.previewRevision += 1
      this.previewLoading = false
      this.hasGeneratedPreview = false
      this.previewReady = false
      this.previewOption = null
      this.previewResponse = null
      this.frozenCondition = null
      this.previewState = 'idle'
      this.previewMessage = ''
      this.generatedFormSnapshot = ''
    },
    previewFormSignature() {
      return JSON.stringify(this.form || {})
    },
    saveRecordId(record) {
      if (!record || typeof record !== 'object') return ''
      const id = record.ID === undefined ? record.id : record.ID
      return id === undefined || id === null ? '' : String(id)
    },
    isCurrentSave(revision, record, recordId) {
      return revision === this.saveRevision &&
        record === this.record &&
        recordId === this.saveRecordId(this.record)
    },
    async validateForm() {
      if (!this.$refs.convertForm) return false
      try {
        return await this.$refs.convertForm.validate() === true
      } catch (error) {
        return false
      }
    },
    validatePieFields() {
      if (this.form.type !== 'pie') return true
      if (this.form.direction !== 'X' && this.form.direction !== 'Y') {
        this.$message.error('请选择饼图统计方向')
        return false
      }
      if (this.form.direction === 'X' && !this.form.GK) {
        this.$message.error('请输入国库或地区编码')
        return false
      }
      if (this.form.direction === 'Y' && !this.form.indexName) {
        this.$message.error('请选择指标编码')
        return false
      }
      return true
    },
    handleGenerate() {
      const revision = ++this.previewRevision
      this.previewLoading = false
      this.previewReady = false
      this.generatedFormSnapshot = ''
      if (!this.hasGeneratedPreview) {
        this.previewOption = null
        this.previewResponse = null
        this.frozenCondition = null
        this.previewState = 'idle'
      }
      this.previewMessage = ''
      return this.validateForm().then(valid => {
        if (revision !== this.previewRevision) return false
        if (!valid || !this.validatePieFields()) return false
        let payload
        try {
          payload = buildPreviewPayload(this.form, this.record)
        } catch (error) {
          this.$message.error(error.message)
          return false
        }

        const request = payload.type === 'pie' ? previewPie : previewBarLine
        const frozenPayload = {
          ...clone(payload),
          colourArray: Array.isArray(this.form.colourArray)
            ? this.form.colourArray.slice()
            : [],
          isGradual: this.form.isGradual === true,
          isRate: this.form.isRate === true
        }
        this.previewLoading = true
        this.previewReady = false
        if (!this.hasGeneratedPreview) {
          this.previewState = 'idle'
        }
        return request(payload).then(res => {
          if (revision !== this.previewRevision) return false
          if (!res || res.result !== 'success') {
            this.previewState = 'error'
            this.previewMessage = (res && res.msg) || '预览失败，请检查配置'
            this.$message.error(this.previewMessage)
            return false
          }
          if (!hasChartData(res)) {
            this.previewState = 'empty'
            this.previewMessage = '暂无可预览数据'
            return false
          }

          const option = buildChartOption(payload.type, res, frozenPayload)
          const optionJson = JSON.stringify(option)
          const frozen = {
            ...frozenPayload,
            option: optionJson,
            query_path: payload.type === 'pie'
              ? 'IndexPie/getIndexPieData'
              : 'IndexBarLine/getIndexBarLineData',
            time_type: payload.time_type || PERIOD_CODES[payload.periodFlag],
            dacct_radio: payload.dacct_radio === undefined
              ? 2
              : payload.dacct_radio
          }
          this.previewResponse = res
          this.previewOption = option
          this.frozenCondition = Object.freeze(frozen)
          this.previewState = 'ready'
          this.hasGeneratedPreview = true
          this.previewReady = true
          this.generatedFormSnapshot = this.previewFormSignature()
          return true
        }).catch(() => {
          if (revision === this.previewRevision) {
            this.previewState = 'error'
            this.previewMessage = '预览失败，请稍后重试'
            this.$message.error(this.previewMessage)
          }
          return false
        }).finally(() => {
          if (revision === this.previewRevision) {
            this.previewLoading = false
          }
        })
      })
    },
    handleOk() {
      const revision = ++this.saveRevision
      const record = this.record
      const recordId = this.saveRecordId(record)
      this.saving = false
      if (!this.previewReady || !this.frozenCondition) {
        this.$message.error('请先生成当前图表配置')
        return Promise.resolve(false)
      }
      const preview = this.$refs.chartPreview
      const content = preview && preview.getDataURL()
      if (!content || content.indexOf('data:image/') !== 0) {
        this.$message.error('预览图片生成失败，请重新生成')
        return Promise.resolve(false)
      }

      let payload
      try {
        payload = buildSavePayload(
          { ...this.form, content },
          this.record,
          this.previewReady,
          this.frozenCondition
        )
        const savedCondition = JSON.parse(payload.condition)
        savedCondition.colourArray = Array.isArray(this.frozenCondition.colourArray)
          ? this.frozenCondition.colourArray.slice()
          : []
        savedCondition.isGradual = this.frozenCondition.isGradual === true
        savedCondition.isRate = this.frozenCondition.isRate === true
        payload.condition = JSON.stringify(savedCondition)
      } catch (error) {
        this.$message.error(error.message)
        return Promise.resolve(false)
      }

      const request = this.frozenCondition.type === 'pie' ? savePie : saveBarLine
      this.saving = true
      return request(payload).then(res => {
        if (!this.isCurrentSave(revision, record, recordId)) return false
        if (res && res.result === 'success') {
          this.$message.success(res.msg || '保存成功')
          this.visible = false
          this.resetPreview()
          this.$emit('ok')
          return true
        }
        this.$message.error((res && res.msg) || '保存失败')
        return false
      }).catch(() => {
        if (!this.isCurrentSave(revision, record, recordId)) return false
        this.$message.error('保存失败，请稍后重试')
        return false
      }).finally(() => {
        if (this.isCurrentSave(revision, record, recordId)) {
          this.saving = false
        }
      })
    },
    handleCancel() {
      this.indexInfoRevision += 1
      this.dimensionTreeRevision += 1
      this.indexLoading = false
      this.saveRevision += 1
      this.visible = false
      this.saving = false
      this.resetPreview()
    }
  }
}
</script>

<style scoped>
.preview-panel {
  margin-top: 20px;
  padding: 16px;
  min-height: 460px;
  color: #d9e2ec;
  background: #252a30;
}

.preview-panel /deep/ .ant-empty {
  display: flex;
  min-height: 460px;
  flex-direction: column;
  justify-content: center;
}

.preview-panel /deep/ .ant-empty-description {
  color: #d9e2ec;
}

@media (max-width: 640px) {
  .preview-panel {
    padding: 12px;
  }
}
</style>
