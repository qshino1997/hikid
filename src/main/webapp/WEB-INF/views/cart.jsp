<%--
  Created by IntelliJ IDEA.
  User: Quang
  Date: 5/12/2025
  Time: 12:02 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><html>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Giỏ hàng của bạn</title>

    <!-- CSS/JS chung -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet">

    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>
</head>
<body class="d-flex flex-column min-vh-100">
<!-- Banner  -->
<div class="banner"></div>
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1">
    <div class="row">
        <!-- MAIN CONTENT -->
        <h2>Giỏ hàng</h2>
        <c:choose>
            <c:when test="${empty cart.items}">
                <p>Giỏ hàng trống.</p>
            </c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/cart/update" method="post">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Sản phẩm</th><th>Đơn giá</th>
                            <th>Số lượng</th><th>Thành tiền</th><th>Hành động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${cart.items}">
                            <tr>
                                <td>${item.product.name}</td>
                                <td>${item.product.price}₫</td>
                                <td>
                                    <input type="number" name="quantity"
                                           value="${item.quantity}" min="1"
                                           style="width:60px;"/>
                                    <input type="hidden" name="productId"
                                           value="${item.product.product_id}"/>
                                </td>
                                <td>${item.subTotal}₫</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/cart/remove/${item.product.product_id}"
                                       class="btn btn-sm btn-danger">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <p><strong>Tổng cộng:</strong> ${cart.total}₫</p>
                    <button type="submit" class="btn btn-primary">Cập nhật giỏ</button>
                    <a href="${pageContext.request.contextPath}/checkout.html"
                       class="btn btn-success">Tiến hành thanh toán</a>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<script src="/resources/js/cart.js"></script>
</body>
</html>
