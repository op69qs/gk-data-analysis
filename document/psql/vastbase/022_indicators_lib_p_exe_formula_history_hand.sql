-- Vastbase source-port script for indicators_lib.p_exe_formula_history_hand
-- Strategy: keep MySQL B-compatible body first, then validate transaction and dynamic-SQL behavior on target.

DROP PROCEDURE IF EXISTS indicators_lib.p_exe_formula_history_hand;
CREATE PROCEDURE indicators_lib.p_exe_formula_history_hand(IN v_period_flag VARCHAR(2), IN v_index_id VARCHAR(32), OUT v_out VARCHAR(1000))
BEGIN
  DECLARE V_PROC_NAME     VARCHAR(80)   DEFAULT 'INDICATORS_LIB.P_EXE_FORMULA_HISTORY_HAND.PRC';
  DECLARE V_START_TIME    CHAR(19)      DEFAULT NOW();
  DECLARE V_STEP_ID       INT           DEFAULT 0;
  DECLARE P_DATA_DATE     VARCHAR(8)	DEFAULT	DATE_FORMAT(NOW(),'%Y-%m');
	
	DECLARE V_END_DATE VARCHAR(20);
  
  DECLARE done INT DEFAULT 0;
  DECLARE index_id VARCHAR (32);
  DECLARE table_name VARCHAR (200);
  DECLARE deleteSql varchar (2000);
  DECLARE selectStr text;
  DECLARE insertStr VARCHAR (2000);
  DECLARE whereStr VARCHAR (2000);
  DECLARE buildType VARCHAR (2);
	
	DECLARE updateStr VARCHAR (2000) DEFAULT CONCAT('UPDATE indicators_lib.`lib_index_relation` SET RUN_BATCH_STATUS=@ WHERE INDEX_ID=''', v_index_id,'''');
	
	DECLARE sqlText CURSOR FOR
					SELECT ID,INDEX_CORRE_TABLE,EXE_DEL,EXE_INSERT,EXE_SQL,EXE_WHERE,BUILD_TYPE FROM `indicators_lib`.`lib_index_formula` WHERE ID = v_index_id;
	
	
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
	   
		 ROLLBACK;
		 
		 GET DIAGNOSTICS CONDITION 1 @V_RETURN_CODE = RETURNED_SQLSTATE ,@V_ERROR_MSG = MESSAGE_TEXT;
		 
		 SET v_out = CONCAT('false,', @V_ERROR_MSG);
		 
		 CALL ETL.EDW_PROC_ERROR_LOG(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,@V_RETURN_CODE,@V_ERROR_MSG);
  END;
	
	IF v_period_flag = '1' THEN  
			SET V_END_DATE = DATE_FORMAT('20211201','%Y-%m-%d');
	ELSEIF v_period_flag = '2' THEN 
			SET V_END_DATE = DATE_FORMAT('20211201','%Y-%m');
	ELSEIF v_period_flag = '3' THEN 
			SET V_END_DATE = CONCAT(year('20211201'),'-0',quarter('20211201'));
	ELSEIF v_period_flag = '4' THEN 
			SET V_END_DATE = year('20211201');
	END IF;
	
	OPEN sqlText;
  WHILE
    done <> 1 DO FETCH sqlText INTO index_id, table_name, deleteSql, insertStr, selectStr, whereStr, buildType;
		IF done <> 1 THEN
				
				START TRANSACTION; 
				
				SET V_STEP_ID = 1;
				SET @del_sql = CONCAT('DELETE FROM ', table_name, ' WHERE PERIOD_FLAG=''', v_period_flag, ''' AND REPLACE(ACCOUNT_PERIOD,', '''Q''', ',''-0'')' , '<=', '''', V_END_DATE, '''');
				PREPARE delSql FROM @del_sql;
				EXECUTE delSql;
				DEALLOCATE PREPARE delSql;
				
				CALL ETL.EDW_PROC_TRACE_LOG(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());  
				
				SET V_STEP_ID = 2;
				SET @update_Sql_0 = REPLACE(updateStr,'@','''1''');
				PREPARE update_0 FROM @update_Sql_0;
				EXECUTE update_0;
				DEALLOCATE PREPARE update_0;
				
				CALL ETL.EDW_PROC_TRACE_LOG(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
				
				IF buildType = '1' THEN
					 SET selectStr = CONCAT(selectStr, ' WHERE PERIOD_FLAG=''', v_period_flag, ''' AND REPLACE(ACCOUNT_PERIOD,', '''Q''', ',''-0'')' , '<=', '''', V_END_DATE, '''');
					 SET whereStr = NULL;
				END IF;
				
				SET V_STEP_ID = 3;
				SET @insert_sql = CONCAT_ws('', insertStr, selectStr, whereStr);
				PREPARE insertSql FROM @insert_sql;
				EXECUTE insertSql;
				DEALLOCATE PREPARE insertSql;
				
				CALL ETL.EDW_PROC_TRACE_LOG(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
				
				SET V_STEP_ID = 4;
				SET @update_Sql_1 = REPLACE(updateStr,'@','''2''');
				PREPARE update_1 FROM @update_Sql_1;
				EXECUTE update_1;
				DEALLOCATE PREPARE update_1;
				
				CALL ETL.EDW_PROC_TRACE_LOG(P_DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT()); 
		END IF;
	END WHILE;
  CLOSE sqlText;
END;
