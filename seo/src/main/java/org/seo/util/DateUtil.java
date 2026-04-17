package org.seo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public DateUtil() {
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String getCurrentDateStr(DateUtil.Pattern pattern) {
        return transformDateToStr(getCurrentDate(), pattern);
    }

    public static String transformDateToStr(Date date, DateUtil.Pattern pattern) {
        if (null != date && null != pattern) {
            SimpleDateFormat format = new SimpleDateFormat(pattern.toString());
            return format.format(date);
        } else {
            return null;
        }
    }

    public static Date transformStrToDate(String dateStr, DateUtil.Pattern pattern) {
        if (null != dateStr && null != pattern) {
            SimpleDateFormat format = new SimpleDateFormat(pattern.toString());
            Date date = null;

            try {
                date = format.parse(dateStr);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }

            return date;
        } else {
            return null;
        }
    }

    public static Date dateAdd(Date date, int dateType, int amount) {
        if (null == date) {
            return null;
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(dateType, amount);
            return c.getTime();
        }
    }

    public static String dateAddString(String date, int dateType, int amount, DateUtil.Pattern pattern) {
        if (null == date || date.equals("")) {
            return null;
        } else {
            Date d = DateUtil.transformStrToDate(date, pattern);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(dateType, amount);
            return DateUtil.transformDateToStr(c.getTime(), pattern);
        }
    }

    public static int compareTo(String dateStr1, String dateStr2, DateUtil.Pattern pattern) {
        if (null != dateStr1 && null != dateStr2 && null != pattern) {
            Date date1 = transformStrToDate(dateStr1, pattern);
            Date date2 = transformStrToDate(dateStr2, pattern);
            return null != date1 && null != date2 ? date1.compareTo(date2) : -2;
        } else {
            return -2;
        }
    }

    private static int getDateNumber(Date date, int dateType) {
        if (null == date) {
            return -1;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(dateType);
        }
    }

    public static int differentDaysByMillisecond(String date1, String date2) {
        Date end = transformStrToDate(date2,Pattern.YYYY_MM_DD);
        Date begin = transformStrToDate(date1,Pattern.YYYY_MM_DD);
        int days = (int) ((end.getTime() - begin.getTime()) / (1000 * 3600 * 24));
        return Math.abs(days);
    }

    public static int getMonthSpace(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        try {
            bef.setTime(sdf.parse(date1));
            aft.setTime(sdf.parse(date2));
            int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
            int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
            return Math.abs(month + result);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void main(String[] args) {
//        System.out.println("getCurrentTime : " + getTime());
//        System.out.println("getCurrentTime : " + getCurrentDate());
//        System.out.println("getCurrentDateStr : " + getCurrentDateStr(DateUtil.Pattern.YYYY_MM_DD_HH_CN));
//        System.out.println("transformDateToStr : " + transformDateToStr(getCurrentDate(), DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("transformStrToDate : " + transformStrToDate("2016-12-01 13:25:07", DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("dateAdd : " + dateAdd(getCurrentDate(), 5, 1));
//        System.out.println("compareTo : " + compareTo("2016-12-01 13:25:07", "2017-12-01 13:25:07", DateUtil.Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("getDateNumber : " + getDateNumber(getCurrentDate(), 5));
//        BigDecimal a=new BigDecimal(1323213.6666777778);
//        DecimalFormat df=new DecimalFormat(",###,##0.00");
//        df.format(a);
        System.out.println(getMonthSpace("2020-01", "2019-05"));

    }

    public static enum Pattern {
        MM_DD("MM-dd"),
        YYYY("yyyy"),
        YYYY_MM("yyyy-MM"),
        YYYY_MM_DD("yyyy-MM-dd"),
        MM_DD_HH_MM("MM-dd HH:mm"),
        MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        MM_DD_EN("MM/dd"),
        YYYY_MM_EN("yyyy/MM"),
        YYYY_MM_DD_EN("yyyy/MM/dd"),
        MM_DD_HH_MM_EN("MM/dd HH:mm"),
        MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
        YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
        YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),
        MM_DD_CN("MM月dd日"),
        YYYY_MM_CN("yyyy年MM月"),
        YYYY_MM_DD_CN("yyyy年MM月dd日"),
        MM_DD_HH_MM_CN("MM月dd日 HH:mm"),
        MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
        YYYY_MM_DD_HH_CN("yyyy年MM月dd日HH时"),
        YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
        YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),
        HH_MM("HH:mm"),
        HH_MM_SS("HH:mm:ss");

        private String value;

        private Pattern(String value) {
            this.value = value;
        }

        public String toString() {
            return this.value;
        }
    }
}
