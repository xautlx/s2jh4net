<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置密码</title>
</head>
<body>
	<form class="form-horizontal form-bordered form-label-stripped form-validation" action="${ctx}/admin/password/reset"
		method="post" data-editrulesurl="false">
		<input type="hidden" name="code" value="${param.code}"> <input type="hidden" name="uid" value="${param.uid}">
		<div class="form-body">
			<h3>请重新设置您的登录密码</h3>
			<div class="form-group">
				<label class="control-label">登录账号</label>
				<div class="controls">
					<p class="form-control-static">${param.uid}</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">找回密码邮箱</label>
				<div class="controls">
					<p class="form-control-static">${param.email}</p>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">输入新密码</label>
				<div class="controls">
					<input type="password" name="newpasswd" class="form-control" required="true">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label">确认新密码</label>
				<div class="controls">
					<input type="password" name="cfmpasswd" class="form-control" required="true" data-rule-equalToByName="newpasswd">
				</div>
			</div>
		</div>

		<div class="form-actions right">
			<button class="btn green" type="submit" data-post-dismiss="modal">提交</button>
			<a class="btn default" href="${ctx}/admin/login">返回登录界面</a>
		</div>

	</form>
</body>
</html>
