export const CHART_TYPES = Object.freeze([
  { value: '1', type: 'bar', label: '柱状图' },
  { value: '2', type: 'line', label: '折线图' },
  { value: '3', type: 'pie', label: '饼图' },
  { value: '4', type: 'barAndLine', label: '柱状折线图' }
])

const CHART_TYPE_NAMES = CHART_TYPES.map(item => item.type)

const CONDITION_FIELDS = [
  ['scheme_id', ['scheme_id', 'schemeId', 'SCHEME_ID']],
  ['schemecolumns', [
    'schemecolumns',
    'schemeColumns',
    'scheme_columns',
    'SCHEME_COLUMNS',
    'SCHEMECOLUMNS'
  ]],
  ['dimensionFlag', ['dimensionFlag', 'dimension_flag', 'DIMENSION_FLAG']],
  ['periodFlag', ['periodFlag', 'period_flag', 'PERIOD_FLAG']],
  ['timeType', ['timeType', 'TIME_TYPE']],
  ['startDate', ['startDate', 'start_date', 'START_DATE']],
  ['endDate', ['endDate', 'end_date', 'END_DATE']],
  ['price', ['price', 'PRICE']],
  ['unit', ['unit', 'UNIT']],
  ['isRate', ['isRate', 'is_rate', 'IS_RATE']],
  ['direction', ['direction', 'DIRECTION']],
  ['GK', ['GK', 'gk']],
  ['indexName', ['indexName', 'index_name', 'INDEX_NAME']],
  ['xTurn', ['xTurn', 'x_turn', 'X_TURN']],
  ['type', ['type', 'TYPE']],
  ['title', ['title', 'TITLE']],
  ['sort', ['sort', 'SORT']],
  ['option', ['option', 'OPTION']],
  ['query_path', ['query_path', 'queryPath', 'QUERY_PATH']],
  ['time_type', ['time_type']],
  ['dimension_type', ['dimension_type', 'dimensionType', 'DIMENSION_TYPE']],
  ['dacct_radio', ['dacct_radio', 'dacctRadio', 'DACCT_RADIO']],
  ['title_old', ['title_old', 'titleOld', 'TITLE_OLD']],
  ['add_user', ['add_user', 'addUser', 'ADD_USER']]
]

function hasOwn(object, key) {
  return Object.prototype.hasOwnProperty.call(object, key)
}

function firstOwnValue(object, aliases) {
  for (const alias of aliases) {
    if (hasOwn(object, alias)) {
      return object[alias]
    }
  }
  return undefined
}

function cloneSchemeColumns(value) {
  if (!Array.isArray(value)) {
    return value
  }
  return value.map(item => {
    if (!item || typeof item !== 'object' || Array.isArray(item)) {
      return item
    }
    const chartId = firstOwnValue(item, ['chartId', 'chartid', 'CHART_ID'])
    const chartDirection = firstOwnValue(
      item,
      ['chartDirection', 'chartdirection', 'CHART_DIRECTION']
    )
    const normalized = {}
    if (chartId !== undefined) normalized.chartId = chartId
    if (chartDirection !== undefined) normalized.chartDirection = chartDirection
    return normalized
  })
}

function normalizeConditionObject(source) {
  const normalized = {}
  for (const [field, aliases] of CONDITION_FIELDS) {
    const value = firstOwnValue(source, aliases)
    if (value !== undefined) {
      normalized[field] = field === 'schemecolumns'
        ? cloneSchemeColumns(value)
        : value
    }
  }
  return normalized
}

function conditionValue(record) {
  if (!record || typeof record !== 'object') return null
  return firstOwnValue(record, [
    'SCHEME_CONDITON',
    'SCHEME_CONDITION',
    'schemeConditon',
    'schemeCondition',
    'condition'
  ])
}

function copyDefined(target, source) {
  for (const key of Object.keys(source)) {
    if (source[key] !== undefined) {
      target[key] = key === 'schemecolumns'
        ? cloneSchemeColumns(source[key])
        : source[key]
    }
  }
  return target
}

export function normalizeSchemeRow(row) {
  const source = row && typeof row === 'object' ? row : {}
  return {
    id: firstOwnValue(source, ['ID', 'id']),
    name: firstOwnValue(source, ['SCHEME_DESCR', 'schemeDescr', 'name']),
    createTime: firstOwnValue(source, ['ADD_DATE', 'addDate', 'createTime']),
    username: firstOwnValue(source, ['realname', 'REALNAME', 'username']),
    raw: row
  }
}

export function parseSchemeCondition(rawCondition) {
  if (rawCondition == null || rawCondition === '') return {}

  let parsed = rawCondition
  if (typeof rawCondition === 'string') {
    if (rawCondition.trim() === '') return {}
    try {
      parsed = JSON.parse(rawCondition)
    } catch (error) {
      throw new Error('方案条件格式错误')
    }
  }

  if (!parsed || typeof parsed !== 'object' || Array.isArray(parsed)) {
    throw new Error('方案条件格式错误')
  }
  return normalizeConditionObject(parsed)
}

export function createInitialForm(record, condition) {
  const sourceRecord = record && typeof record === 'object' ? record : {}
  const normalized = parseSchemeCondition(
    condition === undefined ? conditionValue(sourceRecord) : condition
  )
  const form = copyDefined({}, normalized)

  if (form.scheme_id === undefined) {
    form.scheme_id = firstOwnValue(sourceRecord, ['ID', 'id'])
  }
  form.schemeName = firstOwnValue(
    sourceRecord,
    ['SCHEME_DESCR', 'schemeDescr', 'name']
  )
  return form
}

export function buildPreviewPayload(form, record) {
  const sourceForm = form && typeof form === 'object' ? form : {}
  const sourceRecord = record && typeof record === 'object' ? record : {}
  const payload = copyDefined({}, parseSchemeCondition(conditionValue(sourceRecord)))
  copyDefined(payload, normalizeConditionObject(sourceForm))

  if (payload.scheme_id === undefined) {
    payload.scheme_id = firstOwnValue(sourceRecord, ['ID', 'id'])
  }
  if (CHART_TYPE_NAMES.indexOf(payload.type) === -1) {
    throw new Error('不支持的图表类型')
  }
  return payload
}

export function buildSavePayload(
  form,
  record,
  previewReady,
  frozenCondition
) {
  if (previewReady !== true) {
    throw new Error('请先预览当前图表配置')
  }

  const sourceForm = form && typeof form === 'object' ? form : {}
  const sourceRecord = record && typeof record === 'object' ? record : {}
  const previewCondition = frozenCondition ||
    sourceForm.previewCondition ||
    sourceRecord.previewCondition ||
    buildPreviewPayload(sourceForm, sourceRecord)

  return {
    condition: JSON.stringify(copyDefined({}, parseSchemeCondition(previewCondition))),
    content: sourceForm.content
  }
}

function hasRenderableValue(series) {
  return Array.isArray(series) && series.some(value =>
    value !== null && value !== undefined && value !== ''
  )
}

export function hasChartData(response) {
  if (!response || response.result !== 'success' || !Array.isArray(response.data)) {
    return false
  }
  if (response.type === 'pie') {
    return response.data.length > 0
  }
  return Array.isArray(response.x) &&
    response.x.length > 0 &&
    response.data.some(hasRenderableValue)
}
