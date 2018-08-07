package com.robberte.learning.tool.jdbc;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 简单数据源连接池
 * @author robberte
 * @date 2018/8/3 上午12:12
 */
public class SimpleDateSource implements DataSource {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SimpleDateSource.class);
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/jockey";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static List<Connection> pool = Collections.synchronizedList(new LinkedList<Connection>());
    //private static LinkedList<Connection> pool = (LinkedList<Connection>) Collections.synchronizedList(new LinkedList<Connection>());
    private static SimpleDateSource dateSource = new SimpleDateSource();

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("找不到JDBC驱动类:className={}", JDBC_DRIVER, e);
            e.printStackTrace();
        }
    }

    private SimpleDateSource() {
    }

    public static SimpleDateSource instances() {
        if(dateSource == null) {
            dateSource = new SimpleDateSource();
        }
        return dateSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (pool) {
            if(pool.size() > 0) {
                return pool.remove(0);
            } else {
                return createConnection();
            }
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(JDBC_URL, username, password);
    }

    public void freeConnection(Connection conn) {
        pool.add(conn);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
