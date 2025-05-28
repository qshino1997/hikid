<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<table id="categoriesTable" class="table table-bordered fixed-table-height">
    <thead>
    <tr>
        <th>ID</th>
        <th>Tên</th>
        <th>Danh mục cha</th>
        <th>Thao tác</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${categories}">
        <tr data-id="${c.id}">
            <td>${c.id}</td>
            <td class="editable name">${c.name}</td>
            <td class="parent" data-parent-id="${c.parent != null ? c.parent.id : ''}">
                <span class="text-parent">${c.parent != null ? c.parent.name : ''}</span>
                <select class="form-select form-select-sm d-none select-parent" name="parentId">
                    <option value=""></option>
                    <c:forEach var="opt" items="${parents}">
                        <option value="${opt.id}" <c:if test="${c.parent != null and opt.id == c.parent.id}">selected</c:if>>
                                ${opt.label.trim()}
                        </option>
                    </c:forEach>
                </select>
            </td>
            <td>
                <a href="<c:url value='/admin/category/${c.id}/update'/>"
                   class="btn btn-sm btn-primary">Sửa</a>
                <a href="<c:url value='/admin/category/${c.id}/delete'/>" class="btn btn-sm btn-danger btn-delete"
                   onclick="return confirm('Xác nhận xóa danh mục?');">Xóa</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty categories}"><tr><td colspan="4" class="text-center">Chưa có danh mục</td></tr></c:if>
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

