<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Quản lý sản phẩm" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">
<section class="product-panel">
    <div class="product-header">
        <div>
            <h2>Danh Sách Sản Phẩm</h2>
            <p>Quản lý toàn bộ danh sách sản phẩm trong hệ thống.</p>
        </div>
        <div style="display: flex; gap: 8px;">
            <a class="btn btn-outline" href="<c:url value='/admin/category/list'/>">Danh mục</a>
            <a class="btn btn-primary" href="<c:url value='/admin/products/create'/>">+ Thêm sản phẩm</a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.msg}">
        <div class="alert alert-success">${sessionScope.msg}</div>
        <c:remove var="msg" scope="session"/>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <table class="product-table">
        <thead>
            <tr>
                <th>STT</th>
                <th>Hình ảnh</th>
                <th>Tên sản phẩm</th>
                <th>Slug</th>
                <th>Danh mục</th>
                <th>Giá</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${productList}" var="prod" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty prod.image}">
                            <c:url value="/image" var="imageUrl">
                                <c:param name="fname" value="${prod.image}"/>
                            </c:url>
                            <img class="product-image" src="${imageUrl}" alt="${prod.name}">
                        </c:when>
                        <c:otherwise>
                            <span class="no-image">Chưa có ảnh</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <strong><c:out value="${prod.name}"/></strong>
                </td>
                <td>
                    <code><c:out value="${prod.slug}"/></code>
                </td>
                <td>
                    <span class="badge-category">
                        <c:out value="${not empty prod.category ? prod.category.name : (not empty prod.categoryName ? prod.categoryName : 'Chưa phân loại')}"/>
                    </span>
                </td>
                <td>
                    <span class="price-tag">
                        <fmt:formatNumber value="${prod.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                    </span>
                </td>
                <td class="product-actions">
                    <c:url value="/admin/products/show" var="showUrl">
                        <c:param name="id" value="${prod.id}"/>
                    </c:url>
                    <a class="btn btn-info btn-sm" href="${showUrl}">Xem</a>

                    <c:url value="/admin/products/edit" var="editUrl">
                        <c:param name="id" value="${prod.id}"/>
                    </c:url>
                    <a class="btn btn-outline btn-sm" href="${editUrl}">Sửa</a>

                    <form action="<c:url value='/admin/products/destroy'/>" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">
                        <input type="hidden" name="id" value="${prod.id}">
                        <button class="btn btn-danger btn-sm" type="submit">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty productList}">
            <tr>
                <td colspan="7" class="empty-state" style="text-align: center; padding: 32px;">
                    Chưa có sản phẩm nào trong hệ thống.
                </td>
            </tr>
        </c:if>
        </tbody>
    </table>
</section>
<jsp:include page="/common/web/footer.jsp"/>
