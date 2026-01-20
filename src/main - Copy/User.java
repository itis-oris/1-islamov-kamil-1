package com.computerlist.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private int roleId;
    private Role role; // optional — если ты будешь подгружать объект Role

    public User() {}

    public User(int id, String name, String email, String passwordHash, int roleId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roleId = roleId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getRoleId() {
        return roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", roleId=" + roleId +
                ", role=" + role +
                '}';
    }
}
