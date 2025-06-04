<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Thanh toán</title>
</head>
<body class="d-flex flex-column min-vh-100">
<!-- HEADER -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>

<div class="container my-4">
  <h3 class="mb-4">Xác nhận đơn hàng</h3>

  <c:if test="${empty cartItems}">
    <div class="alert alert-warning">Giỏ hàng của bạn đang trống.</div>
  </c:if>

  <c:if test="${not empty cartItems}">
    <!-- Hiển thị sản phẩm trong giỏ hàng -->
    <div class="table-responsive mb-4">
      <table class="table table-bordered align-middle">
        <thead class="table-light">
        <tr>
          <th style="width: 100px;">Hình ảnh</th>
          <th>Tên sản phẩm</th>
          <th style="width: 100px;">Giá</th>
          <th style="width: 100px;">Số lượng</th>
          <th style="width: 140px;">Thành tiền</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${cartItems}">
          <tr>
            <td class="text-center">
              <img src="${item.product.image.url}"
                   alt="${item.product.name}"
                   class="img-thumbnail"
                   style="width: 80px; height: 80px; object-fit: cover;"
                   onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
              />
            </td>
            <td>${item.product.name}</td>
            <td><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0'đ'"/></td>
            <td class="text-center">${item.quantity}</td>
            <td>
              <fmt:formatNumber value="${item.unitPrice * item.quantity}" pattern="#,##0'đ'"/>
            </td>
          </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
          <th colspan="4" class="text-end">Tổng cộng:</th>
          <th>
            <fmt:formatNumber value="${totalAmount}" pattern="#,##0'đ'"/>
          </th>
        </tr>
        </tfoot>
      </table>
    </div>

    <!-- Form nhập thông tin giao hàng -->
    <form:form action="${pageContext.request.contextPath}/checkout/confirm" method="post" modelAttribute="orderForm">
      <div class="mb-3">
        <label class="form-label">Họ tên người nhận</label>
        <form:input path="receiverName" cssClass="form-control" required="true"/>
        <form:errors path="receiverName" cssClass="text-danger"/>
      </div>
      <div class="mb-3">
        <label class="form-label">Số điện thoại</label>
        <form:input path="phoneNumber" cssClass="form-control" required="true"/>
        <form:errors path="phoneNumber" cssClass="text-danger"/>
      </div>
      <div class="mb-3">
        <label class="form-label">Địa chỉ giao hàng</label>
        <form:textarea path="shippingAddress" cssClass="form-control" rows="3" required="true"/>
        <form:errors path="shippingAddress" cssClass="text-danger"/>
      </div>
      <div class="text-end">
        <button type="submit" class="btn btn-success">
          <i class="bi bi-cart-check me-1"></i> Đặt hàng
        </button>
      </div>
    </form:form>
  </c:if>
</div>

<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>
</body>
</html>
