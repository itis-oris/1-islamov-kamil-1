package com.computerlist.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.computerlist.util.DBConnection; // Импорт вашего класса DBConnection

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DBContextListener implements ServletContextListener {

    // Логгер для этого класса
    final static Logger logger = LogManager.getLogger(DBContextListener.class);

    // SQL-команды для создания структуры БД (Примеры, замените на ваши реальные таблицы)
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, email VARCHAR(100) UNIQUE NOT NULL, password_hash VARCHAR(64) NOT NULL, role_id INT NOT NULL)";
    private static final String CREATE_TOURS_TABLE = "CREATE TABLE IF NOT EXISTS computers (id SERIAL PRIMARY KEY, title VARCHAR(255) NOT NULL, description TEXT, gamingZone_id INT NOT NULL REFERENCES gaming_zones(id), start_date DATE NOT NULL, end_date DATE NOT NULL, hourly_rate DECIMAL(10,2) NOT NULL, creator_id INT NOT NULL REFERENCES users(id))";
    private static final String CREATE_GAMING_ZONES_TABLE = "CREATE TABLE IF NOT EXISTS gaming_zones (id SERIAL PRIMARY KEY, name VARCHAR(100) NOT NULL, location VARCHAR(255) NOT NULL)";


    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("contextInitialized: Starting database initialization...");
        try {
            // Инициализируем HikariCP DataSource через ваш класс
            DBConnection.init();

            // Инициализируем структуру БД (создаем таблицы, если их нет)
            initializeDatabaseSchema();

        } catch (ClassNotFoundException | SQLException e) {
            logger.atError().withThrowable(e).log("Failed to initialize database schema or connection pool");
            e.printStackTrace();
            // Критическая ошибка, останавливаем развертывание приложения
            throw new RuntimeException("Failed to initialize database connection or schema", e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("contextDestroyed: Closing database connection pool.");
        // Закрываем пул соединений
        DBConnection.destroy();
    }

    /**
     * Метод для выполнения DDL-скриптов при запуске приложения.
     * Замените SQL-команды выше на те, что соответствуют структуре вашей БД computermaster.
     */
    private void initializeDatabaseSchema() throws SQLException, ClassNotFoundException {
        // Получаем соединение из пула
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("Executing DDL statements to create database schema (if not exists)...");

            // Выполняем создание последовательности и таблиц
            stmt.execute(CREATE_GAMING_ZONES_TABLE);
            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_TOURS_TABLE);

            logger.info("Database schema initialized successfully.");
        }
    }

    private void dropAllTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS bookings CASCADE");
        stmt.execute("DROP TABLE IF EXISTS computer_room_options CASCADE");
        stmt.execute("DROP TABLE IF EXISTS computers CASCADE");
        stmt.execute("DROP TABLE IF EXISTS destinations CASCADE");
        stmt.execute("DROP TABLE IF EXISTS room_options CASCADE");
        stmt.execute("DROP TABLE IF EXISTS users CASCADE");
        stmt.execute("DROP TABLE IF EXISTS roles CASCADE");
    }
}
