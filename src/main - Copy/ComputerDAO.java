package com.computerlist.dao;


import com.computerlist.model.Computer;

import java.sql.SQLException;
import java.util.List;

public interface ComputerDAO {
    public List<Computer> search(String query, String startDate, String gamingZoneId, int limit, int offset);
    Computer findById(int id);
    List<Computer> findAll();
    List<Computer> findByGamingZone(int gamingZoneId);
    public List<Computer> findComputersByAdminCreated(int user_id);
    boolean create(Computer computer);
    boolean update(Computer computer) throws SQLException, ClassNotFoundException;
    boolean delete(int id);
    public void clearRoomOptionsForComputer(int computerId);
    public void linkRoomOptionsToComputer(int computerId, List<Integer> roomOptionIds);
    public int countSearchComputers(String query, String startDate, String gamingZoneId);

}

