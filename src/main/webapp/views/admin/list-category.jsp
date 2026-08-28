<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Quan ly danh muc" scope="request"/>
<jsp:include page="/common/web/header.jsp"/>
<link rel="stylesheet" href="<c:url value='/assets/css/category.css'/>">
<section class="category-panel">
    <div class="category-header">
        <div><h2>Danh m&#7909;c</h2><p>Qu&#7843;n l&#253; danh m&#7909;c s&#7843;n ph&#7849;m.</p></div>
        <a class="btn btn-primary" href="<c:url value='/admin/category/add'/>">Th&#234;m danh m&#7909;c</a>
    </div>
    <table class="category-table">
        <thead><tr><th>STT</th><th>&#7842;nh</th><th>T&#234;n danh m&#7909;c</th><th>Thao t&#225;c</th></tr></thead>
        <tbody>
        <c:forEach items="${cateList}" var="cate" varStatus="status">
            <tr>
                <td>${status.count}</td>
                <td><c:choose><c:when test="${not empty cate.icon}"><c:url value="/image" var="imageUrl"><c:param name="fname" value="${cate.icon}"/></c:url><img class="category-image" src="${imageUrl}" alt="${cate.name}"></c:when><c:otherwise><span class="no-image">Ch&#432;a c&#243; &#7843;nh</span></c:otherwise></c:choose></td>
                <td><c:out value="${cate.name}"/></td>
                <td class="category-actions">
                    <c:url value="/admin/category/edit" var="editUrl"><c:param name="id" value="${cate.id}"/></c:url>
                    <a class="btn btn-outline" href="${editUrl}">S&#7917;a</a>
                    <form action="<c:url value='/admin/category/delete'/>" method="post" onsubmit="return confirm('Xac nhan xoa danh muc nay?');"><input type="hidden" name="id" value="${cate.id}"><button class="btn btn-danger" type="submit">X&#243;a</button></form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty cateList}"><tr><td colspan="4" class="empty-state">Ch&#432;a c&#243; danh m&#7909;c n&#224;o.</td></tr></c:if>
        </tbody>
    </table>
</section>
<jsp:include page="/common/web/footer.jsp"/>
