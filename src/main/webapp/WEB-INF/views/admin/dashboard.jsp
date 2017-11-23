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
	<div class="note note-info">
		<h4 class="block">提示说明</h4>
		<p>以下Dashboard内容定义主要是演示效果之用，实际需要根据项目业务需求定制设计开发。</p>
	</div>
	<div class="well well-large">
		<h3>项目简介</h3>
		<p>集结最新主流时尚开源技术的面向互联网Web应用的整合前端门户站点、HTML5移动站点及后端管理系统一体的的基础开发框架，提供一个J2EE相关主流开源技术架构整合及一些企业应用基础通用功能和组件的设计实现的最佳实践和原型参考。</p>
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
						<a class="list-group-item" href="#/docs/ui-feature/items" data-path="演示示例:UI组件用法示例">
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
</body>
</html>
