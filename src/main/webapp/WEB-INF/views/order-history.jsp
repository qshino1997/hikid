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
    <link href="<c:url value='/resources/css/order-history.css'/>" rel="stylesheet" type="text/css"/>
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
                        <c:if test="${order.status eq 'PAID'}">
                            <c:set var="review" value="${reviewMap[item.product.product_id]}" />
                            <c:choose>
                                <c:when test="${empty review}">
                                    <tr>
                                        <td colspan="7" class="py-3" style="background-color: #f8f9fa;">
                                            <form action="<c:url value='/order/items/${item.product.product_id}/review'/>"
                                                  method="post" class="row g-2 align-items-center review-form">
                                                <!-- Star‐rating -->
                                                <div class="col-auto">
                                                    <label class="form-label me-2"><strong>Đánh giá (1–5 sao):</strong></label>
                                                    <div class="star-rating d-inline-block">
                                                        <input type="radio" id="star5-${item.product.product_id}" name="rating" value="5" required/>
                                                        <label for="star5-${item.product.product_id}" title="5 sao">★</label>

                                                        <input type="radio" id="star4-${item.product.product_id}" name="rating" value="4"/>
                                                        <label for="star4-${item.product.product_id}" title="4 sao">★</label>

                                                        <input type="radio" id="star3-${item.product.product_id}" name="rating" value="3"/>
                                                        <label for="star3-${item.product.product_id}" title="3 sao">★</label>

                                                        <input type="radio" id="star2-${item.product.product_id}" name="rating" value="2"/>
                                                        <label for="star2-${item.product.product_id}" title="2 sao">★</label>

                                                        <input type="radio" id="star1-${item.product.product_id}" name="rating" value="1"/>
                                                        <label for="star1-${item.product.product_id}" title="1 sao">★</label>
                                                    </div>
                                                </div>

                                                <div class="col-6">
                            <textarea name="comment"
                                      class="form-control"
                                      rows="2"
                                      placeholder="Viết nhận xét về sản phẩm..."
                                      maxlength="500"></textarea>
                                                </div>

                                                <div class="col-auto">
                                                    <button type="submit" class="btn btn-primary">Gửi</button>
                                                </div>
                                            </form>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="text-success py-3" style="background-color: #f8f9fa;">
                                            <i class="bi bi-check-circle-fill me-1"></i> Cảm ơn bạn đã đánh giá sản phẩm này.
                                            <br/>
                                            <strong>Đánh giá của bạn:</strong> ${review.content}
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:forEach>
    </c:if>
</div>

<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>
<script>
    $(document).ready(function () {
        $(".review-form").on("submit", function (event) {
            event.preventDefault();

            const $form = $(this);
            const url = $form.attr("action");
            const rating = $form.find("input[name='rating']:checked").val();
            const comment = $form.find("textarea[name='comment']").val();

            if (!rating) {
                alert("Vui lòng chọn số sao (rating) trước khi gửi.");
                return;
            }

            const payload = {
                rating: rating,
                comment: comment
            };

            $.ajax({
                url: url,
                method: "POST",
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function (data) {
                    if (data.success) {
                        const $td = $form.closest("td[colspan]");
                        if ($td.length) {
                            $td.html(`
                                <div class="text-success">
                                    <i class="bi bi-check-circle-fill me-1"></i>
                                    Cảm ơn bạn đã đánh giá sản phẩm này!
                                    <br/>
                                    <strong>Đánh giá của bạn:</strong> <span class="user-comment"></span>
                                </div>
                            `);
                            $td.find(".user-comment").text(data.comment || "");
                        }
                    } else {
                        alert(data.message || "Không thể gửi đánh giá. Vui lòng thử lại.");
                    }
                },
                error: function () {
                    alert("Đã xảy ra lỗi khi gửi đánh giá.");
                }
            });
        });
    });

</script>
</body>
</html>