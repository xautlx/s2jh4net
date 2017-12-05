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
		action="${ctx}/admin/aud/logging-event/edit" method="post" modelAttribute="entity" data-editrulesurl="false">
		<form:hidden path="id" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aud-logging-event-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">处理状态</label>
						<div class="controls controls-radiobuttons">
							<form:radiobuttons path="state" items="${stateMap}" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">处理说明</label>
						<div class="controls">
							<form:textarea path="operationExplain" rows="3" class="form-control" />
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
	<div class="tabbable tabbable-custom ">
		<ul class="nav nav-tabs">
			<li><a data-toggle="tab" href="#tab-auto">异常明细</a></li>
			<li><a data-toggle="tab" href="#tab-auto">属性明细</a></li>
		</ul>
		<div class="tab-content">
			<div class="tab-pane">
				<div class="scroller" data-height="500">
					<c:forEach items="${entity.loggingEventExceptions}" var="item">
						<fmt:formatNumber type="number" value="${item.id.i}" pattern="0000" />.${item.traceLine}<br>
					</c:forEach>
				</div>
			</div>
			<div class="tab-pane fade">
				<div class="scroller" data-height="500">
					<div class="table-responsive">
						<table class="table table-hover">
							<thead>
								<tr>
									<th>#</th>
									<th>属性名称</th>
									<th>属性值</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${entity.loggingEventProperties}" var="item" varStatus="var">
									<tr>
										<td>${var.count}</td>
										<td>${item.id.mappedKey}</td>
										<td>${item.mappedValue}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>