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
