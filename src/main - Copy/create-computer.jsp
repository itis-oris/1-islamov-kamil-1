<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/templates/fragments/header.jsp" %>
<div class="container" style="max-width: 800px; margin: 30px auto;">
    <h2 style="text-align: center;">Добавить игровой ПК</h2>
    <c:if test="${not empty errorMessage}">
        <div style="color: red; background: #ffe6e6; padding: 10px; margin-bottom: 20px; border-radius: 4px;">
            ${errorMessage}
        </div>
    </c:if>
    <form method="post" enctype="multipart/form-data" style="display: flex; flex-direction: column; gap: 15px;">
        <div>
            <label><strong>Название ПК:</strong></label>
            <input type="text" name="title" required style="width: 100%; padding: 8px;" value="${param.title}">
        </div>
        <div>
            <label><strong>Описание:</strong></label>
            <textarea name="description" rows="4" style="width: 100%; padding: 8px;">${param.description}</textarea>
        </div>
        <div style="position: relative;">
            <label><strong>Игровая зона:</strong></label>
            <input type="text"
                   id="gamingZoneInput"
                   placeholder="Начните вводить название зоны..."
                   style="width: 100%; padding: 8px; margin-top: 5px;"
                   autocomplete="off">
            <input type="hidden" name="gamingZoneId" id="gamingZoneId" required>
            <div id="gamingZoneSuggestions"
                 style="position: absolute; z-index: 1000; background: white; border: 1px solid #ccc;
                        width: 100%; max-height: 200px; overflow-y: auto; display: none;">
            </div>
        </div>
        <div style="display: flex; gap: 15px;">
            <div style="flex: 1;">
                <label><strong>Режим работы (с):</strong></label>
                <input type="time" name="startTime" required style="width: 100%; padding: 8px;" value="${param.startTime}">
            </div>
            <div style="flex: 1;">
                <label><strong>Режим работы (до):</strong></label>
                <input type="time" name="endTime" required style="width: 100%; padding: 8px;" value="${param.endTime}">
            </div>
        </div>
        <div>
            <label><strong>Цена за час (₽):</strong></label>
            <input type="number" step="0.01" name="hourlyRate" required min="0" style="width: 100%; padding: 8px;" value="${param.hourlyRate}">
        </div>
        <!-- === Конфигурация ПК === -->
        <div>
            <label><strong>Конфигурация ПК:</strong></label>
            <div style="display: flex; flex-wrap: wrap; gap: 12px; margin-top: 8px;">
                <c:forEach var="config" items="${allRoomOptions}">
                    <label style="display: flex; align-items: center; gap: 6px; background: #f8f9fa; padding: 10px; border-radius: 6px; border: 1px solid #e9ecef;">
                        <input type="checkbox" name="pcConfigurations" value="${config.id}"
                               <c:if test="${paramValues.pcConfigurations != null}">
                                   <c:forEach var="selectedId" items="${paramValues.pcConfigurations}">
                                       <c:if test="${selectedId == config.id}">checked</c:if>
                                   </c:forEach>
                               </c:if>>
                        <span>
                            <strong>${config.cpu} / ${config.gpu}</strong><br/>
                            <small>${config.ram} ГБ ОЗУ, ${config.storage} Хранилище</small>
                        </span>
                    </label>
                </c:forEach>
            </div>
            <small style="color: #6c757d;">Выберите компоненты конфигурации</small>
        </div>
        <!-- === Загрузка фото === -->
        <div>
            <label><strong>Фотографии ПК (макс. 5 файлов):</strong></label>
            <input type="file" name="photos" accept=".jpg,.jpeg,.png" multiple
                   style="width: 100%; padding: 8px;" />
            <small style="color: #6c757d;">
                Поддерживаются JPG, PNG. Первое загруженное фото станет обложкой (<code>home.jpg</code>).
            </small>
        </div>
        <div style="text-align: center; margin-top: 20px;">
            <button type="submit" style="padding: 10px 25px; background: #28a745; color: white; border: none; border-radius: 5px; cursor: pointer;">
                Добавить ПК
            </button>
            <a href="${pageContext.request.contextPath}/manager/profile"
               style="display: inline-block; margin-left: 15px; padding: 10px 15px; text-decoration: none; color: #6c757d; border: 1px solid #ccc; border-radius: 5px;">
                Отмена
            </a>
        </div>
    </form>
</div>
<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/gamingZone.js"></script>
<%@ include file="/templates/fragments/footer.jsp" %>