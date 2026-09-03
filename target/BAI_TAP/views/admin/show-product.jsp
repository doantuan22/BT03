<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>
<c:set var="pageTitle" value="Chi tiết sản phẩm" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">
<section class="product-detail-panel">
    <div class="product-header">
        <div>
            <h2><c:out value="${product.name}"/></h2>
            <p>Chi tiết thông tin sản phẩm trong hệ thống.</p>
        </div>
        <div style="display: flex; gap: 8px;">
            <c:url value="/admin/products/edit" var="editUrl">
                <c:param name="id" value="${product.id}"/>
            </c:url>
            <a class="btn btn-primary" href="${editUrl}">Chỉnh sửa</a>
            <a class="btn btn-outline" href="<c:url value='/admin/products'/>">Quay lại danh sách</a>
        </div>
    </div>

    <div style="display: flex; gap: 32px; flex-wrap: wrap; margin-top: 20px;">
        <div>
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="${product.imageUrl}" class="img-fluid" alt="Product Image">
                </c:when>
                <c:otherwise>
                    <div style="width: 240px; height: 240px; background: #f3f4f6; display: flex; align-items: center; justify-content: center; border-radius: 8px; border: 1px solid #d1d5db; color: #9ca3af;">
                        Không có hình ảnh
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <div style="flex: 1; min-width: 280px;">
            <table class="product-table" style="border: 1px solid #e5e7eb; border-radius: 6px;">
                <tr>
                    <th style="width: 160px;">Mã sản phẩm (ID):</th>
                    <td><strong>#${product.id}</strong></td>
                </tr>
                <tr>
                    <th>Tên sản phẩm:</th>
                    <td><c:out value="${product.name}"/></td>
                </tr>
                <tr>
                    <th>Slug:</th>
                    <td><code><c:out value="${product.slug}"/></code></td>
                </tr>
                <tr>
                    <th>Danh mục:</th>
                    <td>
                        <span class="badge-category">
                            <c:out value="${not empty product.category ? product.category.name : (not empty product.categoryName ? product.categoryName : 'Chưa phân loại')}"/>
                        </span>
                    </td>
                </tr>
                <tr>
                    <th>Giá niêm yết:</th>
                    <td>
                        <span class="price-tag" style="font-size: 1.25rem;">
                            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="₫" maxFractionDigits="2"/>
                        </span>
                    </td>
                </tr>
                <tr>
                    <th>Ngày tạo:</th>
                    <td><fmt:formatDate value="${product.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                </tr>
                <tr>
                    <th>Cập nhật lần cuối:</th>
                    <td><fmt:formatDate value="${product.updatedAt}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                </tr>
            </table>

            <div style="margin-top: 20px;">
                <h4 style="margin-bottom: 8px; color: #374151;">Mô tả sản phẩm:</h4>
                <div style="background: #f9fafb; padding: 16px; border-radius: 6px; border: 1px solid #e5e7eb; color: #4b5563; line-height: 1.6; min-height: 80px;">
                    <c:choose>
                        <c:when test="${not empty product.description}">
                            <c:out value="${product.description}"/>
                        </c:when>
                        <c:otherwise>
                            <em>Chưa có mô tả cho sản phẩm này.</em>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</section>
<jsp:include page="/common/web/footer.jsp"/>
