<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <table class="table table-bordered fixed-table-height">
  <thead>
  <tr>
    <th>#</th>
    <th>Họ tên</th>
    <th>Email</th>
    <th>Phone</th>
    <th>Địa chỉ</th>
    <th>Ngày sinh</th>
    <th>Thao tác</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="u" items="${users}" varStatus="st">
    <tr>
      <td>${st.index + 1 + (page-1)*10}</td>
      <td>${u.username}</td>
      <td>${u.email}</td>
      <td>${u.phone}</td>
      <td>${u.address}</td>
      <td>${u.date_of_birth}</td>
      <td>
        <a href="<c:url value='/admin/${u.user_id}/profile'/>" class="btn btn-sm btn-primary btn-edit-user"
           data-user-id="${u.user_id}"
           data-username="${u.username}"
           data-email="${u.email}"
           data-phone="${u.phone}"
           data-address="${u.address}"
           data-dob="${u.date_of_birth}">
          Chỉnh sửa
        </a>
        <a class="btn btn-sm btn-danger btn-delete-user"
               href="<c:url value='/admin/${u.user_id}/delete'/>"
               onclick="return confirm('Bạn có chắc chắn muốn xoá người này?');">
        Xoá
        </a>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty users}">
    <tr><td colspan="6" class="text-center">Chưa có nhân viên</td></tr>
  </c:if>
  </tbody>
</table>
<nav>
  <ul class="pagination justify-content-center">
    <li class="page-item ${page == 1 ? 'disabled' : ''}">
      <a class="page-link" href="#" data-page="${page-1}">«</a>
    </li>
    <c:forEach begin="1" end="${pages}" var="i">
      <li class="page-item ${i == page ? 'active' : ''}">
        <a class="page-link" href="#" data-page="${i}">${i}</a>
      </li>
    </c:forEach>
    <li class="page-item ${page == pages ? 'disabled' : ''}">
      <a class="page-link" href="#" data-page="${page+1}">»</a>
    </li>
  </ul>
</nav>
