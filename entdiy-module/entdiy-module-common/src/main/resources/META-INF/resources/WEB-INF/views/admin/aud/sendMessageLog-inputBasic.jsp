<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${ctx}/admin/aud/send-message-log/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<div class="form-actions">
			<button class="btn default" type="button" data-dismiss="modal">关闭</button>
		</div>
		<div class="form-body">
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">信息接收者</label>
						<div class="controls">
                            <p class="form-control-static">${entity.targets}</p>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">标题</label>
						<div class="controls">
                            <p class="form-control-static">${entity.title}</p>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">消息内容</label>
						<div class="controls">
                            <p class="form-control-static">${entity.message}</p>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">消息响应</label>
						<div class="controls">
                            <p class="form-control-static">${entity.response}</p>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">消息类型</label>
						<div class="controls">
                            <p class="form-control-static">${messageTypeMap[entity.messageType]}</p>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">发送时间</label>
						<div class="controls">
                            <p class="form-control-static">${entity.sendTime}</p>
						</div>
					</div>
	            </div>
	        </div>
		</div>
		<div class="form-actions right">
			<button class="btn default" type="button" data-dismiss="modal">关闭</button>
		</div>
	</form:form>
</body>
</html>