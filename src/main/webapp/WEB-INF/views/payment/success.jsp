<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Đơn hàng đang được tiếp nhận</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet"/>
  <style>
    body {
      background-color: #f8f9fa;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 1rem;
    }
    .info-card {
      max-width: 500px;
      width: 100%;
    }
    .info-icon {
      font-size: 4rem;
      color: #0d6efd; /* màu xanh dương cho trạng thái “tiếp nhận” */
    }
  </style>
</head>
<body>
<div class="card shadow-sm info-card">
  <div class="card-body text-center p-4">
    <!-- Icon đơn hàng -->
    <div class="mb-3">
      <i class="bi bi-cart-check-fill info-icon"></i>
    </div>

    <!-- Tiêu đề -->
    <h2 class="card-title mb-3">Đơn hàng đang được tiếp nhận</h2>

    <!-- Nội dung mô tả -->
    <p class="card-text mb-4">
      Cảm ơn bạn đã mua sắm tại Hi‑Kids.
      Đơn hàng của bạn đã được ghi nhận và đang chờ xử lý.
      Chúng tôi sẽ liên hệ với bạn ngay khi đơn hàng được chuẩn bị xong.
    </p>

    <!-- Nút quay về trang chủ -->
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">
      <i class="bi bi-house-door-fill me-2"></i> Quay về trang chủ
    </a>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
