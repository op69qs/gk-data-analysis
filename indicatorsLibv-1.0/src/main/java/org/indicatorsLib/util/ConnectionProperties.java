package org.indicatorsLib.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 动态获取数据库连接配置，以便于代码中读取
 */
@Component
public class ConnectionProperties {

    public static String url;
    public static String driver;
    public static String username;
    public static String password;

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    public void setUrl(String url) {
        ConnectionProperties.url = url;
    }

    @Value("${spring.datasource.dynamic.datasource.master.driver-class-name}")
    public void setDriver(String driver) {
        ConnectionProperties.driver = driver;
    }

    @Value("${spring.datasource.dynamic.datasource.master.username}")
    public void setUsername(String username) {
        ConnectionProperties.username = username;
    }

    @Value("${spring.datasource.dynamic.datasource.master.password}")
    public void setPassword(String password) {
        ConnectionProperties.password = password;
    }

}
