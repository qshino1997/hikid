<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card mb-4">
  <div class="card-header bg-secondary text-white">
    <i class="bi bi-building"></i> Quản lý Nhà sản xuất
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
        <input type="text" name="keyword" value="${keywordDefault}" placeholder="Nhập tên nhà sản xuất"
               class="form-control"/>
      </div>
      <div class="col-md-4 d-flex align-items-end">
        <button type="submit" class="btn btn-primary">
          <i class="bi bi-search me-1"></i> Tìm kiếm
        </button>
      </div>
    </form>

    <div id="manufacturersTable"></div>

    <a href="<c:url value='/admin/manufacturer/create'/>" class="btn btn-success mt-3">
      <i class="bi bi-plus-circle me-1"></i> Thêm Nhà sản xuất
    </a>
  </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
  $(function(){
    const base = '${pageContext.request.contextPath}';

    function loadManufacturers(page = 1) {
      const kw = $('input[name="keyword"]').val().trim();
      $.get(base + '/admin/manufacturer/ajax', { page, size: 10, keyword: kw })
              .done(html => $('#manufacturersTable').html(html))
              .fail((_, status) => alert('Lỗi tải danh sách: ' + status));
    }

    $('#filterForm').on('submit', function(e){
      e.preventDefault();
      loadManufacturers(1);
    });

    $(document).on('click', '#manufacturersTable .pagination .page-link', function(e){
      e.preventDefault();
      const p = $(this).data('page');
      if (p) loadManufacturers(p);
    });

    loadManufacturers();
  });
</script>
