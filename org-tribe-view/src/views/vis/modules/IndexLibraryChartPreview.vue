<template>
  <div ref="chart" class="index-chart-preview" role="img" :aria-label="chartLabel"></div>
</template>

<script>
import echarts from 'echarts'
import { PRODUCTION_COLORS } from '@/utils/indexLibraryScheme'

const META_KEYS = ['INDEX_TYPE', 'INDEX_CORRE_TABLE', 'id', 'chartId']

function seriesName(item, index) {
  if (!item || typeof item !== 'object') return `指标${index + 1}`
  if (item.name !== undefined && item.name !== null) return String(item.name)
  const key = Object.keys(item).find(name => META_KEYS.indexOf(name) === -1)
  return key && item[key] !== undefined ? String(item[key]) : `指标${index + 1}`
}

function seriesId(item, index) {
  if (!item || typeof item !== 'object') return String(index)
  if (item.id !== undefined && item.id !== null) return String(item.id)
  const key = Object.keys(item).find(name => META_KEYS.indexOf(name) === -1)
  return key || String(index)
}

function combinedSeriesType(item, index, condition) {
  const columns = condition && Array.isArray(condition.schemecolumns)
    ? condition.schemecolumns
    : []
  const id = seriesId(item, index)
  const column = columns.find(entry =>
    entry && String(entry.chartId) === id
  ) || columns[index]
  return column && String(column.chartDirection).toLowerCase() === 'line'
    ? 'line'
    : 'bar'
}

function gradientEndColor(color) {
  const match = /^#([0-9a-f]{6})$/i.exec(String(color || ''))
  if (!match) return color
  const value = match[1]
  const channels = [0, 2, 4].map(offset =>
    parseInt(value.slice(offset, offset + 2), 16)
  )
  return `rgba(${channels.join(', ')}, 0.15)`
}

function barGradient(color) {
  return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
    { offset: 0, color },
    { offset: 1, color: gradientEndColor(color) }
  ])
}

export function buildCartesianSeries(type, response, condition) {
  const indexInfoList = Array.isArray(response.indexInfoList)
    ? response.indexInfoList
    : []
  const values = Array.isArray(response.data) ? response.data : []
  return values.map((data, index) => {
    const item = indexInfoList[index]
    const series = {
      name: seriesName(item, index),
      type: type === 'barAndLine'
        ? combinedSeriesType(item, index, condition)
        : type,
      data: Array.isArray(data) ? data.slice() : []
    }
    if (type === 'barAndLine') {
      series.yAxisIndex = item && String(item.INDEX_TYPE) === '1' ? 1 : 0
    }
    return series
  })
}

export function buildChartOption(type, response, condition) {
  const source = response && typeof response === 'object' ? response : {}
  const sourceCondition = condition && typeof condition === 'object'
    ? condition
    : {}
  const colors = Array.isArray(sourceCondition.colourArray) &&
    sourceCondition.colourArray.length
    ? sourceCondition.colourArray.slice()
    : PRODUCTION_COLORS.slice()
  if (type === 'pie') {
    const data = Array.isArray(source.data) ? source.data.slice() : []
    return {
      backgroundColor: '#252a30',
      color: colors,
      textStyle: { color: '#d9e2ec' },
      tooltip: { trigger: 'item' },
      legend: {
        orient: 'vertical',
        left: 'left',
        textStyle: { color: '#d9e2ec' },
        data: data.map(item => item && item.name)
      },
      series: [{
        type: 'pie',
        radius: '60%',
        data
      }]
    }
  }

  const indexInfoList = Array.isArray(source.indexInfoList)
    ? source.indexInfoList
    : []
  const xAxis = {
    type: 'category',
    data: Array.isArray(source.x) ? source.x.slice() : [],
    axisLine: { lineStyle: { color: '#788696' } },
    axisLabel: { color: '#d9e2ec' }
  }
  const valueAxis = {
    type: 'value',
    axisLine: { lineStyle: { color: '#788696' } },
    axisLabel: { color: '#d9e2ec' },
    splitLine: { lineStyle: { color: '#3b424a' } }
  }
  const option = {
    backgroundColor: '#252a30',
    color: colors,
    textStyle: { color: '#d9e2ec' },
    tooltip: { trigger: 'axis' },
    legend: {
      data: indexInfoList.map(seriesName),
      textStyle: { color: '#d9e2ec' }
    },
    grid: {
      top: 52,
      right: 28,
      bottom: 42,
      left: 68
    },
    xAxis,
    yAxis: valueAxis,
    series: buildCartesianSeries(type, source, sourceCondition)
  }
  if (sourceCondition.isGradual === true) {
    option.series.forEach((series, index) => {
      if (series.type === 'bar') {
        series.itemStyle = {
          color: barGradient(colors[index % colors.length])
        }
      }
    })
  }
  if (type === 'barAndLine') {
    option.xAxis = [xAxis]
    option.yAxis = [
      valueAxis,
      {
        ...valueAxis,
        position: 'right',
        axisLabel: {
          color: '#d9e2ec',
          formatter: '{value}%'
        },
        splitLine: { show: false }
      }
    ]
  }
  return option
}

export default {
  name: 'IndexLibraryChartPreview',
  props: {
    type: {
      type: String,
      required: true
    },
    response: {
      type: Object,
      required: true
    },
    condition: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      chart: null
    }
  },
  computed: {
    chartLabel() {
      return `${this.type === 'pie' ? '饼图' : '指标趋势图'}预览`
    }
  },
  watch: {
    type() {
      this.renderChart()
    },
    response: {
      deep: true,
      handler() {
        this.renderChart()
      }
    },
    condition: {
      deep: true,
      handler() {
        this.renderChart()
      }
    }
  },
  mounted() {
    window.addEventListener('resize', this.handleResize)
    this.renderChart()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
  },
  methods: {
    renderChart() {
      if (!this.$refs || !this.$refs.chart) return
      if (!this.chart) {
        this.chart = echarts.init(this.$refs.chart)
      }
      this.chart.setOption(
        buildChartOption(this.type, this.response, this.condition),
        true
      )
    },
    handleResize() {
      if (this.chart) this.chart.resize()
    },
    getDataURL() {
      if (!this.chart || typeof this.chart.getDataURL !== 'function') return ''
      return this.chart.getDataURL({
        pixelRatio: 2,
        backgroundColor: '#252a30'
      })
    }
  }
}
</script>

<style scoped>
.index-chart-preview {
  width: 100%;
  min-height: 460px;
  background: #252a30;
}
</style>
