<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Danh sách sản phẩm</title>

  <!-- Bootstrap CSS + Icons + Font -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet"/>
</head>
<body class="d-flex flex-column min-vh-100">
<!-- Banner -->
<div class="banner"></div>
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1 py-4">
  <!-- Breadcrumb -->
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb bg-white rounded-1 p-2">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
      <li class="breadcrumb-item active" aria-current="page">
        ${subCategory.name}
      </li>
    </ol>
  </nav>
  <!-- Toast container, nằm top-right -->
  <div aria-live="polite" aria-atomic="true"
       class="position-fixed top-0 end-0 p-3" style="z-index: 1080;">
    <div id="cartToast" class="toast align-items-center text-bg-success border-0" role="alert"
         aria-live="assertive" aria-atomic="true">
      <div class="d-flex">
        <div class="toast-body">
          Thêm vào giỏ hàng thành công!
        </div>
        <button type="button" class="btn-close btn-close-white me-2 m-auto"
                data-bs-dismiss="toast" aria-label="Close"></button>
      </div>
    </div>
  </div>
  <!-- Product Grid -->
  <div class="row g-4 row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4">
    <c:forEach var="p" items="${products}">
      <div class="col">
        <div class="card h-100 product-card">
<%--          <c:if test="${not empty p.imageUrl}">--%>
<%--            <img src="${p.imageUrl}" class="card-img-top" alt="${p.name}"/>--%>
<%--          </c:if>--%>
          <div class="card-body text-center">
            <h6 class="card-title fw-semibold">${p.name}</h6>
            <p class="text-danger fw-bold mb-0">${p.price}</p>
          </div>
        <div class="card-footer bg-transparent border-0 text-center d-flex justify-content-around">
          <!-- Nút Xem chi tiết -->
          <a href="${pageContext.request.contextPath}/product/${p.product_id}?subCategoryId=${subCategory.id}"
             class="btn btn-outline-primary btn-sm">
            Xem chi tiết
          </a>

          <!-- Form Thêm vào giỏ hàng -->
          <button
                  class="btn btn-primary btn-sm js-add-to-cart"
                  data-product-id="${p.product_id}"
                  data-product-name="${p.name}"
                  type="button">
            <i class="bi-cart-plus"></i> Thêm giỏ
          </button>
        </div>
        </div>
      </div>
    </c:forEach>
  </div>

</div>
<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script type="text/javascript">
  // Sinh URL đúng với contextPath
  window.ADD_TO_CART_URL = '${pageContext.request.contextPath}/cart/addAjax';
</script>
<script src="/resources/js/cart.js"></script>

</body>
</html>
