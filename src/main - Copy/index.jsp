<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ include file="fragments/header.jsp" %>

<div class="container" style="max-width: 900px; margin: 50px auto; text-align: center;">
    <h1>Добро пожаловать в ComputerList</h1>
    <p>Выбирайте мощные ПК для игр и бронируйте время онлайн!</p>

    <a href="${pageContext.request.contextPath}/computers"
       style="display:inline-block; margin-top: 20px; padding:10px 20px; background:#007bff; color:white; border-radius:5px; text-decoration:none;">
       Выбрать ПК
    </a>
</div>

<%@ include file="fragments/footer.jsp" %>

