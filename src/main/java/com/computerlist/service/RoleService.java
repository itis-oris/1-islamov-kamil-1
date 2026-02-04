package com.computerlist.service;

import com.computerlist.model.Role;
import java.util.List;

public interface RoleService {
    Role getRoleById(int id);
    Role getRoleByName(String name);
    List<Role> getAllRoles();
}
