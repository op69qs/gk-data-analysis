-- Vastbase migration script for ods procedures
-- Verified target syntax: MySQL-compatible CREATE PROCEDURE ... BEGIN ... END

CREATE SCHEMA IF NOT EXISTS ods;

DROP PROCEDURE IF EXISTS ods.p_pt_gy_files_temp;
CREATE PROCEDURE ods.p_pt_gy_files_temp()
AS
BEGIN
    TRUNCATE ods.pt_gy_files_temp;

    INSERT INTO ods.pt_gy_files_temp (
        uuid,
        pcuuid,
        path,
        size_kb,
        types,
        lrrq,
        old_path
    )
    SELECT
        a.uuid,
        a.pcuuid,
        CASE
            WHEN LEFT(a.path, 10) = 'group1/M01' THEN CONCAT('/usr/data/fdfs/storage/170/data2/data', SUBSTR(a.path, 11))
            WHEN LEFT(a.path, 10) = 'group1/M00' THEN CONCAT('/usr/data/fdfs/storage/170/data1/data', SUBSTR(a.path, 11))
            WHEN LEFT(a.path, 10) = 'group2/M01' THEN CONCAT('/usr/data/fdfs/storage/171/data2/data', SUBSTR(a.path, 11))
            WHEN LEFT(a.path, 10) = 'group2/M00' THEN CONCAT('/usr/data/fdfs/storage/171/data1/data', SUBSTR(a.path, 11))
        END AS path,
        a.size_kb,
        a.types,
        a.lrrq,
        a.path
    FROM ods.pt_gy_files a;

    TRUNCATE ods.pt_gy_files;

    INSERT INTO ods.pt_gy_files (
        uuid,
        pcuuid,
        path,
        size_kb,
        types,
        lrrq
    )
    SELECT
        a.uuid,
        a.pcuuid,
        a.path,
        a.size_kb,
        a.types,
        a.lrrq
    FROM ods.pt_gy_files_temp a;
END;
/
