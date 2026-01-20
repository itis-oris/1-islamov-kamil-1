package com.computerlist.controller.Admin;

import com.computerlist.model.User;
import com.computerlist.service.ComputerService;
import com.computerlist.service.impl.ComputerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/admin/delete-computer")
public class DeleteComputerServlet extends HttpServlet {

    private ComputerService computerService = new ComputerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String idParam = req.getParameter("id");

        if (idParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID тура обязателен");
            return;
        }

        try {
            int computerId = Integer.parseInt(idParam);
            var computer = computerService.getComputerById(computerId);

            if (computer == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Тур не найден");
                return;
            }

            // Только автор может удалить
            if (computer.getCreator_id() != currentUser.getId()) {
                resp.sendRedirect(req.getContextPath() + "/admin/profile");
                return;
            }

            if (computerService.deleteComputer(computerId)) {
                resp.sendRedirect(req.getContextPath() + "/admin/profile?deleted=success");
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/profile?deleted=error");
            }

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный ID");
        }
    }
}