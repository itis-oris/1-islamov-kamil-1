package com.computerlist.dao.impl;


import com.computerlist.dao.ReservationDAO;
import com.computerlist.model.Reservation;
import com.computerlist.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    private static final Logger logger = LogManager.getLogger(ReservationDAOImpl.class);

    @Override
    public Reservation findById(int id) {
        String sql = "SELECT * FROM reservations WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractReservation(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findById error", e);
        }
        return null;
    }

    @Override
    public List<Reservation> findByUserId(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractReservation(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findByUserId error", e);
        }
        return list;
    }

    @Override
    public boolean create(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, computer_id, reservation_date, people_count, rooms_count, total_price, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getComputerId());
            ps.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
            ps.setInt(4, reservation.getPeopleCount());
            ps.setInt(5, reservation.getRoomsCount());
            ps.setDouble(6, reservation.getTotalPrice());
            ps.setString(7, reservation.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("create error", e);
        }
        return false;
    }

    @Override
    public boolean update(Reservation reservation) {
        String sql = "UPDATE reservations SET user_id=?, computer_id=?, reservation_date=?, people_count=?, rooms_count=?, total_price=?, status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getComputerId());
            ps.setTimestamp(3, Timestamp.valueOf(reservation.getReservationDate()));
            ps.setInt(4, reservation.getPeopleCount());
            ps.setInt(5, reservation.getRoomsCount());
            ps.setDouble(6, reservation.getTotalPrice());
            ps.setString(7, reservation.getStatus());
            ps.setInt(8, reservation.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("update error", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM reservations WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("delete error", e);
        }
        return false;
    }

    private Reservation extractReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("user_id"),
                null, // user загружается отдельным DAO
                rs.getInt("computer_id"),
                null, // computer загружается отдельным DAO
                rs.getTimestamp("reservation_date").toLocalDateTime(),
                rs.getInt("people_count"),
                rs.getInt("rooms_count"),
                rs.getDouble("total_price"),
                rs.getString("status")
        );
    }
}
