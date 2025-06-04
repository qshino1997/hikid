<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Quản lý Sản phẩm</title>
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
                    <i class="bi bi-box-seam"></i> Thêm Sản phẩm
                </div>
            </c:when>
            <c:otherwise>
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-box-seam"></i> Sửa Sản phẩm
                </div>
            </c:otherwise>
        </c:choose>
        <input id="mode" hidden="hidden" value="${mode}" />
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
            <form:form method="post" modelAttribute="product" id="productForm" enctype="multipart/form-data"
                       action="${mode=='create'? '/admin/product/save' : '/admin/product/update'}">
            <form:hidden path="product_id"/>
            <div class="mb-3">
                <label class="form-label">Ảnh sản phẩm</label>
                <input type="file" name="imageFile" class="form-control editable"
                       accept="image/*" />
                <c:if test="${not empty product.url}">
                    <div class="mt-2">
                        <img src="${product.url}"
                             alt="Ảnh sản phẩm"
                             class="img-thumbnail"
                             style="max-height: 150px;"
                             onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'">
                    </div>
                </c:if>
            </div>
            <div class="mb-3">
                <label class="form-label">Tên sản phẩm</label>
                <form:input path="name" cssClass="form-control editable"
                            readonly="${mode!='create'}" required="true"/>
                <form:errors path="name" cssClass="text-danger"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Giá</label>
                <form:input path="price" cssClass="form-control editable" type="number"
                            min="0" readonly="${mode!='create'}" required="true"/>
                <form:errors path="price" cssClass="text-danger"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Số lượng kho</label>
                <form:input path="stock" cssClass="form-control editable" type="number"
                            min="0" readonly="${mode!='create'}" required="true"/>
                <form:errors path="stock" cssClass="text-danger"/>
            </div>
            <!-- Các trường bổ sung -->
            <div class="mb-3">
                <label class="form-label">Xuất xứ</label>
                <form:input path="made_in"
                            cssClass="form-control editable"
                            readonly="${mode!='create'}"
                            placeholder="Ví dụ: VN"/>
                <form:errors path="made_in" cssClass="text-danger"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Khối lượng (g)</label>
                <form:input path="product_weight"
                            cssClass="form-control editable"
                            type="number" min="0"
                            readonly="${mode!='create'}"/>
                <form:errors path="product_weight" cssClass="text-danger"/>
            </div>
            <div class="row g-3">
                <div class="col-md-6">
                    <label class="form-label">Tuổi từ</label>
                    <form:input path="appropriate_age_start"
                                cssClass="form-control editable"
                                type="number" min="0"
                                readonly="${mode!='create'}"/>
                    <form:errors path="appropriate_age_start" cssClass="text-danger"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Đến tuổi</label>
                    <form:input path="appropriate_age_end"
                                cssClass="form-control editable"
                                type="number" min="0"
                                readonly="${mode!='create'}"/>
                    <form:errors path="appropriate_age_end" cssClass="text-danger"/>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Cảnh báo</label>
                <form:textarea path="warning"
                               cssClass="form-control editable"
                               rows="2"
                               readonly="${mode!='create'}"/>
                <form:errors path="warning" cssClass="text-danger"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Hướng dẫn sử dụng</label>
                <form:textarea path="instructions"
                               cssClass="form-control editable"
                               rows="3"
                               readonly="${mode!='create'}"/>
                <form:errors path="instructions" cssClass="text-danger"/>
            </div>
            <div class="mb-3">
                <label class="form-label">Hướng dẫn bảo quản</label>
                <form:textarea path="storage_instructions"
                               cssClass="form-control editable"
                               rows="2"
                               readonly="${mode!='create'}"/>
                <form:errors path="storage_instructions" cssClass="text-danger"/>
            </div>
            <div class="row g-3 mb-3">
                <div class="col-md-6">
                    <label class="form-label">Danh mục</label>
                    <form:select path="category_id"
                                 cssClass="form-select editable"
                                 readonly="${mode!='create'}">
                        <form:option value=""></form:option>
                        <c:forEach var="cat" items="${categories}">
                            <form:option value="${cat.id}">
                                ${cat.name}
                            </form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="category_id" cssClass="text-danger"/>
                </div>
                <div class="col-md-6">
                    <label class="form-label">Nhà sản xuất</label>
                    <form:select path="manufacturer_id"
                                 cssClass="form-select editable"
                                 readonly="${mode!='create'}">
                        <form:option value=""></form:option>
                        <c:forEach var="m" items="${manufacturers}">
                            <form:option value="${m.id}">${m.name}</form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="manufacturer_id" cssClass="text-danger"/>
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
                    <button type="button" class="btn btn-secondary d-none" id="btnCancel">
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
    document.addEventListener('DOMContentLoaded', function () {
        const btnEdit = document.getElementById('btnEdit');
        const btnSave = document.getElementById('btnSave');
        const btnCancel = document.getElementById('btnCancel');
        const editables = Array.from(document.querySelectorAll('.editable'));
        let originalValues = {};

        function toggleEditMode(editMode) {
            editables.forEach(el => {
                if (el.tagName === 'SELECT' || el.type === 'file') {
                    // enable/disable select
                    el.disabled = !editMode;
                } else {
                    // <input> hoặc <textarea>: toggle readonly
                    if (editMode) {
                        originalValues[el.name] = el.value;
                        el.removeAttribute('readonly');
                    } else {
                        el.setAttribute('readonly', true);
                        if (originalValues[el.name] !== undefined) {
                            el.value = originalValues[el.name];
                        }
                    }
                }
            });
            btnSave.classList.toggle('d-none', !editMode);
            btnCancel.classList.toggle('d-none', !editMode);
            btnEdit.classList.toggle('d-none', editMode);
        }

        if(document.getElementById('mode').value === 'edit' ) toggleEditMode(false);
        if (btnEdit) btnEdit.addEventListener('click', () => toggleEditMode(true));
        if (btnCancel) btnCancel.addEventListener('click', () => toggleEditMode(false));
        if (btnSave) btnSave.addEventListener('click', () => document.getElementById('productForm').submit());
    });
</script>
</body>
</html>
