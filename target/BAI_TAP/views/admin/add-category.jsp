<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Them danh muc" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/category.css'/>">
<section class="category-form-panel">
    <h2>Th&#234;m danh m&#7909;c</h2>
    <c:if test="${not empty error}"><div class="alert alert-danger">${error}</div></c:if>
    <form action="<c:url value='/admin/category/add'/>" method="post" enctype="multipart/form-data">
        <div class="form-group"><label for="name">T&#234;n danh m&#7909;c</label><input id="name" class="form-control" name="name" required></div>
        <div class="form-group"><label for="icon">&#7842;nh &#273;&#7841;i di&#7879;n</label><input id="icon" type="file" name="icon" accept="image/png,image/jpeg,image/gif,image/webp"></div>
        <div class="form-actions"><button class="btn btn-primary" type="submit">Th&#234;m</button><a class="btn btn-outline" href="<c:url value='/admin/category/list'/>">H&#7911;y</a></div>
    </form>
</section>
<jsp:include page="/common/web/footer.jsp"/>
