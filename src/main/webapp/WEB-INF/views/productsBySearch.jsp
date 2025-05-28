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

  <!-- Bootstrap CSS + Icons + Font -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet"/>
  <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet"/>
  <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
  <link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>
  <link href="<c:url value='/resources/css/chatbot.css'/>" rel="stylesheet" type="text/css"/>
  <link href="<c:url value='/resources/css/image.css'/>" rel="stylesheet" type="text/css"/>

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
  <div id="productContainerSearch">
  </div>

</div>
<!-- Footer -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<!-- Bootstrap JS Bundle -->
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/cart.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom.js"></script>

<script>

  $(function(){
    const base = '${pageContext.request.contextPath}';
    function loadProducts(page = 1) {
      const kw = document.getElementById('searchKeyword').value;
      $.get(base + '/product/searchLoadAjax', { page, size: 12 , keyword: kw})
              .done(html => $('#productContainerSearch').html(html))
              .fail((_, status) => alert('Lỗi tải danh sách: ' + status));
    }

    $(document).on('click', '#productContainerSearch .pagination .page-link', function(e){
      e.preventDefault();
      const p = $(this).data('page');
      if (p) loadProducts(p);
    });

    loadProducts();
  });
</script>
</body>
</html>
