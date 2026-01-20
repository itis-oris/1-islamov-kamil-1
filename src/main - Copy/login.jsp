<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ include file="/templates/fragments/header.jsp" %>

<div class="container" style="max-width: 500px; margin: 60px auto; text-align: center;">
    <h2>Вход</h2>

    <form action="${pageContext.request.contextPath}/login" method="post" style="text-align: left;">
        <div style="margin-bottom: 15px;">
            <label><strong>Email:</strong></label><br>
            <input type="email" name="email" required style="width: 100%; padding: 8px;">
        </div>

        <div style="margin-bottom: 15px;">
            <label><strong>Пароль:</strong></label><br>
            <input type="password" name="password" required style="width: 100%; padding: 8px;">
        </div>

        <button type="submit" style="width: 100%; padding: 10px; background: #007bff; color: white; border: none; border-radius: 5px; font-size: 16px;">
            Войти
        </button>
    </form>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red; margin-top: 15px;">
            <%= request.getAttribute("error") %>
        </p>
    <% } %>

    <div style="margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/register" style="color: #28a745; text-decoration: none;">
            Регистрация
        </a>
    </div>
</div>

<%@ include file="/templates/fragments/footer.jsp" %>