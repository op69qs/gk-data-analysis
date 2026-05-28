#!/bin/bash



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

file=/home/guoku/data_oracle

sqlplus -s $ORACLE_USER/$ORACLE_PWD@$ORACLE_SERV<<EOF 

set trimspool on
set linesize 200
set pagesize 200
set newpage 1
set heading off
set trimspool on
set echo off;

spool /home/guoku/data_oracle/PT_TK_SQRXX.txt
select UUID||'|'||SPB_ID||'|'||SQRMC||'|'||SQRLX||'|'||LXRMC||'|'||LXDH||'|'||NSRSBH||'|'||NSRMC||'|'||SQTSJE||'|'||SQRQ||'|'||JBR||'|'||SKYHMC||'|'||SKZH||'|'||TSSQLY||'|'||TSSQLYMC||'|'||SPZT||'|'||SPJG||'|'||HSZT_DM||'|'||YXBZ||'|'||XGR_DM||'|'||XGRQ||'|'||LRRQ||'|'||PCUUID||'|'||ZWRQ||'|'||CZY||'|'||DBQK||'|'||CS_SPZT||'|'||THYY_DM||'|'||TJRQ||'|'||ZHCWTHBZ 
from rhdzhtk.PT_TK_SQRXX;
spool off
quit
EOF
