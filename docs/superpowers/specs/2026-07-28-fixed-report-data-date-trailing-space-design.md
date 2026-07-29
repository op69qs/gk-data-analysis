# Fixed Report DATA_DATE Trailing-Space Compatibility Design

## Context

The onsite `fixedReport-2.1.0.jar` uses exact equality between
`TRS_BUDGET_INCOME_COMPARE.DATA_DATE` and the requested report period. In the
onsite MySQL behavior, padded values such as `2022Q4    ` match `2022Q4`.
After migration, Vastbase stores `DATA_DATE` as `text` and exact equality does
not ignore the trailing spaces. The imported production data proves the
difference: exact equality returns zero rows while `RTRIM(DATA_DATE)` returns
all 31 rows.

## Scope

- Preserve the onsite JAR period expression:
  `REPLACE('${params.ACCOUNT_PERIOD}', '-', '')`.
- Apply `RTRIM` only to the stored `DATA_DATE` value.
- Cover both halves of the monthly quick-report nationwide comparison query.
- Cover both halves of the quarterly quick-report primary query and both halves
  of its template-completion fallback query.
- Do not change `TRS_FINANCE_TAX_STATIS`; its missing `2022Q4` period is a
  separate data-coverage fact and is not a string-compatibility defect.

## Considered Approaches

1. Query-side `RTRIM(DATA_DATE)` (selected): restores MySQL trailing-space
   equality semantics without changing stored production data or report-period
   interpretation.
2. Normalize all imported `DATA_DATE` values: rejected because it mutates 3,658
   historical rows and makes the application depend on a one-time cleanup.
3. Cast to a fixed-width character type: rejected because it depends on an
   arbitrary width and obscures the intended comparison rule.

## Implementation

Change the six predicates from:

```sql
a.DATA_DATE = REPLACE('${params.ACCOUNT_PERIOD}', '-', '')
```

to the equivalent Vastbase form:

```sql
RTRIM(a.DATA_DATE) = REPLACE('${params.ACCOUNT_PERIOD}', '-', '')
```

Use the existing aliases (`src` or `a`) at each call site. No controller,
service, response-shape, ordering, row-splitting, or fallback behavior changes.

## Verification

- A regression test must fail while any of the six untrimmed predicates remain.
- The test must pass only when all six use `RTRIM` and retain the original
  `REPLACE` period expression.
- Direct Vastbase verification for `2022Q4` must return 31 rows: 16 left-side
  rows and 15 right-side rows.
- Existing fixed-report tests must still pass.
