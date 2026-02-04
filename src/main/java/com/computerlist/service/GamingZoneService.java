package com.computerlist.service;

import com.computerlist.model.GamingZone;
import java.util.List;

public interface GamingZoneService {
    GamingZone getGamingZoneById(int id);
    List<GamingZone> getAllGamingZones();
    boolean addGamingZone(GamingZone gamingZone);
    boolean updateGamingZone(GamingZone gamingZone);
    boolean removeGamingZone(int id);
    List<GamingZone> searchByName(String namePart, int limit);
}
