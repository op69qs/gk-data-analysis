import assert from 'node:assert/strict'
import { readFile } from 'node:fs/promises'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const testsDir = dirname(fileURLToPath(import.meta.url))
const projectRoot = resolve(testsDir, '..')

const indexLibrary = await readFile(
  resolve(projectRoot, 'src/views/statistics/indexLibrary.vue'),
  'utf8'
)
const schemeIndex = await readFile(
  resolve(projectRoot, 'src/views/statistics/schemeIndex.vue'),
  'utf8'
)

assert.match(indexLibrary, /<div class="model-title">公共指标/)
assert.match(indexLibrary, /<div class="model-title" style="margin-top:20px;">\s*我的指标/)
assert.match(indexLibrary, /@click="getData">查询<\/a-button>/)
assert.match(indexLibrary, /<v-table/)
assert.match(schemeIndex, /path:\s*'\/statistics\/indexLibrary'/)
assert.doesNotMatch(indexLibrary, /\/vis\/index-library|vis\/IndexLibraryList/)
assert.doesNotMatch(schemeIndex, /\/vis\/index-library|vis\/IndexLibraryList/)
assert.match(
  indexLibrary,
  /let params = \{[\s\S]*?mainCondition: this\.obj,[\s\S]*?userId: userId,[\s\S]*?titles: newObj/,
  'indicator export must pass the current user id so backend data permission remains enforced'
)

const convergeSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026042615-vis-production-menu-converge.sql'),
  'utf8'
)
const forwardSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.sql'),
  'utf8'
)
const rollbackSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.rollback.sql'),
  'utf8'
)

function legacyMenuUpdate(sql) {
  const where = "WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'"
  const whereAt = sql.indexOf(where)
  assert.notStrictEqual(whereAt, -1, 'legacy indicator-query menu guard is missing')
  const updateMatches = [
    ...sql.slice(0, whereAt).matchAll(/UPDATE\s+(?:"jeecg-boot-os"\.)?sys_permission/g)
  ]
  const updateAt = updateMatches.at(-1)?.index ?? -1
  const endAt = sql.indexOf(';', whereAt)
  assert.notStrictEqual(updateAt, -1, 'legacy indicator-query UPDATE is missing')
  assert.notStrictEqual(endAt, -1, 'legacy indicator-query UPDATE is unterminated')
  return sql.slice(updateAt, endAt + 1)
}

for (const sql of [convergeSql, forwardSql]) {
  const block = legacyMenuUpdate(sql)
  assert.match(block, /SET url = '\/statistics\/indexLibrary'/)
  assert.match(block, /component = 'statistics\/indexLibrary'/)
  assert.match(block, /parent_id = 'aea6b487925d084dad182e09c95a6c79'/)
  assert.match(block, /name = '指标查询'/)
  assert.doesNotMatch(block, /\/vis\/|vis\//)
}

const rollbackBlock = legacyMenuUpdate(rollbackSql)
assert.match(rollbackBlock, /SET url = '\/statistics\/schemeIndex'/)
assert.match(rollbackBlock, /component = 'statistics\/schemeIndex'/)

console.log('legacy indicator query page contract passed')
