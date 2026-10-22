import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { createRequire } from 'node:module'
import { test } from 'node:test'

const require = createRequire(new URL('../package.json', import.meta.url))
const Vue = require('vue')
const flush = () => new Promise(resolve => setImmediate(resolve))
const deferred = () => {
  let resolve
  const promise = new Promise(done => { resolve = done })
  return { promise, resolve }
}

function mount(file, bindings = {}) {
  const script = readFileSync(new URL(`../src/views/vis/${file}`, import.meta.url), 'utf8')
    .split('<script>')[1].split('</script>')[0]
    .replace(/^\s*import .* from .*$/gm, '').replace('export default', 'return')
  const listeners = []
  const imports = {
    pick: require('lodash.pick'), ListMixin: {}, GalleryListModal: {}, BigScreenTabTemplate: {},
    resolveVisMediaUrl: value => value, resolveVisCarouselInterval: () => 5000, visPreviewDebug() {},
    getBusinessTypeList: async () => ({ result: 'success', rows: [] }),
    getGalleryList: async () => ({ result: 'success', rows: [], total: 0 }),
    document: { body: { offsetHeight: 900, scrollTop: 0, clientHeight: 900, scrollHeight: 900 },
      documentElement: { scrollTop: 0, clientHeight: 900, scrollHeight: 900 } },
    window: { innerHeight: 900, addEventListener: (...args) => listeners.push(args), removeEventListener() {} },
    ...bindings,
  }
  const options = new Function(...Object.keys(imports), script)(...Object.values(imports))
  const hooks = { created: options.created, mounted: options.mounted, beforeDestroy: options.beforeDestroy }
  options.created = options.mounted = options.beforeCreate = options.beforeDestroy = undefined
  options.mixins = []
  const fields = {}
  const messages = []
  const vm = new Vue({
    ...options,
    beforeCreate() {
      this.$form = { createForm: () => ({ resetFields() { for (const key of Object.keys(fields)) delete fields[key] },
        setFieldsValue(values) { Object.assign(fields, values) }, validateFields(callback) { return callback(null, { ...fields }) } }) }
      this.$message = Object.fromEntries(['error', 'warning', 'success'].map(key => [key, message => messages.push(message)]))
      Vue.util.defineReactive(this, '$route', { query: { info: 'scheme-1' }, fullPath: '/vis/preview?info=scheme-1' })
    },
  })
  if (file === 'GalleryList.vue') {
    vm.dataSource = []
    vm.queryParam = {}
    vm.ipagination = { current: 1, pageSize: 10, total: 30 }
    vm.getQueryParams = () => ({ ...vm.queryParam, pageNo: vm.ipagination.current, pageSize: vm.ipagination.pageSize })
  }
  return { vm, fields, messages, hooks, listeners, environment: imports.window }
}

test('picture gallery entries render an image cover', () => {
  const { vm } = mount('GalleryList.vue')
  assert.equal(vm.isImageGalleryItem({ type: 'p', content: 'picture.png' }), true)
})

test('changing gallery category ignores the previous category response', async () => {
  const old = deferred()
  const { vm } = mount('GalleryList.vue', {
    getGalleryList: data => data.business_id === 'new' ? Promise.resolve({ result: 'success', rows: [{ id: 'new' }], total: 1 }) : old.promise,
  })
  const first = vm.loadData(1)
  vm.callback('new')
  await flush()
  old.resolve({ result: 'success', rows: [{ id: 'old' }], total: 1 })
  await first
  await flush()
  assert.deepEqual(vm.dataSource.map(row => row.id), ['new'])
})

test('gallery pagination retries the failed page without skipping it', async () => {
  const requests = []
  let fail = true
  const { vm } = mount('GalleryList.vue', { getGalleryList: async data => {
    requests.push(data.pageNo)
    if (data.pageNo === 2 && fail) return { result: 'failed' }
    return { result: 'success', rows: [{ id: String(data.pageNo) }], total: 3 }
  } })
  await vm.loadData(1)
  await vm.loadData(2)
  assert.equal(vm.ipagination.current, 1)
  assert.ok(vm.loadError)
  fail = false
  await vm.loadData(2)
  assert.deepEqual(requests, [1, 2, 2])
  assert.deepEqual(vm.dataSource.map(row => row.id), ['1', '2'])
  assert.equal(vm.ipagination.current, 2)
})

test('rapid bottom scrolls issue only one next-page request', async () => {
  const pending = deferred()
  const requests = []
  const { vm, hooks, listeners, environment } = mount('GalleryList.vue', {
    getGalleryList: data => { requests.push(data.pageNo); return pending.promise },
    setTimeout: callback => { callback(); return 0 },
  })
  vm.dataSource = [{ id: 'first' }]
  if (hooks.created) hooks.created.call(vm)
  if (hooks.mounted) hooks.mounted.call(vm)
  const scroll = environment.onscroll || listeners.find(([event]) => event === 'scroll')[1]
  scroll()
  scroll()
  assert.deepEqual(requests, [2])
  pending.resolve({ result: 'success', rows: [{ id: 'second' }], total: 3 })
  await flush()
})

test('gallery loads the next page when its layout container scrolls', async () => {
  const requests = []
  const { vm, hooks, listeners } = mount('GalleryList.vue', {
    document: { body: { scrollTop: 0, clientHeight: 900, scrollHeight: 2000 },
      documentElement: { scrollTop: 0, clientHeight: 900, scrollHeight: 2000 } },
    getGalleryList: async data => { requests.push(data.pageNo); return { result: 'success', rows: [{ id: 'second' }], total: 2 } },
  })
  vm.dataSource = [{ id: 'first' }]
  hooks.mounted.call(vm)
  const scroll = listeners.find(([event]) => event === 'scroll')
  scroll[1]({ target: { scrollTop: 500, clientHeight: 600, scrollHeight: 1100, contains: () => true } })
  await flush()
  assert.deepEqual(requests, [2])
  assert.equal(scroll[2], true)
})

test('gallery save failure keeps the edited title and dialog open', async () => {
  const { vm, fields } = mount('modules/GalleryListModal.vue', { editGallery: async () => ({ result: 'failed', msg: '保存失败' }) })
  vm.edit({ id: 'picture', type: 'p', title: '原图' })
  await Vue.nextTick()
  fields.title = '新的标题'
  await vm.handleOk()
  await flush()
  assert.equal(vm.visible, true)
  assert.equal(fields.title, '新的标题')
  assert.equal(vm.confirmLoading, false)
})

test('editing an image after text never overwrites the image with stale HTML', async () => {
  let saved
  const { vm } = mount('modules/GalleryListModal.vue', { editGallery: async data => { saved = data; return { result: 'success' } } })
  vm.edit({ id: 'text', type: 'h', title: '文本', content: '<p>旧文本</p>' })
  await Vue.nextTick()
  vm.close()
  vm.edit({ id: 'image', type: 'p', title: '图片', content: 'photo.png' })
  await Vue.nextTick()
  await vm.handleOk()
  await flush()
  assert.equal(saved.id, 'image')
  assert.equal(saved.content, undefined)
})

test('repeated gallery confirmation submits once while saving', async () => {
  const pending = deferred()
  let calls = 0
  const { vm } = mount('modules/GalleryListModal.vue', { editGallery: () => { calls++; return pending.promise } })
  vm.edit({ id: 'picture', type: 'p', title: '原图' })
  await Vue.nextTick()
  vm.handleOk()
  vm.handleOk()
  assert.equal(calls, 1)
  pending.resolve({ result: 'success' })
  await flush()
})

test('gallery async form validation cannot submit two pending confirmations', async () => {
  const pending = deferred()
  let calls = 0
  const { vm, fields } = mount('modules/GalleryListModal.vue', { editGallery: () => { calls++; return pending.promise } })
  vm.edit({ id: 'picture', type: 'p', title: '原图' })
  await Vue.nextTick()
  const callbacks = []
  vm.form.validateFields = callback => callbacks.push(callback)
  vm.handleOk()
  vm.handleOk()
  const saving = callbacks[0](null, { ...fields })
  callbacks[1](null, { ...fields })
  assert.equal(calls, 1)
  pending.resolve({ result: 'success' })
  await saving
})

test('old gallery validation cannot submit values against a newly opened image', async () => {
  const writes = []
  const { vm } = mount('modules/GalleryListModal.vue', { editGallery: async data => { writes.push(data); return { result: 'success' } } })
  vm.edit({ id: 'old', type: 'p', title: '旧图片' })
  await Vue.nextTick()
  const callbacks = []
  vm.form.validateFields = callback => callbacks.push(callback)
  vm.handleOk()
  vm.close()
  vm.edit({ id: 'new', type: 'p', title: '新图片' })
  await Vue.nextTick()
  await callbacks[0](null, { title: '旧图片修改' })
  assert.equal(writes.length, 0)
  assert.equal(vm.visible, true)
})

test('preview backend failure releases the loading screen and offers an error', async () => {
  const { vm } = mount('bigscreen/BigScreenPreview.vue', { getSchemeAllPage: async () => ({ result: 'failed' }) })
  await vm.getData()
  await flush()
  assert.equal(vm.isShow, false)
  assert.ok(vm.loadError)
})

test('preview without a scheme never queries all schemes', async () => {
  let requests = 0
  const { vm } = mount('bigscreen/BigScreenPreview.vue', { getSchemeAllPage: async () => { requests++; return { result: 'success', data: [] } } })
  vm.$route = { query: {}, fullPath: '/vis/preview' }
  await vm.getData()
  await flush()
  assert.equal(requests, 0)
  assert.equal(vm.isShow, false)
})

test('reloading preview cannot be overwritten by an older scheme response', async () => {
  const old = deferred()
  const { vm } = mount('bigscreen/BigScreenPreview.vue', { getSchemeAllPage: data => data.scheme_id === 'new' ?
    Promise.resolve({ result: 'success', data: [{ id: 'new', page_sub: [] }] }) : old.promise })
  const first = vm.getData()
  vm.$route = { query: { info: 'new' }, fullPath: '/vis/preview?info=new' }
  await vm.getData()
  old.resolve({ result: 'success', data: [{ id: 'old', page_sub: [] }] })
  await first
  await flush()
  assert.deepEqual(vm.levelData.map(page => page.id), ['new'])
})

test('an old preview child callback cannot complete the new scheme loading', async () => {
  const { vm } = mount('bigscreen/BigScreenPreview.vue', { getSchemeAllPage: async () => ({ result: 'success',
    data: [{ id: 'page-1', page_sub: [{ type: 'p', page_id: 'page-1', content: 'picture.png' }] }] }) })
  const source = readFileSync(new URL('../src/views/vis/bigscreen/BigScreenPreview.vue', import.meta.url), 'utf8')
  const callbackName = source.match(/:fatherMethod="(\w+)"/)[1]
  await vm.getData()
  const oldChildCallback = vm[callbackName]
  await vm.getData()
  oldChildCallback()
  await flush()
  assert.equal(vm.currentNum, 0)
  assert.equal(vm.isShow, true)
  vm[callbackName]()
  await flush()
  assert.equal(vm.currentNum, 1)
  clearTimeout(vm.progressTimer)
})
