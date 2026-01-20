document.addEventListener("DOMContentLoaded", () => {
    const queryInput = document.getElementById("query");
    const suggestions = document.getElementById("computerSuggestions");
    const searchForm = document.getElementById("searchForm");
    const resultsDiv = document.getElementById("results");

    // --- AJAX автоподсказки ---
    queryInput.addEventListener("input", function () {
        const query = this.value.trim();
        if (query.length < 2) {
            suggestions.style.display = "none";
            return;
        }

        const url = window.location.origin + "/computers/suggest?query=" + encodeURIComponent(query);

        fetch(url)
            .then(res => res.json())
            .then(data => {
                suggestions.innerHTML = "";
                data.forEach(title => {
                    const div = document.createElement("div");
                    div.textContent = title;
                    div.style.padding = "5px";
                    div.style.cursor = "pointer";
                    div.onclick = () => {
                        queryInput.value = title;
                        suggestions.style.display = "none";
                    };
                    suggestions.appendChild(div);
                });
                suggestions.style.display = data.length ? "block" : "none";
            })
            .catch(err => console.error("Ошибка автоподсказки:", err));
    });

    // --- AJAX поиск ---
    searchForm.addEventListener("submit", function (e) {
        e.preventDefault();
        loadResults(1); // ← вызываем функцию
    });
});

document.addEventListener("click", function (e) {
    if (e.target.classList.contains("page-btn")) {
        const page = e.target.dataset.page;
        loadResults(page);
    }
});




function loadResults(page = 1) {
    const params = new URLSearchParams(new FormData(document.getElementById("searchForm")));
    params.append("ajax", "1");
    params.append("page", page);

    fetch(contextPath + "/computers/search?" + params.toString())
        .then(r => r.text())
        .then(html => document.querySelector("#results").innerHTML = html);
}

