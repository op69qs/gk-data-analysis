<template>
  <a-form
    :label-col="{ span: 7 }"
    :wrapper-col="{ span: 17 }"
    @submit.prevent
  >
    <a-form-item label="方案名称">
      <a-input v-model="form.schemeName" disabled />
    </a-form-item>
    <a-form-item
      label="图表标题"
      :validate-status="fieldStatus('title')"
      :help="validationErrors.title"
    >
      <a-input v-model.trim="form.title" placeholder="请输入图表标题" />
    </a-form-item>
    <a-form-item
      label="图表类型"
      :validate-status="fieldStatus('type')"
      :help="validationErrors.type"
    >
      <div class="chart-type-grid" role="radiogroup" aria-label="图表类型">
        <button
          v-for="item in chartTypes"
          :key="item.type"
          type="button"
          role="radio"
          class="chart-type-card"
          :class="{ 'chart-type-card--selected': form.type === item.type }"
          :aria-checked="String(form.type === item.type)"
          @click="selectChartType(item.type)"
        >
          <img :src="item.icon" :alt="`${item.label}图标`" />
          <span class="chart-type-card__label">{{ item.label }}</span>
          <span class="chart-type-card__state">
            {{ form.type === item.type ? '已选择' : '选择' }}
          </span>
        </button>
      </div>
    </a-form-item>
    <a-form-item
      label="指标"
      :validate-status="fieldStatus('schemecolumns')"
      :help="validationErrors.schemecolumns"
    >
      <a-select
        mode="multiple"
        :value="selectedIndexIds"
        placeholder="请选择指标"
        :loading="indexLoading"
        @change="handleIndexChange"
      >
        <a-select-option
          v-for="item in indexOptions"
          :key="String(item.id)"
          :value="String(item.id)"
        >
          {{ item.name }}
        </a-select-option>
      </a-select>
    </a-form-item>
    <div v-if="form.type === 'barAndLine'" class="series-directions">
      <div
        v-for="column in form.schemecolumns"
        :key="String(column.chartId)"
        class="series-direction-row"
      >
        <span>{{ indexLabel(column.chartId) }}</span>
        <a-select
          :value="column.chartDirection"
          size="small"
          @change="value => updateChartDirection(column.chartId, value)"
        >
          <a-select-option value="Columnar">柱状</a-select-option>
          <a-select-option value="Line">折线</a-select-option>
        </a-select>
      </div>
    </div>
    <a-row :gutter="12">
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="维度"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
          :validate-status="fieldStatus('dimensionFlag')"
          :help="validationErrors.dimensionFlag"
        >
          <a-select v-model="form.dimensionFlag" placeholder="请选择维度">
            <a-select-option value="1">国库</a-select-option>
            <a-select-option value="2">地区</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="时间粒度"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
          :validate-status="fieldStatus('periodFlag')"
          :help="validationErrors.periodFlag"
        >
          <a-select v-model="form.periodFlag" placeholder="请选择粒度">
            <a-select-option value="1">日</a-select-option>
            <a-select-option value="2">月</a-select-option>
            <a-select-option value="3">季</a-select-option>
            <a-select-option value="4">年</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item
      label="时间范围"
      :validate-status="fieldStatus('timeType')"
      :help="validationErrors.timeType"
    >
      <a-radio-group v-model="form.timeType">
        <a-radio value="1">至今</a-radio>
        <a-radio value="2">时间区间</a-radio>
        <a-radio value="3">当前</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-row :gutter="12">
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="开始"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
          :validate-status="fieldStatus('startDate')"
          :help="validationErrors.startDate"
        >
          <a-input
            v-model.trim="form.startDate"
            :disabled="dateState.disableStart"
            :placeholder="startDatePlaceholder"
          />
        </a-form-item>
      </a-col>
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="结束"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
          :validate-status="fieldStatus('endDate')"
          :help="validationErrors.endDate"
        >
          <a-input
            v-model.trim="form.endDate"
            :disabled="dateState.disableEnd"
            :placeholder="endDatePlaceholder"
          />
        </a-form-item>
      </a-col>
    </a-row>
    <a-row :gutter="12">
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="单位值"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
          :validate-status="fieldStatus('price')"
          :help="validationErrors.price"
        >
          <a-input v-model.trim="form.price" placeholder="如 10000" />
        </a-form-item>
      </a-col>
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="展示单位"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
        >
          <a-input v-model.trim="form.unit" placeholder="如 万元" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item label="横轴模式">
      <a-radio-group v-model="form.xTurn">
        <a-radio value="0">时间</a-radio>
        <a-radio value="1">维度</a-radio>
      </a-radio-group>
    </a-form-item>
    <a-form-item v-if="form.type === 'pie'" label="统计方向">
      <a-select v-model="form.direction" allowClear placeholder="请选择统计方向">
        <a-select-option value="X">指标</a-select-option>
        <a-select-option value="Y">维度</a-select-option>
      </a-select>
    </a-form-item>
    <a-form-item v-else label="维度编码">
      <a-input
        v-model.trim="form.direction"
        placeholder="横轴为时间时选择国库或地区编码"
      />
    </a-form-item>
    <a-row v-if="form.type === 'pie'" :gutter="12">
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="国库/地区"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
        >
          <a-input v-model.trim="form.GK" placeholder="编码" />
        </a-form-item>
      </a-col>
      <a-col :xs="24" :sm="12">
        <a-form-item
          label="指标编码"
          :label-col="{ span: 10 }"
          :wrapper-col="{ span: 14 }"
        >
          <a-select v-model="form.indexName" allowClear placeholder="请选择指标">
            <a-select-option
              v-for="item in indexOptions"
              :key="String(item.id)"
              :value="String(item.id)"
            >
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>

<script>
import barIcon from '@/assets/9.png'
import lineIcon from '@/assets/8.png'
import pieIcon from '@/assets/10.png'
import combinedIcon from '@/assets/7.png'
import { CHART_TYPES } from '@/utils/indexLibraryScheme'

const ICONS = {
  bar: barIcon,
  line: lineIcon,
  pie: pieIcon,
  barAndLine: combinedIcon
}

const DATE_FORMATS = {
  1: {
    hint: 'yyyy-MM-dd',
    valid(value) {
      const match = /^([1-9]\d{3})-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/.exec(value)
      if (!match) return false
      const date = new Date(Date.UTC(Number(match[1]), Number(match[2]) - 1, Number(match[3])))
      return date.getUTCFullYear() === Number(match[1]) &&
        date.getUTCMonth() === Number(match[2]) - 1 &&
        date.getUTCDate() === Number(match[3])
    }
  },
  2: {
    hint: 'yyyy-MM',
    valid: value => /^[1-9]\d{3}-(0[1-9]|1[0-2])$/.test(value)
  },
  3: {
    hint: 'yyyyQ[1-4]',
    valid: value => /^[1-9]\d{3}Q[1-4]$/.test(value)
  },
  4: {
    hint: 'yyyy',
    valid: value => /^[1-9]\d{3}$/.test(value)
  }
}

export function validateDateSelection(periodFlag, timeType, startDate, endDate) {
  const mode = String(timeType || '')
  const format = DATE_FORMATS[String(periodFlag)] || DATE_FORMATS[1]
  if (mode === '3') return ''
  if (!startDate) return '请输入开始日期'
  if (!format.valid(String(startDate))) {
    return `开始日期格式应为 ${format.hint}`
  }
  if (mode === '1' || mode === '4') return ''
  if (!endDate) return '请输入结束日期'
  if (!format.valid(String(endDate))) {
    return `结束日期格式应为 ${format.hint}`
  }
  return String(startDate) > String(endDate) ? '开始日期不能大于结束日期' : ''
}

export function validateIndexLibraryForm(form) {
  const source = form || {}
  const errors = {}
  if (!String(source.title || '').trim()) errors.title = '请输入图表标题'
  if (!CHART_TYPES.some(item => item.type === source.type)) {
    errors.type = '请选择图表类型'
  }
  if (!Array.isArray(source.schemecolumns) || !source.schemecolumns.length) {
    errors.schemecolumns = '请至少选择一个指标'
  }
  if (!source.dimensionFlag) errors.dimensionFlag = '请选择维度'
  if (!source.periodFlag) errors.periodFlag = '请选择时间粒度'
  if (!source.timeType) errors.timeType = '请选择时间范围'
  if (!String(source.price || '').trim()) errors.price = '请输入单位值'

  if (!errors.periodFlag && !errors.timeType) {
    const dateError = validateDateSelection(
      source.periodFlag,
      source.timeType,
      source.startDate,
      source.endDate
    )
    if (dateError) {
      const field = dateError.indexOf('结束日期') === 0 ? 'endDate' : 'startDate'
      errors[field] = dateError
    }
  }
  return errors
}

export default {
  name: 'IndexLibraryConvertForm',
  props: {
    form: {
      type: Object,
      required: true
    },
    indexOptions: {
      type: Array,
      default: () => []
    },
    indexLoading: Boolean
  },
  data() {
    return {
      chartTypes: CHART_TYPES.map(item => ({ ...item, icon: ICONS[item.type] })),
      validationErrors: {}
    }
  },
  computed: {
    selectedIndexIds() {
      return Array.isArray(this.form.schemecolumns)
        ? this.form.schemecolumns.map(item => String(item.chartId))
        : []
    },
    dateState() {
      const mode = String(this.form.timeType || '')
      return {
        disableStart: mode === '3',
        disableEnd: mode === '1' || mode === '3' || mode === '4'
      }
    },
    dateFormatHint() {
      const format = DATE_FORMATS[String(this.form.periodFlag)] || DATE_FORMATS[1]
      return format.hint
    },
    startDatePlaceholder() {
      return this.dateState.disableStart
        ? '后端按当前时间计算'
        : `格式：${this.dateFormatHint}`
    },
    endDatePlaceholder() {
      return this.dateState.disableEnd
        ? '该时间类型无需填写'
        : `格式：${this.dateFormatHint}`
    }
  },
  methods: {
    validate() {
      this.validationErrors = validateIndexLibraryForm(this.form)
      if (Object.keys(this.validationErrors).length) {
        this.$message.error('请完善图表配置后再预览')
        return Promise.resolve(false)
      }
      return Promise.resolve(true)
    },
    fieldStatus(field) {
      return this.validationErrors[field] ? 'error' : ''
    },
    selectChartType(type) {
      this.form.type = type
      if (type === 'barAndLine') {
        this.form.schemecolumns = this.form.schemecolumns.map((item, index) => ({
          ...item,
          chartDirection: item.chartDirection || (index ? 'Line' : 'Columnar')
        }))
      }
    },
    handleIndexChange(ids) {
      const current = Array.isArray(this.form.schemecolumns)
        ? this.form.schemecolumns
        : []
      this.form.schemecolumns = ids.map((id, index) => {
        const existing = current.find(item => String(item.chartId) === String(id))
        return existing || {
          chartId: id,
          chartDirection: this.form.type === 'barAndLine' && index > 0
            ? 'Line'
            : 'Columnar'
        }
      })
    },
    updateChartDirection(chartId, chartDirection) {
      this.form.schemecolumns = this.form.schemecolumns.map(item =>
        String(item.chartId) === String(chartId)
          ? { ...item, chartDirection }
          : item
      )
    },
    indexLabel(id) {
      const item = this.indexOptions.find(option => String(option.id) === String(id))
      return item ? item.name : id
    }
  }
}
</script>

<style scoped>
.chart-type-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(72px, 1fr));
  gap: 8px;
}

.chart-type-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 8px 4px;
  color: #595959;
  background: #fff;
  border: 1px solid #d9d9d9;
  cursor: pointer;
}

.chart-type-card:hover,
.chart-type-card:focus {
  border-color: #e51c46;
  outline: 2px solid rgba(229, 28, 70, 0.18);
}

.chart-type-card--selected {
  border-color: #e51c46;
}

.chart-type-card img {
  width: 38px;
  height: 38px;
  object-fit: contain;
}

.chart-type-card__label {
  font-size: 12px;
}

.chart-type-card__state {
  min-height: 18px;
  color: #767676;
  font-size: 11px;
}

.chart-type-card--selected .chart-type-card__state {
  color: #b51235;
  font-weight: 600;
}

.series-directions {
  margin: -8px 0 16px 29%;
  padding: 8px;
  background: #fafafa;
}

.series-direction-row {
  display: grid;
  grid-template-columns: 1fr 92px;
  gap: 8px;
  align-items: center;
  margin-bottom: 6px;
}

.series-direction-row:last-child {
  margin-bottom: 0;
}

@media (max-width: 640px) {
  .chart-type-grid {
    grid-template-columns: repeat(2, minmax(96px, 1fr));
  }
}
</style>
