<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${ctx}/admin/sys/user-message/edit" method="post" modelAttribute="entity" data-editrulesurl="false">
		<div class="form-body">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">消息内容</label>
						<div class="controls">
							<form:textarea path="message" class="form-control" data-htmleditor="kindeditor" data-height="300px" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
</body>
</html>
