<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Trang chủ" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="card landing-card">
        <h2>Chào mừng đến với IoTStar</h2>
        <c:choose>
            <c:when test="${sessionScope.account == null}">
                <p>Vui lòng đăng nhập hoặc tạo tài khoản mới để tiếp tục.</p>
                <div class="landing-actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a class="btn btn-outline" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </div>
            </c:when>
            <c:otherwise>
                <p>Xin chào, ${sessionScope.account.fullName}!</p>
                <div class="landing-actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/waiting">Vào hệ thống</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<jsp:include page="/common/web/footer.jsp"/>
