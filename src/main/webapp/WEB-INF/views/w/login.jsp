<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
</head>
<body>

	<!-- BEGIN BREADCRUMBS -->
	<div class="row breadcrumbs margin-bottom-40">
		<div class="container">
			<div class="col-md-4 col-sm-4">
				<h1>Login</h1>
			</div>
			<div class="col-md-8 col-sm-8">
				<ul class="pull-right breadcrumb">
					<li><a href="index.html">Home</a></li>
					<li><a href="">Pages</a></li>
					<li class="active">Login</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- END BREADCRUMBS -->

	<!-- BEGIN CONTAINER -->
	<div class="container margin-bottom-40">
		<div class="row">
			<div class="col-md-4 col-md-offset-4 col-sm-6 col-sm-offset-3 login-signup-page">
				<form action="${ctx}/w/login" method="post">

					<h2>账号登录</h2>

					<div class="input-group margin-bottom-20">
						<span class="input-group-addon"><i class="fa fa-envelope"></i></span> <input type="text" class="form-control"
							name="username" placeholder="电子邮件">
					</div>
					<div class="input-group margin-bottom-20">
						<span class="input-group-addon"><i class="fa fa-lock"></i></span> <input type="password" name="password"  class="form-control"
							placeholder="登录密码"> <a href="#" class="login-signup-forgot-link">忘记密码?</a>
					</div>

					<div class="row">
						<div class="col-md-6 col-sm-6">
							<label><input type="checkbox" name="rememberMe"> 记住登录</label>
						</div>
						<div class="col-md-6 col-sm-6">
							<button type="submit" class="btn theme-btn pull-right"> 登 录 </button>
						</div>
					</div>
					<hr>
				</form>
			</div>
		</div>
	</div>
	<!-- END CONTAINER -->
</body>
</html>