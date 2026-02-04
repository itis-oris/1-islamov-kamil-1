package com.computerlist.controller.Computer;

import com.computerlist.model.Computer;
import com.computerlist.service.ComputerService;
import com.computerlist.service.impl.ComputerServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/computers/suggest")
public class ComputerSuggestServlet extends HttpServlet {
    private final ComputerService computerService = new ComputerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String query = req.getParameter("query");
        List<Computer> all = computerService.getAllComputers();

        List<String> suggestions = all.stream()
                .map(Computer::getTitle)
                .filter(t -> t.toLowerCase().contains(query.toLowerCase()))
                .distinct()
                .limit(10)
                .toList();

        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(new org.json.JSONArray(suggestions).toString());
    }
}

