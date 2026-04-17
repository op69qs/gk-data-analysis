package org.indicatorsLib.util;

import java.sql.*;

/**
 * JDBC原生连接数据库代码
 */
public class ContectionForJDBC {

    private static Connection conn = null;

    static {
        try {
            Class.forName(ConnectionProperties.driver);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e + "数据库连接失败");
        }
    }

    //获取数据库连接对象
    public static Connection getConnection() {
        try {
            conn = DriverManager.getConnection(ConnectionProperties.url, ConnectionProperties.username, ConnectionProperties.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据库的方法
    public static void close(ResultSet rs, Statement sta, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (sta != null) {
            try {
                sta.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
