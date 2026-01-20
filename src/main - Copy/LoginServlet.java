package com.computerlist.controller.Auth;

import com.computerlist.model.Role;
import com.computerlist.model.User;
import com.computerlist.service.RoleService;
import com.computerlist.service.UserService;
import com.computerlist.service.impl.RoleServiceImpl;
import com.computerlist.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserService userService;
    private RoleService roleService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
        roleService = new RoleServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("templates/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userService.authenticate(email, password);

        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            String role = roleService.getRoleById(user.getRoleId()).getName();
            // Перенаправление в зависимости от роли
            switch (role) {
                case "MANAGER" -> resp.sendRedirect("admin/profile");
                default -> resp.sendRedirect("profile");
            }
        } else {
            req.setAttribute("error", "Неверный email или пароль");
            req.getRequestDispatcher("templates/user/login.jsp").forward(req, resp);
        }
    }
}

