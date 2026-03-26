$(document).ready(function () {
    const ARTICLE_API = "http://localhost:8080/api/v1/article";

    // Fetch and display articles
    function fetchAllArticles() {
        $.ajax({
            url: `${ARTICLE_API}/getAll`,
            type: "GET",
            success: function (response) {
                if (response.code === 201 || response.code === 200) {
                    let list = response.data;
                    let html = "";
                    if (!list || list.length === 0) {
                        $("#articleTableBody").html("<tr><td colspan='5'>No Articles Found</td></tr>");
                        return;
                    }
                    list.forEach(article => {
                        const img = article.imageUrl
                            ? `<img src="/uploads/${article.imageUrl}" style="width:60px;height:60px;border-radius:5px;object-fit:cover;">`
                            : "No Image";
                        html += `
                            <tr>
                                <td>${article.id}</td>
                                <td>${article.title}</td>
                                <td>${article.description}</td>
                                <td>${img}</td>
                                <td>
                                    <button class="btn btn-warning btn-sm edit-article-btn"
                                        data-id="${article.id}"
                                        data-title="${article.title}"
                                        data-description="${article.description}"
                                        data-image="/uploads/${article.imageUrl || ''}"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editArticleModal">
                                        Edit
                                    </button>
                                    <button class="btn btn-danger btn-sm delete-article-btn" data-id="${article.id}">Delete</button>
                                </td>
                            </tr>
                        `;
                    });
                    $("#articleTableBody").html(html);
                } else {
                    $("#articleTableBody").html("<tr><td colspan='5'>No Articles Found</td></tr>");
                }
            },
            error: function () {
                $("#articleTableBody").html("<tr><td colspan='5'>Error loading articles.</td></tr>");
            }
        });
    }
    fetchAllArticles();

    // Add article
    $("#addArticleForm").submit(function (e) {
        e.preventDefault();
        let formData = new FormData(this);
        $.ajax({
            url: `${ARTICLE_API}/save`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (res) {
                alert(res.message);
                if (res.code === 201 || res.code === 200) {
                    $("#addArticleModal").modal("hide");
                    $("#addArticleForm")[0].reset();
                    $("#addArticleImgPreview").hide();
                    fetchAllArticles();
                }
            },
            error: function () { alert("Error adding article."); }
        });
    });

    // Image preview add
    $("#articleImage").on("change", function (e) {
        let reader = new FileReader();
        reader.onload = function (ev) {
            $("#addArticleImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(e.target.files[0]);
    });

    // Fill edit form
    $(document).on("click", ".edit-article-btn", function () {
        $("#editArticleId").val($(this).data("id"));
        $("#editArticleTitle").val($(this).data("title"));
        $("#editArticleDescription").val($(this).data("description"));

        let image = $(this).data("image");
        if (image && image !== "/uploads/undefined") {
            $("#editArticleImgPreview").attr("src", image).show();
        } else {
            $("#editArticleImgPreview").hide();
        }
    });

    // Image preview edit
    $("#editArticleImage").on("change", function (e) {
        let reader = new FileReader();
        reader.onload = function (ev) {
            $("#editArticleImgPreview").attr("src", ev.target.result).show();
        };
        reader.readAsDataURL(e.target.files[0]);
    });

    // Update article
    $("#editArticleForm").submit(function (e) {
        e.preventDefault();
        let id = $("#editArticleId").val();
        let formData = new FormData(this);
        $.ajax({
            url: `${ARTICLE_API}/update/${id}`,
            type: "POST",
            data: formData,
            processData: false,
            contentType: false,
            success: function (res) {
                alert(res.message);
                if (res.code === 201 || res.code === 200) {
                    $("#editArticleModal").modal("hide");
                    $("#editArticleForm")[0].reset();
                    $("#editArticleImgPreview").hide();
                    fetchAllArticles();
                }
            },
            error: function () { alert("Error updating article."); }
        });
    });

    // Delete article
    $(document).on("click", ".delete-article-btn", function () {
        let id = $(this).data("id");
        if (confirm("Are you sure you want to delete this article?")) {
            $.ajax({
                url: `${ARTICLE_API}/delete/${id}`,
                type: "DELETE",
                success: function (res) {
                    alert(res.message);
                    fetchAllArticles();
                },
                error: function () { alert("Error deleting article."); }
            });
        }
    });

    // Search
    $("#searchArticle").on("keyup", function () {
        const filter = $(this).val().toLowerCase();
        $("#articleTableBody tr").each(function () {
            const title = $(this).find("td:eq(1)").text().toLowerCase();
            const desc = $(this).find("td:eq(2)").text().toLowerCase();
            $(this).toggle(title.includes(filter) || desc.includes(filter));
        });
    });
});
