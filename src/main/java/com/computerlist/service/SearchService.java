package com.computerlist.service;

import com.computerlist.model.Computer;

import java.util.List;

public interface SearchService {
    public List<Computer> searchComputers(String query);
}
