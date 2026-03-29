// ================= ACCOMMODATION MANAGEMENT =================
const ACCOMMODATION_BASE_URL = "http://localhost:8080/api/v1/accommodation";

// Helper function to get auth headers
function getAccommodationAuthHeaders() {
    const token = getFromLocalStorage("token");
    return token ? { Authorization: `Bearer ${token}` } : {};
}

// ================= FETCH ALL ACCOMMODATIONS =================
function loadAllAccommodations() {
    console.log("Loading accommodations...");
    
    $.ajax({
        url: ACCOMMODATION_BASE_URL + "/getAll",
        type: "GET",
        headers: getAccommodationAuthHeaders(),
        success: function(response) {
            console.log("GET Accommodations Response:", response);
            
            let accommodations = [];
            if (response && response.data && Array.isArray(response.data)) {
                accommodations = response.data;
            } else if (Array.isArray(response)) {
                accommodations = response;
            } else if (response && response.code === 201 && response.data) {
                accommodations = response.data;
            }
            
            renderAccommodationTable(accommodations);
        },
        error: function(err) {
            console.error("GET Accommodations Error:", err);
            let errorMsg = "Error loading accommodations.";
            if (err.status === 401 || err.status === 403) {
                errorMsg = "Authentication required. Please login as admin.";
            }
            $("#accommodationTableBody").html(`<tr><td colspan='8' class='text-center text-danger'>${errorMsg}</td></tr>`);
        }
    });
}

// ================= RENDER ACCOMMODATION TABLE =================
function renderAccommodationTable(accommodations) {
    const tbody = $("#accommodationTableBody");
    tbody.empty();
    
    if (!accommodations || accommodations.length === 0) {
        tbody.html('<tr><td colspan="8" class="text-center">No accommodations found</td></tr>');
        return;
    }
    
    let html = "";
    accommodations.forEach(acc => {
        // Handle image URL
        let imageHtml = "-";
        if (acc.imageUrl) {
            if (acc.imageUrl.startsWith('http')) {
                imageHtml = `<img src="${escapeHtml(acc.imageUrl)}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;">`;
            } else {
                imageHtml = `<img src="/uploads/${escapeHtml(acc.imageUrl)}" style="width:70px;height:70px;border-radius:5px;object-fit:cover;" onerror="this.src='https://via.placeholder.com/70'">`;
            }
        }
        
        html += `
            <tr>
                <td>${acc.id || ''}</td>
                <td><strong>${escapeHtml(acc.name || '')}</strong></td>
                <td>${escapeHtml((acc.description || '').substring(0, 60))}${(acc.description || '').length > 60 ? '...' : ''}</td>
                <td>${escapeHtml(acc.location || '')}</td>
                <td><span class="badge-status">${escapeHtml(acc.category || '')}</span></td>
                <td>LKR ${parseFloat(acc.costPerDay || 0).toLocaleString()}</td>
                <td>${imageHtml}</td>
                <td>
                    <button class="btn btn-warning btn-sm rounded-pill me-1 edit-accommodation-btn" 
                        data-id="${acc.id}"
                        data-name="${escapeHtml(acc.name)}"
                        data-description="${escapeHtml(acc.description || '')}"
                        data-location="${escapeHtml(acc.location || '')}"
                        data-category="${escapeHtml(acc.category || '')}"
                        data-cost="${acc.costPerDay || 0}"
                        data-booked="${acc.booked || 'NO'}"
                        data-image="${acc.imageUrl || ''}"
                        data-bs-toggle="modal" 
                        data-bs-target="#editAccommodationModal">
                        <i class="fas fa-edit"></i> Edit
                    </button>
                    <button class="btn btn-danger-lux btn-sm rounded-pill delete-accommodation-btn" data-id="${acc.id}">
                        <i class="fas fa-trash"></i> Delete
                    </button>
                </td>
            </tr>
        `;
    });
    
    tbody.html(html);
}

// ================= ADD ACCOMMODATION =================
$("#addAccommodationForm").off('submit').on('submit', function(e) {
    e.preventDefault();
    
    // Get form values
    const name = $("#accommodationName").val().trim();
    const description = $("#accommodationDescription").val().trim();
    const location = $("#accommodationLocation").val().trim();
    const category = $("#accommodationCategory").val();
    const costPerDay = $("#accommodationCost").val().trim();
    
    // Validate required fields
    if (!name || !description || !location || !category || !costPerDay) {
        showSweetAlert('warning', 'Missing Fields', 'Please fill in all required fields!');
        return;
    }
    
    // Create FormData
    let formData = new FormData();
    formData.append("name", name);
    formData.append("description", description);
    formData.append("location", location);
    formData.append("category", category);
    formData.append("costPerDay", costPerDay);
    formData.append("booked", "NO");
    
    // Handle image upload
    let file = $("#accommodationImage")[0].files[0];
    if (file) {
        formData.append("image", file);
    }
    
    // Show loading
    Swal.fire({
        title: 'Saving...',
        text: 'Please wait while we save the accommodation',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
    
    $.ajax({
        url: ACCOMMODATION_BASE_URL + "/save",
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        headers: getAccommodationAuthHeaders(),
        success: function(response) {
            console.log("ADD SUCCESS:", response);
            Swal.fire({
                icon: 'success',
                title: 'Success!',
                text: 'Accommodation Added Successfully!',
                confirmButtonColor: '#1F6E58',
                timer: 2000
            });
            
            // Reset form and close modal
            $("#addAccommodationForm")[0].reset();
            $("#addImgPreview").hide();
            $("#addAccommodationModal").modal("hide");
            
            // Reload table
            loadAllAccommodations();
            
            // Also update dashboard count if function exists
            if (typeof loadDashboardCounts === 'function') {
                loadDashboardCounts();
            }
        },
        error: function(err) {
            console.error("ADD ERROR:", err);
            let errorMsg = "Error adding accommodation.";
            if (err.responseJSON && err.responseJSON.message) {
                errorMsg = err.responseJSON.message;
            } else if (err.status === 401) {
                errorMsg = "Authentication required. Please login again.";
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
$("#accommodationImage").off('change').on('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(ev) {
            $("#addImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(file);
    } else {
        $("#addImgPreview").hide();
    }
});

// ================= EDIT ACCOMMODATION - FILL FORM =================
$(document).on("click", ".edit-accommodation-btn", function() {
    const id = $(this).data("id");
    const name = $(this).data("name");
    const description = $(this).data("description");
    const location = $(this).data("location");
    const category = $(this).data("category");
    const cost = $(this).data("cost");
    const booked = $(this).data("booked");
    const image = $(this).data("image");
    
    // Set form values
    $("#editAccommodationId").val(id);
    $("#editAccommodationName").val(name);
    $("#editAccommodationDescription").val(description);
    $("#editAccommodationLocation").val(location);
    $("#editAccommodationCategory").val(category);
    $("#editAccommodationCostPerDay").val(cost);
    $("#editAccommodationBooked").val(booked || "NO");
    
    // Show current image
    if (image) {
        if (image.startsWith('http')) {
            $("#editImgPreview").attr("src", image).show();
        } else {
            $("#editImgPreview").attr("src", "/uploads/" + image).show();
        }
    } else {
        $("#editImgPreview").hide();
    }
});

// ================= UPDATE ACCOMMODATION =================
$("#editAccommodationForm").off('submit').on('submit', function(e) {
    e.preventDefault();
    
    const id = $("#editAccommodationId").val();
    
    if (!id) {
        showSweetAlert('error', 'Error', 'No accommodation ID found!');
        return;
    }
    
    // Create FormData with correct parameter names (matching backend)
    let formData = new FormData();
    formData.append("editAccommodationName", $("#editAccommodationName").val());
    formData.append("editAccommodationDescription", $("#editAccommodationDescription").val());
    formData.append("editAccommodationLocation", $("#editAccommodationLocation").val());
    formData.append("editAccommodationCategory", $("#editAccommodationCategory").val());
    formData.append("editAccommodationCostPerDay", $("#editAccommodationCostPerDay").val());
    formData.append("editAccommodationBooked", $("#editAccommodationBooked").val());
    
    // Handle image upload
    let file = $("#editAccommodationImage")[0].files[0];
    if (file) {
        formData.append("editAccommodationImage", file);
    }
    
    // Show loading
    Swal.fire({
        title: 'Updating...',
        text: 'Please wait',
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
    
    $.ajax({
        url: `${ACCOMMODATION_BASE_URL}/update/${id}`,
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        headers: getAccommodationAuthHeaders(),
        success: function(response) {
            console.log("UPDATE SUCCESS:", response);
            Swal.fire({
                icon: 'success',
                title: 'Updated!',
                text: 'Accommodation Updated Successfully!',
                confirmButtonColor: '#1F6E58',
                timer: 2000
            });
            
            // Reset and close
            $("#editAccommodationImage").val('');
            $("#editImgPreview").hide();
            $("#editAccommodationModal").modal("hide");
            
            // Reload table
            loadAllAccommodations();
        },
        error: function(err) {
            console.error("UPDATE ERROR:", err);
            let errorMsg = "Error updating accommodation.";
            if (err.responseJSON && err.responseJSON.message) {
                errorMsg = err.responseJSON.message;
            } else if (err.status === 401) {
                errorMsg = "Authentication required. Please login again.";
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

// ================= EDIT IMAGE PREVIEW =================
$("#editAccommodationImage").off('change').on('change', function(e) {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(ev) {
            $("#editImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(file);
    }
});

// ================= DELETE ACCOMMODATION =================
$(document).on("click", ".delete-accommodation-btn", function() {
    const id = $(this).data("id");
    const row = $(this).closest('tr');
    const name = row.find('td:eq(1)').text();
    
    showConfirmDialog(
        'Delete Accommodation', 
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
                url: `${ACCOMMODATION_BASE_URL}/delete/${id}`,
                type: "DELETE",
                headers: getAccommodationAuthHeaders(),
                success: function(response) {
                    console.log("DELETE SUCCESS:", response);
                    Swal.fire({
                        icon: 'success',
                        title: 'Deleted!',
                        text: 'Accommodation has been deleted successfully.',
                        confirmButtonColor: '#1F6E58',
                        timer: 2000
                    });
                    
                    // Reload table
                    loadAllAccommodations();
                    
                    // Update dashboard count
                    if (typeof loadDashboardCounts === 'function') {
                        loadDashboardCounts();
                    }
                },
                error: function(err) {
                    console.error("DELETE ERROR:", err);
                    let errorMsg = "Error deleting accommodation.";
                    if (err.responseJSON && err.responseJSON.message) {
                        errorMsg = err.responseJSON.message;
                    }
                    Swal.fire({
                        icon: 'error',
                        title: 'Error!',
                        text: errorMsg,
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
// Wait for document to be ready
$(document).ready(function() {
    console.log("Accommodation.js loaded");
    // Small delay to ensure DOM is fully ready
    setTimeout(() => {
        loadAllAccommodations();
    }, 100);
});