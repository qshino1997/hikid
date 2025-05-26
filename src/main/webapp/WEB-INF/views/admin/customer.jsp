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
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>

