package com.luhanlin.transfer.utils;

import com.luhanlin.transfer.annotation.LuComponent;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接工具类
 *
 * @author <a href="mailto:allen_lu_hh@163.com">lin</a>
 * @since 1.0
 */
@LuComponent
public class ConnectionUtils {

    private static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<>();

    public Connection getCurrentThreadConn() throws SQLException {
        Connection connection = connThreadLocal.get();
        if (connection == null) {
            connection = DruidUtils.getInstance().getConnection();
            connThreadLocal.set(connection);
        }
        return connection;
    }

}
