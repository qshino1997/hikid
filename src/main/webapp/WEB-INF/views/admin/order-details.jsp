<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div>
    <h6>Thông tin người dùng</h6>
    <p><strong>Họ tên:</strong> ${user.username}</p>
    <p><strong>Email:</strong> ${user.email}</p>
    <p><strong>SĐT:</strong> ${user.profile.phone}</p>
    <p><strong>Địa chỉ:</strong> ${user.profile.address}</p>

    <hr>

    <h6>Danh sách sản phẩm trong đơn hàng</h6>
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Sản phẩm</th>
            <th>Số lượng</th>
            <th>Đơn giá</th>
            <th>Tổng</th>
            <th>Trạng thái đơn hàng</th>
            <th>Ngày mua hàng</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${orderItem}">
            <tr>
                <td>${item.product.name}</td>
                <td>${item.quantity}</td>
                <td>${item.unit_price}</td>
                <td>${item.unit_price * item.quantity}</td>
                <td>${item.order.status}</td>
                <td>${item.order.created_at}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
