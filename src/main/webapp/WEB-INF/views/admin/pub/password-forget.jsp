<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>找回密码</title>
</head>
<body>
	<form class="form-horizontal form-bordered form-validation" action="${ctx}/admin/password/forget" method="post"
		data-editrulesurl="false">
		<div class="form-body">
			<p>输入您注册时填写的登录账号或邮箱地址.</p>
			<p>如果未设置注册邮箱或遗忘相关注册信息请联系管理员协助处理.</p>
			<div class="form-group">
				<div class="input-icon">
					<i class="fa fa-user"></i> <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
						placeholder="填写登录账号或注册邮箱" name="uid" required="true" />
				</div>
			</div>
			<div class="form-group">
				<div class="input-group">
					<div class="input-icon">
						<i class="fa fa-qrcode"></i> <input class="form-control captcha-text" type="text" autocomplete="off"
							placeholder="验证码...看不清可点击图片可刷新" name="captcha" required="true" />
					</div>
					<span class="input-group-btn" style="cursor: pointer;"> <img alt="验证码" height="34px" class="captcha-img"
						src="${ctx}/assets/img/captcha_placeholder.jpg" title="看不清？点击刷新" />
					</span>
				</div>
			</div>
			<c:if test="${!mailServiceEnabled}">
				<div class="note note-warning" style="margin-bottom: 0px">
					<p>系统当前未开启邮件服务，暂时无法提供找回密码服务！</p>
					<p>若有疑问请联系告知管理员！</p>
				</div>
			</c:if>
		</div>

		<div class="form-actions right">
			<c:if test="${mailServiceEnabled}">
				<button class="btn green" type="submit" data-post-dismiss="modal">提交</button>
			</c:if>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>

	</form>
</body>
</html>
