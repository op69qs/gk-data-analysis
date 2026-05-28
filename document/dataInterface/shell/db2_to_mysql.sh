#!/bin/bash

##############################################################
#					                     #	
#							     #
#	国库智能分析系统&集中支付事中监督系统		     #
#							     #
#		     MySQL  &   db2		             #
#                                                            #
#	      取数频率：每天22:00:00		             #
#                                                            #
#	         同步方式：增量			             #
#                                                            #
#                                                            #
#							     #
############################################################## 
export PATH=$PATH:/opt/ibm/db2/V9.7/bin
db2 terminate
export LANG=zh_CN.gbk

#The DB2 Datebase Host
DB2_HOST=11.8.1.17

#The DB2 Database Password
DB_PASSWORD=Password

#The DB2 Database Name
DATABASE_NAME=tessdb

#The DB2 Datebase User Name
DB_USER_NAME=bduser

#The DB2 Database SCGEMA
SCHEMA=DB2TESS

#The MYSQL Datebase User
MS_USER=root

#The MYSQL Datebase Password
MS_PASSWD=chongqing

#The MySql Datebase Host
HOST=11.8.160.2

#SHELL_HOME
SHELL_HOME=/opt/app/guoku/dataInterface

DATA_DATE=$(date -d "-3 day" +%Y%m%d)
#DATA_DATE=20201020

function load_to_mysql(){

for table_list in `cat $SHELL_HOME/table_list/payout.list`

	do
		db2 "EXPORT TO $SHELL_HOME/data_db2/$table_list.txt OF DEL modified by nochardel codepage=1208 coldel| striplzeros SELECT * FROM $SCHEMA.$table_list WHERE S_ENTRUSTDATE='$DATA_DATE' "
	       # db2 "EXPORT TO $SHELL_HOME/data_db2/$table_list.txt OF DEL modified by nochardel coldel0x09 striplzeros SELECT * FROM $SCHEMA.$table_list"
		mysql -u$MS_USER -p$MS_PASSWD -h$HOST -e "DELETE FROM ods.$table_list WHERE S_ENTRUSTDATE='$DATA_DATE';
				LOAD DATA LOCAL INFILE '$SHELL_HOME/data_db2/$table_list.txt' INTO TABLE ods.$table_list CHARACTER SET utf8 FIELDS TERMINATED BY '|' LINES TERMINATED BY '\n' "

	done
}


db2 connect to $DATABASE_NAME user $DB_USER_NAME using $DB_PASSWORD
load_to_mysql
db2 disconnect $DATABASE_NAME



