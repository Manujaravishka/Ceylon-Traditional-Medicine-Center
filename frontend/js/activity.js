$(document).ready(function () {

    // Fetch and display activities
    function fetchAllActivities() {
        $.ajax({
            url: "http://localhost:8080/api/v1/activity/getAll",
            type: "GET",
            success: function (response) {
                if (response.code === 201) {
                    let list = response.data;
                    let html = "";
                    list.forEach(act => {
                        const img = act.imageUrl
                            ? `<img src="/uploads/${act.imageUrl}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;">`
                            : "-";
                        html += `
                            <tr>
                                <td>${act.id}</td>
                                <td>${act.name}</td>
                                <td>${act.description}</td>
                                <td>${act.costPerDay}</td>
                                <td>${img}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-act-btn"
                                        data-id="${act.id}"
                                        data-name="${act.name}"
                                        data-description="${act.description}"
                                        data-cost="${act.costPerDay}"
                                        data-image="/uploads/${act.imageUrl}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editActivityModal">
                                        Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm delete-act-btn" data-id="${act.id}">Delete</button>
                                </td>
                            </tr>`;
                    });
                    $("#activityTableBody").html(html);
                } else {
                    $("#activityTableBody").html("<tr><td colspan='6'>No data found.</td></tr>");
                }
            },
            error: function () {
                $("#activityTableBody").html("<tr><td colspan='6'>Error loading data.</td></tr>");
            }
        });
    }
    fetchAllActivities();

    // Add activity
    $("#addActivityForm").submit(function (e) {
        e.preventDefault();
        let formData = new FormData(this);

        $.ajax({
            url: "http://localhost:8080/api/v1/activity/save",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Activity Added Successfully!");
                $("#addActivityModal").modal("hide");
                $("#addActivityForm")[0].reset();
                $("#addActivityImgPreview").hide();
                fetchAllActivities();
            },
            error: function () {
                alert("Error adding activity.");
            }
        });
    });

    // Image preview add
    $("#activityImage").on("change", function (e) {
        let reader = new FileReader();
        reader.onload = function (ev) {
            $("#addActivityImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(e.target.files[0]);
    });

    // Fill edit form
    $(document).on("click", ".edit-act-btn", function () {
        $("#editActivityId").val($(this).data("id"));
        $("#editActivityName").val($(this).data("name"));
        $("#editActivityDescription").val($(this).data("description"));
        $("#editActivityCost").val($(this).data("cost"));

        let image = $(this).data("image");
        if (image && image !== "/uploads/undefined") {
            $("#editActivityImgPreview").attr("src", image).show();
        } else {
            $("#editActivityImgPreview").hide();
        }
    });

    // Update activity
    $("#editActivityForm").submit(function (e) {
        e.preventDefault();
        let id = $("#editActivityId").val();
        let formData = new FormData(this);

        $.ajax({
            url: `http://localhost:8080/api/v1/activity/update/${id}`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Activity Updated Successfully!");
                $("#editActivityModal").modal("hide");
                fetchAllActivities();
            },
            error: function () {
                alert("Error updating activity.");
            }
        });
    });

    // Delete activity
    $(document).on("click", ".delete-act-btn", function () {
        let id = $(this).data("id");
        if (confirm("Are you sure you want to delete this activity?")) {
            $.ajax({
                url: `http://localhost:8080/api/v1/activity/delete/${id}`,
                type: "DELETE",
                success: function (res) {
                    alert(res.message);
                    fetchAllActivities();
                },
                error: function () {
                    alert("Error deleting activity.");
                }
            });
        }
    });

});
