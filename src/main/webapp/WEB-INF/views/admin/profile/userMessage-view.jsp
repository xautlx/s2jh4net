<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>通知消息查看</title>
</head>
<body>
	<div class="form-horizontal">
		<div class="form-body">
			<h3 class="margin-bottom-20">${notifyMessage.title}</h3>
			<div class="row">
				<div class="col-md-12">
					<c:choose>
						<c:when test="${empty notifyMessage.externalLink}">
						${notifyMessage.htmlContent}
						</c:when>
						<c:otherwise>
						点击外部链接查看：<a href="${notifyMessage.externalLink}" target="_blank">${notifyMessage.externalLink}</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</body>
</html>