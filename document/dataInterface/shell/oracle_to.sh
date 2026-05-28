#!/bin/bash

#################################################################################################
#																		  						#
#								国库智能分析系统&全程电子退库系统		  						#
#									     MySQL  &   Oracle				  						#
#								      取数频率：每天22:00:00			  						#
#								         同步方式：全量					  						#
#																		  						#
#																		  						#
#################################################################################################
source /usr/local/oracle/instantclient_19_8/network/admin/.bash_profile

#Oracle_USER
ORACLE_USER=ZHTK_CX
#Oracle_PWD
ORACLE_PWD=oracle123
#Oracle_SERV
ORACLE_SERV=9.16.20.178:1521/qcdzkt

#MYSQL_USER
MYSQL_USER=root
#MYSQL_PWD
MYSQL_PWD=chongqing
#MYSQL_HOST
MYSQL_HOST=11.8.204.18

#PATH
file=/home/guoku/data_oracle


#从Oracle数据库中将需要同步的表数据导出成txt格式
sqlplus -s $ORACLE_USER/$ORACLE_PWD@$ORACLE_SERV << EOF 

set trimspool on
set linesize 3000
set pagesize 0
set newpage 1
set heading off
set trimspool on
set termout off
set feedback off
set term off
set termout off
set trimout on
set echo off;

spool /home/guoku/data_oracle/PT_TK_ZSJGHSXX.txt
select UUID||'|'||SQR_UUID||'|'||SLR||'|'||to_char(SLRQ,'yyyy-mm-dd')||'|'||SLQKSM||'|'||SQTSLX||'|'||SQTSLXMC||'|'||TSFQFS||'|'||TSFQFSMC||'|'||TSINFO||'|'||TSHJJE||'|'||DZPZHM||'|'||SKSSSWJGDM||'|'||JMXZDM||'|'||MXS||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.PT_TK_ZSJGHSXX;

quit
EOF


#将TXT文件入库mysql
function load_to_mysql(){
       
	for sql in `ls $file`
	do 
		table_list=${sql%.*}
                echo "${table_list}"	
		mysql -h$MYSQL_HOST -u$MYSQL_USER -p$MYSQL_PWD -e"TRUNCATE ODS.$table_list;LOAD DATA LOCAL INFILE '$file/$sql' INTO TABLE ODS.$table_list CHARACTER SET utf8 FIELDS TERMINATED BY '|' LINES TERMINATED BY '\n'"

	
	done
	
	#执行完SQL后，将文件删除
	
}


load_to_mysql










