package com.computerlist.service.impl;

import com.computerlist.dao.RoleDAO;
import com.computerlist.dao.UserDAO;
import com.computerlist.dao.impl.RoleDAOImpl;
import com.computerlist.dao.impl.UserDAOImpl;
import com.computerlist.model.Role;
import com.computerlist.model.User;
import com.computerlist.service.UserService;
import com.computerlist.util.PasswordHasher;

import java.util.List;


public class UserServiceImpl implements UserService {
    private final UserDAO userDAO = new UserDAOImpl();
    private final RoleDAO roleDAO = new RoleDAOImpl();

    @Override
    public User getUserById(int id) {
        User user = userDAO.findById(id);
        if (user != null) {
            user.setRole(roleDAO.findById(user.getRoleId()));
        }
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userDAO.findByEmail(email);
        if (user != null) {
            user.setRole(roleDAO.findById(user.getRoleId()));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userDAO.findAll();
        users.forEach(u -> u.setRole(roleDAO.findById(u.getRoleId())));
        return users;
    }

    @Override
    public boolean createUser(User user) {
        if (userDAO.findByEmail(user.getEmail()) != null) {
            return false; // email уже занят
        }
        return userDAO.create(user);
    }

    @Override
    public boolean updateUser(User user) {
        return userDAO.update(user);
    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userDAO.findByEmail(email);
        if (user != null && user.getPasswordHash().equals(PasswordHasher.hash(password))) {
            return user;
        }
        return null;
    }
}



