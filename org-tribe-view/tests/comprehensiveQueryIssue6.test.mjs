import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'

const source = path => readFileSync(new URL(path, import.meta.url), 'utf8')

const integratedQuery = source('../src/views/statistics/integratedQuery.vue')
const dataSourceModal = source('../src/views/statistics/modules/DataSourceModal.vue')
const dataTable = source('../src/views/statistics/dataTable.vue')

assert.match(
  integratedQuery,
  /if \(this\.programInfo\) \{\s*delete this\.programInfo\.index;\s*\}/,
  'reset after the first query must not dereference a null programInfo'
)

assert.match(
  dataSourceModal,
  /initialValue:\s*status\[0\]\.id/,
  'new data sources must submit the status code, not its display label'
)
assert.doesNotMatch(
  dataSourceModal,
  /formData\.STATE\s*==\s*['"]启用['"]/,
  'status codes selected by the user must not be remapped as labels'
)

assert.match(dataTable, /this\.isJump\s*=\s*['"]0['"]/, 'back must restore jump state by assignment')
assert.match(dataTable, /this\.isJump\s*=\s*['"]1['"]/, 'back must restore non-jump state by assignment')
assert.match(
  dataTable,
  /if \(data\.nodeType !== ['"]table['"]\) \{\s*return;?\s*\}/,
  'only table nodes may request table details'
)
assert.match(
  dataTable,
  /if \(!res\.rows \|\| res\.rows\.length === 0\)/,
  'an empty table-detail response must not dereference rows[0]'
)

console.log('comprehensive query issue 6 frontend contracts passed')
