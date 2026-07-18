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

console.log('indexLibraryScheme tests passed')
