package com.computerlist.dao;

import com.computerlist.model.RoomOption;

import java.util.List;

public interface RoomOptionDAO {
    public  List<RoomOption> findAllRoomsByComputerId(int computer_id);
    RoomOption findById(int id);
    List<RoomOption> findAll();
    boolean create(RoomOption option);
    boolean update(RoomOption option);
    boolean delete(int id);
}
