-- Missing routine dependencies required by the ten migrated MySQL Events.
-- Target: Vastbase with sql_compatibility = B.

SET search_path TO dmcode, public;

DROP PROCEDURE IF EXISTS edw.p_trs_budget_new;
CREATE PROCEDURE edw.p_trs_budget_new()
AS
BEGIN
    DELETE FROM edw.cm_guoku_bdgorg;

    INSERT INTO edw.cm_guoku_bdgorg (
        S_BDGORGTRECODE,
        S_TRECODE,
        S_BDGORGCODE,
        S_BDGORGNAME
    )
    SELECT DISTINCT
        CONCAT(S_TRECODE, S_BDGORGCODE),
        S_TRECODE,
        S_BDGORGCODE,
        S_BDGORGNAME
    FROM adm.trs_stat_agentbankpay_detail;

    COMMIT;
END;
/

DROP PROCEDURE IF EXISTS edw.proc_trs_guoku_cp;
CREATE PROCEDURE edw.proc_trs_guoku_cp()
AS
BEGIN
    DELETE FROM adm.trs_stat_agentbankpay_back_detail
    WHERE STR_TO_DATE(
        CONCAT_WS(
            '-',
            LEFT(S_ENTRUSTDATE, 4),
            SUBSTR(S_ENTRUSTDATE, 5, 2),
            RIGHT(S_ENTRUSTDATE, 2)
        ),
        '%Y-%m-%d'
    ) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);

    DELETE FROM adm.trs_stat_agentbankpay_detail
    WHERE STR_TO_DATE(
        CONCAT_WS(
            '-',
            LEFT(S_ENTRUSTDATE, 4),
            SUBSTR(S_ENTRUSTDATE, 5, 2),
            RIGHT(S_ENTRUSTDATE, 2)
        ),
        '%Y-%m-%d'
    ) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);

    INSERT INTO adm.trs_stat_agentbankpay_detail
    SELECT *
    FROM ods.trs_stat_agentbankpay_detail
    WHERE STR_TO_DATE(
        CONCAT_WS(
            '-',
            LEFT(S_ENTRUSTDATE, 4),
            SUBSTR(S_ENTRUSTDATE, 5, 2),
            RIGHT(S_ENTRUSTDATE, 2)
        ),
        '%Y-%m-%d'
    ) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);

    INSERT INTO adm.trs_stat_agentbankpay_back_detail
    SELECT *
    FROM ods.trs_stat_agentbankpay_back_detail
    WHERE STR_TO_DATE(
        CONCAT_WS(
            '-',
            LEFT(S_ENTRUSTDATE, 4),
            SUBSTR(S_ENTRUSTDATE, 5, 2),
            RIGHT(S_ENTRUSTDATE, 2)
        ),
        '%Y-%m-%d'
    ) = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
END;
/
