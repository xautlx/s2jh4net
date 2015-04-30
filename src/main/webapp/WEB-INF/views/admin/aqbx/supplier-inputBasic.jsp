<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation" id="supplierEditForm"
		action="${ctx}/admin/aqbx/supplier/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-supplier-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-supplier-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供商名称</label>
						<div class="controls">
							<form:input path="name" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供商Logo</label>
						<div class="controls">
							<a href="javascript:;" class="btn-file-upload"> <img class="img-file-upload"
								src="${empty entity.logo?ctx.concat('/assets/w/app/images/upload.png'):readFileUrlPrefix.concat(entity.logo)}"></img>
							</a>
							<form:hidden path="logo" class="input-file-upload" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供商官网</label>
						<div class="controls">
							<form:input path="webSite" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供商联系人</label>
						<div class="controls">
							<form:input path="contacter" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">供商联系人电话</label>
						<div class="controls">
							<form:input path="contacterTel" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">客服QQ</label>
						<div class="controls">
							<form:input path="ustomerServiceQQ" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">客服电话</label>
						<div class="controls">
							<form:input path="customerServiceTel" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">客服邮箱</label>
						<div class="controls">
							<form:input path="email" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">备注</label>
						<div class="controls">
							<form:textarea path="note" class="form-control" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-supplier-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-supplier-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
	<script>
        $(document).ready(function() {
            $("#supplierEditForm .btn-file-upload").on("image.upload.success", function(event, src) {
                var $btn = $(this);
                var $row = $btn.closest(".controls");
                $row.find(".img-file-upload").attr("src", READ_FILE_URL_PREFIX + src);
                $row.find(".input-file-upload").val(src);
            })
        });
    </script>
</body>
</html>