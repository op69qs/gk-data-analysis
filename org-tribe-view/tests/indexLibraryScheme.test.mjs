import assert from 'assert'
import { readFile } from 'fs/promises'

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

console.log('indexLibraryScheme tests passed')
