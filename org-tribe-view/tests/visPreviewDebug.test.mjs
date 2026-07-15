import assert from 'assert'
import { readFile } from 'fs/promises'

const source = await readFile(new URL('../src/utils/visPreviewDebug.js', import.meta.url), 'utf8')
const moduleUrl = `data:text/javascript;base64,${Buffer.from(source).toString('base64')}`
const { isVisPreviewConsoleDebugEnabled } = await import(moduleUrl)

const disabledStorage = { getItem: () => null }
const enabledStorage = { getItem: () => '1' }

assert.strictEqual(isVisPreviewConsoleDebugEnabled('?visDebug=1', disabledStorage), true)
assert.strictEqual(isVisPreviewConsoleDebugEnabled('?visDebug=0', enabledStorage), true)
assert.strictEqual(isVisPreviewConsoleDebugEnabled('', disabledStorage), false)
assert.strictEqual(isVisPreviewConsoleDebugEnabled('', { getItem: () => { throw new Error('blocked') } }), false)

console.log('visPreviewDebug tests passed')
