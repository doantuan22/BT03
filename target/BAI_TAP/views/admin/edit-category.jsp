<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Sua danh muc" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/category.css'/>">
<section class="category-form-panel">
    <h2>S&#7917;a danh m&#7909;c</h2>
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <form action="<c:url value='/admin/category/edit'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${category.id}">
        <div class="form-group"><label for="name">T&#234;n danh m&#7909;c</label><input id="name" class="form-control" name="name" value="<c:out value='${category.name}'/>" required></div>
        <c:if test="${not empty category.imageUrl}"><img class="category-image current-image" src="${category.imageUrl}" alt="${category.name}"></c:if>
        <div class="form-group"><label for="image">&#7842;nh &#273;&#7841;i di&#7879;n m&#7899;i</label><input id="image" type="file" name="image" accept="image/png,image/jpeg,image/gif,image/webp"></div>
        <div class="form-actions"><button class="btn btn-primary" type="submit">C&#7853;p nh&#7853;t</button><a class="btn btn-outline" href="<c:url value='/admin/category/list'/>">H&#7911;y</a></div>
    </form>
</section>
<jsp:include page="/common/web/footer.jsp"/>
