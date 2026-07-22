CREATE OR REPLACE FUNCTION pg_temp.to_vastbase_indicator_scheme_sql(source_sql TEXT)
RETURNS TEXT
LANGUAGE plpgsql
AS $$
DECLARE
    converted_sql TEXT := source_sql;
BEGIN
    IF converted_sql IS NULL OR converted_sql = '' THEN
        RETURN converted_sql;
    END IF;

    IF converted_sql NOT ILIKE '%IFNULL(%'
       AND converted_sql NOT ILIKE '%SUM(IF(%'
       AND position(chr(96) IN converted_sql) = 0 THEN
        RETURN converted_sql;
    END IF;

    converted_sql := replace(converted_sql, chr(96) || 'jeecg-boot-os' || chr(96), '"jeecg-boot-os"');
    converted_sql := replace(converted_sql, chr(96), '');

    converted_sql := regexp_replace(
        converted_sql,
        $pattern$IFNULL\(FORMAT\(SUM\(IF\(aa\.COLID\s*=\s*'([^']+)'\s*,\s*VALUE\s*,\s*NULL\)\),\s*2\),\s*''\)\s+AS\s+'([^']+)'$pattern$,
        $replacement$COALESCE(CAST(ROUND(SUM(CASE WHEN aa."COLID" = '\1' THEN aa."VALUE" END),2) AS TEXT),'') AS "\2"$replacement$,
        'gi'
    );

    converted_sql := regexp_replace(
        converted_sql,
        $pattern$aa\.COLID\s*,\s*aa\.ACCOUNT_DATE\s*,\s*aa\.ACCOUNT_PERIOD\s*,\s*aa\.CODE\s*,\s*aa\.GK$pattern$,
        'aa."ACCOUNT_DATE" AS "ACCOUNT_DATE", aa."ACCOUNT_PERIOD" AS "ACCOUNT_PERIOD", aa."CODE" AS "CODE", aa."GK" AS "GK"',
        'gi'
    );

    converted_sql := replace(converted_sql, ' AS COLID', ' AS "COLID"');
    converted_sql := replace(converted_sql, ' AS ACCOUNT_DATE', ' AS "ACCOUNT_DATE"');
    converted_sql := replace(converted_sql, ' AS ACCOUNT_PERIOD', ' AS "ACCOUNT_PERIOD"');
    converted_sql := replace(converted_sql, ' AS CODE', ' AS "CODE"');
    converted_sql := replace(converted_sql, ' AS GK', ' AS "GK"');
    converted_sql := replace(converted_sql, ' AS VALUE', ' AS "VALUE"');

    converted_sql := regexp_replace(
        converted_sql,
        $pattern$aa[.](COLID|ACCOUNT_DATE|ACCOUNT_PERIOD|CODE|GK|VALUE)$pattern$,
        $replacement$aa."\1"$replacement$,
        'gi'
    );
    converted_sql := regexp_replace(
        converted_sql,
        $pattern$V[.]([a-zA-Z0-9_]+)$pattern$,
        $replacement$V."\1"$replacement$,
        'g'
    );

    converted_sql := regexp_replace(
        converted_sql,
        $pattern$GROUP\s+BY\s+aa\."ACCOUNT_PERIOD"\s*,\s*aa\."GK"$pattern$,
        'GROUP BY aa."ACCOUNT_DATE", aa."ACCOUNT_PERIOD", aa."CODE", aa."GK"',
        'gi'
    );

    converted_sql := replace(
        converted_sql,
        'CONVERT(REPLACE(ACCOUNT_PERIOD,''Q'',''0''), SIGNED)',
        'CAST(REPLACE(ACCOUNT_PERIOD,''Q'',''0'') AS NUMERIC)'
    );
    converted_sql := replace(
        converted_sql,
        'STR_TO_DATE(ACCOUNT_PERIOD, ''%Y-%m-%d'')',
        'TO_DATE(ACCOUNT_PERIOD, ''YYYY-MM-DD'')'
    );
    converted_sql := regexp_replace(
        converted_sql,
        $pattern$STR_TO_DATE\('([^']+)'\s*,\s*'%Y-%m-%d'\)$pattern$,
        $replacement$TO_DATE('\1', 'YYYY-MM-DD')$replacement$,
        'gi'
    );

    IF converted_sql ILIKE '%IFNULL(%'
       OR converted_sql ILIKE '%SUM(IF(%'
       OR position(chr(96) IN converted_sql) > 0
       OR converted_sql LIKE '%aa.COLID,%' THEN
        RAISE EXCEPTION 'legacy SQL token remains after conversion';
    END IF;

    RETURN converted_sql;
END
$$;
