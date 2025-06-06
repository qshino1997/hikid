<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="vi_VN" />

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>Danh sách sản phẩm</title>
  <style>
    .product-image-wrapper {
      position: relative;
      width: 100%;
      padding-top: 100%;  /* 1:1 ratio */
      overflow: hidden;
    }
    .product-image-wrapper img {
      position: absolute;
      top: 0; left: 0;
      width: 100%; height: 100%;
      object-fit: cover;
    }
  </style>

</head>
<body class="d-flex flex-column min-vh-100">
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
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1 py-4">
  <!-- Breadcrumb -->
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb bg-white rounded-1 p-2">
      <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Trang chủ</a></li>
      <li class="breadcrumb-item active" aria-current="page">
        ${category.name}
      </li>
    </ol>
  </nav>
  <!-- Toast container, nằm top-right -->
  <c:if test="${not empty success}">
    <div class="alert alert-success" id="showAlert">${success}</div>
  </c:if>
  <c:if test="${not empty failed}">
    <div class="alert alert-danger" id="showAlert">${failed}</div>
  </c:if>
  <!-- Product Grid -->
  <div id="productContainer">
  </div>

</div>
<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>


<script>
  $(function(){
    const base = '${pageContext.request.contextPath}';
    function loadProducts(page = 1) {
      $.get(base + '/product/${categoryId}/loadAjax', { page, size: 12 })
              .done(html => $('#productContainer').html(html))
              .fail((_, status) => alert('Lỗi tải danh sách: ' + status));
    }

    $(document).on('click', '#productContainer .pagination .page-link', function(e){
      e.preventDefault();
      const p = $(this).data('page');
      if (p) loadProducts(p);
    });
    loadProducts();
  });

</script>
</body>
</html>
