
$(document).ready(function () {
    // ✅ Fetch and display all doctors
    function fetchAllDoctors() {
        $.ajax({
            url: "http://localhost:8080/api/v1/doctor/getAll",
            type: "GET",
            success: function (response) {
                if (response.code === 201) {
                    let doctors = response.data;
                    let doctorHTML = "";
                    doctors.forEach(doctor => {
                        const img = doctor.imageUrl
                            ? `<img src="/uploads/${doctor.imageUrl}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;">`
                            : "-";
                        doctorHTML += `
                            <tr>
                                <td>${doctor.fullName}</td>
                                <td>${doctor.email}</td>
                                <td>${img}</td>
                                <td>${doctor.description}</td>
                                <td>${doctor.paymentPerDay}</td>
                                <td><a href="${doctor.linkedin}" target="_blank">LinkedIn</a></td>
                                <td>${doctor.booked}</td>
                                <td>${doctor.status}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-doctor-btn"
                                        data-email="${doctor.email}"
                                        data-name="${doctor.fullName}"
                                        data-description="${doctor.description}"
                                        data-payment="${doctor.paymentPerDay}"
                                        data-linkedin="${doctor.linkedin}"
                                        data-image="/uploads/${doctor.imageUrl}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editDoctorModal">
                                        Edit
                                    </button>
                                    <button class="btn btn-success btn-sm activate-doctor-btn" data-email="${doctor.email}">Activate</button>
                                    <button class="btn btn-danger btn-sm deactivate-doctor-btn" data-email="${doctor.email}">Deactivate</button>
                                    <button class="btn btn-outline-danger btn-sm delete-doctor-btn" data-email="${doctor.email}">Delete</button>
                                </td>
                            </tr>`;
                    });
                    $("#doctorTableBody").html(doctorHTML);
                } else {
                    $("#doctorTableBody").html("<tr><td colspan='9'>No doctors found.</td></tr>");
                }
            },
            error: function () {
                $("#doctorTableBody").html("<tr><td colspan='9'>Error loading doctors.</td></tr>");
            }
        });
    }
    fetchAllDoctors();

    // ✅ Add Doctor
    $("#addDoctorForm").submit(function (event) {
        event.preventDefault();
        let formData = new FormData();
        formData.append("fullName", $("#doctorName").val());
        formData.append("email", $("#doctorEmail").val());
        formData.append("description", $("#doctorDescription").val());
        formData.append("paymentPerDay", $("#doctorPayment").val());
        formData.append("linkedin", $("#doctorLinkedIn").val());
        if ($("#doctorImage")[0].files[0]) {
            formData.append("imageUrl", $("#doctorImage")[0].files[0]);
        }

        $.ajax({
            url: "http://localhost:8080/api/v1/doctor/save",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Doctor Added Successfully!");
                $("#addDoctorModal").modal("hide");
                $("#addDoctorForm")[0].reset();
                $("#addImagePreview").hide();
                fetchAllDoctors();
            },
            error: function () {
                alert("Error adding doctor.");
            }
        });
    });

    // ✅ Image Preview Add
    $("#doctorImage").on("change", function (event) {
        let reader = new FileReader();
        reader.onload = function (e) {
            $("#addImagePreview").attr("src", e.target.result).show();
        };
        reader.readAsDataURL(event.target.files[0]);
    });

    // ✅ Edit Doctor - Fill Modal
    $(document).on("click", ".edit-doctor-btn", function () {
        $("#editDoctorEmail").val($(this).data("email"));
        $("#editDoctorName").val($(this).data("name"));
        $("#editDoctorDescription").val($(this).data("description"));
        $("#editDoctorPayment").val($(this).data("payment"));
        $("#editDoctorLinkedIn").val($(this).data("linkedin"));
        let image = $(this).data("image");
        if (image) {
            $("#editImagePreview").attr("src", image).show();
        } else {
            $("#editImagePreview").hide();
        }
    });

    // ✅ Update Doctor
    $("#editDoctorForm").on("submit", function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        let email = $("#editDoctorEmail").val();

        if ($("#editDoctorImage")[0].files[0]) {
            formData.append("editDoctorImage", $("#editDoctorImage")[0].files[0]);
        }

        $.ajax({
            url: `http://localhost:8080/api/v1/doctor/update/${email}`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Doctor updated successfully!");
                $("#editDoctorModal").modal("hide");
                fetchAllDoctors();
            },
            error: function () {
                alert("Error updating doctor.");
            }
        });
    });

    // ✅ Activate / Deactivate Doctor
    $(document).on("click", ".activate-doctor-btn, .deactivate-doctor-btn", function () {
        let email = $(this).data("email");
        let action = $(this).hasClass("activate-doctor-btn") ? "active" : "deactivate";
        $.ajax({
            url: `http://localhost:8080/api/v1/doctor/${action}/${email}`,
            type: "PUT",
            success: function (response) {
                alert(response.message);
                fetchAllDoctors();
            },
            error: function () {
                alert("Error updating doctor status.");
            }
        });
    });

    // ✅ Delete Doctor
    $(document).on("click", ".delete-doctor-btn", function () {
        let email = $(this).data("email");
        if (confirm("Are you sure you want to delete this doctor?")) {
            $.ajax({
                url: `http://localhost:8080/api/v1/doctor/delete/${email}`,
                type: "DELETE",
                success: function (response) {
                    alert(response.message);
                    fetchAllDoctors();
                },
                error: function () {
                    alert("Error deleting doctor.");
                }
            });
        }
    });
});
