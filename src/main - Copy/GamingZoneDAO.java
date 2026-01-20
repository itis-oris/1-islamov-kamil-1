package com.computerlist.dao;


import com.computerlist.model.GamingZone;

import java.util.List;

public interface GamingZoneDAO {
    GamingZone findById(int id);
    List<GamingZone> findAll();
    boolean create(GamingZone gamingZone);
    boolean update(GamingZone gamingZone);
    boolean delete(int id);
    List<GamingZone> findByNameContaining(String namePart, int limit);
}