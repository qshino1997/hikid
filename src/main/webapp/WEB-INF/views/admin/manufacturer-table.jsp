<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table table-bordered">
  <thead>
  <tr>
    <th>#</th>
    <th>Tên</th>
    <th>Quốc gia</th>
    <th>Website</th>
    <th>Đánh giá</th>
    <th>Hành động</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="m" items="${manufacturers}" varStatus="st">
    <tr>
      <td>${st.index + 1 + (page-1)*10}</td>
      <td>${m.name}</td>
      <td>${m.country}</td>
      <td><a href="${m.website}" target="_blank">${m.website}</a></td>
      <td>${m.rating}</td>
      <td>
        <a href="<c:url value='/admin/manufacturer/${m.id}/edit'/>"
           class="btn btn-sm btn-primary">Sửa</a>
        <form action="<c:url value='/admin/manufacturer/${m.id}/delete'/>"
              method="post" style="display:inline;"
              onsubmit="return confirm('Xác nhận xóa?');">
          <button class="btn btn-sm btn-danger">Xóa</button>
        </form>
      </td>
    </tr>
  </c:forEach>
  <c:if test="${empty manufacturers}">
    <tr><td colspan="6" class="text-center">Chưa có nhà sản xuất nào</td></tr>
  </c:if>
  </tbody>
</table>

<nav>
  <ul class="pagination justify-content-center">
    <li class="page-item ${page==1?'disabled':''}">
      <a class="page-link" href="#" data-page="${page-1}">«</a>
    </li>
    <c:forEach begin="1" end="${pages}" var="i">
      <li class="page-item ${i==page?'active':''}">
        <a class="page-link" href="#" data-page="${i}">${i}</a>
      </li>
    </c:forEach>
    <li class="page-item ${page==pages?'disabled':''}">
      <a class="page-link" href="#" data-page="${page+1}">»</a>
    </li>
  </ul>
</nav>
