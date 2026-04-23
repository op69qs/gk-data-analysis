package org.indicatorsLib.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.indicatorsLib.service.IndexRelationService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 组装指标方案的执行SQL
 */
@Component
public class CreateSchemeSQL implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 组装指标账期SQL
     *
     * @param condition
     * @return
     */
    public String getAccountPeriodSql(Map<String, Object> condition, Map<String, Object> eChartsCondition) {
        StringBuilder builder = new StringBuilder();
        IndexRelationService indexRelationService = applicationContext.getBean(IndexRelationService.class);
        List<Map<String, Object>> keys = indexRelationService.getIndicatorsTableName(condition.get("columns").toString().split(","));
        if (keys == null || keys.size() == 0) {
            return null;
        }
        builder.append("SELECT MIN(START_DATE) AS START_DATE,MAX(END_DATE) AS END_DATE FROM(");
        AtomicInteger count = new AtomicInteger(1);
        keys.forEach(map -> {
            builder.append(" SELECT MIN(ACCOUNT_PERIOD) AS START_DATE,MAX(ACCOUNT_PERIOD) AS END_DATE ");
            builder.append(" FROM indicators_lib.`" + map.get("tableName") + "` ");
            builder.append(" WHERE DIMENSION_FLAG='" + condition.get("dimensionFlag").toString() + "' AND PERIOD_FLAG='" + condition.get("periodFlag").toString() + "' ");
            if (oConvertUtils.isNotEmpty(condition.get("direction"))) { //国库/地区/核算主体 条件查询
                builder.append(" AND INDEX_DIM_CODE IN('" + eChartsCondition.get("direction").toString().replaceAll(",", "','") + "') ");
            }

            if (keys.size() > count.get()) {
                builder.append(" UNION ALL");
                count.getAndIncrement();
            }
        });
        builder.append(") V1");
        return builder.toString();
    }

    /**
     * 组装指标国库SQL
     *
     * @param condition
     * @return
     */
    public String getDimensionSQL(Map<String, Object> condition, Map<String, Object> eChartsCondition) {
        StringBuilder builder = new StringBuilder();
        IndexRelationService indexRelationService = applicationContext.getBean(IndexRelationService.class);
        List<Map<String, Object>> keys = indexRelationService.getIndicatorsTableName(condition.get("columns").toString().split(","));
        if (keys == null || keys.size() == 0) {
            return null;
        }
        builder.append("SELECT * FROM(");
        AtomicInteger count = new AtomicInteger(1);
        keys.forEach(map -> {
            builder.append(" SELECT INDEX_DIM_CODE AS dimCode,INDEX_DIM_DESCR AS dimDescr ");
            builder.append(" FROM indicators_lib.`" + map.get("tableName") + "` ");
            builder.append(" WHERE DIMENSION_FLAG='" + condition.get("dimensionFlag").toString() + "' AND PERIOD_FLAG='" + condition.get("periodFlag").toString() + "' ");
            if (oConvertUtils.isNotEmpty(condition.get("direction"))) { //国库/地区/核算主体 条件查询
                builder.append(" AND INDEX_DIM_CODE IN('" + eChartsCondition.get("direction").toString().replaceAll(",", "','") + "') ");
            }

            if (keys.size() > count.get()) {
                builder.append(" UNION ALL");
                count.getAndIncrement();
            }
        });
        builder.append(") V1 GROUP BY V1.dimCode ORDER BY V1.dimCode");
        return builder.toString();
    }

    /**
     * 组装指标方案SQL
     *
     * @param condition
     * @return
     */
/*    private String getSchemeSql(Map<String, Object> condition) {
        StringBuilder builder = new StringBuilder();
        Object startDate = condition.get("startDate"); //起始日期
        Object endDate = condition.get("endDate"); //结束日期
        IndexRelationService indexRelationService = applicationContext.getBean(IndexRelationService.class);
        List<Map<String, Object>> keys = indexRelationService.getIndicatorsTableName(condition.get("columns").toString().split(","));
        if (keys == null || keys.size() == 0) {
            return null;
        }
        builder.append("SELECT ");
        Arrays.asList(condition.get("columns").toString().split(",")).forEach(key -> {
            builder.append(" COALESCE(CAST(ROUND(SUM(CASE WHEN V1.COLID = '" + key + "' THEN VALUE END),2) AS TEXT),'') AS \"" + key + "\",");
        });
        builder.append("ACCOUNT_DATE," +
                " CASE WHEN INSTR(V1.ACCOUNT_PERIOD,'Q1') THEN REPLACE(V1.ACCOUNT_PERIOD,'Q1','年第一季度')"
                + " WHEN INSTR(V1.ACCOUNT_PERIOD,'Q2') THEN REPLACE(V1.ACCOUNT_PERIOD,'Q2','年第二季度')"
                + " WHEN INSTR(V1.ACCOUNT_PERIOD,'Q3') THEN REPLACE(V1.ACCOUNT_PERIOD,'Q3','年第三季度')"
                + " WHEN INSTR(V1.ACCOUNT_PERIOD,'Q4') THEN REPLACE(V1.ACCOUNT_PERIOD,'Q4','年第四季度')"
                + " ELSE V1.ACCOUNT_PERIOD END AS ACCOUNT_PERIOD," +
                "V1.CODE," +
                "V1.GK,"
                + " CASE WHEN V1.C_BDGLEVEL='1' THEN '中央级' WHEN V1.C_BDGLEVEL='2' THEN '省级'"
                + " WHEN V1.C_BDGLEVEL='3' THEN '市级' WHEN V1.C_BDGLEVEL='4' THEN '县（区）级'"
                + " WHEN V1.C_BDGLEVEL='5' THEN '乡（镇）级' WHEN V1.C_BDGLEVEL='6' THEN '地方级'"
                + " WHEN V1.C_BDGLEVEL='7' THEN '全国' ELSE '' END AS C_BDGLEVEL," +
                "C_BDGLEVEL AS LEVEL,"
                + "  CASE WHEN V1.JURISDICTION='0' THEN '全辖' WHEN V1.JURISDICTION='1' THEN '本级' END AS JURISDICTION_NAME," +
                "V1.JURISDICTION FROM (");

        AtomicInteger count = new AtomicInteger(1);
        keys.forEach(map -> {
            builder.append(" SELECT '" + map.get("colId") + "' AS COLID,ACCOUNT_PERIOD AS ACCOUNT_DATE,ACCOUNT_PERIOD,INDEX_DIM_CODE as CODE,INDEX_DIM_DESCR as GK,C_BDGLEVEL,JURISDICTION,");
            builder.append("CASE WHEN '" + map.get("type").toString() + "'='1' THEN ROUND((INDEX_VALUE*100),2) ELSE ROUND(INDEX_VALUE/" + Integer.parseInt(condition.get("price").toString()) + ",2)  END AS VALUE "); //根据指标类型(0数值/1比率)处理指标值
            builder.append(" FROM indicators_lib." + map.get("tableName") + " ");
            builder.append(" WHERE DIMENSION_FLAG='" + condition.get("dimensionFlag").toString() + "' AND PERIOD_FLAG='" + condition.get("periodFlag").toString() + "' ");
            if (oConvertUtils.isNotEmpty(condition.get("dimCode"))) { //国库/地区/核算主体 条件查询
                builder.append(" AND INDEX_DIM_CODE IN('" + condition.get("dimCode").toString().replaceAll(",", "','") + "') ");
            }
            if (oConvertUtils.isNotEmpty(condition.get("C_BDGLEVEL"))) { //级次 条件查询
                builder.append(" AND C_BDGLEVEL IN('" + condition.get("C_BDGLEVEL").toString().replaceAll(",", "','") + "') ");
            }
            if (oConvertUtils.isNotEmpty(condition.get("JURISDICTION"))) { //辖属 条件查询
                builder.append(" AND JURISDICTION IN('" + condition.get("JURISDICTION").toString().replaceAll(",", "','") + "') ");
            }
            if (oConvertUtils.isNotEmpty(startDate) && oConvertUtils.isNotEmpty(endDate)) {
                if (startDate.toString().contains("Q") || endDate.toString().contains("Q")) {
                    if (startDate.toString().equals(endDate.toString())) {
                        builder.append(" AND ACCOUNT_PERIOD ='" + startDate.toString() + "'");
                    } else {
                        builder.append(" AND CAST(REPLACE(ACCOUNT_PERIOD,'Q','0') AS NUMERIC) BETWEEN " + startDate.toString().replace("Q", "0") + " AND " + endDate.toString().replace("Q", "0"));
                    }
                } else {
                    if (startDate.toString().equals(endDate.toString())) {
                        builder.append(" AND ACCOUNT_PERIOD ='" + startDate.toString() + "'");
                    } else {
                        builder.append(" AND TO_DATE(ACCOUNT_PERIOD, 'YYYY-MM-DD') BETWEEN TO_DATE('" + startDate.toString() + "', 'YYYY-MM-DD') AND TO_DATE('" + endDate.toString() + "', 'YYYY-MM-DD')");
                    }
                }
            }
            if (keys.size() > count.get()) {
                builder.append(" UNION ALL");
                count.getAndIncrement();
            }
        });
        builder.append(") V1 GROUP BY V1.ACCOUNT_PERIOD,V1.GK,V1.C_BDGLEVEL,V1.JURISDICTION ORDER BY V1.ACCOUNT_PERIOD,V1.GK,V1.C_BDGLEVEL,V1.JURISDICTION");
        return builder.toString();
    }*/


    /**
     * 组装指标方案SQL_2
     *
     * @param condition
     * @return
     */
    private Map getSchemeSql_2(
            Map<String, Object> condition,
            PageData pageData,
            Map<String, Object> mainCondition) {
        Map mapRes =new HashMap();
        StringBuilder builder = new StringBuilder();
        StringBuilder builderOderBy = new StringBuilder();
        Object startDate = condition.get("startDate"); //起始日期
        Object endDate = condition.get("endDate"); //结束日期
        IndexRelationService indexRelationService = applicationContext.getBean(IndexRelationService.class);
        List<Map<String, Object>> keys = indexRelationService.getIndicatorsTableName(condition.get("columns").toString().split(","));
        if (keys == null || keys.size() == 0) {
            return null;
        }
        // 主查询
        builder.append("SELECT ");
        Arrays.asList(condition.get("columns").toString().split(",")).forEach(key -> {
            builder.append(" COALESCE(CAST(ROUND(SUM(CASE WHEN aa.COLID = '" + key + "' THEN VALUE END),2) AS TEXT),'') AS \"" + key + "\",");
        });
        builder.append(" aa.COLID,\n" +
                "aa.ACCOUNT_DATE,\n" +
                "aa.ACCOUNT_PERIOD,\n" +
                "aa.CODE,\n" +
                "aa.GK\n" +
                "FROM " +
                "(  ");
        AtomicInteger count = new AtomicInteger(1);
        keys.forEach(map -> {
            // 拼接子查询
            builder.append(" SELECT\n" +
                    "'" + map.get("colId") + "' AS COLID,\n" +
                    "       ACCOUNT_PERIOD AS ACCOUNT_DATE,\n" +
                    "       REPLACE(REPLACE(REPLACE(REPLACE(\n" +
                    "       ACCOUNT_PERIOD,\n" +
                    "        'Q1',\n" +
                    "        '年第一季度'\n" +
                    "      ),\n" +
                    "        'Q2',\n" +
                    "        '年第二季度'\n" +
                    "      ),\n" +
                    "        'Q3',\n" +
                    "        '年第三季度'\n" +
                    "      ),\n" +
                    "        'Q4',\n" +
                    "        '年第四季度'\n" +
                    "      ) AS ACCOUNT_PERIOD,\n" +
                    "      INDEX_DIM_CODE AS CODE,\n" +
                    "      INDEX_DIM_DESCR AS GK,\n");
            if ("1".equals(map.get("type").toString())) {
                builder.append("ROUND((INDEX_VALUE*100),2) AS VALUE "); //根据指标类型(0数值/1比率)处理指标值
            }
            if ("0".equals(map.get("type").toString())) {
                builder.append("ROUND(INDEX_VALUE/" + Integer.parseInt(condition.get("price").toString()) + ",2) AS VALUE "); //根据指标类型(0数值/1比率)处理指标值
            }
            builder.append(" FROM indicators_lib." + map.get("tableName") + " ");
            builder.append(" WHERE DIMENSION_FLAG='" + condition.get("dimensionFlag").toString() + "' AND PERIOD_FLAG='" + condition.get("periodFlag").toString() + "' ");
            if (oConvertUtils.isNotEmpty(condition.get("dimCode"))) { //国库/地区/核算主体 条件查询
                builder.append(" AND INDEX_DIM_CODE IN('" + condition.get("dimCode").toString().replaceAll(",", "','") + "') ");
            }

            builder.append("AND INDEX_ID = '" + map.get("colId") + "'");
            if (oConvertUtils.isNotEmpty(startDate) && oConvertUtils.isNotEmpty(endDate)) {
                if (startDate.toString().contains("Q") || endDate.toString().contains("Q")) {
                    if (startDate.toString().equals(endDate.toString())) {
                        builder.append(" AND ACCOUNT_PERIOD ='" + startDate.toString() + "'");
                    } else {
                        builder.append(" AND CAST(REPLACE(ACCOUNT_PERIOD,'Q','0') AS NUMERIC) BETWEEN " + startDate.toString().replace("Q", "0") + " AND " + endDate.toString().replace("Q", "0"));
                    }
                } else {
                    if (startDate.toString().equals(endDate.toString())) {
                        builder.append(" AND ACCOUNT_PERIOD ='" + startDate.toString() + "'");
                    } else {
                        builder.append(" AND TO_DATE(ACCOUNT_PERIOD, 'YYYY-MM-DD') BETWEEN TO_DATE('" + startDate.toString() + "', 'YYYY-MM-DD') AND TO_DATE('" + endDate.toString() + "', 'YYYY-MM-DD')");
                    }
                }
            }
            if (keys.size() > count.get()) {
                builder.append(" UNION ALL");
                count.getAndIncrement();
            }
        });
        builder.append(") aa");

        // 主查询WHERE条件
        if (!oConvertUtils.isEmpty(pageData.get("screenConditon"))) { //不对方案结果集进行过滤查询
            Map<String, Object> sucreenCondition = (Map<String, Object>) JSON.parse(pageData.get("screenConditon").toString()); //方案查询结果集筛选条件
            if (oConvertUtils.isNotEmpty(sucreenCondition.get("where"))) { //添加结果集过滤条件
                JSONArray whereArray = (JSONArray) sucreenCondition.get("where");
                builder.append(" WHERE TRUE");
                whereArray.forEach(arry -> {
                    Map<String, Object> whereMap = (Map<String, Object>) arry;
                    String filterlogic = whereMap.get("filterlogic").toString(); //过滤运算符
                    if (StringUtils.isNotBlank(filterlogic)) {
                        String filterName = whereMap.get("filtername").toString(); //过滤字段

                        String filternumber = whereMap.get("filternumber").toString(); //过滤字段值
                        if ("GK,ACCOUNT_PERIOD".contains(filterName)) {
                            if ("ACCOUNT_PERIOD".equals(filterName)) { //账期的季指标过滤条件需要特殊处理
                                if (!"3".equals(mainCondition.get("periodFlag"))) {
                                    filterName = "CAST(REPLACE(aa." + filterName + ",'-','') AS NUMERIC)";
                                    filternumber = whereMap.get("filternumber").toString().replace("-", "");
                                }
                            }
                        } else {
                            //千分位分隔符、监测值标注
                            filterName = "CASE WHEN aa.COLID = '" + filterName + "' THEN aa.value ";
                        }

                        if ("like".equals(filterlogic)) { //包含
                            builder.append(" AND " + filterName + " LIKE '%" + filternumber + "%'");
                        } else if ("nLike".equals(filterlogic)) {//不包含
                            builder.append(" AND " + filterName + " NOT LIKE '%" + filternumber + "%'");
                        } else if ("lLike".equals(filterlogic)) {//开头是
                            builder.append(" AND " + filterName + " LIKE '" + filternumber + "%'");
                        } else if ("rLike".equals(filterlogic)) {//结尾是
                            builder.append(" AND " + filterName + " LIKE '%" + filternumber + "'");
                        } else {
                            if ("GK".contains(filterName)) { //包含中文
                                builder.append(" AND " + filterName + filterlogic + "'" + filternumber + "'");
                            } else { //数字
                                if ((filternumber.contains(",") || filternumber.contains("，")) && filterName.contains("aa.COLID")) { //含有千分位的数字
                                    builder.append(" AND " + filterName + filterlogic + "CAST(REPLACE('" + filternumber + "',',','') AS DECIMAL)" + " ELSE FALSE END");
                                } else {
                                    if (filterName.contains("aa.COLID")) {
                                        builder.append(" AND " + filterName + filterlogic + filternumber + " ELSE FALSE END");
                                    } else {
                                        builder.append(" AND " + filterName + filterlogic + filternumber);
                                    }
                                }
                            }
                        }
                    }
                });
            }
            // 分组
            builder.append("\n" +
                    "GROUP BY \n" +
                    "aa.ACCOUNT_PERIOD,\n" +
                    "aa.GK\n");
            if (oConvertUtils.isNotEmpty(sucreenCondition.get("order"))) { //添加结果集排序条件
                builderOderBy.append(" ORDER BY ");
                JSONArray orderArray = (JSONArray) sucreenCondition.get("order");
                orderArray.forEach(order -> {
                    Map<String, Object> orderMap = (Map<String, Object>) order;
                    if ("ACCOUNT_PERIOD".equals(orderMap.get("sortname").toString())) {  //账期排序
                        builderOderBy.append("V.ACCOUNT_PERIOD "  +"  " + orderMap.get("sortrule") + ",");
                    } else if ("CODE".equals(orderMap.get("sortname").toString())) {  //国库拍
                        builderOderBy.append("V.CODE "  +"  " + orderMap.get("sortrule") + ",");
                    }  else {
                        builderOderBy.append("\n" +
                                "CAST(REPLACE( V." + orderMap.get("sortname") +" ,',','') AS DECIMAL(28,2))  " + orderMap.get("sortrule") + ",");
                        //builder.append("CAST(" + orderMap.get("sortname") + " AS DECIMAL(28,2)) " + orderMap.get("sortrule") + ",");
                    }
                });
            } else {
                builderOderBy.append("\n" +
                        "ORDER BY " +
                        "V.ACCOUNT_PERIOD,\n" +
                        "V.GK");
            }
        } else {
            builder.append("\n" +
                    "GROUP BY \n" +
                    "aa.ACCOUNT_PERIOD,\n" +
                    "aa.GK");
            builderOderBy.append("\n" +
                    "ORDER BY " +
                    "V.ACCOUNT_PERIOD,\n" +
                    "V.GK");
        }
        mapRes.put("builder",builder.toString().endsWith(",") ? builder.substring(0, builder.lastIndexOf(",")) : builder.toString());
        mapRes.put("builderOderBy",builderOderBy.toString().endsWith(",") ? builderOderBy.substring(0, builderOderBy.lastIndexOf(",")) : builderOderBy.toString());
        return mapRes;
    }


    /**
     * 为指标方案的sql添加筛选条件
     *
     * @param pageData
     * @return
     */
    public String getSchemeSQL(PageData pageData) {
        StringBuilder builder = new StringBuilder();
        Map<String, Object> mainCondition = (Map<String, Object>) JSON.parse(pageData.get("mainCondition").toString()); //方案的查询主SQL条件
        mainCondition.put("columns", pageData.getString("columns"));
        // String mainSQL = this.getSchemeSql(mainCondition); //方案的查询主SQL

        Map  mainSQL = this.getSchemeSql_2(mainCondition, pageData, mainCondition);
        builder.append("SELECT V.* FROM (" + mainSQL.get("builder") + ") V"  + mainSQL.get("builderOderBy"));
        return builder.toString();
        /*if (oConvertUtils.isEmpty(pageData.get("screenConditon"))) { //不对方案结果集进行过滤查询
            return mainSQL;
        } else {
            Map<String, Object> sucreenCondition = (Map<String, Object>) JSON.parse(pageData.get("screenConditon").toString()); //方案查询结果集筛选条件
            builder.append("SELECT V.* FROM (" + mainSQL + ") V");
            if (oConvertUtils.isNotEmpty(sucreenCondition.get("where"))) { //添加结果集过滤条件
                JSONArray whereArray = (JSONArray) sucreenCondition.get("where");
                builder.append(" WHERE 1=1");
                whereArray.forEach(arry -> {
                    Map<String, Object> whereMap = (Map<String, Object>) arry;
                    String filterlogic = whereMap.get("filterlogic").toString(); //过滤运算符
                    if (StringUtils.isNotBlank(filterlogic)) {
                        String filterName = whereMap.get("filtername").toString(); //过滤字段
                        if ("JURISDICTION".equals(filterName)) {
                            filterName = "JURISDICTION_NAME";
                        }
                        String filternumber = whereMap.get("filternumber").toString(); //过滤字段值
                        if ("GK,ACCOUNT_PERIOD,C_BDGLEVEL,JURISDICTION_NAME".contains(filterName)) {
                            if ("ACCOUNT_PERIOD".equals(filterName)) { //账期的季指标过滤条件需要特殊处理
                                if (!"3".equals(mainCondition.get("periodFlag"))) {
                                    filterName = "CAST(REPLACE(" + filterName + ",'-','') AS NUMERIC)";
                                    filternumber = whereMap.get("filternumber").toString().replace("-", "");
                                }
                            }
                        } else {
                            //千分位分隔符、监测值标注
                            filterName = "CAST(REPLACE(REPLACE(" + filterName + ",',',''),'RGB_','') AS DECIMAL)";
                        }

                        if ("like".equals(filterlogic)) { //包含
                            builder.append(" AND " + filterName + " LIKE '%" + filternumber + "%'");
                        } else if ("nLike".equals(filterlogic)) {//不包含
                            builder.append(" AND " + filterName + " NOT LIKE '%" + filternumber + "%'");
                        } else if ("lLike".equals(filterlogic)) {//开头是
                            builder.append(" AND " + filterName + " LIKE '" + filternumber + "%'");
                        } else if ("rLike".equals(filterlogic)) {//结尾是
                            builder.append(" AND " + filterName + " LIKE '%" + filternumber + "'");
                        } else {
                            if ("GK,C_BDGLEVEL,JURISDICTION_NAME".contains(filterName)) { //包含中文
                                builder.append(" AND " + filterName + filterlogic + "'" + filternumber + "'");
                            } else { //数字
                                if (filternumber.contains(",") || filternumber.contains("，")) { //含有千分位的数字
                                    builder.append(" AND " + filterName + filterlogic + "CAST(REPLACE('" + filternumber + "',',','') AS DECIMAL)");
                                } else {
                                    builder.append(" AND " + filterName + filterlogic + filternumber);
                                }
                            }
                        }
                    }
                });
            }
            if (oConvertUtils.isNotEmpty(sucreenCondition.get("order"))) { //添加结果集排序条件
                builder.append(" ORDER BY ");
                JSONArray orderArray = (JSONArray) sucreenCondition.get("order");
                orderArray.forEach(order -> {
                    Map<String, Object> orderMap = (Map<String, Object>) order;
                    if ("ACCOUNT_PERIOD".equals(orderMap.get("sortname").toString())) {  //账期排序
                        builder.append("V.ACCOUNT_PERIOD " + orderMap.get("sortrule") + ",");
                    } else if ("CODE".equals(orderMap.get("sortname").toString())) {  //国库拍
                        builder.append("V.CODE " + orderMap.get("sortrule") + ",");
                    } else if ("C_BDGLEVEL".equals(orderMap.get("sortname").toString())) {  //级次排序
                        builder.append("V.LEVEL " + orderMap.get("sortrule") + ",");
                    } else if ("V.JURISDICTION".equals(orderMap.get("sortname").toString())) {  //级次排序
                        builder.append("V.JURISDICTION " + orderMap.get("sortrule") + ",");
                    } else {
                        builder.append("CASE WHEN V." + orderMap.get("sortname") + "='' THEN NULL ELSE CAST(REPLACE(REPLACE(V." + orderMap.get("sortname") + ",',',''),'RGB_','') AS DECIMAL(28,2)) END " + orderMap.get("sortrule") + ",");
                    }
                });
            }
        }
        return builder.toString().endsWith(",") ? builder.substring(0, builder.lastIndexOf(",")) : builder.toString();
        */
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
