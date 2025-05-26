<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="col-md-2 sidebar d-none d-md-block">
    <div class="position-sticky pt-3">
        <ul class="nav flex-column nav-pills" aria-orientation="vertical">
            <li class="nav-item">
                <a class="nav-link ${page == 'user' ? 'active' : ''}" href="<c:url value='/admin/user'/>">
                    <i class="bi bi-people-fill me-2"></i>Nhân viên
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${page == 'customer' ? 'active' : ''}" href="<c:url value='/admin/customer'/>">
                    <i class="bi bi-person-fill me-2"></i>Khách hàng
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${page == 'order' ? 'active' : ''}" href="<c:url value='/admin/order'/>">
                    <i class="bi bi-basket-fill me-2"></i>Đơn hàng
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link ${page == 'product' ? 'active' : ''}" href="<c:url value='/admin/product'/>">
                    <i class="bi bi-box-seam me-2"></i>Sản phẩm
                </a>
            </li>
        </ul>
    </div>
</div>