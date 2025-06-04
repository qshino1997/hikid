<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Trang Quản Trị</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet" />
    <style>
        body { font-family: 'Quicksand', sans-serif; }
        .sidebar { height: 100vh; background-color: #343a40; }
        .sidebar .nav-link { color: #adb5bd; }
        .sidebar .nav-link.active, .sidebar .nav-link:hover { color: #fff; background-color: #495057; }
        .content-area { padding: 2rem; }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/admin/fragments/header.jsp" %>
<div class="container-fluid">
    <div class="row">
        <main class="col-md-12 ms-sm-auto content-area">
            <div class="card">
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-person-circle"></i> Hồ sơ
                </div>
                <div class="card-body">
                    <c:if test="${not empty success}">
                        <div class="alert alert-success">${success}</div>
                    </c:if>
                    <c:if test="${not empty failed}">
                        <div class="alert alert-danger">${failed}</div>
                    </c:if>
                    <button id="btnEdit" class="btn btn-outline-primary mb-3">
                        <i class="bi bi-pencil-square me-1"></i>
                        Cập nhật
                    </button>
                    <form:form method="post" modelAttribute="user" id="profileForm"
                               action="/user/profile">
                        <form:hidden path="user_id"/>
                        <div class="mb-3">
                            <label class="form-label">Họ và tên</label>
                            <form:input path="username" cssClass="form-control editable"
                                        readonly="true"/>
                            <form:errors path="username" cssClass="text-danger"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <form:input path="email" cssClass="form-control" type="email" readonly="true"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Ngày sinh</label>
                            <form:input path="date_of_birth" type="date" cssClass="form-control editable" readonly="true"/>
                            <form:errors path="date_of_birth" cssClass="text-danger"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Số điện thoại</label>
                            <form:input path="phone" cssClass="form-control editable" readonly="true"/>
                            <form:errors path="phone" cssClass="text-danger"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Địa chỉ</label>
                            <form:input path="address" cssClass="form-control editable" rows="3" readonly="true"/>
                            <form:errors path="address" cssClass="text-danger"/>
                        </div>
                        <div class="d-flex">
                            <button type="button" id="btnSave"
                                    class="btn btn-primary me-2 d-none">
                                <i class="bi bi-save me-1"></i>Cập nhật
                            </button>
                            <button type="button" class="btn btn-secondary d-none" id="btnCancel">
                                <i class="bi bi-x-circle me-1"></i>Hủy
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="card mt-4">
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-lock-fill"></i> Đổi mật khẩu
                </div>
                <div class="card-body">
                    <c:if test="${not empty pwdSuccess}">
                        <div class="alert alert-success">${pwdSuccess}</div>
                    </c:if>
                    <c:if test="${not empty pwdError}">
                        <div class="alert alert-danger">${pwdError}</div>
                    </c:if>

                    <form action="<c:url value='/user/password'/>" method="post" id="passwordForm">
                        <!-- hidden field chứa user_id -->
                        <input type="hidden" name="user_id" value="${user.user_id}" />

                        <div class="mb-3">
                            <label class="form-label">Mật khẩu cũ</label>
                            <input type="password" name="oldPassword" class="form-control" required/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mật khẩu mới</label>
                            <input type="password" name="newPassword" class="form-control" required minlength="6"/>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Xác nhận mật khẩu mới</label>
                            <input type="password" name="confirmPassword" class="form-control" required minlength="6"/>
                        </div>
                        <button type="button" class="btn btn-warning" id="btnChangePassword">
                            <i class="bi bi-key-fill me-1"></i>Đổi mật khẩu
                        </button>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        const btnEdit = document.getElementById('btnEdit');
        const btnSave = document.getElementById('btnSave');
        const btnChange = document.getElementById('btnChangePassword');
        const btnCancel = document.getElementById('btnCancel');
        const inputs = document.querySelectorAll('.editable');
        let originalValues = {};

        function toggleEditMode(editMode) {
            inputs.forEach(input => {
                if (editMode) {
                    originalValues[input.name] = input.value;
                    input.removeAttribute('readonly');
                } else {
                    input.setAttribute('readonly', true);
                    if (originalValues[input.name] !== undefined) {
                        input.value = originalValues[input.name];
                    }
                }
            });
            btnSave.classList.toggle('d-none', !editMode);
            btnCancel.classList.toggle('d-none', !editMode);
            btnEdit.classList.toggle('d-none', editMode);
        }

        if (btnEdit) {
            btnEdit.addEventListener('click', () => toggleEditMode(true));
        }

        if (btnCancel) {
            btnCancel.addEventListener('click', () => toggleEditMode(false));
        }

        if (btnSave) {
            btnSave.addEventListener("click", () => {
                document.getElementById("profileForm").submit();
            });
        }

        if (btnChange) {
            btnChange.addEventListener("click", () => {
                document.getElementById("passwordForm").submit();
            });
        }
    });
</script>
</body>
</html>
