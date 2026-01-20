package com.computerlist.service.impl;


import com.computerlist.dao.impl.ComputerDAOImpl;
import com.computerlist.model.Computer;
import com.computerlist.service.SearchService;

import java.util.List;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private ComputerDAOImpl computerDAO = new ComputerDAOImpl();

    public List<Computer> searchComputers(String query) {
        if (query == null || query.isEmpty()) {
            return computerDAO.findAll();
        }
        String lower = query.toLowerCase();
        return computerDAO.findAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
}
