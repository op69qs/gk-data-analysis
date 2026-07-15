import assert from 'assert'
import { readFile } from 'fs/promises'

const sourceUrl = new URL('../src/utils/visCarousel.js', import.meta.url)
const source = await readFile(sourceUrl, 'utf8')
const moduleUrl = `data:text/javascript;base64,${Buffer.from(source).toString('base64')}`
const { resolveVisCarouselInterval } = await import(moduleUrl)

assert.strictEqual(resolveVisCarouselInterval(), 5000)
assert.strictEqual(resolveVisCarouselInterval(''), 5000)
assert.strictEqual(resolveVisCarouselInterval('invalid'), 5000)
assert.strictEqual(resolveVisCarouselInterval(0), 5000)

assert.strictEqual(resolveVisCarouselInterval(1), 5000)
assert.strictEqual(resolveVisCarouselInterval(5), 5000)
assert.strictEqual(resolveVisCarouselInterval(1000), 5000)
assert.strictEqual(resolveVisCarouselInterval(3000), 5000)

assert.strictEqual(resolveVisCarouselInterval(5000), 5000)
assert.strictEqual(resolveVisCarouselInterval('8000'), 8000)

const previewSource = await readFile(new URL('../src/views/vis/bigscreen/BigScreenPreview.vue', import.meta.url), 'utf8')
assert.match(previewSource, /document\.addEventListener\('keydown', this\.handleNavigationKeydown, false\)/)
assert.match(previewSource, /document\.addEventListener\('keyup', this\.handleNavigationKeyup, false\)/)
assert.match(previewSource, /shouldSuppressVisBrowserShortcut\(event\)/)
assert.match(previewSource, /if \(shouldSuppressVisBrowserShortcut\(event\) && event\.cancelable\) event\.preventDefault\(\)/)
assert.match(previewSource, /window\.addEventListener\('wheel', this\.handleNavigationWheel, \{ passive: false \}\)/)
assert.match(previewSource, /window\.addEventListener\('blur', this\.resetNavigationKeys, false\)/)
assert.match(previewSource, /document\.removeEventListener\('keydown', this\.handleNavigationKeydown, false\)/)
assert.match(previewSource, /document\.removeEventListener\('keyup', this\.handleNavigationKeyup, false\)/)
assert.match(previewSource, /window\.removeEventListener\('wheel', this\.handleNavigationWheel, false\)/)
assert.match(previewSource, /window\.removeEventListener\('blur', this\.resetNavigationKeys, false\)/)
assert.match(previewSource, /if \(decision\.consumed\) event\.preventDefault\(\)/)

console.log('visCarousel interval tests passed')
