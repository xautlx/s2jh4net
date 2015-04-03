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
<title>用户账号基本信息</title>
</head>
<body>
	<form:form
		class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${ctx}/admin/auth/user/edit" method="post"
		modelAttribute="entity" data-editrulesurl="false">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="button">冻结账户</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
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
								<label class="control-label">用户ID</label>
								<div class="controls">
									<p class="form-control-static">${entity.id}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">手机号码</label>
								<div class="controls">
									<p class="form-control-static">${entity.user.mobile}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">用户类型</label>
								<div class="controls">
									<p class="form-control-static">${userTypeMap[entity.userType]}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">昵称</label>
								<div class="controls">
									<p class="form-control-static">${entity.user.nickName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">真实姓名</label>
								<div class="controls">
									<p class="form-control-static">${entity.trueName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">身份证号</label>
								<div class="controls">
									<p class="form-control-static">${entity.idCardNo}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">账户状态</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">注册时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${entity.user.signupTime}" type="both"
											pattern="yyyy-MM-dd HH:mm:ss" />
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">最近登录时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${entity.user.lastLogonTime}"
											type="both" pattern="yyyy-MM-dd HH:mm:ss" />
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
						<i class="fa fa-reorder"></i>学籍信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">认证状态</label>
								<div class="controls">
									<p class="form-control-static">
										<c:choose>
											<c:when test="${entity.siteUserExt.verifiedEdu==null}">
												未提交
											</c:when>
											<c:when test="${entity.siteUserExt.verifiedEdu}">
												已通过
											</c:when>
											<c:otherwise>
												未通过
											</c:otherwise>
										</c:choose>
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">认证通过时间</label>
								<div class="controls">
									<p class="form-control-static">
										<c:if test="${entity.siteUserExt.verifiedEdu}">
											<fmt:formatDate value="${entity.siteUserExt.verifiedEduTime}"
												type="both" pattern="yyyy-MM-dd HH:mm:ss" />
										</c:if>
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">审核人员</label>
								<div class="controls">
									<p class="form-control-static">${entity.siteUserExt.assignVerifyOperator.nickName}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学历</label>
								<div class="controls">
									<p class="form-control-static">
										${educationLevelMap[entity.educationLevel]}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学校</label>
								<div class="controls">
									<p class="form-control-static">${entity.schoolName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">入学年</label>
								<div class="controls">
									<p class="form-control-static">${entity.enrollmentDate}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">学院</label>
								<div class="controls">
									<p class="form-control-static">${entity.collegeName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">专业</label>
								<div class="controls">
									<p class="form-control-static">${entity.majorName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4"></div>
					</div>
				</div>
				<hr>
				<h5>学籍认证记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>申请时间</th>
							<th>认证结果</th>
							<th>认证时间</th>
							<th>拒绝原因</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${siteUserVerifyLogs}">
							<tr>
								<td><fmt:formatDate value="${item.verifySubmitTime}"
										type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><c:choose>
										<c:when test="${item.verifyPassed }">
										认证通过
									</c:when>
										<c:otherwise>认证不通过</c:otherwise>
									</c:choose></td>
								<td><fmt:formatDate
										value="${item.siteUserExt.verifiedEduTime}" type="both"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>${item.verifyFeedback }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="portlet gren">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i>信用信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用分数</label>
								<div class="controls">
									<p class="form-control-static">${entity.creditScore}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用等级</label>
								<div class="controls">
									<p class="form-control-static">${entity.creditLevel}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4"></div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用总额</label>
								<div class="controls">
									<p class="form-control-static">${entity.creditTotalAmount}
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">信用余额</label>
								<div class="controls">
									<p class="form-control-static">${userCapitalAccount!=null ? userCapitalAccount.creditRemainAmount:entity.creditTotalAmount}
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4"></div>
					</div>
				</div>
				<hr>
				<h5>信用分数变化记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>变化时间</th>
							<th>事件</th>
							<th>信用分数变化</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${siteUserCreditScoreLogs}">
							<tr>
								<td><fmt:formatDate value="${item.operationTime}"
										type="both" pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td>TODO</td>
								<td>${item.changeScore }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
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
											value="${userCapitalAccount!=null ? userCapitalAccount.totalApplyAmount:0}"
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
											value="${userCapitalAccount!=null ? userCapitalAccount.totalTobeRepayAmount:0}"
											pattern="#,##0.00" />

									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">逾期次数</label>
								<div class="controls">
									<p class="form-control-static">${userCapitalAccount!=null ? userCapitalAccount.totalFineInterestQuantity:0}
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
								<td><a href="${ctx}/w/borrow-in-apply/view?id=${item.id}"
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
						<i class="fa fa-reorder"></i>债权信息
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">还款中债权</label>
								<div class="controls">
									<p class="form-control-static">${userCapitalAccount!=null ? userCapitalAccount.totalRepayingDealQuantity:0}
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">债权价值</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${userCapitalAccount!=null ? userCapitalAccount.totalInvestAmountValue:0}"
											pattern="#,##0.00" />

									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">待收本息</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>债权记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>借款ID</th>
							<th>借款标题</th>
							<th>借款状态</th>
							<th>投资金额</th>
							<th>待收本息</th>
							<th>下一还款日</th>
							<th>下笔还款额</th>
							<th>交易时间</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowInvestDeals}">
							<tr>
								<td><a href="" data-toggle="modal-ajaxify">${item.borrowInApply.id}</a></td>
								<td>${item.borrowInApply.title}</td>
								<td><c:choose>
										<c:when test="$item.borrowInApply.state=='BIDDING' }">
										投标中
									</c:when>
										<c:when test="${item.borrowInApply.state=='DEALED' }">
										已满标
									</c:when>
										<c:when test="${item.borrowInApply.state=='RELAXING' }">
										还息中
									</c:when>
										<c:when test="${item.borrowInApply.state=='REPAYING' }">
										还本息中
									</c:when>
										<c:when test="${item.borrowInApply.state=='COMPLETED' }">
										已还款完成
									</c:when>
										<c:when test="${item.borrowInApply.state=='CANCELED' }">
										已流标
									</c:when>
									</c:choose></td>
								<td><fmt:formatNumber value="${item.investAmount}"
										pattern="#,##0.00" /></td>
								<td><fmt:formatNumber
										value="${item.borrowInApply.totalTobeRepayAmount*item.investQuantity/item.borrowInApply.applyQuantity}"
										pattern="#,##0.00" /></td>
								<td><fmt:formatDate
										value="${item.borrowInApply.nextRepayDate}" type="both"
										pattern="yyyy-MM-dd" /></td>
								<td><fmt:formatNumber
										value="${item.borrowInApply.nextTobeRepayAmount}"
										pattern="#,##0.00" /></td>
								<td>TODO</td>
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
								<label class="control-label">转让中的债权</label>
								<div class="controls">
									<p class="form-control-static">TODO
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">债权价值</label>
								<div class="controls">
									<p class="form-control-static">
										TODO
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">转让价格</label>
								<div class="controls">
									<p class="form-control-static">TODO</p>
								</div>
							</div>
						</div>
					</div>
				</div>
				<hr>
				<h5>债权转让记录</h5>
				<table class="table">
					<thead>
						<tr>
							<th>债权转让ID</th>
							<th>剩余份额</th>
							<th>每份价值</th>
							<th>转让系数</th>
							<th>每份价格</th>
							<th>转让时间</th>>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${borrowInvestTransferApplys}">
							<tr>
								<td><a href="" data-toggle="modal-ajaxify">${item.id}</a></td>
								<td>${item.transferRemainQuantity}</td>
								<td>TODO</td>
								<td>TODO</td>
								<td>TODO</td>
								<td>TODO</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="button">冻结账户</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>
