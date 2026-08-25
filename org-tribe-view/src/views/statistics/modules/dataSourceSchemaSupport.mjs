export const isVastbaseType = type => type === 'Vastbase'

export const schemaPayload = (type, schemaName) => {
  if (!isVastbaseType(type)) {
    return ''
  }
  return (schemaName || '').trim()
}

export const databaseKey = (type, database) => {
  const schemaName = schemaPayload(type, database.SCHEMA_NAME)
  return `${database.DBNAME}\u0000${schemaName}`
}

export const duplicateLookup = (type, sourceId, database) => {
  const params = {
    SOURCE_ID: sourceId,
    DBNAME: database.DBNAME
  }
  if (isVastbaseType(type)) {
    params.SCHEMA_NAME = schemaPayload(type, database.SCHEMA_NAME)
  }
  return params
}

const selectionId = selection => {
  if (selection && typeof selection === 'object') {
    return selection.key || selection.value
  }
  return selection
}

export const schemaForDatabaseSelection = (type, databases, selection) => {
  if (!isVastbaseType(type)) {
    return ''
  }
  const databaseId = selectionId(selection)
  const database = (databases || []).find(item => String(item.id) === String(databaseId))
  return schemaPayload(type, database && database.SCHEMA_NAME)
}

export const databaseOptionLabel = (type, database) => {
  const schemaName = schemaPayload(type, database && database.SCHEMA_NAME)
  if (!schemaName) {
    return database.name
  }
  return `${database.name}（Schema：${schemaName}）`
}

export const databaseTreeLabel = (type, databaseName, schemaName) => {
  const schema = schemaPayload(type, schemaName)
  if (!schema) {
    return databaseName || ''
  }
  return `${databaseName}-${schema}`
}

export const dataSourceType = source => {
  const label = source && typeof source === 'object' ? source.label : source
  const match = /(.+)?(?:\(|（)(.+)(?=\)|）)/.exec(label || '')
  return match ? match[2] : ''
}

export const nextSelectionRequestToken = currentToken => (currentToken || 0) + 1

export const isCurrentSelectionRequest = (currentToken, requestToken) => currentToken === requestToken
