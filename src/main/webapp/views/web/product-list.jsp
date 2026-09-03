<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Danh sách sản phẩm" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">

<div class="container" style="width: 100%; max-width: 1100px; margin: 0 auto;">
    <!-- Header Section -->
    <div style="margin-bottom: 24px;">
        <h1 style="font-size: 1.85rem; margin: 0 0 6px 0; color: #0f172a; font-weight: 700;">
            Tất Cả Sản Phẩm
        </h1>
        <p style="margin: 0; color: #64748b; font-size: 0.95rem;">
            Khám phá danh mục đa dạng và cập nhật liên tục các sản phẩm mới nhất.
        </p>
    </div>

    <!-- Filter & Sort Bar -->
    <div class="filter-bar">
        <form action="<c:url value='/product'/>" method="get" class="filter-form">
            <div class="filter-group">
                <label for="category_id">Danh mục:</label>
                <select id="category_id" name="category_id" class="filter-control" onchange="this.form.submit()">
                    <option value="">Tất cả danh mục</option>
                    <c:forEach items="${categories}" var="cate">
                        <option value="${cate.id}" ${selectedCategoryId == cate.id ? 'selected' : ''}>
                            <c:out value="${cate.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="filter-group">
                <label for="sort">Sắp xếp:</label>
                <select id="sort" name="sort" class="filter-control" onchange="this.form.submit()">
                    <option value="newest" ${selectedSort == 'newest' ? 'selected' : ''}>Mới nhất</option>
                    <option value="oldest" ${selectedSort == 'oldest' ? 'selected' : ''}>Cũ nhất</option>
                    <option value="price_asc" ${selectedSort == 'price_asc' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                    <option value="price_desc" ${selectedSort == 'price_desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                    <option value="name_asc" ${selectedSort == 'name_asc' ? 'selected' : ''}>Tên: A - Z</option>
                    <option value="name_desc" ${selectedSort == 'name_desc' ? 'selected' : ''}>Tên: Z - A</option>
                </select>
            </div>

            <div class="filter-group" style="flex: 1; min-width: 200px;">
                <input type="text" name="keyword" class="filter-control" style="width: 100%;" placeholder="Tìm theo tên sản phẩm..." value="<c:out value='${keyword}'/>">
            </div>

            <div style="display: flex; gap: 8px;">
                <button type="submit" class="btn btn-primary" style="padding: 8px 16px; font-size: 0.9rem;">Lọc</button>
                <c:if test="${not empty selectedCategoryId || not empty keyword || (not empty selectedSort && selectedSort != 'newest')}">
                    <a href="<c:url value='/product'/>" class="btn btn-outline" style="padding: 8px 14px; font-size: 0.9rem;">Xóa bộ lọc</a>
                </c:if>
            </div>
        </form>
    </div>

    <!-- Results Status -->
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; color: #64748b; font-size: 0.9rem;">
        <div>
            Hiển thị <strong>${productList.size()}</strong> / <strong>${totalProducts}</strong> sản phẩm
            <c:if test="${totalPages > 1}">
                (Trang ${currentPage} / ${totalPages})
            </c:if>
        </div>
    </div>

    <!-- Product Grid (6 per page) -->
    <div class="product-grid">
        <c:forEach items="${productList}" var="prod">
            <c:choose>
                <c:when test="${not empty prod.slug}">
                    <c:url value="/product/${prod.slug}" var="detailUrl"/>
                </c:when>
                <c:otherwise>
                    <c:url value="/product/detail" var="detailUrl">
                        <c:param name="id" value="${prod.id}"/>
                    </c:url>
                </c:otherwise>
            </c:choose>
            <div class="product-card">
                <div class="product-card-media">
                    <a href="${detailUrl}" style="display: block; width: 100%; height: 100%;">
                        <c:choose>
                            <c:when test="${not empty prod.imageUrl}">
                                <img src="${prod.imageUrl}" alt="${prod.name}">
                            </c:when>
                            <c:otherwise>
                                <div style="width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; color: #9ca3af; font-size: 0.85rem; background: #f3f4f6;">
                                    Chưa có ảnh
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </a>
                </div>

                <div class="product-card-body">
                    <div class="product-card-category">
                        <c:out value="${not empty prod.category ? prod.category.name : (not empty prod.categoryName ? prod.categoryName : 'Sản phẩm')}"/>
                    </div>
                    <h3 class="product-card-title">
                        <a href="${detailUrl}" title="<c:out value='${prod.name}'/>">
                            <c:out value="${prod.name}"/>
                        </a>
                    </h3>

                    <div class="product-card-footer">
                        <div class="product-card-price">
                            <fmt:formatNumber value="${prod.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                        </div>
                        <a class="btn btn-primary product-card-btn" href="${detailUrl}">
                            Chi tiết
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${empty productList}">
        <div class="card" style="text-align: center; padding: 48px 20px; color: #64748b; margin-top: 20px;">
            <p style="font-size: 1.1rem; margin-bottom: 12px;">Không tìm thấy sản phẩm nào phù hợp với bộ lọc.</p>
            <a class="btn btn-outline" href="<c:url value='/product'/>">Xem tất cả sản phẩm</a>
        </div>
    </c:if>

    <!-- Pagination Links -->
    <c:if test="${totalPages > 1}">
        <div class="pagination-wrapper">
            <!-- Prev Link -->
            <c:url value="/product" var="prevUrl">
                <c:param name="page" value="${currentPage - 1}"/>
                <c:if test="${not empty selectedCategoryId}">
                    <c:param name="category_id" value="${selectedCategoryId}"/>
                </c:if>
                <c:if test="${not empty selectedSort}">
                    <c:param name="sort" value="${selectedSort}"/>
                </c:if>
                <c:if test="${not empty keyword}">
                    <c:param name="keyword" value="${keyword}"/>
                </c:if>
            </c:url>
            <a class="pagination-item ${currentPage <= 1 ? 'disabled' : ''}" href="${prevUrl}">&laquo; Trước</a>

            <!-- Page Number Links -->
            <c:forEach begin="1" end="${totalPages}" var="p">
                <c:url value="/product" var="pageUrl">
                    <c:param name="page" value="${p}"/>
                    <c:if test="${not empty selectedCategoryId}">
                        <c:param name="category_id" value="${selectedCategoryId}"/>
                    </c:if>
                    <c:if test="${not empty selectedSort}">
                        <c:param name="sort" value="${selectedSort}"/>
                    </c:if>
                    <c:if test="${not empty keyword}">
                        <c:param name="keyword" value="${keyword}"/>
                    </c:if>
                </c:url>
                <a class="pagination-item ${currentPage == p ? 'active' : ''}" href="${pageUrl}">${p}</a>
            </c:forEach>

            <!-- Next Link -->
            <c:url value="/product" var="nextUrl">
                <c:param name="page" value="${currentPage + 1}"/>
                <c:if test="${not empty selectedCategoryId}">
                    <c:param name="category_id" value="${selectedCategoryId}"/>
                </c:if>
                <c:if test="${not empty selectedSort}">
                    <c:param name="sort" value="${selectedSort}"/>
                </c:if>
                <c:if test="${not empty keyword}">
                    <c:param name="keyword" value="${keyword}"/>
                </c:if>
            </c:url>
            <a class="pagination-item ${currentPage >= totalPages ? 'disabled' : ''}" href="${nextUrl}">Sau &raquo;</a>
        </div>
    </c:if>
</div>

<jsp:include page="/common/web/footer.jsp"/>
