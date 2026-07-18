<template>
  <a-modal
    title="指标方案转图"
    width="80%"
    :visible="visible"
    :maskClosable="false"
    :destroyOnClose="true"
    wrapClassName="index-library-convert-modal"
    @cancel="handleCancel"
  >
    <div class="convert-layout">
      <section class="config-panel" aria-labelledby="chart-config-title">
        <h3 id="chart-config-title" class="panel-title">图表配置</h3>
        <index-library-convert-form
          ref="convertForm"
          :form="form"
          :index-options="indexOptions"
          :index-loading="indexLoading"
        />
      </section>

      <section
        class="preview-panel"
        aria-labelledby="chart-preview-title"
        aria-live="polite"
      >
        <div class="preview-heading">
          <h3 id="chart-preview-title" class="panel-title">图表预览</h3>
          <span class="preview-status">
            {{ previewReady ? '当前配置已预览' : '配置变化后需重新预览' }}
          </span>
        </div>
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
            :description="previewMessage || '预览失败，请检查配置'"
          />
          <div v-else class="preview-placeholder">
            <a-icon type="bar-chart" />
            <p>完成配置后点击“预览图表”查看真实数据</p>
          </div>
        </a-spin>
      </section>
    </div>

    <template slot="footer">
      <a-button @click="handleCancel">关闭</a-button>
      <a-button :loading="previewLoading" @click="handlePreview">预览图表</a-button>
      <a-button
        type="primary"
        :disabled="!previewReady || saving"
        :loading="saving"
        @click="handleSave"
      >
        保存图表
      </a-button>
    </template>
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
import {
  parseSchemeCondition,
  createInitialForm,
  buildPreviewPayload,
  buildSavePayload,
  hasChartData
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
      previewReady: false,
      previewState: 'idle',
      previewMessage: '',
      previewResponse: null,
      previewOption: null,
      frozenCondition: null,
      indexInfoRevision: 0,
      previewRevision: 0
    }
  },
  watch: {
    form: {
      deep: true,
      handler() {
        this.invalidatePreview()
      }
    }
  },
  methods: {
    open(record) {
      this.indexInfoRevision += 1
      this.indexLoading = false
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
        this.invalidatePreview()
        this.visible = true
        return this.loadIndexInfo()
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
    invalidatePreview() {
      this.previewRevision += 1
      this.previewLoading = false
      this.previewReady = false
      this.previewOption = null
      this.previewResponse = null
      this.frozenCondition = null
      this.previewState = 'idle'
      this.previewMessage = ''
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
    handlePreview() {
      const revision = ++this.previewRevision
      this.previewLoading = false
      this.previewReady = false
      this.previewOption = null
      this.previewResponse = null
      this.frozenCondition = null
      this.previewState = 'idle'
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
        this.previewLoading = true
        this.previewReady = false
        this.previewState = 'idle'
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

          const option = buildChartOption(payload.type, res, payload)
          const optionJson = JSON.stringify(option)
          const frozen = {
            ...clone(payload),
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
          this.previewReady = true
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
    handleSave() {
      if (!this.previewReady || !this.frozenCondition) {
        this.$message.error('请先预览当前图表配置')
        return Promise.resolve(false)
      }
      const preview = this.$refs.chartPreview
      const content = preview && preview.getDataURL()
      if (!content || content.indexOf('data:image/') !== 0) {
        this.$message.error('预览图片生成失败，请重新预览')
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
      } catch (error) {
        this.$message.error(error.message)
        return Promise.resolve(false)
      }

      const request = this.frozenCondition.type === 'pie' ? savePie : saveBarLine
      this.saving = true
      return request(payload).then(res => {
        if (res && res.result === 'success') {
          this.$message.success(res.msg || '保存成功')
          this.visible = false
          this.$emit('ok')
          return true
        }
        this.$message.error((res && res.msg) || '保存失败')
        return false
      }).catch(() => {
        this.$message.error('保存失败，请稍后重试')
        return false
      }).finally(() => {
        this.saving = false
      })
    },
    handleCancel() {
      this.indexInfoRevision += 1
      this.indexLoading = false
      this.visible = false
      this.saving = false
      this.invalidatePreview()
    }
  }
}
</script>

<style scoped>
.convert-layout {
  display: grid;
  grid-template-columns: minmax(380px, 0.9fr) minmax(480px, 1.35fr);
  gap: 20px;
  min-height: 560px;
}

.config-panel,
.preview-panel {
  min-width: 0;
  border: 1px solid #e8e8e8;
}

.config-panel {
  padding: 18px 18px 8px;
  overflow-y: auto;
  max-height: 68vh;
}

.preview-panel {
  display: flex;
  flex-direction: column;
  padding: 18px;
  color: #d9e2ec;
  background: #252a30;
}

.panel-title {
  margin: 0 0 16px;
  font-size: 16px;
  font-weight: 600;
}

.preview-heading {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}

.preview-status {
  color: #aebbc8;
  font-size: 12px;
}

.preview-placeholder {
  display: flex;
  min-height: 460px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #aebbc8;
  text-align: center;
}

.preview-placeholder .anticon {
  margin-bottom: 12px;
  font-size: 48px;
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

@media (max-width: 1100px) {
  .convert-layout {
    grid-template-columns: 1fr;
  }

  .config-panel {
    max-height: none;
  }
}

@media (max-width: 640px) {
  .config-panel,
  .preview-panel {
    padding: 12px;
  }
}
</style>
