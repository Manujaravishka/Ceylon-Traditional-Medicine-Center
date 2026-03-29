// ================= ACTIVITY MANAGEMENT =================
const ACTIVITY_BASE_URL = "http://localhost:8080/api/v1/activity";

// Helper function to get auth headers
function getActivityAuthHeaders() {
    const token = getFromLocalStorage("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

// ================= FETCH ALL ACTIVITIES =================
function fetchAllActivities() {
    console.log("Loading activities...");
    
    $.ajax({
        url: ACTIVITY_BASE_URL + "/getAll",
        type: "GET",
        headers: getActivityAuthHeaders(),
        success: function (response) {
            console.log("GET Activities Response:", response);
            
            let activities = [];
            if (response && response.data && Array.isArray(response.data)) {
                activities = response.data;
            } else if (Array.isArray(response)) {
                activities = response;
            } else if (response && response.code === 201 && response.data) {
                activities = response.data;
            }
            
            renderActivityTable(activities);
        },
        error: function (err) {
            console.error("GET Activities Error:", err);
            let errorMsg = "Error loading activities.";
            if (err.status === 401 || err.status === 403) {
                errorMsg = "Authentication required. Please login as admin.";
            }
            $("#activityTableBody").html(`<td><td colspan='6' class='text-center text-danger'>${errorMsg}</td></tr>`);
        }
    });
}

// ================= RENDER ACTIVITY TABLE =================
function renderActivityTable(activities) {
    const tbody = $("#activityTableBody");
    tbody.empty();
    
    if (!activities || activities.length === 0) {
        tbody.html('<tr><td colspan="6" class="text-center">No activities found</td></tr>');
        return;
    }
    
    let html = "";
    activities.forEach(act => {
        // Handle image URL
        let imageHtml = "-";
        if (act.imageUrl) {
            if (act.imageUrl.startsWith('http')) {
                imageHtml = `<img src="${escapeHtml(act.imageUrl)}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;">`;
            } else {
                imageHtml = `<img src="/uploads/${escapeHtml(act.imageUrl)}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;" onerror="this.src='https://via.placeholder.com/70'">`;
            }
        }
        
        html += `
            <tr>
                <td>${act.id || ''}</td>
                <td><strong>${escapeHtml(act.name || '')}</strong></td>
                <td>${escapeHtml((act.description || '').substring(0, 50))}${(act.description || '').length > 50 ? '...' : ''}</td>
                <td>LKR ${parseFloat(act.costPerDay || 0).toLocaleString()}</td>
                <td>${imageHtml}</td>
                <td>
                    <button class="btn btn-warning btn-sm rounded-pill me-1 edit-act-btn"
                        data-id="${act.id}"
                        data-name="${escapeHtml(act.name)}"
                        data-description="${escapeHtml(act.description || '')}"
                        data-cost="${act.costPerDay || 0}"
                        data-image="${act.imageUrl || ''}"
                        data-bs-toggle="modal"
                        data-bs-target="#editActivityModal">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button class="btn btn-danger-lux btn-sm rounded-pill delete-act-btn" data-id="${act.id}">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                 </td>
             </tr>
        `;
    });
    
    tbody.html(html);
}

// ================= ADD ACTIVITY =================
$("#addActivityForm").off('submit').on('submit', function (e) {
    e.preventDefault();
    
    // Get form values - MATCHING YOUR HTML IDS
    const name = $("#activityName").val().trim();
    const description = $("#activityDescription").val().trim();
    const costPerDay = $("#activityCost").val().trim();
    
    // Validate
    if (!name || !description || !costPerDay) {
        showSweetAlert('warning', 'Missing Fields', 'Please fill in all required fields!');
        return;
    }
    
    // Create FormData with correct parameter names (matching backend)
    let formData = new FormData();
    formData.append("name", name);
    formData.append("description", description);
    formData.append("costPerDay", costPerDay);
    
    // Handle image upload - YOUR HTML USES activityImage ID
    let file = $("#activityImage")[0].files[0];
    if (file) {
        formData.append("imageUrl", file);  // Backend expects 'imageUrl'
    }
    
    console.log("Sending Activity Data:");
    console.log("Name:", name);
    console.log("Description:", description);
    console.log("Cost Per Day:", costPerDay);
    console.log("Image:", file ? file.name : "No image");
    
    // Show loading
    Swal.fire({
        title: 'Saving...',
        text: 'Please wait while we save the activity',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
    
    $.ajax({
        url: ACTIVITY_BASE_URL + "/save",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        headers: getActivityAuthHeaders(),
        success: function (response) {
            console.log("ADD SUCCESS:", response);
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Activity Added Successfully!',
                confirmButtonColor: '#1F6E58',
                timer: 2000
            });
            
            // Reset form and close modal
            $("#addActivityForm")[0].reset();
            $("#addActivityImgPreview").hide();
            $("#addActivityModal").modal("hide");
            
            // Reload table
            fetchAllActivities();
        },
        error: function (err) {
            console.error("ADD ERROR:", err);
            let errorMsg = "Error adding activity.";
            if (err.responseJSON && err.responseJSON.message) {
                errorMsg = err.responseJSON.message;
            } else if (err.status === 401) {
                errorMsg = "Authentication required. Please login again.";
            } else if (err.status === 409) {
                errorMsg = "Activity with this name already exists!";
            }
            Swal.fire({
                icon: 'error',
                title: 'Error!',
                text: errorMsg,
                confirmButtonColor: '#1F6E58'
            });
        }
    });
});

// ================= IMAGE PREVIEW FOR ADD =================
$("#activityImage").off('change').on('change', function (e) {
    const file = e.target.files[0];
    if (file) {
        let reader = new FileReader();
        reader.onload = function (ev) {
            $("#addActivityImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(file);
    } else {
        $("#addActivityImgPreview").hide();
    }
});

// ================= FILL EDIT FORM =================
$(document).on("click", ".edit-act-btn", function () {
    const id = $(this).data("id");
    const name = $(this).data("name");
    const description = $(this).data("description");
    const cost = $(this).data("cost");
    const image = $(this).data("image");
    
    // Note: Your edit modal needs these IDs - you'll need to create an edit modal
    // For now, we'll use alert since edit modal might not exist
    console.log("Edit clicked - ID:", id);
    
    // If you have an edit modal, uncomment this:
    /*
    $("#editActivityId").val(id);
    $("#editActivityName").val(name);
    $("#editActivityDescription").val(description);
    $("#editActivityCost").val(cost);
    
    if (image && image !== "") {
        if (image.startsWith('http')) {
            $("#editActivityImgPreview").attr("src", image).show();
        } else {
            $("#editActivityImgPreview").attr("src", "/uploads/" + image).show();
        }
    } else {
        $("#editActivityImgPreview").hide();
    }
    */
    
    // Temporary alert since edit modal may not exist
    showSweetAlert('info', 'Edit Feature', `Edit activity: ${name}\nID: ${id}\nThis feature requires an edit modal.`);
});

// ================= DELETE ACTIVITY =================
$(document).on("click", ".delete-act-btn", function () {
    let id = $(this).data("id");
    let row = $(this).closest('tr');
    let name = row.find('td:eq(1)').text();
    
    showConfirmDialog(
        'Delete Activity',
        `Are you sure you want to delete "${name}"? This action cannot be undone.`,
        'Yes, Delete',
        'Cancel'
    ).then((result) => {
        if (result.isConfirmed) {
            // Show loading
            Swal.fire({
                title: 'Deleting...',
                text: 'Please wait',
                allowOutsideClick: false,
                didOpen: () => {
                    Swal.showLoading();
                }
            });
            
            $.ajax({
                url: `${ACTIVITY_BASE_URL}/delete/${id}`,
                type: "DELETE",
                headers: getActivityAuthHeaders(),
                success: function (res) {
                    console.log("DELETE SUCCESS:", res);
                    Swal.fire({
                        icon: 'success',
                        title: 'Deleted!',
                        text: res.message || 'Activity deleted successfully!',
                        confirmButtonColor: '#1F6E58',
                        timer: 2000
                    });
                    fetchAllActivities();
                },
                error: function (err) {
                    console.error("DELETE ERROR:", err);
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: 'Error deleting activity.',
                        confirmButtonColor: '#1F6E58'
                    });
                }
            });
        }
    });
});

// ================= ESCAPE HTML HELPER =================
function escapeHtml(str) {
    if (!str) return '';
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

// ================= INITIAL LOAD =================
$(document).ready(function() {
    console.log("Activity.js loaded");
    setTimeout(function() {
        fetchAllActivities();
    }, 100);
});