(function () {
    emailjs.init("cUuWJLrEenUiJm0d0"); // Your EmailJS Public Key
})();

const contactForm = document.getElementById("contactForm");

contactForm.addEventListener("submit", function (e) {
    e.preventDefault();

    if (!contactForm.checkValidity()) {
        contactForm.classList.add("was-validated");
        return;
    }

    const serviceID = "service_pcroz7e";   // Your EmailJS Service ID
    const templateID = "template_0dv6u7i"; // Your EmailJS Template ID

    emailjs.sendForm(serviceID, templateID, contactForm)
        .then(() => {
            alert("✅ Message sent successfully! Check your inbox.");
            contactForm.reset();
            contactForm.classList.remove("was-validated");
        }, (err) => {
            alert("❌ Failed to send message: " + JSON.stringify(err));
        });
});

