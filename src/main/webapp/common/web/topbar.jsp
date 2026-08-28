<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:choose>
    <c:when test="${sessionScope.account == null}">
        <ul class="right-topbar">
            <li><a href="${pageContext.request.contextPath}/login">Đăng nhập</a></li>
            <li><a href="${pageContext.request.contextPath}/register">Đăng ký</a></li>
        </ul>
    </c:when>
    <c:otherwise>
        <ul class="right-topbar">
            <li><a href="${pageContext.request.contextPath}/waiting">${sessionScope.account.fullName}</a></li>
            <li><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
        </ul>
    </c:otherwise>
</c:choose>
