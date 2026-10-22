import assert from 'node:assert/strict'
import { readFileSync } from 'node:fs'
import { createRequire } from 'node:module'
import { test } from 'node:test'

const require = createRequire(new URL('../package.json', import.meta.url))
const Vue = require('vue')
const compiler = require('vue-template-compiler')
const source = readFileSync(new URL('../src/views/vis/bigscreen/modules/BigScreenTabTemplate.vue', import.meta.url), 'utf8')
const script = source.split('<script>')[1].split('</script>')[0]
  .replace(/^\s*import .* from .*$/gm, '').replace('export default', 'return')
const template = compiler.parseComponent(source).template.content
const compiled = compiler.compile(template)
assert.deepEqual(compiled.errors, [])
const requests = await import(`data:text/javascript;base64,${Buffer.from(readFileSync(new URL('../src/utils/visRequest.js', import.meta.url), 'utf8')).toString('base64')}`)
const flush = () => new Promise(resolve => setImmediate(resolve))

function createPreview(postAction) {
  const stub = name => ({ name, render(h) { return h('div', this.$slots.default) } })
  const bindings = { GridLayout: stub('GridLayout'), GridItem: stub('GridItem'), BScharts: stub('BScharts'), BSGeo: stub('BSGeo'), BSTable: stub('BSTable'),
    moment: require('moment'), postAction, visPreviewDebug() {}, window: { innerHeight: 900 },
    buildVisRequestUrl: requests.normalizeVisRequestUrl, buildVisQueryPayload: requests.buildVisQueryPayload }
  const options = new Function(...Object.keys(bindings), script)(...Object.values(bindings))
  let completed = 0
  const vm = new Vue({ ...options, mounted: undefined, propsData: { layoutData: { colour: '#000', name: '测试' }, fatherMethod: () => { completed++ } },
    render: new Function(compiled.render), staticRenderFns: compiled.staticRenderFns.map(code => new Function(code)) })
  const item = { id: 'sub', gallery_id: 'gallery', page_id: 'page', query_path: '/IndexBarLine/getIndexBarLineData', type: 'b', title: '收入', i: 1, x: 0, y: 0, w: 6, h: 4 }
  vm.layout = [item]
  const render = () => {
    const texts = [], components = []
    const visit = node => {
      if (!node) return
      if (node.text) texts.push(node.text)
      if (node.componentOptions) { components.push(node.componentOptions.Ctor.options.name); (node.componentOptions.children || []).forEach(visit) }
      ;(node.children || []).forEach(visit)
    }
    visit(vm._render())
    return { text: texts.join(' '), components }
  }
  return { vm, item, completed: () => completed, render }
}

test('failed chart response displays the backend reason and finishes progress once', async () => {
  const p = createPreview(async () => ({ result: 'failed', msg: '指标方案不存在或已删除，请重新选择图表' }))
  p.vm.getList(p.item)
  await flush()
  assert.match(p.render().text, /指标方案不存在或已删除/)
  assert.ok(!p.render().components.includes('BScharts'))
  assert.equal(p.completed(), 1)
})

test('network failure is visible instead of leaving a blank chart', async () => {
  const p = createPreview(async () => { throw new Error('network unavailable') })
  p.vm.getList(p.item)
  await flush()
  assert.match(p.render().text, /图表加载失败/)
  assert.equal(p.completed(), 1)
})

test('successful empty data is shown as empty data', async () => {
  const p = createPreview(async () => ({ result: 'success', nodata: true }))
  p.vm.getList(p.item)
  await flush()
  assert.match(p.render().text, /暂无数据/)
  assert.doesNotMatch(p.render().text, /加载失败/)
  assert.equal(p.completed(), 1)
})

test('a successful later load clears a previous failure message', async () => {
  let failed = true
  const p = createPreview(async () => failed ? { result: 'failed', msg: '查询失败' } : { result: 'success', nodata: true })
  p.vm.getList(p.item)
  await flush()
  assert.match(p.render().text, /加载失败/)
  failed = false
  p.vm.getList(p.item)
  await flush()
  assert.match(p.render().text, /暂无数据/)
  assert.doesNotMatch(p.render().text, /加载失败/)
  assert.equal(p.completed(), 2)
})
