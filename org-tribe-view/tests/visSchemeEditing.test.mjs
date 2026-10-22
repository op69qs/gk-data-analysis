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

function mount(bindings = {}) {
  const script = readFileSync(new URL('../src/views/vis/modules/ExhibitionSchemeListModal.vue', import.meta.url), 'utf8')
    .split('<script>')[1].split('</script>')[0]
    .replace(/^\s*import .* from .*$/gm, '').replace('export default', 'return')
  const imports = {
    pick: require('lodash.pick'), Draggable: {}, mapGetters: () => ({ userInfo: () => ({ id: 'qa-user' }) }),
    getPageList: async () => ({ result: 'success', rows: [{ id: 'page-1', name: '页面一' }], total: 1 }),
    getSchemeRel: async () => ({ result: 'success', rows: [] }),
    addSchemeInfo: async () => ({ result: 'success', msg: '添加成功' }),
    editSchemeInfo: async () => ({ result: 'success', msg: '修改成功' }),
    ...bindings,
  }
  const options = new Function(...Object.keys(imports), script)(...Object.values(imports))
  options.mounted = undefined
  const fields = {}
  const messages = []
  const form = {
    resetFields() { for (const key of Object.keys(fields)) delete fields[key] },
    setFieldsValue(values) { Object.assign(fields, values) },
    getFieldValue(key) { return fields[key] },
    validateFields(callback) { return callback(null, { ...fields }) },
  }
  const vm = new Vue({
    ...options,
    beforeCreate() {
      this.$form = { createForm: () => form }
      this.$message = Object.fromEntries(['error', 'warning', 'success'].map(key => [key, value => messages.push(value)]))
    },
  })
  return { vm, form, fields, messages }
}

test('adding a scheme opens an usable form without a permanent loading mask', async () => {
  const { vm } = mount()
  await vm.add()
  await flush()
  assert.equal(vm.visible, true)
  assert.equal(vm.confirmLoading, false)
  assert.equal(vm.pageOptions.length, 1)
})

test('editing waits for every page before restoring the saved rotation order', async () => {
  const requests = []
  const { vm, fields } = mount({
    getPageList: async data => {
      requests.push(data.pageNo)
      return { result: 'success', rows: [{ id: `page-${data.pageNo}`, name: `页面${data.pageNo}` }], total: 2 }
    },
    getSchemeRel: async () => ({ result: 'success', rows: [{ page_id: 'page-2' }, { page_id: 'page-1' }] }),
  })
  await vm.edit({ id: 'scheme-1', name: '展示方案', rotation_interval: 7000 })
  await flush()
  assert.deepEqual(requests, [1, 2])
  assert.deepEqual(vm.page.map(page => page.id), ['page-2', 'page-1'])
  assert.deepEqual(fields.level, ['page-2', 'page-1'])
})

test('a failed page query releases loading and can be retried', async () => {
  let fail = true
  const { vm, messages } = mount({
    getPageList: async () => fail ? { result: 'failed' } : { result: 'success', rows: [{ id: 'page-1' }], total: 1 },
  })
  await vm.add()
  await flush()
  assert.equal(vm.confirmLoading, false)
  assert.ok(vm.loadError)
  assert.ok(messages.length)
  fail = false
  await vm.add()
  await flush()
  assert.equal(vm.loadError, '')
  assert.equal(vm.pageOptions.length, 1)
})

test('failed relationship lookup is not accepted as successful editing data', async () => {
  const response = { result: 'failed', rows: [] }
  const { vm } = mount({ getSchemeRel: async () => response })
  await vm.edit({ id: 'scheme-1', name: '展示方案' })
  await flush()
  assert.equal(response.result, 'failed')
  assert.ok(vm.loadError)
  assert.equal(vm.confirmLoading, false)
})

test('opening add after edit clears the old pages and rotation interval', async () => {
  const { vm } = mount({ getSchemeRel: async () => ({ result: 'success', rows: [{ page_id: 'page-1' }] }) })
  await vm.edit({ id: 'scheme-1', name: '旧方案', rotation_interval: 8000 })
  await vm.add()
  await flush()
  assert.equal(vm.model.id, undefined)
  assert.equal(vm.page.length, 0)
  assert.equal(Number(vm.validatorRules.rotation_interval.initialValue), 5000)
})

test('failed save keeps the entered scheme available for retry', async () => {
  const { vm, form, fields } = mount({ addSchemeInfo: async () => ({ result: 'failed', msg: '添加失败' }) })
  await vm.add()
  await flush()
  form.setFieldsValue({ name: '测试方案', level: ['page-1'] })
  vm.handleChange(['page-1'])
  await vm.handleOk()
  await flush()
  assert.equal(vm.visible, true)
  assert.equal(vm.confirmLoading, false)
  assert.equal(fields.name, '测试方案')
  assert.deepEqual(fields.level, ['page-1'])
})

test('closing and reopening ignores an older relationship response', async () => {
  const old = deferred()
  const { vm, fields } = mount({ getSchemeRel: () => old.promise })
  const editing = vm.edit({ id: 'scheme-1', name: '旧方案' })
  await flush()
  vm.close()
  await vm.add()
  old.resolve({ result: 'success', rows: [{ page_id: 'page-1' }] })
  await editing
  await flush()
  assert.equal(vm.model.id, undefined)
  assert.equal(vm.page.length, 0)
  assert.deepEqual(fields.level, [])
})

test('editing retains a deleted page relationship until it is explicitly removed', async () => {
  let saved
  const { vm, form } = mount({
    getPageList: async data => ({ result: 'success', rows: data.id ? [] : [{ id: 'page-1', name: '页面一' }], total: data.id ? 0 : 1 }),
    getSchemeRel: async () => ({ result: 'success', rows: [{ page_id: 'deleted', thumbnail: 'old-preview' }, { page_id: 'page-1' }] }),
    editSchemeInfo: async data => { saved = data; return { result: 'success' } },
  })
  await vm.edit({ id: 'scheme-1', name: '历史方案' })
  assert.deepEqual(vm.page.map(page => page.id), ['deleted', 'page-1'])
  assert.match(vm.page[0].name, /已删除/)
  assert.equal(vm.page[0].thumbnail, 'old-preview')
  assert.equal(vm.hasUnavailablePages, true)
  form.setFieldsValue({ name: '历史方案', level: ['deleted', 'page-1'] })
  await vm.handleOk()
  await flush()
  assert.deepEqual(saved.schemeRel.map(page => page.page_id), ['deleted', 'page-1'])
})

test('a related page outside the selectable list is restored by id', async () => {
  const { vm } = mount({
    getPageList: async data => ({ result: 'success', rows: data.id ? [{ id: 'unpublished', name: '旧页面' }] : [], total: data.id ? 1 : 0 }),
    getSchemeRel: async () => ({ result: 'success', rows: [{ page_id: 'unpublished' }] }),
  })
  await vm.edit({ id: 'scheme-1', name: '历史方案' })
  assert.equal(vm.page.length, 1)
  assert.equal(vm.page[0].name, '旧页面')
  assert.equal(vm.hasUnavailablePages, false)
})
