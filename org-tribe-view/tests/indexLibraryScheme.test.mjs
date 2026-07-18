import assert from 'assert'
import { readFile } from 'fs/promises'
import vm from 'vm'

const utilsSource = await readFile(
  new URL('../src/utils/indexLibraryScheme.js', import.meta.url),
  'utf8'
)
const utilsUrl = `data:text/javascript;base64,${Buffer.from(utilsSource).toString('base64')}`
const {
  CHART_TYPES,
  normalizeSchemeRow,
  parseSchemeCondition,
  createInitialForm,
  buildPreviewPayload,
  buildSavePayload,
  hasChartData
} = await import(utilsUrl)

assert.deepStrictEqual(CHART_TYPES.map(item => item.type), [
  'bar',
  'line',
  'pie',
  'barAndLine'
])
assert.strictEqual(CHART_TYPES.some(item => item.type === 'map'), false)
assert.strictEqual(CHART_TYPES.some(item => item.type === 'strip'), false)
assert.strictEqual(CHART_TYPES.some(item => item.type === 'bigNumber'), false)

const productionRow = {
  ID: 's1',
  SCHEME_DESCR: '收入',
  SCHEME_CONDITON: '{"PERIOD_FLAG":"2"}',
  SCHEME_COLUMS: 'I1,I2',
  ADD_DATE: '2026-01-01',
  realname: '管理员'
}
assert.deepStrictEqual(normalizeSchemeRow(productionRow), {
  id: 's1',
  name: '收入',
  createTime: '2026-01-01',
  username: '管理员',
  raw: productionRow
})

assert.deepStrictEqual(parseSchemeCondition(''), {})
assert.deepStrictEqual(parseSchemeCondition(null), {})
assert.throws(() => parseSchemeCondition('{broken'), /方案条件格式错误/)
assert.throws(() => parseSchemeCondition('[]'), /方案条件格式错误/)

const normalizedCondition = parseSchemeCondition(JSON.stringify({
  SCHEME_ID: 's1',
  SCHEME_COLUMNS: [
    { CHART_ID: 'I1', CHART_DIRECTION: 'Columnar' },
    { chartid: 'I2', chartdirection: 'Line' }
  ],
  DIMENSION_FLAG: '1',
  PERIOD_FLAG: '2',
  TIME_TYPE: '2',
  START_DATE: '2026-01',
  END_DATE: '2026-06',
  PRICE: '10000',
  IS_RATE: false,
  DIRECTION: 'Y',
  GK: 'A',
  INDEX_NAME: 'I1',
  X_TURN: '1',
  TYPE: 'line',
  TITLE: '收入趋势',
  UNIT: '万元'
}))
assert.deepStrictEqual(normalizedCondition, {
  scheme_id: 's1',
  schemecolumns: [
    { chartId: 'I1', chartDirection: 'Columnar' },
    { chartId: 'I2', chartDirection: 'Line' }
  ],
  dimensionFlag: '1',
  periodFlag: '2',
  timeType: '2',
  startDate: '2026-01',
  endDate: '2026-06',
  price: '10000',
  isRate: false,
  direction: 'Y',
  GK: 'A',
  indexName: 'I1',
  xTurn: '1',
  type: 'line',
  title: '收入趋势',
  unit: '万元'
})

const form = createInitialForm(productionRow, normalizedCondition)
assert.strictEqual(form.scheme_id, 's1')
assert.strictEqual(form.schemeName, '收入')
assert.strictEqual(form.type, 'line')
assert.deepStrictEqual(form.schemecolumns, normalizedCondition.schemecolumns)

for (const type of CHART_TYPES.map(item => item.type)) {
  const payload = buildPreviewPayload({ ...form, type }, productionRow)
  assert.strictEqual(payload.type, type)
  assert.strictEqual(payload.scheme_id, 's1')
  assert.strictEqual(payload.dimensionFlag, '1')
  assert.strictEqual(payload.periodFlag, '2')
  assert.strictEqual(payload.timeType, '2')
  assert.strictEqual(payload.startDate, '2026-01')
  assert.strictEqual(payload.endDate, '2026-06')
  assert.strictEqual(payload.price, '10000')
  assert.strictEqual(payload.isRate, false)
  assert.strictEqual(payload.direction, 'Y')
  assert.strictEqual(payload.GK, 'A')
  assert.strictEqual(payload.indexName, 'I1')
  assert.strictEqual(payload.xTurn, '1')
  assert.deepStrictEqual(payload.schemecolumns, normalizedCondition.schemecolumns)
  assert.strictEqual(Object.prototype.hasOwnProperty.call(payload, 'schemeSql'), false)
  assert.strictEqual(Object.prototype.hasOwnProperty.call(payload, 'previewToken'), false)
}
assert.throws(
  () => buildPreviewPayload({ ...form, type: 'map' }, productionRow),
  /不支持的图表类型/
)

const previewCondition = Object.freeze(buildPreviewPayload(form, productionRow))
assert.throws(
  () => buildSavePayload(
    { ...form, title: '已修改', content: '{"series":[]}' },
    productionRow,
    false,
    previewCondition
  ),
  /请先预览当前图表配置/
)
for (const type of ['map', 'strip', 'bigNumber']) {
  assert.throws(
    () => buildSavePayload(
      { ...form, type: 'bar', content: '{"series":[]}' },
      productionRow,
      true,
      { ...previewCondition, type }
    ),
    /不支持的图表类型/
  )
}
const savePayload = buildSavePayload(
  { ...form, title: '保存时不应覆盖', content: '{"series":[]}' },
  productionRow,
  true,
  previewCondition
)
assert.deepStrictEqual(Object.keys(savePayload).sort(), ['condition', 'content'])
assert.strictEqual(savePayload.content, '{"series":[]}')
assert.deepStrictEqual(JSON.parse(savePayload.condition), previewCondition)
assert.strictEqual(JSON.parse(savePayload.condition).title, '收入趋势')
for (const forbidden of ['map', 'strip', 'bigNumber', 'schemeSql', 'previewToken']) {
  assert.strictEqual(savePayload.condition.includes(forbidden), false)
}

assert.strictEqual(hasChartData(null), false)
assert.strictEqual(hasChartData({ result: 'failed', data: [[1]] }), false)
assert.strictEqual(hasChartData({ result: 'success', type: 'bar', x: [], data: [[1]] }), false)
assert.strictEqual(hasChartData({ result: 'success', type: 'line', x: ['一月'], data: [[]] }), false)
assert.strictEqual(hasChartData({ result: 'success', type: 'bar', x: ['一月'], data: [[0]] }), true)
assert.strictEqual(hasChartData({ result: 'success', type: 'pie', data: [] }), false)
assert.strictEqual(hasChartData({
  result: 'success',
  type: 'pie',
  data: [{ name: '收入', value: '0' }]
}), true)

const apiSource = await readFile(
  new URL('../src/api/indexLibraryScheme.js', import.meta.url),
  'utf8'
)
const expectedRoutes = [
  '/vis/api/indexSchemeController/selectSchemeTable',
  '/vis/api/indexSchemeController/deleteScheme',
  '/vis/api/indexSchemeController/getIndexInfo',
  '/vis/api/IndexBarLine/getIndexBarLineData',
  '/vis/api/IndexBarLine/saveIndexBarLine',
  '/vis/api/IndexPie/getIndexPieData',
  '/vis/api/IndexPie/saveIndexPie'
]
for (const route of expectedRoutes) {
  assert.strictEqual(apiSource.includes(route), true, `missing API route: ${route}`)
}
assert.strictEqual((apiSource.match(/postAction\(/g) || []).length, 7)
assert.strictEqual(/IndexPie\/getIndexPieData[\s\S]+IndexPie\/saveIndexPie/.test(apiSource), true)
assert.strictEqual(/IndexBarLine\/getIndexBarLineData[\s\S]+IndexBarLine\/saveIndexBarLine/.test(apiSource), true)
assert.strictEqual(/Index(Map|Strip|BigNumber)|schemeSql/.test(apiSource), false)

const listSource = await readFile(
  new URL('../src/views/vis/IndexLibraryList.vue', import.meta.url),
  'utf8'
)
assert.match(listSource, /begin_time/)
assert.match(listSource, /end_time/)
assert.match(listSource, /listSchemes/)
assert.match(listSource, /deleteScheme/)
assert.match(listSource, /normalizeSchemeRow/)
assert.match(listSource, /INDEX_NAME/)
assert.match(listSource, /realname/)
assert.match(listSource, /convertModal\.open\(record\.raw\)/)
assert.doesNotMatch(listSource, /indexLibraryScheme\/getPage/)
assert.doesNotMatch(listSource, /indexLibraryScheme\/del/)
assert.doesNotMatch(listSource, /ListMixin/)

const componentScript = listSource
  .match(/<script>([\s\S]*?)<\/script>/)[1]
  .replace(/^import .*$/gm, '')
  .replace('export default', 'component =')
const listRequests = []
const deleteRequests = []
let listResponse = {
  result: 'success',
  rows: [productionRow],
  total: '11'
}
let deleteResponse = {
  result: 'success',
  msg: '删除指标方案成功'
}
const componentContext = {
  component: null,
  IndexLibraryConvertModal: {},
  normalizeSchemeRow,
  listSchemes(params) {
    listRequests.push(params)
    return Promise.resolve(listResponse)
  },
  deleteScheme(params) {
    deleteRequests.push(params)
    return Promise.resolve(deleteResponse)
  }
}
vm.runInNewContext(componentScript, componentContext)
const listComponent = componentContext.component

function createListVm() {
  const messages = []
  const openedRecords = []
  const instance = {
    ...listComponent.data(),
    $message: {
      error(message) {
        messages.push({ type: 'error', message })
      },
      success(message) {
        messages.push({ type: 'success', message })
      }
    },
    $refs: {
      convertModal: {
        open(record) {
          openedRecords.push(record)
        }
      }
    }
  }
  Object.assign(instance, listComponent.methods)
  return { instance, messages, openedRecords }
}

const listVm = createListVm()
listVm.instance.queryParam = {
  name: '收入',
  begin_time: '2026-01-01',
  end_time: '2026-01-31'
}
listVm.instance.pagination.current = 2
await listVm.instance.loadData()
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(listRequests.pop())),
  {
  name: '收入',
  begin_time: '2026-01-01',
  end_time: '2026-01-31',
  pageNo: 2,
  pageSize: 10
  }
)
assert.strictEqual(listVm.instance.loading, false)
assert.strictEqual(listVm.instance.pagination.total, 11)
assert.strictEqual(listVm.instance.dataSource[0].raw, productionRow)

const invalidDateVm = createListVm()
invalidDateVm.instance.onDateChange([
  { format: pattern => pattern === 'YYYY-MM-DD' ? '2026-02-01' : '' },
  { format: pattern => pattern === 'YYYY-MM-DD' ? '2026-01-31' : '' }
], ['unstable-start', 'unstable-end'])
assert.strictEqual(invalidDateVm.instance.queryParam.begin_time, '2026-02-01')
assert.strictEqual(invalidDateVm.instance.queryParam.end_time, '2026-01-31')
invalidDateVm.instance.queryParam = {
  name: '',
  begin_time: '2026-02-01',
  end_time: '2026-01-31'
}
const requestsBeforeInvalidDate = listRequests.length
await invalidDateVm.instance.loadData()
assert.strictEqual(listRequests.length, requestsBeforeInvalidDate)
assert.deepStrictEqual(invalidDateVm.messages, [{
  type: 'error',
  message: '开始日期不能大于结束日期'
}])

const paginationVm = createListVm()
paginationVm.instance.pagination.current = 4
await paginationVm.instance.searchQuery()
assert.strictEqual(paginationVm.instance.pagination.current, 1)
paginationVm.instance.queryParam = {
  name: '待重置',
  begin_time: '2026-01-01',
  end_time: '2026-01-31'
}
paginationVm.instance.createDateRange = ['start', 'end']
await paginationVm.instance.handleReset()
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(paginationVm.instance.queryParam)),
  {
    name: '',
    begin_time: '',
    end_time: ''
  }
)
assert.strictEqual(paginationVm.instance.createDateRange.length, 0)
await paginationVm.instance.handleTableChange({ current: 3, pageSize: 20 })
assert.strictEqual(paginationVm.instance.pagination.current, 3)
assert.strictEqual(paginationVm.instance.pagination.pageSize, 20)

const actionVm = createListVm()
const normalizedRow = normalizeSchemeRow(productionRow)
actionVm.instance.handleConvert(normalizedRow)
assert.strictEqual(actionVm.openedRecords[0], productionRow)
actionVm.instance.dataSource = [normalizedRow]
actionVm.instance.pagination.current = 3
await actionVm.instance.handleDelete(normalizedRow)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(deleteRequests.pop())),
  { schemeId: 's1' }
)
assert.strictEqual(actionVm.instance.pagination.current, 2)
assert.deepStrictEqual(actionVm.messages, [{
  type: 'success',
  message: '删除指标方案成功'
}])

const modalSource = await readFile(
  new URL('../src/views/vis/modules/IndexLibraryConvertModal.vue', import.meta.url),
  'utf8'
)
const formSource = await readFile(
  new URL('../src/views/vis/modules/IndexLibraryConvertForm.vue', import.meta.url),
  'utf8'
)
const convertSource = `${modalSource}\n${formSource}`
assert.match(modalSource, /width="80%"/)
assert.match(formSource, /a-form-model/)
assert.match(formSource, /role="radiogroup"/)
assert.match(formSource, /role="radio"/)
assert.match(formSource, /aria-checked/)
assert.match(formSource, /已选择/)
assert.match(modalSource, /previewBarLine/)
assert.match(modalSource, /previewPie/)
assert.match(modalSource, /previewReady/)
assert.match(modalSource, /保存图表/)
assert.match(modalSource, /暂无可预览数据/)
assert.match(modalSource, /:disabled="!previewReady/)
for (const icon of ['9.png', '8.png', '10.png', '7.png']) {
  assert.match(formSource, new RegExp(icon.replace('.', '\\.')))
}
assert.doesNotMatch(convertSource, /value="map"/)
assert.doesNotMatch(convertSource, /toGallery/)
assert.doesNotMatch(convertSource, /SourceService|BigNumber|条形图/)

const chartSource = await readFile(
  new URL('../src/views/vis/modules/IndexLibraryChartPreview.vue', import.meta.url),
  'utf8'
)
assert.match(chartSource, /echarts\.init/)
assert.match(chartSource, /addEventListener\('resize'/)
assert.match(chartSource, /removeEventListener\('resize'/)
assert.match(chartSource, /\.dispose\(\)/)
assert.match(chartSource, /getDataURL/)

function sfcScript(source) {
  return source
    .match(/<script>([\s\S]*?)<\/script>/)[1]
    .replace(/^import[\s\S]*?from ['"][^'"]+['"]\s*$/gm, '')
    .replace(/export function /g, 'function ')
    .replace('export default', 'component =')
}

const chartContext = {
  component: null,
  echarts: {
    init() {
      throw new Error('chart init should be replaced by the lifecycle test')
    }
  }
}
vm.runInNewContext(sfcScript(chartSource), chartContext)
const chartComponent = chartContext.component
const chartResponse = {
  result: 'success',
  type: 'barAndLine',
  x: ['一月', '二月'],
  data: [[0, '12.5'], ['3.2', '']],
  indexInfoList: [
    { I1: '收入', INDEX_TYPE: '0', INDEX_CORRE_TABLE: 'T1' },
    { I2: '增长率', INDEX_TYPE: '1', INDEX_CORRE_TABLE: 'T2' }
  ]
}
const chartCondition = {
  schemecolumns: [
    { chartId: 'I1', chartDirection: 'Columnar' },
    { chartId: 'I2', chartDirection: 'Line' }
  ]
}
const combinedOption = chartContext.buildChartOption(
  'barAndLine',
  chartResponse,
  chartCondition
)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(combinedOption.legend.data)),
  ['收入', '增长率']
)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(combinedOption.xAxis.data)),
  ['一月', '二月']
)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(combinedOption.series.map(item => item.type))),
  ['bar', 'line']
)
assert.strictEqual(combinedOption.series[0].data[0], 0)
assert.strictEqual(combinedOption.series[1].data[0], '3.2')

for (const type of ['bar', 'line']) {
  const option = chartContext.buildChartOption(type, chartResponse, chartCondition)
  assert.strictEqual(option.series.every(item => item.type === type), true)
}
const pieOption = chartContext.buildChartOption('pie', {
  result: 'success',
  type: 'pie',
  data: [{ name: '收入', value: 0 }]
}, {})
assert.strictEqual(pieOption.series[0].type, 'pie')
assert.strictEqual(pieOption.series[0].data[0].value, 0)
assert.strictEqual(pieOption.legend.orient, 'vertical')

const resizeListeners = []
const removedResizeListeners = []
let disposed = false
const chartInstance = {
  setOption() {},
  resize() {},
  getDataURL() {
    return 'data:image/png;base64,chart-preview'
  },
  dispose() {
    disposed = true
  }
}
chartContext.echarts.init = () => chartInstance
chartContext.window = {
  addEventListener(type, listener) {
    resizeListeners.push({ type, listener })
  },
  removeEventListener(type, listener) {
    removedResizeListeners.push({ type, listener })
  }
}
const chartVm = {
  ...chartComponent.data(),
  type: 'barAndLine',
  response: chartResponse,
  condition: chartCondition,
  $refs: { chart: {} }
}
Object.assign(chartVm, chartComponent.methods)
chartComponent.mounted.call(chartVm)
assert.strictEqual(resizeListeners.length, 1)
assert.strictEqual(resizeListeners[0].listener, chartVm.handleResize)
chartComponent.beforeDestroy.call(chartVm)
assert.strictEqual(removedResizeListeners.length, 1)
assert.strictEqual(removedResizeListeners[0].listener, chartVm.handleResize)
assert.strictEqual(disposed, true)
assert.strictEqual(chartVm.chart, null)

const indexInfoRequests = []
const barPreviewRequests = []
const piePreviewRequests = []
const barSaveRequests = []
const pieSaveRequests = []
let indexInfoResponse = {
  result: 'success',
  indexInfoList: [
    { id: 'I1', name: '收入' },
    { id: 'I2', name: '增长率' }
  ]
}
let barPreviewResponse = chartResponse
let piePreviewResponse = {
  result: 'success',
  type: 'pie',
  data: [{ name: '收入', value: '0' }]
}
let barSaveResponse = { result: 'success', msg: '添加成功' }
let pieSaveResponse = { result: 'success', msg: '添加成功' }
let previewImage = 'data:image/png;base64,chart-preview'
const modalContext = {
  component: null,
  IndexLibraryChartPreview: chartComponent,
  IndexLibraryConvertForm: {},
  CHART_TYPES,
  parseSchemeCondition,
  createInitialForm,
  buildPreviewPayload,
  buildSavePayload,
  hasChartData,
  buildChartOption: chartContext.buildChartOption,
  getIndexInfo(params) {
    indexInfoRequests.push(params)
    return Promise.resolve(indexInfoResponse)
  },
  previewBarLine(params) {
    barPreviewRequests.push(params)
    return Promise.resolve(barPreviewResponse)
  },
  previewPie(params) {
    piePreviewRequests.push(params)
    return Promise.resolve(piePreviewResponse)
  },
  saveBarLine(params) {
    barSaveRequests.push(params)
    return Promise.resolve(barSaveResponse)
  },
  savePie(params) {
    pieSaveRequests.push(params)
    return Promise.resolve(pieSaveResponse)
  }
}
vm.runInNewContext(sfcScript(modalSource), modalContext)
const modalComponent = modalContext.component

function createModalVm() {
  const messages = []
  const emitted = []
  const instance = {
    ...modalComponent.data(),
    $message: {
      error(message) {
        messages.push({ type: 'error', message })
      },
      warning(message) {
        messages.push({ type: 'warning', message })
      },
      success(message) {
        messages.push({ type: 'success', message })
      }
    },
    $refs: {
      form: {
        validate(callback) {
          callback(true)
        }
      },
      chartPreview: {
        getDataURL() {
          return previewImage
        }
      }
    },
    $set(target, key, value) {
      target[key] = value
    },
    $nextTick(callback) {
      if (callback) callback()
      return Promise.resolve()
    },
    $emit(event) {
      emitted.push(event)
    }
  }
  Object.assign(instance, modalComponent.methods)
  return { instance, messages, emitted }
}

const modalRecord = {
  ID: 'scheme-modal',
  SCHEME_DESCR: '财政收入',
  SCHEME_COLUMS: 'I1,I2',
  SCHEME_CONDITON: JSON.stringify({
    SCHEME_ID: 'scheme-modal',
    SCHEME_COLUMNS: [
      { CHART_ID: 'I1', CHART_DIRECTION: 'Columnar' },
      { CHART_ID: 'I2', CHART_DIRECTION: 'Line' }
    ],
    DIMENSION_FLAG: '1',
    PERIOD_FLAG: '2',
    TIME_TYPE: '2',
    START_DATE: '2026-01',
    END_DATE: '2026-06',
    PRICE: '10000',
    UNIT: '万元',
    DIRECTION: 'X',
    GK: 'GK01',
    INDEX_NAME: 'I1',
    X_TURN: '0',
    TYPE: 'barAndLine',
    TITLE: '财政收入趋势'
  })
}
const openVm = createModalVm()
await openVm.instance.open(modalRecord)
assert.strictEqual(openVm.instance.visible, true)
assert.strictEqual(openVm.instance.form.schemeName, '财政收入')
assert.strictEqual(openVm.instance.form.title, '财政收入趋势')
assert.strictEqual(openVm.instance.form.type, 'barAndLine')
assert.strictEqual(openVm.instance.form.dimensionFlag, '1')
assert.strictEqual(openVm.instance.form.periodFlag, '2')
assert.strictEqual(openVm.instance.form.timeType, '2')
assert.strictEqual(openVm.instance.form.startDate, '2026-01')
assert.strictEqual(openVm.instance.form.endDate, '2026-06')
assert.strictEqual(openVm.instance.form.price, '10000')
assert.strictEqual(openVm.instance.form.unit, '万元')
assert.strictEqual(openVm.instance.form.direction, 'X')
assert.strictEqual(openVm.instance.form.GK, 'GK01')
assert.strictEqual(openVm.instance.form.indexName, 'I1')
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(indexInfoRequests.pop())),
  { SCHEME_COLUMS: 'I1,I2' }
)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(openVm.instance.indexOptions)),
  indexInfoResponse.indexInfoList
)

const noUnitCondition = JSON.parse(modalRecord.SCHEME_CONDITON)
delete noUnitCondition.UNIT
const noUnitVm = createModalVm()
await noUnitVm.instance.open({
  ...modalRecord,
  SCHEME_CONDITON: JSON.stringify(noUnitCondition)
})
assert.strictEqual(noUnitVm.instance.form.price, '10000')
assert.strictEqual(noUnitVm.instance.form.unit, undefined)

openVm.instance.previewReady = true
openVm.instance.previewOption = { series: [] }
modalComponent.watch.form.handler.call(openVm.instance)
assert.strictEqual(openVm.instance.previewReady, false)
assert.strictEqual(openVm.instance.previewOption, null)

const previewVm = createModalVm()
await previewVm.instance.open(modalRecord)
await previewVm.instance.handlePreview()
assert.strictEqual(barPreviewRequests.length > 0, true)
assert.strictEqual(previewVm.instance.previewReady, true)
assert.strictEqual(previewVm.instance.previewOption.series.length, 2)
assert.notStrictEqual(previewVm.instance.frozenCondition, previewVm.instance.form)
assert.strictEqual(
  typeof previewVm.instance.frozenCondition.option,
  'string'
)
assert.deepStrictEqual(
  JSON.parse(previewVm.instance.frozenCondition.option),
  JSON.parse(JSON.stringify(previewVm.instance.previewOption))
)

const frozenTitle = previewVm.instance.frozenCondition.title
previewVm.instance.form.title = '保存时不得覆盖冻结标题'
await previewVm.instance.handleSave()
const savedBar = barSaveRequests.pop()
assert.strictEqual(JSON.parse(savedBar.condition).title, frozenTitle)
assert.strictEqual(JSON.parse(savedBar.condition).price, '10000')
assert.strictEqual(JSON.parse(savedBar.condition).unit, '万元')
assert.strictEqual(
  savedBar.content,
  'data:image/png;base64,chart-preview'
)
assert.strictEqual(previewVm.instance.visible, false)
assert.deepStrictEqual(previewVm.emitted, ['ok'])

const pieVm = createModalVm()
await pieVm.instance.open({
  ...modalRecord,
  SCHEME_CONDITON: JSON.stringify({
    ...JSON.parse(modalRecord.SCHEME_CONDITON),
    TYPE: 'pie'
  })
})
await pieVm.instance.handlePreview()
assert.strictEqual(piePreviewRequests.length > 0, true)
assert.strictEqual(pieVm.instance.previewReady, true)
await pieVm.instance.handleSave()
assert.strictEqual(pieSaveRequests.length > 0, true)

const emptyVm = createModalVm()
barPreviewResponse = {
  result: 'success',
  type: 'bar',
  x: ['一月'],
  data: [[]],
  indexInfoList: []
}
await emptyVm.instance.open({
  ...modalRecord,
  SCHEME_CONDITON: JSON.stringify({
    ...JSON.parse(modalRecord.SCHEME_CONDITON),
    TYPE: 'bar'
  })
})
await emptyVm.instance.handlePreview()
assert.strictEqual(emptyVm.instance.previewReady, false)
assert.strictEqual(emptyVm.instance.previewState, 'empty')

const previewFailureVm = createModalVm()
barPreviewResponse = { result: 'failed', msg: '查询失败', data: [] }
await previewFailureVm.instance.open(modalRecord)
previewFailureVm.instance.form.type = 'bar'
await previewFailureVm.instance.handlePreview()
assert.strictEqual(previewFailureVm.instance.visible, true)
assert.strictEqual(previewFailureVm.instance.previewReady, false)
assert.deepStrictEqual(previewFailureVm.messages.slice(-1), [{
  type: 'error',
  message: '查询失败'
}])

barPreviewResponse = chartResponse
barSaveResponse = { result: 'failed', msg: '保存失败' }
const saveFailureVm = createModalVm()
await saveFailureVm.instance.open(modalRecord)
await saveFailureVm.instance.handlePreview()
await saveFailureVm.instance.handleSave()
assert.strictEqual(saveFailureVm.instance.visible, true)
assert.deepStrictEqual(saveFailureVm.messages.slice(-1), [{
  type: 'error',
  message: '保存失败'
}])

barSaveResponse = { result: 'success', msg: '添加成功' }
previewImage = ''
const imageFailureVm = createModalVm()
await imageFailureVm.instance.open(modalRecord)
await imageFailureVm.instance.handlePreview()
const saveCountBeforeImageFailure = barSaveRequests.length
await imageFailureVm.instance.handleSave()
assert.strictEqual(barSaveRequests.length, saveCountBeforeImageFailure)
assert.strictEqual(imageFailureVm.instance.visible, true)
assert.deepStrictEqual(imageFailureVm.messages.slice(-1), [{
  type: 'error',
  message: '预览图片生成失败，请重新预览'
}])

console.log('indexLibraryScheme tests passed')
