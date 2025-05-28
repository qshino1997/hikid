<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="card mb-4">
  <div class="card-header bg-secondary text-white">
    <i class="bi bi-people"></i> Quản lý Người dùng
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
        <label>Tìm theo User ID / Ten</label>
        <input type="text" name="keyword" value="${keywordDefault}" placeholder="Nhập tên hoặc ID" class="form-control"/>
      </div>
      <div class="col-md-4 d-flex align-items-end">
        <button type="submit" class="btn btn-primary">Tim kiem</button>
      </div>
    </form>
    <div id="usersTable"></div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/resources/js/jquery-3.6.0.min.js"></script>
<script>
  $(function(){
    const base = '${pageContext.request.contextPath}';

    // 1) Load fragment lần đầu hoặc khi chuyển trang
    function loadUsers(page = 1) {
      const keyword = $('input[name="keyword"]').val()
      $.get(base + '/admin/ajaxUser', { page, size: 5, role : 3, keyword: keyword })
              .done(html => $('#usersTable').html(html))
              .fail((_, s) => alert('Lỗi tải danh sách: ' + s));
    }

    // Bind event submit
    $('#filterForm').on('submit', function(e){
      e.preventDefault();
      loadUsers(1);
    });

    // 2) Event delegation cho pagination
    $(document).on('click', '#usersTable .pagination .page-link', function(e){
      e.preventDefault();
      const p = $(this).data('page');
      if (p) loadUsers(p);
    });

    // 3) Load lần đầu
    loadUsers();
  });

</script>
