package com.computerlist.controller.Admin;

import com.computerlist.model.RoomOption;
import com.computerlist.model.Computer;
import com.computerlist.model.User;
import com.computerlist.service.GamingZoneService;
import com.computerlist.service.RoomOptionService;
import com.computerlist.service.ComputerService;
import com.computerlist.service.impl.GamingZoneServiceImpl;
import com.computerlist.service.impl.RoomOptionServiceImpl;
import com.computerlist.service.impl.ComputerServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/edit-computer")
public class EditComputerServlet extends HttpServlet {

    private ComputerService computerService = new ComputerServiceImpl();
    private GamingZoneService gamingZoneService = new GamingZoneServiceImpl();
    private RoomOptionService roomOptionService = new RoomOptionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID тура обязателен");
            return;
        }

        try {
            int computerId = Integer.parseInt(idParam);
            Computer computer = computerService.getComputerById(computerId);

            if (computer == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Тур не найден");
                return;
            }

            // Проверка: только автор может редактировать
            if (computer.getCreator_id() != currentUser.getId()) {
                resp.sendRedirect(req.getContextPath() + "/admin/profile");
                return;
            }

            // Загружаем данные для формы
            req.setAttribute("computer", computer);
            req.setAttribute("gamingZones", gamingZoneService.getAllGamingZones());
            req.setAttribute("allRoomOptions", roomOptionService.getAllRoomOptions());
            req.setAttribute("selectedRoomOptionIds", getSelectedRoomOptionIds(computerId));

            req.getRequestDispatcher("/templates/admin/edit-computer.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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
            Computer existingComputer = computerService.getComputerById(computerId);

            if (existingComputer == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Тур не найден");
                return;
            }

            // Проверка прав
            if (existingComputer.getCreator_id() != currentUser.getId()) {
                resp.sendRedirect(req.getContextPath() + "/admin/profile");
                return;
            }

            // Получаем данные из формы
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String gamingZoneIdStr = req.getParameter("gamingZoneId");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String hourlyRateStr = req.getParameter("hourlyRate");

            if (title == null || title.trim().isEmpty() ||
                    gamingZoneIdStr == null || startDateStr == null || endDateStr == null || hourlyRateStr == null) {
                req.setAttribute("errorMessage", "Все поля обязательны.");
                forwardWithError(req, resp, computerId);
                return;
            }

            int gamingZoneId = Integer.parseInt(gamingZoneIdStr);
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            double hourlyRate = Double.parseDouble(hourlyRateStr);

            if (startDate.isAfter(endDate)) {
                req.setAttribute("errorMessage", "Дата окончания не может быть раньше даты начала.");
                forwardWithError(req, resp, computerId);
                return;
            }

            existingComputer.setTitle(title.trim());
            existingComputer.setDescription(description != null ? description.trim() : "");
            existingComputer.setGamingZoneId(gamingZoneId);
            existingComputer.setStartDate(startDate);
            existingComputer.setEndDate(endDate);
            existingComputer.setHourlyRate(hourlyRate);

            if (computerService.updateComputer(existingComputer)) {
                // Обновляем связь с RoomOption
                String[] selectedRoomIds = req.getParameterValues("roomOptions");
                List<Integer> roomOptionIds = new ArrayList<>();
                if (selectedRoomIds != null) {
                    for (String id : selectedRoomIds) {
                        try {
                            roomOptionIds.add(Integer.parseInt(id));
                        } catch (NumberFormatException ignored) {}
                    }
                }
                computerService.assignRoomOptionsToComputer(computerId, roomOptionIds);

                resp.sendRedirect(req.getContextPath() + "/admin/profile?updated=success");
            } else {
                req.setAttribute("errorMessage", "Не удалось обновить тур.");
                forwardWithError(req, resp, computerId);
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
            forwardWithError(req, resp, Integer.parseInt(idParam));
        }
    }

    // Метод для получения уже выбранных RoomOption
    private List<Integer> getSelectedRoomOptionIds(int computerId) {
        List<RoomOption> selected = roomOptionService.getRoomOptionsByComputer(computerId);
        List<Integer> ids = new ArrayList<>();
        for (RoomOption r : selected) {
            ids.add(r.getId());
        }
        return ids;
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp, int computerId) throws ServletException, IOException {
        Computer computer = computerService.getComputerById(computerId);
        req.setAttribute("computer", computer);
        req.setAttribute("gamingZones", gamingZoneService.getAllGamingZones());
        req.setAttribute("allRoomOptions", roomOptionService.getAllRoomOptions());
        req.setAttribute("selectedRoomOptionIds", getSelectedRoomOptionIds(computerId));
        req.getRequestDispatcher("/templates/admin/edit-computer.jsp").forward(req, resp);
    }
}
