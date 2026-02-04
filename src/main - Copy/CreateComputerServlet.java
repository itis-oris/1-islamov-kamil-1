package com.computerlist.controller.Admin;

import com.computerlist.model.GamingZone;
import com.computerlist.model.RoomOption;
import com.computerlist.model.Computer;
import com.computerlist.service.GamingZoneService;
import com.computerlist.service.RoomOptionService;
import com.computerlist.service.ComputerService;
import com.computerlist.service.UserService;
import com.computerlist.service.impl.GamingZoneServiceImpl;
import com.computerlist.service.impl.RoomOptionServiceImpl;
import com.computerlist.service.impl.ComputerServiceImpl;
import com.computerlist.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 5 * 1024 * 1024,    // 5 MB на файл
        maxRequestSize = 25 * 1024 * 1024 // 25 MB всего
)
@WebServlet("/admin/create-computer")
public class CreateComputerServlet extends HttpServlet {

    private ComputerService computerService = new ComputerServiceImpl();
    private GamingZoneService gamingZoneService = new GamingZoneServiceImpl();
    private UserService userService = new UserServiceImpl();
    private RoomOptionService roomOptionService = new RoomOptionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Загружаем список направлений для выпадающего списка
        List<GamingZone> gamingZones = gamingZoneService.getAllGamingZones();
        req.setAttribute("gamingZones", gamingZones);
        List<RoomOption> allRoomOptions = roomOptionService.getAllRoomOptions();
        req.setAttribute("allRoomOptions", allRoomOptions);

        req.getRequestDispatcher("/templates/admin/create-computer.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Object userObj = session.getAttribute("user");
        int adminId = (userObj instanceof com.computerlist.model.User)
                ? ((com.computerlist.model.User) userObj).getId()
                : (Integer) userObj;

        try {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String gamingZoneIdStr = req.getParameter("gamingZoneId");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String hourlyRateStr = req.getParameter("hourlyRate");

            if (title == null || title.trim().isEmpty() ||
                    gamingZoneIdStr == null || startDateStr == null || endDateStr == null || hourlyRateStr == null) {
                req.setAttribute("errorMessage", "Все поля обязательны.");
                forwardWithError(req, resp);
                return;
            }

            int gamingZoneId = Integer.parseInt(gamingZoneIdStr);
            LocalDate startDate = LocalDate.parse(startDateStr);
            LocalDate endDate = LocalDate.parse(endDateStr);
            double hourlyRate = Double.parseDouble(hourlyRateStr);

            if (startDate.isAfter(endDate)) {
                req.setAttribute("errorMessage", "Дата окончания не может быть раньше даты начала.");
                forwardWithError(req, resp);
                return;
            }

            Computer computer = new Computer();
            computer.setTitle(title.trim());
            computer.setDescription(description != null ? description.trim() : "");
            computer.setGamingZoneId(gamingZoneId);
            computer.setStartDate(startDate);
            computer.setEndDate(endDate);
            computer.setHourlyRate(hourlyRate);
            computer.setCreator_id(adminId);

            // Сначала создаём тур (без фото)
            if (computerService.createComputer(computer)) {
                int computerId = computer.getId();

                // Папка для фото: webapp/img/computer/{id}
                String uploadDir = req.getServletContext().getRealPath("/img/computer/" + computerId);
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists()) {
                    uploadFolder.mkdirs();
                }

                // Получаем все части с именем "photos"
                Collection<Part> photoParts = req.getParts().stream()
                        .filter(part -> "photos".equals(part.getName()))
                        .toList();

                int count = 0;
                for (Part photoPart : photoParts) {
                    if (photoPart.getSize() == 0) continue;
                    if (count >= 5) break; // Максимум 5

                    String originalFileName = Paths.get(photoPart.getSubmittedFileName()).getFileName().toString();
                    String targetFileName;

                    if (count == 0) {
                        // Первое фото → home.jpg
                        targetFileName = "home.jpg";
                    } else {
                        targetFileName = sanitizeFileName(originalFileName);
                        // Ограничиваем длину и заменяем пробелы
                        if (targetFileName.length() > 50) {
                            String ext = "";
                            int dot = targetFileName.lastIndexOf('.');
                            if (dot > 0) {
                                ext = targetFileName.substring(dot);
                                targetFileName = targetFileName.substring(0, dot);
                            }
                            targetFileName = targetFileName.substring(0, Math.min(40, targetFileName.length())) + ext;
                        }
                    }

                    Path targetPath = Paths.get(uploadDir, targetFileName);
                    try {
                        Files.copy(photoPart.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        // Логируем, но не прерываем — тур уже создан
                        e.printStackTrace();
                    }

                    count++;
                }

                resp.sendRedirect(req.getContextPath() + "/admin/profile?created=success");
            } else {
                req.setAttribute("errorMessage", "Не удалось создать тур. Попробуйте позже.");
                forwardWithError(req, resp);
            }

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Ошибка в данных: " + e.getMessage());
            forwardWithError(req, resp);
        }
    }

    private void forwardWithError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("gamingZones", gamingZoneService.getAllGamingZones());
        req.getRequestDispatcher("/templates/admin/create-computer.jsp").forward(req, resp);
    }
    private String sanitizeFileName(String name) {
        if (name == null || name.isEmpty()) {
            return "image.jpg";
        }
        // Оставляем только безопасные символы
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}