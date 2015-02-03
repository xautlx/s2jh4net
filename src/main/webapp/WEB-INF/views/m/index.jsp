<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<body>
	<div data-role="page">
		<div data-role="header">
			<h1>Mobile Login</h1>
		</div>
		<div role="main" class="ui-content">
			<h2>
				Welcome
				<shiro:user>
					<shiro:principal property="nickName" />
				(
				<shiro:principal property="authUid" />
				)
				</shiro:user>
				!
			</h2>
			<a href="${ctx}/m/login" class="ui-btn ">Login</a>
		</div>
	</div>
</body>
</html>