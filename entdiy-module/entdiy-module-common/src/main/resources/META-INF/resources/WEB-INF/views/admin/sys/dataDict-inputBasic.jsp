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
		action="${ctx}/admin/sys/data-dict/edit?parent.id=${param['parent.id']}" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<div class="form-actions">
			<button class="btn green" type="submit" data-post-dismiss="modal">保存</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">主标识</label>
						<div class="controls">
							<form:input path="primaryKey" class="form-control" />
							<div class="help-block">代码一般会在程序中引用，请勿随意变更代码</div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">次标识</label>
						<div class="controls">
							<form:input path="secondaryKey" class="form-control" />
							<div class="help-block">代码一般会在程序中引用，请勿随意变更代码</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">主要数据</label>
						<div class="controls">
							<form:input path="primaryValue" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">次要数据</label>
						<div class="controls">
							<form:input path="secondaryValue" class="form-control" />
							<div class="help-block">在主要数据基础上可以再额外维护一个次要数据，在业务逻辑中根据需要定制使用</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">文件路径数据</label>
						<div class="controls">
                            <form:input path="filePathValue" class="form-control" data-upload="single-file" />
							<div class="help-block">在主要数据基础上可以再额外维护一个文件路径数据，在业务逻辑中根据需要定制使用</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">图片路径数据</label>
						<div class="controls">
                            <div class="help-block">在主要数据基础上可以再额外维护一个图片路径数据，在业务逻辑中根据需要定制使用</div>
							<form:hidden path="imagePathValue" class="form-control"  data-multisplitimage="edit"/>
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
							<div class="help-block">数字越大，越靠前</div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">禁用标识</label>
						<div class="controls controls-radiobuttons">
							<form:radiobuttons path="disabled" items="${applicationScope.cons.booleanLabelMap}" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">HTML属性值</label>
						<div class="controls">
							<form:textarea path="richTextValue" class="form-control" data-htmleditor="kindeditor" data-height="400px"
								id="htmlValue" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn green" type="submit" data-post-dismiss="modal">保存</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>
