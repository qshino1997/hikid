<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card mb-4">
  <div class="card-header bg-secondary text-white">
    <i class="bi bi-tags"></i> Quản lý danh mục
  </div>
  <div class="card-body">
    <form id="filterForm" class="row g-3 mb-4">
      <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
      </c:if>
      <c:if test="${not empty failed}">
        <div class="alert alert-danger">${failed}</div>
      </c:if>
      <div class="col-md-4">
        <label>Tìm theo tên</label>
        <input type="text" name="keyword" value="${keywordDefault}" placeholder="Nhập tên danh mục"
               class="form-control"/>
      </div>
      <div class="col-md-4 d-flex align-items-end">
        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
      </div>
    </form>
    <div id="categoriesTable"></div>



    <a href="<c:url value='/admin/category/create'/>" class="btn btn-success mt-3">
      <i class="bi bi-plus-circle me-1"></i> Thêm danh mục
    </a>
  </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
  $(function(){
    const base = '${pageContext.request.contextPath}';
    function loadCategories(page = 1) {
      const kw = $('input[name="keyword"]').val().trim();
      $.get(base + '/admin/category/ajaxCategory', { page, size: 10, keyword: kw })
              .done(html => $('#categoriesTable').html(html))
              .fail((_, s) => alert('Lỗi tải danh sách: ' + s));
    }
    $('#filterForm').on('submit', e => { e.preventDefault(); loadCategories(1); });

    $(document).on('click', '#categoriesTable .pagination .page-link', function(e){
      e.preventDefault(); const p = $(this).data('page'); if(p) loadCategories(p);
    });
    loadCategories();
  });
</script>