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
		action="${ctx}/admin/aqbx/student/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-student-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-student-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">姓名</label>
						<div class="controls">
			                <form:input path="trueName" class="form-control"/>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">身份证号码</label>
						<div class="controls">
			                <form:input path="idCardNo" class="form-control"/>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">手机号码</label>
						<div class="controls">
			                <form:input path="mobile" class="form-control"/>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">电子邮箱</label>
						<div class="controls">
			                <form:input path="email" class="form-control"/>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">性别</label>
						<div class="controls">
			                <form:radiobutton path="sex" value="1" />男
							<form:radiobutton path="sex" value="2"/>女
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">学历</label>
						<div class="controls">
							<form:select path="educationLevel" class="form-control" items="${educationLevelMap }">
							</form:select>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">学制</label>
						<div class="controls">
			                <form:select path="educationType" class="form-control" items="${educationTypeMap }">
		                    </form:select>
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">所属学校</label>
						<div class="controls">
			                <form:input path="schoolName" class="form-control autocomplete-edu-school" placeholder="请输入学校名称"
										data-target-name="schoolId" data-url="${eduApiUrlPrefix}/university/query" />
						<form:hidden path="schoolId"/>
						</div>
					</div>
	            </div>
	        </div>

	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">所属专业</label>
						<div class="controls">
			                <form:input path="majorName" class="form-control autocomplete-edu-major" placeholder="请输入专业名称"
										data-target-name="majorId" data-url="${eduApiUrlPrefix}/university/major/all" />
									<form:hidden path="majorId" />
						</div>
					</div>
	            </div>
	        </div>
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">审核状态</label>
						<div class="controls">
			                <form:input path="verified" class="form-control"/>
						</div>
					</div>
	            </div>
	        </div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-student-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-student-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>