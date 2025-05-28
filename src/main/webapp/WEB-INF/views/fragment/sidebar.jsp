<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link href="<c:url value='/resources/css/custom.css'/>" rel="stylesheet" type="text/css"/>
<link href="<c:url value='/resources/css/styles.css'/>" rel="stylesheet" type="text/css"/>

<div class="sidebar">
  <c:forEach var="cat" items="${categories}">
    <!-- Cấp 1 -->
    <div class="menu-item">
      <a href="<c:url value='product/${cat.id}/list'/>" class="menu-link has-children">
          ${cat.name} <i class="bi bi-chevron-right"></i>
      </a>

      <c:if test="${not empty cat.subCategories}">
        <div class="submenu">
          <c:forEach var="sub1" items="${cat.subCategories}">
            <!-- Cấp 2 -->
            <div class="menu-item">
              <a href="<c:url value='product/${sub1.id}/list'/>" class="menu-link has-children">
                  ${sub1.name}
                <c:if test="${not empty sub1.subCategories}">
                  <i class="bi bi-chevron-right"></i>
                </c:if>
              </a>

              <c:if test="${not empty sub1.subCategories}">
                <div class="submenu">
                  <c:forEach var="sub2" items="${sub1.subCategories}">
                    <!-- Cấp 3 -->
                    <a href="<c:url value='product/${sub2.id}/list'/>" class="menu-link">
                        ${sub2.name}
                    </a>
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





