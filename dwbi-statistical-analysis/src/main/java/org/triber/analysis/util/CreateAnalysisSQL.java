package org.triber.analysis.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.triber.analysis.service.KeyIndicatorsMonitorService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author haojiang.
 * @Ddate 2020/9/27 18:08
 * @Description 组装统计分析SQL: 运维预测分析
 */
@Component
public class CreateAnalysisSQL extends BaseController {

    @Autowired
    private KeyIndicatorsMonitorService keyIndicatorsService;

    //获取echarts时间区间
    public List<String> getDateRange(PageData pageData) {
        List<String> dateRange = new ArrayList<>();
        //处理半小时、一小时周期时间
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                dateRange.add(pageData.getString("endTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                dateRange.add(pageData.getString("startTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime"))) {
                    dateRange.add(pageData.getString("startTime"));
                } else {
                    if ("1".equals(pageData.getString("period"))) { //半小时
                        for (int i = 0; i < 48; i++) {
                            if (pageData.getString("endTime").equals(LocalTime.parse(pageData.getString("startTime")).plusMinutes(30 * i).toString())) {
                                dateRange.add(pageData.getString("endTime"));
                                break;
                            } else {
                                dateRange.add(LocalTime.parse(pageData.getString("startTime")).plusMinutes(30 * i).toString());
                            }
                        }
                    } else { //一小时
                        for (int i = 0; i < 24; i++) {
                            if (pageData.getString("endTime").equals(LocalTime.parse(pageData.getString("startTime")).plusHours(i).toString())) {
                                dateRange.add(pageData.getString("endTime"));
                                break;
                            } else {
                                dateRange.add(LocalTime.parse(pageData.getString("startTime")).plusHours(i).toString());
                            }
                        }
                    }
                }
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            if (StringUtils.isBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始时间为空
                dateRange.add(pageData.getString("endTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isBlank(pageData.getString("endTime"))) { //结束时间为空
                dateRange.add(pageData.getString("startTime"));
            } else if (StringUtils.isNotBlank(pageData.getString("startTime")) && StringUtils.isNotBlank(pageData.getString("endTime"))) { //起始、结束时间都不为空
                if (pageData.getString("startTime").equals(pageData.getString("endTime") + "'")) {
                    dateRange.add(pageData.getString("startTime"));
                } else {
                    long distance = 0;
                    LocalDate startDate = null;
                    LocalDate endDate = null;
                    switch (pageData.getString("period")) {
                        case "3":
                            startDate = LocalDate.parse(pageData.getString("startTime"));
                            endDate = LocalDate.parse(pageData.getString("endTime"));
                            distance = ChronoUnit.DAYS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusDays(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                            });
                            break;
                        case "4":
                            startDate = LocalDate.parse(pageData.getString("startTime") + "-01");
                            endDate = LocalDate.parse(pageData.getString("endTime") + "-01");
                            distance = ChronoUnit.MONTHS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusMonths(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                            });
                            break;
                        case "5":
                            startDate = LocalDate.parse(pageData.getString("startTime") + "-01-01");
                            endDate = LocalDate.parse(pageData.getString("endTime") + "-01-01");
                            distance = ChronoUnit.YEARS.between(startDate, endDate);
                            Stream.iterate(startDate, date -> {
                                return date.plusYears(1);
                            }).limit(distance + 1).forEach(filter -> {
                                dateRange.add(filter.format(DateTimeFormatter.ofPattern("yyyy")));
                            });
                            break;
                    }
                }
            }
        }
        return dateRange;
    }

    /**
     * @Author haojiang
     * @Date 2020/10/24 16:38
     * @Description 参数维护：预测阈值SQL
     */
    public String geThresholdPredictionSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        if ("ucloud".equals(pageData.get("dataBase"))) {
            builder.append("SELECT GROUP_CONCAT(v.content) AS value FROM (");
            builder.append("SELECT a.dateTime,IFNULL(b.Count,0) AS content FROM (");
            for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
            }
            builder.append(") a");
            if ("1".equals(pageData.get("data_type"))) { //业务数据
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.index_id='" + pageData.get("index_id") + "' AND b.period='3' AND b.table_name LIKE 'ucloud.api_interface_system_data%' AND b.resource_id='" + pageData.get("resource") + "'");
            } else if ("2".equals(pageData.get("data_type"))) { //告警数据
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.index_id='" + pageData.get("index_id") + "' AND b.period='3' AND b.table_name='ucloud.api_interface_alarm_data'");
            }
            builder.append(" GROUP BY dateTime ORDER BY dateTime) v");
        } else if ("upm".equals(pageData.get("dataBase"))) {
            builder.append("SELECT GROUP_CONCAT(v.content) AS value FROM (");
            builder.append("SELECT a.dateTime,MAX(IFNULL(CAST(b.Count AS DECIMAL(16,6)),0)) AS content FROM (");
            for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
            }
            builder.append(") a");
            builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.index_id='" + pageData.get("index_id") + "' AND b.period='3' AND (b.lineId='" + pageData.get("resource") + "' OR b.resource_code='" + pageData.get("resource") + "')");
            builder.append(" GROUP BY dateTime ORDER BY dateTime) v");
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/9/27 18:10
     * @Description 组装预测分析SQL
     */
    public String getForecastSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        if ("devOps".equals(pageData.getString("type"))) { //运维
            builder.append("SELECT GROUP_CONCAT(v.content) AS value FROM (");
            if ("2".equals(pageData.getString("period"))) { //一小时
                builder.append("SELECT SUBSTR(a.dateTime,12,6) AS dateTime,IFNULL(b.Count,0) AS content FROM(");
                for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("SELECT '" + dateRange.get(i) + ":00' AS dateTime FROM DUAL");
                }
                builder.append(") a");
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
                builder.append("SELECT SUBSTR(a.dateTime,12,6) AS dateTime,IFNULL(b.Count,0) AS content FROM(");
                for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
                }
                builder.append(") a");
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            }
            builder.append(") v ORDER BY dateTime");
        } else if ("network".equals(pageData.getString("type"))) { //网络
            builder.append("SELECT GROUP_CONCAT(v.content) AS value FROM (");
            if ("2".equals(pageData.getString("period"))) { //一小时
                builder.append("SELECT SUBSTR(a.dateTime,12,6) AS dateTime,MAX(IFNULL(CAST(b.Count AS DECIMAL(18,6)),0)) AS content FROM(");
                for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("SELECT '" + dateRange.get(i) + ":00' AS dateTime FROM DUAL");
                }
                builder.append(") a");
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND (b.lineId='" + pageData.get("resources") + "' OR b.resource_code='" + pageData.get("resources") + "')");
                builder.append(" GROUP BY dateTime ORDER BY dateTime");
            } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
                builder.append("SELECT a.dateTime AS dateTime,MAX(IFNULL(CAST(b.Count AS DECIMAL(18,6)),0)) AS content FROM(");
                for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
                }
                builder.append(") a");
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND  (b.lineId='" + pageData.get("resources") + "' OR b.resource_code='" + pageData.get("resources") + "')");
                builder.append(" GROUP BY dateTime ORDER BY dateTime");
            }
            builder.append(") v ORDER BY dateTime");
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/9/27 18:10
     * @Description 组装预测分析SQL
     */
    public String getForecastSQL2(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT GROUP_CONCAT(v.content) AS value FROM (");
        if ("1".contains(pageData.getString("period"))) { //半小时
            builder.append("SELECT SUBSTR(o.dateTime,12,6) AS dateTime,IFNULL(o.Count,0) AS content FROM(");
            for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT a.dateTime,b.content AS Count FROM(SELECT '" + dateRange.get(i) + ":00' AS dateTime) a");
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_interface_system_data" + dateRange.get(i).replace("-", "").substring(0, 7) + " b ON a.dateTime=b.time");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "'");
            }
            builder.append(") o");
        } else if ("2".equals(pageData.getString("period"))) { //一小时
            builder.append("SELECT SUBSTR(a.dateTime,12,6) AS dateTime,IFNULL(b.Count,0) AS content FROM(");
            for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + ":00' AS dateTime FROM DUAL");
            }
            builder.append(") a");
            if ("devOps".equals(pageData.getString("type"))) { //运维
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                if (pageData.getString("index_name").contains("告警次数")) {
                    builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name='ucloud.api_interface_alarm_data'");
                } else {
                    builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
                }
            } else if ("network".equals(pageData.getString("type"))) { //网络
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_code='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            }
        } else {
            builder.append("SELECT a.dateTime,IFNULL(b.Count,0) AS content FROM(");
            for (int i = 0; i < dateRange.size(); i++) { //防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
            }
            builder.append(") a");
            if ("devOps".equals(pageData.getString("type"))) { //运维
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                if (pageData.getString("index_name").contains("告警次数")) {
                    builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name='ucloud.api_interface_alarm_data'");
                } else {
                    builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_id='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
                }
            } else if ("network".equals(pageData.getString("type"))) { //网络
                builder.append(" LEFT JOIN " + pageData.get("dataBase") + ".api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "'");
                builder.append(" AND b.index_id='" + pageData.get("indicators") + "' AND b.resource_code='" + pageData.get("resources") + "' AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            }
        }
        builder.append(") v ORDER BY dateTime");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/9/28 9:17
     * @Description 组装重点指标视图SQL: 重点指标峰值资源序时分析
     */
    public String getKeyIndicatorSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        if ("1".equals(pageData.getString("period"))) { //半小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("SELECT SUBSTR(a.dateTime,12,5) AS dateTime,MAX(CAST(IFNULL(b.content,0) AS DECIMAL(18,2))) AS value FROM(");
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + yesterday + " " + dateRange.get(i) + ":00' AS dateTime FROM DUAL");
            }
            builder.append(") a LEFT JOIN ucloud.api_interface_system_data" + yesterday.substring(0, 7).replace("-", "") + " b ON a.dateTime=b.time AND b.index_id='" + pageData.get("indicatorsId") + "'");
            builder.append(" GROUP BY dateTime ORDER BY dateTime");
        } else if ("2".equals(pageData.getString("period"))) { //一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("SELECT SUBSTR(a.dateTime,12,5) AS dateTime,MAX(CAST(IFNULL(b.Count,0) AS DECIMAL(18,2))) AS value FROM(");
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + yesterday + " " + dateRange.get(i) + ":00' AS dateTime FROM DUAL");
            }
            builder.append(") a LEFT JOIN ucloud.api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "' AND b.index_id='" + pageData.get("indicatorsId") + "'");
            builder.append(" GROUP BY dateTime ORDER BY dateTime");
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            builder.append("SELECT a.dateTime AS dateTime,MAX(CAST(IFNULL(b.Count,0) AS DECIMAL(18,2))) AS value FROM(");
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime FROM DUAL");
            }
            builder.append(") a LEFT JOIN ucloud.api_alarm_summary b ON a.dateTime=b.time AND b.period='" + pageData.get("period") + "' AND b.index_id='" + pageData.get("indicatorsId") + "'");
            builder.append(" GROUP BY dateTime ORDER BY dateTime");
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/9/28 9:17
     * @Description 组装重点状态指标视图SQL: 重点状态指标分析
     */
    public String getKeyStatusIndicatorSQL(PageData pageData, List<String> dateRange) {
        int start = 0;
        Map<String, Object> map = new HashMap<>();
        Set<String> tableList = new LinkedHashSet<>(); //去重，保证元素顺序
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.name AS name,SUM(v.value) AS value FROM (");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("(SELECT b.code_name AS name,COUNT(a.content) AS value");
            builder.append(" FROM ucloud.api_interface_system_data" + yesterday.substring(0, 7).replace("-", "") + " a");
            builder.append(" ALL LEFT JOIN system_docking.api_system_index_status b on a.index_id=b.index_id AND a.content=b.code_no");
            builder.append(" WHERE a.index_id='" + pageData.get("indicatorsId") + "' AND a.time BETWEEN '" + yesterday + " " + dateRange.get(0) + ":00' AND '" + yesterday + " " + dateRange.get(dateRange.size() - 1) + ":00'");
            builder.append(" GROUP BY b.code_name ORDER BY b.code_name)");
        } else if ("3".equals(pageData.getString("period"))) { //日
            StringBuilder condition = new StringBuilder();
            for (String date : dateRange) {
                condition.append(",'api_interface_system_data" + date.substring(0, 7).replace("-", "") + "'");
            }
            map.put("dataBase", "ucloud");
            map.put("table_name", "IN (" + condition.toString().substring(1) + ")");
            keyIndicatorsService.getTableByDataBase(map).forEach(tableMap -> {
                tableList.add(String.valueOf(tableMap.get("table_name")));
            });
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT b.code_name AS name,COUNT(a.content) AS value");
                builder.append(" FROM ucloud." + tableName + " a");
                builder.append(" ALL LEFT JOIN system_docking.api_system_index_status b on a.index_id=b.index_id AND a.content=b.code_no");
                builder.append(" WHERE a.index_id='" + pageData.get("indicatorsId") + "' AND a.time BETWEEN '" + dateRange.get(0) + " 00:00:00' AND '" + dateRange.get(dateRange.size() - 1) + " 24:00:00'");
                builder.append(" GROUP BY b.code_name ORDER BY b.code_name)");
                start++;
            }
        } else if ("4".equals(pageData.getString("period"))) { //月
            StringBuilder condition = new StringBuilder();
            for (String date : dateRange) {
                condition.append(",'api_interface_system_data" + date.replace("-", "") + "'");
            }
            map.put("dataBase", "ucloud");
            map.put("table_name", "IN (" + condition.toString().substring(1) + ")");
            keyIndicatorsService.getTableByDataBase(map).forEach(tableMap -> {
                tableList.add(String.valueOf(tableMap.get("table_name")));
            });
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT b.code_name AS name,COUNT(a.content) AS value");
                builder.append(" FROM ucloud." + tableName + " a");
                builder.append(" ALL LEFT JOIN system_docking.api_system_index_status b on a.index_id=b.index_id AND a.content=b.code_no");
                builder.append(" WHERE a.index_id='" + pageData.get("indicatorsId") + "'");
                builder.append(" GROUP BY b.code_name ORDER BY b.code_name)");
                start++;
            }
        } else if ("5".equals(pageData.getString("period"))) { //年
            for (String date : dateRange) {
                map.put("dataBase", "ucloud");
                map.put("table_name", "LIKE 'api_interface_system_data" + date + "%'");
                keyIndicatorsService.getTableByDataBase(map).forEach(tableMap -> {
                    tableList.add(String.valueOf(tableMap.get("table_name")));
                });
            }
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT b.code_name AS name,COUNT(a.content) AS value");
                builder.append(" FROM ucloud." + tableName + " a");
                builder.append(" ALL LEFT JOIN system_docking.api_system_index_status b on a.index_id=b.index_id AND a.content=b.code_no");
                builder.append(" WHERE a.index_id='" + pageData.get("indicatorsId") + "'");
                builder.append(" GROUP BY  b.code_name ORDER BY b.code_name)");
                start++;
            }
        }
        builder.append(") v GROUP BY name ORDER BY name");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/9/28 9:17
     * @Description 组装重点指标视图SQL: 重点指标资源排名分析
     */
    public String getResourceOrderSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.name,ROUND(IFNULL(v.content,0),2) AS value FROM (");
        if ("1".contains(pageData.getString("period"))) { //半小时(查明细表)
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("SELECT b.resource_desc AS name,MAX(CAST(a.content AS DECIMAL(18,12))) AS content");
            builder.append(" FROM ucloud.api_interface_system_data" + yesterday.substring(0, 7).replace("-", "") + " a");
            builder.append(" LEFT JOIN `system_docking`.api_system_resource b ON a.resource_id=b.ID AND b.is_enable='1'");
            builder.append(" WHERE a.index_id='" + pageData.get("indicatorsId") + "' AND a.time BETWEEN '" + yesterday + " " + dateRange.get(0) + ":00' AND '" + yesterday + " " + dateRange.get(dateRange.size() - 1) + ":00'");
            builder.append(" GROUP BY name ORDER BY content DESC,name LIMIT 15");
        } else if ("2".contains(pageData.getString("period"))) { //一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("SELECT a.resource_desc AS name,MAX(CAST(b.Count AS DECIMAL(18,12))) AS content");
            builder.append(" FROM `system_docking`.api_system_resource a");
            builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.ID=b.resource_id AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            builder.append(" WHERE a.is_enable='1' AND b.index_id='" + pageData.get("indicatorsId") + "' AND b.time BETWEEN '" + yesterday + " " + dateRange.get(0) + ":00' AND '" + yesterday + " " + dateRange.get(dateRange.size() - 1) + ":00'");
            builder.append(" GROUP BY name ORDER BY content DESC,name LIMIT 15");
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            builder.append("SELECT a.resource_desc AS name,MAX(CAST(b.Count AS DECIMAL(18,2))) AS content");
            builder.append(" FROM `system_docking`.api_system_resource a");
            builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.ID=b.resource_id AND b.table_name LIKE 'ucloud.api_interface_system_data%'");
            builder.append(" WHERE a.is_enable='1' AND b.index_id='" + pageData.get("indicatorsId") + "' AND b.time BETWEEN '" + dateRange.get(0) + "' AND '" + dateRange.get(dateRange.size() - 1) + "'");
            builder.append(" GROUP BY name ORDER BY content DESC,name LIMIT 15");
        }
        builder.append(") v GROUP BY name ORDER BY value,name LIMIT 15");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 监测器告警构成分析---按监测器所在平台
     */
    public String getCompositionAnalysis1SQL(PageData pageData, List<String> dateRange, List<Map<String, Object>> platformData) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.name,SUM(v.value) AS value FROM( ");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (Map<String, Object> data : platformData) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + data.get("label") + "' as name,IFNULL(SUM(b.Count),0) AS value");
                builder.append(" FROM system_docking.api_interface_system_platform_info a");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.platform_id AND b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND b.platform_id='" + data.get("value") + "' AND b.time BETWEEN '" + yesterday + " " + dateRange.get(0) + ":00' AND '" + yesterday + " " + dateRange.get(dateRange.size() - 1) + ":00'");
                builder.append(" WHERE EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id)");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (Map<String, Object> data : platformData) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + data.get("label") + "' as name,IFNULL(SUM(b.Count),0) as value");
                builder.append(" FROM system_docking.api_interface_system_platform_info a");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.platform_id AND b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND b.platform_id='" + data.get("value") + "' AND b.time BETWEEN '" + dateRange.get(0) + "' AND '" + dateRange.get(dateRange.size() - 1) + "'");
                builder.append(" WHERE EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id)");
                start++;
            }
        }
        builder.append(") v GROUP BY name");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 监测器告警构成分析---按监测器报警次数
     */
    public String getCompositionAnalysis2SQL(PageData pageData, List<String> dateRange, List<Map<String, Object>> indexList) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.name,SUM(v.value) AS value FROM( ");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (Map<String, Object> data : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + data.get("label") + "' as name,IFNULL(SUM(b.Count),0) as value");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id AND b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND b.index_id='" + data.get("value") + "' AND b.time BETWEEN '" + yesterday + " " + dateRange.get(0) + ":00' AND '" + yesterday + " " + dateRange.get(dateRange.size() - 1) + ":00'");
                builder.append(" WHERE EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id)");
                builder.append(" GROUP BY name");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (Map<String, Object> data : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + data.get("label") + "' as name,IFNULL(SUM(b.Count),0) as value");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id AND b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND b.index_id='" + data.get("value") + "' AND b.time BETWEEN '" + dateRange.get(0) + "' AND '" + dateRange.get(dateRange.size() - 1) + "'");
                builder.append(" WHERE EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id)");
                start++;
            }
        }
        builder.append(") v GROUP BY name");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 监测器告警序时分析
     */
    public String getChronologyAnalysisSQL(PageData pageData, List<String> dateRange, List<String> indexList) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (String indexId : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT v.name,CONCAT('{\"',v.alarm_time,'\":\"',IFNULL(v.alarm_count,0),'\"}') AS value FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("(SELECT a.index_name AS name,'" + dateRange.get(i) + "' AS alarm_time,SUM(b.Count) AS alarm_count");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id");
                    builder.append(" WHERE b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND b.index_id='" + indexId + "' AND b.time='" + yesterday + " " + dateRange.get(i) + ":00'");
                    builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id))");
                }
                builder.append(") v");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (String indexId : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT v.name,CONCAT('{\"',v.alarm_time,'\":\"',IFNULL(v.alarm_count,0),'\"}') AS value FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("(SELECT a.index_name AS name,'" + dateRange.get(i) + "' AS alarm_time,SUM(b.Count) AS alarm_count");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id");
                    builder.append(" WHERE b.table_name='ucloud.api_interface_alarm_data' AND b.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND b.index_id='" + indexId + "' AND b.time='" + dateRange.get(i) + "'");
                    builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where b.resource_id=id))");
                }
                builder.append(") v");
                start++;
            }
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 分平台监测器告警分析
     */
    public String getPlatformAlarmAnalysisSQL(PageData pageData, List<String> dateRange, List<String> indexList) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (String indexId : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT v.name,CONCAT('{\"',v.alarm_time,'\":\"',v.alarm_count,'\"}') AS value FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("(SELECT a.index_name AS name,'" + dateRange.get(i) + "' AS alarm_time,IFNULL(SUM(d.Count),0) AS alarm_count");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.id");
                    builder.append(" LEFT JOIN system_docking.api_interface_system_info c ON b.system_id=c.id");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary d ON a.id=d.index_id");
                    builder.append(" WHERE d.table_name='ucloud.api_interface_alarm_data' AND d.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND d.index_id='" + indexId + "' AND d.time='" + yesterday + " " + dateRange.get(i) + ":00'");
                    builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where d.resource_id=id))");
                }
                builder.append(") v");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            for (String indexId : indexList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT v.name,CONCAT('{\"',v.alarm_time,'\":\"',v.alarm_count,'\"}') AS value FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("(SELECT a.index_name AS name,'" + dateRange.get(i) + "' AS alarm_time,IFNULL(SUM(d.Count),0) AS alarm_count");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.id");
                    builder.append(" LEFT JOIN system_docking.api_interface_system_info c ON b.system_id=c.id");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary d ON a.id=d.index_id");
                    builder.append(" WHERE d.table_name='ucloud.api_interface_alarm_data' AND d.period='" + pageData.get("period") + "' AND a.is_monitor='1' AND d.index_id='" + indexId + "' AND d.time='" + dateRange.get(i) + "'");
                    builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where d.resource_id=id))");
                }
                builder.append(")v");
                start++;
            }
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 获取所选周期内的告警资源
     */
    public String getAlarmResourceSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.value,v.label,v.index_id,v.alarm_count FROM(");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ");
                }
                builder.append("(SELECT d.id AS value,d.resource_desc AS label,c.index_id,IFNULL(SUM(c.Count),0) AS alarm_count");
                builder.append(" FROM system_docking.api_interface_system_platform_info a");
                builder.append(" LEFT JOIN system_docking.api_platform_index_info b ON a.id=b.platform_id");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary c ON b.id=c.index_id");
                builder.append(" LEFT JOIN system_docking.api_system_resource d ON c.resource_id = d.id ");
                builder.append(" WHERE c.table_name='ucloud.api_interface_alarm_data' AND b.is_monitor='1' AND a.is_enable='1' AND b.is_enable='1' AND c.period='" + pageData.get("period") + "' AND c.platform_id='" + pageData.get("platform") + "' AND c.time = '" + yesterday + " " + dateRange.get(i) + ":00'");
                builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where c.resource_id=id)");
                builder.append(" GROUP BY d.id ORDER BY alarm_count DESC,label)");
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ");
                }
                builder.append("(SELECT d.id AS value,d.resource_desc AS label,c.index_id,IFNULL(SUM(c.Count),0) AS alarm_count");
                builder.append(" FROM system_docking.api_interface_system_platform_info a");
                builder.append(" LEFT JOIN system_docking.api_platform_index_info b ON a.id=b.platform_id");
                builder.append(" LEFT JOIN ucloud.api_alarm_summary c ON b.id=c.index_id");
                builder.append(" LEFT JOIN system_docking.api_system_resource d ON c.resource_id = d.id ");
                builder.append(" WHERE c.table_name='ucloud.api_interface_alarm_data' AND b.is_monitor='1' AND a.is_enable='1' AND b.is_enable='1' AND c.period='" + pageData.get("period") + "' AND c.platform_id='" + pageData.get("platform") + "' AND c.time='" + dateRange.get(i) + "'");
                builder.append(" AND EXISTS(select 1 from system_docking.api_system_resource where c.resource_id=id)");
                builder.append(" GROUP BY d.id ORDER BY alarm_count DESC,label)");
            }
        }
        builder.append(") v WHERE v.value IS NOT NULL GROUP BY index_id,value ORDER BY alarm_count,value LIMIT 10");
        return builder.toString();
    }


    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 获取所选周期内的告警资源下的指标
     */
    public String getAlarmResourceOfIndexSQL(PageData pageData, List<String> dateRange, List<String> resourceData) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (String resourceId : resourceData) {
                if (start > 0) {
                    builder.append(" UNION ");
                }
                builder.append("SELECT v.value,v.label FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ");
                    }
                    builder.append("(SELECT a.id AS value,a.index_name AS label");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id");
                    builder.append(" LEFT JOIN system_docking.api_system_resource c ON b.resource_id=c.id");
                    builder.append(" WHERE b.table_name='ucloud.api_interface_alarm_data' AND b.resource_id='" + resourceId + "' AND a.is_monitor='1' AND a.is_enable='1' AND c.is_enable='1' AND b.period='" + pageData.get("period") + "' AND b.platform_id='" + pageData.get("platform") + "' AND b.time = '" + yesterday + " " + dateRange.get(i) + ":00')");
                }
                builder.append(") v");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (String resourceId : resourceData) {
                if (start > 0) {
                    builder.append(" UNION ");
                }
                builder.append("SELECT v.value,v.label FROM(");
                for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                    if (i > 0) {
                        builder.append(" UNION ");
                    }
                    builder.append("(SELECT a.id AS value,a.index_name AS label");
                    builder.append(" FROM system_docking.api_platform_index_info a");
                    builder.append(" LEFT JOIN ucloud.api_alarm_summary b ON a.id=b.index_id");
                    builder.append(" LEFT JOIN system_docking.api_system_resource c ON b.resource_id=c.id");
                    builder.append(" WHERE b.table_name='ucloud.api_interface_alarm_data' AND b.resource_id='" + resourceId + "' AND a.is_monitor='1' AND a.is_enable='1' AND c.is_enable='1' AND b.period='" + pageData.get("period") + "' AND b.platform_id='" + pageData.get("platform") + "' AND b.time='" + dateRange.get(i) + "')");
                }
                builder.append(") v");
                start++;
            }
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/07 16:10
     * @Description 组装监测告警专题视图SQL: 分平台分设备监测器告警分析
     */
    public String getDeviceAlarmAnalysisSQL(PageData pageData, List<String> dateRange, Map<String, Object> indexMap, List<String> resourceList) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            String date = String.valueOf(dateRange).replace("[", "'" + yesterday + " ").replace(",", ":00','" + yesterday + " ").replace("]", ":00'");
            for (String indexId : indexMap.keySet()) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + indexMap.get(indexId) + "'AS name,GROUP_CONCAT(IFNULL(v.alarm_count,0)) AS value FROM(");
                for (int i = 0; i < resourceList.size(); i++) {
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("(SELECT '" + resourceList.get(i) + "'AS id,SUM(a.Count) AS alarm_count");
                    builder.append(" FROM ucloud.api_alarm_summary a");
                    builder.append(" WHERE a.table_name='ucloud.api_interface_alarm_data' AND a.period='" + pageData.get("period") + "' AND a.resource_id='" + resourceList.get(i) + "' AND a.index_id='" + indexId + "' AND a.time IN ( " + date + "))");
                }
                builder.append(") v");
                start++;
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            for (String indexId : indexMap.keySet()) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + indexMap.get(indexId) + "'AS name,GROUP_CONCAT(v.alarm_count) AS value FROM(");
                for (int i = 0; i < resourceList.size(); i++) {
                    if (i > 0) {
                        builder.append(" UNION ALL ");
                    }
                    builder.append("SELECT '" + resourceList.get(i) + "'AS id,SUM(v.alarm_count) AS alarm_count FROM(");
                    for (int j = 0; j < dateRange.size(); j++) {
                        if (j > 0) {
                            builder.append(" UNION ALL ");
                        }
                        builder.append("(SELECT '" + resourceList.get(i) + "'AS id,IFNULL(SUM(a.Count),0) AS alarm_count");
                        builder.append(" FROM ucloud.api_alarm_summary a");
                        builder.append(" WHERE a.table_name='ucloud.api_interface_alarm_data' AND a.period='" + pageData.get("period") + "' AND a.resource_id='" + resourceList.get(i) + "' AND a.index_id='" + indexId + "' AND a.time='" + dateRange.get(j) + "')");
                    }
                    builder.append(") v");
                }
                builder.append(") v");
                start++;
            }
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/12 10:50
     * @Description 组装统计分析运维综合查询QL
     */
    public String getDevOpsComprehensiveQueryDataSQL(PageData pageData, List<String> dateRange) {
        int start = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("dataBase", "ucloud");
        Set<String> tableList = new LinkedHashSet<>(); //去重，保证元素顺序
        StringBuilder builder = new StringBuilder();
        StringBuilder dateBuilder = new StringBuilder();
        StringBuilder conditon = new StringBuilder(); //指标、平台、资源条件
        if (StringUtils.isNotBlank(pageData.getString("indicators"))) { //选择指标
            conditon.append(" AND a.ID='" + pageData.getString("indicators") + "'");
        }
        if (StringUtils.isNotBlank(pageData.getString("platform"))) { //选择平台
            conditon.append(" AND a.platform_id='" + pageData.getString("platform") + "'");
        }
        if (StringUtils.isNotBlank(pageData.getString("resources"))) { //选择资源
            conditon.append(" AND d.resource_id='" + pageData.getString("resources") + "'");
        }
        builder.append("SELECT v.* FROM (");
        if ("3".equals(pageData.getString("period"))) { //日
            //获取日期所在的表
            for (String date : dateRange) {
                dateBuilder.append(",'api_interface_system_data" + date.substring(0, 7).replace("-", "") + "'");
            }
            map.put("table_name", "IN(" + dateBuilder.toString().substring(1) + ")");
            this.getClickhouseTables(map).forEach(tableMap -> {
                tableList.add(String.valueOf(tableMap.get("table_name")));
            });
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT a.index_name AS index_name,b.platfrom_name AS platfrom_name,c.system_name AS system_name,e.resource_desc AS resource_desc,d.resource_ip AS resource_ip,d.time AS dateTime,CONCAT(d.content,a.index_unit) AS index_value");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.ID");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_info c ON b.system_id=c.ID");
                builder.append(" ALL LEFT JOIN ucloud." + tableName + " d ON a.ID=d.index_id");
                builder.append(" ALL LEFT JOIN system_docking.api_system_resource e ON d.resource_id=e.ID");
                builder.append(" WHERE a.is_enable='1' AND a.is_monitor='0' AND b.is_enable='1' AND c.is_enable='1' AND e.is_enable='1' AND LEFT(d.time,10) IN(");
                for (int i = 0; i < dateRange.size(); i++) {
                    if (i == 0) {
                        builder.append("'" + dateRange.get(i) + "'");
                    } else {
                        builder.append(",'" + dateRange.get(i) + "'");
                    }
                }
                builder.append(")" + conditon.toString());
                start++;
            }
        } else if ("4".equals(pageData.getString("period"))) { //月
            //获取日期所在的表
            for (String date : dateRange) {
                dateBuilder.append(",'api_interface_system_data" + date.replace("-", "") + "'");
            }
            map.put("table_name", "IN(" + dateBuilder.toString().substring(1) + ")");
            this.getClickhouseTables(map).forEach(tableMap -> {
                tableList.add(String.valueOf(tableMap.get("table_name")));
            });
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT a.index_name AS index_name,b.platfrom_name AS platfrom_name,c.system_name AS system_name,e.resource_desc AS resource_desc,d.resource_ip AS resource_ip,d.time AS dateTime,CONCAT(d.content,a.index_unit) AS index_value");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.ID");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_info c ON b.system_id=c.ID");
                builder.append(" ALL LEFT JOIN ucloud." + tableName + " d ON a.ID=d.index_id");
                builder.append(" ALL LEFT JOIN system_docking.api_system_resource e ON d.resource_id=e.ID");
                builder.append(" WHERE a.is_enable='1' AND a.is_monitor='0' AND b.is_enable='1' AND c.is_enable='1' AND e.is_enable='1'" + conditon.toString());
                start++;
            }
        } else if ("5".equals(pageData.getString("period"))) { //年
            //获取日期所在的表
            for (String date : dateRange) {
                map.put("table_name", "LIKE 'api_interface_system_data" + date + "%'");
                this.getClickhouseTables(map).forEach(tableMap -> {
                    tableList.add(String.valueOf(tableMap.get("table_name")));
                });
            }
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT a.index_name AS index_name,b.platfrom_name AS platfrom_name,c.system_name AS system_name,e.resource_desc AS resource_desc,d.resource_ip AS resource_ip,d.time AS dateTime,CONCAT(d.content,a.index_unit) AS index_value");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.ID");
                builder.append(" ALL LEFT JOIN system_docking.api_interface_system_info c ON b.system_id=c.ID");
                builder.append(" ALL LEFT JOIN ucloud." + tableName + " d ON a.ID=d.index_id");
                builder.append(" ALL LEFT JOIN system_docking.api_system_resource e ON d.resource_id=e.ID");
                builder.append(" WHERE a.is_enable='1' AND a.is_monitor='0' AND b.is_enable='1' AND c.is_enable='1' AND e.is_enable='1'" + conditon.toString());
                start++;
            }
        }
        builder.append(")v ORDER BY dateTime,index_name");
        return builder.toString();
    }


    /**
     * @Author haojiang
     * @Date 2020/10/12 10:50
     * @Description 组装统计分析网络综合查询QL
     */
    public String getNetworkComprehensiveQueryDataSQL(PageData pageData, List<String> dateRange) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder head = new StringBuilder();
        StringBuilder conditon = new StringBuilder(); //指标、平台、资源条件
        String queryTable = ""; //查询表
        if ("ea0c68b5192545e5828f69ff3d682ebd".equals(pageData.get("indicators"))) { //网络日志表指标
            queryTable = "netperformanceeventlog_";
            head.append("(SELECT d.ID AS index_id,d.index_name AS index_name,d.platform_id AS platform_id,c.platfrom_name AS platfrom_name,a.resource_desc AS resource_desc,'' AS resource_ip,b.time AS dateTime,b.triggerCondition AS index_value");
            head.append(" FROM system_docking.api_system_resource a");
            head.append(" ALL LEFT JOIN upm.@tableName b ON a.resource_code=b.lineId");
            head.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info c ON a.platform_id=c.ID");
            head.append(" ALL LEFT JOIN system_docking.api_platform_index_info d ON c.ID=d.platform_id");
            head.append(" WHERE b.name='网络无流量' AND d.ID='" + pageData.get("indicators") + "'AND b.id IS NOT NULL");
            //页面查询条件
            if (StringUtils.isNotBlank(pageData.getString("platform"))) { //选择平台
                head.append(" AND c.ID='" + pageData.getString("platform") + "'");
            }
            if (StringUtils.isNotBlank(pageData.getString("resources"))) { //选择资源
                head.append(" AND b.lineId='" + pageData.get("resources") + "'");
            }
            head.append(")");
        } else if ("bc30f16fa02345809dd5713dbd223a26".equals(pageData.get("indicators"))) { //异常行为日志表指标
            queryTable = "alarmlogabnormalbehavior_";
            head.append("(SELECT d.ID AS index_id,d.index_name AS index_name,d.platform_id AS platform_id,c.platfrom_name AS platfrom_name,a.resource_desc AS resource_desc,b.ipAddr AS resource_ip,b.time AS dateTime,b.triggerCondition AS index_value");
            head.append(" FROM system_docking.api_system_resource a");
            head.append(" ALL LEFT JOIN upm.@tableName b ON a.resource_code=b.netsegmentId");
            head.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info c ON a.platform_id=c.ID");
            head.append(" ALL LEFT JOIN system_docking.api_platform_index_info d ON c.ID=d.platform_id");
            head.append(" WHERE b.name='交易无响应' AND d.ID='" + pageData.get("indicators") + "'AND b.id IS NOT NULL");
            //页面查询条件
            if (StringUtils.isNotBlank(pageData.getString("platform"))) { //选择平台
                head.append(" AND c.ID='" + pageData.get("platform") + "'");
            }
            if (StringUtils.isNotBlank(pageData.getString("resources"))) { //选择资源
                head.append(" AND b.netsegmentId='" + pageData.get("resources") + "'");
            }
            head.append(")");
        } else {
            queryTable = "api_interface_system_data";
            head.append("(SELECT a.ID AS index_id,a.index_name AS index_name,a.platform_id AS platform_id,b.platfrom_name AS platfrom_name,d.resource_desc AS resource_desc,c.resource_ip AS resource_ip,c.time AS dateTime,c.content AS index_value");
            head.append(" FROM system_docking.api_platform_index_info a");
            head.append(" ALL LEFT JOIN system_docking.api_interface_system_platform_info b ON a.platform_id=b.ID");
            head.append(" ALL LEFT JOIN upm.@tableName c ON a.ID=c.index_id");
            head.append(" ALL LEFT JOIN system_docking.api_system_resource d ON c.resource_code=d.resource_code");
            head.append(" WHERE c.index_id='" + pageData.get("indicators") + "' AND c.ID IS NOT NULL");
            //页面查询条件
            if (StringUtils.isNotBlank(pageData.getString("platform"))) { //选择平台
                head.append(" AND a.platform_id='" + pageData.get("platform") + "'");
            }
            if (StringUtils.isNotBlank(pageData.getString("resources"))) { //选择资源
                head.append(" AND c.resource_code='" + pageData.get("resources") + "'");
            }
            head.append(")");
        }
        StringBuilder dateBuilder = new StringBuilder();
        builder.append("SELECT v.* FROM(");
        if ("3".equals(pageData.getString("period"))) { //日
            Map<String, Object> map = new HashMap<>();
            map.put("dataBase", "upm");
            Set<String> tableList = new LinkedHashSet<>(); //去重，保证元素顺序
            for (String date : dateRange) {
                dateBuilder.append(",'" + queryTable + date.replace("-", "") + "'");
            }
            map.put("table_name", "IN(" + dateBuilder.toString().substring(1) + ")");
            this.getClickhouseTables(map).forEach(tableMap -> {
                tableList.add(String.valueOf(tableMap.get("table_name")));
            });
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append(head.toString().replace("@tableName", tableName));
                start++;
            }
        } else if ("4".equals(pageData.getString("period"))) { //月
            Map<String, Object> map = new HashMap<>();
            Set<String> tableList = new LinkedHashSet<>(); //去重，保证元素顺序
            for (String date : dateRange) {
                map.put("dataBase", "upm");
                map.put("table_name", "LIKE '" + queryTable + date.replace("-", "") + "%'");
                this.getClickhouseTables(map).forEach(tableMap -> {
                    tableList.add(String.valueOf(tableMap.get("table_name")));
                });
            }
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append(head.toString().replace("@tableName", tableName));
                start++;
            }
        } else if ("5".equals(pageData.getString("period"))) { //年
            Map<String, Object> map = new HashMap<>();
            Set<String> tableList = new LinkedHashSet<>(); //去重，保证元素顺序
            for (String date : dateRange) {
                map.put("dataBase", "upm");
                map.put("table_name", "LIKE '" + queryTable + date + "%'");
                this.getClickhouseTables(map).forEach(tableMap -> {
                    tableList.add(String.valueOf(tableMap.get("table_name")));
                });
            }
            //遍历表格，组装查询SQL
            for (String tableName : tableList) {
                if (start > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append(head.toString().replace("@tableName", tableName));
                start++;
            }
        }
        builder.append(")v  ORDER BY dateTime,platform_id");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/14 11:20
     * @Description 组装数据包发送接收情况分析SQL
     */
    public String getDataPacketSendAndReceiveSQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.dateTime,v.sendData,v.receiveData FROM (");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime,IFNULL(SUM(o.sendData),0) AS sendData,IFNULL(SUM(o.receiveData),0) AS receiveData FROM(");
                builder.append("(SELECT CASE WHEN a.id='bcb93e91e00e4b0dacd5d16a375d98a7' THEN b.Count END AS sendData,");
                builder.append("CASE WHEN a.id='786fcbdb07d3423bb13779c172ed6c99' THEN b.Count END AS receiveData");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" LEFT JOIN upm.api_alarm_summary b ON a.id=b.index_id");
                builder.append(" WHERE b.period='" + pageData.get("period") + "' AND a.id IN('bcb93e91e00e4b0dacd5d16a375d98a7','786fcbdb07d3423bb13779c172ed6c99') AND a.is_enable='1' AND b.time='" + yesterday + " " + dateRange.get(i) + ":00' GROUP BY index_id)) o");
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            for (int i = 0; i < dateRange.size(); i++) {
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("SELECT '" + dateRange.get(i) + "' AS dateTime,IFNULL(SUM(o.sendData),0) AS sendData,IFNULL(SUM(o.receiveData),0) AS receiveData FROM(");
                builder.append("(SELECT CASE WHEN a.id='bcb93e91e00e4b0dacd5d16a375d98a7' THEN b.Count END AS sendData,");
                builder.append("CASE WHEN a.id='786fcbdb07d3423bb13779c172ed6c99' THEN b.Count END AS receiveData");
                builder.append(" FROM system_docking.api_platform_index_info a");
                builder.append(" LEFT JOIN upm.api_alarm_summary b ON a.id=b.index_id");
                builder.append(" WHERE b.period='" + pageData.get("period") + "' AND a.id IN('bcb93e91e00e4b0dacd5d16a375d98a7','786fcbdb07d3423bb13779c172ed6c99') AND a.is_enable='1' AND b.time='" + dateRange.get(i) + "' GROUP BY index_id)) o");
            }
        }
        builder.append(") v GROUP BY dateTime ORDER BY dateTime");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/14 11:20
     * @Description 组装网络(DNS)性能警报分析SQL
     */
    public String getNetworkPerformanceSQL(PageData pageData, List<String> dateRange, List<String> legendIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT v.dateTime,v.alarm_count FROM (");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,a.Count AS alarm_count");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + legendIds.get(0) + "' AND a.time='" + yesterday + " " + dateRange.get(i) + ":00')");
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            for (int i = 0; i < dateRange.size(); i++) {
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,a.Count AS alarm_count");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + legendIds.get(0) + "' AND a.time='" + dateRange.get(i) + "')");
            }
        }
        builder.append(") v ORDER BY dateTime");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/14 11:20
     * @Description 组装重置率/无响应率排名前三的链路SQL
     */
    public String getConnectionTop3SQL(PageData pageData, List<String> dateRange) {
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            builder.append("SELECT b.resource_code,a.resource_desc,CAST(b.count AS DECIMAL(9,4)) AS content");
            builder.append(" FROM system_docking.api_system_resource a");
            builder.append(" LEFT JOIN upm.api_alarm_summary b ON a.resource_code=b.resource_code");
            builder.append(" WHERE b.period='" + pageData.get("period") + "' AND b.index_id='" + pageData.get("index_id") + "' AND a.is_enable = '1' AND b.time IN(");
            for (int i = 0; i < dateRange.size(); i++) {
                if (i == 0) {
                    builder.append("'" + yesterday + " " + dateRange.get(i) + ":00'");
                } else {
                    builder.append(",'" + yesterday + " " + dateRange.get(i) + ":00'");
                }
            }
            builder.append(") GROUP BY resource_code ORDER BY content DESC LIMIT 3");
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            builder.append("SELECT b.resource_code,a.resource_desc,CAST(b.count AS DECIMAL(9,4)) AS content");
            builder.append(" FROM system_docking.api_system_resource a");
            builder.append(" LEFT JOIN upm.api_alarm_summary b ON a.resource_code=b.resource_code");
            builder.append(" WHERE b.period='" + pageData.get("period") + "' AND b.index_id='" + pageData.get("index_id") + "' AND a.is_enable = '1' AND b.time IN(");
            for (int i = 0; i < dateRange.size(); i++) {
                if (i == 0) {
                    builder.append("'" + dateRange.get(i) + "'");
                } else {
                    builder.append(",'" + dateRange.get(i) + "'");
                }
            }
            builder.append(") GROUP BY resource_code ORDER BY content DESC,resource_code LIMIT 3");
        }
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/14 11:20
     * @Description 组装建立连接情况构成分析SQL
     */
    public String getConnectionCompositionSQL(PageData pageData, List<String> dateRange, String resourceCode, String compositionId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT GROUP_CONCAT(IFNULL(v.content,0)) AS value FROM (");
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (int i = 0; i < dateRange.size(); i++) {
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,MAX(CAST(a.count AS DECIMAL(9,4))) AS content");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + compositionId + "' AND a.resource_code='" + resourceCode + "' AND a.time='" + yesterday + " " + dateRange.get(i) + ":00')");
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日、月、年
            for (int i = 0; i < dateRange.size(); i++) {
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,MAX(CAST(a.count AS DECIMAL(9,4))) AS content");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + compositionId + "' AND a.resource_code='" + resourceCode + "' AND a.time='" + dateRange.get(i) + "')");
            }
        }
        builder.append(") v ORDER BY dateTime");
        return builder.toString();
    }

    /**
     * @Author haojiang
     * @Date 2020/10/14 11:20
     * @Description 组装异常行为警报分析SQL
     */
    public String getAbnormalBehaviorSQL(PageData pageData, List<String> dateRange, List<String> legendIds) {
        int start = 0;
        StringBuilder builder = new StringBuilder();
        if ("1,2".contains(pageData.getString("period"))) { //半小时、一小时
            String yesterday = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //前一天
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,a.Count AS alarm_count");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + legendIds.get(0) + "' AND a.time='" + yesterday + " " + dateRange.get(i) + ":00')");
            }
        } else if ("3,4,5".contains(pageData.getString("period"))) { //日
            for (int i = 0; i < dateRange.size(); i++) {//防止有日期不存在数据，从而少返回数据
                if (i > 0) {
                    builder.append(" UNION ALL ");
                }
                builder.append("(SELECT '" + dateRange.get(i) + "' AS dateTime,a.Count AS alarm_count");
                builder.append(" FROM upm.api_alarm_summary a");
                builder.append(" WHERE a.period='" + pageData.get("period") + "' AND a.index_id='" + legendIds.get(0) + "' AND a.time='" + dateRange.get(i) + "')");
            }
        }
        return builder.toString();
    }
}
