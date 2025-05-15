<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<!-- Banner & Header -->
<div class="banner mb-4"></div>
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>

<div class="container-fluid flex-grow-1 py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-white rounded-1 p-2">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product/${category.id}/list">${category.name}</a></li>
            <li class="breadcrumb-item active" aria-current="page">${product.name}</li>
        </ol>
    </nav>

    <!-- Product Detail -->
    <div class="row p-4" style="background-color: white">
        <!-- Image -->
<%--        <div class="col-md-6 text-center">--%>
<%--            <c:if test="${not empty product.imageUrl}">--%>
<%--                <img src="${product.imageUrl}" class="img-fluid rounded" alt="${product.name}"/>--%>
<%--            </c:if>--%>
<%--        </div>--%>
        <!-- Info -->
        <div class="col-md-6">
            <p class="text-muted">Thương hiệu: <strong>${product.manufacturerName}</strong></p>
            <h2 class="fw-bold">${product.name}</h2>
            <p class="text-muted">Xuất xứ: <strong>${product.madeInFull}</strong></p>
            <h4 class="text-danger mb-3">${product.price}₫</h4>

            <!-- Stock -->
            <p>Kho còn: <strong>${product.stock}</strong> chiếc</p>

            <!-- Add to Cart -->
            <form action="${pageContext.request.contextPath}/cart/add" method="post" class="mt-4">
                <input type="hidden" name="productId" value="${product.product_id}"/>
                <div class="input-group mb-3" style="max-width: 140px;">
                    <span class="input-group-text">SL</span>
                    <input type="number" name="quantity" class="form-control" value="1" min="1" max="${product.stock}"/>
                </div>
                <button type="submit" class="btn btn-primary btn-lg">
                    <i class="bi-cart-plus me-1"></i> Thêm vào giỏ
                </button>
            </form>
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
                <td>${product.manufacturerName}</td>
            </tr>
            <tr>
                <th>Xuất xứ thương hiệu</th>
                <td>${product.madeInFull}</td>
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
<%--            <tr>--%>
<%--                <th>Nhà sản xuất</th>--%>
<%--                <td>--%>
<%--                    ${product.manufacturerAddress}--%>
<%--                </td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <th>Thành phần</th>--%>
<%--                <td>${product.ingredients}</td>--%>
<%--            </tr>--%>
<%--            <tr>--%>
<%--                <th>Công ty nhập khẩu</th>--%>
<%--                <td>${product.importerName}--%>
<%--                    <br/>${product.importerAddress}--%>
<%--                </td>--%>
<%--            </tr>--%>
            </tbody>
        </table>

</div>

<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
