export function normalizeVisRequestUrl(url) {
  if (typeof url !== 'string' || url === '' || url === 'null') {
    return url
  }
  if (url.startsWith('/vis/api/')) {
    return url
  }
  const stripped = url.replace(/^\/+/, '')
  if (/^(queryData|queryTableData|queryMapData|IndexBarLine|IndexPie)\//.test(stripped)) {
    return '/vis/api/' + stripped
  }
  return url
}

export function buildVisQueryPayload(item) {
  const payload = { id: item.id }
  if (/^\/vis\/api\/(IndexBarLine|IndexPie)\//.test(normalizeVisRequestUrl(item.query_path))) {
    payload.gallery_id = item.gallery_id
    payload.sub_id = item.id
  }
  return payload
}

export function normalizeVisTableRow(row) {
  if (!row || typeof row !== 'object') {
    return row
  }
  const normalized = { ...row }
  const aliasMap = {
    area_dscr: 'AREA_DSCR',
    index_value: 'INDEX_VALUE',
    growth_index_value: 'GROWTH_INDEX_VALUE',
    index_value1: 'INDEX_VALUE1',
    growth_index_value1: 'GROWTH_INDEX_VALUE1',
    rownum: 'rownum'
  }
  Object.keys(aliasMap).forEach((lowerKey) => {
    const upperKey = aliasMap[lowerKey]
    if (normalized[upperKey] == null && normalized[lowerKey] != null) {
      normalized[upperKey] = normalized[lowerKey]
    }
  })
  return normalized
}

export function normalizeVisTableRows(rows) {
  if (!Array.isArray(rows)) {
    return []
  }
  return rows.map(normalizeVisTableRow)
}
