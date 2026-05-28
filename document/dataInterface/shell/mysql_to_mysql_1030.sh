#!/bin/sh

#凯盈达mysql地址
MYSQL_HOST=11.8.161.1

#凯盈达MySQL用户
MYSQL_USER=sfuser

#凯盈达MySQL密码
MYSQL_PASSWORD=sfuser

#M_HOST
M_HOST=11.8.204.18

#M_USER
M_USER=root

#M_PASSWORD
M_PASSWORD=chongqing

#日期
DATE=$(date -d "-3 day" +%Y%m%d)
#date=20181207

echo "$DATE"
function load_to_mysql(){


	#取数
	mysqldump --single-transaction -h$MYSQL_HOST -u$MYSQL_USER -p$MYSQL_PASSWORD -t gkdas tv_fin_income_detail --where="s_intredate = $DATE;" > /home/guoku/data_mysql/tv_fin_income_detail_$DATE.sql


	#删除历史历史数据
        mysql -h$M_HOST -u$M_USER -p$M_PASSWORD -e "delete from ods.tv_fin_income_detail "
	#导数
	mysql -h$M_HOST -u$M_USER -p$M_PASSWORD -Dods < /home/guoku/data_mysql/tv_fin_income_detail_$DATE.sql





}


load_to_mysql

