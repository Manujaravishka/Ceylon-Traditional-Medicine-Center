$(document).ready(function () {

    // Fetch and display accommodations
    function fetchAllAccommodations() {
        $.ajax({
            url: "http://localhost:8080/api/v1/accommodation/getAll",
            type: "GET",
            success: function (response) {
                if (response.code === 201) {
                    let list = response.data;
                    let html = "";
                    list.forEach(acc => {
                        const img = acc.imageUrl
                            ? `<img src="/uploads/${acc.imageUrl}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;">`
                            : "-";
                        html += `
                            <tr>
                                <td>${acc.id}</td>
                                <td>${acc.name}</td>
                                <td>${acc.description}</td>
                                <td>${acc.location}</td>
                                <td>${acc.category}</td>
                                <td>${acc.costPerDay}</td>
                                <td>${img}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-acc-btn"
                                        data-id="${acc.id}"
                                        data-name="${acc.name}"
                                        data-description="${acc.description}"
                                        data-location="${acc.location}"
                                        data-category="${acc.category}"
                                        data-cost="${acc.costPerDay}"
                                        data-image="/uploads/${acc.imageUrl}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editAccommodationModal">
                                        Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm delete-acc-btn" data-id="${acc.id}">Delete</button>
                                </td>
                            </tr>`;
                    });
                    $("#accommodationTableBody").html(html);
                } else {
                    $("#accommodationTableBody").html("<tr><td colspan='8'>No data found.</td></tr>");
                }
            },
            error: function () {
                $("#accommodationTableBody").html("<tr><td colspan='8'>Error loading data.</td></tr>");
            }
        });
    }
    fetchAllAccommodations();

    // Add accommodation
    $("#addAccommodationForm").submit(function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        if ($("#accommodationImage")[0].files[0]) {
            formData.append("imageUrl", $("#accommodationImage")[0].files[0]);
        }

        $.ajax({
            url: "http://localhost:8080/api/v1/accommodation/save",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Accommodation Added Successfully!");
                $("#addAccommodationModal").modal("hide");
                $("#addAccommodationForm")[0].reset();
                $("#addImgPreview").hide();
                fetchAllAccommodations();
            },
            error: function () {
                alert("Error adding accommodation.");
            }
        });
    });

    // Image preview add
    $("#accommodationImage").on("change", function (e) {
        let reader = new FileReader();
        reader.onload = function (ev) {
            $("#addImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(e.target.files[0]);
    });

    // Fill edit form
    $(document).on("click", ".edit-acc-btn", function () {
        $("#editAccommodationId").val($(this).data("id"));
        $("#editAccommodationName").val($(this).data("name"));
        $("#editAccommodationDescription").val($(this).data("description"));
        $("#editAccommodationLocation").val($(this).data("location"));
        $("#editAccommodationCategory").val($(this).data("category"));
        $("#editAccommodationCostPerDay").val($(this).data("cost"));
        let image = $(this).data("image");
        if (image) {
            $("#editImgPreview").attr("src", image).show();
        } else {
            $("#editImgPreview").hide();
        }
    });

    // Update accommodation
    $("#editAccommodationForm").submit(function (e) {
        e.preventDefault();
        let id = $("#editAccommodationId").val();

        let formData = new FormData(this); // This already includes all fields + file input

        $.ajax({
            url: `http://localhost:8080/api/v1/accommodation/update/${id}`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Accommodation Updated Successfully!");
                $("#editAccommodationModal").modal("hide");
                fetchAllAccommodations();
            },
            error: function () {
                alert("Error updating accommodation.");
            }
        });
    });


    // Delete accommodation
    $(document).on("click", ".delete-acc-btn", function () {
        let id = $(this).data("id");
        if (confirm("Are you sure you want to delete this accommodation?")) {
            $.ajax({
                url: `http://localhost:8080/api/v1/accommodation/delete/${id}`,
                type: "DELETE",
                success: function (res) {
                    alert(res.message);
                    fetchAllAccommodations();
                },
                error: function () {
                    alert("Error deleting accommodation.");
                }
            });
        }
    });

});
