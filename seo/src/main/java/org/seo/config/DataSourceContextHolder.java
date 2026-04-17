package org.seo.config;

public class DataSourceContextHolder {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal();

    public static synchronized void setDBType(String data) {
        threadLocal.set(data);
    }

    public static String getDBType() {
        return threadLocal.get();
    }
}
