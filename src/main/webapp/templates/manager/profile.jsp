<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../fragments/header.jsp" %>

<div class="container" style="max-width: 900px; margin: 30px auto;">
    <h1>Мой профиль</h1>

    <p><strong>Имя:</strong> ${admin.name}</p>
    <p><strong>Email:</strong> ${admin.email}</p>
    <p><strong>Роль:</strong> ${admin.role.name}</p>

    <div style="text-align: right; margin: 20px 0;">
        <a href="${pageContext.request.contextPath}/admin/create-computer"
           style="padding: 8px 16px; background-color: #28a745; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;">
            + Создать тур
        </a>
    </div>

    <h2>Мои туры</h2>

    <c:choose>
        <c:when test="${empty computers}">
            <p>Вы пока не создали ни одного тура.</p>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 15px;">
                <c:forEach var="computer" items="${computers}">
                    <div style="border: 1px solid #ddd; padding: 15px; border-radius: 6px; background: #f9f9f9;">
                        <h3>${computer.title}</h3>
                        <p><strong>Направление:</strong> ${computer.gamingZone.name}</p>
                        <p><strong>Даты:</strong> ${computer.startDate} – ${computer.endDate}</p>
                        <p><strong>Цена от:</strong> ${computer.hourlyRate} ₽</p>
                        <div>
                            <a href="${pageContext.request.contextPath}/computer?id=${computer.id}"
                               style="margin-right: 12px; color: #007bff; text-decoration: none;">Просмотр</a>
                            <a href="${pageContext.request.contextPath}/admin/edit-computer?id=${computer.id}"
                               style="color: #ffc107; text-decoration: none;">Редактировать</a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <div style="margin-top: 30px; text-align: center;">
        <a href="${pageContext.request.contextPath}/"
           style="color: #6c757d; text-decoration: none;">← Вернуться на главную</a>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>