import assert from 'assert'
import { readFile } from 'fs/promises'
import vm from 'vm'

function deferred() {
  let resolve
  let reject
  const promise = new Promise((done, fail) => {
    resolve = done
    reject = fail
  })
  return { promise, resolve, reject }
}

async function waitFor(predicate, failureMessage, timeoutMs = 1000) {
  const deadline = Date.now() + timeoutMs
  while (!predicate()) {
    if (Date.now() >= deadline) {
      throw new Error(failureMessage)
    }
    await new Promise(resolve => setTimeout(resolve, 0))
  }
}

async function settleWithin(promise, failureMessage, timeoutMs = 1000) {
  let timeout
  try {
    return await Promise.race([
      promise,
      new Promise((resolve, reject) => {
        timeout = setTimeout(() => reject(new Error(failureMessage)), timeoutMs)
      })
    ])
  } finally {
    clearTimeout(timeout)
  }
}

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
let listHandler = () => Promise.resolve(listResponse)
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
    return listHandler(params)
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

const olderListResponse = deferred()
const latestListResponse = deferred()
const listResponseQueue = [olderListResponse, latestListResponse]
listHandler = () => listResponseQueue.shift().promise
const concurrentListVm = createListVm()
const olderListRequest = concurrentListVm.instance.loadData()
concurrentListVm.instance.queryParam.name = '最新查询'
const latestListRequest = concurrentListVm.instance.loadData()
latestListResponse.resolve({
  result: 'success',
  rows: [{ ...productionRow, ID: 'latest', SCHEME_DESCR: '最新方案' }],
  total: '1'
})
await settleWithin(latestListRequest, 'latest list request did not settle')
olderListResponse.resolve({
  result: 'success',
  rows: [{ ...productionRow, ID: 'older', SCHEME_DESCR: '过期方案' }],
  total: '99'
})
await settleWithin(olderListRequest, 'older list request did not settle')
assert.strictEqual(concurrentListVm.instance.dataSource[0].id, 'latest')
assert.strictEqual(concurrentListVm.instance.pagination.total, 1)
assert.strictEqual(concurrentListVm.instance.loading, false)

const rejectedOlderListResponse = deferred()
const pendingLatestListResponse = deferred()
const rejectionQueue = [rejectedOlderListResponse, pendingLatestListResponse]
listHandler = () => rejectionQueue.shift().promise
const rejectedListVm = createListVm()
const rejectedOlderRequest = rejectedListVm.instance.loadData()
const pendingLatestRequest = rejectedListVm.instance.loadData()
rejectedOlderListResponse.reject(new Error('stale request failed'))
await settleWithin(rejectedOlderRequest, 'rejected older list request did not settle')
assert.strictEqual(rejectedListVm.instance.loading, true)
assert.deepStrictEqual(rejectedListVm.messages, [])
pendingLatestListResponse.resolve({
  result: 'success',
  rows: [{ ...productionRow, ID: 'latest-after-reject' }],
  total: '2'
})
await settleWithin(pendingLatestRequest, 'pending latest list request did not settle')
assert.strictEqual(rejectedListVm.instance.dataSource[0].id, 'latest-after-reject')
assert.strictEqual(rejectedListVm.instance.pagination.total, 2)
assert.strictEqual(rejectedListVm.instance.loading, false)
listHandler = () => Promise.resolve(listResponse)

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

const normalizedIdVm = createListVm()
await normalizedIdVm.instance.handleDelete({
  id: 'normalized-id',
  ID: 'legacy-top-level-id',
  raw: { ID: 'raw-production-id' }
})
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(deleteRequests.pop())),
  { schemeId: 'normalized-id' }
)

const missingIdVm = createListVm()
const deleteRequestsBeforeMissingId = deleteRequests.length
const missingIdResult = await missingIdVm.instance.handleDelete({ raw: {} })
assert.strictEqual(missingIdResult, false)
assert.strictEqual(deleteRequests.length, deleteRequestsBeforeMissingId)
assert.deepStrictEqual(missingIdVm.messages, [{
  type: 'error',
  message: '方案ID缺失，无法删除'
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
assert.match(modalSource, /ref="convertForm"/)
assert.match(modalSource, /await this\.\$refs\.convertForm\.validate\(\)/)
assert.doesNotMatch(formSource, /a-form-model/)
assert.match(formSource, /<a-form(?:\s|>)/)
assert.match(formSource, /<a-form-item(?:\s|>)/)
assert.doesNotMatch(formSource, /\$refs\.modelForm/)
assert.doesNotMatch(formSource, /:rules=|(?:\s)prop=/)
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
    .replace(/^\s*import[\s\S]*?from ['"][^'"]+['"]\s*$/gm, '')
    .replace(/export function /g, 'function ')
    .replace('export default', 'component =')
}

assert.match(formSource, /:disabled="dateState\.disableStart"/)
assert.match(formSource, /:disabled="dateState\.disableEnd"/)
const formContext = {
  component: null,
  barIcon: '9.png',
  lineIcon: '8.png',
  pieIcon: '10.png',
  combinedIcon: '7.png',
  CHART_TYPES
}
vm.runInNewContext(sfcScript(formSource), formContext)
const validateDateSelection = formContext.validateDateSelection
const formComponent = formContext.component
for (const sample of [
  ['1', '2', '2026-01-01', '2026-01-31'],
  ['2', '2', '2026-01', '2026-12'],
  ['3', '2', '2026Q1', '2026Q4'],
  ['4', '2', '2026', '2027']
]) {
  assert.strictEqual(validateDateSelection(...sample), '')
}
assert.strictEqual(validateDateSelection('1', '1', '2026-01-01', ''), '')
assert.strictEqual(validateDateSelection('2', '1', '2026-01', ''), '')
assert.strictEqual(validateDateSelection('3', '4', '2026Q2', ''), '')
assert.strictEqual(validateDateSelection('4', '3', '', ''), '')
assert.match(validateDateSelection('1', '2', '2026-02-30', '2026-03-01'), /yyyy-MM-dd/)
assert.match(validateDateSelection('2', '2', '2026-13', '2026-12'), /yyyy-MM/)
assert.match(validateDateSelection('3', '2', '2026-Q1', '2026Q4'), /yyyyQ\[1-4\]/)
assert.match(validateDateSelection('4', '2', '26', '2027'), /yyyy/)
assert.match(validateDateSelection('2', '2', '2026-12', '2026-01'), /开始日期不能大于结束日期/)
assert.match(validateDateSelection('1', '1', '', ''), /请输入开始日期/)
assert.match(validateDateSelection('1', '2', '2026-01-01', ''), /请输入结束日期/)
assert.match(validateDateSelection('4', '4', '', ''), /请输入开始日期/)

function createFormVm(form) {
  const messages = []
  const instance = {
    ...formComponent.data(),
    form,
    indexOptions: [],
    indexLoading: false,
    $message: {
      error(message) {
        messages.push(message)
      }
    }
  }
  Object.assign(instance, formComponent.methods)
  return { instance, messages }
}

const validFormVm = createFormVm({
  title: '财政收入趋势',
  type: 'bar',
  schemecolumns: [{ chartId: 'I1', chartDirection: 'Columnar' }],
  dimensionFlag: '1',
  periodFlag: '2',
  timeType: '2',
  startDate: '2026-01',
  endDate: '2026-06',
  price: '10000'
})
assert.strictEqual(await validFormVm.instance.validate(), true)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(validFormVm.instance.validationErrors)),
  {}
)
assert.deepStrictEqual(validFormVm.messages, [])

const invalidFormVm = createFormVm({
  title: '',
  type: '',
  schemecolumns: [],
  dimensionFlag: '',
  periodFlag: '1',
  timeType: '2',
  startDate: '2026-02-30',
  endDate: '',
  price: ''
})
assert.strictEqual(await invalidFormVm.instance.validate(), false)
assert.strictEqual(invalidFormVm.instance.validationErrors.title, '请输入图表标题')
assert.strictEqual(invalidFormVm.instance.validationErrors.type, '请选择图表类型')
assert.strictEqual(
  invalidFormVm.instance.validationErrors.schemecolumns,
  '请至少选择一个指标'
)
assert.strictEqual(invalidFormVm.instance.validationErrors.dimensionFlag, '请选择维度')
assert.match(invalidFormVm.instance.validationErrors.startDate, /yyyy-MM-dd/)
assert.strictEqual(invalidFormVm.instance.validationErrors.price, '请输入单位值')
assert.deepStrictEqual(invalidFormVm.messages, ['请完善图表配置后再预览'])

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
  JSON.parse(JSON.stringify(combinedOption.xAxis[0].data)),
  ['一月', '二月']
)
assert.strictEqual(Array.isArray(combinedOption.xAxis), true)
assert.strictEqual(Array.isArray(combinedOption.yAxis), true)
assert.strictEqual(combinedOption.yAxis.length, 2)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(combinedOption.series.map(item => item.type))),
  ['bar', 'line']
)
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(combinedOption.series.map(item => item.yAxisIndex))),
  [0, 1]
)
assert.strictEqual(combinedOption.series[0].data[0], 0)
assert.strictEqual(combinedOption.series[1].data[0], '3.2')

const bigScreenOption = JSON.parse(JSON.stringify(combinedOption))
assert.doesNotThrow(() => {
  bigScreenOption.yAxis[0].name = '金额(万元)'
  bigScreenOption.legend.top = '0%'
  bigScreenOption.legend.width = '75%'
  bigScreenOption.xAxis[0].data = ['三月']
})
assert.strictEqual(bigScreenOption.yAxis[0].name, '金额(万元)')
assert.deepStrictEqual(bigScreenOption.xAxis[0].data, ['三月'])

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

const gallerySource = await readFile(
  new URL('../src/views/vis/GalleryList.vue', import.meta.url),
  'utf8'
)
assert.strictEqual(
  (gallerySource
    .split('<script>')[0]
    .match(/isImageGalleryItem\(item\)/g) || []).length,
  2
)
const galleryContext = {
  component: null,
  ListMixin: {},
  GalleryListModal: {},
  getBusinessTypeList() {
    return Promise.resolve({ result: 'success', rows: [] })
  },
  getGalleryList() {
    return Promise.resolve({ result: 'success', rows: [], total: 0 })
  },
  resolveVisMediaUrl(value) {
    return value
  }
}
vm.runInNewContext(sfcScript(gallerySource), galleryContext)
const galleryComponent = galleryContext.component
for (const type of ['b', 't', 'bar', 'line', 'pie', 'barAndLine']) {
  assert.strictEqual(
    galleryComponent.methods.isImageGalleryItem({
      type,
      content: 'data:image/png;base64,preview'
    }),
    true,
    `${type} should render an image cover`
  )
}
for (const type of ['h', 'specialHtml', 'v', 'bigNumber']) {
  assert.strictEqual(
    galleryComponent.methods.isImageGalleryItem({ type, content: 'content' }),
    false,
    `${type} should preserve its non-image behavior`
  )
}
for (const type of ['', 'map', 'futureWidget']) {
  assert.strictEqual(
    galleryComponent.methods.isImageGalleryItem({ type, content: 'content' }),
    false,
    `${type || 'empty type'} must not be treated as an image cover`
  )
}
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
let indexInfoHandler = () => Promise.resolve(indexInfoResponse)
let barPreviewResponse = chartResponse
let barPreviewHandler = () => Promise.resolve(barPreviewResponse)
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
    return indexInfoHandler(params)
  },
  previewBarLine(params) {
    barPreviewRequests.push(params)
    return barPreviewHandler(params)
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
      convertForm: {
        validate() {
          return Promise.resolve(true)
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
  INDEX_NAME: '收入,增长率',
  ADD_DATE: '2026-07-01',
  realname: '管理员',
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
    TITLE: '财政收入趋势',
    DIMENSION_TYPE: 'g',
    TITLE_OLD: '历史标题',
    ADD_USER: 'user-1'
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

const firstIndexInfo = deferred()
const secondIndexInfo = deferred()
const indexInfoQueue = [firstIndexInfo, secondIndexInfo]
indexInfoHandler = () => indexInfoQueue.shift().promise
const concurrentIndexInfoVm = createModalVm()
const firstIndexInfoRequest = concurrentIndexInfoVm.instance.open(modalRecord)
const latestRecord = {
  ...modalRecord,
  SCHEME_COLUMS: 'I3',
  SCHEME_CONDITON: JSON.stringify({
    ...JSON.parse(modalRecord.SCHEME_CONDITON),
    SCHEME_COLUMNS: [{ CHART_ID: 'I3', CHART_DIRECTION: 'Columnar' }]
  })
}
const secondIndexInfoRequest = concurrentIndexInfoVm.instance.open(latestRecord)
secondIndexInfo.resolve({
  result: 'success',
  indexInfoList: [{ id: 'I3', name: '最新指标' }]
})
await settleWithin(secondIndexInfoRequest, 'latest index-info request did not settle')
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(concurrentIndexInfoVm.instance.indexOptions)),
  [{ id: 'I3', name: '最新指标' }]
)
assert.strictEqual(concurrentIndexInfoVm.instance.indexLoading, false)
firstIndexInfo.reject(new Error('stale index-info request failed'))
await settleWithin(firstIndexInfoRequest, 'stale index-info request did not settle')
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(concurrentIndexInfoVm.instance.indexOptions)),
  [{ id: 'I3', name: '最新指标' }]
)
assert.strictEqual(concurrentIndexInfoVm.instance.indexLoading, false)
assert.deepStrictEqual(concurrentIndexInfoVm.messages, [])

const closedIndexInfo = deferred()
indexInfoHandler = () => closedIndexInfo.promise
const closedIndexInfoVm = createModalVm()
const closedIndexInfoRequest = closedIndexInfoVm.instance.open(modalRecord)
closedIndexInfoVm.instance.handleCancel()
closedIndexInfo.resolve({
  result: 'success',
  indexInfoList: [{ id: 'stale-after-close', name: '关闭后旧指标' }]
})
await settleWithin(closedIndexInfoRequest, 'closed index-info request did not settle')
assert.strictEqual(closedIndexInfoVm.instance.indexOptions.length, 0)
assert.strictEqual(closedIndexInfoVm.instance.indexLoading, false)
assert.deepStrictEqual(closedIndexInfoVm.messages, [])
indexInfoHandler = () => Promise.resolve(indexInfoResponse)

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

const invalidPreviewVm = createModalVm()
await invalidPreviewVm.instance.open(modalRecord)
invalidPreviewVm.instance.$refs.convertForm.validate = () => Promise.resolve(false)
const barRequestsBeforeInvalidPreview = barPreviewRequests.length
const pieRequestsBeforeInvalidPreview = piePreviewRequests.length
assert.strictEqual(await invalidPreviewVm.instance.handlePreview(), false)
assert.strictEqual(barPreviewRequests.length, barRequestsBeforeInvalidPreview)
assert.strictEqual(piePreviewRequests.length, pieRequestsBeforeInvalidPreview)
assert.strictEqual(invalidPreviewVm.instance.previewReady, false)
assert.strictEqual(invalidPreviewVm.instance.previewState, 'idle')
assert.strictEqual(invalidPreviewVm.instance.previewLoading, false)

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
assert.strictEqual(previewVm.instance.frozenCondition.dimension_type, 'g')
assert.strictEqual(previewVm.instance.frozenCondition.title_old, '历史标题')
assert.strictEqual(previewVm.instance.frozenCondition.add_user, 'user-1')
assert.deepStrictEqual(
  JSON.parse(previewVm.instance.frozenCondition.option),
  JSON.parse(JSON.stringify(previewVm.instance.previewOption))
)

const firstPreview = deferred()
const secondPreview = deferred()
const previewQueue = [firstPreview, secondPreview]
barPreviewHandler = () => previewQueue.shift().promise
const concurrentVm = createModalVm()
await concurrentVm.instance.open(modalRecord)
const requestsBeforeConcurrentPreview = barPreviewRequests.length
const firstRequest = concurrentVm.instance.handlePreview()
await waitFor(
  () => barPreviewRequests.length === requestsBeforeConcurrentPreview + 1 &&
    previewQueue.length === 1,
  'first concurrent preview did not issue its API request'
)
const secondRequest = concurrentVm.instance.handlePreview()
await waitFor(
  () => barPreviewRequests.length === requestsBeforeConcurrentPreview + 2 &&
    previewQueue.length === 0,
  'second concurrent preview did not issue its API request'
)
secondPreview.resolve({
  ...chartResponse,
  x: ['最新响应']
})
await settleWithin(secondRequest, 'second concurrent preview did not settle')
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(concurrentVm.instance.previewResponse.x)),
  ['最新响应']
)
assert.strictEqual(concurrentVm.instance.previewLoading, false)
firstPreview.resolve({
  ...chartResponse,
  x: ['过期响应']
})
await settleWithin(firstRequest, 'first concurrent preview did not settle')
assert.deepStrictEqual(
  JSON.parse(JSON.stringify(concurrentVm.instance.previewResponse.x)),
  ['最新响应']
)
assert.strictEqual(concurrentVm.instance.previewLoading, false)
barPreviewHandler = () => Promise.resolve(barPreviewResponse)

const sparseCondition = JSON.parse(modalRecord.SCHEME_CONDITON)
delete sparseCondition.DIMENSION_TYPE
delete sparseCondition.TITLE_OLD
delete sparseCondition.ADD_USER
const sparseVm = createModalVm()
await sparseVm.instance.open({
  ...modalRecord,
  SCHEME_CONDITON: JSON.stringify(sparseCondition)
})
await sparseVm.instance.handlePreview()
for (const field of ['dimension_type', 'title_old', 'add_user']) {
  assert.strictEqual(
    Object.prototype.hasOwnProperty.call(sparseVm.instance.frozenCondition, field),
    false,
    `${field} must not be inferred`
  )
}

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
