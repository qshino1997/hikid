<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Trang chủ Shop</title>

    <!-- CSS/JS chung -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.9.1/font/bootstrap-icons.css" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@500;600&display=swap" rel="stylesheet">

    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/resources/css/chatbot.css'/>" rel="stylesheet" type="text/css"/>

</head>
<body class="d-flex flex-column min-vh-100">
<!-- Banner  -->
<div class="banner"></div>
<!-- Header -->
<jsp:include page="/WEB-INF/views/fragment/header.jsp"/>
<!-- Content -->
<div class="container-fluid flex-grow-1">
    <div class="row">
        <!-- SIDEBAR -->
        <div class="col-md-3 col-lg-2 p-0">
            <jsp:include page="/WEB-INF/views/fragment/sidebar.jsp"/>
        </div>

        <!-- MAIN CONTENT -->
        <div class="col-md-9 col-lg-10 pt-4">

            <!-- BANNER/HEADER NHỎ -->
            <div class="bg-dark text-white text-center py-4 mb-4">
                <h1 class="display-5 fw-bolder">Shop in style</h1>
                <p class="lead fw-normal text-white-50 mb-0">With this shop homepage template</p>
            </div>

            <!-- SẢN PHẨM -->
<%--            <div class="row g-4 row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-4">--%>
<%--                <c:forEach var="p" items="${products}">--%>
<%--                    <div class="col mb-5">--%>
<%--                        <div class="card h-100 shadow-sm">--%>
<%--                            <!-- Ảnh SP -->--%>
<%--                            <img class="card-img-top" src="${p.imageUrl}" alt="${p.name}" />--%>
<%--                            <!-- Chi tiết SP -->--%>
<%--                            <div class="card-body p-4">--%>
<%--                                <div class="text-center">--%>
<%--                                    <h5 class="fw-bolder">${p.name}</h5>--%>
<%--                                    <c:if test="${not empty p.oldPrice}">--%>
<%--                                        <span class="text-muted text-decoration-line-through">${p.oldPrice}</span>--%>
<%--                                    </c:if>--%>
<%--                                    <span class="text-danger fw-bold ms-1">${p.price}</span>--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                            <!-- Nút hành động -->--%>
<%--                            <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">--%>
<%--                                <div class="text-center">--%>
<%--                                    <a class="btn btn-outline-dark mt-auto" href="/product/${p.id}">Xem chi tiết</a>--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </c:forEach>--%>
<%--            </div>--%>

        </div>
    </div>
</div>

<!-- Chatbot Popup -->
<div id="chatbot-popup" class="chatbot-popup">
    <div class="chatbot-header">
        <button id="chatbot-header" class="close-chatbot">X</button>
        <h5>Chat với Chatbot</h5>
    </div>
    <div id="chatbot-body" class="chatbot-body">
        <!-- Tin nhắn chatbot sẽ xuất hiện ở đây -->
    </div>
    <div class="chatbot-footer">
        <input id="message-input" type="text" class="form-control" placeholder="Nhập câu hỏi của bạn..." />
        <button id="send-message" class="btn btn-primary mt-2">Gửi</button>
    </div>
</div>

<!-- Open Chatbot Button -->
<button id="open-chatbot"
        class="btn btn-info position-fixed bottom-0 end-0 mb-4 me-4">
    <i class="bi bi-chat-dots"></i>
</button>
<!-- FOOTER -->
<jsp:include page="/WEB-INF/views/fragment/footer.jsp"/>

<!-- JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="/resources/js/chatbot.js"></script>

</body>

</html>
