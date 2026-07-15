import assert from 'assert'
import { readFile } from 'fs/promises'

const source = await readFile(new URL('../src/utils/visWheelNavigation.js', import.meta.url), 'utf8')
const moduleUrl = `data:text/javascript;base64,${Buffer.from(source).toString('base64')}`
const { createVisWheelCoordinator } = await import(moduleUrl)

const actions = []
const decisions = []
const wheel = createVisWheelCoordinator(
  (code) => actions.push(code),
  (decision) => decisions.push(decision),
  { threshold: 80, cooldownMs: 400 }
)

let decision = wheel.handleWheel({ deltaY: 30 }, 1000)
assert.strictEqual(decision.consumed, true, 'a non-zero preview wheel event must suppress page scrolling')
assert.deepStrictEqual(actions, [], 'small wheel deltas must accumulate')

wheel.handleWheel({ deltaY: 55 }, 1010)
assert.deepStrictEqual(actions, [39], 'wheel down must navigate to the next page once')

wheel.handleWheel({ deltaY: 120 }, 1100)
assert.deepStrictEqual(actions, [39], 'wheel events inside cooldown must not navigate again')

wheel.handleWheel({ deltaY: 50 }, 1500)
wheel.handleWheel({ deltaY: -50 }, 1510)
assert.deepStrictEqual(actions, [39], 'direction reversal must discard the previous accumulation')
wheel.handleWheel({ deltaY: -35 }, 1520)
assert.deepStrictEqual(actions, [39, 37], 'wheel up must navigate to the previous page once')

decision = wheel.handleWheel({ deltaY: 0 }, 2000)
assert.strictEqual(decision.consumed, false, 'zero-delta wheel events must remain untouched')

wheel.reset()
wheel.handleWheel({ deltaY: 80 }, 2001)
assert.deepStrictEqual(actions, [39, 37, 39], 'reset must clear cooldown and accumulation')

assert.deepStrictEqual(
  decisions.map(({ handled, reason, consumed }) => ({ handled, reason, consumed })),
  [
    { handled: false, reason: 'accumulating', consumed: true },
    { handled: true, reason: 'threshold', consumed: true },
    { handled: false, reason: 'cooldown', consumed: true },
    { handled: false, reason: 'accumulating', consumed: true },
    { handled: false, reason: 'direction-reset', consumed: true },
    { handled: true, reason: 'threshold', consumed: true },
    { handled: false, reason: 'zero-delta', consumed: false },
    { handled: false, reason: 'reset', consumed: false },
    { handled: true, reason: 'threshold', consumed: true },
  ],
  'diagnostics must expose each wheel normalization decision'
)

console.log('visWheelNavigation tests passed')
