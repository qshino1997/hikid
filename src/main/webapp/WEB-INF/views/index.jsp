<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>Shop Homepage - Start Bootstrap Template</title>

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/assets/favicon.ico"/>

    <!-- Bootstrap icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet"/>

    <!-- Core theme CSS -->
    <link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet"/>

    <style>
        body {
            padding-top: 140px; /* chừa chỗ cho navbar + header */
        }
        .sidebar-heading {
            font-size: 1.25rem;
        }
        .list-group-item {
            font-size: 1rem;
        }
        #sidebar-wrapper {
            width: 250px;
            min-height: 100vh;
            position: sticky;
            top: 140px; /* cố định sidebar khi scroll */
        }
    </style>
</head>
<body>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-light bg-light fixed-top shadow-sm">
    <div class="container px-4 px-lg-5">
        <a class="navbar-brand fw-bold" href="#">Start Bootstrap</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
                <li class="nav-item"><a class="nav-link active" aria-current="page" href="#">Home</a></li>
                <li class="nav-item"><a class="nav-link" href="#">About</a></li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" id="navbarDropdown" href="#" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><a class="dropdown-item" href="#">All Products</a></li>
                        <li><hr class="dropdown-divider"/></li>
                        <li><a class="dropdown-item" href="#">Popular Items</a></li>
                        <li><a class="dropdown-item" href="#">New Arrivals</a></li>
                    </ul>
                </li>
            </ul>
            <form class="d-flex">
                <button class="btn btn-outline-dark" type="submit">
                    <i class="bi-cart-fill me-1"></i>
                    Cart
                    <span class="badge bg-dark text-white ms-1 rounded-pill">0</span>
                </button>
            </form>
        </div>
    </div>
    <div class="container px-4 px-lg-5 text-center text-white">
        <h1 class="display-5 fw-bolder">Shop in style</h1>
        <p class="lead fw-normal text-white-50 mb-0">With this shop homepage template</p>
    </div>
</nav>


<!-- Main Content -->
<div class="container-fluid">
    <div class="row">
        <!-- Sidebar -->
        <div class="col-lg-3">
            <div id="sidebar-wrapper" class="bg-white border rounded p-3 shadow-sm">
                <div class="sidebar-heading text-center fw-bold text-primary mb-4">Danh mục</div>
                <div class="list-group list-group-flush">
                    <a class="list-group-item list-group-item-action py-2" href="#">🍼 Bỉm tã</a>
                    <a class="list-group-item list-group-item-action py-2" href="#">🥛 Sữa dinh dưỡng</a>
                    <a class="list-group-item list-group-item-action py-2" href="#">🍽️ Đồ dùng ăn uống</a>
                    <a class="list-group-item list-group-item-action py-2" href="#">👩‍👧 Mẹ và Bé</a>
                    <a class="list-group-item list-group-item-action py-2" href="#">🧸 Đồ chơi trẻ em</a>
                    <a class="list-group-item list-group-item-action py-2" href="#">👕 Thời trang trẻ em</a>
                </div>
            </div>
        </div>

        <!-- Main Section -->
        <div class="col-lg-9 mt-4">
            <jsp:include page="sectionHome.jsp" />
        </div>
    </div>
</div>

<!-- Footer -->
<footer class="py-5 bg-dark mt-5">
    <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
</footer>

<!-- Bootstrap core JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Core theme JS -->
<script src="${pageContext.request.contextPath}/resources/js/scripts.js"></script>

</body>
</html>
