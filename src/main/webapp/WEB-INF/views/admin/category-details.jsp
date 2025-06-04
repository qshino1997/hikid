<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Chi tiết Danh mục</title>
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
                    <i class="bi bi-list-ul"></i> Thêm Danh mục
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-list-ul"></i> Sửa Danh mục
                </div>
            </c:otherwise>
        </c:choose>
        <div class="card-body">
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            <c:if test="${not empty failed}">
                <div class="alert alert-danger">${failed}</div>
            </c:if>
            <c:if test="${mode!='create'}">
                <button id="btnEdit" class="btn btn-outline-primary mb-3">
                    <i class="bi bi-pencil-square me-1"></i> Chỉnh sửa
                </button>
            </c:if>
            <input id="mode" hidden="hidden" value="${mode}" />
            <form:form id="categoryForm" method="post" modelAttribute="category"
                       action="${mode=='create'? '/admin/category/create' : '/admin/category/update'}">
                <form:hidden path="id"/>

                <div class="mb-3">
                    <label class="form-label">Tên Danh mục</label>
                    <form:input path="name" cssClass="form-control editable"
                                readonly="${mode!='create'}" required="true"/>
                    <form:errors path="name" cssClass="text-danger"/>
                </div>

                <div class="mb-3">
                    <label class="form-label">Danh mục cha</label>
                    <form:select path="categoryParentId" cssClass="form-select editable"
                                 disabled="${mode!='create'}">
                        <form:option value=""> </form:option>
                        <c:forEach var="p" items="${parents}">
                            <form:option value="${p.id}">${p.label.trim()}</form:option>
                        </c:forEach>
                    </form:select>
                </div>

                <div class="d-flex mt-3">
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
        let original = {};

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

        // ban đầu luôn ở chế độ xem-only
        if(document.getElementById('mode').value === 'edit' ) toggleEdit(false);

        if (btnEdit)   btnEdit.addEventListener('click',   () => toggleEdit(true));
        if (btnCancel) btnCancel.addEventListener('click', () => toggleEdit(false));
        if (btnSave)   btnSave.addEventListener('click',   () => document.getElementById('categoryForm').submit());
    });
</script>
</body>
</html>
