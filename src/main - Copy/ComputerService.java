package com.computerlist.service;

import com.computerlist.model.Computer;

import java.sql.SQLException;
import java.util.List;

public interface ComputerService {
    public List<Computer> searchComputers(String query, String startDate, String gamingZoneId, int page, int limit);
    public List<Computer> getComputersByAdminCreatedId(int user_id);
    Computer getComputerById(int id);
    List<Computer> getAllComputers();
    List<Computer> getComputersByGamingZone(int gamingZoneId);
    boolean createComputer(Computer computer);
    boolean updateComputer(Computer computer) throws SQLException, ClassNotFoundException;
    boolean deleteComputer(int id);
    public int countSearchComputers(String query, String startDate, String gamingZoneId);
}
