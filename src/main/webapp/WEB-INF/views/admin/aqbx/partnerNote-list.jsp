<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商谈纪要列表</title>
</head>
<body>
		<div class="form-body">
			<div class="portlet gren">
				<h5>商谈纪要列表</h5>
				<table class="table">
					<thead>
						<tr>
							<th>商谈纪要</th>
							<th>纪要时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${partnerNotes}">
							<tr>
								<td>${item.businessNote }</td>
								<td><fmt:formatDate value="${item.operationTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			
		</div>
</body>
</html>
