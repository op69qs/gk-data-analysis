import assert from 'node:assert/strict'
import * as schemaSupport from '../src/views/statistics/modules/dataSourceSchemaSupport.mjs'

const databaseOptions = [
  {id: 'database-edw', name: 'gk_data_analysis', SCHEMA_NAME: ' edw '},
  {id: 'database-ods', name: 'gk_data_analysis', SCHEMA_NAME: 'ods'}
]

assert.equal(
  typeof schemaSupport.schemaForDatabaseSelection,
  'function',
  'data table maintenance must resolve the configured Schema from the selected database ID'
)
assert.equal(
  schemaSupport.schemaForDatabaseSelection('Vastbase', databaseOptions, {key: 'database-edw'}),
  'edw'
)
assert.equal(
  schemaSupport.schemaForDatabaseSelection('Vastbase', databaseOptions, {value: 'database-ods'}),
  'ods'
)
assert.equal(
  schemaSupport.schemaForDatabaseSelection('Mysql', databaseOptions, {key: 'database-edw'}),
  '',
  'non-Vastbase data table maintenance must not expose a Schema value'
)

assert.equal(
  typeof schemaSupport.databaseOptionLabel,
  'function',
  'Vastbase database options must distinguish Schemas before selection'
)
assert.equal(
  schemaSupport.databaseOptionLabel('Vastbase', databaseOptions[0]),
  'gk_data_analysis（Schema：edw）'
)
assert.equal(
  schemaSupport.databaseOptionLabel('Mysql', databaseOptions[0]),
  'gk_data_analysis',
  'legacy database labels must remain unchanged'
)

console.log('Vastbase data table frontend behavior passed')
