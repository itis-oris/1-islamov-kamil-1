package com.computerlist.dao;



import com.computerlist.dao.impl.*;
import com.computerlist.model.*;

import java.time.LocalDateTime;
import java.util.List;

public class TestDAO {
    public static void main(String[] args) {

        // ----------------- UserDAO -----------------
        UserDAOImpl userDAO = new UserDAOImpl();
        RoleDAOImpl roleDAO = new RoleDAOImpl();

        // Найти пользователя
        User user = userDAO.findById(1);
        System.out.println("Find user by ID: " + user);

        // Создать нового пользователя
        User newUser = new User();
        newUser.setName("Test User");
        newUser.setEmail("testuser@mail.com");
        newUser.setPasswordHash("hashedPassword");
        newUser.setRoleId(2); // Предположим, роль USER
        boolean created = userDAO.create(newUser);
        System.out.println("User created: " + created);

        // Найти всех пользователей
        List<User> allUsers = userDAO.findAll();
        allUsers.forEach(System.out::println);

        // ----------------- RoleDAO -----------------
        Role role = roleDAO.findByName("ADMIN");
        System.out.println("Role by name: " + role);

        // ----------------- GamingZoneDAO -----------------
        GamingZoneDAOImpl gamingZoneDAO = new GamingZoneDAOImpl();
        List<GamingZone> gamingZones = gamingZoneDAO.findAll();
        gamingZones.forEach(System.out::println);

        // ----------------- ComputerDAO -----------------
        ComputerDAOImpl computerDAO = new ComputerDAOImpl();
        List<Computer> computers = computerDAO.findAll();
        computers.forEach(System.out::println);

        // ----------------- RoomOptionDAO -----------------
        RoomOptionDAOImpl roomDAO = new RoomOptionDAOImpl();
        List<RoomOption> rooms = roomDAO.findAll();
        rooms.forEach(System.out::println);

        // ----------------- ReservationDAO -----------------
        ReservationDAOImpl reservationDAO = new ReservationDAOImpl();

        // Создать бронирование
        Reservation reservation = new Reservation();
        reservation.setUserId(1);
        reservation.setComputerId(1);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setPeopleCount(2);
        reservation.setRoomsCount(1);
        reservation.setTotalPrice(1000.0);
        reservation.setStatus("PENDING");

        boolean reservationCreated = reservationDAO.create(reservation);
        System.out.println("Reservation created: " + reservationCreated);

        // Получить бронирования по пользователю
        List<Reservation> reservations = reservationDAO.findByUserId(1);
        reservations.forEach(System.out::println);
    }
}

