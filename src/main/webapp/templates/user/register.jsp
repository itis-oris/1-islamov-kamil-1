<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ include file="/templates/fragments/header.jsp" %>

<div class="container" style="max-width: 500px; margin: 40px auto;">
    <h2 style="text-align: center;">Регистрация</h2>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div style="margin-bottom: 15px;">
            <label><strong>Имя:</strong></label><br/>
            <input type="text" name="name" required
                   value="${param.name}" style="width: 100%; padding: 8px;">
        </div>

        <div style="margin-bottom: 15px;">
            <label><strong>Email:</strong></label><br/>
            <input type="email" name="email" required
                   value="${param.email}" style="width: 100%; padding: 8px;">
        </div>

        <div style="margin-bottom: 15px;">
            <label><strong>Пароль:</strong></label><br/>
            <input type="password" name="password" required minlength="6"
                   style="width: 100%; padding: 8px;">
        </div>

        <div style="margin-bottom: 15px;">
            <label><strong>Повторите пароль:</strong></label><br/>
            <input type="password" name="confirmPassword" required minlength="6"
                   style="width: 100%; padding: 8px;">
        </div>

        <div style="margin-bottom: 20px;">
            <label><strong>Роль:</strong></label><br/>
            <select name="roleId" style="width: 100%; padding: 8px;">
                <option value="3">Пользователь</option>
                <option value="2">Менеджер</option>
            </select>
        </div>

        <button type="submit" style="width: 100%; padding: 10px; background: #28a745; color: white; border: none; border-radius: 5px; font-size: 16px;">
            Создать аккаунт
        </button>
    </form>

    <% if (request.getAttribute("error") != null) { %>
        <p style="color: red; margin-top: 15px; text-align: center;">
            <%= request.getAttribute("error") %>
        </p>
    <% } %>

    <div style="text-align: center; margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/login" style="color: #007bff; text-decoration: none;">
            ← Уже есть аккаунт? Войти
        </a>
    </div>
</div>

<%@ include file="/templates/fragments/footer.jsp" %>