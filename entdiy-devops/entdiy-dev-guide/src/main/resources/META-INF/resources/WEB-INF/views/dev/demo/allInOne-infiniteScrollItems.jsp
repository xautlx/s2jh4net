<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:forEach var="item" items="${pageData.content}">
	<tr>
		<td>${item.code}</td>
		<td>${item.createDate}</td>
	</tr>
</c:forEach>