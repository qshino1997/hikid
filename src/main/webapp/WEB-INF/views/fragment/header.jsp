<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>

<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom sticky-top py-3 px-2">
    <div class="container-fluid">
        <a class="navbar-brand" href="<c:url value="/"></c:url> ">Hi-Kids</a>
        <!-- left menu -->
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item"><a class="nav-link" href="#">Giới thiệu</a></li>
            <li class="nav-item"><a class="nav-link" href="#">Khuyến mãi</a></li>
        </ul>
        <!-- search -->
        <form class="d-flex mx-lg-4 flex-grow-1">
            <input class="form-control me-2" type="search" placeholder="Tìm sản phẩm, thương hiệu..." />
            <button class="btn btn-outline-primary" type="submit"><i class="bi-search"></i></button>
        </form>
        <!-- cart & account -->
        <ul class="navbar-nav mb-2 mb-lg-0">
            <li class="nav-item me-3">
                <a class="nav-link position-relative" href="/cart"><i class="bi-cart3 fs-5"></i>
                    <span id="cart-count"
                          class="badge bg-danger position-absolute top-0 start-100 translate-middle">
                        ${cart.totalQuantity}
                    </span>
                </a>
            </li>
            <!-- Nếu chưa login -->
            <sec:authorize access="isAnonymous()">
                <li class="nav-item">
                    <a class="nav-link" href="#"
                       data-bs-toggle="modal"
                       data-bs-target="#loginModal">
                        <i class="bi-person fs-5"></i>
                    </a>
                </li>
            </sec:authorize>

            <!-- Nếu đã login -->
            <sec:authorize access="isAuthenticated()">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userMenu" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        Xin chào <sec:authentication property="principal.username"/>
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
                        <li><a class="dropdown-item" href="<c:url value='/user/home'/>">Trang cá nhân</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="<c:url value='/logout'/>">Đăng xuất</a></li>
                    </ul>
                </li>
            </sec:authorize>
        </ul>

    </div>
</nav>
<!-- spacer for fixed header -->
<div style="height:10px;"></div>

<!-- Modal Login -->
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form action="<c:url value='/user/login'/>" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="loginModalLabel">Đăng nhập</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                </div>
                <div class="modal-body">
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="text" class="form-control" id="email" name="email"
                               placeholder="Nhập tên tài khoản" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu</label>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="Nhập mật khẩu" required>
                    </div>
<%--                    <c:if test="${param.error != null}">--%>
<%--                        <div class="alert alert-danger py-2" role="alert">--%>
<%--                            Tên đăng nhập hoặc mật khẩu không đúng!--%>
<%--                        </div>--%>
<%--                    </c:if>--%>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Đăng nhập</button>
                </div>
            </form>
        </div>
    </div>
</div>
