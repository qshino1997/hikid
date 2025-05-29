<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />

<div class="product-grid">
  <c:forEach var="prod" items="${products}">
    <c:url var="detailUrl" value="/product/${prod.product_id}"/>
    <div class="card product-card h-100">
      <!-- Link & Image -->
      <a href="${pageContext.request.contextPath}/product/${prod.product_id}"
         class="text-decoration-none text-dark">
        <img src="<c:url value='/resources/images/${prod.url}'/>"
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
          <form action="${pageContext.request.contextPath}/cart/add"
                method="post" class="mb-0">
            <input type="hidden" name="productId" value="${prod.product_id}" />
            <input type="hidden" name="categoryId" value="${category.id}" />
            <input type="hidden" name="quantity" value="1" />
            <input type="hidden" name="mode" value="3" />
            <button type="submit" class="btn btn-sm btn-primary">
              <i class="bi bi-cart-plus"></i>
            </button>
          </form>
        </div>
      </div>
    </div>
  </c:forEach>
</div>

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