<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thanh toán thất bại</title>
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
    .error-card {
      max-width: 500px;
      width: 100%;
    }
    .error-icon {
      font-size: 4rem;
      color: #dc3545; /* đỏ tươi cho lỗi */
    }
  </style>
</head>
<body>
<div class="card shadow-sm error-card">
  <div class="card-body text-center p-4">
    <!-- Icon cảnh báo -->
    <div class="mb-3">
      <i class="bi bi-exclamation-triangle-fill error-icon"></i>
    </div>

    <!-- Tiêu đề -->
    <h2 class="card-title mb-3">Thanh toán thất bại hoặc bị hủy</h2>

    <!-- Nội dung mô tả -->
    <p class="card-text mb-4">
      Đã có lỗi xảy ra hoặc bạn đã hủy giao dịch.<br>
      Vui lòng kiểm tra lại thông tin và thử lại sau.
    </p>

    <!-- Nút quay lại giỏ hàng -->
    <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary btn-lg">
      <i class="bi bi-cart3 me-2"></i> Quay lại giỏ hàng
    </a>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
