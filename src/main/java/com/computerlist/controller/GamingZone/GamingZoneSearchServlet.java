package com.computerlist.controller.GamingZone;

import com.computerlist.model.GamingZone;
import com.computerlist.service.GamingZoneService;
import com.computerlist.service.impl.GamingZoneServiceImpl;
import com.computerlist.util.JsonResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/gamingZones/search")
public class GamingZoneSearchServlet extends HttpServlet {

    private GamingZoneService gamingZoneService = new GamingZoneServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String query = req.getParameter("q");

        List<GamingZone> results = new ArrayList<>();
        if (query != null && !query.trim().isEmpty()) {
            // Ограничиваем 5 результатами
            results = gamingZoneService.searchByName(query.trim(), 5);
        }

        // Используем твою утилиту
        JsonResponseWriter.writeJson(resp, results);
    }

}