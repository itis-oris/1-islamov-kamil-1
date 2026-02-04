package com.computerlist.dao;


import com.computerlist.model.Role;

import java.util.List;

public interface RoleDAO {
    Role findById(int id);
    Role findByName(String name);
    List<Role> findAll();
}