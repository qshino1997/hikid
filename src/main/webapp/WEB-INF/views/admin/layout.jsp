<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <title>Trang Quản Trị</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet" />
  <style>
    body { font-family: 'Quicksand', sans-serif; }
    .sidebar { background-color: #343a40; }
    .sidebar .nav-link { color: #adb5bd; }
    .sidebar .nav-link.active, .sidebar .nav-link:hover { color: #fff; background-color: #495057; }
    .content-area { padding: 2rem; }
    .fixed-table-height { max-height: 400px; overflow-y: auto; }
    .fixed-table-height-product{ max-height: 800px; overflow-y: auto; }
    .table-fixed { table-layout: fixed; width: 100%; }
    .table-fixed th, .table-fixed td { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  </style>
</head>
<body>
<tiles:insertAttribute name="header"/>
<div class="container-fluid">
  <div class="row">
    <tiles:insertAttribute name="sidebar"/>
    <main class="col-md-10 ms-sm-auto content-area">
      <tiles:insertAttribute name="body"/>
    </main>
  </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>