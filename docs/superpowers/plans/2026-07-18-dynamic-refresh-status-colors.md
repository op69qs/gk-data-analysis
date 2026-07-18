# Dynamic Refresh Status Colors Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make failed dynamic-refresh statuses red and successful statuses green in both status tables.

**Architecture:** Keep the existing per-component `statusMeta` mappings and replace only the two unsupported semantic color values with Ant Design Vue preset color names. No shared abstraction or CSS override is introduced.

**Tech Stack:** Vue 2, Ant Design Vue

## Global Constraints

- Change both the main task list and the run-history modal.
- Use `red` for status `500` and `green` for status `200`.
- Do not change status text or business behavior.
- Per user instruction, do not add tests, run tests, or compile the project.

---

### Task 1: Update Status Tag Colors

**Files:**
- Modify: `org-tribe-view/src/views/manualCallReport/manualCallReportList.vue`
- Modify: `org-tribe-view/src/views/manualCallReport/modules/runHistoryModal.vue`

**Interfaces:**
- Consumes: Existing `statusMeta(status)` methods used by `<a-tag :color>`.
- Produces: Existing mappings with Ant Design Vue preset colors `green` and `red`.

- [ ] **Step 1: Update the main-list mapping**

Replace:

```javascript
'200': { text: '成功', color: 'success' },
'500': { text: '失败', color: 'error' }
```

with:

```javascript
'200': { text: '成功', color: 'green' },
'500': { text: '失败', color: 'red' }
```

- [ ] **Step 2: Update the run-history mapping**

Apply the same replacement in `runHistoryModal.vue`.

- [ ] **Step 3: Review the scoped diff**

Run:

```bash
git diff -- org-tribe-view/src/views/manualCallReport/manualCallReportList.vue \
  org-tribe-view/src/views/manualCallReport/modules/runHistoryModal.vue
```

Expected: exactly four color-token replacements and no other source changes.

- [ ] **Step 4: Commit**

```bash
git add org-tribe-view/src/views/manualCallReport/manualCallReportList.vue \
  org-tribe-view/src/views/manualCallReport/modules/runHistoryModal.vue
git commit -m "fix: use explicit dynamic refresh status colors"
```
