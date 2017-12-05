<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta content="/WEB-INF/views/layouts/admin-login.jsp" name="decorator" />
<title>请重置密码</title>
</head>
<body>
	<div class="row">
		<div class="col-md-12">
			<form class="form-horizontal form-bordered form-validation" method="post"
				action="${ctx}/admin/profile/credentials-expire" data-editrulesurl="false">
				<h3 class="form-title" style="color: #666666">请重置密码</h3>
				<div class="note note-info">
					<p>系统检测您是首次或长期未修改密码，为了您的账户安全，请重置修改您的登录密码。</p>
					<p>请注意：系统会根据规则对新密码进行强度校验，例如包括但不限于'不能等于上次密码'等规则。</p>
				</div>
				<div class="form-body">
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
					<button class="btn blue" type="submit" data-post-dismiss="modal">提 交</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>