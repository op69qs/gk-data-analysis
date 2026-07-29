# Fixed Report DATA_DATE Trailing-Space Compatibility Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Restore the onsite MySQL JAR's trailing-space comparison behavior for monthly and quarterly quick-report nationwide comparison queries on Vastbase.

**Architecture:** Keep the existing controller, service, period expression, row splitting, ordering, and fallback behavior unchanged. Add query-side `RTRIM` only around `TRS_BUDGET_INCOME_COMPARE.DATA_DATE`, protected by a resource-level regression test that requires all six predicates.

**Tech Stack:** Java 8, JUnit 4, MyBatis XML, Maven, Vastbase SQL

## Global Constraints

- Preserve `REPLACE('${params.ACCOUNT_PERIOD}', '-', '')` exactly.
- Modify only the six `DATA_DATE` predicates in the monthly and quarterly quick-report nationwide comparison queries.
- Do not modify `TRS_FINANCE_TAX_STATIS` or introduce cross-period fallback.
- Do not normalize or update production data.

---

### Task 1: Lock the onsite-JAR comparison contract

**Files:**
- Create: `fixedReport/src/test/java/org/fixedReport/mapper/FixedReportDataDateCompatibilityTest.java`
- Test: `fixedReport/src/test/java/org/fixedReport/mapper/FixedReportDataDateCompatibilityTest.java`

**Interfaces:**
- Consumes: classpath resources `mybatis/fixedReport/NewsFlashMapper.xml` and `mybatis/fixedReport/NewsFlashQuarterMapper.xml`
- Produces: a JUnit regression test that requires two monthly and four quarterly `RTRIM(DATA_DATE)` predicates

- [ ] **Step 1: Write the failing resource-level regression test**

```java
package org.fixedReport.mapper;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FixedReportDataDateCompatibilityTest {
    @Test
    public void nationwideComparisonTrimsStoredDataDateAtAllCallSites() throws Exception {
        String monthly = read("mybatis/fixedReport/NewsFlashMapper.xml");
        String quarterly = read("mybatis/fixedReport/NewsFlashQuarterMapper.xml");

        assertEquals(2, occurrences(monthly, "RTRIM(src.DATA_DATE) ="));
        assertEquals(4, occurrences(quarterly, "RTRIM(a.DATA_DATE) ="));
        assertFalse(monthly.contains("AND src.DATA_DATE =REPLACE"));
        assertFalse(quarterly.contains("AND a.DATA_DATE =REPLACE"));
    }

    private static String read(String resource) throws Exception {
        try (InputStream input = FixedReportDataDateCompatibilityTest.class
                .getClassLoader().getResourceAsStream(resource)) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            return new String(output.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    private static int occurrences(String value, String token) {
        int count = 0;
        for (int index = 0; (index = value.indexOf(token, index)) >= 0; index += token.length()) {
            count++;
        }
        return count;
    }
}
```

- [ ] **Step 2: Run the test and verify RED**

Run: `mvn -pl fixedReport -Dtest=FixedReportDataDateCompatibilityTest test`

Expected: FAIL because both expected occurrence counts are zero before the Mapper change.

### Task 2: Apply the minimal Mapper change

**Files:**
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashMapper.xml:2339`
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashMapper.xml:2358`
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml:1493`
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml:1509`
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml:1547`
- Modify: `fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml:1564`
- Test: `fixedReport/src/test/java/org/fixedReport/mapper/FixedReportDataDateCompatibilityTest.java`

**Interfaces:**
- Consumes: the same report `ACCOUNT_PERIOD` values supplied by the onsite JAR controller
- Produces: monthly and quarterly Mapper SQL that matches padded MySQL source values on Vastbase

- [ ] **Step 1: Wrap each stored `DATA_DATE` alias with `RTRIM`**

```xml
AND RTRIM(src.DATA_DATE) =REPLACE('${params.ACCOUNT_PERIOD}','-','')
AND RTRIM(a.DATA_DATE) =REPLACE('${params.ACCOUNT_PERIOD}','-','')
```

Use the lowercase `replace` spelling already present in each right-side query; change only the left operand.

- [ ] **Step 2: Run the focused test and verify GREEN**

Run: `mvn -pl fixedReport -Dtest=FixedReportDataDateCompatibilityTest test`

Expected: PASS with two monthly and four quarterly trimmed predicates.

- [ ] **Step 3: Run existing fixed-report tests**

Run: `mvn -pl fixedReport test`

Expected: PASS.

- [ ] **Step 4: Verify against imported production data**

Run the equivalent read-only Vastbase query for `2022Q4`.

Expected: 31 matched rows, split into 16 rows with `ROWS_ID < 34` and 15 rows with `ROWS_ID > 33`.

- [ ] **Step 5: Review and commit the atomic fix**

```bash
git add fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashMapper.xml \
  fixedReport/src/main/resources/mybatis/fixedReport/NewsFlashQuarterMapper.xml \
  fixedReport/src/test/java/org/fixedReport/mapper/FixedReportDataDateCompatibilityTest.java \
  docs/superpowers/plans/2026-07-28-fixed-report-data-date-trailing-space.md
git commit -m "fix: preserve MySQL report period matching"
```
