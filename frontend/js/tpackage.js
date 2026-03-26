$(document).ready(function () {

    // ✅ Fetch and display all packages
    function fetchAllPackages() {
        $.ajax({
            url: "http://localhost:8080/api/v1/package/getAll",
            type: "GET",
            success: function (response) {
                if (response.code === 201) {
                    let packages = response.data;
                    let packageHTML = "";
                    packages.forEach(pkg => {
                        const activities = pkg.activities ? pkg.activities.join(", ") : "-";
                        const img = pkg.imageUrl
                            ? `<img src="/uploads/${pkg.imageUrl}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;" onerror="this.src='https://via.placeholder.com/70'; this.onerror=null;">`
                            : "-";
                        packageHTML += `
                            <tr>
                                <td>${pkg.id}</td>
                                <td>${pkg.name}</td>
                                <td>${pkg.estimateDays}</td>
                                <td>${pkg.price}</td>
                                <td>${activities}</td>
                                <td>${img}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-package-btn"
                                        data-id="${pkg.id}"
                                        data-name="${pkg.name}"
                                        data-price="${pkg.price}"
                                        data-days="${pkg.estimateDays}"
                                        data-activities='${JSON.stringify(pkg.activities)}'
                                        data-image="${pkg.imageUrl ? '/uploads/' + pkg.imageUrl : ''}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editPackageModal">
                                        Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm delete-package-btn" data-id="${pkg.id}">Delete</button>
                                </td>
                            </tr>`;
                    });
                    $("#packageTableBody").html(packageHTML);
                } else {
                    $("#packageTableBody").html("<tr><td colspan='7'>No packages found.</td></tr>");
                }
            },
            error: function () {
                $("#packageTableBody").html("<tr><td colspan='7'>Error loading packages.</td></tr>");
            }
        });
    }

    fetchAllPackages();

    // ✅ Load activities into select (with pre-selection for edit)
    function loadActivities(selectId, selected = []) {
        $.ajax({
            url: "http://localhost:8080/api/v1/activity/getAll",
            type: "GET",
            success: function (response) {
                const select = $(`#${selectId}`);
                select.empty();
                if (response.code === 201 && response.data) {
                    response.data.forEach(activity => {
                        const isSelected = selected.includes(activity.name) ? "selected" : "";
                        select.append(`<option value="${activity.id}" ${isSelected}>${activity.name}</option>`);
                    });
                }
            }
        });
    }

    loadActivities("packageActivities"); // Load activities for add form

    // ✅ Add Package
    $("#addPackageForm").submit(function (e) {
        e.preventDefault();

        // Collect selected activity IDs manually
        let selectedActivities = $("#packageActivities").val(); // array of selected IDs
        let formData = new FormData(this);

        // Replace original activity field with array of IDs
        formData.delete("activity"); // remove any auto-added
        selectedActivities.forEach(id => {
            formData.append("activity", id);
        });

        $.ajax({
            url: "http://localhost:8080/api/v1/package/save",
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Package added successfully!");
                $("#addPackageModal").modal("hide");
                $("#addPackageForm")[0].reset();
                $("#PackageImgPreview").hide();
                fetchAllPackages();
            },
            error: function () {
                alert("Error adding package.");
            }
        });
    });

    // ✅ Image Preview Add
    $("#packageImage").on("change", function () {
        let file = this.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $("#PackageImgPreview").attr("src", e.target.result).show();
            };
            reader.readAsDataURL(file);
        } else {
            $("#PackageImgPreview").hide();
        }
    });

    // ✅ Edit Package - Fill Modal
    $(document).on("click", ".edit-package-btn", function () {
        $("#editPackageId").val($(this).data("id"));
        $("#editPackageName").val($(this).data("name"));
        $("#editPackagePrice").val($(this).data("price"));
        $("#editPackageDays").val($(this).data("days"));

        // Load activities with pre-selection
        const selectedActivities = $(this).data("activities") || [];
        loadActivities("editPackageActivities", selectedActivities);

        let image = $(this).data("image");
        if (image) {
            $("#editPackageImgPreview").attr("src", image).show();
        } else {
            $("#editPackageImgPreview").hide();
        }
    });

    // ✅ Update Package
    $("#editPackageForm").submit(function (e) {
        e.preventDefault();

        let id = $("#editPackageId").val();
        let selectedActivities = $("#editPackageActivities").val(); // array of IDs
        let formData = new FormData(this);

        // Replace original activity field with array of IDs
        formData.delete("activity");
        selectedActivities.forEach(aid => {
            formData.append("activity", aid);
        });

        $.ajax({
            url: `http://localhost:8080/api/v1/package/update/${id}`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function () {
                alert("Package updated successfully!");
                $("#editPackageModal").modal("hide");
                fetchAllPackages();
            },
            error: function () {
                alert("Error updating package.");
            }
        });
    });

    // ✅ Image Preview Edit
    $("#editPackageImage").on("change", function () {
        let file = this.files[0];
        if (file) {
            let reader = new FileReader();
            reader.onload = function (e) {
                $("#editPackageImgPreview").attr("src", e.target.result).show();
            };
            reader.readAsDataURL(file);
        } else {
            $("#editPackageImgPreview").hide();
        }
    });

    // ✅ Delete Package
    $(document).on("click", ".delete-package-btn", function () {
        let id = $(this).data("id");
        if (confirm("Are you sure you want to delete this package?")) {
            $.ajax({
                url: `http://localhost:8080/api/v1/package/delete/${id}`,
                type: "DELETE",
                success: function () {
                    alert("Package deleted successfully!");
                    fetchAllPackages();
                },
                error: function () {
                    alert("Error deleting package.");
                }
            });
        }
    });

});
