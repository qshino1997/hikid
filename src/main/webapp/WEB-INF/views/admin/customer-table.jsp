<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table class="table table-bordered fixed-table-height">
  <thead>
  <tr>
    <th>Họ tên</th>
    <th>Email</th>
    <th>Phone</th>
    <th>Địa chỉ</th>
    <th>Ngày sinh</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="u" items="${users}">
    <tr>
      <td>${u.username}</td>
      <td>${u.email}</td>
      <td>${u.phone}</td>
      <td>${u.address}</td>
      <td>${u.date_of_birth}</td>
    </tr>
  </c:forEach>
  <c:if test="${empty users}">
    <tr><td colspan="6" class="text-center">Khong co nguoi dung</td></tr>
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
