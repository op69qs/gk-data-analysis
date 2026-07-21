<template>
  <div class="production-chart-form">
    <div class="form-label">选择图表类型：</div>
    <a-radio-group
      v-model="chartValue"
      class="chart-type-group"
      role="radiogroup"
      aria-label="图表类型"
      @change="onChartChange"
    >
      <a-radio
        v-for="item in chartTypes"
        :key="item.value"
        :value="item.value"
        role="radio"
        :aria-checked="String(form.type === item.type)"
      >
        <img
          :src="item.icon"
          :alt="`${item.label}图标`"
          :title="item.label"
        />
        <span class="sr-only">
          {{ item.label }}{{ form.type === item.type ? '，已选择' : '' }}
        </span>
      </a-radio>
    </a-radio-group>

    <a-form layout="inline" class="production-form-fields" @submit.prevent>
      <a-row>
        <a-col :md="12" :sm="12">
          <a-form-item
            label="图表标题"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 18 }"
            required
            :validate-status="fieldStatus('title')"
            :help="validationErrors.title"
          >
            <a-input
              v-model.trim="form.title"
              placeholder="请输入图表标题"
            />
          </a-form-item>
        </a-col>
        <a-col :md="18" :sm="18">
          <a-form-item
            label="指标"
            :label-col="{ span: 2 }"
            :wrapper-col="{ span: 22 }"
            required
            :validate-status="fieldStatus('schemecolumns')"
            :help="validationErrors.schemecolumns"
          >
            <a-input
              :value="indexDisplay"
              disabled
              aria-label="指标"
              placeholder="方案未返回指标"
            />
          </a-form-item>
        </a-col>
      </a-row>

      <a-row>
        <a-col :md="9" :sm="9">
          <a-form-item
            label="维度"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            required
            :validate-status="fieldStatus('dimensionFlag')"
            :help="validationErrors.dimensionFlag"
          >
            <a-select :value="form.dimensionFlag" disabled>
              <a-select-option value="1">国库</a-select-option>
              <a-select-option value="2">地区</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="9" :sm="9">
          <a-form-item
            label="周期"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            required
            :validate-status="fieldStatus('periodFlag')"
            :help="validationErrors.periodFlag"
          >
            <a-select :value="form.periodFlag" disabled>
              <a-select-option value="1">日</a-select-option>
              <a-select-option value="2">月</a-select-option>
              <a-select-option value="3">季</a-select-option>
              <a-select-option value="4">年</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="6">
          <a-form-item
            label="单位"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            required
            :validate-status="fieldStatus('price')"
            :help="validationErrors.price"
          >
            <a-select :value="form.price" disabled>
              <a-select-option
                v-for="item in priceOptions"
                :key="item.value"
                :value="item.value"
              >
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
      </a-row>

      <a-row>
        <a-col :md="9" :sm="9">
          <a-form-item
            label="时间类型"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 19 }"
            required
            :validate-status="fieldStatus('timeType')"
            :help="validationErrors.timeType"
          >
            <a-radio-group
              :value="form.timeType"
              @change="onTimeTypeChange"
            >
              <a-radio
                v-for="item in timeTypeOptions"
                :key="item.value"
                :value="item.value"
              >
                {{ item.label }}
              </a-radio>
            </a-radio-group>
          </a-form-item>
        </a-col>
        <a-col :md="9" :sm="9">
          <a-form-item
            v-if="!dateControl.disabled"
            label="选择时间"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18 }"
            required
            :validate-status="dateValidationStatus"
            :help="dateValidationHelp"
          >
            <el-date-picker
              v-if="isDayOrMonth && dateControl.range"
              v-model="dateRange"
              :type="dateControl.kind === 'date' ? 'daterange' : 'monthrange'"
              :value-format="dateControl.kind === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM'"
              range-separator="~"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            />
            <el-date-picker
              v-else-if="isDayOrMonth"
              v-model="startDateValue"
              :type="dateControl.kind"
              :value-format="dateControl.kind === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM'"
              :placeholder="dateControl.kind === 'date' ? '请选择日期' : '请选择月份'"
            />
            <data-month
              v-else-if="dateControl.kind === 'quarter'"
              :time-type="quarterTimeType"
              :chose-quarter-data="startQuarterValue"
              :chose-quarter-data1="endQuarterValue"
              @startquarter="setStartQuarter"
              @endquarter="setEndQuarter"
            />
            <data-year
              v-else-if="dateControl.kind === 'year' && dateControl.range"
              :start-year-data="form.startDate"
              :end-year-data="form.endDate"
              @startYearValue="setStartDate"
              @endYearValue="setEndDate"
            />
            <el-date-picker
              v-else
              v-model="startDateValue"
              type="year"
              value-format="yyyy"
              placeholder="请选择年份"
            />
          </a-form-item>
        </a-col>
        <a-col v-if="form.type === 'pie'" :md="4" :sm="4" class="switch-field">
          是否包含比率：
          <a-switch
            :checked="form.isRate === true"
            @change="value => setFormField('isRate', value)"
          />
        </a-col>
      </a-row>

      <a-row v-if="form.type !== 'pie'">
        <a-col :md="9" :sm="9">
          <a-form-item
            label="横轴显示"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('xTurn')"
            :help="validationErrors.xTurn"
          >
            <a-select v-model="form.xTurn" placeholder="请选择横轴显示">
              <a-select-option value="0">账期</a-select-option>
              <a-select-option value="1">{{ dimensionLabel }}</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="10" :sm="10">
          <a-form-item
            v-if="form.xTurn === '0'"
            :label="dimensionLabel"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('direction')"
            :help="validationErrors.direction"
          >
            <a-select
              :value="form.direction"
              allow-clear
              :placeholder="`请选择${dimensionLabel}`"
              @change="value => setFormField('direction', value)"
            >
              <a-select-option
                v-for="item in dimensionCandidates"
                :key="item.value"
                :value="item.value"
              >
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            v-else
            label="账期"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('dateId')"
            :help="validationErrors.dateId"
          >
            <a-select
              :value="form.dateId"
              allow-clear
              placeholder="请选择具体账期"
              @change="value => setFormField('dateId', value)"
            >
              <a-select-option
                v-for="item in accountingPeriodCandidates"
                :key="item"
                :value="item"
              >
                {{ item }}
              </a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="2" :sm="2" class="generate-action">
          <a-button type="primary" @click="generateImage">生成图片</a-button>
        </a-col>
      </a-row>

      <a-row v-else>
        <a-col :md="9" :sm="9">
          <a-form-item
            label="统计方向"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('direction')"
            :help="validationErrors.direction"
          >
            <a-select
              :value="form.direction"
              allow-clear
              placeholder="请选择统计方向"
              @change="value => setFormField('direction', value)"
            >
              <a-select-option value="X">指标</a-select-option>
              <a-select-option value="Y">维度</a-select-option>
            </a-select>
          </a-form-item>
        </a-col>
        <a-col :md="10" :sm="10">
          <a-form-item
            v-if="form.direction === 'X'"
            :label="dimensionLabel"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('GK')"
            :help="validationErrors.GK"
          >
            <a-select
              :value="form.GK"
              allow-clear
              :placeholder="`请选择${dimensionLabel}`"
              @change="value => setFormField('GK', value)"
            >
              <a-select-option
                v-for="item in dimensionCandidates"
                :key="item.value"
                :value="item.value"
              >
                {{ item.label }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            v-else-if="form.direction === 'Y'"
            label="指标"
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 18, offset: 1 }"
            required
            :validate-status="fieldStatus('indexName')"
            :help="validationErrors.indexName"
          >
            <a-select
              :value="form.indexName"
              allow-clear
              placeholder="请选择指标"
              @change="value => setFormField('indexName', value)"
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
        </a-col>
        <a-col :md="2" :sm="2" class="generate-action">
          <a-button type="primary" @click="generateImage">生成图片</a-button>
        </a-col>
      </a-row>

      <a-row v-if="generated" class="generated-settings">
        <a-col :md="18" :sm="24">
          <span class="setting-label">设置图表颜色：</span>
          <a-select
            v-model="selectedColorIndexes"
            mode="multiple"
            :max-tag-count="4"
            placeholder="请选择颜色"
          >
            <a-select-option
              v-for="(color, index) in productionColors"
              :key="index"
              :value="index"
            >
              <span class="color-option" :style="{ background: color }">
                色值{{ index + 1 }}
              </span>
            </a-select-option>
          </a-select>
        </a-col>
        <a-col
          v-if="form.type !== 'pie'"
          :md="6"
          :sm="24"
          class="switch-field"
        >
          是否渐变：
          <a-switch
            :checked="form.isGradual === true"
            @change="value => setFormField('isGradual', value)"
          />
        </a-col>
      </a-row>

      <div
        v-if="form.type === 'barAndLine'"
        class="series-directions"
        aria-label="柱状折线方向"
      >
        <div class="series-direction-header">
          <span>指标</span>
          <span>柱折方向</span>
          <span>操作</span>
        </div>
        <div
          v-for="(column, index) in form.schemecolumns"
          :key="String(column.chartId)"
          class="series-direction-row"
        >
          <span>{{ indexLabel(column.chartId) }}</span>
          <a-select
            :value="column.chartDirection"
            @change="value => updateChartDirection(column.chartId, value)"
          >
            <a-select-option value="Columnar">柱状图</a-select-option>
            <a-select-option value="Line">折线图</a-select-option>
          </a-select>
          <a-button type="danger" size="small" @click="removeSeries(index)">
            删除
          </a-button>
        </div>
      </div>
    </a-form>
  </div>
</template>

<script>
import barIcon from '@/assets/9.png'
import lineIcon from '@/assets/8.png'
import pieIcon from '@/assets/10.png'
import combinedIcon from '@/assets/7.png'
import dataMonth from './dataMonth.vue'
import dataYear from './dataYear.vue'
import {
  CHART_TYPES,
  PRODUCTION_COLORS,
  getTimeTypeOptions,
  getDateControl,
  getDimensionCandidates,
  normalizeBarLineColumns,
  validateProductionChartFields
} from '@/utils/indexLibraryScheme'

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

function normalizeQuarter(value) {
  return String(value || '').replace(/^([1-9]\d{3})-Q([1-4])$/, '$1Q$2')
}

function quarterPickerValue(value) {
  return String(value || '').replace(/^([1-9]\d{3})Q([1-4])$/, '$1-Q$2')
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
  return Object.assign(errors, validateProductionChartFields(source))
}

export default {
  name: 'IndexLibraryConvertForm',
  components: {
    dataMonth,
    dataYear
  },
  props: {
    form: {
      type: Object,
      required: true
    },
    indexOptions: {
      type: Array,
      default: () => []
    },
    indexLoading: Boolean,
    generated: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      chartTypes: CHART_TYPES.map(item => ({ ...item, icon: ICONS[item.type] })),
      productionColors: PRODUCTION_COLORS,
      validationErrors: {}
    }
  },
  computed: {
    chartValue: {
      get() {
        const selected = CHART_TYPES.find(item => item.type === this.form.type)
        return selected ? selected.value : ''
      },
      set(value) {
        const selected = CHART_TYPES.find(item => String(item.value) === String(value))
        this.selectChartType(selected ? selected.type : '')
      }
    },
    indexDisplay() {
      return this.indexOptions.map(item => item.name).join('、')
    },
    priceOptions() {
      const labels = {
        1: '元',
        10000: '万元',
        100000000: '亿元'
      }
      const value = String(this.form.price == null ? '' : this.form.price)
      const options = Object.keys(labels).map(key => ({
        value: key,
        label: labels[key]
      }))
      if (value && !labels[value]) {
        options.push({
          value,
          label: this.form.unit || value
        })
      }
      return options
    },
    timeTypeOptions() {
      return getTimeTypeOptions(this.form.dacct_radio)
    },
    dateControl() {
      return getDateControl(this.form.periodFlag, this.form.timeType)
    },
    isDayOrMonth() {
      return this.dateControl.kind === 'date' || this.dateControl.kind === 'month'
    },
    dateRange: {
      get() {
        return this.form.startDate && this.form.endDate
          ? [this.form.startDate, this.form.endDate]
          : []
      },
      set(value) {
        const range = Array.isArray(value) ? value : []
        this.setStartDate(range[0] || '')
        this.setEndDate(range[1] || '')
      }
    },
    startDateValue: {
      get() {
        return this.form.startDate
      },
      set(value) {
        this.setStartDate(value)
      }
    },
    quarterTimeType() {
      if (this.dateControl.range) return 2
      return String(this.form.timeType) === '4' ? 3 : 1
    },
    startQuarterValue() {
      return quarterPickerValue(this.form.startDate)
    },
    endQuarterValue() {
      return quarterPickerValue(this.form.endDate)
    },
    dateValidationStatus() {
      return this.validationErrors.startDate || this.validationErrors.endDate
        ? 'error'
        : ''
    },
    dateValidationHelp() {
      return this.validationErrors.startDate || this.validationErrors.endDate
    },
    dimensionLabel() {
      if (String(this.form.dimensionFlag) === '1') return '国库'
      if (String(this.form.dimensionFlag) === '2') return '地区'
      return '维度'
    },
    dimensionCandidates() {
      return getDimensionCandidates(this.form)
    },
    accountingPeriodCandidates() {
      const values = [this.form.dateId, this.form.startDate, this.form.endDate]
        .filter(value => value != null && String(value).trim() !== '')
        .map(value => String(value))
      return values.filter((value, index) => values.indexOf(value) === index)
    },
    selectedColorIndexes: {
      get() {
        const selected = Array.isArray(this.form.colourArray)
          ? this.form.colourArray
          : []
        return selected
          .map(color => PRODUCTION_COLORS.indexOf(color))
          .filter(index => index >= 0)
      },
      set(indexes) {
        this.selectColors(indexes)
      }
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
    onChartChange(event) {
      const value = event && event.target ? event.target.value : event
      const selected = CHART_TYPES.find(item => String(item.value) === String(value))
      if (selected) this.selectChartType(selected.type)
    },
    selectChartType(type) {
      const selected = CHART_TYPES.find(item => item.type === type)
      if (!selected) return
      this.setFormField('type', selected.type)
      this.setFormField('xTurn', '0')
      for (const field of ['direction', 'GK', 'indexName', 'dateId']) {
        this.setFormField(field, '')
      }
      if (type === 'barAndLine') {
        const columns = Array.isArray(this.form.schemecolumns)
          ? this.form.schemecolumns
          : []
        this.setFormField(
          'schemecolumns',
          normalizeBarLineColumns(columns)
        )
      }
    },
    onTimeTypeChange(event) {
      const value = event && event.target ? event.target.value : event
      this.setFormField('timeType', value)
      const state = getDateControl(this.form.periodFlag, value)
      if (state.disabled) {
        this.setStartDate('')
        this.setEndDate('')
      } else if (!state.range) {
        this.setEndDate('')
      }
    },
    setFormField(field, value) {
      if (typeof this.$set === 'function') {
        this.$set(this.form, field, value)
      } else {
        this.form[field] = value
      }
    },
    setStartDate(value) {
      this.setFormField('startDate', value || '')
    },
    setEndDate(value) {
      this.setFormField('endDate', value || '')
    },
    setStartQuarter(value) {
      this.setStartDate(normalizeQuarter(value))
    },
    setEndQuarter(value) {
      this.setEndDate(normalizeQuarter(value))
    },
    generateImage() {
      this.$emit('generate')
    },
    selectColors(indexes) {
      const selectedIndexes = Array.isArray(indexes) ? indexes : []
      this.setFormField('colourArray', selectedIndexes
        .map(index => PRODUCTION_COLORS[Number(index)])
        .filter(Boolean))
    },
    updateChartDirection(chartId, chartDirection) {
      const columns = Array.isArray(this.form.schemecolumns)
        ? this.form.schemecolumns
        : []
      this.setFormField(
        'schemecolumns',
        normalizeBarLineColumns(columns.map(item =>
          String(item.chartId) === String(chartId)
            ? { ...item, chartDirection }
            : item
        ))
      )
    },
    removeSeries(index) {
      const columns = Array.isArray(this.form.schemecolumns)
        ? this.form.schemecolumns
        : []
      this.setFormField(
        'schemecolumns',
        columns.filter((item, itemIndex) => itemIndex !== index)
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
.production-chart-form {
  color: #595959;
}

.form-label {
  margin-bottom: 8px;
}

.chart-type-group {
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  margin-bottom: 16px;
}

.chart-type-group ::v-deep .ant-radio-wrapper {
  width: 25%;
  margin-right: 0;
}

.chart-type-group img {
  width: 60px;
  height: 60px;
  object-fit: contain;
  vertical-align: middle;
}

.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

.production-form-fields {
  width: 100%;
}

.production-form-fields ::v-deep .ant-form-item {
  display: flex;
  width: 100%;
}

.production-form-fields ::v-deep .ant-form-item-control-wrapper {
  flex: 1;
  min-width: 0;
}

.production-form-fields ::v-deep .ant-select,
.production-form-fields ::v-deep .ant-input,
.production-form-fields ::v-deep .el-date-editor {
  width: 100%;
}

.switch-field {
  padding-top: 8px;
}

.generate-action {
  padding-top: 4px;
}

.generated-settings {
  margin: 16px 0;
  color: #777;
}

.setting-label {
  margin-right: 8px;
}

.generated-settings ::v-deep .ant-select {
  width: 80%;
}

.color-option {
  display: inline-block;
  padding: 0 8px;
  color: #fff;
  font-size: 12px;
}

.series-directions {
  margin-top: 16px;
  border: 1px solid #e8e8e8;
}

.series-direction-header,
.series-direction-row {
  display: grid;
  grid-template-columns: minmax(140px, 1fr) 160px 80px;
  gap: 12px;
  align-items: center;
  padding: 8px 12px;
}

.series-direction-header {
  background: #fafafa;
  font-weight: 600;
}

.series-direction-row {
  border-top: 1px solid #e8e8e8;
}

@media (max-width: 640px) {
  .chart-type-group ::v-deep .ant-radio-wrapper {
    width: 50%;
  }

  .production-form-fields > .ant-row > [class*='ant-col-'] {
    width: 100%;
  }

  .series-direction-header,
  .series-direction-row {
    grid-template-columns: 1fr;
  }
}
</style>
