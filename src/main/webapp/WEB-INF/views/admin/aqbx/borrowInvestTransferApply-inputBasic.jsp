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
<title>债权转让详情</title>
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
								<label class="control-label">转让ID</label>
								<div class="controls">
									<p class="form-control-static">${entity.id}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">出让人</label>
								<div class="controls">
									<p class="form-control-static">${entity.investUser.user.nickName}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">借款ID</label>
								<div class="controls">
									<p class="form-control-static">${entity.borrowInApply.id}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">累计转入份额</label>
								<div class="controls">
									<p class="form-control-static">${entity.applyQuantity}</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">剩余份额</label>
								<div class="controls">
									<p class="form-control-static">${entity.transferRemainQuantity}</p>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">当前每份价值</label>
								<div class="controls">
									<p class="form-control-static"><fmt:formatNumber
											value="${entity.singleRemainCostAmount}" pattern="#0.00#" /></p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">当前转让系数</label>
								<div class="controls">
									<p class="form-control-static">
										100%
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-4">
							<div class="form-group">
								<label class="control-label">当前转让价格</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatNumber
											value="${entity.singleTransferCostAmount}" pattern="#0.00#" />
									</p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="portlet gren">
				<hr>
				<h5>转让操作记录TODO</h5>
				<table class="table">
					<thead>
						<tr>
							<th>操作内容</th>
							<th>操作时间</th>
							<th>转让份额</th>
							<th>当时每份价值</th>
							<th>转让系数</th>
							<th>当时每份价格</th>
							<th>转让费用率</th>
						</tr>
					</thead>
					<tbody>

					</tbody>
				</table>
				<hr>
				<h5>成交记录TODO</h5>
				<table class="table">
					<thead>
						<tr>
							<th>受让人</th>
							<th>转让份额</th>
							<th>转让时每份价值</th>
							<th>转让系数</th>
							<th>转让价格</th>
							<th>出售收入</th>
							<th>成交时间</th>
							<th>转让协议</th>
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
