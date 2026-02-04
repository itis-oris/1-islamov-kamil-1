package com.computerlist.controller.Auth;

import com.computerlist.model.User;
import com.computerlist.service.impl.UserServiceImpl;
import com.computerlist.util.PasswordHasher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private UserServiceImpl userService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/templates/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String roleIdStr = req.getParameter("roleId");

        // Валидация
        if (name == null || name.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || confirmPassword == null ||
                !password.equals(confirmPassword)) {

            req.setAttribute("error", "Все поля обязательны. Пароли должны совпадать.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/templates/user/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6 || password.length() > 16) {
            req.setAttribute("error", "Пароль должен содержать от 6 до 16 символов.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/templates/user/register.jsp").forward(req, resp);
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
            // Разрешаем только ваши ID: 2 (менеджер) и 3 (пользователь)
            if (roleId != 2 && roleId != 3) {
                roleId = 3; // по умолчанию — пользователь
            }
        } catch (NumberFormatException e) {
            roleId = 3;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(PasswordHasher.hash(password));
        user.setRoleId(roleId);

        if (userService.createUser(user)) {
            resp.sendRedirect(req.getContextPath() + "/login?registered=success");
        } else {
            req.setAttribute("error", "Пользователь с таким email уже существует.");
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.getRequestDispatcher("/templates/user/register.jsp").forward(req, resp);
        }
    }
}