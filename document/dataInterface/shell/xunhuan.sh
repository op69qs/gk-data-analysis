#! /bin/bash

start_date=20200101
end_date=20200131

while ["$start_date" < "$end_date"]; 
do

start_date=`date -d "$start_date" +%Y-%m-%d`

echo "$start_date"

start_date=`date -d "$start_date+1days" +%Y%m%d`


done

