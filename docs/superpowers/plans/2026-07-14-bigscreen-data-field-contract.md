# Bigscreen Data Field Contract Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore every big-screen query-data field contract to the behavior expected by the original `dwbi_vis_screen` frontend and controller, while making the SQL valid and case-stable on Vastbase.

**Architecture:** Treat the original controller's `Map.get(...)` keys and frontend response reads as the contract. Encode database-result casing explicitly in MyBatis SQL aliases, keep REST response names unchanged, remove the temporary case-insensitive controller fallback, and use a focused Mapper contract test plus live scheme `3333` as regression gates.

**Tech Stack:** Java 8, Spring Boot, MyBatis XML, JUnit 4, Vastbase/PostgreSQL SQL, Vue 2, ECharts

## Global Constraints

- Use `/root/work-project/project-02/dwbi_vis_screen` as the source-of-truth implementation.
- Preserve source REST fields such as `data`, `x`, `amount`, `name`, and `value`.
- Use quoted SQL aliases such as `AS "DACCT"` when the controller reads uppercase Map keys.
- Preserve lowercase SQL result keys when the source controller reads lowercase keys.
- Do not add controller-side case-insensitive fallback behavior.
- Do not modify database rows.
- Preserve unrelated existing changes in the dirty workspace.

---

### Task 1: Encode the Mapper field contract as a failing test

**Files:**
- Create: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/QueryDataMapperContractTest.java`
- Read: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml`

**Interfaces:**
- Consumes: MyBatis `<select id="...">` definitions in `QueryDataMapper.xml`.
- Produces: a JUnit contract gate that requires the exact SQL result-key casing and annual-ordering expressions used by the source controller.

- [x] **Step 1: Write the failing Mapper contract test**

Create a JUnit 4 test that loads the Mapper XML as a classpath resource, extracts a `<select>` body by ID, and checks these concrete contracts:

```java
package org.jeecg.modules.visualScreen.mapper;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueryDataMapperContractTest {
    private String mapperXml;

    @Before
    public void loadMapper() throws Exception {
        String resource = "org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml";
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        assertTrue("Mapper resource must exist", input != null);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        mapperXml = new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private String select(String id) {
        Pattern pattern = Pattern.compile("<select\\s+id=\\\"" + Pattern.quote(id)
                + "\\\"[\\s\\S]*?</select>", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(mapperXml);
        assertTrue("Missing select " + id, matcher.find());
        return matcher.group();
    }

    private void assertUppercaseSeriesFields(String id) {
        String sql = select(id);
        assertTrue(id + " must expose DACCT", sql.contains("AS \"DACCT\""));
        assertTrue(id + " must expose INDEX_VALUE", sql.contains("AS \"INDEX_VALUE\""));
    }

    @Test
    public void controllerUppercaseKeysHaveQuotedAliases() {
        String[] queries = {
                "getTransferIncome", "getTransferIncomeRate",
                "getLocalFinancial", "getLocalFinancialRate",
                "getLandTransfer", "getLandTransferRate",
                "getPublicBudget", "getPublicBudgetRate",
                "getThreeBudget", "getThreeBudgetRate",
                "getTaxRevenue", "getTaxRevenueRate",
                "getIncomePayoutGap", "getIndustryTax",
                "getCustomsRevenue", "getCustomsRevenueRate",
                "getCustomsNonTax", "getCustomsNonTaxRate",
                "getCustomsImportDuties", "getCustomsImportDutiesRate",
                "getCustomsImportVat", "getCustomsImportVatRate",
                "getImportDutyArticles", "getImportDutyArticlesRate",
                "getIndustryMain", "getTreasuryIndex",
                "getInventoryBalance", "getInventoryForm",
                "getCustomsIncomeSituationTb", "getCustomsIncomeSituation"
        };
        for (String query : queries) {
            assertUppercaseSeriesFields(query);
        }
        String structure = select("getStructure");
        assertTrue(structure.contains("AS \"INDEX_NAME\""));
        assertTrue(structure.contains("AS \"INDEX_VALUE\""));
    }

    @Test
    public void sourceLowercaseKeysRemainLowercase() {
        String[] queries = {
                "getMunicipalitiesDirectly", "getIndustryType", "getIndustryName",
                "getIndustryTop10", "getSubjectPay", "getSubjectPaySub",
                "getPurposePay", "getAccountToGuoku", "getGuokuToAccount",
                "getGuokuToOrg", "getOrgToGuoku"
        };
        for (String query : queries) {
            String sql = select(query);
            assertTrue(query + " must expose lowercase name", sql.contains("AS \"name\""));
            assertTrue(query + " must expose lowercase value", sql.contains("AS \"value\""));
            assertFalse(query + " must not change REST item keys", sql.contains("AS \"NAME\""));
        }
    }

    @Test
    public void annualDistinctQueriesOrderByTheirSelectedYear() {
        for (String query : new String[]{"getTransferIncomeRate", "getLocalFinancialRate",
                "getLandTransferRate", "getThreeBudgetRate", "getTaxRevenueRate"}) {
            String sql = select(query);
            assertTrue(query + " must order annual rows by selected year",
                    sql.contains("ORDER BY LEFT(a.DACCT, 4)"));
            assertTrue(query + " must retain non-annual full-date ordering",
                    sql.contains("ORDER BY a.DACCT"));
        }
    }

    @Test
    public void groupedIndustryNameOrdersByItsGroupedColumn() {
        String sql = select("getIndustryName");
        assertTrue(sql.contains("GROUP BY a.industrial_name"));
        assertTrue(sql.contains("ORDER BY a.industrial_name"));
        assertFalse(sql.contains("ORDER BY a.industrial_type"));
    }
}
```

- [x] **Step 2: Run the focused test and verify RED**

Run:

```bash
mvn -pl jeecg-boot-module-system -am -DskipTests=false -Dtest=QueryDataMapperContractTest test
```

from `vis-screen-backend`. Expected: FAIL on the first currently unquoted `DACCT`/`INDEX_VALUE` query or missing annual `LEFT(a.DACCT, 4)` ordering.

- [x] **Step 3: Commit only the failing test**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/QueryDataMapperContractTest.java
git commit -m "test: define bigscreen query field contracts"
```

### Task 2: Make Vastbase query results match the source controller contract

**Files:**
- Modify: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml`
- Test: `vis-screen-backend/jeecg-boot-module-system/src/test/java/org/jeecg/modules/visualScreen/mapper/QueryDataMapperContractTest.java`

**Interfaces:**
- Consumes: source controller keys `DACCT`, `INDEX_NAME`, `INDEX_VALUE`, `GROWTH_INDEX_VALUE`, plus lowercase `name` and `value`.
- Produces: Vastbase result Maps with exactly those keys.

- [x] **Step 1: Add explicit aliases for uppercase controller keys**

For every query listed by `controllerUppercaseKeysHaveQuotedAliases`, use this shape:

```xml
SELECT DISTINCT
a.DACCT AS "DACCT",
<include refid="sumIndexExpr"/> AS "INDEX_VALUE"
```

Keep the already-correct inventory shape:

```xml
a.DACCT AS "DACCT",
<include refid="sumIndexExpr"/> AS "INDEX_VALUE",
<include refid="sumGrowthRateExpr"/> AS "GROWTH_INDEX_VALUE"
```

Also quote non-series uppercase keys read directly by the source controller:

```xml
a.area_dscr AS "INDEX_NAME"
COALESCE(...) AS "BUDGET_COMPLETE"
COALESCE(...) AS "BUDGET_NOT_COMPLETE"
ROUND(...) AS "BUDGET_RATE"
SUBJECT_DSCR AS "SUBJECT_DSCR"
ROUND(...) AS "INDEX_VALUE_COMPLETE"
ROUND(...) AS "INDEX_VALUE_NOT_COMPLETE"
ROUND(...) AS "RATE"
```

- [x] **Step 2: Preserve explicit lowercase aliases where the source reads lowercase keys**

Use quoted lowercase aliases so Vastbase cannot change the contract:

```xml
a.AREA_DSCR AS "name",
<include refid="sumIndexExpr"/> AS "value"
```

Apply the same `AS "name"` / `AS "value"` shape to the industry, subject-pay, purpose-pay, and account/treasury relationship queries listed by `sourceLowercaseKeysRemainLowercase`.

- [x] **Step 3: Fix annual DISTINCT ordering**

Replace unconditional `ORDER BY a.DACCT` in the five annual-capable rate queries with matching conditional expressions:

```xml
<if test='params.PERIOD_FLAG != "4"'>ORDER BY a.DACCT</if>
<if test='params.PERIOD_FLAG == "4"'>ORDER BY LEFT(a.DACCT, 4)</if>
```

The annual `SELECT`, `GROUP BY`, and `ORDER BY` expressions must all be `LEFT(a.DACCT, 4)`.

Also change `getIndustryName` to `ORDER BY a.industrial_name`, matching its selected and grouped field; ordering by `a.industrial_type` is not valid Vastbase grouped-query SQL.

- [x] **Step 4: Run the focused contract test and verify GREEN**

Run the Task 1 Maven command again. Expected: `QueryDataMapperContractTest` passes with zero failures.

- [x] **Step 5: Commit the Mapper contract fix**

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml
git commit -m "fix: preserve bigscreen query field casing"
```

### Task 3: Remove the case-insensitive fallback and restore exact source reads

**Files:**
- Modify: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/QueryDataController.java`
- Delete: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/util/MapKeyUtil.java`

**Interfaces:**
- Consumes: exact Mapper keys established by Task 2.
- Produces: the original source REST response contract without a hidden casing fallback.

- [x] **Step 1: Restore direct Map reads from `dwbi_vis_screen`**

Remove the `MapKeyUtil` import and replace all temporary reads with the original source forms:

```java
x.add(a.get("DACCT") + "");
y1.add(Double.parseDouble(a.get("INDEX_VALUE") + ""));
y2.add(Double.parseDouble(a.get("GROWTH_INDEX_VALUE") + ""));
```

For two- and three-series helpers, use the same direct `INDEX_VALUE` reads for every series. For `getAmountAndRate`, keep response keys `x`, `amount`, and `rate` unchanged.

- [x] **Step 2: Delete the now-unused compatibility utility**

Delete `MapKeyUtil.java` after confirming no remaining references with:

```bash
rg -n "MapKeyUtil" vis-screen-backend/jeecg-boot-module-system/src
```

Expected: no matches.

- [x] **Step 3: Run the Mapper test and compile the backend module**

Run:

```bash
mvn -pl jeecg-boot-module-system -am -DskipTests=false -Dtest=QueryDataMapperContractTest test
mvn -pl jeecg-boot-module-system -am -DskipTests package
```

Expected: test and package both exit 0.

- [x] **Step 4: Commit the controller cleanup** *(no controller diff remained after restoring source reads)*

```bash
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/controller/QueryDataController.java
git add vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/util/MapKeyUtil.java
git commit -m "refactor: enforce exact bigscreen query keys"
```

### Task 4: Verify the complete migrated big-screen behavior

**Files:**
- Verify: `org-tribe-view/src/views/vis/bigscreen/BigScreenPreview.vue`
- Verify: `org-tribe-view/src/views/vis/bigscreen/modules/BigScreenTabTemplate.vue`
- Verify: `vis-screen-backend/jeecg-boot-module-system/src/main/java/org/jeecg/modules/visualScreen/mapper/xml/QueryDataMapper.xml`

**Interfaces:**
- Consumes: scheme ID `2fcab29baf8c47cfa2150a515de41651` and its five `queryData/*` requests.
- Produces: two visible pages with populated charts and no query failures.

- [x] **Step 1: Run existing frontend regression test**

Run from `org-tribe-view` with project Node 14:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH node tests/visCarousel.test.mjs
```

Expected: `visCarousel interval tests passed`.

- [x] **Step 2: Build the frontend production bundle**

Run:

```bash
PATH=/root/.local/share/fnm/node-versions/v14.21.3/installation/bin:$PATH npm run build
```

Expected: Vue production build exits 0; existing CSS-order and asset-size warnings may remain.

- [ ] **Step 3: Deploy/restart the backend instance used for browser verification**

Copy the freshly built frontend through the existing Maven packaging flow, restart the `org-tribe-system` service used by the test environment, and confirm the updated `QueryDataMapper.xml` is present under `target/classes`.

- [ ] **Step 4: Verify scheme 3333 through the portal**

From `http://cui02:3000`, log in with the supplied test user, enter `GK_DATA_ANALYSIS`, open scheme `3333`, and verify:

- exactly two carousel pages;
- `getStructure`, `getThreeBudget`, `getTaxRevenue`, `getSubjectPay`, and `getPurposePay` all return HTTP 200 with `result: "success"`;
- no response reports SQL grammar errors;
- every configured data chart shows non-empty data;
- right arrow and `ArrowLeft` switch pages without losing chart data;
- autoplay uses at least 5000ms;
- browser console has no errors caused by this change.

- [ ] **Step 5: Review scoped changes and record the environment boundary**

Run:

```bash
git status --short
git diff --check
```

Expected: no whitespace errors. If the browser still points to a developer-local `localhost:9090` that does not load this workspace's build, record that deployment boundary explicitly rather than claiming the live fix is verified.
