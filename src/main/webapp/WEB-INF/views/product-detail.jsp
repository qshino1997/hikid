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
    <link href="<c:url value='/resources/css/product-detail.css'/>" rel="stylesheet" type="text/css"/>

    <style>
        body {
            background-color: #f0f0f0; /* Màu nền tổng thể */
            font-family: 'Quicksand', sans-serif;
        }
        .section-white {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 2px 6px rgba(0,0,0,0.05);
        }
        .product-image {
            width: 100%;
            max-width: 360px;
            height: 360px;
            object-fit: contain;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: #fafafa;
        }
        .table-detail th {
            width: 200px;
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="d-flex flex-column min-vh-100 bg-light">

<!-- Toast Notification -->
<div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
    <div id="cartToast" class="toast align-items-center text-bg-success border-0" role="alert"
         aria-live="assertive" aria-atomic="true"
         data-bs-autohide="true"
         data-bs-delay="2000">
        <div class="d-flex">
            <div class="toast-body"></div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto"
                    data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>

<!-- Main Content -->
<div class="container flex-grow-1 py-4">
    <!-- Breadcrumb -->
    <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-light rounded-1 p-2">
            <li class="breadcrumb-item">
                <a href="${pageContext.request.contextPath}/">Trang chủ</a>
            </li>
            <li class="breadcrumb-item active" aria-current="page">
                ${product.category_name}
            </li>
        </ol>
    </nav>

    <!-- Thông báo thành công / thất bại -->
    <c:if test="${not empty success}">
        <div class="alert alert-success" id="showAlert">${success}</div>
    </c:if>
    <c:if test="${not empty failed}">
        <div class="alert alert-danger" id="showAlert">${failed}</div>
    </c:if>

    <!-- Product Detail (Màu nền trắng, bo góc, shadow) -->
    <div class="row p-4 section-white">
        <!-- Image -->
        <div class="col-md-6 text-center mb-3 mb-md-0">
            <img src="${product.url}"
                 alt="${product.name}"
                 onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                 style="width:360px; height:360px; object-fit:contain;" />
        </div>
        <!-- Info -->
        <div class="col-md-6">
            <h2 class="fw-bold mb-3">${product.name}</h2>
            <p class="text-muted">
                Thương hiệu: <strong>${product.manufacturer_name}</strong> |
                Xuất xứ: <strong>${product.made_in}</strong>
            </p>
            <h4 class="text-danger mb-3">
                <fmt:formatNumber value="${product.price}" pattern="#,##0'đ'"/>
            </h4>
            <hr class="my-3"/>
            <div class="d-flex align-items-center mb-3">
                <button type="button" class="btn btn-sm btn-outline-secondary btn-decrease">
                    <i class="bi bi-dash"></i>
                </button>
                <input
                        type="text"
                        class="form-control form-control-sm text-center js-quantity mx-1"
                        value="1"
                        data-max="50"
                        style="width: 60px;"
                />
                <button type="button" class="btn btn-sm btn-outline-secondary btn-increase">
                    <i class="bi bi-plus"></i>
                </button>
            </div>
            <button type="button"
                    class="btn btn-primary js-add-to-cart"
                    data-product-id="${product.product_id}"
                    data-quantity="1"
                    data-product-name="${product.name}">
                <i class="bi bi-cart-plus"></i> Thêm vào giỏ hàng
            </button>
        </div>
    </div>
    <div class="row p-2 bg-light"></div>
    <div class="row p-4 section-white">
        <h3 class="mb-4">Sản phẩm tương tự</h3>
        <div class="similar-products-container">
            <div class="similar-products-track">
                <c:forEach items="${similarProducts}" var="sp">
                    <div class="card similar-card">
                        <a href="${pageContext.request.contextPath}/product/${sp.product_id}">
                            <img src="${sp.url}"
                                 class="card-img-top"
                                 alt="${sp.name}"
                                 onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png' />';" />
                        </a>
                        <div class="card-body text-center">
                            <a href="${pageContext.request.contextPath}/product/${sp.product_id}"
                               class="card-title d-block text-decoration-none text-dark"
                               title="${sp.name}">
                                    ${sp.name}
                            </a>
                            <p class="card-price mb-0">
                                <fmt:formatNumber value="${sp.price}" pattern="#,##0'đ'" />
                            </p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="row p-2 bg-light"></div>
    <!-- Chi Tiết Sản Phẩm (Màu nền trắng, bo góc, shadow) -->
    <div class="row p-4 section-white">
        <h3 class="mb-4">Chi Tiết Sản Phẩm</h3>
        <div class="table-responsive">
            <table class="table table-bordered table-detail mb-0">
                <tbody>
                <tr>
                    <th>Tên sản phẩm</th>
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
                                <c:if test="${not empty product.appropriate_age_end}">
                                    – ${product.appropriate_age_end} tháng
                                </c:if>
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
    </div>

</div>

<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>

<script>
    // Định nghĩa biến toàn cục cho mọi script sau này dùng
    window.ADD_TO_CART_URL = '${pageContext.request.contextPath}/cart/add';

    document.addEventListener('DOMContentLoaded', function() {

        document.body.addEventListener('input', function(e) {
            const input = e.target;
            if (!input.classList.contains('js-quantity')) return;

            // Chỉ giữ lại ký tự số (0-9)
            let cleaned = input.value.replace(/\D/g, '');
            if (cleaned === '') {
                // Nếu xóa hết, đặt thành 1 tạm thời (hoặc bạn có thể để '' để người dùng tiếp tục nhập)
                cleaned = '1';
            }
            // Chuyển về số nguyên
            let num = parseInt(cleaned, 10);
            if (isNaN(num) || num < 1) {
                num = 1;
            }

            // Lấy giới hạn tối đa từ data-max
            const maxStock = parseInt(input.dataset.max, 10) || num;
            if (num > maxStock) {
                num = maxStock;
            }

            // Cập nhật lại giá trị sau khi lọc
            input.value = num;
        });

        const btnDec = document.querySelector('.btn-decrease');
        const btnInc = document.querySelector('.btn-increase');
        const qtyInput = document.querySelector('.js-quantity');
        const maxStock = parseInt(qtyInput.getAttribute('data-max'), 10);

        btnDec.addEventListener('click', () => {
            let current = parseInt(qtyInput.value) || 1;
            if (current > 1) {
                qtyInput.value = current - 1;
            }
        });
        btnInc.addEventListener('click', () => {
            let current = parseInt(qtyInput.value) || 1;
            if (current < maxStock) {
                qtyInput.value = current + 1;
            }
        });

        // Khi click “Thêm vào giỏ hàng”, cập nhật data-quantity của button
        const addBtn = document.querySelector('.js-add-to-cart');
        addBtn.addEventListener('click', () => {
            let chosen = parseInt(qtyInput.value) || 1;
            if (chosen > maxStock) {
                chosen = maxStock;
                qtyInput.value = maxStock;
            }
            addBtn.setAttribute('data-quantity', chosen);
            // Giờ cart.js sẽ đọc đúng data-quantity khi thêm vào giỏ
        });

        const track = document.querySelector('.similar-products-track');
        if (!track) return;

        let cardWidth = track.querySelector('.similar-card')?.offsetWidth || 300;
        let scrollInterval = 2000; // dừng 2 giây mỗi lần scroll
        let step = cardWidth + 16; // card + gap (1rem = 16px)

        function scrollNextCard() {
            // Nếu gần đến cuối → quay lại đầu
            if (track.scrollLeft + track.clientWidth >= track.scrollWidth - step) {
                track.scrollTo({ left: 0, behavior: 'smooth' });
            } else {
                track.scrollBy({ left: step, behavior: 'smooth' });
            }
        }

        let interval = setInterval(scrollNextCard, scrollInterval);

        // Tạm dừng khi hover
        track.addEventListener('mouseenter', () => clearInterval(interval));
        track.addEventListener('mouseleave', () => {
            interval = setInterval(scrollNextCard, scrollInterval);
        });
    });
</script>
</body>
</html>
