<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Trang chủ Shop</title>
    <link href="<c:url value='/resources/css/chatbot.css'/>" rel="stylesheet" type="text/css"/>

</head>
<body class="d-flex flex-column min-vh-100">
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1">
    <c:if test="${param.error != null}">
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
            <div id="errorToast" class="toast align-items-center text-bg-danger border-0 show" role="alert" aria-live="assertive" aria-atomic="true"
                 data-bs-autohide="true"
                 data-bs-delay="2000">
                <div class="d-flex">
                    <div class="toast-body">
                        Tên đăng nhập hoặc mật khẩu không đúng!
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>
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
    <c:if test="${param.logout != null}">
        <div class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
            <div id="logoutToast" class="toast align-items-center text-bg-success border-0 show" role="alert" aria-live="assertive" aria-atomic="true"
                 data-bs-autohide="true"
                 data-bs-delay="2000">
                <div class="d-flex">
                    <div class="toast-body">
                        Bạn đã đăng xuất thành công!
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty success}">
        <div class="alert alert-success" id="showAlert">${success}</div>
    </c:if>
    <c:if test="${not empty failed}">
        <div class="alert alert-danger" id="showAlert">${failed}</div>
    </c:if>
    <div class="row">
        <!-- SIDEBAR -->
        <div class="col-md-3 col-lg-2 p-0">
            <jsp:include page="/WEB-INF/views/fragment/sidebar.jsp"/>
        </div>

        <!-- MAIN CONTENT -->
        <div class="col-md-9 col-lg-10 pt-4">
            <!-- BANNER/HEADER NHỎ -->
            <div class="bg-gradient-primary text-white text-center py-5 mb-4 position-relative overflow-hidden" style="background: linear-gradient(135deg, #43a047, #66bb6a);">
                <div class="container">
                    <h1 class="display-5 fw-bold mb-2">Hi Kids</h1>
                </div>
                <!-- Đường cong trang trí -->
                <svg viewBox="0 0 1440 100" style="position: absolute; bottom: 0; width: 100%; height: 60px;">
                    <path fill="#ffffff" fill-opacity="1"
                          d="M0,64L60,58.7C120,53,240,43,360,48C480,53,600,75,720,80C840,85,960,75,1080,64C1200,53,1320,43,1380,37.3L1440,32L1440,0L1380,0C1320,0,1200,0,1080,0C960,0,840,0,720,0C600,0,480,0,360,0C240,0,120,0,60,0L0,0Z">
                    </path>
                </svg>
            </div>

            <!-- Section Sữa -->
            <div class="section-block mb-5">
                <div class="section-header d-flex justify-content-between align-items-center mb-3">
                    <h4 class="section-title mb-0 " data-icon="🍼">Sữa</h4>
                    <a href="<c:url value='/product/3/list'/>"
                       class="btn btn-outline-primary btn-sm view-more-btn">
                        Xem thêm
                        <i class="bi bi-arrow-right-circle ms-1"></i>
                    </a>
                </div>

                <div class="product-grid">
                    <c:forEach var="prod" items="${milkList}">
                        <c:url var="detailUrl" value="/product/${prod.product_id}"/>
                        <div class="card product-card h-100">
                            <!-- Link & Image -->
                            <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                               class="text-decoration-none text-dark">
                                <img src="${prod.url}"
                                     class="card-img-top product-image"
                                     onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                                     alt="${prod.name}" />
                            </a>

                            <div class="card-body d-flex flex-column">
                                <!-- Tên & link chi tiết -->
                                <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                                   class="text-decoration-none text-dark mb-2">
                                    <h6 class="card-title mb-0">${prod.name}</h6>
                                </a>

                                <!-- Giá & nút thêm vào giỏ -->
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <div class="fs-5 fw-bold text-danger">
                                        <fmt:formatNumber value="${prod.price}" pattern="#,##0'đ'" />
                                    </div>
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary js-add-to-cart"
                                            data-product-id="${prod.product_id}"
                                            data-quantity="1"
                                            data-product-name="${prod.name}">
                                        <i class="bi bi-cart-plus"></i>
                                    </button>

                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- HÀNG 2: VITAMIN -->
            <div class="section-block mb-5">
                <div class="section-header d-flex justify-content-between align-items-center mb-3">
                    <h4 class="section-title mb-0" data-icon="💊">Vitamin</h4>
                    <a href="<c:url value='/product/4/list'/>"
                       class="btn btn-outline-primary btn-sm view-more-btn">
                        Xem thêm
                        <i class="bi bi-arrow-right-circle ms-1"></i>
                    </a>
                </div>

                <div class="product-grid">
                    <c:forEach var="prod" items="${vitaminList}">
                        <!-- Tạo URL chi tiết -->
                        <c:url var="detailUrl" value="/product/${prod.product_id}"/>
                        <div class="card product-card h-100">
                            <!-- Link & Image -->
                            <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                               class="text-decoration-none text-dark">
                                <img src="${prod.url}"
                                     class="card-img-top product-image"
                                     onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                                     alt="${prod.name}" />
                            </a>

                            <div class="card-body d-flex flex-column">
                                <!-- Tên & link chi tiết -->
                                <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                                   class="text-decoration-none text-dark mb-2">
                                    <h6 class="card-title mb-0">${prod.name}</h6>
                                </a>

                                <!-- Giá & nút thêm vào giỏ -->
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <div class="fs-5 fw-bold text-danger">
                                        <fmt:formatNumber value="${prod.price}" pattern="#,##0'đ'" />
                                    </div>
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary js-add-to-cart"
                                            data-product-id="${prod.product_id}"
                                            data-quantity="1"
                                            data-product-name="${prod.name}">
                                        <i class="bi bi-cart-plus"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- HÀNG 3: ĐỒ CHƠI -->
            <div class="section-block mb-5">
                <div class="section-header d-flex justify-content-between align-items-center mb-3">
                    <h4 class="section-title mb-0" data-icon="🧸">Đồ chơi</h4>
                    <a href="<c:url value='/product/1/list'/>"
                       class="btn btn-outline-primary btn-sm view-more-btn">
                        Xem thêm
                        <i class="bi bi-arrow-right-circle ms-1"></i>
                    </a>
                </div>

                <div class="product-grid">
                    <c:forEach var="prod" items="${toyList}">
                        <c:url var="detailUrl" value="/product/${prod.product_id}"/>
                        <div class="card product-card h-100">
                            <!-- Link & Image -->
                            <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                               class="text-decoration-none text-dark">
                                <img src="${prod.url}"
                                     class="card-img-top product-image"
                                     onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                                     alt="${prod.name}" />
                            </a>

                            <div class="card-body d-flex flex-column">
                                <!-- Tên & link chi tiết -->
                                <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                                   class="text-decoration-none text-dark mb-2">
                                    <h6 class="card-title mb-0">${prod.name}</h6>
                                </a>

                                <!-- Giá & nút thêm vào giỏ -->
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <div class="fs-5 fw-bold text-danger">
                                        <fmt:formatNumber value="${prod.price}" pattern="#,##0'đ'" />
                                    </div>
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary js-add-to-cart"
                                            data-product-id="${prod.product_id}"
                                            data-quantity="1"
                                            data-product-name="${prod.name}">
                                        <i class="bi bi-cart-plus"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- HÀNG 4: QUẦN ÁO -->
            <div class="section-block mb-5">
                <div class="section-header d-flex justify-content-between align-items-center mb-3">
                    <h4 class="section-title mb-0" data-icon="👕">Quần áo</h4>
                    <a href="<c:url value='/product/2/list'/>"
                       class="btn btn-outline-primary btn-sm view-more-btn">
                        Xem thêm
                        <i class="bi bi-arrow-right-circle ms-1"></i>
                    </a>
                </div>

                <div class="product-grid">
                    <c:forEach var="prod" items="${clothesList}">
                        <c:url var="detailUrl" value="/product/${prod.product_id}"/>
                        <div class="card product-card h-100">
                            <!-- Link & Image -->
                            <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                               class="text-decoration-none text-dark">
                                <img src="${prod.url}"
                                     class="card-img-top product-image"
                                     onerror="this.onerror=null; this.src='<c:url value='/resources/images/default.png'/>'"
                                     alt="${prod.name}" />
                            </a>

                            <div class="card-body d-flex flex-column">
                                <!-- Tên & link chi tiết -->
                                <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
                                   class="text-decoration-none text-dark mb-2">
                                    <h6 class="card-title mb-0">${prod.name}</h6>
                                </a>

                                <!-- Giá & nút thêm vào giỏ -->
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <div class="fs-5 fw-bold text-danger">
                                        <fmt:formatNumber value="${prod.price}" pattern="#,##0'đ'" />
                                    </div>
                                    <button
                                            type="button"
                                            class="btn btn-sm btn-primary js-add-to-cart"
                                            data-product-id="${prod.product_id}"
                                            data-quantity="1"
                                            data-product-name="${prod.name}">
                                        <i class="bi bi-cart-plus"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Chatbot Popup -->
<div id="mychatbot-popup" class="mychat-popup">
    <div class="mychat-header">
        <h5>Chat với Chatbot</h5>
        <button id="mychatbot-close" class="mychat-close-btn">X</button>
    </div>
    <div id="mychatbot-body" class="mychat-body">
        <!-- Tin nhắn chatbot sẽ xuất hiện ở đây -->
    </div>
    <div class="mychat-footer">
        <input id="mychat-input" type="text" placeholder="Nhập câu hỏi của bạn..." />
        <button id="mychat-send">Gửi</button>
    </div>
</div>

<!-- Open Chatbot Button -->
<button id="open-mychatbot" class="btn btn-info position-fixed bottom-0 end-0 mb-4 me-4">
    <i class="bi bi-chat-dots"></i>
</button>
<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/chatbot.js"></script>
<!-- Khi có param showLogin → tự động bật modal -->
<c:if test="${param.showLogin != null}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
            loginModal.show();
        });
    </script>
</c:if>
<c:if test="${openRegisterModal == true}">
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            new bootstrap.Modal(document.getElementById('registerModal')).show();
        });
    </script>
</c:if>
</body>
</html>
