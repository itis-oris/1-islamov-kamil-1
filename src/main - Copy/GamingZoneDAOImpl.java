package com.computerlist.dao.impl;


import com.computerlist.dao.GamingZoneDAO;
import com.computerlist.model.GamingZone;
import com.computerlist.util.DBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GamingZoneDAOImpl implements GamingZoneDAO {

    private static final Logger logger = LogManager.getLogger(GamingZoneDAOImpl.class);

    @Override
    public GamingZone findById(int id) {
        String sql = "SELECT * FROM gamingZones WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractGamingZone(rs);
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findById error", e);
        }
        return null;
    }

    @Override
    public List<GamingZone> findAll() {
        List<GamingZone> list = new ArrayList<>();
        String sql = "SELECT * FROM gamingZones";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractGamingZone(rs));
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findAll error", e);
        }
        return list;
    }

    @Override
    public boolean create(GamingZone gamingZone) {
        String sql = "INSERT INTO gamingZones (name, country) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gamingZone.getName());
            ps.setString(2, gamingZone.getCountry());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("create error", e);
        }
        return false;
    }

    @Override
    public boolean update(GamingZone gamingZone) {
        String sql = "UPDATE gamingZones SET name=?, country=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gamingZone.getName());
            ps.setString(2, gamingZone.getCountry());
            ps.setInt(3, gamingZone.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("update error", e);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM gamingZones WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("delete error", e);
        }
        return false;
    }
    @Override
    public List<GamingZone> findByNameContaining(String namePart, int limit) {
        String sql = "SELECT id, name, country FROM gamingZones WHERE LOWER(name) LIKE LOWER(?) ORDER BY name LIMIT ?";
        List<GamingZone> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + namePart + "%");
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractGamingZone(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("findByGamingZone error", e);
        }
        return list;
    }

    private GamingZone extractGamingZone(ResultSet rs) throws SQLException {
        return new GamingZone(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("country")
        );
    }
}

