<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户账号基本信息</title>
</head>
<body>
	<c:choose>
		<c:when test="${entity.auditTime==null}">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
				action="${ctx}/admin/auth/signup-user/audit" method="post" modelAttribute="entity" data-editrulesurl="false">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-actions">
					<button class="btn blue" type="submit" data-grid-reload="#grid-auth-signup-user-index">
						<i class="fa fa-check"></i> 保存
					</button>
					<button class="btn default" type="button" data-dismiss="modal">取消</button>
				</div>
				<div class="form-body">
					<div class="row" data-equal-height="false">
						<div class="col-md-5">
							<div class="form-group">
								<label class="control-label">管理授权</label>
								<div class="controls controls-radiobuttons">
									<form:radiobuttons path="user.mgmtGranted" class="form-control"
										items="${applicationScope.cons.booleanLabelMap}" />
									<span class="help-block">标识是否有权限登录访问管理端后台</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">账号失效日期</label>
								<div class="controls">
									<form:input path="user.accountExpireTime" class="form-control" data-picker="date" />
									<span class="help-block">
										留空表示永不失效<br>如果到期后用户登录无法登录管理端后台
									</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">密码失效日期</label>
								<div class="controls">
									<form:input path="user.credentialsExpireTime" class="form-control" data-picker="date" />
									<span class="help-block">
										留空表示永不失效<br>如果到期后用户登录后会强制要求重新设置密码
									</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">登录账号</label>
								<div class="controls">
									<p class="form-control-static">${entity.authUid}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">真实姓名</label>
								<div class="controls">
									<p class="form-control-static">${entity.trueName}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">昵称</label>
								<div class="controls">
									<p class="form-control-static">${entity.nickName}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">电子邮件</label>
								<div class="controls">
									<p class="form-control-static">${entity.email}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">移动电话</label>
								<div class="controls">
									<p class="form-control-static">${entity.mobile}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">注册时间</label>
								<div class="controls">
									<p class="form-control-static">${entity.signupTime}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">用户备注说明</label>
								<div class="controls">
									<p class="form-control-static">${entity.remarkInfo}</p>
								</div>
							</div>
						</div>
						<div class="col-md-7">
							<div class="form-group">
								<label class="control-label">关联角色</label>
								<div class="controls">
									<form:select path="user.selectedRoleIds" items="${roles}" itemValue="id" itemLabel="name" class="form-control"
										data-toggle="double-multi-select" data-height="500px" />
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="form-actions right">
					<button class="btn blue" type="submit" data-grid-reload="#grid-auth-signup-user-index">
						<i class="fa fa-check"></i> 保存
					</button>
					<button class="btn default" type="button" data-dismiss="modal">取消</button>
				</div>
			</form:form>
		</c:when>
		<c:otherwise>
			<div class="note note-info">
				<h4 class="block">注册信息已审核处理</h4>
				<p>可访问"用户管理"进行用户信息管理.</p>
			</div>
			<div class="form-horizontal form-bordered form-label-stripped ">
				<div class="form-body">
					<div class="form-group">
						<label class="control-label">登录账号</label>
						<div class="controls">
							<p class="form-control-static">${entity.authUid}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">真实姓名</label>
						<div class="controls">
							<p class="form-control-static">${entity.trueName}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">昵称</label>
						<div class="controls">
							<p class="form-control-static">${entity.nickName}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">电子邮件</label>
						<div class="controls">
							<p class="form-control-static">${entity.email}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">移动电话</label>
						<div class="controls">
							<p class="form-control-static">${entity.mobile}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">注册时间</label>
						<div class="controls">
							<p class="form-control-static">${entity.signupTime}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">审核时间</label>
						<div class="controls">
							<p class="form-control-static">${entity.auditTime}</p>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">用户备注说明</label>
						<div class="controls">
							<p class="form-control-static">${entity.remarkInfo}</p>
						</div>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>

</body>
</html>
