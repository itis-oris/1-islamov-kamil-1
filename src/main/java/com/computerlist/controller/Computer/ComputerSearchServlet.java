package com.computerlist.controller.Computer;

import com.computerlist.model.Computer;
import com.computerlist.service.GamingZoneService;
import com.computerlist.service.ComputerService;
import com.computerlist.service.impl.GamingZoneServiceImpl;
import com.computerlist.service.impl.ComputerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/computers/search")
public class ComputerSearchServlet extends HttpServlet {

    private ComputerService computerService;
    private GamingZoneService gamingZoneService = new GamingZoneServiceImpl();

    @Override
    public void init() {
        computerService = new ComputerServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String ajax = req.getParameter("ajax");
        String query = req.getParameter("query");
        String startDate = req.getParameter("startDate");


        String gamingZoneIdParam = req.getParameter("gamingZoneId");
        Integer gamingZoneId = null;
        if (gamingZoneIdParam != null && !gamingZoneIdParam.isEmpty()) {
            try {
                gamingZoneId = Integer.parseInt(gamingZoneIdParam);
            } catch (NumberFormatException ignored) {
                // просто игнорируем, если не число(unreal)
            }
        }

        int page = parseInt(req.getParameter("page"), 1);
        int pageSize = 10;

        List<Computer> computers = computerService.searchComputers(
                query,
                startDate,
                gamingZoneId != null ? gamingZoneId.toString() : null,
                page,
                pageSize
        );

        int totalComputers = computerService.countSearchComputers(
                query,
                startDate,
                gamingZoneId != null ? gamingZoneId.toString() : null
        );
        int totalPages = (int) Math.ceil((double) totalComputers / pageSize);

        req.setAttribute("computers", computers);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);
        req.setAttribute("gamingZones", gamingZoneService.getAllGamingZones());


        if ("1".equals(ajax)) {
            req.getRequestDispatcher("/templates/computer/computer-results.jsp").forward(req, resp);

        } else {
            req.getRequestDispatcher("/templates/computer/computer-search.jsp").forward(req, resp);
        }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }
}
