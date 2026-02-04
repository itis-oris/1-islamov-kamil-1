package com.computerlist.service.impl;

import com.computerlist.service.RoomOptionService;
import com.computerlist.dao.RoomOptionDAO;
import com.computerlist.dao.impl.RoomOptionDAOImpl;
import com.computerlist.model.RoomOption;

import java.util.List;

public class RoomOptionServiceImpl implements RoomOptionService {

    private final RoomOptionDAO roomOptionDAO = new RoomOptionDAOImpl();

    @Override
    public RoomOption getRoomOptionById(int id) {
        return roomOptionDAO.findById(id);
    }

    @Override
    public List<RoomOption> getAllRoomOptions() {
        return roomOptionDAO.findAll();
    }

    @Override
    public List<RoomOption> getRoomOptionsByComputer(int computerId) {
        return roomOptionDAO.findAllRoomsByComputerId(computerId);
    }

    @Override
    public boolean createRoomOption(RoomOption option) {
        if (option.getPriceMultiplier() <= 0) {
            option.setPriceMultiplier(1.0);
        }
        return roomOptionDAO.create(option);
    }

    @Override
    public boolean updateRoomOption(RoomOption option) {
        return roomOptionDAO.update(option);
    }

    @Override
    public boolean deleteRoomOption(int id) {
        return roomOptionDAO.delete(id);
    }
}