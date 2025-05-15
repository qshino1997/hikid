<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>

<%--<div class="accordion" id="categoryAccordion" style="position:sticky; top:56px;">--%>
<%--  <div class="accordion-item">--%>
<%--    <h2 class="accordion-header" id="heading0">--%>
<%--      <button class="accordion-button" type="button" data-bs-toggle="collapse"--%>
<%--              data-bs-target="#collapse0" aria-expanded="true" aria-controls="collapse0">--%>
<%--        Danh mục sản phẩm--%>
<%--      </button>--%>
<%--    </h2>--%>
<%--    <div id="collapse0" class="accordion-collapse collapse show" aria-labelledby="heading0"--%>
<%--         data-bs-parent="#categoryAccordion">--%>
<%--      <div class="accordion-body p-0">--%>
<%--        <ul class="list-group list-group-flush">--%>
<%--          <li class="list-group-item"><a href="#">🍼 Bỉm tã</a></li>--%>
<%--          <li class="list-group-item"><a href="#">🥛 Sữa dinh dưỡng</a></li>--%>
<%--          <li class="list-group-item"><a href="#">🍽️ Đồ dùng ăn uống</a></li>--%>
<%--          <li class="list-group-item"><a href="#">👩‍👧 Mẹ và Bé</a></li>--%>
<%--          <li class="list-group-item"><a href="#">🧸 Đồ chơi trẻ em</a></li>--%>
<%--          <li class="list-group-item"><a href="#">👕 Thời trang trẻ em</a></li>--%>
<%--        </ul>--%>
<%--      </div>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--</div>--%>


<%--<div class="sidebar">--%>
<%--  <div class="menu-item">--%>
<%--    <a href="#" class="menu-link">Tã - Bỉm <i class="bi bi-chevron-right float-end"></i></a>--%>
<%--    <div class="submenu">--%>
<%--      <a href="#">Tã dán</a>--%>
<%--      <a href="#">Tã quần</a>--%>
<%--      <a href="#">Khăn ướt</a>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--  <div class="menu-item">--%>
<%--    <a href="#" class="menu-link">Sữa cho bé <i class="bi bi-chevron-right float-end"></i></a>--%>
<%--    <div class="submenu">--%>
<%--      <a href="#">Sữa bột</a>--%>
<%--      <a href="#">Sữa công thức</a>--%>
<%--      <a href="#">Sữa tươi</a>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--  <div class="menu-item">--%>
<%--    <a href="#" class="menu-link">Thực phẩm <i class="bi bi-chevron-right float-end"></i></a>--%>
<%--    <div class="submenu">--%>
<%--      <a href="#">Bột ăn dặm</a>--%>
<%--      <a href="#">Bánh ăn dặm</a>--%>
<%--      <a href="#">Ngũ cốc</a>--%>
<%--    </div>--%>
<%--  </div>--%>
<%--  <!-- Bạn có thể thêm nhiều mục menu khác -->--%>
<%--</div>--%>


<div class="sidebar">
  <c:forEach var="category" items="${categories}">
    <div class="menu-item">
      <a href="<c:url value="product/${category.id}/list"/>" class="menu-link">
          ${category.name}
        <i class="bi bi-chevron-right"></i>
      </a>

      <c:if test="${!empty category.subCategories}">
        <div class="submenu">
          <c:forEach var="sub1" items="${category.subCategories}">
            <div class="menu-item">
              <a href="<c:url value="product/${sub1.id}/list"/>" class="menu-link">
                  ${sub1.name}
                    <c:if test="${!empty sub1.subCategories}">
                      <i class="bi bi-chevron-right"></i>
                    </c:if>
              </a>

              <c:if test="${!empty sub1.subCategories}">
                <div class="submenu">
                  <c:forEach var="sub2" items="${sub1.subCategories}">
                    <a href="#" class="menu-link">${sub2.name}</a>
                  </c:forEach>
                </div>
              </c:if>
            </div>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </c:forEach>
</div>




