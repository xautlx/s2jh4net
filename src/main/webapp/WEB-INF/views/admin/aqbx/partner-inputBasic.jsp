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
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${ctx}/admin/aqbx/partner/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-partner-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-partner-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">城市名字</label>
						<div class="controls">
							<form:input path="cityName" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商铺名称</label>
						<div class="controls">
							<form:input path="name" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商铺分类</label>
						<div class="controls">
							<form:select path="category" class="form-control">
								<c:forEach items="${categoryMap }" var="item">
									<option value="${item.key }" <c:if test="${item.key==entity.category }">selected="selected"</c:if>>
										${item.value }</option>
								</c:forEach>
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商户规模</label>
						<div class="controls">
							<form:input path="scaleDescn" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商铺地址</label>
						<div class="controls">
							<form:input path="address" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">联系人姓名</label>
						<div class="controls">
							<form:input path="linkman" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">联系人电话</label>
						<div class="controls">
							<form:input path="phone" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">联系人身份证号码</label>
						<div class="controls">
							<form:input path="idCardNo" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">状态</label>
						<div class="controls">
							<form:select path="cooperationState" class="form-control">
								<c:forEach items="${cooperationStateMap }" var="item">
									<option value="${item.key }" <c:if test="${item.key==entity.cooperationState }">selected="selected"</c:if>>
										${item.value }</option>
								</c:forEach>
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">签约日期</label>
						<div class="controls">
							<form:input path="dealDate" class="form-control" data-picker="date-time" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">营业执照编号</label>
						<div class="controls">
							<form:input path="businessLicenseNo" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">营业执照上传照片URL</label>
						<div class="controls">
							<form:hidden path="businessLicensePhoto" class="form-control" />
							<img src="<tags:img path='${entity.businessLicensePhoto}' />" id="LogoImageId" style="height: 80px; width: 80px" />
							<span class="btn green fileinput-button"> <i class="fa fa-plus"></i><span>点击上传</span>
							</span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-partner-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-partner-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
	<div class="modal fade" id="idVerifyModal" tabindex="-1" role="basic" aria-hidden="true">
		<form:form class="form-horizontal" action="${ctx}/w/image/upload" id="PhotoForm" enctype="multipart/form-data"
			method="post">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button data-dismiss="modal" class="close" type="button"></button>
						<h4 class="modal-title">图片上传</h4>
					</div>
					<div class="modal-body">
						<input type="file" name="photo" id="photo" />
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="submit" class="btn btn-primary">提交</button>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<script>
        $(document).ready(function() {
            $(".fileinput-button").click(function() {
                $("#photo").click();
            });

            $("#photo").change(function() {
                $("#PhotoForm").ajaxSubmit({
                    dataType : "json",
                    method : "post",
                    success : function(response) {
                        if (response.type == 'success') {
                            $("#businessLicensePhoto").val(response.data);
                            $("#LogoImageId").attr("src", READ_FILE_URL_PREFIX + response.data).show();
                            Global.notify('success', '图片上传成功', '');
                        }
                    }
                });
                return false;
            });

            if ('${entity.businessLicensePhoto}' == '') {
                $("#LogoImageId").hide();
            }

        });
    </script>
</body>
</html>