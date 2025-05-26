<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập Quản trị</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Quicksand', sans-serif;
        }

        .login-form {
            max-width: 400px;
            margin: 6% auto;
            padding: 2rem;
            background: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
        }

        .form-title {
            text-align: center;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>

<div class="login-form">
    <h2 class="form-title">Đăng nhập Quản trị</h2>

    <form action="<c:url value='/admin/login'/>" method="post">
        <div class="mb-3">
            <label for="email" class="form-label">Tên đăng nhập</label>
            <input type="text" class="form-control" id="email" name="email" required autofocus />
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu</label>
            <input type="password" class="form-control" id="password" name="password" required />
        </div>

        <c:if test="${param.error != null}">
            <div class="alert alert-danger">Sai tên đăng nhập hoặc mật khẩu!</div>
        </c:if>

        <c:if test="${param.logout != null}">
            <div class="alert alert-success">Đăng xuất thành công.</div>
        </c:if>

        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
    </form>
</div>

</body>
</html>
