package com.computerlist.dao;


import com.computerlist.model.User;

import java.util.List;

public interface UserDAO {
    User findById(int id);
    User findByEmail(String email);
    List<User> findAll();
    boolean create(User user);
    boolean update(User user);
    boolean delete(int id);
}
