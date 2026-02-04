<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${not empty computers}">
    <div class="computers-grid" style="display: flex; flex-direction: column; gap: 25px; margin: 20px 0;">
        <c:forEach var="computer" items="${computers}">
            <div style="border: 1px solid #e0e0e0; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 6px rgba(0,0,0,0.1); background: white;">
                <!-- Фото (попытка загрузить photo1.jpg) -->
                <div style="height: 200px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; overflow: hidden;">
                    <img src="${pageContext.request.contextPath}/img/computer/${computer.id}/home.jpg"
                         onerror="this.style.display='none'"
                         style="width: 100%; height: 100%; object-fit: cover;"
                         alt="${computer.title}">
                </div>

                <div style="padding: 20px;">
                    <h2 style="margin-top: 0; color: #333;">${computer.title}</h2>

                    <div style="display: flex; flex-wrap: wrap; gap: 15px; margin-bottom: 15px; font-size: 14px; color: #555;">
                        <span>📍 ${computer.gamingZone.name}</span>
                        <span>📅 ${computer.startDate} – ${computer.endDate}</span>
                        <span>💰 От ${computer.hourlyRate} ₽</span>
                    </div>

                    <!-- Описание (только первые 3 слова) -->
                    <div style="margin: 15px 0; color: #333; line-height: 1.5;">
                        <c:choose>
                            <c:when test="${not empty computer.description}">
                                <c:set var="words" value="${fn:split(computer.description, ' ')}"/>
                                <c:choose>
                                    <c:when test="${fn:length(words) > 3}">
                                        <p style="margin: 0;">
                                            ${words[0]} ${words[1]} ${words[2]}...
                                        </p>
                                    </c:when>
                                    <c:otherwise>
                                        <p style="margin: 0;">${computer.description}</p>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <p style="margin: 0; color: #888;">Описание отсутствует.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Кнопка -->
                    <div style="text-align: right; margin-top: 15px;">
                        <a href="${pageContext.request.contextPath}/computers?id=${computer.id}"
                           style="padding: 8px 16px; background: #007bff; color: white; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Подробнее →
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</c:if>

<c:if test="${empty computers}">
    <div style="text-align: center; padding: 40px; color: #666; font-size: 18px;">
        <p>🕗 Туры не найдены. Попробуйте изменить параметры поиска.</p>
    </div>
</c:if>

<!-- Пагинация -->
<c:if test="${totalPages > 1}">
    <div style="margin: 30px 0; display: flex; justify-content: center; align-items: center; gap: 12px; flex-wrap: wrap;">
        <c:if test="${currentPage > 1}">
            <a href="#" class="pagination-link" data-page="${currentPage - 1}"
               style="padding: 8px 14px; text-decoration: none; background: #f1f3f5; color: #007bff; border-radius: 5px;">← Назад</a>
        </c:if>

        <span style="min-width: 120px; text-align: center; font-weight: 500;">
            Страница ${currentPage} из ${totalPages}
        </span>

        <c:if test="${currentPage < totalPages}">
            <a href="#" class="pagination-link" data-page="${currentPage + 1}"
               style="padding: 8px 14px; text-decoration: none; background: #f1f3f5; color: #007bff; border-radius: 5px;">Вперёд →</a>
        </c:if>
    </div>
</c:if>