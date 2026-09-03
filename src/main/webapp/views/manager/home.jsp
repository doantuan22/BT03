<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Trang quản lý" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="dashboard-card">
    <h2>Xin chào, ${sessionScope.account.fullName}!</h2>
    <p>Bạn đã đăng nhập thành công (roleId = ${sessionScope.account.roleId})</p>
    <a class="btn btn-outline" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</div>
<jsp:include page="/common/web/footer.jsp"/>
