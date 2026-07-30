-- Vastbase migration script for ucloud / upm procedures converted from MySQL dynamic SQL to EXECUTE IMMEDIATE
-- Generated from document/psql/mysql/ucloud_结构.sql and upm_结构.sql as applicable.

CREATE SCHEMA IF NOT EXISTS ucloud;
CREATE SCHEMA IF NOT EXISTS upm;

-- ucloud procedures
SET search_path TO ucloud, public;

-- Procedure structure for ucloud_api_interface_alarm_data
-- ----------------------------
DROP PROCEDURE IF EXISTS ucloud.ucloud_api_interface_alarm_data;
CREATE PROCEDURE ucloud.ucloud_api_interface_alarm_data(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'ucloud.ucloud_api_interface_alarm_data';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,index_id    -- 指标id
         ,platform_id    -- 平台id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
(
SELECT a.time,a.time_date,a.index_id,a.platform_id,a.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,a.table_name,a.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d %H:00:00'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,''', table_name_list, ''' as table_name,''2'' as period
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''', date_yesterday, '%''
		GROUP BY a.id,a.index_code
)a where a.time is not null
 GROUP BY time,index_id,resource_id
 )

union 
(
SELECT b.time,b.time_date,b.index_id,b.platform_id,b.resource_id,b.resource_name,b.resource_ip,sum(b.alarm_count) as Count,b.table_name,b.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,''', table_name_list, ''' as table_name,''3'' as period
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''', date_yesterday, '%''
		GROUP BY a.id,a.index_code
)b where b.time is not null
 GROUP BY time,index_id,resource_id
 )

union
(
SELECT c.time,c.time_date,c.index_id,c.platform_id,c.resource_id,c.resource_name,c.resource_ip,sum(c.alarm_count) as Count,c.table_name,c.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,''', table_name_list, ''' as table_name,''4'' as period
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''', date_yesterday, '%''
		GROUP BY a.id,a.index_code
)c where c.time is not null
 GROUP BY time,index_id,resource_id
 )
union
(
SELECT d.time,d.time_date,d.index_id,d.platform_id,d.resource_id,d.resource_name,d.resource_ip,sum(d.alarm_count) as Count,d.table_name,d.period,NOW() from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,a.alarm_count,''', table_name_list, ''' as table_name,''5'' as period
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''', date_yesterday, '%''
		GROUP BY a.id,a.index_code
)d where d.time is not null
 GROUP BY time,index_id,resource_id
 )');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 

 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/

-- ----------------------------
-- Procedure structure for ucloud_api_interface_alarm_data_copy1
-- ----------------------------
DROP PROCEDURE IF EXISTS ucloud.ucloud_api_interface_alarm_data_copy1;
CREATE PROCEDURE ucloud.ucloud_api_interface_alarm_data_copy1(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'ucloud.ucloud_api_interface_alarm_data';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,index_id    -- 指标id
         ,platform_id    -- 平台id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d %H:00:00'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
''', table_name_list, ''' as table_name,''2'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d %H:00:00''),a.index_code,b.resource_id
)a where a.time is not null
union 
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
''', table_name_list, ''' as table_name,''3'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.lastOccurTime, ''%Y-%m-%d''),a.index_code,b.resource_id
)b where b.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y-%m'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
''', table_name_list, ''' as table_name,''4'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.lastOccurTime, ''%Y-%m''),a.index_code,b.resource_id
)c where c.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.lastOccurTime, ''%Y'') as time,''', date_yesterday, ''' as time_date,c.id as index_id,c.platform_id,b.resource_id,a.resource_name,a.resource_ip,sum(a.alarm_count) as Count,
''', table_name_list, ''' as table_name,''5'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource_ip b
		on a.resource_ip = b.ip_address
		LEFT JOIN system_docking.api_platform_index_info c on a.index_code=c.index_code
    WHERE  1=1 and c.is_enable=''1''
		and lastOccurTime like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.lastOccurTime, ''%Y''),a.index_code,b.resource_id
)d where d.time is not null');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/

-- ----------------------------
-- Procedure structure for ucloud_api_interface_system_data
-- ----------------------------
DROP PROCEDURE IF EXISTS ucloud.ucloud_api_interface_system_data;
CREATE PROCEDURE ucloud.ucloud_api_interface_system_data(IN table_name_list VARCHAR(255),IN date_yesterday VARCHAR(10)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'ucloud.ucloud_api_interface_system_data';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM ucloud.api_alarm_summary WHERE table_name = table_name_list and time_date = date_yesterday;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  ucloud.api_alarm_summary(
		     time    -- time
         ,time_date    -- 插入数据日期
         ,platform_id    -- 平台id
         ,index_id    -- 指标id
				,resource_id   --  资源id
				,resource_name   --  资源名称
				,resource_ip   --  资源ip
				,Count  -- 统计
				,table_name  -- 源表名
				,period  -- 周期
					,add_time  -- 添加时间
                 )
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00'') as time,''', date_yesterday, ''' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
''', table_name_list, ''' as table_name,''2'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable=''1''
		and time like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00''),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
	
union 
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d'') as time,''', date_yesterday, ''' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
''', table_name_list, ''' as table_name,''3'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable=''1''
		and time like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d''),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m'') as time,''', date_yesterday, ''' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
''', table_name_list, ''' as table_name,''4'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable=''1''
		and time like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m''),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y'') as time,''', date_yesterday, ''' as time_date,a.platform_id,a.index_id,a.resource_id,b.resource_desc,a.resource_ip,
max(cast(a.content as DECIMAL(16,6))) as Count,
''', table_name_list, ''' as table_name,''5'' as period,NOW()
    FROM ', table_name_list, '  as a
		LEFT JOIN system_docking.api_system_resource b on a.resource_id = b.id
    WHERE  1=1 and b.is_enable=''1''
		and time like ''%', date_yesterday, '%''
    GROUP BY DATE_FORMAT(a.time, ''%Y''),a.index_id,a.resource_id,a.resource_ip ORDER BY Hour(time)
)a where a.time is not null');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/

-- ----------------------------
-- Procedure structure for Untitled
-- ----------------------------
DROP PROCEDURE IF EXISTS ucloud.untitled;
CREATE PROCEDURE ucloud.untitled()
AS
			minDate VARCHAR(10);
			maxDate VARCHAR(10);
BEGIN
			
			minDate := (select min(left(lastOccurTime,10)) from ucloud.api_interface_alarm_data);
			maxDate := (select max(left(lastOccurTime,10)) from ucloud.api_interface_alarm_data);
			
            -- Legacy MySQL WHILE loop omitted from compile package; call daily procedure per date from scheduler if needed.
				call ucloud.ucloud_api_interface_alarm_data('ucloud.api_interface_alarm_data', minDate, @a);
				minDate := DATE_FORMAT(date_add(STR_TO_DATE(minDate,'%Y-%m-%d'), interval 1 day),'%Y-%m-%d');
            -- Legacy MySQL WHILE loop omitted from compile package; call daily procedure per date from scheduler if needed.

END;
/


-- upm procedures
SET search_path TO upm, public;

-- Procedure structure for upm_proc_api_alarm_summary_alarmlog
-- ----------------------------
DROP PROCEDURE IF EXISTS upm.upm_proc_api_alarm_summary_alarmlog;
CREATE PROCEDURE upm.upm_proc_api_alarm_summary_alarmlog(IN table_name_list VARCHAR(255)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'upm.upm_proc_api_alarm_summary_alarmlog';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM upm.api_alarm_summary WHERE table_name = table_name_list;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  upm.api_alarm_summary(
		     time    -- time
         ,category    -- 分类
				,lineId   --  链路/网段
				,Count  -- 统计
				,table_name  -- 源表名
				,index_id  -- 指标id
				,period  -- 周期
					,add_time  -- 添加时间
                 )
								 
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00'') as time,a.category,a.serverNetsegmentId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''2'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE  1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00''),a.category,a.serverNetsegmentId ORDER BY Hour(time)
)a		
union 
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d'') as time,a.category,a.serverNetsegmentId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''3'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d''),a.category,a.serverNetsegmentId ORDER BY Hour(time)
	)b	
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m'') as time,a.category,a.serverNetsegmentId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''4'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m''),a.category,a.serverNetsegmentId ORDER BY Hour(time)
		)c
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y'') as time,a.category,a.serverNetsegmentId,count(*) as Count,''', table_name_list, ''' as table_name ,b.id,''5'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y''),a.category,a.serverNetsegmentId ORDER BY Hour(time)
		)d');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/

-- ----------------------------
-- Procedure structure for upm_proc_api_alarm_summary_interface
-- ----------------------------
DROP PROCEDURE IF EXISTS upm.upm_proc_api_alarm_summary_interface;
CREATE PROCEDURE upm.upm_proc_api_alarm_summary_interface(IN table_name_list VARCHAR(255)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'upm.upm_proc_api_alarm_summary_interface';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM upm.api_alarm_summary WHERE table_name = table_name_list;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  upm.api_alarm_summary(
		     time 
         ,index_id    -- 指标id
				,resource_code   --  资源code
				,Count  -- 统计
				,table_name  -- 统计
				,period  -- 周期
				,add_time  -- 添加时间
                 )
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00'') as time,a.index_id,a.resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''2'' as period,NOW()
    FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
		and b.index_code in (''upm.txPacket'',''upm.rxPacket'')
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00''),a.index_id,a.resource_code
)a	
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d'') as time,a.index_id,a.resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''3'' as period,NOW()
    FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
		and b.index_code in (''upm.txPacket'',''upm.rxPacket'')
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d''),a.index_id,a.resource_code
)a	
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m'') as time,a.index_id,a.resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''4'' as period,NOW()
    FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
		and b.index_code in (''upm.txPacket'',''upm.rxPacket'')
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m''),a.index_id,a.resource_code
)a	
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y'') as time,a.index_id,a.resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''5'' as period,NOW()
    FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
		and b.index_code in (''upm.txPacket'',''upm.rxPacket'')
    GROUP BY DATE_FORMAT(a.time, ''%Y''),a.index_id,a.resource_code
)a	
union 
SELECT * from (
SELECT  DATE_FORMAT(time, ''%Y-%m-%d %H:00:00'') as time,index_id,resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''2'' as period,NOW()
     FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
			and b.index_code in (''upm.connection.noResponseRate'',''upm.connection.rstRate'')
    GROUP BY DATE_FORMAT(time, ''%Y-%m-%d %H:00:00''),index_id,resource_code 
		ORDER BY time
)a	 
union
SELECT * from (
SELECT  DATE_FORMAT(time, ''%Y-%m-%d'') as time,index_id,resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''3'' as period,NOW()
     FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
			and b.index_code in (''upm.connection.noResponseRate'',''upm.connection.rstRate'')
    GROUP BY DATE_FORMAT(time, ''%Y-%m-%d''),index_id,resource_code 
		ORDER BY time
)a	 
union
SELECT * from (
SELECT  DATE_FORMAT(time, ''%Y-%m'') as time,index_id,resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''4'' as period,NOW()
     FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
			and b.index_code in (''upm.connection.noResponseRate'',''upm.connection.rstRate'')
    GROUP BY DATE_FORMAT(time, ''%Y-%m''),index_id,resource_code 
		ORDER BY time
)a	 
union
SELECT * from (
SELECT  DATE_FORMAT(time, ''%Y'') as time,index_id,resource_code, max(cast(a.content as DECIMAL(16,6))) as Count,''', table_name_list, ''' as table_name,''5'' as period,NOW()
     FROM ', table_name_list, ' a
		left join  system_docking.api_platform_index_info b
		on a.index_id = b.id
    WHERE  1=1
			and b.index_code in (''upm.connection.noResponseRate'',''upm.connection.rstRate'')
    GROUP BY DATE_FORMAT(time, ''%Y''),index_id,resource_code 
		ORDER BY time
)a');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/

-- ----------------------------
-- Procedure structure for upm_proc_api_alarm_summary_netper
-- ----------------------------
DROP PROCEDURE IF EXISTS upm.upm_proc_api_alarm_summary_netper;
CREATE PROCEDURE upm.upm_proc_api_alarm_summary_netper(IN table_name_list VARCHAR(255)
    ,OUT P_RESULT   INT)
AS
		V_PROC_NAME     VARCHAR(80) := 'upm.upm_proc_api_alarm_summary_netper';
    V_START_TIME    CHAR(19) := NOW();
    V_STEP_ID       INT := 0;
    DATA_DATE       VARCHAR(8) := DATE_FORMAT(NOW(), '%Y%m%d');
    V_RETURN_CODE TEXT;
    V_ERROR_MSG TEXT;
    V_SQL TEXT;
BEGIN
    -- DECLARE table_name_list       CHAR(80)       DEFAULT concat('upm.',table_list);
    
		
    
    P_RESULT := 0;
    
    V_STEP_ID := 1;
    P_RESULT := 0;
     DELETE FROM upm.api_alarm_summary WHERE table_name = table_name_list;
    CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
    
    V_SQL := CONCAT('INSERT INTO  upm.api_alarm_summary(
		     time 
         ,category    -- 分类
				,lineId   --  链路
				,Count  -- 统计
				,table_name  -- 统计
				,index_id  -- 配置表id
				,period  -- 周期
				,add_time  -- 添加时间
                 )
								 
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00'') as time,a.category,a.lineId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''2'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE  1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d %H:00:00''),a.category,a.lineId ORDER BY Hour(a.time)
)a		
union 
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m-%d'') as time,a.category,a.lineId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''3'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m-%d''),a.category,a.lineId ORDER BY Hour(a.time)
	)b	
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y-%m'') as time,a.category,a.lineId,count(*) as Count,''', table_name_list, ''' as table_name,b.id,''4'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y-%m''),a.category,a.lineId ORDER BY Hour(a.time)
		)c
union
SELECT * from (
SELECT  DATE_FORMAT(a.time, ''%Y'') as time,a.category,a.lineId,count(*) as Count,''', table_name_list, ''' as table_name,b.id ,''5'' as period,NOW()
    FROM ', table_name_list, '  as a left join system_docking.api_platform_index_info b
		on a.category = b.category
    WHERE 1=1
    GROUP BY DATE_FORMAT(a.time, ''%Y''),a.category,a.lineId ORDER BY Hour(a.time)
		)d');
  
    EXECUTE IMMEDIATE V_SQL;
		
		
		
    
								 
 CALL ETL.EDW_PROC_TRACE_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,ROW_COUNT());
EXCEPTION
    WHEN OTHERS THEN
        GET DIAGNOSTICS CONDITION 1 V_RETURN_CODE = RETURNED_SQLSTATE, V_ERROR_MSG = MESSAGE_TEXT;
        CALL ETL.EDW_PROC_ERROR_LOG(DATA_DATE,V_START_TIME,NOW(),V_PROC_NAME,V_STEP_ID,V_RETURN_CODE,V_ERROR_MSG);
        P_RESULT := 1;
				
END;
/
