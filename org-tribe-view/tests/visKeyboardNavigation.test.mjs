import assert from 'assert'
import { readFile } from 'fs/promises'

const source = await readFile(new URL('../src/utils/visKeyboardNavigation.js', import.meta.url), 'utf8')
const moduleUrl = `data:text/javascript;base64,${Buffer.from(source).toString('base64')}`
const { createVisKeyboardCoordinator, shouldSuppressVisBrowserShortcut } = await import(moduleUrl)

assert.strictEqual(shouldSuppressVisBrowserShortcut({ key: 'Alt', code: 'AltLeft' }), true)
assert.strictEqual(shouldSuppressVisBrowserShortcut({ keyCode: 37, altKey: true }), true)
assert.strictEqual(shouldSuppressVisBrowserShortcut({ keyCode: 39, altKey: true }), true)
assert.strictEqual(shouldSuppressVisBrowserShortcut({ keyCode: 37, altKey: false }), false)
assert.strictEqual(shouldSuppressVisBrowserShortcut({ keyCode: 38, altKey: false }), false)

const actions = []
const decisions = []
const keyboard = createVisKeyboardCoordinator(
  (code) => actions.push(code),
  (decision) => decisions.push(decision)
)

keyboard.handleKeydown({ keyCode: 39, repeat: false })
assert.deepStrictEqual(actions, [39], 'keydown must navigate immediately')
keyboard.handleKeyup({ keyCode: 39 })
assert.deepStrictEqual(actions, [39], 'matching keyup must not navigate twice')

keyboard.handleKeydown({ keyCode: 37, repeat: false })
keyboard.handleKeydown({ keyCode: 37, repeat: false })
assert.deepStrictEqual(actions, [39, 37, 37], 'separate non-repeat left keydowns must each navigate even if keyup is lost')
keyboard.handleKeyup({ keyCode: 37 })
assert.deepStrictEqual(actions, [39, 37, 37], 'release after multiple keydowns must not add another navigation')

keyboard.handleKeydown({ keyCode: 37, repeat: true })
assert.deepStrictEqual(actions, [39, 37, 37], 'repeat keydown must be ignored')

keyboard.handleKeyup({ keyCode: 37 })
assert.deepStrictEqual(actions, [39, 37, 37, 37], 'keyup-only terminals must navigate once')

keyboard.handleKeydown({ which: 39, repeat: false })
keyboard.reset()
keyboard.handleKeyup({ which: 39 })
assert.deepStrictEqual(actions, [39, 37, 37, 37, 39, 39], 'blur reset must allow a release-only fallback')

keyboard.handleKeydown({ keyCode: 65, repeat: false })
keyboard.handleKeyup({ keyCode: 65 })
assert.deepStrictEqual(actions, [39, 37, 37, 37, 39, 39], 'unrelated keys must be ignored')

assert.deepStrictEqual(
  decisions.map(({ eventType, code, handled, reason }) => ({ eventType, code, handled, reason })),
  [
    { eventType: 'keydown', code: 39, handled: true, reason: 'keydown' },
    { eventType: 'keyup', code: 39, handled: false, reason: 'release' },
    { eventType: 'keydown', code: 37, handled: true, reason: 'keydown' },
    { eventType: 'keydown', code: 37, handled: true, reason: 'keydown' },
    { eventType: 'keyup', code: 37, handled: false, reason: 'release' },
    { eventType: 'keydown', code: 37, handled: false, reason: 'repeat' },
    { eventType: 'keyup', code: 37, handled: true, reason: 'keyup-fallback' },
    { eventType: 'keydown', code: 39, handled: true, reason: 'keydown' },
    { eventType: 'reset', code: null, handled: false, reason: 'reset' },
    { eventType: 'keyup', code: 39, handled: true, reason: 'keyup-fallback' },
    { eventType: 'keydown', code: 65, handled: false, reason: 'unsupported' },
    { eventType: 'keyup', code: 65, handled: false, reason: 'unsupported' },
  ],
  'diagnostics must explain every accepted and ignored keyboard event'
)

const abnormalActions = []
const abnormalDecisions = []
const abnormal = createVisKeyboardCoordinator(
  (code) => abnormalActions.push(code),
  (decision) => abnormalDecisions.push(decision)
)

abnormal.handleKeydown({ keyCode: 40, key: 'ArrowDown', repeat: false })
abnormal.handleKeyup({ keyCode: 37, key: 'ArrowLeft' })
abnormal.handleKeyup({ keyCode: 40, key: 'ArrowDown' })
assert.deepStrictEqual(abnormalActions, [37], 'mismatched left gesture must navigate once')

abnormal.handleKeydown({ keyCode: 38, key: 'ArrowUp', repeat: false })
abnormal.handleKeyup({ keyCode: 39, key: 'ArrowRight' })
abnormal.handleKeyup({ keyCode: 38, key: 'ArrowUp' })
assert.deepStrictEqual(abnormalActions, [37, 39], 'mismatched right gesture must navigate once')

abnormal.handleKeydown({ keyCode: 40, key: 'ArrowDown', repeat: false })
abnormal.handleKeyup({ keyCode: 40, key: 'ArrowDown' })
abnormal.handleKeydown({ keyCode: 38, key: 'ArrowUp', repeat: false })
abnormal.handleKeyup({ keyCode: 38, key: 'ArrowUp' })
assert.deepStrictEqual(abnormalActions, [37, 39, 39, 37], 'terminal up/down translation must preserve physical left/right direction')

assert.deepStrictEqual(
  abnormalDecisions.map(({ eventType, code, handled, reason }) => ({ eventType, code, handled, reason })),
  [
    { eventType: 'keydown', code: 40, handled: false, reason: 'compat-pending' },
    { eventType: 'keyup', code: 37, handled: true, reason: 'compat-horizontal-keyup' },
    { eventType: 'keyup', code: 40, handled: false, reason: 'paired-release' },
    { eventType: 'keydown', code: 38, handled: false, reason: 'compat-pending' },
    { eventType: 'keyup', code: 39, handled: true, reason: 'compat-horizontal-keyup' },
    { eventType: 'keyup', code: 38, handled: false, reason: 'paired-release' },
    { eventType: 'keydown', code: 40, handled: false, reason: 'compat-pending' },
    { eventType: 'keyup', code: 40, handled: true, reason: 'compat-vertical-keyup' },
    { eventType: 'keydown', code: 38, handled: false, reason: 'compat-pending' },
    { eventType: 'keyup', code: 38, handled: true, reason: 'compat-vertical-keyup' },
  ],
  'diagnostics must expose compatibility gesture normalization'
)

const repeatedActions = []
const repeated = createVisKeyboardCoordinator((code) => repeatedActions.push(code))
for (let index = 0; index < 10; index += 1) {
  repeated.handleKeydown({ keyCode: 38, key: 'ArrowUp', repeat: false })
  repeated.handleKeyup({ keyCode: 38, key: 'ArrowUp' })
}
assert.deepStrictEqual(
  repeatedActions,
  Array(10).fill(37),
  'ten physical left gestures translated to ArrowUp must produce exactly ten previous-page navigations'
)

for (let index = 0; index < 4; index += 1) {
  repeated.handleKeydown({ keyCode: 38, key: 'ArrowUp', repeat: false })
  repeated.handleKeyup({ keyCode: 39, key: 'ArrowRight' })
  repeated.handleKeyup({ keyCode: 38, key: 'ArrowUp' })
  repeated.handleKeydown({ keyCode: 40, key: 'ArrowDown', repeat: false })
  repeated.handleKeyup({ keyCode: 37, key: 'ArrowLeft' })
  repeated.handleKeyup({ keyCode: 40, key: 'ArrowDown' })
}
assert.deepStrictEqual(
  repeatedActions.slice(10),
  [39, 37, 39, 37, 39, 37, 39, 37],
  'alternating malformed gestures must preserve event-to-navigation correspondence'
)

const missingReleaseActions = []
const missingRelease = createVisKeyboardCoordinator((code) => missingReleaseActions.push(code))
for (let index = 0; index < 30; index += 1) {
  missingRelease.handleKeydown({ keyCode: 37, key: 'ArrowLeft', repeat: false })
}
for (let index = 0; index < 30; index += 1) {
  missingRelease.handleKeydown({ keyCode: 39, key: 'ArrowRight', repeat: false })
}
assert.deepStrictEqual(
  missingReleaseActions,
  [...Array(30).fill(37), ...Array(30).fill(39)],
  'lost keyup events must not lock out later non-repeat left and right presses'
)

console.log('visKeyboardNavigation tests passed')
