<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Chỉnh sửa sản phẩm" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/product.css'/>">
<section class="product-form-panel">
    <h2>Chỉnh Sửa Sản Phẩm</h2>
    <p style="color: #6b7280; font-size: 0.9rem; margin-top: 4px; margin-bottom: 20px;">
        Cập nhật thông tin chi tiết của sản phẩm.
    </p>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <form action="<c:url value='/admin/products/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${product.id}">

        <div class="form-row">
            <div class="form-group">
                <label for="name">Tên sản phẩm <span style="color:red">*</span></label>
                <input id="name" class="form-control" name="name" value="<c:out value='${product.name}'/>" required autofocus>
            </div>
            <div class="form-group">
                <label for="slug">Slug (Đường dẫn thân thiện)</label>
                <input id="slug" class="form-control" name="slug" value="<c:out value='${product.slug}'/>" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="category_id">Danh mục <span style="color:red">*</span></label>
                <select id="category_id" name="category_id" class="form-control" required>
                    <option value="">-- Chọn danh mục --</option>
                    <c:forEach items="${categories}" var="cate">
                        <option value="${cate.id}" ${product.categoryId == cate.id ? 'selected' : ''}>
                            <c:out value="${cate.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="price">Giá bán (VNĐ) <span style="color:red">*</span></label>
                <input id="price" type="number" step="0.01" min="0" class="form-control" name="price" value="<c:out value='${product.price}'/>" required>
            </div>
        </div>

        <div class="form-group">
            <label for="description">Mô tả sản phẩm</label>
            <textarea id="description" class="form-control" name="description" rows="4"><c:out value="${product.description}"/></textarea>
        </div>

        <div class="form-group">
            <label>Hình ảnh hiện tại</label>
            <c:choose>
                <c:when test="${not empty product.imageUrl}">
                    <img src="${product.imageUrl}" class="img-thumbnail" alt="Current Image" style="max-height: 200px;">
                </c:when>
                <c:otherwise>
                    <p class="no-image">Chưa có hình ảnh</p>
                </c:otherwise>
            </c:choose>
            <label for="image" style="margin-top: 10px;">Tải lên hình ảnh mới (thay thế ảnh cũ)</label>
            <input id="image" type="file" name="image" class="form-control" accept="image/png,image/jpeg,image/gif,image/webp">
        </div>

        <div class="form-actions" style="margin-top: 24px;">
            <button class="btn btn-primary" type="submit">Cập nhật sản phẩm</button>
            <a class="btn btn-outline" href="<c:url value='/admin/products'/>">Hủy</a>
        </div>
    </form>
</section>
<jsp:include page="/common/web/footer.jsp"/>
