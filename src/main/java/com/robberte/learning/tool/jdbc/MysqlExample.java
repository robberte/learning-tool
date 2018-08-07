package com.robberte.learning.tool.jdbc;

import java.sql.*;

/**
 * @author robberte
 * @date 2018/8/1 上午12:31
 */
public class MysqlExample {

    public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static String URL = "jdbc:mysql://127.0.0.1:3306/jockey";
    public static String USERNAME = "root";
    public static String PASSWORD = "";
    public static String sql = "select * from user";

    public static void main(String[] args) {
        MysqlExample mysqlExample = new MysqlExample();
        mysqlExample.executeExample();
        mysqlExample.executeExampleByDatasouce();
    }

    public void executeExample() {
        Connection conn = null;
        try {
            Driver.class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()) {
                System.out.println(
                        String.format("name=%s, id=%s",
                                rs.getString("name"),
                                rs.getLong("id")));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void executeExampleByDatasouce() {
        SimpleDateSource dateSource = SimpleDateSource.instances();
        Connection conn = null;
        try {
            conn = dateSource.getConnection();
            Statement ps = conn.createStatement();
            ResultSet rs = ps.executeQuery(sql);
            while(rs.next()) {
                System.out.println(
                        String.format("name=%s, id=%s",
                                rs.getString("name"),
                                rs.getLong("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                dateSource.freeConnection(conn);
            }
        }
    }
}
