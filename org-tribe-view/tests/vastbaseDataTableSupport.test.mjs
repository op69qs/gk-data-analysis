import assertModule from 'assert'
import * as schemaSupport from '../src/views/statistics/modules/dataSourceSchemaSupport.mjs'

const assert = assertModule.strict

const databaseOptions = [
  {id: 'database-edw', name: 'gk_data_analysis', SCHEMA_NAME: ' edw '},
  {id: 'database-ods', name: 'gk_data_analysis', SCHEMA_NAME: 'ods'}
]

assert.equal(
  typeof schemaSupport.nextSelectionRequestToken,
  'function',
  'data table maintenance must invalidate older table and field requests when the selection changes'
)
assert.equal(schemaSupport.nextSelectionRequestToken(0), 1)
assert.equal(schemaSupport.nextSelectionRequestToken(3), 4)
assert.equal(
  schemaSupport.isCurrentSelectionRequest(4, 3),
  false,
  'an older Schema request must not overwrite the current selection'
)
assert.equal(schemaSupport.isCurrentSelectionRequest(4, 4), true)

assert.equal(
  typeof schemaSupport.dataSourceType,
  'function',
  'data table maintenance must update the source type before database options finish loading'
)
assert.equal(schemaSupport.dataSourceType({label: '国库数据源(Vastbase)'}), 'Vastbase')
assert.equal(schemaSupport.dataSourceType('业务数据源(Mysql)'), 'Mysql')
assert.equal(schemaSupport.dataSourceType('invalid label'), '')

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
