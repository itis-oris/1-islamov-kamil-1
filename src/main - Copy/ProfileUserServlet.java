package com.computerlist.controller.User;

import com.computerlist.model.User;
import com.computerlist.model.Reservation;
import com.computerlist.service.UserService;
import com.computerlist.service.ReservationService;
import com.computerlist.service.impl.UserServiceImpl;
import com.computerlist.service.impl.ReservationServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile")
public class ProfileUserServlet extends HttpServlet {

    private UserService userService;
    private ReservationService reservationService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
        reservationService = new ReservationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() +  "/login");
            return;
        }

        // Подгружаем бронирования пользователя
        List<Reservation> reservations = reservationService.getReservationsByUserId(user.getId());

        req.setAttribute("user", user);
        req.setAttribute("reservations", reservations);

        req.getRequestDispatcher("/templates/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() +  "/login");
            return;
        }

        // Изменяем имя пользователя
        String newName = req.getParameter("name");
        if (newName != null && !newName.trim().isEmpty()) {
            user.setName(newName.trim());
            userService.updateUser(user); // предполагаем, что UserService умеет обновлять пользователя
            session.setAttribute("user", user); // обновляем сессию
        }

        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}
