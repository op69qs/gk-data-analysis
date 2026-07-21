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

console.log('legacy indicator query page contract passed')
