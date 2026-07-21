# Legacy Indicator Query Production Restore Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore the independent legacy “指标库 / 指标查询” menu so it opens the complete production `statistics/indexLibrary` query page without changing the `vis` indicator-library menu.

**Architecture:** Treat the production page and menu record as one contract with two independently testable slices. First restore the original query component from the known production baseline, then repair both fresh-install and live-site menu SQL with a guarded forward migration and rollback.

**Tech Stack:** Vue 2.6, vue-template-compiler 2.6, Node.js built-in test/assert modules, Vastbase-compatible SQL, Git.

## Global Constraints

- Do not modify `/vis/index-library`, `vis/IndexLibraryList`, or any vis permission record.
- Keep `statistics/schemeIndex.vue` as an independent public-scheme list page.
- Restore `statistics/indexLibrary.vue` from production baseline commit `84e5e01`; do not redesign it.
- Do not change database tables or old indicator-query API contracts.
- Limit SQL updates to menu ID `cae8031ed1a7aeaed5625928a5ed74da`, parent ID `aea6b487925d084dad182e09c95a6c79`, and name `指标查询`.
- Do not claim the live database was changed unless the SQL is actually executed against it and read back.

---

### Task 1: Restore the Production Query Page

**Files:**
- Create: `org-tribe-view/tests/legacyIndicatorQuery.test.mjs`
- Modify: `org-tribe-view/src/views/statistics/indexLibrary.vue`
- Read only: `org-tribe-view/src/views/statistics/schemeIndex.vue`

**Interfaces:**
- Consumes: production baseline `84e5e01:org-tribe-view/src/views/statistics/indexLibrary.vue`
- Produces: a complete `indexLibrary` component and a reusable static contract test

- [ ] **Step 1: Write the failing production-page regression test**

```javascript
// org-tribe-view/tests/legacyIndicatorQuery.test.mjs
import assert from 'node:assert/strict'
import { readFile } from 'node:fs/promises'
import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

const testsDir = dirname(fileURLToPath(import.meta.url))
const projectRoot = resolve(testsDir, '..')

const indexLibrary = await readFile(
  resolve(projectRoot, 'src/views/statistics/indexLibrary.vue'),
  'utf8'
)
const schemeIndex = await readFile(
  resolve(projectRoot, 'src/views/statistics/schemeIndex.vue'),
  'utf8'
)

assert.match(indexLibrary, /<div class="model-title">公共指标/)
assert.match(indexLibrary, /<div class="model-title" style="margin-top:20px;">\s*我的指标/)
assert.match(indexLibrary, /@click="getData">查询<\/a-button>/)
assert.match(indexLibrary, /<v-table/)
assert.match(schemeIndex, /path:\s*'\/statistics\/indexLibrary'/)
assert.doesNotMatch(indexLibrary, /\/vis\/index-library|vis\/IndexLibraryList/)
assert.doesNotMatch(schemeIndex, /\/vis\/index-library|vis\/IndexLibraryList/)

console.log('legacy indicator query page contract passed')
```

- [ ] **Step 2: Run the test and verify the current incomplete page fails**

Run:

```bash
cd org-tribe-view
node --test tests/legacyIndicatorQuery.test.mjs
```

Expected: FAIL on the missing `公共指标` assertion.

- [ ] **Step 3: Restore the exact production component**

Generate the reverse patch for this file only:

```bash
git diff HEAD 84e5e01 -- org-tribe-view/src/views/statistics/indexLibrary.vue
```

Apply that complete diff with `apply_patch`. Confirm afterward:

```bash
git diff --numstat -- org-tribe-view/src/views/statistics/indexLibrary.vue
```

Expected: the working-tree change restores the content removed after `84e5e01`; no other source file is changed.

- [ ] **Step 4: Compile the restored Vue template**

Run:

```bash
cd org-tribe-view
node -e 'const fs=require("fs"); const c=require("vue-template-compiler"); const s=fs.readFileSync("src/views/statistics/indexLibrary.vue","utf8"); const p=c.parseComponent(s); const out=c.compile(p.template.content,{outputSourceRange:true}); if(out.errors.length){console.error(out.errors); process.exit(1)} console.log("indexLibrary template compile passed")'
```

Expected: `indexLibrary template compile passed`.

- [ ] **Step 5: Run the page regression test**

Run:

```bash
cd org-tribe-view
node --test tests/legacyIndicatorQuery.test.mjs
```

Expected: PASS with `legacy indicator query page contract passed`.

- [ ] **Step 6: Commit the page slice**

```bash
git add org-tribe-view/tests/legacyIndicatorQuery.test.mjs org-tribe-view/src/views/statistics/indexLibrary.vue
git diff --cached --check
git commit -m "fix: restore production indicator query page"
```

---

### Task 2: Repair the Legacy Menu SQL Without Touching vis

**Files:**
- Modify: `org-tribe-view/tests/legacyIndicatorQuery.test.mjs`
- Modify: `document/implementation_plans/2026042615-vis-production-menu-converge.sql:446`
- Create: `document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.sql`
- Create: `document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.rollback.sql`
- Modify: `document/implementation_plans/2026042619-fix-legacy-indicator-query-menu.md`

**Interfaces:**
- Consumes: the fixed page contract from Task 1 and the exact legacy menu identity from the approved design
- Produces: corrected initialization SQL, guarded live-site SQL, rollback SQL, and menu contract assertions

- [ ] **Step 1: Extend the regression test with menu-contract assertions**

Append the following before the final `console.log`:

```javascript
const convergeSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026042615-vis-production-menu-converge.sql'),
  'utf8'
)
const forwardSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.sql'),
  'utf8'
)
const rollbackSql = await readFile(
  resolve(projectRoot, '../document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.rollback.sql'),
  'utf8'
)

function legacyMenuUpdate(sql) {
  const where = "WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'"
  const whereAt = sql.indexOf(where)
  assert.notStrictEqual(whereAt, -1, 'legacy indicator-query menu guard is missing')
  const updateAt = sql.lastIndexOf('UPDATE sys_permission', whereAt)
  const endAt = sql.indexOf(';', whereAt)
  assert.notStrictEqual(updateAt, -1, 'legacy indicator-query UPDATE is missing')
  assert.notStrictEqual(endAt, -1, 'legacy indicator-query UPDATE is unterminated')
  return sql.slice(updateAt, endAt + 1)
}

for (const sql of [convergeSql, forwardSql]) {
  const block = legacyMenuUpdate(sql)
  assert.match(block, /SET url = '\/statistics\/indexLibrary'/)
  assert.match(block, /component = 'statistics\/indexLibrary'/)
  assert.match(block, /parent_id = 'aea6b487925d084dad182e09c95a6c79'/)
  assert.match(block, /name = '指标查询'/)
  assert.doesNotMatch(block, /\/vis\/|vis\//)
}

const rollbackBlock = legacyMenuUpdate(rollbackSql)
assert.match(rollbackBlock, /SET url = '\/statistics\/schemeIndex'/)
assert.match(rollbackBlock, /component = 'statistics\/schemeIndex'/)
```

- [ ] **Step 2: Run the test and verify the SQL contract fails**

Run:

```bash
cd org-tribe-view
node --test tests/legacyIndicatorQuery.test.mjs
```

Expected: FAIL because the forward and rollback SQL files do not exist yet, or because the aggregate SQL still targets `schemeIndex`.

- [ ] **Step 3: Correct the aggregate initialization SQL**

Change only the guarded legacy-menu update to:

```sql
UPDATE sys_permission
SET url = '/statistics/indexLibrary',
    component = 'statistics/indexLibrary',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';
```

- [ ] **Step 4: Add the guarded live-site forward SQL**

```sql
BEGIN;

UPDATE sys_permission
SET url = '/statistics/indexLibrary',
    component = 'statistics/indexLibrary',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';

SELECT id, parent_id, name, url, component, component_name, redirect
FROM sys_permission
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
   OR url = '/vis/index-library'
ORDER BY id;

COMMIT;
```

- [ ] **Step 5: Add the guarded rollback SQL**

```sql
BEGIN;

UPDATE sys_permission
SET url = '/statistics/schemeIndex',
    component = 'statistics/schemeIndex',
    component_name = NULL,
    redirect = NULL,
    update_by = 'admin',
    update_time = CURRENT_TIMESTAMP
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da'
  AND parent_id = 'aea6b487925d084dad182e09c95a6c79'
  AND name = '指标查询';

SELECT id, parent_id, name, url, component, component_name, redirect
FROM sys_permission
WHERE id = 'cae8031ed1a7aeaed5625928a5ed74da';

COMMIT;
```

- [ ] **Step 6: Correct the implementation record**

Replace the incorrect conclusion in `2026042619-fix-legacy-indicator-query-menu.md` with these explicit statements:

```markdown
- `statistics/schemeIndex.vue` 是独立的公共方案列表页。
- `statistics/indexLibrary.vue` 是生产“指标查询”菜单直接打开的查询页。
- 旧“指标查询”菜单应指向 `/statistics/indexLibrary + statistics/indexLibrary`。
- vis“指标库方案”继续独立指向 `/vis/index-library`，本修复不修改它。
```

- [ ] **Step 7: Run the complete regression test**

Run:

```bash
cd org-tribe-view
node --test tests/legacyIndicatorQuery.test.mjs
```

Expected: PASS.

- [ ] **Step 8: Commit the menu slice**

```bash
git add org-tribe-view/tests/legacyIndicatorQuery.test.mjs \
  document/implementation_plans/2026042615-vis-production-menu-converge.sql \
  document/implementation_plans/2026042619-fix-legacy-indicator-query-menu.md \
  document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.sql \
  document/implementation_plans/2026072101-fix-legacy-indicator-query-entry.rollback.sql
git diff --cached --check
git commit -m "fix: point legacy indicator menu to production query"
```

---

### Task 3: Build and Verify the Production Artifact

**Files:**
- Verify: `org-tribe-view/src/views/statistics/indexLibrary.vue`
- Verify generated output: `org-tribe-view/dist/js/*.js`
- Verify: all files committed in Tasks 1 and 2

**Interfaces:**
- Consumes: restored component and corrected menu contract
- Produces: build evidence and a final scope audit; generated `dist` files remain uncommitted unless the repository already tracks them

- [ ] **Step 1: Run the focused regression and template checks**

```bash
cd org-tribe-view
node --test tests/legacyIndicatorQuery.test.mjs
node -e 'const fs=require("fs"); const c=require("vue-template-compiler"); const s=fs.readFileSync("src/views/statistics/indexLibrary.vue","utf8"); const p=c.parseComponent(s); const out=c.compile(p.template.content); if(out.errors.length){console.error(out.errors); process.exit(1)}'
```

Expected: both commands exit 0.

- [ ] **Step 2: Run the production frontend build**

```bash
cd org-tribe-view
npm run build
```

Expected: `DONE  Build complete` and exit code 0.

- [ ] **Step 3: Inspect the generated bundle for production markers**

```bash
cd org-tribe-view
rg -l '公共指标' dist/js | head
rg -l '我的指标' dist/js | head
rg -l 'selectIndexRelationTree' dist/js | head
```

Expected: every command prints at least one built JavaScript file.

- [ ] **Step 4: Audit scope and repository state**

```bash
git diff HEAD~2..HEAD --name-only
git status --short
git diff --check HEAD~2..HEAD
```

Expected: committed changes are limited to the approved test, statistics page, menu SQL, rollback SQL, and implementation record; pre-existing unrelated untracked files remain untouched.

- [ ] **Step 5: Record verification evidence**

In the completion response, report:

- focused test result;
- Vue template compilation result;
- production build result;
- generated marker lookup result;
- forward SQL path and rollback SQL path;
- explicit statement that the live database was not modified unless a real execution and readback occurred.
