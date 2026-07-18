package org.jeecg.modules.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil_old {
    public static long getTime() { return System.currentTimeMillis(); }
    public static Date getCurrentDate() { return new Date(); }
    public static String getCurrentDateStr(Pattern pattern) {
        return transformDateToStr(getCurrentDate(), pattern);
    }
    public static String transformDateToStr(Date date, Pattern pattern) {
        return date == null || pattern == null ? null
                : new SimpleDateFormat(pattern.toString()).format(date);
    }
    public static Date transformStrToDate(String dateStr, Pattern pattern) {
        if (dateStr == null || pattern == null) { return null; }
        try {
            return new SimpleDateFormat(pattern.toString()).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Date dateAdd(Date date, int dateType, int amount) {
        if (date == null) { return null; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dateType, amount);
        return calendar.getTime();
    }
    public static int compareTo(String first, String second, Pattern pattern) {
        if (first == null || second == null || pattern == null) { return -2; }
        Date firstDate = transformStrToDate(first, pattern);
        Date secondDate = transformStrToDate(second, pattern);
        return firstDate == null || secondDate == null ? -2 : firstDate.compareTo(secondDate);
    }
    private static int getDateNumber(Date date, int dateType) {
        if (date == null) { return -1; }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(dateType);
    }
    public static void main(String[] args) {
        System.out.println("getCurrentTime : " + getTime());
    }

    public enum Pattern {
        YYYY("yyyy"), MM_DD("MM-dd"), YYYY_MM("yyyy-MM"), YYYY_MM_DD("yyyy-MM-dd"),
        MM_DD_HH_MM("MM-dd HH:mm"), MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"), YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        MM_DD_EN("MM/dd"), YYYY_MM_EN("yyyy/MM"), YYYY_MM_DD_EN("yyyy/MM/dd"),
        MM_DD_HH_MM_EN("MM/dd HH:mm"), MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"), MM_DD_CN("MM月dd日"),
        YYYY_MM_CN("yyyy年MM月"), YYYY_MM_DD_CN("yyyy年MM月dd日"),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm"), MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
        YYYY_MM_DD_HH_CN("yyyy年MM月dd日HH时"), YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"), HH_MM("HH:mm"), HH_MM_SS("HH:mm:ss");
        private String value;
        Pattern(String value) { this.value = value; }
        @Override public String toString() { return value; }
    }
}
