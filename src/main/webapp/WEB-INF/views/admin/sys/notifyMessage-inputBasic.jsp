<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知消息编辑</title>
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${ctx}/admin/sys/notify-message/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<div class="form-actions">
			<button class="btn green" type="submit" data-grid-reload="#grid-sys-notify-message-index" data-post-dismiss="modal">保存</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">标题</label>
						<div class="controls">
							<form:input path="title" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">外部链接</label>
						<div class="controls">
							<form:input path="externalLink" class="form-control" />
							<div class="help-block">如果定义了外部链接，显示公告时忽略公告内容直接新开转向链接定义的页面</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">消息内容</label>
						<div class="controls">
							<form:textarea path="htmlContent" class="form-control" data-htmleditor="kindeditor" data-height="400px" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">生效时间</label>
						<div class="controls">
							<form:input path="publishTime" class="form-control" data-toggle="datetimepicker"/>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">过期时间</label>
						<div class="controls">
							<form:input path="expireTime" class="form-control" data-toggle="datetimepicker"/>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">排序号</label>
						<div class="controls">
							<form:input path="orderRank" class="form-control" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">显示范围</label>
						<div class="controls  controls-checkboxes">
							<label><form:checkbox path="mgmtShow" class="form-control" />管理后台</label><label><form:checkbox
									path="siteShow" class="form-control" />前端站点</label>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn green" type="submit" data-grid-reload="#grid-sys-notify-message-index" data-post-dismiss="modal">保存</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>
