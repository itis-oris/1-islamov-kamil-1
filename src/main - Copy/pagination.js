
function loadPage(page) {
    const form = document.getElementById("searchForm");
    const formData = new FormData(form);
    formData.set("page", page); // передаём номер страницы
    const params = new URLSearchParams(formData).toString();
    fetch(form.action + "?" + params + "&ajax=1")
        .then(res => res.text())
        .then(html => document.getElementById("results").innerHTML = html)
        .catch(err => console.error(err));
}
