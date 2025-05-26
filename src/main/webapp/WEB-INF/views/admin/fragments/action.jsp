<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Edit User Modal -->
<div class="modal fade" id="editUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form:form method="post" modelAttribute="user" action="${pageContext.request.contextPath}/admin/update">
            <div class="modal-content">
                <div class="modal-header bg-warning">
                    <h5 class="modal-title">Sửa thông tin Khách hàng</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <form:hidden path="user_id"/>

                    <div class="mb-3">
                        <label class="form-label">Họ tên</label>
                        <form:input path="username" cssClass="form-control"/>
                        <form:errors path="username" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <form:input path="email" type="email" cssClass="form-control"/>
                        <form:errors path="email" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <form:input path="phone" cssClass="form-control"/>
                        <form:errors path="phone" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ</label>
                        <form:input path="address" cssClass="form-control"/>
                        <form:errors path="address" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ngày sinh</label>
                        <form:input path="date_of_birth" type="date" cssClass="form-control"/>
                        <form:errors path="date_of_birth" cssClass="text-danger"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-warning">Lưu thay đổi</button>
                </div>
            </div>
        </form:form>
    </div>
</div>
<!-- Delete Confirmation Modal -->
<div class="modal fade" id="deleteUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
        <form id="deleteUserForm" method="post" action="${pageContext.request.contextPath}/admin/user/delete">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">Xác nhận Xóa</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="id" id="deleteUserId"/>
                    <p>Bạn có chắc muốn xóa khách hàng <strong id="deleteUserName"></strong> không?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-danger">Xóa</button>
                </div>
            </div>
        </form>
    </div>
</div>
