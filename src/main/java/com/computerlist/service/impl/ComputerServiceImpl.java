package com.computerlist.service.impl;

import com.computerlist.dao.ComputerDAO;
import com.computerlist.dao.GamingZoneDAO;
import com.computerlist.dao.impl.RoomOptionDAOImpl;
import com.computerlist.dao.impl.ComputerDAOImpl;
import com.computerlist.dao.impl.GamingZoneDAOImpl;
import com.computerlist.model.RoomOption;
import com.computerlist.model.Computer;
import com.computerlist.model.GamingZone;
import com.computerlist.model.User;
import com.computerlist.service.ComputerService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComputerServiceImpl implements ComputerService {

    private final ComputerDAOImpl computerDAO = new ComputerDAOImpl();
    private final GamingZoneDAO gamingZoneDAO = new GamingZoneDAOImpl();
    private final RoomOptionDAOImpl roomOptionDAO = new RoomOptionDAOImpl();

    public List<Computer> searchComputers(String query, String startDate, String gamingZoneId, int page, int limit) {
        int offset = (page - 1) * limit;
        return computerDAO.search(query, startDate, gamingZoneId, limit, offset);
    }

    public List<RoomOption> findAllRoomsByComputerId(int computer_id) {
        List<RoomOption> list = roomOptionDAO.findAllRoomsByComputerId(computer_id);
        return list;
    }

    @Override
    public Computer getComputerById(int id) {
        Computer computer = computerDAO.findById(id);
        if (computer != null) {
            GamingZone dest = gamingZoneDAO.findById(computer.getGamingZoneId());
            computer.setGamingZone(dest);
        }
        return computer;
    }

    @Override
    public List<Computer> getAllComputers() {
        List<Computer> computers = computerDAO.findAll();
        for (Computer t : computers) {
            t.setGamingZone(gamingZoneDAO.findById(t.getGamingZoneId()));
        }
        return computers;
    }

    @Override
    public List<Computer> getComputersByGamingZone(int gamingZoneId) {
        return computerDAO.findByGamingZone(gamingZoneId);
    }

    public List<Computer> getComputersByAdminCreatedId(int user_id) {
        return computerDAO.findComputersByAdminCreated(user_id);
    }

    @Override
    public boolean createComputer(Computer computer) {
        return computerDAO.create(computer);
    }

    @Override
    public boolean updateComputer(Computer computer) throws SQLException, ClassNotFoundException {
        return computerDAO.update(computer);
    }

    @Override
    public boolean deleteComputer(int id) {
        return computerDAO.delete(id);
    }

    public int countSearchComputers(String query, String startDate, String gamingZoneId) {
        return computerDAO.countSearchComputers(query,startDate, gamingZoneId);
    }

    @Override
    public void assignRoomOptionsToComputer(int computerId, List<Integer> roomOptionIds) {

    }
}

