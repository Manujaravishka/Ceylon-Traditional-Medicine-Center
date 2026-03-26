document.addEventListener("DOMContentLoaded", function () {
    const userDropdown = document.getElementById("userDropdown");
    const signInUpLink = document.getElementById("signInUpLink"); // Updated selector
    const userSpan = document.querySelector("#userDropdown span");
    const logoutBtn = document.getElementById("logoutBtn"); // Updated selector

    // Function to check user login status
    function checkUserLogin() {
        const name = localStorage.getItem("name");
        const token = localStorage.getItem("token");

        if (name && token) {
            // Set username in dropdown
            userSpan.textContent = `Hi, ${name}`;

            // Show dropdown and hide sign-in link
            userDropdown.classList.remove("d-none");
            signInUpLink.classList.add("d-none");
        } else {
            // Hide dropdown and show sign-in link
            userDropdown.classList.add("d-none");
            signInUpLink.classList.remove("d-none");
        }
    }

    // Logout function
    function logoutUser() {
        localStorage.removeItem("name");
        localStorage.removeItem("token");
        localStorage.removeItem("email");
        localStorage.removeItem("role");

        // Show sign-in link and hide dropdown
        userDropdown.classList.add("d-none");
        signInUpLink.classList.remove("d-none");

        // Redirect to index page
        window.location.href = "index.html";
    }

    // Attach logout event
    logoutBtn.addEventListener("click", function (event) {
        event.preventDefault();
        logoutUser();
    });

    // Run check when page loads
    checkUserLogin();
});

// Function to load pages
function loadPage(page) {
    window.location.href = page; // Redirects the browser to the selected page
}