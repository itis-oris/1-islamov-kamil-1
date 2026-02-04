<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>ComputerList</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
</head>
<body>
<header style="background:#007bff; padding:15px; color:white; text-align:center;">
    <h2>PCGamer — бронируй игровые ПК за пару кликов!</h2>
    <nav style="margin-top:10px;">
        <a href="${pageContext.request.contextPath}/index" style="color:white; margin:0 10px;">Главная</a>
        <a href="${pageContext.request.contextPath}/computers/search" style="color:white; margin:0 10px;">Туры</a>

        <!-- Проверка, залогинен ли пользователь -->
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <!-- Проверяем роль -->
                <c:choose>
                    <c:when test="${sessionScope.user.roleId == 2}">
                        <a href="${pageContext.request.contextPath}/admin/profile" style="color:white; margin:0 10px;">
                            Профиль менеджера
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/profile" style="color:white; margin:0 10px;">
                            Профиль
                        </a>
                    </c:otherwise>
                </c:choose>

                <a href="${pageContext.request.contextPath}/logout" style="color:white; margin:0 10px;">
                    Выйти
                </a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login" style="color:white; margin:0 10px;">
                    Войти
                </a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
<main>
