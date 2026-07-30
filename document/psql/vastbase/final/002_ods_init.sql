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

DROP PROCEDURE IF EXISTS ods.p_trs_info_enterprises_temporary;
CREATE PROCEDURE ods.p_trs_info_enterprises_temporary()
AS
BEGIN
    TRUNCATE ods.trs_info_enterprises;

    INSERT INTO ods.trs_info_enterprises
    SELECT DISTINCT ON (entname, uniscid) *
    FROM ods.trs_info_enterprises_temporary
    ORDER BY entname, uniscid, datauptime DESC;
END;
/

DROP PROCEDURE IF EXISTS ods.p_trs_info_nonbusiness_temporary;
CREATE PROCEDURE ods.p_trs_info_nonbusiness_temporary()
AS
BEGIN
    TRUNCATE ods.trs_info_nonbusiness;

    INSERT INTO ods.trs_info_nonbusiness (
        sorg_type,
        cn_name,
        unified_code,
        name,
        card_type,
        id_card,
        residence,
        reg_mon,
        business,
        borg_name,
        sign_date,
        sign_end_date,
        morg_name,
        act_area,
        sorg_status,
        link_man,
        link_phone
    )
    SELECT
        sorg_type,
        cn_name,
        unified_code,
        name,
        card_type,
        id_card,
        residence,
        reg_mon,
        business,
        borg_name,
        sign_date,
        sign_end_date,
        morg_name,
        act_area,
        sorg_status,
        link_man,
        link_phone
    FROM ods.trs_info_nonbusiness_temporary;
END;
/

DROP PROCEDURE IF EXISTS ods.p_trs_info_practice_cancel_temporary;
CREATE PROCEDURE ods.p_trs_info_practice_cancel_temporary()
AS
BEGIN
    TRUNCATE ods.trs_info_practice_cancel;

    INSERT INTO ods.trs_info_practice_cancel (
        id,
        bbh,
        sjbbm,
        sjblx,
        dwdm,
        dwmc,
        infoactiontype,
        infodate,
        pripid,
        uniscid,
        regno,
        traname,
        compform,
        apprdate,
        regorg,
        notno,
        estdate,
        enttype,
        regstate,
        industryco,
        empnum,
        opscope,
        oploc,
        oplocdistrict,
        oplocpostalcode,
        oploctel,
        prolocstreetcode,
        canregrea,
        candate,
        isuniscid
    )
    SELECT
        id,
        bbh,
        sjbbm,
        sjblx,
        dwdm,
        dwmc,
        infoactiontype,
        infodate,
        pripid,
        uniscid,
        regno,
        traname,
        compform,
        apprdate,
        regorg,
        notno,
        estdate,
        enttype,
        regstate,
        industryco,
        empnum,
        opscope,
        oploc,
        oplocdistrict,
        oplocpostalcode,
        oploctel,
        prolocstreetcode,
        canregrea,
        candate,
        isuniscid
    FROM ods.trs_info_practice_cancel_temporary;
END;
/
