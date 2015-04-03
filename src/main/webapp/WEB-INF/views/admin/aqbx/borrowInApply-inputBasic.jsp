<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>借款详情</title>
</head>
<body>
	<form:form
		class="form-horizontal form-bordered form-label-stripped form-validation"
		action="" method="post"
		modelAttribute="entity" data-editrulesurl="false">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-body">
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>基本信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">借款ID</label>
								<div class="controls">
									<p class="form-control-static">${entity.id}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">借款人</label>
								<div class="controls">
									<p class="form-control-static">${entity.borrowUser.user.nickName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">用户类型</label>
								<div class="controls">
									<p class="form-control-static">${borrowInApplyStateMap[entity.state]}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">借款金额</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatNumber value="${entity.applyAmount}" pattern="#,##0.00" /></p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">约定宽限期</label>
								<div class="controls">
									<p class="form-control-static">${entity.relaxMonths}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">约定还款期</label>
								<div class="controls">
									<p class="form-control-static">${entity.repayMonths}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">年化利率</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatNumber
											value="${entity.borrowRate*12*100}" pattern="#0.00#" />%</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">平台月费率</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${entity.mgmtChargeRate*100}" pattern="#0.00#" />%
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">服务费率</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${entity.serviceChargeRate*100}" pattern="#0.00#" />%
									</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">申请时间</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatDate value="${entity.applyDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" /></p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">成交时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${entity.dealDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">还清时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${entity.repayedDate}" type="both" pattern="yyyy-MM-dd HH:mm:ss" />
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>还款信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">待还本息</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatNumber value="${entity.totalTobeRepayAmount}" pattern="#,##0.00" /></p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">待还本金</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">待还利息</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">还款开始日</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">剩余宽限期</label>
								<div class="controls">
									<p class="form-control-static">${entity.remainRelaxMonths}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">剩余还款期</label>
								<div class="controls">
									<p class="form-control-static">${entity.remainRepayMonths}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">逾期次数</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">未还罚息</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">提前还款</label>
								<div class="controls">
									<p class="form-control-static">
										<c:choose>
											<c:when test="${entity.totalRepayAllChargeAmount>0 }">
												是
											</c:when>
											<c:otherwise>
												否
											</c:otherwise>
										</c:choose>
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">提前还款额</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">提前还款时间</label>
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
				<h5>还款计划/记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>期数</th>
							<th>还款状态</th>
							<th>约定还款日</th>
							<th>实际还款日</th>
							<th>应还本息</th>
							<th>应还本金</th>
							<th>应还利息</th>
							<th>还款后剩余本金</th>
							<th>逾期天数</th>
							<th>逾期罚息</th>
							<th>逾期管理费</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowRepayPlans}">
							<tr>
								<td>${item.orderIndex }</td>
								<td><c:choose>
									<c:when test="${item.repayedDate==null }">
										未还款
									</c:when>
									<c:otherwise>
										已还款
									</c:otherwise>
								</c:choose></td>
								<td><fmt:formatDate value="${item.planDate}" type="both" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatDate value="${item.repayedDate}" type="both" pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatNumber value="${item.repayMonthAmount}" pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.repayPrincipalAmount}" pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.repayInterestAmount}" pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.remainPrincipalAmount}" pattern="#,##0.00" /></td>
								<td>${item.borrowInApply.distanceFromNextRepayDate }</td>
								<td><fmt:formatNumber value="${item.fineInterestAmount}" pattern="#,##0.00" /></td>
								<td><fmt:formatNumber value="${item.fineMgmtAmount}" pattern="#,##0.00" /></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>债权人信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">债权人数量</label>
								<div class="controls">
									<p class="form-control-static">${fn:length(borrowInvestDeals)}</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>债权人列表</h5>
				<table class="table">
					<thead>
						<tr>
							<th>债权人</th>
							<th>投资金额</th>
							<th>持有份额</th>
							<th>占有比例</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowInvestDeals}">
							<tr>
								<td>${item.investUser.user.nickName }</td>
								<td><fmt:formatNumber value="${item.investAmount}" pattern="#,##0.00" /></td>
								<td>${item.investQuantity}</td>
								<td><fmt:formatNumber value="${item.investQuantity*100/item.borrowInApply.applyQuantity}" pattern="#,##0.00" />%</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>债权转让信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">转让次数</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>转让列表TODO</h5>
				<table class="table">
					<thead>
						<tr>
							<th>转让ID</th>
							<th>出让人</th>
							<th>受让人</th>
							<th>转让份额</th>
							<th>转让时每份价格</th>
							<th>转让系数</th>
							<th>转让价格</th>
							<th>成交时间</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
			</div>
		</div>
	</form:form>
</body>
</html>
