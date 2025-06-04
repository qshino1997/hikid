<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Lịch sử mua hàng</title>
</head>
<body class="d-flex flex-column min-vh-100">
<!-- INCLUDE HEADER -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>

<!-- NỘI DUNG CHÍNH -->
<div class="container my-4">
    <h3 class="mb-4">Đơn hàng của tôi</h3>

    <!-- Nếu không có order nào -->
    <c:if test="${empty orderList}">
        <div class="alert alert-info">
            Bạn chưa có đơn hàng nào.
        </div>
    </c:if>

    <!-- Nếu có ít nhất 1 order -->
    <c:if test="${not empty orderList}">
        <!-- Duyệt qua từng order trong orderList -->
        <c:forEach var="order" items="${orderList}" varStatus="loopOrder">
            <!-- Tiêu đề cho mỗi order = ngày giờ tạo -->
            <div class="mb-2">
                <h5 class="fw-bold">
                    <i class="bi bi-calendar-event me-1"></i>
                    Ngày tạo đơn: ${order.createdAtFormatted}
                </h5>
            </div>

            <!-- Bắt đầu bảng liệt kê OrderItem của order hiện tại -->
            <div class="table-responsive mb-4">
                <table class="table table-bordered align-middle">
                    <thead class="table-light">
                    <tr>
                        <th scope="col" style="width: 100px;">Hình ảnh</th>
                        <th scope="col">Tên sản phẩm</th>
                        <th scope="col" style="width: 100px;">Giá đơn vị</th>
                        <th scope="col" style="width: 100px;">Số lượng</th>
                        <th scope="col" style="width: 140px;">Thành tiền</th>
                        <th scope="col" style="width: 100px;">Trạng thái đơn hàng </th>
                    </tr>
                    </thead>
                    <tbody>
                    <!-- Nếu order này không có item nào -->
                    <c:if test="${empty order.items}">
                        <tr>
                            <td colspan="5" class="text-center text-muted py-3">
                                Đơn hàng này chưa có sản phẩm.
                            </td>
                        </tr>
                    </c:if>

                    <!-- Duyệt từng OrderItem -->
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <!-- 1. Hình ảnh sản phẩm -->
                            <td class="align-middle text-center">
                                <img src="${item.product.image.url}"
                                     alt="${item.product.name}"
                                     class="img-thumbnail"
                                     style="width: 80px; height: 80px; object-fit: cover;"
                                     onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                                />
                            </td>

                            <!-- 2. Tên sản phẩm -->
                            <td class="align-middle">
                                    ${item.product.name}
                            </td>

                            <!-- 3. Giá đơn vị -->
                            <td class="align-middle">
                                <fmt:formatNumber value="${item.unit_price}" pattern="#,##0'đ'" />
                            </td>

                            <!-- 4. Số lượng -->
                            <td class="align-middle text-center">
                                    ${item.quantity}
                            </td>

                            <!-- 5. Thành tiền = đơn giá * số lượng -->
                            <td class="align-middle">
                                <fmt:formatNumber
                                        value="${item.unit_price.multiply(item.quantity)}"
                                        pattern="#,##0'đ'" />
                            </td>
                            <td class="align-middle">
                                    ${item.order.status}
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <!-- Kết thúc table liệt kê items của order hiện tại -->
        </c:forEach>
    </c:if>
</div>

<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

</body>
</html>