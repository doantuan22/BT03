<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Đăng ký" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<div class="auth-wrapper">
    <div class="auth-card">
        <h2>Tạo tài khoản mới</h2>
        <c:if test="${alert != null}">
            <div class="alert alert-danger">${alert}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
            <div class="form-group">
                <label for="username">Tài khoản</label>
                <input type="text" id="username" name="username" placeholder="Tài khoản" class="form-control">
            </div>
            <div class="form-group">
                <label for="fullname">Họ tên</label>
                <input type="text" id="fullname" name="fullname" placeholder="Họ tên" class="form-control">
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Nhập Email" class="form-control">
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại</label>
                <input type="text" id="phone" name="phone" placeholder="Số điện thoại" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Mật khẩu" class="form-control">
            </div>
            <button type="submit" class="btn btn-primary btn-block">Tạo tài khoản</button>
        </form>
        <p class="auth-alt">
            Nếu bạn đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </div>
</div>
<jsp:include page="/common/web/footer.jsp"/>
