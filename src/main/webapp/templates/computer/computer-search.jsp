<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/templates/fragments/header.jsp" %>

<div class="container" style="max-width: 900px; margin: 30px auto;">
    <h2>Поиск игровых ПК</h2>

    <form id="searchForm" method="get" action="${pageContext.request.contextPath}/computers/search">
        <div style="position: relative; margin-bottom: 15px;">
            <label>Название ПК или игры:</label><br>
            <input type="text" id="query" name="query" style="width: 100%; padding: 8px;" autocomplete="off" placeholder="Например: RTX 3080, CS2, Cyberpunk">
            <div id="computerSuggestions"
                 style="position: absolute; z-index: 1000; background: white; border: 1px solid #ccc;
                        width: 100%; max-height: 200px; overflow-y: auto; display: none;">
            </div>
        </div>

        <div style="margin-top: 15px;">
            <label>Режим работы:</label><br>
            <input type="date" name="startDate" style="width: 100%; padding: 8px;">
        </div>

        <div style="position: relative; margin-top: 15px;">
            <label><strong>Игровая зона:</strong></label>
            <input type="text"
                   id="gamingZoneInput"
                   placeholder="Начните вводить название зоны..."
                   style="width: 100%; padding: 8px; margin-top: 5px;"
                   autocomplete="off">
            <input type="hidden" name="gamingZoneId" id="gamingZoneId">
            <div id="gamingZoneSuggestions"
                 style="position: absolute; z-index: 1000; background: white; border: 1px solid #ccc;
                        width: 100%; max-height: 200px; overflow-y: auto; display: none;">
            </div>
        </div>

        <button type="submit" style="margin-top: 20px; padding: 10px 20px; background: #007bff; color: white; border: none; border-radius: 4px;">
            Найти ПК
        </button>
    </form>

    <hr>

    <div id="results">
        <%-- AJAX вставит сюда результаты --%>
    </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
</script>
<script src="${pageContext.request.contextPath}/js/gamingZone.js"></script>
<script src="${pageContext.request.contextPath}/js/computer-search.js"></script>

<%@ include file="/templates/fragments/footer.jsp" %>