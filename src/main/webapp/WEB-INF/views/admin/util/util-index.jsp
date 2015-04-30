<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>辅助管理功能</title>
</head>
<body>
	<div class="row">
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 缓存管理
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/cache-clear' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>为了系统运行效率，系统会基于Hibernate和Spring的Cache支持尽可能缓存数据</p>
								<p>此功能主要用于直接修改数据库数据后，通知缓存框架移除选取范围的缓存数据从而加载最新数据库数据.</p>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit">
								<i class="fa fa-check"></i> 刷新清空全部缓存数据
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 动态更新Logger日志级别
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/logger-update' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>此功能主要用于在应用运行过程中动态修改Logger日志级别从而实现在线Debug调试系统日志信息以便实时进行一些线上问题分析排查.</p>
								<p class="text-warning">在调低日志级别问题排查完毕后，最好把日志级别调整会预设较高级别以避免大量日志信息影响系统运行效率</p>
							</div>
							<div class="form-group">
								<label class="control-label">Logger Name</label>
								<div class="controls">
									<input type="text" name="loggerName" class="form-control" placeholder="root，特定package名称，特定logger名称等">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">Logger Level</label>
								<div class="controls">
									<select name="loggerLevel">
										<option value="OFF">OFF</option>
										<option value="ERROR">ERROR</option>
										<option value="WARN">WARN</option>
										<option value="INFO">INFO</option>
										<option value="DEBUG">DEBUG</option>
										<option value="TRACE">TRACE</option>
										<option value="ALL">ALL</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit">
								<i class="fa fa-check"></i> 动态更新Logger日志级别
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 负载均衡验证
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<a class="btn blue" href="${ctx}/admin/util/load-balance-test" target="_blank">点击显示负载均衡信息页面</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>