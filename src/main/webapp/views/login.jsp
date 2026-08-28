<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Đăng nhập" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="auth-card">
        <h2>Đăng Nhập Vào Hệ Thống</h2>
        <c:if test="${param.activated == 'true'}">
            <div class="alert alert-success">Tài khoản đã được kích hoạt thành công! Bạn có thể đăng nhập ngay.</div>
        </c:if>
        <c:if test="${param.reset == 'true'}">
            <div class="alert alert-success">Mật khẩu đã được đặt lại thành công! Bạn có thể đăng nhập ngay.</div>
        </c:if>
        <c:if test="${alert != null}">
            <div class="alert alert-danger">
                ${alert}
                <c:if test="${unactivated}">
                    <div style="margin-top: 8px;">
                        <a class="btn btn-outline btn-block" style="font-size: 0.85rem; padding: 6px 12px;" href="${pageContext.request.contextPath}/activate-account?email=${unactivatedUsername}">Nhập mã OTP để kích hoạt ngay</a>
                    </div>
                </c:if>
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
            <div class="form-group">
                <label for="username">Tài khoản</label>
                <input type="text" id="username" name="username" placeholder="Tài khoản" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Mật khẩu" class="form-control">
            </div>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
                <label class="form-check" style="margin-bottom: 0;"><input type="checkbox" name="remember"> Nhớ tôi</label>
                <a href="${pageContext.request.contextPath}/forgot-password" style="font-size: 0.9rem;">Quên mật khẩu?</a>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Đăng nhập</button>
        </form>
        <p class="auth-alt">
            Nếu bạn chưa có tài khoản trên hệ thống, thì hãy
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </p>
    </div>
</div>
<jsp:include page="/common/web/footer.jsp"/>
