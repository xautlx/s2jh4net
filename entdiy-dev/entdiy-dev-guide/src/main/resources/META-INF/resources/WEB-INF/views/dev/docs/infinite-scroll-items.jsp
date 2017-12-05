<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:forEach var="item" items="${pageData.content}">
	<tr>
		<td>${item.code}</td>
		<td>${item.rebuildTime}</td>
	</tr>
</c:forEach>