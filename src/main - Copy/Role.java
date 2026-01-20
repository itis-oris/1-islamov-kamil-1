package com.computerlist.model;

public class Role {

    private int id;
    private String name;

    public Role() {}

    public Role(int id, String name) {
        this.id = id;
        this.name = name;
    }
    // getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    //setters


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
