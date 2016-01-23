<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Dashboard</title>
</head>
<body>
	<div class="row">
		<div class="col-md-3">
			<div class="dashboard-stat yellow">
				<div class="visual">
					<i class="fa fa-bullhorn"></i>
				</div>
				<div class="details">
					<div class="number number-notify-message-count">-</div>
					<div class="desc">未读公告消息</div>
				</div>
				<a href="javascript:;" rel="address:/admin/profile/notify-message|公告消息列表" class="more">
					View more <i class="m-icon-swapright m-icon-white"></i>
				</a>
			</div>
		</div>
		<div class="col-md-3">
			<div class="dashboard-stat blue">
				<div class="visual">
					<i class="fa fa-envelope"></i>
				</div>
				<div class="details">
					<div class="number number-user-message-count">-</div>
					<div class="desc">未读个人消息</div>
				</div>
				<a href="javascript:;" rel="address:/admin/profile/user-message|个人消息列表" class="more">
					View more <i class="m-icon-swapright m-icon-white"></i>
				</a>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
			<div class="dashboard-stat green">
				<div class="visual">
					<i class="fa fa-shopping-cart"></i>
				</div>
				<div class="details">
					<div class="number">549</div>
					<div class="desc">New Orders</div>
				</div>
				<a href="#" class="more">
					View more <i class="m-icon-swapright m-icon-white"></i>
				</a>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
			<div class="dashboard-stat purple">
				<div class="visual">
					<i class="fa fa-globe"></i>
				</div>
				<div class="details">
					<div class="number">+89%</div>
					<div class="desc">Brand Popularity</div>
				</div>
				<a href="#" class="more">
					View more <i class="m-icon-swapright m-icon-white"></i>
				</a>
			</div>
		</div>
		<div class="col-lg-3 col-md-3 col-sm-6 col-xs-12">
			<div class="dashboard-stat yellow">
				<div class="visual">
					<i class="fa fa-bar-chart-o"></i>
				</div>
				<div class="details">
					<div class="number">12,5M$</div>
					<div class="desc">Total Profit</div>
				</div>
				<a href="#" class="more">
					View more <i class="m-icon-swapright m-icon-white"></i>
				</a>
			</div>
		</div>
	</div>
	<div class="well well-large">
		<h4>业务Dashboard展示区域</h4>
		根据业务定制
	</div>
	<c:if test="${cfg.dev_mode}">
		<div class="alert alert-block alert-info fade in">
			<h4 class="alert-heading">
				提示说明: <small>当前界面内容只有在开发模式(dev_mode=true)才会显示！</small>
			</h4>
			<div class="row">
				<div class="col-md-12">
					<div class="list-group">
						<a class="list-group-item" href="${ctx}/docs/markdown/README" target="_blank">
							<h4 class="list-group-item-heading">开发框架介绍与开发指南</h4>
							<p class="list-group-item-text">对整个开发框架整体的设计说明和开发指南。</p>
						</a>
						<a class="list-group-item" href="javascript:;" rel="address:/docs/ui-feature/items|UI组件用法示例">
							<h4 class="list-group-item-heading">UI组件用法示例</h4>
							<p class="list-group-item-text">针对典型的UI组件，提供一个基本的用法示意参考，结合对应的JSDoc文档了解相关UI组件的用法和支持的功能属性。</p>
						</a>
						<a class="list-group-item" href="${ctx}/docs/jsdoc/global.html" target="_blank">
							<h4 class="list-group-item-heading">Javascript注释JSDoc文档</h4>
							<p class="list-group-item-text">基于框架主要Javascript代码文件注释，用jsdoc3-maven-plugin生成的UI组件用法参考文档。</p>
						</a>
					</div>
				</div>
			</div>
		</div>
	</c:if>
	<script type="text/javascript">
        $(function() {
            AdminGlobal.updateMessageCount();
        });
    </script>
</body>
</html>
