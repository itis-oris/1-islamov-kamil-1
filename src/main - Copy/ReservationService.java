package com.computerlist.service;

import com.computerlist.model.Reservation;
import java.util.List;

public interface ReservationService {

    Reservation getReservationById(int id);
    List<Reservation> getReservationsByUserId(int userId);
    boolean createReservation(Reservation reservation);
    boolean updateReservation(Reservation reservation);
    boolean cancelReservation(int id); // логическое удаление или изменение статуса
    boolean deleteReservation(int id); // физическое удаление
}
