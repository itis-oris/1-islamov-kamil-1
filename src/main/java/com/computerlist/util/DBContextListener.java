package com.computerlist.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DBContextListener implements ServletContextListener {

    final static Logger logger = LogManager.getLogger(DBContextListener.class);

    // SQL команды для создания структуры БД в соответствии с DAO
    private static final String CREATE_ROLES_TABLE =
            "CREATE TABLE IF NOT EXISTS roles ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(50) NOT NULL UNIQUE"
                    + ");";

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE IF NOT EXISTS users ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(50) NOT NULL,"
                    + "email VARCHAR(100) NOT NULL UNIQUE,"
                    + "password_hash VARCHAR(64) NOT NULL,"
                    + "role_id INT NOT NULL REFERENCES roles(id)"
                    + ");";

    private static final String CREATE_GAMINGZONES_TABLE =
            "CREATE TABLE IF NOT EXISTS gamingZones ("
                    + "id SERIAL PRIMARY KEY,"
                    + "name VARCHAR(100) NOT NULL,"
                    + "country VARCHAR(255) NOT NULL"
                    + ");";

    private static final String CREATE_PC_CONFIGURATIONS_TABLE =
            "CREATE TABLE IF NOT EXISTS pc_configurations ("
                    + "id SERIAL PRIMARY KEY,"
                    + "room_type VARCHAR(50) NOT NULL,"
                    + "capacity INT NOT NULL,"
                    + "bed_type VARCHAR(50) NOT NULL,"
                    + "bed_count INT NOT NULL,"
                    + "price_multiplier NUMERIC(5,2) NOT NULL DEFAULT 1.0"
                    + ");";

    private static final String CREATE_COMPUTERS_TABLE =
            "CREATE TABLE IF NOT EXISTS computers ("
                    + "id SERIAL PRIMARY KEY,"
                    + "title VARCHAR(255) NOT NULL,"
                    + "description TEXT,"
                    + "gamingZone_id INT NOT NULL REFERENCES gamingZones(id),"
                    + "start_date DATE NOT NULL,"
                    + "end_date DATE NOT NULL,"
                    + "hourly_rate NUMERIC(10,2) NOT NULL,"
                    + "creator_id INT NOT NULL REFERENCES users(id)"
                    + ");";

    private static final String CREATE_COMPUTER_PC_CONFIGURATIONS_TABLE =
            "CREATE TABLE IF NOT EXISTS computer_pc_configurations ("
                    + "computer_id INT NOT NULL REFERENCES computers(id) ON DELETE CASCADE,"
                    + "pc_configuration_id INT NOT NULL REFERENCES pc_configurations(id) ON DELETE CASCADE,"
                    + "PRIMARY KEY (computer_id, pc_configuration_id)"
                    + ");";

    private static final String CREATE_RESERVATIONS_TABLE =
            "CREATE TABLE IF NOT EXISTS reservations ("
                    + "id SERIAL PRIMARY KEY,"
                    + "user_id INT NOT NULL REFERENCES users(id),"
                    + "computer_id INT NOT NULL REFERENCES computers(id),"
                    + "reservation_date TIMESTAMP NOT NULL,"
                    + "people_count INT NOT NULL,"
                    + "rooms_count INT NOT NULL,"
                    + "total_price NUMERIC(10,2) NOT NULL,"
                    + "status VARCHAR(20) NOT NULL DEFAULT 'PENDING'"
                    + ");";

    // Заполняем начальными данными (роли)
    private static final String INSERT_DEFAULT_ROLES =
            "INSERT INTO roles (name) "
                    + "SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');"
                    + "INSERT INTO roles (name) "
                    + "SELECT 'MANAGER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'MANAGER');"
                    + "INSERT INTO roles (name) "
                    + "SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'USER');";

    // Заполняем начальными данными (тестовый пользователь-администратор)
    private static final String INSERT_DEFAULT_ADMIN =
            "INSERT INTO users (name, email, password_hash, role_id) "
                    + "SELECT 'Admin', 'admin@admin.com', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@admin.com');";

    // Заполняем начальными данными (тестовые игровые зоны)
    private static final String INSERT_DEFAULT_GAMINGZONES =
            "INSERT INTO gamingZones (name, country) "
                    + "SELECT 'Киберспорт Арена', 'г. Москва, ул. Центральная 1' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM gamingZones WHERE name = 'Киберспорт Арена');"
                    + "INSERT INTO gamingZones (name, country) "
                    + "SELECT 'GameHub', 'г. Санкт-Петербург, пр. Невский 10' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM gamingZones WHERE name = 'GameHub');"
                    + "INSERT INTO gamingZones (name, country) "
                    + "SELECT 'Pixel Arena', 'г. Екатеринбург, ул. Гагарина 45' "
                    + "WHERE NOT EXISTS (SELECT 1 FROM gamingZones WHERE name = 'Pixel Arena');";

    // Заполняем начальными данными (тестовые конфигурации ПК)
    private static final String INSERT_DEFAULT_PC_CONFIGURATIONS =
            "INSERT INTO pc_configurations (room_type, capacity, bed_type, bed_count, price_multiplier) "
                    + "SELECT 'Новичок', 1, 'Intel Core i3', 4, 1.0 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM pc_configurations WHERE room_type = 'Новичок');"
                    + "INSERT INTO pc_configurations (room_type, capacity, bed_type, bed_count, price_multiplier) "
                    + "SELECT 'Геймер', 1, 'Intel Core i5', 6, 1.5 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM pc_configurations WHERE room_type = 'Геймер');"
                    + "INSERT INTO pc_configurations (room_type, capacity, bed_type, bed_count, price_multiplier) "
                    + "SELECT 'Киберспортсмен', 1, 'Intel Core i7', 8, 2.0 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM pc_configurations WHERE room_type = 'Киберспортсмен');"
                    + "INSERT INTO pc_configurations (room_type, capacity, bed_type, bed_count, price_multiplier) "
                    + "SELECT 'Премиум', 1, 'Intel Core i9', 8, 2.5 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM pc_configurations WHERE room_type = 'Премиум');";

    // Заполняем начальными данными (тестовые игровые ПК)
    private static final String INSERT_DEFAULT_COMPUTERS =
            "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) "
                    + "SELECT 'Стандартный ПК', 'Intel Core i3 / NVIDIA GTX 1650 / 16GB RAM / SSD 512GB', 1, '2024-01-01', '2025-12-31', 300.00, 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM computers WHERE title = 'Стандартный ПК');"
                    + "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) "
                    + "SELECT 'Продвинутый ПК', 'Intel Core i5 / NVIDIA RTX 3060 / 32GB RAM / SSD 1TB', 1, '2024-01-01', '2025-12-31', 450.00, 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM computers WHERE title = 'Продвинутый ПК');"
                    + "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) "
                    + "SELECT 'Киберспорт ПК', 'Intel Core i7 / NVIDIA RTX 3080 / 32GB RAM / SSD 1TB', 2, '2024-01-01', '2025-12-31', 600.00, 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM computers WHERE title = 'Киберспорт ПК');"
                    + "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) "
                    + "SELECT 'VR-станция', 'Intel Core i9 / NVIDIA RTX 3090 / 64GB RAM / SSD 2TB', 3, '2024-01-01', '2025-12-31', 800.00, 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM computers WHERE title = 'VR-станция');"
                    + "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) "
                    + "SELECT 'Стримерский ПК', 'AMD Ryzen 9 / NVIDIA RTX 3080 Ti / 64GB RAM / SSD 4TB', 2, '2024-01-01', '2025-12-31', 700.00, 1 "
                    + "WHERE NOT EXISTS (SELECT 1 FROM computers WHERE title = 'Стримерский ПК');";

    // Заполняем начальными данными (связи между ПК и конфигурациями)
    private static final String INSERT_DEFAULT_COMPUTER_PC_CONFIGURATIONS =
            "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) "
                    + "SELECT c.id, r.id FROM computers c, pc_configurations r "
                    + "WHERE c.title = 'Стандартный ПК' AND r.room_type = 'Новичок' "
                    + "AND NOT EXISTS (SELECT 1 FROM computer_pc_configurations WHERE computer_id = c.id AND pc_configuration_id = r.id);"
                    + "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) "
                    + "SELECT c.id, r.id FROM computers c, pc_configurations r "
                    + "WHERE c.title = 'Продвинутый ПК' AND r.room_type = 'Геймер' "
                    + "AND NOT EXISTS (SELECT 1 FROM computer_pc_configurations WHERE computer_id = c.id AND pc_configuration_id = r.id);"
                    + "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) "
                    + "SELECT c.id, r.id FROM computers c, pc_configurations r "
                    + "WHERE c.title = 'Киберспорт ПК' AND r.room_type = 'Киберспортсмен' "
                    + "AND NOT EXISTS (SELECT 1 FROM computer_pc_configurations WHERE computer_id = c.id AND pc_configuration_id = r.id);"
                    + "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) "
                    + "SELECT c.id, r.id FROM computers c, pc_configurations r "
                    + "WHERE c.title = 'VR-станция' AND r.room_type = 'Премиум' "
                    + "AND NOT EXISTS (SELECT 1 FROM computer_pc_configurations WHERE computer_id = c.id AND pc_configuration_id = r.id);"
                    + "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) "
                    + "SELECT c.id, r.id FROM computers c, pc_configurations r "
                    + "WHERE c.title = 'Стримерский ПК' AND r.room_type IN ('Киберспортсмен', 'Премиум') "
                    + "AND NOT EXISTS (SELECT 1 FROM computer_pc_configurations WHERE computer_id = c.id AND pc_configuration_id = r.id);";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("contextInitialized: Starting database initialization...");
        try {
            DBConnection.init();
            initializeDatabaseSchema();
        } catch (Exception e) {
            logger.atError().withThrowable(e).log("Failed to initialize database schema or connection pool");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize database connection or schema", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("contextDestroyed: Closing database connection pool.");
        DBConnection.destroy();
    }

    private void initializeDatabaseSchema() throws SQLException, ClassNotFoundException {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("Dropping all existing tables...");
            dropAllTables(conn);

            logger.info("Creating database schema...");
            stmt.execute(CREATE_ROLES_TABLE);
            stmt.execute(CREATE_USERS_TABLE);
            stmt.execute(CREATE_GAMINGZONES_TABLE);
            stmt.execute(CREATE_PC_CONFIGURATIONS_TABLE);
            stmt.execute(CREATE_COMPUTERS_TABLE);
            stmt.execute(CREATE_COMPUTER_PC_CONFIGURATIONS_TABLE);
            stmt.execute(CREATE_RESERVATIONS_TABLE);

            logger.info("Inserting default data...");
            stmt.execute(INSERT_DEFAULT_ROLES);
            stmt.execute(INSERT_DEFAULT_ADMIN);
            stmt.execute(INSERT_DEFAULT_GAMINGZONES);
            stmt.execute(INSERT_DEFAULT_PC_CONFIGURATIONS);
            stmt.execute(INSERT_DEFAULT_COMPUTERS);
            stmt.execute(INSERT_DEFAULT_COMPUTER_PC_CONFIGURATIONS);

            logger.info("Database schema initialized successfully.");
        }
    }

    private void dropAllTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DROP TABLE IF EXISTS reservations CASCADE");
        stmt.execute("DROP TABLE IF EXISTS computer_pc_configurations CASCADE");
        stmt.execute("DROP TABLE IF EXISTS computers CASCADE");
        stmt.execute("DROP TABLE IF EXISTS pc_configurations CASCADE");
        stmt.execute("DROP TABLE IF EXISTS gamingZones CASCADE");
        stmt.execute("DROP TABLE IF EXISTS users CASCADE");
        stmt.execute("DROP TABLE IF EXISTS roles CASCADE");
    }
}