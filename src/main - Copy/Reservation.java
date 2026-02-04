package com.computerlist.model;

import java.time.LocalDateTime;

public class Reservation {

    private int id;
    private int userId;
    private User user;
    private int computerId;
    private Computer computer;
    private LocalDateTime reservationDate;
    private int peopleCount;
    private int roomsCount;
    private double totalPrice;
    private String status;

    public Reservation() {}

    public Reservation(int id, int userId, User user, int computerId, Computer computer,
                   LocalDateTime reservationDate, int peopleCount,
                   int roomsCount, double totalPrice, String status) {
        this.id = id;
        this.userId = userId;
        this.user = user;
        this.computerId = computerId;
        this.computer = computer;
        this.reservationDate = reservationDate;
        this.peopleCount = peopleCount;
        this.roomsCount = roomsCount;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getComputerId() {
        return computerId;
    }

    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }

    public Computer getComputer() {
        return computer;
    }

    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public int getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(int roomsCount) {
        this.roomsCount = roomsCount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", userId=" + userId +
                ", user=" + user +
                ", computerId=" + computerId +
                ", computer=" + computer +
                ", reservationDate=" + reservationDate +
                ", peopleCount=" + peopleCount +
                ", roomsCount=" + roomsCount +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                '}';
    }
}
