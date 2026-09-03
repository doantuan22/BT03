<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Trang chủ - Sản phẩm mới nhất" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">

<div class="container" style="width: 100%; max-width: 1100px; margin: 0 auto;">
    <!-- Welcome Header / Hero Banner -->
    <div class="card" style="margin-bottom: 32px; padding: 28px 32px; border-radius: 12px; background: linear-gradient(135deg, #1e293b 0%, #334155 100%); color: #fff;">
        <h1 style="margin: 0 0 10px 0; font-size: 1.85rem; color: #fff;">
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    Xin chào, <c:out value="${sessionScope.account.fullName}"/>!
                </c:when>
                <c:otherwise>
                    Chào mừng bạn đến với Cửa Hàng IoTStar!
                </c:otherwise>
            </c:choose>
        </h1>
        <p style="margin: 0; font-size: 1rem; color: #cbd5e1; max-width: 650px; line-height: 1.5;">
            Khám phá những sản phẩm công nghệ mới nhất với mức giá hấp dẫn và chất lượng hàng đầu.
        </p>
        <c:if test="${empty sessionScope.account}">
            <div style="margin-top: 18px; display: flex; gap: 12px;">
                <a class="btn btn-primary" href="<c:url value='/login'/>">Đăng nhập</a>
                <a class="btn btn-outline" style="color: #fff; border-color: #64748b; background: rgba(255,255,255,0.1);" href="<c:url value='/register'/>">Đăng ký tài khoản</a>
            </div>
        </c:if>
    </div>

    <!-- 10 Newest Products Section -->
    <div style="display: flex; align-items: baseline; justify-content: space-between; margin-bottom: 8px; flex-wrap: wrap; gap: 8px;">
        <div>
            <h2 style="font-size: 1.6rem; margin: 0; color: #0f172a; font-weight: 700;">
                Sản Phẩm Mới Nhất
            </h2>
            <p style="margin: 4px 0 0; color: #64748b; font-size: 0.95rem;">
                Top 10 sản phẩm vừa được cập nhật trên hệ thống.
            </p>
        </div>
        <c:if test="${sessionScope.account.roleId == 1}">
            <a class="btn btn-outline btn-sm" href="<c:url value='/admin/products'/>">Quản trị sản phẩm &rarr;</a>
        </c:if>
    </div>

    <!-- Product Grid -->
    <div class="product-grid">
        <c:forEach items="${top10Products != null ? top10Products : productList}" var="prod">
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

    <c:if test="${empty top10Products && empty productList}">
        <div class="card" style="text-align: center; padding: 48px 20px; color: #64748b; margin-top: 20px;">
            <p style="font-size: 1.1rem; margin-bottom: 12px;">Hiện chưa có sản phẩm nào được bày bán.</p>
            <c:if test="${sessionScope.account.roleId == 1}">
                <a class="btn btn-primary" href="<c:url value='/admin/products/create'/>">+ Thêm sản phẩm đầu tiên</a>
            </c:if>
        </div>
    </c:if>
</div>

<jsp:include page="/common/web/footer.jsp"/>
