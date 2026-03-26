// const BASE_URL = "http://localhost:8080/api/v1"; // ← Update if needed
//
// // Navigation
// function showPanel(panelId) {
//     $('.content > div').addClass('hidden');
//     $('#' + panelId).removeClass('hidden');
//     $('.sidebar a').removeClass('active');
//     $(`.sidebar a[onclick*="${panelId}"]`).addClass('active');
//
//     switch (panelId) {
//         case 'dashboard':
//             loadDashboardCounts();
//             loadAppointmentsPerDayChart();
//             loadRevenueByTreatmentChart();
//             break;
//         case 'patients':
//             loadAllPatients();
//             break;
//         case 'practitioners':
//             loadAllPractitioners();
//             break;
//         case 'appointments':
//             loadAllAppointments();
//             break;
//         case 'treatments':
//             loadAllTreatments();
//             break;
//         case 'medicines':
//             loadAllMedicines();
//             break;
//         case 'payments':
//             loadAllPayments();
//             break;
//     }
// }
//
// // Logout
// function logout() {
//     localStorage.removeItem("name");
//     localStorage.removeItem("token");
//     localStorage.removeItem("email");
//     localStorage.removeItem("role");
//     window.location.href = "index.html";
// }
//
// // DASHBOARD ---------------------------------------------------
// function loadDashboardCounts() {
//     $.ajax({
//         url: `${BASE_URL}/admin/dashboard/counts`,
//         method: "GET",
//         success: function (res) {
//             const d = res.data || {};
//             $('#totalAppointments').text(d.totalAppointments ?? 0);
//             $('#totalPatients').text(d.totalPatients ?? 0);
//             $('#totalPractitioners').text(d.totalPractitioners ?? 0);
//             $('#revenueToday').text((d.revenueToday ?? 0).toLocaleString());
//         },
//         error: function () {
//             console.warn("Failed to load dashboard counts");
//         }
//     });
// }
//
// let appointmentsChartRef, revenueChartRef;
//
// function loadAppointmentsPerDayChart() {
//     $.ajax({
//         url: `${BASE_URL}/admin/dashboard/chart/appointments-per-day`,
//         method: "GET",
//         success: function (res) {
//             const labels = (res.data || []).map(i => i.date);
//             const data = (res.data || []).map(i => i.count);
//             if (appointmentsChartRef) appointmentsChartRef.destroy();
//             appointmentsChartRef = new Chart(document.getElementById('appointmentsPerDayChart'), {
//                 type: 'bar',
//                 data: {
//                     labels,
//                     datasets: [{
//                         label: 'Appointments',
//                         data,
//                         backgroundColor: 'rgba(102,187,106,0.7)'
//                     }]
//                 },
//                 options: { plugins: { legend: { display: false } } }
//             });
//         },
//         error: function () {
//             console.warn("Failed to load appointments-per-day chart");
//         }
//     });
// }
//
// function loadRevenueByTreatmentChart() {
//     $.ajax({
//         url: `${BASE_URL}/admin/dashboard/chart/revenue-by-treatment`,
//         method: "GET",
//         success: function (res) {
//             const labels = (res.data || []).map(i => i.treatmentName);
//             const data = (res.data || []).map(i => i.revenue);
//             if (revenueChartRef) revenueChartRef.destroy();
//             revenueChartRef = new Chart(document.getElementById('revenueByTreatmentChart'), {
//                 type: 'line',
//                 data: {
//                     labels,
//                     datasets: [{
//                         label: 'Revenue (LKR)',
//                         data,
//                         fill: false,
//                         tension: 0.2,
//                         borderColor: 'rgba(46,125,50,0.9)'
//                     }]
//                 },
//                 options: { plugins: { legend: { display: false } } }
//             });
//         },
//         error: function () {
//             console.warn("Failed to load revenue-by-treatment chart");
//         }
//     });
// }
//
// // PATIENTS ----------------------------------------------------
// let allPatients = [];
// function loadAllPatients() {
//     $.ajax({
//         url: `${BASE_URL}/patients/getAll`,
//         method: "GET",
//         success: function (res) {
//             allPatients = res.data || [];
//             renderPatients(allPatients);
//         },
//         error: function () { alert("Failed to load patients."); }
//     });
// }
// function renderPatients(list) {
//     const tbody = $("#patientTableBody");
//     tbody.empty();
//     list.forEach(p => {
//         tbody.append(`
//           <tr>
//             <td>${p.name || ''}</td>
//             <td>${p.email || ''}</td>
//             <td>${p.phone || ''}</td>
//             <td>${p.age ?? ''}</td>
//             <td>${p.registeredOn || ''}</td>
//             <td>
//               <button class="btn btn-danger btn-sm" onclick="deletePatient('${p.email}')">Delete</button>
//             </td>
//           </tr>
//         `);
//     });
// }
// $("#searchPatient").on("input", function(){
//     const q = $(this).val().toLowerCase();
//     const filtered = allPatients.filter(p =>
//         (p.name||'').toLowerCase().includes(q) ||
//         (p.email||'').toLowerCase().includes(q) ||
//         (p.phone||'').toLowerCase().includes(q)
//     );
//     renderPatients(filtered);
// });
// function deletePatient(email){
//     if(!confirm("Delete this patient?")) return;
//     $.ajax({
//         url: `${BASE_URL}/patients/delete/${email}`,
//         method: "DELETE",
//         success: function(){ loadAllPatients(); },
//         error: function(){ alert("Failed to delete patient."); }
//     });
// }
//
// // PRACTITIONERS ----------------------------------------------
// let allPractitioners = [];
//
// function loadAllPractitioners() {
//     $.ajax({
//         url: "http://localhost:8080/api/v1/doctor/getAll",
//         method: "GET",
//         success: function (res) {
//             allPractitioners = res.data || [];
//             renderPractitioners(allPractitioners);
//         },
//         error: function () { alert("Failed to load doctors."); }
//     });
// }
//
// function renderPractitioners(list) {
//     const tbody = $("#practitionerTableBody");
//     tbody.empty();
//     list.forEach(pr => {
//         tbody.append(`
//           <tr>
//             <td>${pr.fullName || ''}</td>
//             <td>${pr.email || ''}</td>
//             <td>${pr.description || ''}</td>
//             <td>${(pr.paymentPerDay ?? 0).toLocaleString()}</td>
//             <td>${pr.status ?? 'ACTIVE'}</td>
//             <td>${pr.booked ?? 'NO'}</td>
//             <td>
//               <a href="${pr.linkedin || '#'}" target="_blank">${pr.linkedin ? 'LinkedIn' : ''}</a>
//             </td>
//             <td>
//               ${pr.imageUrl ? `<img src="${pr.imageUrl}" style="width:50px;height:50px;border-radius:6px;">` : ''}
//             </td>
//             <td>
//               <button class="btn btn-danger btn-sm" onclick="deletePractitioner('${pr.email}')">Delete</button>
//             </td>
//           </tr>
//         `);
//     });
// }
//
// $("#searchPractitioner").on("input", function () {
//     const q = $(this).val().toLowerCase();
//     const filtered = allPractitioners.filter(pr =>
//         (pr.fullName || '').toLowerCase().includes(q) ||
//         (pr.email || '').toLowerCase().includes(q)
//     );
//     renderPractitioners(filtered);
// });
//
// function deletePractitioner(email) {
//     if (!confirm("Delete this practitioner?")) return;
//     $.ajax({
//         url: `${BASE_URL}/practitioners/delete/${email}`,
//         method: "DELETE",
//         success: function () { loadAllPractitioners(); },
//         error: function () { alert("Failed to delete practitioner."); }
//     });
// }
//
// // Add Practitioner (image optional)
// $("#prImage").on("change", function () {
//     const f = this.files?.[0];
//     if (!f) return;
//     const reader = new FileReader();
//     reader.onload = e => $("#prImagePreview").attr("src", e.target.result).show();
//     reader.readAsDataURL(f);
// });
//
// $("#addPractitionerForm").on("submit", function (e) {
//     e.preventDefault();
//     const fd = new FormData();
//     fd.append("fullName", $("#prName").val());
//     fd.append("email", $("#prEmail").val());
//     fd.append("description", $("#prDescription").val());
//     fd.append("paymentPerDay", $("#prFee").val());
//     fd.append("linkedin", $("#prLinkedIn").val());
//     fd.append("status", "ACTIVE"); // default
//     fd.append("booked", "NO");     // default
//     const img = $("#prImage")[0].files[0];
//     if (img) fd.append("image", img);
//
//     $.ajax({
//         url: "http://localhost:8080/api/v1/doctor/getAll",
//         method: "POST",
//         data: fd,
//         processData: false,
//         contentType: false,
//         success: function () {
//             $("#addPractitionerModal").modal('hide');
//             $("#addPractitionerForm")[0].reset();
//             $("#prImagePreview").hide();
//             loadAllPractitioners();
//         },
//         error: function () { alert("Failed to create Doctor."); }
//     });
// });
//
// // APPOINTMENTS -----------------------------------------------
// let allAppointments = [];
// function loadAllAppointments() {
//     $.ajax({
//         url: `${BASE_URL}/appointments/getAll`,
//         method: "GET",
//         success: function (res) {
//             allAppointments = res.data || [];
//             renderAppointments(allAppointments);
//         },
//         error: function(){ alert("Failed to load appointments."); }
//     });
// }
// function renderAppointments(list){
//     const tbody = $("#appointmentTableBody");
//     tbody.empty();
//     list.forEach(a => {
//         tbody.append(`
//           <tr>
//             <td>${a.patientName || a.patientEmail || ''}</td>
//             <td>${a.practitionerName || a.practitionerEmail || ''}</td>
//             <td>${a.treatmentName || ''}</td>
//             <td>${a.date || ''}</td>
//             <td>${a.time || ''}</td>
//             <td>${a.status || ''}</td>
//             <td>${(a.fee ?? 0).toLocaleString()}</td>
//           </tr>
//         `);
//     });
// }
// $("#searchAppointment").on("input", function(){
//     const q = $(this).val().toLowerCase();
//     const filtered = allAppointments.filter(a =>
//         (a.patientName||'').toLowerCase().includes(q) ||
//         (a.practitionerName||'').toLowerCase().includes(q) ||
//         (a.patientEmail||'').toLowerCase().includes(q) ||
//         (a.practitionerEmail||'').toLowerCase().includes(q)
//     );
//     renderAppointments(filtered);
// });
//
// // TREATMENTS -------------------------------------------------
// let allTreatments = [];
// function loadAllTreatments() {
//     $.ajax({
//         url: `${BASE_URL}/treatments/getAll`,
//         method: "GET",
//         success: function (res) {
//             allTreatments = res.data || [];
//             renderTreatments(allTreatments);
//         },
//         error: function(){ alert("Failed to load treatments."); }
//     });
// }
// function renderTreatments(list){
//     const tbody = $("#treatmentTableBody");
//     tbody.empty();
//     list.forEach(t => {
//         tbody.append(`
//           <tr>
//             <td>${t.code || ''}</td>
//             <td>${t.name || ''}</td>
//             <td>${t.durationMinutes ?? ''}</td>
//             <td>${(t.baseFee ?? 0).toLocaleString()}</td>
//             <td>${t.description || ''}</td>
//             <td>${t.imageUrl ? `<img src="${t.imageUrl}" style="width:60px;height:60px;border-radius:4px;object-fit:cover">` : ''}</td>
//             <td>
//               <button class="btn btn-danger btn-sm" onclick="deleteTreatment('${t.code}')">Delete</button>
//             </td>
//           </tr>
//         `);
//     });
// }
// $("#trImage").on("change", function(){
//     const f = this.files?.[0];
//     if(!f) return;
//     const reader = new FileReader();
//     reader.onload = e => $("#trImagePreview").attr("src", e.target.result).show();
//     reader.readAsDataURL(f);
// });
// $("#addTreatmentForm").on("submit", function(e){
//     e.preventDefault();
//     const fd = new FormData();
//     fd.append("code", $("#trCode").val());
//     fd.append("name", $("#trName").val());
//     fd.append("durationMinutes", $("#trDuration").val());
//     fd.append("baseFee", $("#trFee").val());
//     fd.append("description", $("#trDescription").val());
//     const img = $("#trImage")[0].files[0];
//     if(img) fd.append("image", img);
//
//     $.ajax({
//         url: `${BASE_URL}/treatments/create`,
//         method: "POST",
//         data: fd,
//         processData: false,
//         contentType: false,
//         success: function(){
//             $("#addTreatmentModal").modal('hide');
//             $("#addTreatmentForm")[0].reset();
//             $("#trImagePreview").hide();
//             loadAllTreatments();
//         },
//         error: function(){ alert("Failed to create treatment."); }
//     });
// });
// $("#searchTreatment").on("input", function(){
//     const q = $(this).val().toLowerCase();
//     const filtered = allTreatments.filter(t =>
//         (t.name||'').toLowerCase().includes(q) ||
//         (t.code||'').toLowerCase().includes(q) ||
//         (t.description||'').toLowerCase().includes(q)
//     );
//     renderTreatments(filtered);
// });
// function deleteTreatment(code){
//     if(!confirm("Delete this treatment?")) return;
//     $.ajax({
//         url: `${BASE_URL}/treatments/delete/${code}`,
//         method: "DELETE",
//         success: function(){ loadAllTreatments(); },
//         error: function(){ alert("Failed to delete treatment."); }
//     });
// }
//
// // MEDICINES --------------------------------------------------
// let allMedicines = [];
// function loadAllMedicines() {
//     $.ajax({
//         url: `${BASE_URL}/medicines/getAll`,
//         method: "GET",
//         success: function (res) {
//             allMedicines = res.data || [];
//             renderMedicines(allMedicines);
//         },
//         error: function(){ alert("Failed to load medicines."); }
//     });
// }
// function renderMedicines(list){
//     const tbody = $("#medicineTableBody");
//     tbody.empty();
//     list.forEach(m => {
//         tbody.append(`
//           <tr>
//             <td>${m.sku || ''}</td>
//             <td>${m.name || ''}</td>
//             <td>${m.category || ''}</td>
//             <td>${m.stock ?? 0}</td>
//             <td>${(m.unitPrice ?? 0).toLocaleString()}</td>
//             <td>${m.reorderLevel ?? 0}</td>
//             <td>
//               <button class="btn btn-danger btn-sm" onclick="deleteMedicine('${m.sku}')">Delete</button>
//             </td>
//           </tr>
//         `);
//     });
// }
// $("#searchMedicine").on("input", function(){
//     const q = $(this).val().toLowerCase();
//     const filtered = allMedicines.filter(m =>
//         (m.name||'').toLowerCase().includes(q) ||
//         (m.sku||'').toLowerCase().includes(q)
//     );
//     renderMedicines(filtered);
// });
// function deleteMedicine(sku){
//     if(!confirm("Delete this item?")) return;
//     $.ajax({
//         url: `${BASE_URL}/medicines/delete/${sku}`,
//         method: "DELETE",
//         success: function(){ loadAllMedicines(); },
//         error: function(){ alert("Failed to delete medicine."); }
//     });
// }
//
// // PAYMENTS ---------------------------------------------------
// let allPayments = [];
// function loadAllPayments() {
//     $.ajax({
//         url: `${BASE_URL}/payments/getAll`,
//         method: "GET",
//         success: function (res) {
//             allPayments = res.data || [];
//             renderPayments(allPayments);
//         },
//         error: function(){ alert("Failed to load payments."); }
//     });
// }
// function renderPayments(list){
//     const tbody = $("#paymentTableBody");
//     tbody.empty();
//     list.forEach(p => {
//         const amt = Number(p.amount || 0);
//         tbody.append(`
//           <tr>
//             <td>${p.id || ''}</td>
//             <td>${p.patientEmail || ''}</td>
//             <td>LKR ${amt.toLocaleString(undefined,{minimumFractionDigits:2, maximumFractionDigits:2})}</td>
//             <td>${p.method || ''}</td>
//             <td>${p.paymentDate || ''}</td>
//           </tr>
//         `);
//     });
// }
// $("#searchPaymentEmail").on("input", function(){
//     const q = $(this).val().toLowerCase();
//     const filtered = allPayments.filter(p => (p.patientEmail||'').toLowerCase().includes(q));
//     renderPayments(filtered);
// });
//
// // INIT -------------------------------------------------------
// $(document).ready(function () {
//     showPanel('dashboard'); // default
// });