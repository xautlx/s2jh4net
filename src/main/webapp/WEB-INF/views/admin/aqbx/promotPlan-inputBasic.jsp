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
		action="${ctx}/admin/aqbx/promot-plan/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-promot-plan-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-promot-plan-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">方案标题</label>
						<div class="controls">
							<form:input path="title" class="form-control" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">方案编号</label>
						<div class="controls">
							<p class="form-control-static">
								<c:choose>
									<c:when test="${entity.id eq null }">自动生成</c:when>
									<c:otherwise>
									${entity.id}
								</c:otherwise>
								</c:choose>
							</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">推广商品</label>
						<div class="controls">
							<form:select path="product.id" items="${productItems}" class="form-control" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">创建时间</label>
						<div class="controls">
							<p class="form-control-static">${entity.createdDate}</p>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">方案内容</label>
						<div class="controls">
							<form:textarea path="description" class="form-control" maxlength="2000" />
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">效果图</label>
						<div class="controls">
							<table class="table table-r2list" data-dynamic-table="true">
								<thead>
									<tr>
										<!-- <th>效果图</th> -->
										<th>说明</th>
										<th>上传效果图</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${entity.promotPlanImages}" varStatus="status">
										<tr>
											<input type="hidden" name="promotPlanImages[${status.index}].extraAttributes['operation']" value="update" />
											<input type="hidden" name="promotPlanImages[${status.index}].promotPlan.id" value="${entity.id}" />

											<td><form:input path="promotPlanImages[${status.index}].description" class="form-control" /></td>
											<td><form:input path="promotPlanImages[${status.index}].imagePath" class="form-control"
													data-upload="single-file" /></td>
										</tr>
									</c:forEach>
									<tr class="dynamic-table-row-template">
										<input type="hidden" name="promotPlanImages[X].promotPlan.id" value="${entity.id }"></input>
										<td><input type="text" name="promotPlanImages[X].description" class="form-control" /></td>
										<td><input type="text" name="promotPlanImages[X].imagePath" class="form-control"
											data-upload="single-file" /></td>

									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>

			<div class="row">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">材料</label>
						<div class="controls">
							<table class="table table-r2list" data-dynamic-table="true">
								<thead>
									<tr>
										<th>材料类型</th>
										<th>材料数量</th>

									</tr>
								</thead>
								<tbody>
									<c:forEach var="item" items="${entity.promotPlanMaterials}" varStatus="status">
										<tr>
											<input type="hidden" name="promotPlanMaterials[${status.index}].extraAttributes['operation']" value="update" />
											<input type="hidden" name="promotPlanMaterials[${status.index}].promotPlan.id" value="${entity.id}" />

											<td><form:select path="promotPlanMaterials[${status.index}].promotMaterial.id"
													items="${promotMaterialItems}" /></td>
											<td><form:input path="promotPlanMaterials[${status.index}].quantity" class="form-control" /></td>
										</tr>
									</c:forEach>
									<tr class="dynamic-table-row-template">
										<input type="hidden" name="promotPlanMaterials[X].promotPlan.id" value="${entity.id}" />
										<td><select name="promotPlanMaterials[X].promotMaterial.id" class="form-control">
												<c:forEach var="item" items="${promotMaterialItems}">
													<option value="${item.key}">${item.value}</option>
												</c:forEach>
												<option></option>
										</select></td>
										<td><input type="text" name="promotPlanMaterials[X].quantity" class="form-control" /></td>
									</tr>

								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">方案停用</label>
						<div class="controls controls-radiobuttons">
							<form:radiobuttons path="expired" items="${applicationScope.cons.booleanLabelMap}" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">停用原因</label>
						<div class="controls">
							<form:textarea path="expireExplain" class="form-control" maxlength="512" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-aqbx-promot-plan-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-aqbx-promot-plan-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form:form>
</body>
</html>