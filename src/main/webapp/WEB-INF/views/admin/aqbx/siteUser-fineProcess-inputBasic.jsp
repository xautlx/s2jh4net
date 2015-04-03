<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>逾期详情</title>
</head>
<body>
	<form:form
		class="form-horizontal form-bordered form-label-stripped form-validation"
		action="" method="post"
		modelAttribute="entity" data-editrulesurl="false">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue btn-post-url" type="button" data-url="${ctx}/admin/p2p/fine-process/log?id=${entity.id}"
							data-prompt="处理记录" data-post-success="modal">添加处理记录</button>
			<button class="btn default" type="button">冻结账户</button>
		</div>
		<div class="form-body">
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>用户信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">用户ID</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.id}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">手机号码</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.user.mobile}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">账户状态</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">昵称</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.user.nickName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">真实姓名</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.trueName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">身份证号</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.idCardNo}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用分数</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.creditScore }</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用等级</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.creditLevel }
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用总额</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.creditTotalAmount}
									</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学历</label>
								<div class="controls">
									<p class="form-control-static">${educationLevelMap[entity.siteUser.educationLevel] }</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学校</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.schoolName }
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">入学年</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.enrollmentDate}
									</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学院</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUser.collegeName }</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">专业</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.majorName }
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">审核人员</label>
								<div class="controls">
									<p class="form-control-static">
										${entity.siteUser.siteUserExt.assignVerifyOperator.nickName}
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>相关资料</h5>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<c:if test="${entity.siteUser.siteUserExt.idCardAndHeadPhoto!=null}">
								<img src="${applicationScope.cfg.image_view_url_prefix}${entity.siteUser.siteUserExt.idCardAndHeadPhoto}"></img>
							</c:if>
						</div>
						<div class="col-md-4">
							<div class="row">
								<div class="col-md-12">
									<c:if test="${entity.siteUser.siteUserExt.positiveIdCardPhoto!=null}">
										<img src="${applicationScope.cfg.image_view_url_prefix}${entity.siteUser.siteUserExt.positiveIdCardPhoto}"></img>
									</c:if>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12">
									<c:if test="${entity.siteUser.siteUserExt.negativeIdCardPhoto!=null}">
										<img src="${applicationScope.cfg.image_view_url_prefix}${entity.siteUser.siteUserExt.negativeIdCardPhoto}"></img>
									</c:if>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<c:if test="${entity.siteUser.siteUserExt.xueXinPhoto!=null}">
								<img src="${applicationScope.cfg.image_view_url_prefix}${entity.siteUser.siteUserExt.xueXinPhoto}"></img>
							</c:if>
						</div>
					</div>
				</div>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>借款信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">申请借款</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">成功借款</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">还清借款</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">累计借款金额</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${entity!=null ? entity.totalApplyAmount:0}"
											pattern="#,##0.00" />
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">待还本息</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${entity!=null ? entity.totalTobeRepayAmount:0}"
											pattern="#,##0.00" />

									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">逾期次数</label>
								<div class="controls">
									<p class="form-control-static">${entity!=null ? entity.totalFineInterestQuantity:0}
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>借款记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>借款ID</th>
							<th>借款标题</th>
							<th>借款状态</th>
							<th>借款金额</th>
							<th>年化利率</th>
							<th>交易时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowInApplies}">
							<tr>
								<td><a href="${ctx}/admin/p2p/borrow-in-apply/edit?id=${item.id}"
									data-toggle="modal-ajaxify">${item.id}</a></td>
								<td>${item.title}</td>
								<td><c:choose>
										<c:when test="${item.state=='BIDDING' }">
										投标中
									</c:when>
										<c:when test="${item.state=='DEALED' }">
										已满标
									</c:when>
										<c:when test="${item.state=='RELAXING' }">
										还息中
									</c:when>
										<c:when test="${item.state=='REPAYING' }">
										还本息中
									</c:when>
										<c:when test="${item.state=='COMPLETED' }">
										已还款完成
									</c:when>
										<c:when test="${item.state=='CANCELED' }">
										已流标
									</c:when>
									</c:choose></td>
								<td><fmt:formatNumber value="${item.applyAmount}"
										pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.borrowRate*12*100}"
										pattern="#0.00#" />%</td>
								<td><fmt:formatDate value="${item.dealDate}" type="both"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>逾期信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">当前逾期款项</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">当前最高逾期时长</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">累计逾期次数</label>
								<div class="controls">
									<p class="form-control-static">${entity!=null ? entity.totalFineInterestQuantity:0}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">逾期金额</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">罚息合计</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">逾期管理费合计</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>逾期记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>借款ID</th>
							<th>期数</th>
							<th>预定还款日</th>
							<th>逾期金额</th>
							<th>逾期时长</th>
							<th>罚息</th>
							<th>逾期管理费</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowRepayPlans}">
							<tr>
								<td><a href="${ctx}/admin/p2p/borrow-in-apply/edit?id=${item.borrowInApply.id}"
									data-toggle="modal-ajaxify">${item.borrowInApply.id}</a></td>
								<td>${item.orderIndex}</td>
								<td><fmt:formatDate value="${item.planDate}" type="both"
										pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatNumber value="${item.repayMonthAmount}"
										pattern="#,##0.00" /></td>
								<td>TODO</td>
								<td><fmt:formatNumber value="${item.fineInterestAmount}"
										pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.fineMgmtAmount}"
										pattern="#,##0.00" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>逾期处理
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">处理次数</label>
								<div class="controls">
									<p class="form-control-static">${entity.totalFineProcessQuantity }</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">上次处理时间</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatDate value="${entity.lastFineProcessDate}" type="both"
										pattern="yyyy-MM-dd HH:mm:ss" /></p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>处理记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>处理时间</th>
							<th>处理说明</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${SiteUserFineProcessLogs}">
							<tr>
								<td><fmt:formatDate value="${item.operationTime}" type="both"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${item.processLog}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>
</html>
