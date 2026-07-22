#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
mapper="${repo_root}/indicatorsLibv-1.0/src/main/resources/mybatis/indicatorsLib/IndexRelationMapper.xml"
sql_builder="${repo_root}/indicatorsLibv-1.0/src/main/java/org/indicatorsLib/util/CreateSchemeSQL.java"
controller="${repo_root}/indicatorsLibv-1.0/src/main/java/org/indicatorsLib/controller/IndexRelationController.java"
service="${repo_root}/indicatorsLibv-1.0/src/main/java/org/indicatorsLib/service/impl/IndexRelationServiceImpl.java"

metadata_query="$(sed -n '/<select id="getIndicatorsTableName"/,/<\/select>/p' "$mapper")"
active_helper_sql="$(sed -n '1,/private Map getSchemeSql_2/p' "$sql_builder")"
scheme_sql="$(sed -n '/private Map getSchemeSql_2/,/public String getSchemeSQL/p' "$sql_builder")"

for alias in COLID TABLENAME TYPE; do
  if ! grep -Fq "AS \"${alias}\"" <<<"$metadata_query"; then
    echo "FAIL: getIndicatorsTableName must expose quoted uppercase key ${alias}" >&2
    exit 1
  fi
done

if grep -Fq 'indicators_lib.`' <<<"$active_helper_sql"; then
  echo 'FAIL: active Vastbase SQL still contains MySQL backtick identifiers' >&2
  exit 1
fi

for alias in START_DATE END_DATE DIMCODE DIMDESCR; do
  if ! grep -Fq "AS \\\"${alias}\\\"" <<<"$active_helper_sql"; then
    echo "FAIL: generated SQL must preserve result key ${alias}" >&2
    exit 1
  fi
done

if ! grep -Fq 'GROUP BY V1.\"DIMCODE\",V1.\"DIMDESCR\"' <<<"$active_helper_sql"; then
  echo 'FAIL: dimension SQL must group every selected non-aggregate column' >&2
  exit 1
fi

for alias in ACCOUNT_DATE ACCOUNT_PERIOD CODE GK COLID VALUE; do
  if ! grep -Fq "AS \\\"${alias}\\\"" <<<"$scheme_sql"; then
    echo "FAIL: generated indicator SQL must explicitly expose quoted uppercase alias ${alias}" >&2
    exit 1
  fi
done

if grep -Eq 'aa\.(ACCOUNT_DATE|ACCOUNT_PERIOD|CODE|GK)(,|\\n)' <<<"$scheme_sql"; then
  echo 'FAIL: outer indicator columns must have explicit quoted uppercase aliases' >&2
  exit 1
fi

if grep -Fq '"aa.COLID,\n" +' <<<"$scheme_sql"; then
  echo 'FAIL: pivot discriminator COLID must not be selected outside its aggregate' >&2
  exit 1
fi

for grouped_column in ACCOUNT_DATE ACCOUNT_PERIOD CODE; do
  if [[ "$(grep -Fc "aa.\\\"${grouped_column}\\\",\\n" <<<"$scheme_sql")" -lt 2 ]]; then
    echo "FAIL: generated query must group selected column aa.${grouped_column}" >&2
    exit 1
  fi
done

if [[ "$(grep -Fc 'aa.\"GK\"' <<<"$scheme_sql")" -lt 3 ]]; then
  echo 'FAIL: generated query must group selected column aa.GK' >&2
  exit 1
fi

if grep -Fq 'normalizeIndicatorRows' "$service"; then
  echo 'FAIL: response keys must come from SQL aliases, not Java map rewriting' >&2
  exit 1
fi

if ! grep -Fq 'V.\"ACCOUNT_DATE\" AS \"ACCOUNT_DATE\"' "$controller"; then
  echo 'FAIL: detection-value projection must preserve quoted uppercase fixed columns' >&2
  exit 1
fi

for reference in 'C.\"ACCOUNT_PERIOD\"' 'C.\"CODE\"' 'C.\"GK\"'; do
  if ! grep -Fq "$reference" "$service"; then
    echo "FAIL: chart SQL must reference quoted derived column ${reference}" >&2
    exit 1
  fi
done

echo 'PASS: indicatorsLib Vastbase query aliases and identifiers are compatible'
