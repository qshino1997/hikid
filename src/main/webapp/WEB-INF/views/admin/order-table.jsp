<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="ordersTable">
  <table class="table table-bordered fixed-table-height">
    <thead>
    <tr>
      <th style="width: 30%;">Mã đơn</th>
      <th style="width: 10%;">Khách hàng</th>
      <th style="width: 20%;">Tổng tiền</th>
      <th style="width: 30%;">Ngày tạo</th>
      <th style="width: 10%;">Trạng thái</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orders}">
      <tr>
        <td>${order.payment_id}</td>
        <td>
          <a href="#" class="viewUserOrder"
               data-user-id="${order.user_id}"
               data-order-id="${order.order_id}">
            ${order.user_id}
          </a>
        </td>
        <td>${order.total_amount}</td>
        <td>${formattedDates[order.order_id]}</td>
        <td>${order.status}</td>
      </tr>
    </c:forEach>
    <c:if test="${empty orders}">
      <tr><td colspan="5" class="text-center">Không có đơn hàng</td></tr>
    </c:if>
    </tbody>
  </table>

  <!-- Phân trang Bootstrap -->
  <nav>
    <ul class="pagination justify-content-center">
      <li class="page-item ${page == 1 ? 'disabled' : ''}">
        <a class="page-link" href="#" data-page="${page - 1}">«</a>
      </li>

      <c:forEach begin="1" end="${pages}" var="i">
        <li class="page-item ${i == page ? 'active' : ''}">
          <a class="page-link" href="#" data-page="${i}">${i}</a>
        </li>
      </c:forEach>

      <li class="page-item ${page == pages ? 'disabled' : ''}">
        <a class="page-link" href="#" data-page="${page + 1}">»</a>
      </li>
    </ul>
  </nav>
</div>