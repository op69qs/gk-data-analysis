#!/bin/bash


source /usr/local/oracle/instantclient_19_8/network/admin/.bash_profile

#Oracle_USER
ORACLE_USER=ZHTK_CX
#Oracle_PWD
ORACLE_PWD=oracle123
#Oracle_SERV
ORACLE_SERV=9.16.20.178:1521/qcdzkt



sqlplus -s $ORACLE_USER/$ORACLE_PWD@$ORACLE_SERV << EOF 

set trimspool on
set linesize 3000
set pagesize 0
set newpage 1
set nowp none
set heading off
set trimspool on
set termout off
set feedback off
set term off
set termout off
set trimout on
set echo off;

spool /home/guoku/data_oracle/PT_GY_XWRZ.txt
select UUID||'|'||YW_DM||'|'||XW_DM||'|'||ZT||'|'||LRR_DM||'|'||HSZT_DM||'|'||to_char(KSSJ,'yyyy-mm-dd')||'|'||to_char(JSSJ,'yyyy-mm-dd')||'|'||dbms_lob.substr(BZ)
from rhdzhtk.PT_GY_XWRZ;

spool off
quit
EOF


function to_mysql(){


	mysql -h11.8.204.18 -uroot -pchongqing -e"truncate ods.PT_GY_XWRZ;LOAD DATA LOCAL INFILE'/home/guoku/data_oracle/PT_GY_XWRZ.txt'INTO TABLE ODS.PT_GY_XWRZ CHARACTER SET utf8 FIELDS TERMINATED BY '|' LINES TERMINATED BY '\n'"






}

to_mysql
