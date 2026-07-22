#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
mapper="${repo_root}/indicatorsLibv-1.0/src/main/resources/mybatis/indicatorsLib/IndexRelationMapper.xml"
sql_builder="${repo_root}/indicatorsLibv-1.0/src/main/java/org/indicatorsLib/util/CreateSchemeSQL.java"

metadata_query="$(sed -n '/<select id="getIndicatorsTableName"/,/<\/select>/p' "$mapper")"
active_helper_sql="$(sed -n '1,/private Map getSchemeSql_2/p' "$sql_builder")"
scheme_sql="$(sed -n '/private Map getSchemeSql_2/,/public String getSchemeSQL/p' "$sql_builder")"

for alias in colId tableName type; do
  if ! grep -Fq "AS \"${alias}\"" <<<"$metadata_query"; then
    echo "FAIL: getIndicatorsTableName must preserve map key ${alias}" >&2
    exit 1
  fi
done

if grep -Fq 'indicators_lib.`' <<<"$active_helper_sql"; then
  echo 'FAIL: active Vastbase SQL still contains MySQL backtick identifiers' >&2
  exit 1
fi

for alias in START_DATE END_DATE dimCode dimDescr; do
  if ! grep -Fq "AS \\\"${alias}\\\"" <<<"$active_helper_sql"; then
    echo "FAIL: generated SQL must preserve result key ${alias}" >&2
    exit 1
  fi
done

if ! grep -Fq 'GROUP BY V1.\"dimCode\",V1.\"dimDescr\"' <<<"$active_helper_sql"; then
  echo 'FAIL: dimension SQL must group every selected non-aggregate column' >&2
  exit 1
fi

if grep -Fq '"aa.COLID,\n" +' <<<"$scheme_sql"; then
  echo 'FAIL: pivot discriminator COLID must not be selected outside its aggregate' >&2
  exit 1
fi

for grouped_column in ACCOUNT_DATE ACCOUNT_PERIOD CODE; do
  if [[ "$(grep -Fc "aa.${grouped_column},\\n" <<<"$scheme_sql")" -lt 2 ]]; then
    echo "FAIL: generated query must group selected column aa.${grouped_column}" >&2
    exit 1
  fi
done

if [[ "$(grep -Fc 'aa.GK' <<<"$scheme_sql")" -lt 3 ]]; then
  echo 'FAIL: generated query must group selected column aa.GK' >&2
  exit 1
fi

echo 'PASS: indicatorsLib Vastbase query aliases and identifiers are compatible'
