package org.jeecg.modules.util;

import org.jeecg.modules.visualScreen.model.PageSub;
import org.jeecg.modules.visualScreen.model.PageWhere;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhereUtil {

    public static Map<String, Object> greateWhere(PageSub pageSub, List<PageWhere> pageWhereList) {
        HashMap<String, Object> greateWhereMap = new HashMap<String, Object>();
        HashMap<String, Object> dateInfo = new HashMap<String, Object>();
        String whereSql = "";
        String dateDesc = "";
        String startDate = "";
        String endDate = "";
        if (null != pageWhereList && pageWhereList.size() > 0) {
            for (PageWhere pw : pageWhereList) {
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("t")) {
                    if (pageSub.getTime_interval().equals("1")) {//至今
                        if (pageSub.getTime_type().equals("d")) { //日
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + pw.getWhere_key() + " >= '" + pw.getWhere_value() + "'";
                            }
                            whereSql += " and " + pw.getWhere_key() + " <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD) + "'";
                            dateDesc = pw.getWhere_value() + " ~ " + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
                            startDate = pw.getWhere_value();
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + pw.getWhere_value() + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM) + "'";
                            dateDesc = pw.getWhere_value() + " ~ " + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            startDate = pw.getWhere_value();
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getQStart(pw.getWhere_value()) + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM) + "'";
                            dateDesc = getQStart(pw.getWhere_value()) + " ~ " + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            startDate = getQStart(pw.getWhere_value());
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
//                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
//                                whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + pw.getWhere_value() + "'";
//                            }
//                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "'";
//                            dateDesc = pw.getWhere_value() + " ~ " + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
//                            startDate = pw.getWhere_value();
//                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                            // 由于本月数据不全，改为获取上一月份的当前时间，cuijsh
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + pw.getWhere_value() + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + DateUtil.getLastMonthCurrentDateStr(DateUtil.Pattern.YYYY_MM) + "'";
                            dateDesc = pw.getWhere_value() + " ~ " + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                            startDate = pw.getWhere_value();
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                        }
                    }
                    if (pageSub.getTime_interval().equals("2")) {//时间区间
                        String[] time = pw.getWhere_value().split(",");
                        if (pageSub.getTime_type().equals("d")) { //日
                            whereSql += " and " + pw.getWhere_key() + " >= '" + time[0] + "'";
                            whereSql += " and " + pw.getWhere_key() + " <= '" + time[1] + "'";
                            dateDesc = time[0] + " ~ " + time[1];
                            startDate = time[0];
                            endDate = time[1];
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + time[0] + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + time[1] + "'";
                            dateDesc = time[0] + " ~ " + time[1];
                            startDate = time[0];
                            endDate = time[1];
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getQStart(time[0]) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getQEnd(time[1]) + "'";
                            dateDesc = getQStart(time[0]) + " ~ " + getQEnd(time[1]);
                            startDate = getQStart(time[0]);
                            endDate = getQEnd(time[1]);
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + time[0] + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + time[1] + "'";
                            dateDesc = time[0] + " ~ " + time[1];
                            startDate = time[0];
                            endDate = time[1];
                        }
                    }
                    if (pageSub.getTime_interval().equals("3")) {//当前
                        if (pageSub.getTime_type().equals("d")) { //日
                            whereSql += " and " + pw.getWhere_key() + " >= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD) + "'";
                            whereSql += " and " + pw.getWhere_key() + " <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD) + "'";
                            dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
                            startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD);
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM) + "'";
                            dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            String q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            q = q.substring(q.lastIndexOf("-") + 1);
                            if (q.equals("01") || q.equals("02") || q.equals("03")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q1";
                                dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q1";
                                startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q1";
                                endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q1";
                            }
                            if (q.equals("04") || q.equals("05") || q.equals("06")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q2";
                                dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q2";
                                startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q2";
                                endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q2";
                            }
                            if (q.equals("07") || q.equals("08") || q.equals("09")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q3";
                                dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q3";
                                startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q3";
                                endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q3";
                            }
                            if (q.equals("10") || q.equals("11") || q.equals("12")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q4";
                                dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q4";
                                startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q4";
                                endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q4";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getQStart(q) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getQEnd(q) + "'";
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "'";
                            dateDesc = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                            startDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                            endDate = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY);
                        }
                    }
                    if (pageSub.getTime_interval().equals("4")) {//只传单个日期，该日期非当前日期，而是前台选择的日期
                        if (pageSub.getTime_type().equals("d")) { //日
                            whereSql += " and " + pw.getWhere_key() + " = '" + pw.getWhere_value() + "'";
                            dateDesc = pw.getWhere_value();
                            startDate = pw.getWhere_value();
                            endDate = pw.getWhere_value();
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) = '" + pw.getWhere_value() + "'";
                            dateDesc = pw.getWhere_value();
                            startDate = pw.getWhere_value();
                            endDate = pw.getWhere_value();
                        }
                        if (pageSub.getTime_type().equals("q")) { //季 ，可能有问题，需要后续调试，cuijiesheng cuijsh
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) = '" + pw.getWhere_value() + "'";
                            dateDesc = pw.getWhere_value();
                            startDate = pw.getWhere_value();
                            endDate = pw.getWhere_value();
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) = '" + pw.getWhere_value() + "'";
                            dateDesc = pw.getWhere_value();
                            startDate = pw.getWhere_value();
                            endDate = pw.getWhere_value();
                        }
                    }
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("g")) {//国库
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("b")) {//核算主体
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("a")) {//地区
                    whereSql += createGuoku(pw);
                }
            }
        }

        dateInfo.put("dateDesc", dateDesc);
        dateInfo.put("startDate", startDate);
        dateInfo.put("endDate", endDate);

        greateWhereMap.put("whereSql", whereSql);
        greateWhereMap.put("dateInfo", dateInfo);
        return greateWhereMap;
    }


    public static String greateTQWhere(PageSub pageSub, List<PageWhere> pageWhereList) {
        String whereSql = "";
        if (null != pageWhereList && pageWhereList.size() > 0) {
            for (PageWhere pw : pageWhereList) {
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("t")) {
                    if (pageSub.getTime_interval().equals("1")) {//至今
                        if (pageSub.getTime_type().equals("d")) { //日
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + pw.getWhere_key() + " >= '" + getLastYear(pw.getWhere_value(), DateUtil.Pattern.YYYY_MM_DD) + "'";
                            }
                            whereSql += " and " + pw.getWhere_key() + " <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD) + "'";
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(pw.getWhere_value(), DateUtil.Pattern.YYYY_MM) + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM), DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(getQStart(pw.getWhere_value()), DateUtil.Pattern.YYYY_MM) + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM), DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            if (null != pw.getWhere_value() && !"".equals(pw.getWhere_value())) {
                                whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + getLastYear(pw.getWhere_value(), DateUtil.Pattern.YYYY) + "'";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY), DateUtil.Pattern.YYYY) + "'";
                        }
                    }
                    if (pageSub.getTime_interval().equals("2")) {//时间区间
                        String[] time = pw.getWhere_value().split(",");
                        if (pageSub.getTime_type().equals("d")) { //日
                            whereSql += " and " + pw.getWhere_key() + " >= '" + getLastYear(time[0], DateUtil.Pattern.YYYY_MM_DD) + "'";
                            whereSql += " and " + pw.getWhere_key() + " <= '" + getLastYear(time[1], DateUtil.Pattern.YYYY_MM_DD) + "'";
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(time[0], DateUtil.Pattern.YYYY_MM) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(time[1], DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(getQStart(time[0]), DateUtil.Pattern.YYYY_MM) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(getQEnd(time[1]), DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + getLastYear(time[0], DateUtil.Pattern.YYYY) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + getLastYear(time[1], DateUtil.Pattern.YYYY) + "'";
                        }
                    }
                    if (pageSub.getTime_interval().equals("3")) {//当前
                        if (pageSub.getTime_type().equals("d")) { //日
                            whereSql += " and " + pw.getWhere_key() + " >= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD) + "'";
                            whereSql += " and " + pw.getWhere_key() + " <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD), DateUtil.Pattern.YYYY_MM_DD) + "'";
                        }
                        if (pageSub.getTime_type().equals("m")) { //月
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM), DateUtil.Pattern.YYYY_MM) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM), DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("q")) { //季
                            String q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
                            q = q.substring(q.lastIndexOf("-") + 1);
                            if (q.equals("01") || q.equals("02") || q.equals("03")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q1";
                            }
                            if (q.equals("04") || q.equals("05") || q.equals("06")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q2";
                            }
                            if (q.equals("07") || q.equals("08") || q.equals("09")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q3";
                            }
                            if (q.equals("10") || q.equals("11") || q.equals("12")) {
                                q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY) + "-Q4";
                            }
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) >= '" + getLastYear(getQStart(q), DateUtil.Pattern.YYYY_MM) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",7) <= '" + getLastYear(getQEnd(q), DateUtil.Pattern.YYYY_MM) + "'";
                        }
                        if (pageSub.getTime_type().equals("y")) { //年
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) >= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY), DateUtil.Pattern.YYYY) + "'";
                            whereSql += " and " + "left(" + pw.getWhere_key() + ",4) <= '" + getLastYear(DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY), DateUtil.Pattern.YYYY) + "'";
                        }
                    }
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("g")) {//国库
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("b")) {//核算主体
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("a")) {//地区
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("s")) {//科目
                    whereSql += createGuoku(pw);
                }
                if (null != pw.getWhere_type() && pw.getWhere_type().equals("ts")) {//t科目
                    whereSql += createGuoku(pw);
                }
            }
        }
        return whereSql;
    }

    private static String getQEnd(String time) {
        if (time.indexOf("-Q1") > -1) {
            return time.substring(0, 4) + "-03";
        } else if (time.indexOf("-Q2") > -1) {
            return time.substring(0, 4) + "-06";
        } else if (time.indexOf("-Q3") > -1) {
            return time.substring(0, 4) + "-09";
        } else if (time.indexOf("-Q4") > -1) {
            return time.substring(0, 4) + "-12";
        } else {
            return "";
        }
    }

    private static String getQStart(String time) {
        if (time.indexOf("-Q1") > -1) {
            return time.substring(0, 4) + "-01";
        } else if (time.indexOf("-Q2") > -1) {
            return time.substring(0, 4) + "-04";
        } else if (time.indexOf("-Q3") > -1) {
            return time.substring(0, 4) + "-07";
        } else if (time.indexOf("-Q4") > -1) {
            return time.substring(0, 4) + "-10";
        } else {
            return "";
        }
    }

    private static String getLastYear(String time, DateUtil.Pattern p) {
        return DateUtil.dateToStr(DateUtil.dateAdd(DateUtil.strToDate(time, p), 1, -1), p);
    }

    private static String createGuoku(PageWhere pw) {
        String id = pw.getWhere_value();
        String sql = "";
        if (null != id && !"".equals(id)) {
            if (id.indexOf(",") > -1) {
                String[] names = id.split(",");
                String temp = "(";
                for (String n : names) {
                    temp = temp + "'" + n + "',";
                }
                sql += " and " + pw.getWhere_key() + " in " + temp.substring(0, temp.lastIndexOf(",")) + ")";
            } else {
                sql += " and " + pw.getWhere_key() + " in " + "('" + id + "')";
            }
        }
        return sql;
    }
}
