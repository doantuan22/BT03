<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Quên mật khẩu" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="auth-card">
        <h2>Quên Mật Khẩu</h2>
        <p style="color: #6b7280; font-size: 0.9rem; margin-top: -10px; margin-bottom: 20px;">
            Nhập địa chỉ Email đã đăng ký để nhận mã OTP thiết lập lại mật khẩu mới.
        </p>

        <c:if test="${not empty alert}">
            <div class="alert alert-danger">${alert}</div>
        </c:if>
        <c:if test="${not empty successMsg}">
            <div class="alert alert-success">${successMsg}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password" method="post" class="auth-form">
            <div class="form-group">
                <label for="email">Địa chỉ Email</label>
                <input type="email" id="email" name="email" value="<c:out value='${email}'/>" placeholder="name@example.com" class="form-control" required autofocus>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Gửi mã OTP</button>
        </form>

        <p class="auth-alt" style="margin-top: 20px;">
            Quay lại trang
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </div>
</div>
<jsp:include page="/common/web/footer.jsp"/>
