<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Giỏ hàng của bạn</title>
</head>
<body class="d-flex flex-column min-vh-100">
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
    <div id="cartToast" class="toast align-items-center text-bg-success border-0" role="alert" aria-live="assertive" aria-atomic="true"
         data-bs-autohide="true"
         data-bs-delay="2000">
        <div class="d-flex">
            <div class="toast-body">
            </div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>
<c:if test="${not empty failed}">
    <div class="alert alert-danger" id="showAlert">${failed}</div>
</c:if>
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1">
    <div class="row">
        <!-- MAIN CONTENT -->
        <h2 class="page-title">Giỏ hàng của bạn</h2>
        <c:choose>
            <c:when test="${empty cart.items}">
                <div class="alert alert-warning text-center">
                    <i class="bi bi-cart-x" style="font-size: 1.5rem;"></i>
                    <span class="ms-2">Giỏ hàng của bạn hiện chưa có sản phẩm.</span>
                </div>
                <div class="text-center mt-4">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-outline-primary">
                        <i class="bi bi-arrow-left-circle"></i> Tiếp tục mua sắm
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <form:form action="${pageContext.request.contextPath}/order/checkout" method="post" modelAttribute="orderForm" class="mb-4">
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Họ tên người nhận</label>
                        <div class="col-sm-9">
                            <form:input path="username" cssClass="form-control" />
                            <form:errors path="username" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Email</label>
                        <div class="col-sm-9">
                            <form:input path="email_v2" cssClass="form-control"/>
                            <form:errors path="email_v2" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Số điện thoại</label>
                        <div class="col-sm-9">
                            <form:input path="phone" cssClass="form-control"/>
                            <form:errors path="phone" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Địa chỉ giao hàng</label>
                        <div class="col-sm-9">
                            <form:textarea path="address" cssClass="form-control" rows="3"/>
                            <form:errors path="address" cssClass="text-danger"/>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-9 offset-sm-3 text-end">
                            <sec:authorize access="isAuthenticated()">
                                <!-- Đã đăng nhập -->
                                <button type="submit" class="btn btn-success">
                                        <i class="bi bi-cart-check me-1"></i> Tiến hành thanh toán
                                    </button>
                            </sec:authorize>
                            <sec:authorize access="isAnonymous()">
                                    <!-- Chưa đăng nhập -->
                                <span data-bs-toggle="tooltip"
                                      data-bs-placement="top"
                                      title="Bạn phải đăng nhập thì mới có thể tiến hành thanh toán">
                                    <button type="button" class="btn btn-success disabled">
                                        <i class="bi bi-cart-check me-1"></i> Tiến hành thanh toán
                                    </button>
                                </span>
                            </sec:authorize>
                        </div>
                    </div>
                </form:form>

                <form action="${pageContext.request.contextPath}/cart/update" method="post">
                    <div class="table-responsive mt-3">
                        <table class="table table-bordered align-middle cart-table bg-white shadow-sm">
                            <thead class="table-light">
                            <tr class="text-center">
                                <th scope="col">Sản phẩm</th>
                                <th scope="col">Hình ảnh</th>
                                <th scope="col">Đơn giá</th>
                                <th scope="col">Số lượng</th>
                                <th scope="col">Thành tiền</th>
                                <th scope="col">Hành động</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${cart.items}">
                                <tr>
                                    <!-- Tên sản phẩm (có link về chi tiết) -->
                                    <td>
                                        <a href="${pageContext.request.contextPath}/product/${item.product.product_id}"
                                           class="text-decoration-none text-dark fw-semibold">
                                                ${item.product.name}
                                        </a>
                                    </td>

                                    <!-- Hình ảnh sản phẩm -->
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/product/${item.product.product_id}">
                                            <img src="${item.product.url}"
                                                 alt="${item.product.name}"
                                                 style="width: 70px; height: auto; object-fit: cover;"
                                                 onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/resources/images/default.png';"/>
                                        </a>
                                    </td>

                                    <!-- Đơn giá -->
                                    <td class="text-center text-danger fw-bold">
                                        <fmt:formatNumber value="${item.product.price}" pattern="#,##0'₫'"/>
                                    </td>

                                    <!-- Số lượng (input + hidden productId) -->
                                    <td class="text-center">
                                        <div class="d-flex justify-content-center align-items-center">
                                            <input type="number" name="quantity"
                                                   value="${item.quantity}" min="1"
                                                   class="form-control form-control-sm text-center cart-qty-input"
                                                   style="width: 60px;"/>
                                            <input type="hidden" name="productId"
                                                   value="${item.product.product_id}"/>
                                        </div>
                                    </td>

                                    <!-- Thành tiền -->
                                    <td class="text-center text-success fw-bold subtotal-cell">
                                        <fmt:formatNumber value="${item.subTotal}" pattern="#,##0'₫'"/>
                                    </td>

                                    <!-- Nút xóa -->
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/cart/remove/${item.product.product_id}"
                                           class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash"></i> Xóa
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <!-- Tổng cộng và các nút hành động -->
                    <div class="row mt-4">
                        <div class="col-md-6 d-flex align-items-center">
                            <span class="cart-summary">Tổng cộng:
                                <span class="text-danger total-cell">
                                    <fmt:formatNumber value="${cart.total}" pattern="#,##0'₫'"/>
                                </span>
                            </span>
                        </div>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>
<script>
    window.UPDATE_TO_CART_URL = '${pageContext.request.contextPath}/cart/update';

    document.addEventListener("DOMContentLoaded", function () {
        var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.forEach(function (tooltipTriggerEl) {
            new bootstrap.Tooltip(tooltipTriggerEl);
        });

        $(document).on('change', '.cart-qty-input', function() {
            const $input = $(this);
            let quantity = parseInt($input.val(), 10);
            if (isNaN(quantity) || quantity < 1) {
                quantity = 1; // mặc định 1 nếu người dùng nhập thất thường
                $input.val(1);
            }

            // Lấy productId từ data-attribute của <tr>
            const $tr = $input.closest('tr');
            const productIdString = $tr.find("input[name='productId']").val();
            const productId = parseInt(productIdString, 10); // ép về số

            // Gửi AJAX cập nhật
            $.ajax({
                url: window.UPDATE_TO_CART_URL,
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    productId: productId,
                    quantity: quantity
                }),
                success: function(response) {
                    const cartCountEl = document.getElementById('cart-count');
                    if (cartCountEl) cartCountEl.textContent = response.totalQuantity;
                    $tr.find('.subtotal-cell')
                        .text(response.newSubtotal.toLocaleString('vi-VN') + '₫');
                    $('.total-cell').text(response.totalCell.toLocaleString('vi-VN') + '₫');
                },
                error: function(xhr, status, error) {
                    console.error("Lỗi AJAX:", error);
                    alert("Đã xảy ra lỗi khi cập nhật giỏ hàng.");
                }
            });
        });
    });
</script>
</body>
</html>
