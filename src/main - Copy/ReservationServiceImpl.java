package com.computerlist.service.impl;

import com.computerlist.dao.ReservationDAO;
import com.computerlist.dao.ComputerDAO;
import com.computerlist.dao.UserDAO;
import com.computerlist.dao.impl.ReservationDAOImpl;
import com.computerlist.dao.impl.ComputerDAOImpl;
import com.computerlist.dao.impl.UserDAOImpl;
import com.computerlist.model.Reservation;
import com.computerlist.model.Computer;
import com.computerlist.model.User;
import com.computerlist.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();
    private final UserDAO userDAO = new UserDAOImpl();
    private final ComputerDAO computerDAO = new ComputerDAOImpl();

    @Override
    public Reservation getReservationById(int id) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation != null) {
            // Подгрузка связанных сущностей
            User user = userDAO.findById(reservation.getUserId());
            Computer computer = computerDAO.findById(reservation.getComputerId());
            reservation.setUser(user);
            reservation.setComputer(computer);
        }
        return reservation;
    }

    @Override
    public List<Reservation> getReservationsByUserId(int userId) {
        List<Reservation> list = reservationDAO.findByUserId(userId);
        for (Reservation b : list) {
            b.setUser(userDAO.findById(b.getUserId()));
            b.setComputer(computerDAO.findById(b.getComputerId()));
        }
        return list;
    }

    @Override
    public boolean createReservation(Reservation reservation) {
        if (reservation.getReservationDate() == null) {
            reservation.setReservationDate(LocalDateTime.now());
        }
        if (reservation.getStatus() == null || reservation.getStatus().isEmpty()) {
            reservation.setStatus("PENDING");
        }

        // Упрощённый расчёт для ПК: цена за час * количество часов
        Computer computer = computerDAO.findById(reservation.getComputerId());
        if (computer != null) {
            // Предполагаем, что durationHours хранится в reservation.peopleCount
            int durationHours = reservation.getPeopleCount();
            reservation.setTotalPrice(computer.getHourlyRate() * durationHours);
        }

        return reservationDAO.create(reservation);
    }

    @Override
    public boolean updateReservation(Reservation reservation) {
        return reservationDAO.update(reservation);
    }

    @Override
    public boolean cancelReservation(int id) {
        Reservation reservation = reservationDAO.findById(id);
        if (reservation == null) return false;

        reservation.setStatus("CANCELLED");
        return reservationDAO.update(reservation);
    }

    @Override
    public boolean deleteReservation(int id) {
        return reservationDAO.delete(id);
    }
}

