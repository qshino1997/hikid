<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Chi tiết sản phẩm - ${product.name}</title>

    <!-- Bootstrap CSS + Icons + Font -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet"/>
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
<!-- Banner & Header -->
<div class="banner mb-4"></div>
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>

<div class="container-fluid flex-grow-1 py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-white rounded-1 p-2">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">${product.category_name}</li>
        </ol>
    </nav>
    <c:if test="${not empty success}">
    <div class="alert alert-success" id="showAlert">${success}</div>
    </c:if>
    <c:if test="${not empty failed}">
    <div class="alert alert-danger" id="showAlert">${failed}</div>
    </c:if>
    <!-- Product Detail -->
    <div class="row p-4" style="background-color: white">
        <!-- Image -->
        <div class="col-md-6 text-center">
            <img src="${product.url}"
                 alt="${product.name}"
                 onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                 style="width:360px; height:360px; object-fit:contain;" />
        </div>
        <!-- Info -->
        <div class="col-md-6">
            <p class="text-muted">Thương hiệu: <strong>${product.manufacturer_name}</strong></p>
            <h2 class="fw-bold">${product.name}</h2>
            <p class="text-muted">Xuất xứ: <strong>${product.made_in}</strong></p>
            <h4 class="text-danger mb-3"> <fmt:formatNumber value="${product.price}" pattern="#,##0'đ'" /></h4>

            <!-- Stock -->
            <p>Kho còn: <strong>${product.stock}</strong> chiếc</p>

            <button
                    type="button"
                    class="btn btn-sm btn-primary js-add-to-cart"
                    data-product-id="${product.product_id}"
                    data-quantity="1"
                    data-product-name="${product.name}">
                <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
            </button>
        </div>
    </div>

    <!-- Sau phần Breadcrumb, thay thế đoạn tabs bằng bảng chi tiết -->
    <div class="row mt-4 p-4" style="background-color: white">
        <h3 class="mb-4">Chi Tiết Sản Phẩm</h3>
        <table class="table table-bordered">
            <tbody>
            <tr>
                <th style="width: 200px;">Tên sản phẩm</th>
                <td>${product.name}</td>
            </tr>
            <tr>
                <th>Thương hiệu</th>
                <td>${product.manufacturer_name}</td>
            </tr>
            <tr>
                <th>Xuất xứ thương hiệu</th>
                <td>${product.made_in}</td>
            </tr>
            <tr>
                <th>Trọng lượng sản phẩm</th>
                <td>${product.product_weight}</td>
            </tr>
            <tr>
                <th>Độ tuổi phù hợp</th>
                <td>
                    <c:choose>
                        <c:when test="${not empty product.appropriate_age_start}">
                            Từ ${product.appropriate_age_start} tháng
                            <c:if test="${not empty product.appropriate_age_end}">– ${product.appropriate_age_end} tháng</c:if>
                        </c:when>
                        <c:otherwise>Không xác định</c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <th>Cảnh báo</th>
                <td>${product.warning}</td>
            </tr>
            <tr>
                <th>Hướng dẫn sử dụng</th>
                <td>${product.instructions}</td>
            </tr>
            <tr>
                <th>Bảo quản</th>
                <td>${product.storage_instructions}</td>
            </tr>
            </tbody>
        </table>

</div>

<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/cart.js"></script>
<script>
// Định nghĩa biến toàn cục cho mọi script sau này dùng
window.ADD_TO_CART_URL = '${pageContext.request.contextPath}/cart/add';
</script>
</body>
</html>
