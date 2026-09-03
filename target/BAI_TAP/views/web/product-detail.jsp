<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="${product.name}" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">

<div class="container" style="max-width: 1050px; margin: 0 auto; padding: 24px 0;">
    <!-- Breadcrumb -->
    <nav style="margin-bottom: 20px; font-size: 0.9rem; color: #64748b;">
        <a href="<c:url value='/home'/>">Trang chủ</a> &rsaquo;
        <a href="<c:url value='/product'/>">Sản phẩm</a> &rsaquo;
        <c:if test="${not empty product.category}">
            <a href="<c:url value='/product'><c:param name='category_id' value='${product.categoryId}'/></c:url>">
                <c:out value="${product.category.name}"/>
            </a> &rsaquo;
        </c:if>
        <span style="color: #111827; font-weight: 500;"><c:out value="${product.name}"/></span>
    </nav>

    <!-- Product Detail Card -->
    <div class="card" style="padding: 36px; border-radius: 12px; box-shadow: 0 4px 16px rgba(0,0,0,0.06); display: flex; gap: 40px; flex-wrap: wrap;">
        <!-- Left: Image Box -->
        <div style="flex: 1; min-width: 300px; max-width: 440px; display: flex; flex-direction: column; align-items: center;">
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="${product.imageUrl}" alt="${product.name}" style="width: 100%; max-height: 400px; object-fit: cover; border-radius: 10px; border: 1px solid #e5e7eb; box-shadow: 0 2px 8px rgba(0,0,0,0.04);">
                </c:when>
                <c:otherwise>
                    <div style="width: 100%; height: 340px; background: #f3f4f6; border-radius: 10px; display: flex; align-items: center; justify-content: center; color: #9ca3af; border: 1px solid #e5e7eb;">
                        Chưa có hình ảnh
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Right: Information Box -->
        <div style="flex: 1.5; min-width: 320px;">
            <div style="margin-bottom: 10px;">
                <span class="badge-category" style="font-size: 0.85rem; padding: 4px 12px;">
                    <c:out value="${not empty product.category ? product.category.name : (not empty product.categoryName ? product.categoryName : 'Chưa phân loại')}"/>
                </span>
            </div>

            <h1 style="margin: 6px 0 16px; font-size: 1.95rem; color: #0f172a; line-height: 1.3;">
                <c:out value="${product.name}"/>
            </h1>

            <div style="margin-bottom: 20px; display: flex; align-items: baseline; gap: 16px;">
                <span style="font-size: 2rem; font-weight: 700; color: #047857;">
                    <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                </span>
            </div>

            <hr style="border: 0; border-top: 1px solid #e5e7eb; margin: 20px 0;">

            <!-- Description -->
            <div>
                <h3 style="font-size: 1.1rem; margin-bottom: 10px; color: #374151; font-weight: 600;">Mô tả sản phẩm</h3>
                <div style="line-height: 1.7; color: #4b5563; white-space: pre-line; background: #f8fafc; padding: 18px; border-radius: 8px; border: 1px solid #e2e8f0; font-size: 0.95rem;">
                    <c:choose>
                        <c:when test="${not empty product.description}">
                            <c:out value="${product.description}"/>
                        </c:when>
                        <c:otherwise>
                            <em>Chưa có mô tả chi tiết cho sản phẩm này.</em>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Meta details -->
            <div style="margin-top: 20px; font-size: 0.85rem; color: #64748b;">
                <p style="margin: 4px 0;">Slug: <code><c:out value="${product.slug}"/></code></p>
                <c:if test="${not empty product.createdAt}">
                    <p style="margin: 4px 0;">Ngày đăng: <fmt:formatDate value="${product.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                </c:if>
            </div>

            <!-- Action buttons -->
            <div style="margin-top: 28px; display: flex; gap: 12px; flex-wrap: wrap;">
                <a class="btn btn-primary" href="<c:url value='/product'/>">Xem tất cả sản phẩm</a>
                <a class="btn btn-outline" href="<c:url value='/home'/>">&larr; Quay lại trang chủ</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/common/web/footer.jsp"/>
