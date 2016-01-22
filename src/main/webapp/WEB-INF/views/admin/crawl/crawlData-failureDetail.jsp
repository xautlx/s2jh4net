<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="row">
		<div class="col-md-12">
			<table class="table table-condensed  table-advance table-hover">
				<thead>
					<tr>
						<th>属性</th>
						<th>数据</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${datasMap}" var="item">
						<tr>
							<td class="success" style="width:250px"><pre>${item.key}</pre></td>
							<td class="property-value"><pre><c:out value="${item.value}" escapeXml="true" /></pre></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>