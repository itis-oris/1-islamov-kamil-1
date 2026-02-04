package com.computerlist.dao.impl;

import com.computerlist.dao.ComputerDAO;
import com.computerlist.model.RoomOption;
import com.computerlist.model.Computer;
import com.computerlist.model.User;
import com.computerlist.util.DBConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAOImpl implements ComputerDAO {

    private static final Logger logger = LogManager.getLogger(ComputerDAOImpl.class);


    @Override
    public List<Computer> search(String query, String startDate, String gamingZoneId, int limit, int offset) {
        List<Computer> computers = new ArrayList<>();

        StringBuilder sql = new StringBuilder("SELECT * FROM computers WHERE 1=1");

        // динамически собираем SQL в зависимости от фильтров
        if (query != null && !query.isEmpty()) {
            sql.append(" AND LOWER(title) LIKE LOWER(?)");
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND start_date >= ?");
        }
        if (gamingZoneId != null && !gamingZoneId.isEmpty()) {
            sql.append(" AND gamingZone_id = ?");
        }

        sql.append(" ORDER BY start_date ASC LIMIT ? OFFSET ?");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (query != null && !query.isEmpty()) {
                ps.setString(paramIndex++, "%" + query + "%");
            }
            if (startDate != null && !startDate.isEmpty()) {
                ps.setDate(paramIndex++, Date.valueOf(startDate));
            }
            if (gamingZoneId != null && !gamingZoneId.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(gamingZoneId));
            }


            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    computers.add(extractComputer(rs));
                }
            }
            logger.info("SQL: " + sql);
            logger.info("Params: query=" + query + ", startDate=" + startDate + ", gamingZoneId=" + gamingZoneId);
            logger.info("Found computers: " + computers.size());

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("search computers error", e);
        }

        return computers;
    }

    @Override
    public Computer findById(int id) {
        String sql = "SELECT * FROM computers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractComputer(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findById error", e);
        }
        return null;
    }

    @Override
    public List<Computer> findAll() {
        List<Computer> list = new ArrayList<>();
        String sql = "SELECT * FROM computers";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractComputer(rs));
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findAll error", e);
        }
        return list;
    }

    @Override
    public List<Computer> findByGamingZone(int gamingZoneId) {
        List<Computer> list = new ArrayList<>();
        String sql = "SELECT * FROM computers WHERE gamingZone_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gamingZoneId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractComputer(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findByGamingZone error", e);
        }
        return list;
    }

    public List<Computer> findComputersByAdminCreated(int user_id) {
        String sql = "SELECT id, title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id FROM computers WHERE creator_id=?";
        List<Computer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, user_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractComputer(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findByGamingZone error", e);
        }
        return list;
    }

    @Override
    public boolean create(Computer computer) {
        String sql = "INSERT INTO computers (title, description, gamingZone_id, start_date, end_date, hourly_rate, creator_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, computer.getTitle());
            ps.setString(2, computer.getDescription());
            ps.setInt(3, computer.getGamingZoneId());
            ps.setDate(4, Date.valueOf(computer.getStartDate()));
            ps.setDate(5, Date.valueOf(computer.getEndDate()));
            ps.setDouble(6, computer.getHourlyRate());
            ps.setInt(7, computer.getCreator_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("create computer error", e);
        }
        return false;
    }

    @Override
    public boolean update(Computer computer) {
        String sql = "UPDATE computers SET title=?, description=?, gamingZone_id=?, start_date=?, end_date=?, hourly_rate=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, computer.getTitle());
            ps.setString(2, computer.getDescription());
            ps.setInt(3, computer.getGamingZoneId());
            ps.setDate(4, Date.valueOf(computer.getStartDate()));
            ps.setDate(5, Date.valueOf(computer.getEndDate()));
            ps.setDouble(6, computer.getHourlyRate());
            ps.setInt(7, computer.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) return false;

            // ← Вот тут получаем id из БД
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    computer.setId(generatedId); // сохраняем в объекте
                }
            }

            return true;

        } catch (Exception e) {
            logger.error("create computer error", e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM computers WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("delete computer error", e);
        }
        return false;
    }
    // === НОВЫЕ МЕТОДЫ ДЛЯ MANY-TO-MANY ===

    @Override
    public void clearRoomOptionsForComputer(int computerId) {
        String sql = "DELETE FROM computer_pc_configurations WHERE computer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, computerId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("clearRoomOptionsForComputer error", e);
        }
    }

    @Override
    public void linkRoomOptionsToComputer(int computerId, List<Integer> roomOptionIds) {
        if (roomOptionIds == null || roomOptionIds.isEmpty()) return;

        String sql = "INSERT INTO computer_pc_configurations (computer_id, pc_configuration_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int roomId : roomOptionIds) {
                ps.setInt(1, computerId);
                ps.setInt(2, roomId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("linkRoomOptionsToComputer error", e);
        }
    }
    @Override
    public int countSearchComputers(String query, String startDate, String gamingZoneId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM computers WHERE 1=1");

        if (query != null && !query.isEmpty()) {
            sql.append(" AND LOWER(title) LIKE LOWER(?)");
        }
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND start_date >= ?");
        }
        if (gamingZoneId != null && !gamingZoneId.isEmpty()) {
            sql.append(" AND gamingZone_id = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (query != null && !query.isEmpty()) {
                ps.setString(paramIndex++, "%" + query + "%");
            }
            if (startDate != null && !startDate.isEmpty()) {
                ps.setDate(paramIndex++, Date.valueOf(startDate));
            }
            if (gamingZoneId != null && !gamingZoneId.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(gamingZoneId));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("countSearchComputers error", e);
        }

        return 0;
    }


    private Computer extractComputer(ResultSet rs) throws SQLException {
        return new Computer(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("gamingZone_id"),
                null, // gamingZone можно загрузить отдельно
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(),
                rs.getDouble("hourly_rate"),
                rs.getInt("creator_id"));
    }
}
