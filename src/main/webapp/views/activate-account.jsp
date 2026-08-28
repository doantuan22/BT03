<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Kích hoạt tài khoản" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="auth-card">
        <h2>Kích Hoạt Tài Khoản (OTP)</h2>
        <p style="color: #6b7280; font-size: 0.9rem; margin-top: -10px; margin-bottom: 20px;">
            Vui lòng nhập email và mã OTP 6 chữ số đã được gửi tới hộp thư của bạn.
        </p>

        <c:if test="${not empty alert}">
            <div class="alert alert-danger">${alert}</div>
        </c:if>
        <c:if test="${not empty successMsg}">
            <div class="alert alert-success">${successMsg}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/activate-account" method="post" class="auth-form">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" name="email" value="<c:out value='${not empty email ? email : username}'/>" placeholder="Nhập địa chỉ Email" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="otp">Mã OTP (6 chữ số)</label>
                <input type="text" id="otp" name="otp" placeholder="Nhập 6 chữ số OTP" maxlength="6" pattern="[0-9]{6}" class="form-control" style="font-size: 1.2rem; letter-spacing: 4px; text-align: center;" required autofocus>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Kích hoạt tài khoản</button>
        </form>

        <form action="${pageContext.request.contextPath}/activate-account" method="post" style="margin-top: 15px; text-align: center;">
            <input type="hidden" name="action" value="resend">
            <input type="hidden" name="email" value="<c:out value='${not empty email ? email : username}'/>">
            <button type="submit" class="btn btn-outline btn-block" style="font-size: 0.9rem;">Gửi lại mã OTP</button>
        </form>

        <p class="auth-alt" style="margin-top: 20px;">
            Đã kích hoạt tài khoản?
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </div>
</div>
<jsp:include page="/common/web/footer.jsp"/>
