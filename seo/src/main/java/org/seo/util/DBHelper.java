package org.seo.util;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {

    /**
     * @author zc
     * @description 创建ORACLE连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Connection initOracle(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        }catch(Exception se) {
            System.out.println("ORACLE连接失败"+ se);
            return null;
        }
        return conn;
    }

    /**
     * @author zc
     * @description 创建DB2连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Map<String,Object> initDB2(String jdbcurl, String username, String password, boolean autoCommit) {
        Map<String, Object> result = new HashMap<>();
        Connection conn=null;
        try {
            Driver driver=(Driver) Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            //连接数据库
            DriverManager.registerDriver(driver);
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
            result.put("conn",conn);
        }catch(Exception se) {
            System.out.println("DB2连接失败"+ se);
            result.put("se",se.getMessage());
            return result;
        }
        return result;
    }

    /**
     * @author zc
     * @description 创建MYSQL连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Map<String,Object> initMysql(String jdbcurl, String username, String password, boolean autoCommit) {
        Map<String, Object> result = new HashMap<>();
        Connection conn=null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
            result.put("conn",conn);
        }catch(Exception se) {
            //连接失败
            System.out.println("MYSQL连接失败"+ se);
            result.put("se",se.getMessage());
            return result;
        }
        return result;
    }

    /**
     * @author zc
     * @description 创建SQLServer连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Connection initSQLServer(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            // 连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            // 连接失败
            System.out.println("SQLSERVER连接失败"+ se);
            return null;
        }
        return conn;
    }

    /**
     * @author zc
     * @description 创建HIVE连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Connection initHIVE(String jdbcurl, String username, String password, boolean autoCommit) {
        Connection conn=null;
        try {
            Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
            //conn = DriverManager.getConnection("jdbc:pivotal:greenplum://192.168.229.146:5432;DatabaseName=database", "gpadmin", "111");
            conn = DriverManager.getConnection(jdbcurl,username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            // 连接失败
            System.out.println("HIVE连接失败"+ se);
            return null;
        }
        return conn;
    }

    /**
     * @author zc
     * @description 创建ClickHouse连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @return
     */
    public static Map<String,Object> initClickHouse(String jdbcurl, String username, String password, boolean autoCommit) {
        Map<String, Object> result = new HashMap<>();
        Connection conn=null;
        try {
            Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
            // 连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
            result.put("conn",conn);
        } catch (Exception se) {
            // 连接失败
            System.out.println("ClickHouse连接失败"+ se);
            result.put("se",se.getMessage());
            return result;
        }
        return result;
    }

    /**
     * @author zc
     * @description 根据传入的数据源类型，创建Connection连接信息
     * @param jdbcurl
     * @param username
     * @param password
     * @param autoCommit
     * @param dbtype
     * @return
     */
    public static Connection intiConnection(String jdbcdriver,String jdbcurl, String username, String password, boolean autoCommit, String dbtype) {
        Connection conn = null;
        try {
            Class.forName(jdbcdriver);
            // 连接数据库
            conn = DriverManager.getConnection(jdbcurl, username, password);
            conn.setAutoCommit(autoCommit);
        } catch (Exception se) {
            // 连接失败
            System.out.println("HIVE连接失败"+ se);
            return null;
        }
        return conn;
    }

    /**
     * 关闭数据库
     * @author zc
     * @param conn
     * @param pstmt
     */
    public static void closeDB(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            rs = null;
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
            if (conn != null) {
                conn.close();
            }
            conn = null;
        } catch (Exception se) {
            System.out.println("数据源关闭失败"+ se);
        }
    }
}
