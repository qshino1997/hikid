<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Chi tiết Nhà sản xuất</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet"/>
</head>
<body>
<%@ include file="/WEB-INF/views/admin/fragments/header.jsp" %>
<div class="container-fluid content-area">
    <div class="card mt-4">
        <c:choose>
            <c:when test="${mode=='create'}">
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-building"></i> Thêm Nhà sản xuất
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-building"></i> Sửa Nhà sản xuất
                </div>
            </c:otherwise>
        </c:choose>
        <div class="card-body">
            <c:if test="${mode!='create'}">
                <button id="btnEdit" class="btn btn-outline-primary mb-3">
                    <i class="bi bi-pencil-square me-1"></i> Chỉnh sửa
                </button>
            </c:if>

            <form:form id="manufacturerForm" method="post" modelAttribute="manufacturer"
                       action="${mode=='create'? '/admin/manufacturer/save' : '/admin/manufacturer/update'}">
                <form:hidden path="id"/>

                <div class="mb-3">
                    <label class="form-label">Tên</label>
                    <form:input path="name" cssClass="form-control editable"
                                readonly="${mode!='create'}" required="true"/>
                    <form:errors path="name" cssClass="text-danger"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Quốc gia</label>
                    <form:input path="country" cssClass="form-control editable"
                                readonly="${mode!='create'}"/>
                    <form:errors path="country" cssClass="text-danger"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Website</label>
                    <form:input path="website" cssClass="form-control editable"
                                readonly="${mode!='create'}"/>
                    <form:errors path="website" cssClass="text-danger"/>
                </div>

                <div class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">Followers</label>
                        <form:input path="followers_count" cssClass="form-control editable"
                                    type="number" min="0" readonly="${mode!='create'}"/>
                        <form:errors path="followers_count" cssClass="text-danger"/>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Satisfaction (%)</label>
                        <form:input path="satisfaction_level" cssClass="form-control editable"
                                    type="number" min="0" max="100" readonly="${mode!='create'}"/>
                        <form:errors path="satisfaction_level" cssClass="text-danger"/>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label">Rating</label>
                        <form:input path="rating" cssClass="form-control editable"
                                    type="number" step="0.1" readonly="${mode!='create'}"/>
                        <form:errors path="rating" cssClass="text-danger"/>
                    </div>
                </div>

                <div class="row g-3 mt-3">
                    <div class="col-md-6">
                        <label class="form-label">Reviews</label>
                        <form:input path="number_of_reviews" cssClass="form-control editable"
                                    type="number" min="0" readonly="${mode!='create'}"/>
                        <form:errors path="number_of_reviews" cssClass="text-danger"/>
                    </div>
                </div>

                <div class="d-flex mt-4">
                    <button type="button" id="btnSave"
                            class="btn btn-primary me-2 <c:if test='${mode!="create"}'>d-none</c:if>">
                        <c:choose>
                            <c:when test="${mode=='create'}">Tạo mới</c:when>
                            <c:otherwise>Cập nhật</c:otherwise>
                        </c:choose>
                    </button>
                    <c:if test="${mode!='create'}">
                        <button type="button" id="btnCancel" class="btn btn-secondary d-none">
                            Hủy
                        </button>
                    </c:if>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const btnEdit   = document.getElementById('btnEdit');
        const btnSave   = document.getElementById('btnSave');
        const btnCancel = document.getElementById('btnCancel');
        const editables = Array.from(document.querySelectorAll('.editable'));
        let original    = {};

        function toggleEdit(isEdit) {
            editables.forEach(el => {
                if (el.tagName === 'SELECT') {
                    el.disabled = !isEdit;
                } else {
                    if (isEdit) {
                        original[el.name] = el.value;
                        el.removeAttribute('readonly');
                    } else {
                        el.setAttribute('readonly', true);
                        if (original.hasOwnProperty(el.name)) {
                            el.value = original[el.name];
                        }
                    }
                }
            });
            btnSave.classList.toggle('d-none', !isEdit);
            btnCancel.classList.toggle('d-none', !isEdit);
            btnEdit.classList.toggle('d-none', isEdit);
        }

        if (btnEdit)   btnEdit.addEventListener('click',   () => toggleEdit(true));
        if (btnCancel) btnCancel.addEventListener('click', () => toggleEdit(false));
        if (btnSave)   btnSave.addEventListener('click',   () => document.getElementById('manufacturerForm').submit());
    });
</script>
</body>
</html>
