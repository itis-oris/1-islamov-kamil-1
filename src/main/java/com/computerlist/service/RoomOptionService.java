package com.computerlist.service;

import com.computerlist.model.RoomOption;
import java.util.List;

public interface RoomOptionService {

    RoomOption getRoomOptionById(int id);
    List<RoomOption> getAllRoomOptions();
    List<RoomOption> getRoomOptionsByComputer(int computerId);
    boolean createRoomOption(RoomOption option);
    boolean updateRoomOption(RoomOption option);
    boolean deleteRoomOption(int id);
}
