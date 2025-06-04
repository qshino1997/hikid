<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>403 - Access Denied</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f9f9f9; }
        .container {
            max-width: 600px;
            margin: 100px auto;
            padding: 20px;
            background: #fff;
            border: 1px solid #ddd;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
        }
        h1 { color: #d9534f; }
        p { color: #555; }
        a { color: #337ab7; text-decoration: none; }
        a:hover { text-decoration: underline; }
    </style>
</head>
<body>
<div class="container">
    <h1>403 - Bạn không có quyền truy cập</h1>
    <p>Bạn không được phép xem trang này. Nếu cho rằng đây là nhầm lẫn, vui lòng liên hệ quản trị viên.</p>
    <p><a href="${pageContext.request.contextPath}/">Quay về trang chủ</a></p>
</div>
</body>
