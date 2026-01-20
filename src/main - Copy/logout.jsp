<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ include file="/templates/fragments/header.jsp" %>

<div class="container" style="max-width: 600px; margin: 50px auto; text-align: center;">
    <h2>Вы действительно хотите выйти?</h2>

    <form method="post" action="${pageContext.request.contextPath}/logout">
        <button type="submit" style="padding: 10px 20px; font-size: 16px; cursor: pointer;">
            Выйти
        </button>
        <a href="${pageContext.request.contextPath}/index" style="margin-left:20px; font-size:16px; text-decoration:none;">
            Отмена
        </a>
    </form>
</div>

<%@ include file="/templates/fragments/footer.jsp" %>
