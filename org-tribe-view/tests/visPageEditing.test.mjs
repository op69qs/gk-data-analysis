import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { createRequire } from 'node:module'
import { test } from 'node:test'

const require = createRequire(new URL('../package.json', import.meta.url))
const Vue = require('vue')
const flush = () => new Promise(resolve => setImmediate(resolve))

// Exercise the real Vue component methods and reactivity without starting the server.
function component(file, bindings = {}) {
  const source = readFileSync(new URL(`../src/views/vis/${file}`, import.meta.url), 'utf8')
    .split('<script>')[1].split('</script>')[0]
    .replace(/^\s*import .* from .*$/gm, '').replace('export default', 'return')
  const imports = {
    require: name => name,
    pick: require('lodash.pick'), mapGetters: () => ({}),
    GridLayout: {}, GridItem: {}, AddTemplateDrawer: {}, Chrome: {},
    dataYear: {}, dataMonth: {}, moment: () => ({}),
    ListMixin: {}, visPreviewDebug: () => {},
    ...bindings,
  }
  return new Function(...Object.keys(imports), source)(...Object.values(imports))
}

function mount(file, bindings = {}, route = { params: {}, query: {} }) {
  const options = component(file, bindings)
  const messages = []
  options.created = options.mounted = options.destroyed = undefined
  options.mixins = []
  const vm = new Vue({
    ...options,
    beforeCreate() {
      this.$form = { createForm: () => ({ resetFields() {}, setFieldsValue() {} }) }
      Vue.util.defineReactive(this, '$route', route)
      this.$router = { push() {} }
      this.$message = Object.fromEntries(['warning', 'error', 'success'].map(key => [key, msg => messages.push(msg)]))
    },
  })
  vm.messages = messages
  return vm
}

test('module gallery supplies pagination and keeps search parameters independent', async () => {
  let request
  const vm = mount('modules/AddTemplateDrawer.vue', {
    getGalleryList: async data => { request = data; return { result: 'success', rows: [], total: 25 } },
  })
  vm.searchParam = { title: '收入' }
  await vm.getGalleryList()
  await flush()
  assert.equal(request.pageNo, 1)
  assert.equal(request.pageSize, 12)
  assert.equal(String(request.state), '0')
  assert.deepEqual(JSON.parse(JSON.stringify(vm.searchParam)), { title: '收入' })
  assert.equal(vm.galleryTotal, 25)
})

test('failed gallery request releases the spinner and reports the error', async () => {
  const vm = mount('modules/AddTemplateDrawer.vue', { getGalleryList: async () => { throw new Error('offline') } })
  await vm.getGalleryList()
  await flush()
  assert.equal(vm.confirmLoading, false)
  assert.ok(vm.messages.length)
})

test('T subject selection does not require ordinary subject selection', () => {
  let emitted
  const vm = mount('modules/AddTemplateDrawer.vue')
  vm.currentImg = 'chart-1'
  vm.imgSrcList = { id: 'chart-1', type: 'bar', title: '图表' }
  vm.itemSet = { dimension_type: 'ts' }
  vm.queryParam = { tsubject_code: '1001' }
  vm.$emit = (event, data) => { emitted = data }
  vm.handleOk()
  assert.ok(emitted, vm.messages.join(','))
  assert.deepEqual(JSON.parse(JSON.stringify(emitted.pageWhere)), [
    { where_type: 'ts', where_key: 'subject_code', where_value: '1001' },
  ])
})

test('empty date range and empty single date warn without throwing or emitting', () => {
  const vm = mount('modules/AddTemplateDrawer.vue')
  vm.currentImg = 'chart-1'
  vm.imgSrcList = { id: 'chart-1', type: 'bar' }
  vm.itemSet = { time_type: 'd' }
  vm.$emit = () => assert.fail('incomplete dates must not be submitted')
  for (const timeType of ['2', '4']) {
    vm.queryParam = { timeType, time_type: 'd' }
    vm.timeData = undefined
    vm.startTimeData = undefined
    assert.doesNotThrow(() => vm.handleOk())
  }
  assert.equal(vm.messages.length, 2)
})

test('HTML module edits retain all paragraphs', () => {
  const vm = mount('modules/AddTemplateDrawer.vue')
  vm.changeText({ target: { innerHTML: '<p>第一段</p><p>第二段</p>', firstChild: { innerHTML: '第一段' } } })
  assert.equal(vm.htmlContent, '<p>第一段</p><p>第二段</p>')
})

test('restoring a module outside the current gallery page loads its metadata', async () => {
  const vm = mount('modules/AddTemplateDrawer.vue', {
    getGalleryList: async () => ({ result: 'success', rows: [{ id: 'old', type: 'bar', time_type: 'y' }] }),
  })
  vm.defaultData = { gallery_id: 'old', time_type: 'y', time_interval: '3', unit: '1' }
  await flush()
  assert.equal(vm.imgSrcList.id, 'old')
  assert.equal(vm.queryParam.timeType, '3')
})

test('module selection updates newly added fields reactively', async () => {
  const vm = mount('PageEditorEntry.vue')
  vm.layout = [{ i: '1', content: '' }]
  vm.index = '1'
  vm.$refs.modalForm = { visible: true }
  let observed
  vm.$watch(() => vm.layout[0].gallery_id, value => { observed = value })
  vm.getTemplateData({ gallery_id: 'chart-1', content: 'chart.png' })
  await Vue.nextTick()
  assert.equal(observed, 'chart-1')
})

test('screenshot failure releases submit state and reports a retryable error', async () => {
  const vm = mount('PageEditorEntry.vue', { domtoimage: { toPng: async () => { throw new Error('image unavailable') } } })
  vm.$refs.imageWrapper = { $el: {} }
  vm.addLoading = true
  await vm.toImage({})
  await flush()
  assert.equal(vm.addLoading, false)
  assert.ok(vm.messages.length)
})

test('rejected edit releases submit state and keeps the editor open', async () => {
  const vm = mount('PageEditorEntry.vue', {
    domtoimage: { toPng: async () => 'data:image/png;base64,AA==' },
    editPageInfo: async () => ({ result: 'failed', msg: '保存失败' }),
  }, { params: { type: 'edit', itemData: { id: 'page-1' } }, query: {} })
  vm.$refs.imageWrapper = { $el: {} }
  vm.addLoading = true
  await vm.toImage({ name: '收入页' })
  await new Promise(resolve => setTimeout(resolve, 150))
  assert.equal(vm.addLoading, false)
  assert.ok(vm.messages.includes('保存失败'))
})

test('refreshing an editor URL reloads the page and preserves title settings', async () => {
  const vm = mount('PageEditorEntry.vue', {
    getPageList: async () => ({ result: 'success', rows: [{ id: 'page-1', name: '收入页', template: '6', title_background: '1', background_type: '1', colour: '#123456' }] }),
    getPageSubAll: async () => ({ result: 'success', rows: [] }),
  }, { params: {}, query: { id: 'page-1', type: 'edit' } })
  await vm.handleEdit()
  await flush()
  assert.equal(vm.titleValue, 1)
  assert.equal(vm.titleImgSrc, false)
  assert.equal(vm.titleName, '收入页')
  assert.equal(vm.editLoading, false)
})

test('clicking outside a destroyed color picker does not throw', () => {
  const vm = mount('PageEditorEntry.vue')
  globalThis.document = { getElementById: () => null }
  try { assert.doesNotThrow(() => vm.setColorPicker({ target: {} })) }
  finally { delete globalThis.document }
})

test('page editor links survive browser refresh', () => {
  const vm = mount('PageList.vue')
  vm.dataSource = [{ id: 'page-1' }]
  let location
  vm.$router.push = value => { location = value }
  vm.onTabChange('edit', 'page-1')
  assert.deepEqual(location.query, { id: 'page-1', type: 'edit' })
})

test('out-of-order page searches retain only the latest query results', async () => {
  const pending = []
  const vm = mount('PageList.vue', { getPageList: () => new Promise(resolve => pending.push(resolve)) })
  vm.ipagination = { current: 1, pageSize: 10, total: 0 }
  vm.getQueryParams = () => ({ pageNo: 1, pageSize: 10 })
  vm.loadData(1)
  vm.loadData(1)
  pending[1]({ result: 'success', rows: [{ id: 'new' }], total: 1 })
  await flush()
  pending[0]({ result: 'success', rows: [{ id: 'old' }], total: 1 })
  await flush()
  assert.equal(vm.dataSource[0].id, 'new')
})

test('opening add after edit clears the previous page id before saving', async () => {
  const saved = []
  const vm = mount('PageEditorEntry.vue', {
    domtoimage: { toPng: async () => 'data:image/png;base64,AA==' },
    addPageInfo: async value => { saved.push({ operation: 'add', ...value }); return { result: 'success' } },
    editPageInfo: async value => { saved.push({ operation: 'edit', ...value }); return { result: 'success' } },
  }, { name: 'VisPageEditorEntry', params: {}, query: { type: 'edit', id: 'previous-page' } })
  vm.pageId = 'previous-page'
  vm.$route = { name: 'VisPageEditorEntry', params: {}, query: { type: 'add' } }
  await Vue.nextTick()
  vm.$refs.imageWrapper = { $el: {} }
  await vm.toImage({ name: '新页面' })
  assert.equal(saved[0].operation, 'add')
  assert.equal(saved[0].id, undefined)
})
