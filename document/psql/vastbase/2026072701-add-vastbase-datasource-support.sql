BEGIN;

DO $migration$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'seo'
          AND table_name = 'seo_datasource_database'
          AND column_name = 'schema_name'
    ) THEN
        ALTER TABLE seo.seo_datasource_database
            ADD COLUMN schema_name varchar(100);
    END IF;
END
$migration$;

INSERT INTO seo.seo_datasource_enum (id, datasource, url, driverclass)
SELECT
    '4',
    'Vastbase',
    'jdbc:postgresql://ip:port/DBNAME?currentSchema=SCHEMA_NAME',
    'org.postgresql.Driver'
WHERE NOT EXISTS (
    SELECT 1
    FROM seo.seo_datasource_enum
    WHERE datasource = 'Vastbase'
);

COMMIT;

-- Verification:
-- SELECT column_name FROM information_schema.columns
--  WHERE table_schema = 'seo'
--    AND table_name = 'seo_datasource_database'
--    AND column_name = 'schema_name';
-- SELECT id, datasource, url, driverclass
--   FROM seo.seo_datasource_enum
--  WHERE datasource = 'Vastbase';
