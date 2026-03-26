// $("#showPassword").on("change", function() {
//     const pwd = $("#password");
//     if (this.checked) {
//         pwd.attr("type", "text");
//     } else {
//         pwd.attr("type", "password");
//     }
// });
//
// $("#signupForm").on("submit", function(e) {
//     e.preventDefault();
//     const username = $("#username").val().trim();
//     const password = $("#password").val();
//     const role = $("#role").val();
//
//     if (!username || !password || !role) {
//         alert("Please fill in all fields.");
//         return;
//     }
//
//     $.ajax({
//         url: "http://localhost:8080/auth/register",
//         type: "POST",
//         contentType: "application/json",
//         data: JSON.stringify({ username, password, role }),
//         success: function(response) {
//             alert(response.message || "Registered successfully!");
//             window.location.href = "signin.html";
//         },
//         error: function(xhr) {
//             const errorMsg = xhr.responseJSON?.message || "Registration failed.";
//             alert(errorMsg);
//         }
//     });
// });
// Show / Hide password
// $("#showPassword").on("change", function() {
//     const pwd = $("#password");
//     if (this.checked) {
//         pwd.attr("type", "text");
//     } else {
//         pwd.attr("type", "password");
//     }
// });
//
// // Handle Sign Up form submit
// $("#signupForm").on("submit", function(e) {
//     e.preventDefault();
//
//     const username = $("#username").val().trim();
//     const email = $("#email").val().trim();   // ✅ new email field
//     const password = $("#password").val();
//     const role = $("#role").val();
//
//     if (!username || !email || !password || !role) {
//         alert("Please fill in all fields.");
//         return;
//     }
//
//     $.ajax({
//         url: "http://localhost:8080/auth/register",
//         type: "POST",
//         contentType: "application/json",
//         data: JSON.stringify({ username, email, password, role }), // ✅ send email
//         success: function(response) {
//             alert(response.message || "Registered successfully!");
//             window.location.href = "signin.html";
//         },
//         error: function(xhr) {
//             const errorMsg = xhr.responseJSON?.message || "Registration failed.";
//             alert(errorMsg);
//         }
//     });
// });





//
// const container = document.querySelector('.container');
// const registerBtn = document.querySelector('.register-btn');
// const loginBtn = document.querySelector('.login-btn');
//
// registerBtn.addEventListener('click', () => {
//     container.classList.add('active');
// });
//
// loginBtn.addEventListener('click', () => {
//     container.classList.remove('active');
// });
//
// const API_BASE = "http://localhost:8080/api/v1";
// document.addEventListener("DOMContentLoaded", function () {
//     const phoneInputField = document.querySelector("#contact");
//     const phoneError = document.querySelector("#phone-error");
//     const signupBtn = document.querySelector("#signupBtn");
//
//     // Initialize intl-tel-input
//     const iti = window.intlTelInput(phoneInputField, {
//         initialCountry: "lk", // Default country (Sri Lanka)
//         preferredCountries: ["lk", "us", "gb", "in", "au"],
//         separateDialCode: true,
//         utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/17.0.8/js/utils.js",
//     });
//
//     // Function to validate phone number in real-time
//     function validatePhoneNumber() {
//         const phoneNumber = phoneInputField.value.trim();
//         const phoneRegex = /^07\d{8}$/; // Sri Lanka format
//
//         if (phoneNumber !== "" && (!iti.isValidNumber() || !phoneRegex.test(phoneNumber))) {
//             phoneError.style.display = "block";
//             phoneError.textContent = "Invalid phone number format!";
//             phoneInputField.classList.add("invalid");
//             signupBtn.disabled = true;
//         } else {
//             phoneError.style.display = "none";
//             phoneInputField.classList.remove("invalid");
//             signupBtn.disabled = false;
//         }
//     }
//
//     phoneInputField.addEventListener("input", validatePhoneNumber);
//
//     document.querySelector("form").addEventListener("submit", function (event) {
//         const phoneNumber = phoneInputField.value.trim();
//         if (!iti.isValidNumber() || !/^07\d{8}$/.test(phoneNumber)) {
//             event.preventDefault();
//             phoneError.style.display = "block";
//             phoneError.textContent = "Please enter a valid phone number!";
//         }
//     });
//
//     // ✅ Signup
//     $("#signupBtn").click(function (e) {
//         e.preventDefault();
//
//         let name = $("#name").val();
//         let email = $("#signupEmail").val();
//         let contact = iti.getNumber();
//         let password = $("#signupPassword").val();
//
//         $("#signupBtn").prop("disabled", true).text("Registering...");
//
//         $.ajax({
//             url: `${API_BASE}/user/register`,
//             type: "POST",
//             contentType: "application/json",
//             data: JSON.stringify({ name, email, contact, password }),
//             success: function () {
//                 alert("✅ Signup Successful!");
//                 window.location.reload();
//             },
//             error: function (xhr) {
//                 let errorMessage = xhr.responseJSON?.message || "Unknown error";
//                 alert("❌ Signup Failed: " + errorMessage);
//             },
//             complete: function () {
//                 $("#signupBtn").prop("disabled", false).text("Register");
//             }
//         });
//     });
//
//     // ✅ Login
//     $("#loginBtn").click(function (e) {
//         e.preventDefault();
//
//         let email = $("#loginEmail").val();
//         let password = $("#loginPassword").val();
//
//         $.ajax({
//             url: `${API_BASE}/auth/authentication`,
//             type: "POST",
//             contentType: "application/json",
//             data: JSON.stringify({ email, password }),
//             success: function (response) {
//                 localStorage.setItem("token", response.data.token);
//                 checkUserRole();
//                 getUsername();
//             },
//             error: function (xhr) {
//                 alert("Login Failed: " + xhr.responseJSON.message);
//             }
//         });
//     });
//
//     function checkUserRole() {
//         let token = localStorage.getItem("token");
//         if (!token) {
//             showDashboard("Unauthorized: Please log in.");
//             return;
//         }
//
//         function checkRole(url, successCallback, failureCallback) {
//             $.ajax({
//                 url: `${API_BASE}/admin/${url}`,
//                 type: "GET",
//                 headers: { "Authorization": `Bearer ${token}` },
//                 success: successCallback,
//                 error: function (jqXHR) {
//                     if (jqXHR.status === 403) {
//                         console.log(`Access Denied for ${url}`);
//                         if (failureCallback) failureCallback(jqXHR);
//                     } else {
//                         console.error(`Error checking role at ${url}:`, jqXHR);
//                         if (failureCallback) failureCallback(jqXHR);
//                     }
//                 }
//             });
//         }
//
//         // ✅ Only Admin + User checks remain
//         checkRole("admin",
//             function () {
//                 console.log("hi admin");
//                 window.location.href = 'adminDashboard.html';
//             },
//             function () {
//                 console.log("Not admin → checking user role...");
//                 window.location.href = 'index.html';
//
//                 checkRole("user",
//                     function () {
//                         console.log("hi user");
//                     },
//                     function () {
//                         showDashboard("Unauthorized: Access denied.");
//                     }
//                 );
//             }
//         );
//     }
//
//     function getUsername() {
//         const email = $('#loginEmail').val().trim();
//         const token = localStorage.getItem("token");
//
//         $.ajax({
//             url: `http://localhost:8080/api/v1/user/getUserByEmail?email=${encodeURIComponent(email)}`,
//             method: 'GET',
//             contentType: 'application/json',
//             headers: {
//                 'Authorization': 'Bearer ' + token
//             },
//             success: function (response) {
//                 if (response.data) {
//                     saveToLocalStorage("name", response.data.name, 5);
//                     saveToLocalStorage("email", response.data.email, 5);
//                     saveToLocalStorage("role", response.data.role, 5);
//                     console.log("User retrieved successfully!");
//                 } else {
//                     alert("User not found.");
//                 }
//             },
//             error: function (error) {
//                 console.error('Error:', error);
//                 alert(error.responseJSON?.message || 'Failed to get user details.');
//             }
//         });
//     }
//
//     function saveToLocalStorage(key, value, days) {
//         const now = new Date();
//         const expiryTime = now.getTime() + days * 24 * 60 * 60 * 1000;
//         const data = { value: value, expiry: expiryTime };
//         localStorage.setItem(key, JSON.stringify(data));
//     }
// });

