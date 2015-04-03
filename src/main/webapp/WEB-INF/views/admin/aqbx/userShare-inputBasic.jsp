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
		action="${ctx}/admin/aqbx/user-share/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-user-share-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-user-share-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享唯一标识</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.shareUid}</p>
								</c:when>
								<c:otherwise>
									<form:input path="shareUid" class="form-control" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">关联用户</label>
						<div class="controls">
							<form:hidden path="siteUser.id" class="form-control" data-select2-type="remote"
								data-url="${ctx}/admin/aqbx/site-user/list" data-display="${siteUser.display}"
								data-query="search['CN_user.authUid_OR_user.nickName_OR_user.mobile_OR_user.email']" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商品</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.productId}</p>
								</c:when>
								<c:otherwise>
									<form:input path="productId" class="form-control" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">商品SKU</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.productSKUId}</p>
								</c:when>
								<c:otherwise>
									<form:input path="productSKUId" class="form-control" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享渠道</label>
						<div class="controls">
							<form:input path="shareTarget" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享链接</label>
						<div class="controls">
							<form:input path="shareUrl" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享文本</label>
						<div class="controls">
							<form:input path="shareText" class="form-control" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享创建时间</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.shareTime}</p>
								</c:when>
								<c:otherwise>
									<form:input path="shareTime" class="form-control" data-toggle="datetimepicker" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">成功处理时间</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.successTime}</p>
								</c:when>
								<c:otherwise>
									<form:input path="successTime" class="form-control" data-toggle="datetimepicker" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享首次访问时间</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.firstVisitTime}</p>
								</c:when>
								<c:otherwise>
									<form:input path="firstVisitTime" class="form-control" data-toggle="datetimepicker" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享最近访问时间</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.lastVisitTime}</p>
								</c:when>
								<c:otherwise>
									<form:input path="lastVisitTime" class="form-control" data-toggle="datetimepicker" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">分享被访问统计次数</label>
						<div class="controls">
							<c:choose>
								<c:when test="${entity.notNew}">
									<p class="form-control-static">${entity.visitTotalCount}</p>
								</c:when>
								<c:otherwise>
									<form:input path="visitTotalCount" class="form-control" />
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-user-share-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-user-share-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>