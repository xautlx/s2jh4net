<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="note note-info">
	<p>UI示例组件对应源码请直接参考对应代码：src\main\webapp\WEB-INF\views\admin\docs\ui-feature-items.jsp和
		lab.s2jh.support.web.DocumentController</p>
</div>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#tab-auto">基础组件</a></li>
	<li><a data-toggle="tab" href="#tab-auto">下拉、选取等处理组件</a></li>
	<li><a data-toggle="tab" href="#tab-auto">日期、时间等处理组件</a></li>
	<li><a data-toggle="tab" href="#tab-auto">Text、HTML等处理组件</a></li>
	<li><a data-toggle="tab" href="#tab-auto">文件、图片等处理组件</a></li>
	<li><a data-toggle="tab" href="#tab-auto">动态表格等处理组件</a></li>
</ul>
<div class="tab-content">
	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label" data-tooltips="[输入提示说明内容]" data-tooltipster-position="bottom">普通文本输入元素/输入提示</label>
							<div class="controls">
								<input type="text" class="form-control" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initControlLabelTooltips" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">个人收藏支持功能的表单输入组件</label>
							<div class="controls">
								<form:input path="textContent" class="form-control" data-profile-param="just-mock-test" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDataProfileParam" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">基于zTree的树形选取组件</label>
							<div class="controls">
								<form:hidden path="department.id" />
								<form:input path="department.display" class="form-control" data-toggle="tree-select"
									data-url="${ctx}/docs/mock/tree-datas.json" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTreeSelect" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">以Dropdown形式弹出AJAX加载DIV区域</label>
							<div class="controls">
								<form:input path="selectedId" class="form-control" data-toggle="dropdown-select"
									data-url="${ctx}/docs/ui-feature/dropdownselect" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDropdownSelect" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">基于Select2组件扩展标准的下拉框组件</label>
							<div class="controls">
								<form:select path="selectedId" items="${multiSelectItems}" class="form-control" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">基于Select2组件combobox下拉可输入组件</label>
							<div class="controls">
								<form:select path="selectedId" items="${multiSelectItems}" data-select2-type="combobox" class="form-control" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">基于输入内容remote查询列表选取组件</label>
							<div class="controls">
								<form:input path="selectedId" class="form-control" data-select2-type="remote" data-display="${'当前显示字面值'}"
									data-url="${ctx}/admin/auth/user/list" data-query="search['CN_authUid_OR_nickName_OR_email_OR_mobile']" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Remote" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">自由输入多个Tag(从标签数组中选取)</label>
						<div class="controls">
							<form:input path="splitText" class="form-control" data-select2-type="tags" value="A,D" data-tags="A,B,C,D" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Tags" target="jsdoc">JSDoc</a></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">选取多个Tag(Remote查询选取)</label>
						<div class="controls">
							<form:input path="splitText" class="form-control" data-select2-type="tags" value="A,D"
								data-url="${ctx}/docs/mock/tags" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Tags" target="jsdoc">JSDoc</a></span>
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">基于multiSelect封装的左右互选组件</label>
						<div class="controls">
							<form:select path="selectedIds" items="${multiSelectItems}" class="form-control"
								data-toggle="double-multi-select" data-height="300px" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initMultiSelectDouble" target="jsdoc">JSDoc</a></span>
				</div>
			</div>
		</form:form>
	</div>
	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">年月日 日期选取组件</label>
							<div class="controls">
								<form:input path="saleDate" class="form-control" data-picker="date" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDatePicker" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">年月日-时分 选取组件</label>
							<div class="controls">
								<form:input path="publishTime" class="form-control" data-picker="date-time" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDateTimePicker" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">时间段 选取组件</label>
							<div class="controls">
								<form:input path="searchDate" class="form-control" data-picker="date-range" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDateRangePicker" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
		</form:form>
	</div>

	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-body">
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">Textarea文本框可输入字符数显示和校验</label>
							<div class="controls">
								<form:textarea path="textContent" class="form-control" maxlength="100" />
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTextareaMaxlength" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">将Textarea转换为HTML可视化编辑器</label>
							<div class="controls">
								<form:textarea path="htmlContent" class="form-control" data-htmleditor='kindeditor' data-height="400" />
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTextareaHtmleditor" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-body">
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">单文件上传组件</label>
							<div class="controls">
								<form:input path="filePath" class="form-control" data-upload="single-file" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadSingleFile" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">单图片文件上传预览组件</label>
							<div class="controls">
								<form:hidden path="filePath" class="form-control" data-upload="single-image" data-image-width="80"
									data-image-height="80" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadSingleImage" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-6">
						<div class="form-group">
							<label class="control-label">多图片文件上传预览组件</label>
							<div class="controls">
								<c:forEach items="${imagePaths}" var="item" varStatus="status">
									<input type="hidden" name="imagePaths[${status.index}]" data-multiimage="edit" data-pk="${status.index}"
										value="${item}" />
								</c:forEach>
								<input type="hidden" name="imagePaths[x]" data-multiimage="btn" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadMultiImage" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
		</form:form>
	</div>

	<div class="tab-pane">
		<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
			action="${ctx}/docs/mock/dynamic-table" method="post" data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}"
			modelAttribute="entity">
			<form:hidden path="id" />
			<form:hidden path="version" />
			<div class="form-actions">
				<button class="btn blue" type="submit" data-reload="false">
					<i class="fa fa-check"></i> 保存
				</button>
				<button class="btn default" type="button" data-dismiss="modal">取消</button>
			</div>
			<div class="form-body">

				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">标题</label>
							<div class="controls">
								<input type="text" class="form-control" name="textContent" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">数量</label>
							<div class="controls">
								<input type="text" class="form-control" name="quantity" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">选取多个Tag(Remote查询选取)</label>
							<div class="controls">
								<form:input path="splitText" class="form-control" data-select2-type="tags" value=""
									data-url="${ctx}/docs/mock/tags" />
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">关联动态行项</label>
							<div class="controls">
								<table class="table table-r2list" data-dynamic-table="true">
									<thead>
										<tr>
											<th>下拉选项</th>
											<th>日期选项</th>
											<th>文本框选项</th>
											<th>文件上传选项</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${entity.mockItemEntites}" varStatus="status">
											<tr>
												<input type="hidden" name="mockItemEntites[${status.index}].extraAttributes['operation']" value="update" />
												<input type="hidden" name="mockItemEntites[${status.index}].mockEntity.id" value="${entity.id}" />
												<td><form:hidden path="mockItemEntites[${status.index}].department.id" /> <form:input
														path="mockItemEntites[${status.index}].department.display" class="form-control" data-toggle="tree-select"
														data-url="${ctx}/docs/mock/tree-datas.json" /></td>
												<td><form:input path="mockItemEntites[${status.index}].saleDate" class="form-control"
														data-picker="date" /></td>
												<td><form:input path="mockItemEntites[${status.index}].textContent" class="form-control" /></td>
												<td><form:input path="mockItemEntites[${status.index}].imagePath" class="form-control"
														data-upload="single-file" /></td>
											</tr>
										</c:forEach>
										<tr class="dynamic-table-row-template">
											<input type="hidden" name="mockItemEntites[X].extraAttributes['operation']" value="create" />
											<input type="hidden" name="mockItemEntites[X].assignment.id" value="${entity.id}" />
											<td><input type="hidden" name="mockItemEntites[X].department.id" /> <input type="text"
												name="mockItemEntites[X].department.display" class="form-control" data-toggle="tree-select"
												data-url="${ctx}/docs/mock/tree-datas.json" /></td>
											<td><input type="text" name="mockItemEntites[X].saleDate" class="form-control" data-picker="date" /></td>
											<td><input type="text" name="mockItemEntites[X].textContent" class="form-control" /></td>
											<td><input type="text" name="mockItemEntites[X].imagePath" class="form-control"
												data-upload="single-file" /></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDynamicTable" target="jsdoc">JSDoc</a></span>
					</div>
				</div>
			</div>
			<div class="form-actions right">
				<button class="btn blue" type="submit" data-reload="false">
					<i class="fa fa-check"></i> 保存
				</button>
				<button class="btn default" type="button" data-dismiss="modal">取消</button>
			</div>
		</form:form>
	</div>
</div>
