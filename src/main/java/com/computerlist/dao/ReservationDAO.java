package com.computerlist.dao;


import com.computerlist.model.Reservation;

import java.util.List;

public interface ReservationDAO {
    Reservation findById(int id);
    List<Reservation> findByUserId(int userId);
    boolean create(Reservation reservation);
    boolean update(Reservation reservation);
    boolean delete(int id);
}

