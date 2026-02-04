package com.computerlist.model;

public class GamingZone {

    private int id;
    private String name;
    private String location;

    public GamingZone(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return this.location; // или создай отдельное поле country, если нужно
    }

    public void setCountry(String country) {
        this.location = country;
    }

    @Override
    public String toString() {
        return "GamingZone{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}