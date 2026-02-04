package com.computerlist.controller.Computer;

import com.computerlist.model.Reservation;
import com.computerlist.model.RoomOption;
import com.computerlist.model.Computer;
import com.computerlist.model.User;
import com.computerlist.service.ReservationService;
import com.computerlist.service.RoomOptionService;
import com.computerlist.service.ComputerService;
import com.computerlist.service.impl.ReservationServiceImpl;
import com.computerlist.service.impl.RoomOptionServiceImpl;
import com.computerlist.service.impl.ComputerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/computers")
public class ComputerDetailsServlet extends HttpServlet {

    private ComputerService computerService;
    private RoomOptionService roomOptionService;
    private ReservationService reservationService;

    @Override
    public void init() {
        computerService = new ComputerServiceImpl();
        roomOptionService = new RoomOptionServiceImpl();
        reservationService = new ReservationServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Computer ID is required");
            return;
        }
        String realPath = req.getServletContext().getRealPath("/img/computer/" + idParam);
        File photoDir = new File(realPath);
        try {
            int computerId = Integer.parseInt(idParam);
            Computer computer = computerService.getComputerById(computerId);

            if (computer == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Computer not found");
                return;
            }
            List<String> photoFileNames = new ArrayList<>();

            if (photoDir.exists() && photoDir.isDirectory()) {
                File[] files = photoDir.listFiles((dir, name) -> {
                    String lower = name.toLowerCase();
                    return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
                            lower.endsWith(".png") || lower.endsWith(".webp");
                });

                if (files != null) {
                    for (File file : files) {
                        photoFileNames.add(file.getName());
                    }
                }
            }

            req.setAttribute("photoFileNames", photoFileNames);

            List<RoomOption> roomOptions = roomOptionService.getRoomOptionsByComputer(computerId);

            req.setAttribute("computer", computer);
            req.setAttribute("roomOptions", roomOptions);

            req.getRequestDispatcher("/templates/computer/computer-details.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid computer ID format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Проверка авторизации (предполагается, что user.id хранится в сессии)
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login?error=unauthorized");
            return;
        }

        // Получаем пользователя из сессии
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        // Получаем параметры формы
        String computerIdParam = req.getParameter("computerId");
        String peopleCountParam = req.getParameter("peopleCount");
        String roomsCountParam = req.getParameter("roomsCount");

        if (computerIdParam == null || peopleCountParam == null || roomsCountParam == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required reservation parameters");
            return;
        }

        try {
            int computerId = Integer.parseInt(computerIdParam);
            int peopleCount = Integer.parseInt(peopleCountParam);
            int roomsCount = Integer.parseInt(roomsCountParam);

            // Защита от некорректных значений
            if (peopleCount <= 0 || roomsCount <= 0) {
                req.setAttribute("errorMessage", "Количество людей и комнат должно быть больше 0.");
                handleGetAndShowError(req, resp, computerId);
                return;
            }

            // Создаём объект бронирования
            Reservation reservation = new Reservation();
            reservation.setUserId(userId);
            reservation.setComputerId(computerId);
            reservation.setPeopleCount(peopleCount);
            reservation.setRoomsCount(roomsCount);
            reservation.setReservationDate(LocalDateTime.now());
            // totalPrice и status будут установлены в ReservationServiceImpl

            // Сохраняем бронь
            if (reservationService.createReservation(reservation)) {
                // Успешно → редирект на страницу подтверждения или личный кабинет
                resp.sendRedirect(req.getContextPath() + "/profile?reservation=success");
            } else {
                req.setAttribute("errorMessage", "Не удалось создать бронирование. Попробуйте позже.");
                handleGetAndShowError(req, resp, computerId);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Некорректные данные формы.");
            handleGetAndShowError(req, resp, Integer.parseInt(computerIdParam));
        }
    }

    // Вспомогательный метод: повторно загрузить тур и показать ошибку
    private void handleGetAndShowError(HttpServletRequest req, HttpServletResponse resp, int computerId) throws ServletException, IOException {
        Computer computer = computerService.getComputerById(computerId);
        List<RoomOption> roomOptions = roomOptionService.getRoomOptionsByComputer(computerId);

        req.setAttribute("computer", computer);
        req.setAttribute("roomOptions", roomOptions);
        req.getRequestDispatcher("/templates/computer/computer-details.jsp").forward(req, resp);
    }
}