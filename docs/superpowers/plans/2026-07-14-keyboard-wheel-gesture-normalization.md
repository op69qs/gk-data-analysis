# Keyboard and Wheel Gesture Normalization Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make big-screen page navigation match physical keyboard and wheel gestures exactly once, including the captured mismatched arrow sequences.

**Architecture:** Keep event normalization in small framework-independent utilities and keep `BigScreenPreview.vue` responsible only for listener lifecycle and carousel commands. The keyboard utility becomes a gesture state machine; a new wheel utility accumulates signed deltas and applies cooldown before emitting direction codes.

**Tech Stack:** Vue 2, Element UI carousel, browser keyboard/wheel events, Node ESM assertion tests, Playwright browser verification.

## Global Constraints

- Preserve immediate one-page navigation for normal `ArrowLeft` and `ArrowRight` keydown events.
- Normalize the captured mismatched arrow sequences into one logical gesture.
- Guarantee that one logical keyboard or wheel gesture changes at most one page.
- Use `ArrowDown` as previous and `ArrowUp` as next only for compatibility fallback.
- Use positive wheel delta for next and negative wheel delta for previous.
- Keep keyboard and wheel diagnostics opt-in through `visDebug=1`.
- Do not stage or commit unrelated migration files from the dirty workspace.

---

### Task 1: Keyboard gesture state machine

**Files:**
- Modify: `org-tribe-view/src/utils/visKeyboardNavigation.js`
- Modify: `org-tribe-view/tests/visKeyboardNavigation.test.mjs`

**Interfaces:**
- Consumes: browser-like events containing `keyCode`, `which`, `key`, and `repeat`.
- Produces: `createVisKeyboardCoordinator(onKeyCode, onDecision)` with `handleKeydown(event)`, `handleKeyup(event)`, and `reset()`.
- Emits: left code `37`, right code `39`, and existing escape code `27` through `onKeyCode`.

- [ ] **Step 1: Add failing real-log replay tests**

Add isolated coordinators and assertions for these exact sequences:

```js
const abnormalActions = []
const abnormal = createVisKeyboardCoordinator((code) => abnormalActions.push(code))

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
assert.deepStrictEqual(abnormalActions, [37, 39, 37, 39], 'vertical-only fallback must preserve direction')
```

Retain assertions for normal horizontal keydown immediacy, repeat suppression, keyup-only terminals, blur reset, escape, and unrelated keys.

- [ ] **Step 2: Run the keyboard test and verify RED**

Run:

```bash
cd org-tribe-view
node tests/visKeyboardNavigation.test.mjs
```

Expected: FAIL because vertical keydown/keyup is currently reported as `unsupported` and paired releases are not modeled as one gesture.

- [ ] **Step 3: Implement the minimal keyboard state machine**

Use these states:

```js
const HORIZONTAL_CODES = new Set([37, 39])
const VERTICAL_FALLBACK = new Map([[40, 37], [38, 39]])
let gesture = null
```

Behavior:

```js
// horizontal keydown: emit immediately, gesture = { resolved: true }
// vertical keydown: gesture = { resolved: false, fallbackCode }
// horizontal keyup with unresolved gesture: emit its horizontal code once
// vertical keyup with unresolved gesture: emit fallbackCode once
// any later arrow keyup in the resolved gesture: paired-release, no emit
// new non-repeat keydown replaces the completed gesture
// reset clears gesture and existing handled-key state
```

Every branch calls the existing decision callback with a falsifiable reason: `keydown`, `compat-pending`, `compat-horizontal-keyup`, `compat-vertical-keyup`, `paired-release`, `release`, `keyup-fallback`, `repeat`, `unsupported`, or `reset`.

- [ ] **Step 4: Run the keyboard test and verify GREEN**

Run the Step 2 command. Expected: `visKeyboardNavigation tests passed` and exit code `0`.

- [ ] **Step 5: Inspect only the focused keyboard files**

```bash
git diff --check -- org-tribe-view/src/utils/visKeyboardNavigation.js org-tribe-view/tests/visKeyboardNavigation.test.mjs
```

Expected: no output and exit code `0`. Do not commit these pre-existing untracked migration paths independently.

---

### Task 2: Wheel gesture normalizer

**Files:**
- Create: `org-tribe-view/src/utils/visWheelNavigation.js`
- Create: `org-tribe-view/tests/visWheelNavigation.test.mjs`

**Interfaces:**
- Produces: `createVisWheelCoordinator(onDirection, onDecision, options)`.
- `handleWheel(event, now)` returns a decision and emits `39` for next or `37` for previous.
- Options: `threshold` defaults to `80`; `cooldownMs` defaults to `400`.
- `reset()` clears accumulated delta, direction, and cooldown state.

- [ ] **Step 1: Write failing wheel behavior tests**

Use a controllable timestamp and real utility code loaded through a data URL, matching the existing test style:

```js
const actions = []
const wheel = createVisWheelCoordinator((code) => actions.push(code), () => {}, {
  threshold: 80,
  cooldownMs: 400,
})

wheel.handleWheel({ deltaY: 30 }, 1000)
wheel.handleWheel({ deltaY: 30 }, 1020)
assert.deepStrictEqual(actions, [], 'sub-threshold deltas must accumulate')
wheel.handleWheel({ deltaY: 25 }, 1040)
assert.deepStrictEqual(actions, [39], 'positive threshold crossing must navigate next once')
wheel.handleWheel({ deltaY: 120 }, 1100)
assert.deepStrictEqual(actions, [39], 'momentum during cooldown must be ignored')
wheel.handleWheel({ deltaY: -50 }, 1500)
wheel.handleWheel({ deltaY: -40 }, 1530)
assert.deepStrictEqual(actions, [39, 37], 'negative threshold crossing after cooldown must navigate previous')
```

Add direction-reversal assertions: `+50` followed by `-50` must reset accumulation and not cross the threshold.

- [ ] **Step 2: Run the wheel test and verify RED**

```bash
cd org-tribe-view
node tests/visWheelNavigation.test.mjs
```

Expected: FAIL because `visWheelNavigation.js` and `createVisWheelCoordinator` do not exist.

- [ ] **Step 3: Implement the minimal wheel coordinator**

The implementation keeps:

```js
let accumulatedDelta = 0
let direction = 0
let cooldownUntil = 0
```

On each event, normalize `Math.sign(event.deltaY)`, reject zero deltas, reject events before `cooldownUntil`, reset accumulation on direction reversal, and emit only when `Math.abs(accumulatedDelta) >= threshold`. On emit, set `cooldownUntil = now + cooldownMs` and reset accumulation. Decision reasons are `zero-delta`, `accumulating`, `direction-reset`, `threshold`, `cooldown`, and `reset`.

- [ ] **Step 4: Run the wheel test and verify GREEN**

Run the Step 2 command. Expected: `visWheelNavigation tests passed` and exit code `0`.

- [ ] **Step 5: Inspect focused wheel files**

```bash
git diff --check -- org-tribe-view/src/utils/visWheelNavigation.js org-tribe-view/tests/visWheelNavigation.test.mjs
```

Expected: no output and exit code `0`. Keep these paths with the uncommitted frontend migration set.

---

### Task 3: Big-screen listener lifecycle and diagnostics

**Files:**
- Modify: `org-tribe-view/src/views/vis/bigscreen/BigScreenPreview.vue`
- Test: `org-tribe-view/tests/visKeyboardNavigation.test.mjs`
- Test: `org-tribe-view/tests/visWheelNavigation.test.mjs`

**Interfaces:**
- Consumes: both coordinators emitting numeric direction codes.
- Reuses: `navigateByKeyCode(code)` as the single carousel mutation boundary.
- Produces: opt-in labels `keyboard decision`, `keyboard navigation`, `wheel decision`, and `wheel navigation`.

- [ ] **Step 1: Add wheel coordinator lifecycle**

Import `createVisWheelCoordinator`, add `wheelCoordinator` to component data, create it in `mounted`, and register:

```js
window.addEventListener('wheel', this.handleNavigationWheel, { passive: false })
```

Remove the listener in `beforeDestroy` using the same capture value. Call `reset()` alongside keyboard reset on blur.

- [ ] **Step 2: Route handled wheel gestures through the carousel boundary**

Implement:

```js
handleNavigationWheel(event) {
  const decision = this.wheelCoordinator.handleWheel(event, performance.now())
  if (decision && decision.consumed) event.preventDefault()
}
```

The wheel coordinator callback calls `navigateByKeyCode(code, 'wheel')`; keyboard calls it with source `keyboard`. Extend navigation diagnostics with `source`, `beforeIndex`, and `afterIndex` without changing carousel behavior.

- [ ] **Step 3: Keep diagnostics opt-in**

Only register capture diagnostics and call `visPreviewDebug` for keyboard/wheel decisions when `keyboardDebugEnabled` is true. Default preview navigation must not append keyboard or wheel log entries.

- [ ] **Step 4: Run all focused frontend tests**

```bash
cd org-tribe-view
node tests/visKeyboardNavigation.test.mjs
node tests/visWheelNavigation.test.mjs
node tests/visPreviewDebug.test.mjs
node tests/visCarousel.test.mjs
```

Expected: four success messages and exit code `0`.

- [ ] **Step 5: Build the production frontend into the running 9090 classpath**

From the repository root, outside the restricted network sandbox:

```bash
mvn -pl org-tribe-system -DskipTests package
```

Expected: `BUILD SUCCESS`; `org-tribe-system/target/classes/static` contains the newly hashed application and big-screen chunks.

- [ ] **Step 6: Verify normal and abnormal keyboard gestures in the browser**

After portal SSO, open:

```text
http://localhost:9090/vis/preview?info=9ddc70ac2ee342698885979936e2083b&interval=5000&autoSetting=0&visDebug=1
```

Verify a normal right key changes `0 → 1`. Replay the abnormal event patterns through a Playwright page dispatch only for this known localhost page and verify each full sequence changes exactly one page. Inspect `window.__VIS_PREVIEW_DEBUG__` read-only and require one `keyboard navigation` record per gesture.

- [ ] **Step 7: Verify wheel threshold and default no-log mode**

On the debug URL, dispatch deltas below and above threshold and verify exactly one `wheel navigation` entry and one page transition. Reload without `visDebug=1`, perform one normal key and one deliberate wheel gesture, verify both transitions occur and filtered keyboard/wheel diagnostic count remains zero.

- [ ] **Step 8: Final review**

```bash
git diff --check -- \
  org-tribe-view/src/utils/visKeyboardNavigation.js \
  org-tribe-view/src/utils/visWheelNavigation.js \
  org-tribe-view/src/views/vis/bigscreen/BigScreenPreview.vue \
  org-tribe-view/tests/visKeyboardNavigation.test.mjs \
  org-tribe-view/tests/visWheelNavigation.test.mjs
```

Expected: no output. Review correctness, listener cleanup, security of diagnostics, and unbounded-event performance. Do not stage unrelated dirty-worktree files.
