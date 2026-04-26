package org.jeecg.modules.util;

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

    public static Date getLastMonthCurrentDate() {
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);//当前时间前去一个月，即一个月前的时间
        return calendar.getTime();
    }

    public static String getLastMonthCurrentDateStr(Pattern pattern) {
        return dateToStr(getLastMonthCurrentDate(), pattern);
    }

    public static String getCurrentDateStr(Pattern pattern) {
        return dateToStr(getCurrentDate(), pattern);
    }

    public static String dateToStr(Date date, Pattern pattern) {
        if (null != date && null != pattern) {
            SimpleDateFormat format = new SimpleDateFormat(pattern.toString());
            return format.format(date);
        } else {
            return null;
        }
    }

    public static Date strToDate(String dateStr, Pattern pattern) {
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

    public static int compareTo(String dateStr1, String dateStr2, Pattern pattern) {
        if (null != dateStr1 && null != dateStr2 && null != pattern) {
            Date date1 = strToDate(dateStr1, pattern);
            Date date2 = strToDate(dateStr2, pattern);
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

    /**
     * @return 21位随机数
     * caoyg
     */
    public static String generateKey() {
        int length = 21;
        String key = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yMMddHHmmssms");
        key = sdf.format(new Date()) + (Math.random() * 100);
        for (; key.length() < length + 1; ) {
            key = key + "0";
        }
        return key.replace(".", "").substring(0, length);
    }

    public static void main(String[] args) {
//        System.out.println("getCurrentTime : " + getTime());
//        System.out.println("getCurrentTime : " + getCurrentDate());
//        System.out.println("getCurrentDateStr : " + getCurrentDateStr(Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("transformDateToStr : " + transformDateToStr(getCurrentDate(), Pattern.YYYY));
//        System.out.println("transformStrToDate : " + transformStrToDate("2016-12-01 13:25:07", Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("dateAdd : " + dateAdd(getCurrentDate(), 5, 10));
//        System.out.println("compareTo : " + compareTo("2016-12-01 13:25:07", "2017-12-01 13:25:07", Pattern.YYYY_MM_DD_HH_MM_SS));
//        System.out.println("getDateNumber : " + getDateNumber(getCurrentDate(), 5));
        String q = DateUtil.getCurrentDateStr(DateUtil.Pattern.YYYY_MM);
        System.out.println(q.substring(q.lastIndexOf("-") + 1));
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
