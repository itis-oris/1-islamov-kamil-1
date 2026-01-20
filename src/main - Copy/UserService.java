package com.computerlist.service;

import com.computerlist.model.User;
import java.util.List;

public interface UserService {

    User getUserById(int id);
    User getUserByEmail(String email);
    List<User> getAllUsers();
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    User authenticate(String email, String password);
}

