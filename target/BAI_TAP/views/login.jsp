<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Đăng nhập" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="auth-card">
        <h2>Đăng Nhập Vào Hệ Thống</h2>
        <c:if test="${alert != null}">
            <div class="alert alert-danger">${alert}</div>
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
            <div class="form-check">
                <label><input type="checkbox" name="remember"> Nhớ tôi</label>
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
