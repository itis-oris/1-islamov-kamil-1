<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/templates/fragments/header.jsp" %>

<div class="container" style="max-width: 900px; margin: 30px auto;">
    <h2>Профиль пользователя</h2>

    <form method="post" action="${pageContext.request.contextPath}/profile">
        <label>Имя:</label><br>
        <input type="text" name="name" value="${user.name}" style="width: 100%;"><br><br>
        <label>Email (нельзя менять):</label><br>
        <input type="text" value="${user.email}" style="width: 100%;" disabled><br><br>
        <button type="submit">Сохранить изменения</button>
    </form>

    <hr>

    <h3>Мои бронирования</h3>
    <c:choose>
        <c:when test="${not empty reservations}">
            <table border="1" cellpadding="5" cellspacing="0" style="width:100%;">
                <tr>
                    <th>ID</th>
                    <th>Тур</th>
                    <th>Дата бронирования</th>
                    <th>Кол-во людей</th>
                    <th>Кол-во комнат</th>
                    <th>Цена</th>
                    <th>Статус</th>
                </tr>
                <c:forEach var="b" items="${reservations}">
                    <tr>
                        <td>${b.id}</td>
                        <td>${b.computer != null ? b.computer.title : '—'}</td>
                        <td>${b.reservationDate}</td>
                        <td>${b.peopleCount}</td>
                        <td>${b.roomsCount}</td>
                        <td>${b.totalPrice}</td>
                        <td>${b.status}</td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <p>У вас пока нет бронирований.</p>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/templates/fragments/footer.jsp" %>
