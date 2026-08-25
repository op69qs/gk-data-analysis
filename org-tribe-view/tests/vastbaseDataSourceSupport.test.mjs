import assertModule from 'assert'
import {
  databaseKey,
  duplicateLookup,
  schemaPayload
} from '../src/views/statistics/modules/dataSourceSchemaSupport.mjs'

const assert = assertModule.strict

const vastbase = {DBNAME: 'gk_data_analysis', SCHEMA_NAME: 'edw'}

assert.equal(
  databaseKey('Vastbase', vastbase),
  'gk_data_analysis\u0000edw',
  'Vastbase uniqueness must use physical database and Schema together'
)
assert.notEqual(
  databaseKey('Vastbase', vastbase),
  databaseKey('Vastbase', {...vastbase, SCHEMA_NAME: 'ods'}),
  'two Schemas in one Vastbase database must be allowed'
)
assert.deepEqual(
  duplicateLookup('Vastbase', 'source-1', vastbase),
  {SOURCE_ID: 'source-1', DBNAME: 'gk_data_analysis', SCHEMA_NAME: 'edw'}
)
assert.deepEqual(
  duplicateLookup('Mysql', 'source-1', vastbase),
  {SOURCE_ID: 'source-1', DBNAME: 'gk_data_analysis'},
  'legacy duplicate checks must remain database-only'
)
assert.equal(schemaPayload('Vastbase', ' edw '), 'edw')
assert.equal(schemaPayload('Mysql', 'ignored'), '')

console.log('Vastbase datasource frontend behavior passed')
