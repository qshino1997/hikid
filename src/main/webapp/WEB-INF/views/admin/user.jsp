<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card mb-4">
  <div class="card-header bg-secondary text-white">
    <i class="bi bi-people"></i> Quản lý Người dùng
  </div>
  <div class="card-body">
    <table class="table table-hover">
      <thead>
      <tr>
        <th>Họ tên</th>
        <th>Email</th>
        <th>Số điện thoại</th>
        <th>Địa chỉ</th>
        <th>Ngày sinh</th>
        <th>Thao tác</th>
      </tr>
      </thead>
      <tbody>
      <jsp:useBean id="users" scope="request" type="java.util.List"/>
      <c:forEach var="u" items="${users}">
        <tr>
          <td>${u.username}</td>
          <td>${u.email}</td>
          <td>${u.phone}</td>
          <td>${u.address}</td>
          <td>${u.date_of_birth}</td>
          <td>
            <!-- Nút Sửa -->
            <a href="<c:url value='/admin/${u.user_id}/profile'/>">Chỉnh sửa</a>
            <!-- Nút Xóa -->
            <a class="btn btn-sm btn-danger"
               href="<c:url value='/admin/${u.user_id}/delete'/>"
               onclick="return confirm('Bạn có chắc chắn muốn xoá người này?');">
              Xoá
            </a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
    <!-- Pagination -->
<%--    <nav>--%>
<%--      <ul class="pagination justify-content-center">--%>
<%--        <li class="page-item ${page == 1 ? 'disabled' : ''}">--%>
<%--          <a class="page-link"--%>
<%--             href="<c:url value='/admin/user'>--%>
<%--                     <c:param name='page' value='${page-1}'/>--%>
<%--                     <c:param name='size' value='${size}'/>--%>
<%--                   </c:url>">«</a>--%>
<%--        </li>--%>

<%--        <c:forEach begin="1" end="${pages}" var="i">--%>
<%--          <li class="page-item ${i == page ? 'active' : ''}">--%>
<%--            <a class="page-link"--%>
<%--               href="<c:url value='/admin/user'>--%>
<%--                       <c:param name='page' value='${i}'/>--%>
<%--                       <c:param name='size' value='${size}'/>--%>
<%--                     </c:url>">${i}</a>--%>
<%--          </li>--%>
<%--        </c:forEach>--%>

<%--        <li class="page-item ${page == pages ? 'disabled' : ''}">--%>
<%--          <a class="page-link"--%>
<%--             href="<c:url value='/admin/user'>--%>
<%--                     <c:param name='page' value='${page+1}'/>--%>
<%--                     <c:param name='size' value='${size}'/>--%>
<%--                   </c:url>">»</a>--%>
<%--        </li>--%>
<%--      </ul>--%>
<%--    </nav>--%>

    <a href="<c:url value='/admin/create'/>" class="btn btn-primary">
      <i class="bi bi-plus-circle me-1"></i>Thêm Nhan Vien
    </a>
  </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
  $(function(){
    // Edit button
    $(document).on('click', '.btn-edit-user', function(){
      const btn = $(this);
      $('#editUserId').val(btn.data('user-id'));
      $('#editUsername').val(btn.data('username'));
      $('#editEmail').val(btn.data('email'));
      $('#editPhone').val(btn.data('phone'));
      $('#editAddress').val(btn.data('address'));
      $('#editDob').val(btn.data('dob'));
      $('#editUserModal').modal('show');
    });

    // Delete button
    $(document).on('click', '.btn-delete-user', function(){
      const btn = $(this);
      $('#deleteUserId').val(btn.data('user-id'));
      $('#deleteUserName').text(btn.data('username'));
      $('#deleteUserModal').modal('show');
    });
  });

  $(document).ready(function(){
    const urlParams = new URLSearchParams(window.location.search);
    const openModal = urlParams.get('openModal');
    if (openModal === 'editUserModal') {
      $('#editUserModal').modal('show');
    }
  });
</script>
