package com.computerlist.service.impl;

import com.computerlist.dao.RoleDAO;
import com.computerlist.dao.impl.RoleDAOImpl;
import com.computerlist.model.Role;
import com.computerlist.service.RoleService;

import java.util.List;

public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO = new RoleDAOImpl();

    @Override
    public Role getRoleById(int id) {
        return roleDAO.findById(id);
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDAO.findByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDAO.findAll();
    }
}

