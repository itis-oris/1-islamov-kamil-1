<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="../fragments/header.jsp" %>

<div class="container" style="max-width: 900px; margin: 30px auto;">
    <c:if test="${not empty errorMessage}">
        <div style="color: red; background: #ffe6e6; padding: 12px; margin-bottom: 20px; border-radius: 5px;">
            ${errorMessage}
        </div>
    </c:if>

    <h1>${computer.title}</h1>

    <p><strong>Направление:</strong> ${computer.gamingZone.name}</p>
    <p><strong>Даты:</strong> ${computer.startDate} – ${computer.endDate}</p>
    <p><strong>Цена от:</strong> ${computer.hourlyRate} ₽</p>

    <h2>Описание</h2>
    <p style="white-space: pre-wrap;">${computer.description}</p>

    <!-- Фотографии тура -->
    <div style="margin: 20px 0; display: flex; flex-wrap: wrap; gap: 12px; align-items: flex-start;">
        <c:forEach var="fileName" items="${photoFileNames}">
            <img src="${pageContext.request.contextPath}/img/computer/${computer.id}/${fileName}"
                 alt="${fn:escapeXml(fileName)}"
                 onerror="this.style.display='none'"
                 style="max-width: 300px; max-height: 300px; object-fit: cover; border: 1px solid #eee; border-radius: 4px;">
        </c:forEach>
    </div>

    <!-- Варианты размещения -->
    <h2>Варианты размещения</h2>
    <c:choose>
        <c:when test="${empty roomOptions}">
            <p>Информация о номерах временно недоступна.</p>
        </c:when>
        <c:otherwise>
            <div style="display: flex; flex-direction: column; gap: 15px;">
                <c:forEach var="room" items="${roomOptions}">
                    <div style="padding: 12px; border: 1px solid #ddd; border-radius: 6px; background: #f8f9fa;">
                        <strong>${room.roomType}</strong><br/>
                        До ${room.capacity} гостей,
                        ${room.bed_count} ${room.bed_type} кровать(и),
                        Коэффициент цены: ${room.priceMultiplier}
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

    <!-- Кнопка редактирования (только для автора) -->
    <c:if test="${not empty sessionScope.user && sessionScope.user.id == computer.creator_id}">
        <div style="margin: 25px 0; text-align: center;">
            <a href="${pageContext.request.contextPath}/admin/edit-computer?id=${computer.id}"
               style="padding: 10px 20px; background-color: #ffc107; color: #212529; text-decoration: none; border-radius: 5px; font-weight: bold;">
                Редактировать тур
            </a>
        </div>
    </c:if>

    <!-- Форма бронирования -->
    <h2>Забронировать тур</h2>
    <form method="post" style="background: #f1f3f5; padding: 20px; border-radius: 8px;">
        <input type="hidden" name="computerId" value="${computer.id}" />

        <div style="margin-bottom: 15px;">
            <label><strong>Количество людей:</strong></label><br/>
            <input type="number" name="peopleCount" min="1" value="1" required
                   style="padding: 8px; width: 100%; max-width: 200px;" />
        </div>

        <div style="margin-bottom: 15px;">
            <label><strong>Количество комнат:</strong></label><br/>
            <input type="number" name="roomsCount" min="1" value="1" required
                   style="padding: 8px; width: 100%; max-width: 200px;" />
        </div>

        <button type="submit"
                style="padding: 10px 25px; background-color: #28a745; color: white; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;">
            Забронировать
        </button>
    </form>

    <div style="margin-top: 30px; text-align: center;">
        <a href="${pageContext.request.contextPath}/computers" style="color: #6c757d; text-decoration: none;">← Все туры</a>
    </div>
</div>

<%@ include file="../fragments/footer.jsp" %>