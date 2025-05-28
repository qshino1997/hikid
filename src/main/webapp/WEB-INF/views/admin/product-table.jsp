<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table class="table table-bordered fixed-table-height-product">
    <thead>
    <tr>
        <th>Tên</th>
        <th>Xuất xứ</th>
        <th>Khối lượng</th>
        <th>Giá</th>
        <th>Độ tuổi phù hợp</th>
        <th>Tồn kho</th>
        <th>Danh mục</th>
        <th>Nhà sản xuất</th>
        <th>Thao tác</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="p" items="${products}">
        <tr>
            <td>${p.name}</td>
            <td>${p.made_in}</td>
            <td>${p.product_weight}</td>
            <td>${p.price}</td>
            <td>${p.appropriate_age_start} - ${p.appropriate_age_end}</td>
            <td>${p.stock}</td>
            <td>${p.category != null ? p.category.name : ''}</td>
            <td>${p.manufacturer != null ? p.manufacturer.name : ''}</td>
            <td>
                <a href="<c:url value='/admin/product/${p.product_id}/edit'/>"
                   class="btn btn-sm btn-primary">Sửa</a>
                <a href="<c:url value='/admin/product/${p.product_id}/delete'/>"
                   class="btn btn-sm btn-danger"
                   onclick="return confirm('Xác nhận xóa sản phẩm này?');">
                    Xóa
                </a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty products}">
        <tr><td colspan="9" class="text-center">Chưa có sản phẩm</td></tr>
    </c:if>
    </tbody>
</table>

<nav>
    <ul class="pagination justify-content-center">
        <li class="page-item ${page == 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page - 1}">«</a>
        </li>
        <c:forEach begin="1" end="${pages}" var="i">
            <li class="page-item ${i == page ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i}</a>
            </li>
        </c:forEach>
        <li class="page-item ${page == pages ? 'disabled' : ''}">
            <a class="page-link" href="#" data-page="${page + 1}">»</a>
        </li>
    </ul>
</nav>
