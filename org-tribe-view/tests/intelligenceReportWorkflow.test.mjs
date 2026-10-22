import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { createRequire } from 'node:module'
import { test } from 'node:test'

const require = createRequire(new URL('../package.json', import.meta.url))
const Vue = require('vue')
const flush = () => new Promise(resolve => setImmediate(resolve))
const user = { id: 'qa-user', guokuId: 'qa-guoku' }

function options(name, bindings = {}) {
  const script = readFileSync(new URL(`../src/views/intelligenceReport/modules/${name}.vue`, import.meta.url), 'utf8')
    .split('<script>')[1].split('</script>')[0]
    .replace(/import[\s\S]*?from\s*['"][^'"]+['"]\s*;?/g, '')
    .replace('export default', 'return')
  const imports = {
    quarterDate: {}, echarts: {}, CQJSON: {}, CQPartJSON: {},
    getGuokuTree: async () => ({ result: 'success', rows: [] }),
    areaReport: async () => ({ result: 'success', rows: [] }),
    addEntityReport: async () => ({ result: 'success', REPORT_ID: 'report-new' }),
    editEntityReport: async () => ({ result: 'success' }),
    addMonthlyReport: async () => ({ result: 'success' }),
    addNewsFlashReport: async () => ({ result: 'success' }),
    addQuarterReport: async () => ({ result: 'success' }),
    addQuarterQuickReport: async () => ({ result: 'success' }),
    ...bindings,
  }
  return new Function(...Object.keys(imports), script)(...Object.values(imports))
}

function mountCreate(bindings = {}) {
  const messages = []
  const fields = {
    REPORT_NAME: '报告', REPORT_TYPE_ID: { key: '1', label: '月报模板' },
    AMT_UNIT: '10000', ACCOUNT_PERIOD: { format: () => '2026-06' },
  }
  const form = { resetFields() {}, validateFields(callback) { callback(null, { ...fields }) } }
  const vm = new Vue({
    ...options('editReport', bindings),
    beforeCreate() {
      this.$form = { createForm: () => form }
      this.$sessionStorage = { ls: { get: () => user } }
      this.$message = Object.fromEntries(['error', 'warning', 'success'].map(key => [key, value => messages.push(value)]))
    },
  })
  vm.model = { DIM_VALUE: { value: '500000', label: '重庆市' } }
  vm.visibleModal = true
  return { vm, fields, messages }
}

test('generation stays open and loading until the content generation request completes', async () => {
  let resolve
  const pending = new Promise(done => { resolve = done })
  const { vm } = mountCreate({ addMonthlyReport: () => pending })
  vm.handleOk()
  await flush()
  assert.equal(vm.visibleModal, true)
  assert.equal(vm.confirmLoading, true)
  resolve({ result: 'success' })
  await flush()
  assert.equal(vm.visibleModal, false)
  assert.equal(vm.confirmLoading, false)
})

test('a generation failure preserves inputs and retries the existing report without creating a duplicate', async () => {
  let creates = 0
  let generates = 0
  const { vm, messages } = mountCreate({
    addEntityReport: async () => { creates++; return { result: 'success', REPORT_ID: 'report-new' } },
    addMonthlyReport: async () => ++generates === 1 ? { result: 'fail', rows: 'SQL failed' } : { result: 'success' },
  })
  vm.handleOk()
  await flush()
  assert.equal(vm.visibleModal, true)
  assert.equal(vm.model.DIM_VALUE.value, '500000')
  assert.equal(vm.confirmLoading, false)
  assert.ok(messages.some(message => message.includes('SQL failed')))
  vm.handleOk()
  await flush()
  assert.equal(creates, 1)
  assert.equal(generates, 2)
  assert.equal(vm.visibleModal, false)
})

test('a rejected create request leaves a usable form and reports the error', async () => {
  const { vm, messages } = mountCreate({ addEntityReport: async () => { throw new Error('network unavailable') } })
  vm.handleOk()
  await flush()
  assert.equal(vm.visibleModal, true)
  assert.equal(vm.confirmLoading, false)
  assert.ok(messages.some(message => message.includes('network unavailable')))
})

test('the real Ant Modal cancel/change event sequence cannot hide a running generation', () => {
  const Modal = require('ant-design-vue/lib/modal/Modal').default
  const opening = readFileSync(new URL('../src/views/intelligenceReport/modules/editReport.vue', import.meta.url), 'utf8')
    .match(/<a-modal[\s\S]*?>/)[0]
  const compiled = require('vue-template-compiler').compileToFunctions(opening + '</a-modal>')
  const { vm } = mountCreate()
  vm.$options.components = { ...vm.$options.components, AModal: Modal }
  vm.$options.render = compiled.render
  vm.$options.staticRenderFns = compiled.staticRenderFns
  vm.confirmLoading = true
  const vnode = vm._render()
  Modal.methods.handleCancel.call({
    $emit(name, value) {
      const listener = vnode.componentOptions.listeners[name]
      if (Array.isArray(listener)) listener.forEach(callback => callback(value))
      else if (listener) listener(value)
    },
  })
  assert.equal(vm.visibleModal, true)
  vm.confirmLoading = false
  vm.handleClose()
  assert.equal(vm.visibleModal, false)
})

for (const name of ['detailsReport', 'monthQuickReport', 'quarterlyReport', 'quarterlyQuickReport']) {
  test(`${name} saves the current edited document instead of its stored snapshot`, async () => {
    let saved
    const component = options(name, { editEntityReport: async params => { saved = params; return { result: 'success' } } })
    globalThis.document = { getElementById: () => ({ innerHTML: '<p>edited</p>' }) }
    await component.methods.handleOk.call({
      model: { REPORT_ID: 'report-1', HTML_REPORT: '<p>old</p>' },
      $refs: { reportContent: { innerHTML: '<p>edited</p>' } },
      $message: { success() {}, error() {} }, handleClose() {}, $emit() {},
    })
    assert.equal(saved.HTML_REPORT, '<p>edited</p>')
  })

  test(`${name} reports a failed document save and keeps editing open`, async () => {
    const messages = []
    let closed = false
    const component = options(name, { editEntityReport: async () => ({ result: 'fail', msg: 'save failed' }) })
    globalThis.document = { getElementById: () => ({ innerHTML: '<p>edited</p>' }) }
    await component.methods.handleOk.call({
      model: { REPORT_ID: 'report-1', HTML_REPORT: '<p>old</p>' },
      $refs: { reportContent: { innerHTML: '<p>edited</p>' } },
      $message: { success() {}, error: message => messages.push(message) },
      handleClose() { closed = true }, $emit() {},
    })
    assert.equal(closed, false)
    assert.ok(messages.some(message => message.includes('save failed')))
  })

  test(`${name} does not download when saving the current report fails`, async () => {
    const opened = []
    const messages = []
    globalThis.window = { _CONFIG: { domianURL: 'https://example.test' }, open: url => opened.push(url) }
    globalThis.document = { getElementById: () => ({ innerHTML: '<p>report</p>' }) }
    globalThis.localStorage = { 'pro__Access-Token': JSON.stringify({ value: 'qa-token' }) }
    Object.defineProperty(globalThis, 'navigator', { value: { platform: 'Linux', userAgent: 'qa' }, configurable: true })
    const component = options(name, { editEntityReport: async () => ({ result: 'fail', msg: 'save failed' }) })
    const context = {
      model: { REPORT_ID: 'report-1', HTML_REPORT: '' }, state: '1',
      chart1: 'abcdefghi', chart2: 'abcdefghi', chart3: 'abcdefghi', chart4: 'abcdefghi',
      $refs: { reportContent: { innerHTML: '<p>report</p>' } },
      $sessionStorage: { ls: { get: () => user } },
      $message: { error: message => messages.push(message) },
    }
    if (component.methods.splitChunks) context.splitChunks = component.methods.splitChunks
    await component.methods.handleExport.call(context)
    assert.equal(opened.length, 0)
    assert.ok(messages.some(message => message.includes('save failed')))
  })

  test(`${name} downloads a saved report with the endpoint's required query parameters`, async () => {
    const opened = []
    globalThis.window = { _CONFIG: { domianURL: 'https://example.test' }, open: url => opened.push(url) }
    globalThis.localStorage = { 'pro__Access-Token': JSON.stringify({ value: 'qa-token' }) }
    Object.defineProperty(globalThis, 'navigator', { value: { platform: 'Linux', userAgent: 'qa' }, configurable: true })
    const component = options(name, { editEntityReport: async () => { throw new Error('saved report must not be rewritten') } })
    await component.methods.handleExport.call({
      model: { REPORT_ID: 'report-1', HTML_REPORT: '<p>saved</p>' }, state: '1',
      $sessionStorage: { ls: { get: () => user } },
    })
    assert.equal(opened.length, 1)
    const url = new URL(opened[0])
    const route = { detailsReport: 'monthReport', monthQuickReport: 'newsFlash', quarterlyReport: 'qurReport', quarterlyQuickReport: 'newsFlashQuarter' }[name]
    assert.equal(url.pathname, `/fixedReport/${route}/downLoadCheckList`)
    for (const [key, value] of Object.entries({ REPORT_ID: 'report-1', ADD_USERID: 'qa-user', 'X-Access-Token': 'qa-token', state: '1' })) {
      assert.equal(url.searchParams.get(key), value)
    }
    assert.equal(url.searchParams.get('isCover'), name.includes('Quick') ? '1' : '0')
  })
}

test('quarterly quick report explains missing chart images before trying to save or download', async () => {
  let saved = false
  const messages = []
  globalThis.window = { _CONFIG: { domianURL: 'https://example.test' }, open: () => assert.fail('download started') }
  const component = options('quarterlyQuickReport', { editEntityReport: async () => { saved = true } })
  await component.methods.handleExport.call({
    model: { REPORT_ID: 'report-1', HTML_REPORT: '' }, state: '1',
    chart1: '', chart2: '', chart3: '', chart4: '',
    splitChunks: component.methods.splitChunks,
    $sessionStorage: { ls: { get: () => user } },
    $message: { error: message => messages.push(message) },
  })
  assert.equal(saved, false)
  assert.ok(messages.some(message => message.includes('图表')))
})
