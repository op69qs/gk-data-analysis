import assert from 'node:assert/strict'
import { readFile } from 'node:fs/promises'
import { test } from 'node:test'

const source = await readFile(new URL('../src/utils/visRequest.js', import.meta.url), 'utf8')
const { normalizeVisRequestUrl, buildVisQueryPayload } = await import(`data:text/javascript;base64,${Buffer.from(source).toString('base64')}`)

test('legacy index chart endpoints are sent through the vis gateway', () => {
  assert.equal(normalizeVisRequestUrl('/IndexBarLine/getIndexBarLineData'), '/vis/api/IndexBarLine/getIndexBarLineData')
  assert.equal(normalizeVisRequestUrl('IndexPie/getIndexPieData'), '/vis/api/IndexPie/getIndexPieData')
})

test('existing query endpoints and prefixed endpoints retain their routes', () => {
  for (const controller of ['queryData', 'queryTableData', 'queryMapData']) {
    assert.equal(normalizeVisRequestUrl(`/${controller}/getData`), `/vis/api/${controller}/getData`)
  }
  const current = '/vis/api/IndexPie/getIndexPieData'
  assert.equal(normalizeVisRequestUrl(current), current)
})

test('unrelated and absolute endpoints are not rewritten', () => {
  for (const url of ['/fixedReport/report/download', 'https://charts.example/IndexPie/getIndexPieData', '', null]) {
    assert.equal(normalizeVisRequestUrl(url), url)
  }
})

test('index chart preview includes both the gallery and module condition identifiers', () => {
  for (const path of ['/IndexBarLine/getIndexBarLineData', '/vis/api/IndexPie/getIndexPieData']) {
    assert.deepEqual(buildVisQueryPayload({ id: 'sub-1', gallery_id: 'gallery-1', query_path: path }),
      { id: 'sub-1', gallery_id: 'gallery-1', sub_id: 'sub-1' })
  }
})

test('ordinary preview queries retain their module id contract', () => {
  assert.deepEqual(buildVisQueryPayload({ id: 'sub-1', gallery_id: 'gallery-1', query_path: '/queryData/getData' }), { id: 'sub-1' })
})
