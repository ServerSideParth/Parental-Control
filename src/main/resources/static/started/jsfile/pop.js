function showpop() {
    var popup = document.getElementById("popup");
    if (popup) {
        popup.classList.add("show");
        setTimeout(function () {
            popup.classList.remove("show");
        }, 4000);
    } else {
        console.error("Element with ID 'popup' not found.");
    }
}