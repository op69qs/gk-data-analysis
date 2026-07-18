package org.jeecg.modules.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class IndexChartsHelper {
    public static String createIndexPieSQL(List<Map<String, String>> info, String[] columns,
                                            PageData pageData) {
        if (CollectionUtils.isEmpty(info)) { return null; }
        return createSUBSql(info, columns, pageData, new StringBuilder(" "), null).append(" ").toString();
    }

    public static String createIndexUnionSQL(List<Map<String, String>> info, String[] columns,
                                              PageData pageData, String checked) {
        if (CollectionUtils.isEmpty(info)) { return null; }
        StringBuilder sql = new StringBuilder("SELECT V.* FROM (SELECT");
        for (int i = 0; i < info.size(); i++) {
            sql.append(" IFNULL(SUM(IF(aa.COLID = '").append(columns[i])
                    .append("',VALUE,NULL)),'') AS '").append(columns[i]).append("',");
        }
        sql.append(" aa.COLID, aa.ACCOUNT_DATE, aa.ACCOUNT_PERIOD, aa.CODE, aa.GK FROM ( ");
        createSUBSql(info, columns, pageData, sql, checked);
        return sql.append(" ) aa GROUP BY aa.ACCOUNT_PERIOD, aa.GK ) V ")
                .append("ORDER BY V.ACCOUNT_PERIOD, V.GK ").toString();
    }

    private static StringBuilder createSUBSql(List<Map<String, String>> info, String[] columns,
                                               PageData pageData, StringBuilder sql, String checked) {
        String start = pageData.getString("startDate").trim();
        String end = pageData.getString("endDate").trim();
        for (int i = 0; i < info.size(); i++) {
            sql.append(" SELECT '").append(columns[i]).append("' AS COLID, ")
                    .append("ACCOUNT_PERIOD AS ACCOUNT_DATE, ")
                    .append("REPLACE(REPLACE(REPLACE(REPLACE(ACCOUNT_PERIOD,'Q1','年第一季度'),")
                    .append("'Q2','年第二季度'),'Q3','年第三季度'),'Q4','年第四季度') AS ACCOUNT_PERIOD, ")
                    .append("INDEX_DIM_CODE AS CODE, INDEX_DIM_DESCR AS GK, INDEX_VALUE AS VALUE ")
                    .append("FROM indicators_lib.`").append(info.get(i).get("INDEX_CORRE_TABLE"))
                    .append("` WHERE DIMENSION_FLAG = '").append(pageData.getString("dimensionFlag"))
                    .append("' AND PERIOD_FLAG = '").append(pageData.getString("periodFlag"))
                    .append("' AND INDEX_ID = '").append(columns[i])
                    .append("' AND ACCOUNT_PERIOD >='").append(start)
                    .append("' AND ACCOUNT_PERIOD <='").append(end).append("'");
            if (StringUtils.isNotEmpty(checked)) {
                sql.append(" AND find_in_set(INDEX_ID, '").append(checked).append("') ");
            }
            if (i < info.size() - 1) { sql.append(" UNION ALL "); }
        }
        return sql;
    }

    public static List<String> setEChartsScale(Map<String, Object> map) {
        List<String> result = new ArrayList<>();
        String period = map.get("periodFlag").toString();
        String start = map.get("startDate").toString();
        String end = map.get("endDate").toString();
        if ("1".equals(period)) {
            LocalDate first = LocalDate.parse(start);
            long count = ChronoUnit.DAYS.between(first, LocalDate.parse(end));
            Stream.iterate(first, date -> date.plusDays(1)).limit(count + 1)
                    .forEach(date -> result.add(date.toString()));
        } else if ("2".equals(period)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            LocalDate first = DateUtil_old.transformStrToDate(start, DateUtil_old.Pattern.YYYY_MM)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate last = DateUtil_old.transformStrToDate(end, DateUtil_old.Pattern.YYYY_MM)
                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Stream.iterate(first, date -> date.plusMonths(1))
                    .limit(ChronoUnit.MONTHS.between(first, last) + 1)
                    .forEach(date -> result.add(date.format(formatter)));
        } else if ("3".equals(period)) {
            String[] first = start.split("Q");
            String[] last = end.split("Q");
            if (first[0].equals(last[0])) {
                if (first[1].equals(last[1])) {
                    result.add(first[0] + "年" + getQuarterMap().get(first[1]));
                } else {
                    int count = Math.abs(Integer.valueOf(last[1]) - Integer.valueOf(first[1]));
                    int year = Integer.valueOf(first[0]);
                    int quarter = Integer.valueOf(first[1]);
                    for (int i = 0; i <= count; i++) {
                        result.add(year + "年" + getQuarterMap().get(String.valueOf(quarter)));
                        if (quarter == 4) {
                            year++;
                            quarter = 1;
                        } else {
                            quarter++;
                        }
                    }
                }
            } else {
                int yearCount = Integer.valueOf(last[0]) - Integer.valueOf(first[0]);
                int year = Integer.valueOf(first[0]);
                int quarter = Integer.valueOf(first[1]);
                int endYear = Integer.valueOf(last[0]);
                int endQuarter = Integer.valueOf(last[1]);
                for (int i = 0; i <= yearCount; i++) {
                    for (String ignored : getQuarterMap().keySet()) {
                        result.add(year + "年" + getQuarterMap().get(String.valueOf(quarter)));
                        if (quarter == 4) {
                            year++;
                            quarter = 1;
                            break;
                        } else {
                            quarter++;
                            if (year == endYear && quarter == endQuarter) {
                                result.add(year + "年"
                                        + getQuarterMap().get(String.valueOf(quarter)));
                                return result;
                            }
                        }
                    }
                }
            }
        } else if ("4".equals(period)) {
            for (int year = Integer.parseInt(start); year <= Integer.parseInt(end); year++) {
                result.add(String.valueOf(year));
            }
        }
        return result;
    }

    private static Map<String, String> getQuarterMap() {
        Map<String, String> result = new HashMap<>();
        result.put("1", "第一季度"); result.put("2", "第二季度");
        result.put("3", "第三季度"); result.put("4", "第四季度");
        return result;
    }
}
