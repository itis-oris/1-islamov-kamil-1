package com.computerlist.service.impl;

import com.computerlist.dao.GamingZoneDAO;
import com.computerlist.dao.impl.GamingZoneDAOImpl;
import com.computerlist.model.GamingZone;
import com.computerlist.service.GamingZoneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GamingZoneServiceImpl implements GamingZoneService {

    private static final Logger logger = LogManager.getLogger(GamingZoneServiceImpl.class);
    private final GamingZoneDAO gamingZoneDAO = new GamingZoneDAOImpl();


    @Override
    public GamingZone getGamingZoneById(int id) {
        if (id <= 0) {
            logger.warn("Invalid gamingZone id: {}", id);
            return null;
        }
        return gamingZoneDAO.findById(id);
    }

    @Override
    public List<GamingZone> getAllGamingZones() {
        return gamingZoneDAO.findAll();
    }

    @Override
    public boolean addGamingZone(GamingZone gamingZone) {
        if (gamingZone == null || gamingZone.getName() == null || gamingZone.getName().isEmpty()) {
            logger.warn("Cannot add gamingZone: invalid data");
            return false;
        }
        return gamingZoneDAO.create(gamingZone);
    }

    @Override
    public boolean updateGamingZone(GamingZone gamingZone) {
        if (gamingZone == null || gamingZone.getId() <= 0) {
            logger.warn("Cannot update gamingZone: invalid id");
            return false;
        }
        return gamingZoneDAO.update(gamingZone);
    }

    @Override
    public boolean removeGamingZone(int id) {
        if (id <= 0) {
            logger.warn("Cannot remove gamingZone: invalid id");
            return false;
        }
        return gamingZoneDAO.delete(id);
    }
    @Override
    public List<GamingZone> searchByName(String namePart, int limit) {
        return gamingZoneDAO.findByNameContaining(namePart, limit);
    }
}
