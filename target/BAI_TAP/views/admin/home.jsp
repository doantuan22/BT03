<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Trang quản trị" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="dashboard-card" style="max-width: 700px;">
    <h2>Xin chào, ${sessionScope.account.fullName}!</h2>
    <p>Chào mừng bạn đến với Trang quản trị hệ thống IoTStar.</p>
    <div style="display: flex; gap: 12px; margin: 24px 0; flex-wrap: wrap;">
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/category/list">Quản lý Danh mục</a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/products">Quản lý Sản phẩm</a>
        <a class="btn btn-outline" href="${pageContext.request.contextPath}/admin/products/create">Thêm Sản phẩm mới</a>
    </div>
    <a class="btn btn-outline" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</div>
<jsp:include page="/common/web/footer.jsp"/>
