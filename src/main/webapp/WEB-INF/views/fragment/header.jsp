<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>
<!-- CSS/JS chung -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet"/>
<link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet">

<link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/image.css'/>" rel="stylesheet" type="text/css"/>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-primary" href="<c:url value='/'/>">Hi-Kids</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="mainNav">
            <form id="searchForm"
                  class="d-flex flex-grow-1 mx-3"
                  action="${pageContext.request.contextPath}/product/searchList"
                  method="get">
                <input class="form-control me-2"
                       type="search"
                       name="keyword"
                       id="searchKeyword"
                       value="${keyword}"
                       placeholder="Tìm sản phẩm, thương hiệu..."
                       aria-label="Search">
                <button class="btn btn-outline-primary" type="submit"><i class="bi bi-search"></i></button>
            </form>

            <ul class="navbar-nav mb-2 mb-lg-0 align-items-center">
                <li class="nav-item me-3 position-relative">
                    <a class="btn btn-outline-primary position-relative" href="/cart">
                        <i class="bi bi-cart3 fs-5"></i>
                        <span id="cart-count" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">${cartCount}</span>
                    </a>
                </li>

                <sec:authorize access="isAnonymous()">
                    <li class="nav-item">
                        <button class="btn btn-outline-secondary" data-bs-toggle="modal" data-bs-target="#loginModal">
                            <i class="bi bi-person"></i> Đăng nhập
                        </button>
                    </li>
                    <li class="nav-item ms-2">
                        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#registerModal">
                            <i class="bi bi-pencil-square"></i> Đăng ký
                        </button>
                    </li>
                </sec:authorize>

                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="userMenu" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            Xin chào <sec:authentication property="principal.username"/>
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userMenu">
                            <li><a class="dropdown-item" href="<c:url value='/user/profile'/>">Trang cá nhân</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/order/history'/>">Lịch sử đơn hàng</a></li>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="<c:url value='/logout'/>">Đăng xuất</a></li>
                        </ul>
                    </li>
                </sec:authorize>
            </ul>
        </div>
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
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Đăng nhập</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Modal Register -->
<div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <form:form action="${pageContext.request.contextPath}/register" method="post" modelAttribute="registerForm">
                <div class="modal-body">
                    <form:hidden path="user_id" cssClass="form-control" />
                    <div class="mb-3">
                        <label class="form-label">Họ tên</label>
                        <form:input path="username" cssClass="form-control" placeholder="Nhập họ tên" required="required"/>
                        <form:errors path="username" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <form:input path="email" type="email" cssClass="form-control" placeholder="Nhập email" required="required"/>
                        <form:errors path="email" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mật khẩu</label>
                        <form:password path="password" cssClass="form-control" placeholder="Tạo mật khẩu" required="required"/>
                        <form:errors path="password" cssClass="text-danger"/>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Xác nhận mật khẩu</label>
                        <form:password path="confirmPassword"
                                       cssClass="form-control"
                                       placeholder="Nhập lại mật khẩu"
                                       required="required"/>
                        <form:errors path="confirmPassword" cssClass="text-danger"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-success">Đăng ký</button>
                </div>
            </form:form>
            <div class="text-center my-3">
                <p>Hoặc</p>
                <a href="<c:url value='/oauth2/authorization/google'/>" class="btn btn-danger">
                    <i class="bi bi-google"></i> Đăng nhập với Google
                </a>
            </div>
        </div>
    </div>
</div>
