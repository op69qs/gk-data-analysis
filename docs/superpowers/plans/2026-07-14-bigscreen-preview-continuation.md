# Bigscreen Preview Continuation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Finish the existing big-screen preview migration by enforcing a readable 5000ms minimum carousel interval without disturbing the verified two-page rendering path.

**Architecture:** Keep the migrated source components and API contract unchanged. Centralize interval normalization in `src/utils/visCarousel.js`, align the scheme editor constraint, and verify the complete behavior through a focused Node test, a production build, and the live browser.

**Tech Stack:** Vue 2, Element UI carousel, Ant Design Vue, Node 14 native assertions, Spring/MyBatis backend (read-only for this change)

## Global Constraints

- Preserve all pre-existing uncommitted migration work.
- Do not change SQL aliases unless live API evidence shows a field mismatch.
- Keep interval values in milliseconds and enforce a 5000ms minimum.
- Do not mutate production or test database rows.

---

### Task 1: Carousel interval contract

**Files:**
- Create: `org-tribe-view/tests/visCarousel.test.mjs`
- Modify: `org-tribe-view/src/utils/visCarousel.js`
- Modify: `org-tribe-view/src/views/vis/modules/ExhibitionSchemeListModal.vue`

**Interfaces:**
- Consumes: route/query value accepted by `resolveVisCarouselInterval(raw)`.
- Produces: a finite millisecond interval of at least 5000.

- [x] **Step 1: Write the failing test**

```js
assert.equal(resolveVisCarouselInterval(1000), 5000)
assert.equal(resolveVisCarouselInterval(5), 5000)
assert.equal(resolveVisCarouselInterval(5000), 5000)
assert.equal(resolveVisCarouselInterval(8000), 8000)
```

- [x] **Step 2: Run test to verify it fails**

Run: `node tests/visCarousel.test.mjs`

Expected: FAIL because the current helper returns 1000 for an input of 1000.

- [x] **Step 3: Implement the minimum interval**

```js
var MINIMUM_MS = 5000
var milliseconds = n < 100 ? n * 1000 : n
return Math.max(milliseconds, MINIMUM_MS)
```

Change the scheme editor input from `:min="1000"` to `:min="5000"`.

- [x] **Step 4: Run test to verify it passes**

Run: `node tests/visCarousel.test.mjs`

Expected: all interval cases PASS.

### Task 2: Integrated verification

**Files:**
- Verify: `org-tribe-view/src/views/vis/bigscreen/BigScreenPreview.vue`
- Verify: `org-tribe-view/src/views/vis/bigscreen/modules/BigScreenTabTemplate.vue`

**Interfaces:**
- Consumes: `/vis/api/schemeInfo/getAllPage` response for scheme `2fcab29baf8c47cfa2150a515de41651`.
- Produces: two rendered carousel pages with pointer and keyboard navigation and a 5000ms automatic interval.

- [x] **Step 1: Build the front end**

Run: `npm run build`

Expected: Vue production build exits successfully.

- [x] **Step 2: Verify the live page**

Open 3333 from `http://cui02:3000` through the portal welcome page. Confirm two carousel items, right-arrow navigation to page 2, ArrowLeft navigation to page 1, and `el-carousel.__vue__.interval === 5000` with autoplay enabled.

- [x] **Step 3: Review scoped diff**

Run: `git diff -- org-tribe-view/src/utils/visCarousel.js org-tribe-view/src/views/vis/modules/ExhibitionSchemeListModal.vue org-tribe-view/tests/visCarousel.test.mjs`

Expected: only the interval contract, form constraint, and focused regression test changed in this continuation.

Verification note: these continuation files are currently untracked within a pre-existing dirty workspace, so the scoped review was performed directly against their contents rather than relying on `git diff` output.
