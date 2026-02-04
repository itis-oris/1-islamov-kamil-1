package com.computerlist.model;

public class RoomOption {
    private int id;
    private String roomType;
    private int capacity;
    private String bed_type;
    private int bed_count;
    private double priceMultiplier;

    public RoomOption(int id, String roomType, int capacity, String bed_type, int bed_count, double priceMultiplier) {
        this.id = id;
        this.roomType = roomType;
        this.capacity = capacity;
        this.bed_type = bed_type;
        this.bed_count = bed_count;
        this.priceMultiplier = priceMultiplier;
    }

    public RoomOption() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getBed_type() {
        return bed_type;
    }

    public void setBed_type(String bed_type) {
        this.bed_type = bed_type;
    }

    public int getBed_count() {
        return bed_count;
    }

    public void setBed_count(int bed_count) {
        this.bed_count = bed_count;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public void setPriceMultiplier(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    // Конструкторы, геттеры и сеттеры
}