# Budget Map and Keyboard Reliability Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore the regional budget-revenue map on Vastbase and make each physical left/right key press switch the big-screen carousel exactly once and immediately.

**Architecture:** Keep the existing backend/frontend field contract and remove only the PostgreSQL-invalid redundant `DISTINCT`. Isolate keyboard event de-duplication in a DOM-free utility so `keydown`, `keyup` fallback, repeat suppression, and blur reset can be tested independently before wiring it into the Vue preview component.

**Tech Stack:** Java 8, Spring Boot 2, MyBatis XML, JUnit 4.12, Vue 2, Element UI Carousel, Node.js 14 ESM tests.

## Global Constraints

- Preserve the original `dwbi_vis_screen` response-field contract.
- Quote `AREA_DSCR` and `INDEX_VALUE` aliases in uppercase for Vastbase; keep frontend-required lowercase aliases lowercase.
- Do not change other big-screen queries, carousel intervals, or page styling.
- Preserve all unrelated tracked and untracked migration changes in the dirty worktree.

---

### Task 1: Make budget title queries valid on Vastbase

**Files:**
- Modify: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/QueryMapDataMapperContractTest.java`
- Modify: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryMapDataMapper.xml:90-107`

**Interfaces:**
- Consumes: MyBatis statements `getBudgetRevenueTitle` and `getBudgetRevenueRateTitle`.
- Produces: rows keyed by uppercase `AREA_DSCR` and `INDEX_VALUE`, grouped and ordered by `AREA_CODE` without PostgreSQL-invalid `SELECT DISTINCT`.

- [ ] **Step 1: Write the failing Mapper contract test**

Add `assertFalse` and extend `budgetRevenueMapPreservesSourceFieldCasing()`:

```java
import static org.junit.Assert.assertFalse;

for (String query : new String[]{"getBudgetRevenueTitle", "getBudgetRevenueRateTitle"}) {
    String sql = select(query);
    assertTrue(query + " must expose controller key AREA_DSCR", sql.contains("AS \"AREA_DSCR\""));
    assertTrue(query + " must expose controller key INDEX_VALUE", sql.contains("AS \"INDEX_VALUE\""));
    assertFalse(query + " must not combine DISTINCT with ORDER BY AREA_CODE",
            Pattern.compile("SELECT\\s+DISTINCT", Pattern.CASE_INSENSITIVE).matcher(sql).find());
}
```

- [ ] **Step 2: Run the focused test and verify RED**

Run from `vis-screen-backend`:

```bash
mvn -pl jeecg-boot-module-system -am -DskipTests package
java -cp "jeecg-boot-module-system/target/test-classes:jeecg-boot-module-system/target/classes:$(cat /tmp/vis-test-classpath.txt)" org.junit.runner.JUnitCore org.jeecg.modules.visualScreen.mapper.QueryMapDataMapperContractTest
```

Expected: FAIL twice with `must not combine DISTINCT with ORDER BY AREA_CODE`.

- [ ] **Step 3: Remove only the redundant DISTINCT keywords**

Apply these two exact replacements and leave the surrounding clauses byte-for-byte unchanged:

```diff
-        SELECT DISTINCT a.AREA_DSCR AS "AREA_DSCR", <include refid="sumIndexExpr"/> AS "INDEX_VALUE"
+        SELECT a.AREA_DSCR AS "AREA_DSCR", <include refid="sumIndexExpr"/> AS "INDEX_VALUE"
-        SELECT DISTINCT a.AREA_DSCR AS "AREA_DSCR", <include refid="sumRateExpr"/> AS "INDEX_VALUE"
+        SELECT a.AREA_DSCR AS "AREA_DSCR", <include refid="sumRateExpr"/> AS "INDEX_VALUE"
```

- [ ] **Step 4: Run Mapper tests and verify GREEN**

Rebuild the test classpath and run all three Mapper contract classes:

```bash
mvn -pl jeecg-boot-module-system dependency:build-classpath \
  -Dmdep.outputFile=/tmp/vis-test-classpath.txt \
  -DincludeScope=test
java -cp "jeecg-boot-module-system/target/test-classes:jeecg-boot-module-system/target/classes:$(cat /tmp/vis-test-classpath.txt)" org.junit.runner.JUnitCore \
  org.jeecg.modules.visualScreen.mapper.QueryDataMapperContractTest \
  org.jeecg.modules.visualScreen.mapper.QueryTableDataMapperContractTest \
  org.jeecg.modules.visualScreen.mapper.QueryMapDataMapperContractTest
```

Expected: `OK (9 tests)` or a higher count after adding the assertion.

- [ ] **Step 5: Record the scoped change**

Because this checkout contains pre-existing migration changes, inspect only these paths and do not stage unrelated files:

```bash
git diff --check -- \
  vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryMapDataMapper.xml \
  vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/QueryMapDataMapperContractTest.java
```

Commit these two files only if the new test file can be committed without capturing unrelated pre-existing work; otherwise leave them as a reviewed scoped diff and report that constraint.

---

### Task 2: Add deterministic keyboard event coordination

**Files:**
- Create: `org-tribe-view/src/utils/visKeyboardNavigation.js`
- Create: `org-tribe-view/tests/visKeyboardNavigation.test.mjs`

**Interfaces:**
- Produces: `createVisKeyboardCoordinator(onKeyCode)`.
- Returned methods: `handleKeydown(event)`, `handleKeyup(event)`, and `reset()`.
- Callback contract: `onKeyCode(code)` is called once for key codes `27`, `37`, or `39` per physical press.

- [ ] **Step 1: Write the failing keyboard event-sequence test**

Create `org-tribe-view/tests/visKeyboardNavigation.test.mjs`:

```js
import assert from 'assert'
import { readFile } from 'fs/promises'

const source = await readFile(new URL('../src/utils/visKeyboardNavigation.js', import.meta.url), 'utf8')
const moduleUrl = `data:text/javascript;base64,${Buffer.from(source).toString('base64')}`
const { createVisKeyboardCoordinator } = await import(moduleUrl)

const actions = []
const keyboard = createVisKeyboardCoordinator((code) => actions.push(code))

keyboard.handleKeydown({ keyCode: 39, repeat: false })
assert.deepStrictEqual(actions, [39], 'keydown must navigate immediately')
keyboard.handleKeyup({ keyCode: 39 })
assert.deepStrictEqual(actions, [39], 'matching keyup must not navigate twice')

keyboard.handleKeydown({ keyCode: 37, repeat: true })
assert.deepStrictEqual(actions, [39], 'repeat keydown must be ignored')

keyboard.handleKeyup({ keyCode: 37 })
assert.deepStrictEqual(actions, [39, 37], 'keyup-only terminals must navigate once')

keyboard.handleKeydown({ which: 39, repeat: false })
keyboard.reset()
keyboard.handleKeyup({ which: 39 })
assert.deepStrictEqual(actions, [39, 37, 39, 39], 'blur reset must allow a release-only fallback')

keyboard.handleKeydown({ keyCode: 65, repeat: false })
keyboard.handleKeyup({ keyCode: 65 })
assert.deepStrictEqual(actions, [39, 37, 39, 39], 'unrelated keys must be ignored')

console.log('visKeyboardNavigation tests passed')
```

- [ ] **Step 2: Run the test and verify RED**

Run from `org-tribe-view`:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH node tests/visKeyboardNavigation.test.mjs
```

Expected: FAIL because `src/utils/visKeyboardNavigation.js` does not exist.

- [ ] **Step 3: Implement the minimal DOM-free coordinator**

Create `org-tribe-view/src/utils/visKeyboardNavigation.js`:

```js
const NAVIGATION_KEY_CODES = new Set([27, 37, 39])

function resolveKeyCode(event) {
  return event && (event.keyCode || event.which)
}

export function createVisKeyboardCoordinator(onKeyCode) {
  const handledKeydowns = new Set()

  return {
    handleKeydown(event) {
      const code = resolveKeyCode(event)
      if (!NAVIGATION_KEY_CODES.has(code) || event.repeat) return
      handledKeydowns.add(code)
      onKeyCode(code)
    },
    handleKeyup(event) {
      const code = resolveKeyCode(event)
      if (!NAVIGATION_KEY_CODES.has(code)) return
      if (!handledKeydowns.delete(code)) onKeyCode(code)
    },
    reset() {
      handledKeydowns.clear()
    }
  }
}
```

- [ ] **Step 4: Run the test and verify GREEN**

Run the same Node command.

Expected: `visKeyboardNavigation tests passed`.

---

### Task 3: Wire the coordinator into the big-screen preview

**Files:**
- Modify: `org-tribe-view/src/views/vis/bigscreen/BigScreenPreview.vue:70-115,220-233,307-314`
- Modify: `org-tribe-view/tests/visCarousel.test.mjs:22-24`

**Interfaces:**
- Consumes: `createVisKeyboardCoordinator(onKeyCode)` from Task 2.
- Produces: document-level immediate keyboard navigation with keyup fallback and complete listener cleanup.

- [ ] **Step 1: Extend the source contract test before changing the component**

Replace the two existing keyup-only assertions in `visCarousel.test.mjs` with:

```js
assert.match(previewSource, /document\.addEventListener\('keydown', this\.handleNavigationKeydown, false\)/)
assert.match(previewSource, /document\.addEventListener\('keyup', this\.handleNavigationKeyup, false\)/)
assert.match(previewSource, /window\.addEventListener\('blur', this\.resetNavigationKeys, false\)/)
assert.match(previewSource, /document\.removeEventListener\('keydown', this\.handleNavigationKeydown, false\)/)
assert.match(previewSource, /document\.removeEventListener\('keyup', this\.handleNavigationKeyup, false\)/)
assert.match(previewSource, /window\.removeEventListener\('blur', this\.resetNavigationKeys, false\)/)
```

- [ ] **Step 2: Run the existing test and verify RED**

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH node tests/visCarousel.test.mjs
```

Expected: FAIL because the component still registers only `keyup` with `myKeyup`.

- [ ] **Step 3: Wire the coordinator into `BigScreenPreview.vue`**

Add the import:

```js
import { createVisKeyboardCoordinator } from '@/utils/visKeyboardNavigation'
```

In `mounted()`, initialize once and register all events:

```js
this.keyboardCoordinator = createVisKeyboardCoordinator(this.navigateByKeyCode)
document.addEventListener('keydown', this.handleNavigationKeydown, false)
document.addEventListener('keyup', this.handleNavigationKeyup, false)
window.addEventListener('blur', this.resetNavigationKeys, false)
```

Replace `myKeyup` with focused methods:

```js
handleNavigationKeydown(event) {
  this.keyboardCoordinator.handleKeydown(event)
},
handleNavigationKeyup(event) {
  this.keyboardCoordinator.handleKeyup(event)
},
resetNavigationKeys() {
  if (this.keyboardCoordinator) this.keyboardCoordinator.reset()
},
navigateByKeyCode(code) {
  if (code === 27) {
    this.$router.push({ path: '/vis/bigscreen/schemes' })
  } else if (code === 37 && this.$refs.carouselFull) {
    this.$refs.carouselFull.prev()
  } else if (code === 39 && this.$refs.carouselFull) {
    this.$refs.carouselFull.next()
  }
},
```

In `beforeDestroy()`, remove the exact same listeners and reset state:

```js
document.removeEventListener('keydown', this.handleNavigationKeydown, false)
document.removeEventListener('keyup', this.handleNavigationKeyup, false)
window.removeEventListener('blur', this.resetNavigationKeys, false)
this.resetNavigationKeys()
```

- [ ] **Step 4: Run both frontend tests and verify GREEN**

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH node tests/visKeyboardNavigation.test.mjs
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH node tests/visCarousel.test.mjs
```

Expected: both scripts print their `tests passed` messages.

- [ ] **Step 5: Build the production frontend**

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH npm run build
```

Expected: `DONE Build complete`; existing CSS-order and asset-size warnings are acceptable.

- [ ] **Step 6: Record the scoped change**

Run `git diff --check` on the four frontend paths. Since `BigScreenPreview.vue` and the tests directory are pre-existing untracked migration work, do not create a partial commit that references other uncommitted modules; leave the verified scoped diff for the containing migration commit.

---

### Task 4: Deployment evidence and end-to-end verification

**Files:**
- Verify artifact: `vis-screen-backend/jeecg-boot-module-system/target/jeecg-boot-module-system-2.3.0.jar`
- Verify artifact: `org-tribe-view/dist/`

**Interfaces:**
- Consumes: built backend Mapper XML and frontend production bundle.
- Produces: evidence that the deployed `getBudgetRevenue` response contains map data and each keyboard press advances one page.

- [ ] **Step 1: Verify packaged Mapper content**

```bash
unzip -p vis-screen-backend/jeecg-boot-module-system/target/jeecg-boot-module-system-2.3.0.jar \
  org/jeecg/modules/visualScreen/mapper/xml/QueryMapDataMapper.xml | \
  sed -n '/getBudgetRevenueTitle/,/<\/select>/p'
```

Expected: uppercase quoted aliases, `GROUP BY`, and `ORDER BY`, with no `SELECT DISTINCT`.

- [ ] **Step 2: Deploy/restart the actual 9090 services**

The browser-visible 9090 process is outside the current container. Replace its frontend and `vis-screen` artifacts using that environment's normal deployment path, then restart both components. Do not report runtime success until this has happened.

- [ ] **Step 3: Verify the real scheme in a browser**

Log in through `http://cui02:3000` as `lj_001`, open scheme ID `9ddc70ac2ee342698885979936e2083b`, and verify:

- `queryMapData/getBudgetRevenue` returns `result: success`, `msg: 查询成功`, non-empty `data`, and three-entry `titleArea`.
- The map renders values rather than remaining blank.
- Eight deliberate ArrowRight presses produce eight immediate single-page advances.
- Holding ArrowRight produces only one advance until release.
- ArrowLeft advances exactly one page in the reverse direction.

- [ ] **Step 4: Final hygiene**

Run scoped `git diff --check`, confirm no temporary `[DEBUG-...]` logs or browser probe files remain, and summarize unrelated dirty-worktree files as intentionally untouched.
