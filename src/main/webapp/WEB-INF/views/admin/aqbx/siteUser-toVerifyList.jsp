<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>审核处理列表</title>
</head>
<body>
	<form class="form-validation" action="${ctx}/admin/p2p/verify/pass" method="post" data-editrulesurl="false">
		<c:forEach var="item" items="${pageData.content}">

			<div class="well" style="background-color: white">
				<input type="hidden" name="ids" value="${item.id}" />
				<div class="row">
					<div class="col-md-3">用户ID：<a target="modal-ajaxify" title="用户详情" href="${ctx}/admin/p2p/site-user/edit?id=${item.id}">${item.user.authUid}</a></div>
					<div class="col-md-2">拒绝次数：${item.siteUserExt.verifyRejectCount}</div>
					<div class="col-md-6">上次拒绝原因 ：${item.siteUserExt.verifyFeedback}</div>
					<div class="col-md-1">
						<button class="btn purple btn-post-url" type="button" data-url="${ctx}/admin/p2p/verify/reject?id=${item.id}"
							data-prompt="请输入拒绝原因" data-post-success="$(this).closest('.well').remove()">拒绝</button>
					</div>
				</div>
				<div class="row">
					<div class="col-md-1">${item.trueName}</div>
					<div class="col-md-2">${item.idCardNo}</div>
					<div class="col-md-1">${educationLevelMap[item.educationLevel]}</div>
					<div class="col-md-2">学校  ${item.schoolName}</div>
					<div class="col-md-2">学院  ${item.collegeName}</div>
					<div class="col-md-2">专业  ${item.majorName}</div>
					<div class="col-md-2">入学年  ${item.enrollmentDate}年</div>
				</div>
				<div class="row">
					<div class="col-md-5">
						<c:if test="${item.siteUserExt.idCardAndHeadPhoto!=null}">
							<img src="${applicationScope.cfg.read_file_url_prefix}${item.siteUserExt.idCardAndHeadPhoto}"></img>
						</c:if>
					</div>
					<div class="col-md-2">
						<div class="row">
							<div class="col-md-12">
								<c:if test="${item.siteUserExt.positiveIdCardPhoto!=null}">
									<img src="${applicationScope.cfg.read_file_url_prefix}${item.siteUserExt.positiveIdCardPhoto}"></img>
								</c:if>
							</div>
						</div>
						<div class="row">
							<div class="col-md-12">
								<c:if test="${item.siteUserExt.negativeIdCardPhoto!=null}">
									<img src="${applicationScope.cfg.read_file_url_prefix}${item.siteUserExt.negativeIdCardPhoto}"></img>
								</c:if>
							</div>
						</div>
					</div>
					<div class="col-md-5">
						<c:if test="${item.siteUserExt.xueXinPhoto!=null}">
							<img src="${applicationScope.cfg.read_file_url_prefix}${item.siteUserExt.xueXinPhoto}"></img>
						</c:if>
					</div>
				</div>
			</div>
		</c:forEach>
		<div class="form-actions right">
			未被拒绝的用户均视为认证通过
			<button class="btn blue" type="submit" data-post-reload="container">
				<i class="fa fa-check"></i> 提 交 信 息
			</button>
		</div>
	</form>
	<tags:pagination page="${pageData}" />
</body>
</html>