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

spool /home/guoku/data_oracle/PT_TK_SQRXX.txt
select UUID||'|'||SPB_ID||'|'||SQRMC||'|'||SQRLX||'|'||LXRMC||'|'||LXDH||'|'||NSRSBH||'|'||NSRMC||'|'||SQTSJE||'|'||to_char(SQRQ,'yyyy-mm-dd')||'|'||JBR||'|'||SKYHMC||'|'||SKZH||'|'||TSSQLY||'|'||TSSQLYMC||'|'||SPZT||'|'||SPJG||'|'||HSZT_DM||'|'||YXBZ||'|'||XGR_DM||'|'||to_char(XGRQ,'yyyy-mm-dd')||'|'||to_char(LRRQ,'yyyy-mm-dd')||'|'||PCUUID||'|'||to_char(ZWRQ,'yyyy-mm-dd')||'|'||CZY||'|'||DBQK||'|'||CS_SPZT||'|'||THYY_DM||'|'||to_char(TJRQ,'yyyy-mm-dd')||'|'||ZHCWTHBZ 
from rhdzhtk.PT_TK_SQRXX;
spool off
spool /home/guoku/data_oracle/PT_TK_ZSJGHSXX.txt
select UUID||'|'||SQR_UUID||'|'||SLR||'|'||to_char(SLRQ,'yyyy-mm-dd')||'|'||SLQKSM||'|'||SQTSLX||'|'||SQTSLXMC||'|'||TSFQFS||'|'||TSFQFSMC||'|'||TSINFO||'|'||TSHJJE||'|'||DZPZHM||'|'||SKSSSWJGDM||'|'||JMXZDM||'|'||MXS||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.PT_TK_ZSJGHSXX;
spool off
spool /home/guoku/data_oracle/PT_TK_SPMXXX.txt
select UUID||'|'||ZSJGHS_UUID||'|'||XH||'|'||SPHM||'|'||to_char(SKSSRQQ,'yyyy-mm-dd')||'|'||to_char(SKSSRQZ,'yyyy-mm-dd')||'|'||YSKMDM||'|'||YSKMMC||'|'||SZMC||'|'||ZSPMMC||'|'||SPJE||'|'||SJJE||'|'||to_char(LRRQ,'yyyy-mm-dd')||'|'||SPUUID
from rhdzhtk.PT_TK_SPMXXX;
spool off
spool /home/guoku/data_oracle/PT_TK_TSMX.txt
select UUID||'|'||TK_UUID||'|'||YSKMDM||'|'||YSKMMC||'|'||YSFPBLDM||'|'||YSFPBLMC||'|'||ZSXMDM||'|'||ZSXMMC||'|'||ZSPMDM||'|'||ZSPMMC||'|'||TDSFYYLXDM||'|'||TDSFYYLXMC||'|'||TKJE||'|'||ZLQBZ||'|'||FZBZ||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.PT_TK_TSMX;
spool off
spool /home/guoku/data_oracle/PT_GY_JYLS.txt
select DZSPHM||'|'||SPZT||'|'||SPJG||'|'||HSZT_DM||'|'||YH_DM||'|'||to_char(LRRQ,'yyyy-mm-dd')||'|'||UUID||'|'||SQR_UUID
from rhdzhtk.PT_GY_JYLS;
spool off
spool /home/guoku/data_oracle/CS_GY_XTCS.txt
select CSFL||'|'||CSM||'|'||CSZ||'|'||XYBZ||'|'||BZ
from rhdzhtk.CS_GY_XTCS;
spool off
spool /home/guoku/data_oracle/DM_GKZT_YSKM_FZBZ.txt
select GKZT_DM||'|'||YSKM_DM||'|'||YSZL_DM||'|'||FZBZ_DM||'|'||FZBZ_CENTER_DM||'|'||FZBZ_CITY_DM||'|'||FZBZ_PROVINCE_DM||'|'||FZBZ_COUNTY_DM||'|'||FZBZ_COUNTRY_DM
from rhdzhtk.DM_GKZT_YSKM_FZBZ;
spool off
spool /home/guoku/data_oracle/DM_GY_GKZT.txt
select GKZT_DM||'|'||GKZTMC||'|'||GKZTJC||'|'||JC||'|'||SX||'|'||HSZT_DM||'|'||CITY_GKZT_DM||'|'||PROVINCE_GKZT_DM||'|'||COUNTY_GKZT_DM||'|'||JM||'|'||ZT||'|'||XGR_DM||'|'||to_char(XGRQ,'yyyy-mm-dd')||'|'||LRR_DM||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.DM_GY_GKZT;
spool off
spool /home/guoku/data_oracle/DM_GY_HSZT.txt
select HSZT_DM||'|'||HSZTMC||'|'||HSZTJC||'|'||JC||'|'||SX||'|'||SJHSZT_DM||'|'||CITY_HSZT_DM||'|'||PROVINCE_HSZT_DM||'|'||COUNTY_HSZT_DM||'|'||ZT||'|'||XGR_DM||'|'||to_char(XGRQ,'yyyy-mm-dd')||'|'||LRR_DM||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.DM_GY_HSZT;
spool off
spool /home/guoku/data_oracle/DM_GY_SPZT.txt
select SPZT||'|'||SPMC||'|'||XYBZ||'|'||YXBZ
from rhdzhtk.DM_GY_SPZT;
spool off
spool /home/guoku/data_oracle/DM_GY_THYY.txt
select THYY_DM||'|'||THYYMC||'|'||YWLX||'|'||YXBZ||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.DM_GY_THYY;
spool off
spool /home/guoku/data_oracle/PT_GY_XWRZ.txt
select UUID||'|'||YW_DM||'|'||XW_DM||'|'||ZT||'|'||LRR_DM||'|'||HSZT_DM||'|'||to_char(KSSJ,'yyyy-mm-dd')||'|'||to_char(JSSJ,'yyyy-mm-dd')||'|'||dbms_lob.substr(BZ)
from rhdzhtk.PT_GY_XWRZ;
spool off
spool /home/guoku/data_oracle/DM_GY_YHHH.txt
select BANKCODE||'|'||BANKNAME||'|'||BANKCATALOG||'|'||BANKTYPE||'|'||PBCCODE||'|'||CCPC||'|'||DRECCODE||'|'||AGENTSETTBANK||'|'||SUPRLIST||'|'||SBSTITNBK||'|'||DEBTORCITY||'|'||SYSCODE||'|'||TEL||'|'||EFFECTDATE||'|'||EXPDATE
from rhdzhtk.DM_GY_YHHH;
spool off
spool /home/guoku/data_oracle/PT_YHXX.txt
select YH_DM||'|'||YHXM||'|'||YHKL||'|'||ZJHM||'|'||to_char(CSRQ,'yyyy-mm-dd')||'|'||XB||'|'||HSZT_DM||'|'||GKZT_DM||'|'||YHLX_DM||'|'||ZT||'|'||XGR_DM||'|'||to_char(XGRQ,'yyyy-mm-dd')||'|'||LRR_DM||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.PT_YHXX;
spool off
spool /home/guoku/data_oracle/QX_YHLX_GNS.txt
select UUID||'|'||YHLX_DM||'|'||GN_DM||'|'||YXBZ||'|'||XGR_DM||'|'||to_char(XGRQ,'yyyy-mm-dd')||'|'||LRR_DM||'|'||to_char(LRRQ,'yyyy-mm-dd')
from rhdzhtk.QX_YHLX_GNS;
spool off
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










