package com.computerlist.controller.Admin;

import com.computerlist.model.Computer;
import com.computerlist.model.User;
import com.computerlist.service.ComputerService;
import com.computerlist.service.UserService;
import com.computerlist.service.impl.ComputerServiceImpl;
import com.computerlist.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/profile")
public class AdminProfileServlet extends HttpServlet {

    private ComputerService computerService = new ComputerServiceImpl();
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User admin = (User) session.getAttribute("user");
        if (admin.getRoleId() != 2) {
            resp.sendRedirect(req.getContextPath() + "index");
            return;
        }
        int adminId = admin.getId();

        // Обновляем данные пользователя (на случай, если роль изменилась)
        admin = userService.getUserById(adminId);

        // Загружаем туры, созданные этим менеджером
        List<Computer> computers = computerService.getComputersByAdminCreatedId(adminId);

        req.setAttribute("admin", admin);
        req.setAttribute("computers", computers);

        req.getRequestDispatcher("/templates/admin/profile.jsp").forward(req, resp);
    }
}