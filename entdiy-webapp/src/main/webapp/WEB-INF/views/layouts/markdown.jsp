<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>${name}</title>
<link href="${ctx}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
</head>
<body style="margin: 20px">
	<div class="row">

		<div class="col-md-10">${mdHtml}</div>
		<div class="col-md-2">
			<ul>
				<c:forEach var="item" items="${files}">
					<c:if test="${item!='images'}">
						<li><a href="${ctx}/docs/markdown/${item}">${item}</a></li>
					</c:if>
				</c:forEach>
			</ul>
		</div>
	</div>
</body>
</html>