<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>个人配置</title>
</head>
<body>
	<form class="form-horizontal form-bordered form-label-stripped form-validation" method="post"
		action="${ctx}/admin/profile/edit" data-editrulesurl="false">
		<div class="form-body">
			<div class="form-group">
				<label class="control-label">登录账号</label>
				<div class="controls">
					<p class="form-control-static">${user.authUid}</p>
				</div>
			</div>

		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-post-dismiss="modal">
				<i class="fa fa-check"></i> 保存
			</button>
		</div>
	</form>
</body>
</html>