-- Vastbase source-port bundle for visual_screen task procedures
-- Strategy: keep MySQL B-compatible procedure bodies first, then validate direct callees in sequence.

-- PROCEDURE `visual_screen`.`P_task_vscreen`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_task_vscreen`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_task_vscreen`(IN V_BATCH_DATE VARCHAR(10))
BEGIN
	DECLARE DONE             INT DEFAULT FALSE;	
	DECLARE V_DATA_DATE      VARCHAR(10);          
	DECLARE CUR_D_ACCT CURSOR FOR 
	SELECT 
	DISTINCT 
	IF(
	DATE_FORMAT(d_acct,'%Y-%m-%d') IS NULL,
	DATE_FORMAT(CONCAT(d_acct,'01'),'%Y-%m-%d'),
	DATE_FORMAT(d_acct,'%Y-%m-%d')
	) AS d_acct
	FROM 
	(
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_BUDGET_INCOME
	WHERE BATCH_DATE = V_BATCH_DATE
	UNION ALL 
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_BUDGET_PAYOUT
	WHERE BATCH_DATE = V_BATCH_DATE
	UNION ALL 
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_STOCK
	WHERE BATCH_DATE = V_BATCH_DATE 
	UNION ALL
	SELECT data_date 
	FROM STG.TRS_TMIS_BUDGET_INCOME_PROVINCES
	WHERE BATCH_DATE = V_BATCH_DATE) a
	ORDER BY
	IF(
	DATE_FORMAT(d_acct,'%Y-%m-%d') IS NULL,
	DATE_FORMAT(CONCAT(d_acct,'01'),'%Y-%m-%d'),
	DATE_FORMAT(d_acct,'%Y-%m-%d')
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET DONE:= TRUE;
	
	OPEN CUR_D_ACCT;
	ENTLOOP:LOOP
		FETCH CUR_D_ACCT INTO V_DATA_DATE;
		IF DONE THEN
		LEAVE ENTLOOP;
		END IF;
	 	
		CALL visual_screen.p_vs_inventory_analyze		              (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_area                        (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_balance		              (V_DATA_DATE);
		CALL visual_screen.P_vs_inventory_form                        (V_DATA_DATE);
		
		CALL visual_screen.p_vs_revenu_display                        (V_DATA_DATE);
		CALL visual_screen.p_vs_amount_ranking_by_region              (V_DATA_DATE);
		CALL visual_screen.p_vs_regional_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_municipalities_directly               (V_DATA_DATE);
		CALL visual_screen.p_vs_five_provinces_in_southwest_china     (V_DATA_DATE);
	        
	        IF V_DATA_DATE = LAST_DAY(V_DATA_DATE)
	        THEN 
	        
		CALL visual_screen.p_vs_gemini_structure                      (V_DATA_DATE);
		CALL visual_screen.p_vs_income_expenditure                    (V_DATA_DATE);
		CALL visual_screen.p_vs_three_budget_revenue                  (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_revenue                           (V_DATA_DATE);
		CALL visual_screen.p_vs_local_financial_resources             (V_DATA_DATE);
		CALL visual_screen.p_vs_transfer_income                       (V_DATA_DATE);
		CALL visual_screen.p_vs_land_transfer                         (V_DATA_DATE);
		CALL visual_screen.p_vs_industry_tax                          (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_subject                           (V_DATA_DATE);
		CALL visual_screen.p_vs_pillar_industries                     (V_DATA_DATE);
		CALL visual_screen.p_vs_budget_revenue                        (V_DATA_DATE);
		CALL visual_screen.p_vs_public_budget                         (V_DATA_DATE);
		CALL visual_screen.p_vs_chongqing_economic_zone               (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_growth_public_budget                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_revenue                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_non_tax                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_vat                    (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_duties                 (V_DATA_DATE);
		CALL visual_screen.p_vs_import_duty_on_imported_articles      (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_pay                          (V_DATA_DATE);
		CALL visual_screen.p_vs_region_pay                            (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_purpose_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay_sub                       (V_DATA_DATE);
		CALL visual_screen.p_vs_area_pay			                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_income_situation              (V_DATA_DATE);
		CALL visual_screen.P_vs_area_income              	          (V_DATA_DATE);
		
		
	        END IF;	
	
		END LOOP ENTLOOP;
		CLOSE CUR_D_ACCT;
	
	END
$$


-- PROCEDURE `visual_screen`.`P_task_vscreen1`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_task_vscreen1`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_task_vscreen1`(IN V_BATCH_DATE VARCHAR(10))
BEGIN
	DECLARE DONE             INT DEFAULT FALSE;	
	DECLARE V_DATA_DATE      VARCHAR(10);          
	DECLARE CUR_D_ACCT CURSOR FOR 
	SELECT 
	DISTINCT 
	IF(
	DATE_FORMAT(d_acct,'%Y-%m-%d') IS NULL,
	DATE_FORMAT(CONCAT(d_acct,'01'),'%Y-%m-%d'),
	DATE_FORMAT(d_acct,'%Y-%m-%d')
	) AS d_acct
	FROM 
	(
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_BUDGET_INCOME
	WHERE BATCH_DATE = V_BATCH_DATE
	UNION ALL 
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_BUDGET_PAYOUT
	WHERE BATCH_DATE = V_BATCH_DATE
	UNION ALL 
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_STOCK
	WHERE BATCH_DATE = V_BATCH_DATE 
	UNION ALL
	SELECT data_date 
	FROM STG.TRS_TMIS_BUDGET_INCOME_PROVINCES
	WHERE BATCH_DATE = V_BATCH_DATE) a
	ORDER BY
	IF(
	DATE_FORMAT(d_acct,'%Y-%m-%d') IS NULL,
	DATE_FORMAT(CONCAT(d_acct,'01'),'%Y-%m-%d'),
	DATE_FORMAT(d_acct,'%Y-%m-%d')
	);
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET DONE:= TRUE;
	
	OPEN CUR_D_ACCT;
	ENTLOOP:LOOP
		FETCH CUR_D_ACCT INTO V_DATA_DATE;
		IF DONE THEN
		LEAVE ENTLOOP;
		END IF;
	 	
		CALL visual_screen.p_vs_inventory_analyze		              (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_area                        (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_balance		              (V_DATA_DATE);
		CALL visual_screen.P_vs_inventory_form                        (V_DATA_DATE);
		
		CALL visual_screen.p_vs_revenu_display                        (V_DATA_DATE);
		CALL visual_screen.p_vs_amount_ranking_by_region              (V_DATA_DATE);
		CALL visual_screen.p_vs_regional_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_municipalities_directly               (V_DATA_DATE);
		CALL visual_screen.p_vs_five_provinces_in_southwest_china     (V_DATA_DATE);
	        

	        IF 1 = 1
	        THEN 
	        
		CALL visual_screen.p_vs_gemini_structure                      (V_DATA_DATE);
		CALL visual_screen.p_vs_income_expenditure                    (V_DATA_DATE);
		CALL visual_screen.p_vs_three_budget_revenue                  (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_revenue                           (V_DATA_DATE);
		CALL visual_screen.p_vs_local_financial_resources             (V_DATA_DATE);
		CALL visual_screen.p_vs_transfer_income                       (V_DATA_DATE);
		CALL visual_screen.p_vs_land_transfer                         (V_DATA_DATE);
		CALL visual_screen.p_vs_industry_tax                          (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_subject                           (V_DATA_DATE);
		CALL visual_screen.p_vs_pillar_industries                     (V_DATA_DATE);
		CALL visual_screen.p_vs_budget_revenue                        (V_DATA_DATE);
		CALL visual_screen.p_vs_public_budget                         (V_DATA_DATE);
		CALL visual_screen.p_vs_chongqing_economic_zone               (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_growth_public_budget                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_revenue                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_non_tax                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_vat                    (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_duties                 (V_DATA_DATE);
		CALL visual_screen.p_vs_import_duty_on_imported_articles      (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_pay                          (V_DATA_DATE);
		CALL visual_screen.p_vs_region_pay                            (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_purpose_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay_sub                       (V_DATA_DATE);
		CALL visual_screen.p_vs_area_pay			                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_income_situation              (V_DATA_DATE);
		CALL visual_screen.P_vs_area_income              	          (V_DATA_DATE);
		
		
	        END IF;	
	
		END LOOP ENTLOOP;
		CLOSE CUR_D_ACCT;
	
	END
$$


-- PROCEDURE `visual_screen`.`P_task_vscreen_new`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_task_vscreen_new`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_task_vscreen_new`(IN V_BATCH_DATE VARCHAR(10))
BEGIN
	DECLARE DONE             INT DEFAULT FALSE;	
	DECLARE V_DATA_DATE      VARCHAR(10);      
	
	DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_task_vscreen_new.PRC';
	DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
	DECLARE V_STEP_ID       INT           DEFAULT 0;
	DECLARE P_DATA_DATE     VARCHAR(8)	DEFAULT	DATE_FORMAT(CONCAT(V_BATCH_DATE,'01'),'%Y-%m');
    
	DECLARE CUR_D_ACCT CURSOR FOR 
	SELECT 
	DISTINCT 
	 
	DATE_FORMAT(CONCAT(REPLACE(d_acct,'-',''),''),'%Y-%m-%d')
	  AS d_acct
	FROM 
	(
	
	SELECT D_ACCT 
	FROM STG.TRS_TMIS_STOCK
	WHERE data_date LIKE    CONCAT(V_BATCH_DATE,'%') )a;
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET DONE:= TRUE;
	
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
	BEGIN
	GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
	CALL ETL.EDW_PROC_ERROR_LOG(P_DATA_DATE,V_START_TIME,NOW(),'gk',V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
	END;  
	
	OPEN CUR_D_ACCT;
	ENTLOOP:LOOP
		FETCH CUR_D_ACCT INTO V_DATA_DATE;
		IF DONE THEN
		LEAVE ENTLOOP;
		END IF;
		
		SET V_STEP_ID = 1;
	 	CALL ETL.EDW_PROC_ERROR_LOG(P_DATA_DATE,V_START_TIME,NOW(),'gk',V_STEP_ID,@V_RETURN_CODE,'1111');
	 	SET V_STEP_ID = 2;
		CALL visual_screen.p_vs_inventory_analyze		              (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_area                        (V_DATA_DATE);
		CALL visual_screen.p_vs_inventory_balance		              (V_DATA_DATE);
		CALL visual_screen.P_vs_inventory_form                        (V_DATA_DATE);
		
		CALL visual_screen.p_vs_revenu_display                        (V_DATA_DATE);
		CALL visual_screen.p_vs_amount_ranking_by_region              (V_DATA_DATE);
		CALL visual_screen.p_vs_regional_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_municipalities_directly               (V_DATA_DATE);
		CALL visual_screen.p_vs_five_provinces_in_southwest_china     (V_DATA_DATE);
	        
	       
	       IF 1=1
	        THEN 
	        
		CALL visual_screen.p_vs_gemini_structure                      (V_DATA_DATE);
		CALL visual_screen.p_vs_income_expenditure                    (V_DATA_DATE);
		CALL visual_screen.p_vs_three_budget_revenue                  (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_revenue                           (V_DATA_DATE);
		CALL visual_screen.p_vs_local_financial_resources             (V_DATA_DATE);
		CALL visual_screen.p_vs_transfer_income                       (V_DATA_DATE);
		CALL visual_screen.p_vs_land_transfer                         (V_DATA_DATE);
		CALL visual_screen.p_vs_industry_tax                          (V_DATA_DATE);
		CALL visual_screen.p_vs_tax_subject                           (V_DATA_DATE);
		CALL visual_screen.p_vs_pillar_industries                     (V_DATA_DATE);
		CALL visual_screen.p_vs_budget_revenue                        (V_DATA_DATE);
		CALL visual_screen.p_vs_public_budget                         (V_DATA_DATE);
		CALL visual_screen.p_vs_chongqing_economic_zone               (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_taxation                     (V_DATA_DATE);
		CALL visual_screen.p_vs_growth_public_budget                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_revenue                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_non_tax                       (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_vat                    (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_import_duties                 (V_DATA_DATE);
		CALL visual_screen.p_vs_import_duty_on_imported_articles      (V_DATA_DATE);
		CALL visual_screen.p_vs_economic_pay                          (V_DATA_DATE);
		CALL visual_screen.p_vs_region_pay                            (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_purpose_pay                           (V_DATA_DATE);
		CALL visual_screen.p_vs_subject_pay_sub                       (V_DATA_DATE);
		CALL visual_screen.p_vs_area_pay			                  (V_DATA_DATE);
		CALL visual_screen.p_vs_customs_income_situation              (V_DATA_DATE);
		CALL visual_screen.P_vs_area_income              	          (V_DATA_DATE);
		
		
	        END IF;	
	
		END LOOP ENTLOOP;
		CLOSE CUR_D_ACCT;
	
	END
$$


-- PROCEDURE `visual_screen`.`P_VS_AMOUNT_RANKING_BY_REGION`
DROP PROCEDURE IF EXISTS `visual_screen`.`P_VS_AMOUNT_RANKING_BY_REGION`;
DELIMITER $$
CREATE DEFINER=`root`@`%` PROCEDURE `P_VS_AMOUNT_RANKING_BY_REGION`(IN V_DATA_DATE VARCHAR(10))
BEGIN
DECLARE V_STEP_ID       INT           DEFAULT 0; 
DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'visual_screen.P_VS_AMOUNT_RANKING_BY_REGION.PRC';
DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATE_FORMAT(V_START_TIME,'%Y-%m'),V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);

-- Included procedures:
--   visual_screen.P_task_vscreen
--   visual_screen.P_task_vscreen1
--   visual_screen.P_task_vscreen_new
