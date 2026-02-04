package com.computerlist.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DBConnection {

    final static Logger logger = LogManager.getLogger(DBConnection.class);
    private static DataSource dataSource;

    public static void init() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5436/computermaster");
        config.setUsername("user");
        config.setPassword("12345");
        config.setConnectionTimeout(50000);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

        logger.debug("dataSource created successfully");
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (dataSource != null) {
            logger.debug("Returning connection");
            return dataSource.getConnection();
        } else {
            init();
            return dataSource.getConnection();
        }
    }

    public static void destroy() {
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
    }
}