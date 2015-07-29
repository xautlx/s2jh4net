<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<div class="note note-info">
	<ul>
		<li>UI示例组件对应源码请直接参考对应代码：src\main\webapp\WEB-INF\views\admin\docs\ui-feature-items.jsp和
			lab.s2jh.support.web.DocumentController</li>
		<li>点击元素后方的"JSDoc"链接可查看组件的具体用法Javascript注释文档</li>
		<li>鼠标双击portlet标题区域可以切换单个展开或收拢
			<div data-toggle="buttons" class="btn-group">
				<label class="btn btn-default active" onclick="$('#docs-demo-list a.expand').click()"> <input type="radio"
					class="toggle"> 展开全部
				</label> <label class="btn btn-default" onclick="$('#docs-demo-list a.collapse').click()"> <input type="radio"
					class="toggle"> 收拢全部
				</label>
			</div>
		</li>
	</ul>
</div>

<div id="docs-demo-list">
	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 基础组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation" action="${ctx}/todo"
				method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label" data-tooltips="[输入提示说明内容]" data-tooltipster-position="bottom">普通文本输入元素/输入提示</label>
								<div class="controls">
									<input type="text" class="form-control" /> <span class="help-block">鼠标移动到label区域自动生成的小图标会出现美化的tooltips内容</span>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initControlLabelTooltips" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">个人收藏支持功能的表单输入组件</label>
								<div class="controls">
									<form:input path="textContent" class="form-control" data-profile-param="just-mock-test" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDataProfileParam" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<div class="form-group">
								<label class="control-label">单选按钮组</label>
								<div class="controls controls-radiobuttons">
									<form:radiobuttons path="expired" items="${applicationScope.cons.booleanLabelMap}" />
									<span class="help-block">在对应所属的controls元素上追加样式controls-radiobuttons</span>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label class="control-label">复选按钮组</label>
								<div class="controls controls-checkboxes">
									<form:checkboxes path="expired" items="${applicationScope.cons.booleanLabelMap}" />
									<span class="help-block">在对应所属的controls元素上追加样式controls-checkboxes</span>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">表单数据显示</label>
								<div class="controls">
									<p class="form-control-static">[动态显示内容]</p>
									<span class="help-block">为了对齐效果给显示文本内容添加p.form-control-static包裹</span>
								</div>
							</div>
						</div>
						<div class="col-md-2"></div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 表单控制及校验
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
				action="${ctx}/docs/mock/validation-confirm" method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-body">
					<div class="row" data-equal-height="false">
						<div class="col-md-6">
							<div class="form-group">
								<label class="control-label">发货开始日期</label>
								<div class="controls">
									<input type="text" class="form-control" name="startDate" required="true" data-rule-dateLT="endDate"
										data-picker="date"><span class="help-block">日期前后关系关联校验</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">发货结束日期</label>
								<div class="controls">
									<input type="text" class="form-control" name="endDate" required="true" data-rule-dateGT="startDate"
										data-picker="date"><span class="help-block">日期前后关系关联校验</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">计划发货量</label>
								<div class="controls">
									<input type="text" class="form-control" name="quantity" required="true" data-rule-digits="true"> <span
										class="help-block">模拟规则：大于100提交表单触发confirm确认提示，小于100通过</span>
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<p class="form-control-static">当前页面相关表单和框架已有基础功能包含一系列表单控制处理演示，可直接参考各功能点设计实现</p>
							<p class="form-control-static">点击相关链接可查阅主要技术点文说明文档:</p>
							<span class="help-block"><a href="${ctx}/docs/markdown/表单控制#get" target="_blank">列表界面的GET查询表单</a></span> <span
								class="help-block"><a href="${ctx}/docs/markdown/表单控制#post" target="_blank">编辑界面的POST提交表单</a></span><span
								class="help-block"><a href="${ctx}/docs/markdown/表单控制#jsr303" target="_blank">集成服务端和客户端的Form/Data
									Validation的设计处理</a></span> <span class="help-block"><a href="${ctx}/docs/markdown/表单控制#rules" target="_blank">基于JQuery
									Validation及扩展的校验语法规则定义</a></span> <span class="help-block"><a href="${ctx}/docs/markdown/表单控制#confirm"
								target="_blank">Form表单数据的提交Confirm确认处理</a></span>
						</div>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn blue" type="submit" data-success-reload="false">
						<i class="fa fa-check"></i> 演示服务端确认校验
					</button>
					<button class="btn default" type="button" data-dismiss="modal">取消</button>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 扩展组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation" action="${ctx}/todo"
				method="post" data-editrulesurl="false" modelAttribute="entity">
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">手机号码</label>
								<div class="controls">
									<input type="text" class="form-control" name="mobile" required="true" data-rule-mobile="true"> <span
										class="help-block">手机号码格式校验；输入手机号用于发送验证码关联校验</span>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">手机验证码</label>
								<div class="controls">
									<div class="input-group">
										<input type="text" class="form-control" name="code" required="true"> <span class="input-group-btn">
											<button type="button" class="btn red btn-send-sms-code" data-mobile-el="mobile">获取验证码</button>
										</span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindBtnSendSmsCode" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<hr>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">图片验证码</label>
								<div class="controls">
									<div class="input-group">
										<div class="input-icon">
											<i class="fa fa-qrcode"></i> <input class="form-control captcha-text" type="text" autocomplete="off"
												placeholder="验证码...看不清可点击图片可刷新" name="captcha" required="true" />
										</div>
										<span class="input-group-btn" style="cursor: pointer;"> <img alt="验证码" class="captcha-img"
											src="${ctx}/assets/img/captcha_placeholder.jpg" title="看不清？点击刷新" />
										</span>
									</div>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindCaptchaCode" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<hr>
						</div>
					</div>

					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">触发弹窗组件</label>
								<div class="controls">
									<p class="form-control-static">
										<a title="修改密码" data-modal-size="600px" data-toggle="modal-ajaxify" href="/aqbx/admin/profile/password">修改密码</a>
									</p>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindModalAjaxify" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">按钮发起POST请求</label>
								<div class="controls">
									<button class="btn blue btn-post-url" type="button" data-url="${ctx}/docs/mock/btn-post" data-confirm="确认提交请求？">模拟按钮POST请求</button>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindBtnPostUrl" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">扩展AJAX加载Tab组件</label>
								<div class="controls">
									<p class="form-control-static">扩展Bootstrap默认的Tab组件支持AJAX加载页面，详见各功能编辑tab界面</p>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindExtBootstrapTab" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 下拉、选取等处理组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
				action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">基于zTree的树形选取组件</label>
								<div class="controls">
									<form:hidden path="department.id" />
									<form:input path="department.display" class="form-control" data-toggle="tree-select"
										data-url="${ctx}/docs/mock/tree-datas.json" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTreeSelect" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">以Dropdown形式弹出AJAX加载DIV区域</label>
								<div class="controls">
									<form:input path="selectedId" class="form-control" data-toggle="dropdown-select"
										data-url="${ctx}/docs/ui-feature/dropdownselect" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDropdownSelect" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">基于Select2组件扩展标准的下拉框组件</label>
								<div class="controls">
									<form:select path="selectedId" items="${multiSelectItems}" class="form-control" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">基于Select2组件combobox下拉可输入组件</label>
								<div class="controls">
									<form:select path="selectedId" items="${multiSelectItems}" data-select2-type="combobox" class="form-control" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">基于输入内容remote查询列表选取组件</label>
								<div class="controls">
									<form:input path="selectedId" class="form-control" data-select2-type="remote" data-display="${'当前显示字面值'}"
										data-url="${ctx}/admin/auth/user/list" data-query="search['CN_authUid_OR_nickName_OR_email_OR_mobile']" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Remote" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">自由输入多个Tag(从标签数组中选取)</label>
							<div class="controls">
								<form:input path="splitText" class="form-control" data-select2-type="tags" value="A,D" data-tags="A,B,C,D" />
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Tags" target="_blank">Javascript用法文档</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">选取多个Tag(Remote查询选取)</label>
							<div class="controls">
								<form:input path="splitText" class="form-control" data-select2-type="tags" value="A,D"
									data-url="${ctx}/docs/mock/tags" />
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2Tags" target="_blank">Javascript用法文档</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">基于multiSelect封装的左右互选组件</label>
							<div class="controls">
								<form:select path="selectedIds" items="${multiSelectItems}" class="form-control"
									data-toggle="double-multi-select" data-height="300px" />
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initMultiSelectDouble" target="_blank">Javascript用法文档</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<hr>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">下拉级联组件-触发元素</label>
							<div class="controls">
								<select name="clazz" class="form-control" required="true" placeholder="记录数据对象" data-cascade-name="property"
									data-cascade-url="${ctx}/admin/aud/revision-entity/properties">
									<c:forEach items="${clazzMapping}" var="item">
										<option value="${item.key}">${item.value}</option>
									</c:forEach>
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-2">
						<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSelect2" target="_blank">Javascript用法文档</a></span>
					</div>
				</div>
				<div class="row">
					<div class="col-md-10">
						<div class="form-group">
							<label class="control-label">下拉级联组件-目标元素</label>
							<div class="controls">
								<select name="property" class="form-control" placeholder="选取变更属性">
								</select>
							</div>
						</div>
					</div>
					<div class="col-md-2"></div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 日期、时间等处理组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-lg"
				action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">年月日 日期选取组件</label>
								<div class="controls">
									<form:input path="saleDate" class="form-control" data-picker="date" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDatePicker" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">年月日-时分 选取组件</label>
								<div class="controls">
									<form:input path="publishTime" class="form-control" data-picker="date-time" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDateTimePicker" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">时间段 选取组件</label>
								<div class="controls">
									<form:input path="searchDate" class="form-control" data-picker="date-range" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDateRangePicker" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> Text、HTML等处理组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
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
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTextareaMaxlength" target="_blank">Javascript用法文档</a></span>
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
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initTextareaHtmleditor" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 文件、图片等处理组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
				action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">单文件上传组件</label>
								<div class="controls">
									<form:input path="filePath" class="form-control" data-upload="single-file" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadSingleFile" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">单图片文件上传预览组件</label>
								<div class="controls">
									<form:hidden path="filePath" class="form-control" data-upload="single-image" data-image-width="100"
										data-image-height="100" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadSingleImage" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
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
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initUploadMultiImage" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 动态表格等处理组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
				action="${ctx}/docs/mock/dynamic-table" method="post" data-editrulesurl="false" modelAttribute="entity">
				<form:hidden path="id" />
				<form:hidden path="version" />
				<div class="form-actions">
					<button class="btn blue" type="submit" data-success-reload="false">
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
									<input type="text" class="form-control" name="textContent" required="true" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">数量</label>
								<div class="controls">
									<input type="text" class="form-control" name="quantity" required="true" />
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">选取多个Tag(Remote查询选取)</label>
								<div class="controls">
									<form:input path="splitText" class="form-control" data-select2-type="tags" data-url="${ctx}/docs/mock/tags" />
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
															data-url="${ctx}/docs/mock/tree-datas.json" required="true" /></td>
													<td><form:input path="mockItemEntites[${status.index}].saleDate" class="form-control"
															data-picker="date" required="true" /></td>
													<td><form:input path="mockItemEntites[${status.index}].textContent" class="form-control"
															required="true" /></td>
													<td><form:input path="mockItemEntites[${status.index}].imagePath" class="form-control"
															data-upload="single-file" /></td>
												</tr>
											</c:forEach>
											<tr class="dynamic-table-row-template">
												<input type="hidden" name="mockItemEntites[X].extraAttributes['operation']" value="create" />
												<input type="hidden" name="mockItemEntites[X].assignment.id" value="${entity.id}" />
												<td><input type="hidden" name="mockItemEntites[X].department.id" /> <input type="text"
													name="mockItemEntites[X].department.display" class="form-control" data-toggle="tree-select"
													data-url="${ctx}/docs/mock/tree-datas.json" required="true" /></td>
												<td><input type="text" name="mockItemEntites[X].saleDate" class="form-control" data-picker="date"
													required="true" /></td>
												<td><input type="text" name="mockItemEntites[X].textContent" class="form-control" required="true" /></td>
												<td><input type="text" name="mockItemEntites[X].imagePath" class="form-control"
													data-upload="single-file" /></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initDynamicTable" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
				</div>
				<div class="form-actions right">
					<button class="btn blue" type="submit" data-success-reload="false">
						<i class="fa fa-check"></i> 保存
					</button>
					<button class="btn default" type="button" data-dismiss="modal">取消</button>
				</div>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 递归树形和拖放操作Grid组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">

			<div class="row">
				<div class="col-md-10">
					<div class="note note-danger">
						<p>仅作UI效果展示，屏蔽实际提交操作。实际操作可直接访问“菜单管理”功能体验。</p>
						<p>父子项展开后，点击工具条的十字移动图标，开启鼠标拖放移动行项功能，一般用于类似菜单父子结构数据的管理操作。</p>
					</div>
					<div class="row search-form-default">
						<div class="col-md-12">
							<form method="get" class="form-inline form-validation form-search-init"
								data-grid-search="#grid-sys-menu-index-demo">
								<div class="form-group">
									<div class="controls controls-clearfix">
										<input type="text" name="search['CN_name']" class="form-control input-large" placeholder="名称...">
									</div>
								</div>
								<div class="form-group search-group-btn">
									<button class="btn green" type="submmit">
										<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
									</button>
									<button class="btn default" type="reset">
										<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
									</button>
								</div>
							</form>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table id="grid-sys-menu-index-demo"></table>
						</div>
					</div>
					<script type="text/javascript">
                        $(function() {
                            $("#grid-sys-menu-index-demo").data("gridOptions", {
                                url : WEB_ROOT + '/admin/sys/menu/list',
                                colModel : [ {
                                    label : '名称',
                                    name : 'name',
                                    width : 150,
                                    editable : true,
                                    editoptions : {
                                        spellto : 'code'
                                    },
                                    align : 'left'
                                }, {
                                    label : '图标',
                                    name : 'style',
                                    editable : true,
                                    width : 80,
                                    align : 'center',
                                    formatter : function(cellValue, options, rowdata, action) {
                                        if (cellValue) {
                                            return '<i class="fa ' + cellValue + '" icon="' + cellValue + '"></i>';
                                        } else {
                                            return ''
                                        }
                                    },
                                    unformat : function(cellValue, options, cell) {
                                        return $('i', cell).attr('icon');
                                    }
                                }, {
                                    label : '菜单URL',
                                    name : 'url',
                                    width : 200,
                                    align : 'left'
                                }, {
                                    label : '展开',
                                    name : 'initOpen',
                                    editable : true,
                                    formatter : "checkbox"
                                }, {
                                    label : '禁用',
                                    name : 'disabled',
                                    editable : true,
                                    formatter : "checkbox"
                                }, {
                                    label : '排序号',
                                    name : 'orderRank',
                                    width : 60,
                                    editable : true,
                                    editoptions : {
                                        defaultValue : 1000
                                    },
                                    sorttype : 'number'
                                }, {
                                    label : '备注说明',
                                    name : 'description',
                                    width : 200,
                                    hidden : true,
                                    editable : true,
                                    edittype : 'textarea'
                                } ],
                                multiselect : false,
                                subGrid : true,
                                gridDnD : true,
                                subGridRowExpanded : function(subgrid_id, row_id) {
                                    Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
                                },
                                editurl : false
                            });
                        });
                    </script>
				</div>
				<div class="col-md-2">
					<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initGrid" target="_blank">主表格JSDoc</a></span><span
						class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initSubGrid" target="_blank">子表格JSDoc</a></span><span
						class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initRecursiveSubGrid" target="_blank">父子表格JSDoc</a></span>
				</div>
			</div>


		</div>
	</div>


	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 百度地图
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-xl"
				action="${ctx}/todo" method="post" data-editrulesurl="false" modelAttribute="entity">
				<div class="form-body">
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">以地址信息初始化地图</label>
								<div class="controls">
									<div class="gmaps gmaps-baidu gmaps-demo-location" data-location="北京市朝阳区望京SOHO" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initGmapsBaidu" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">位置信息</label>
								<div class="controls">
									<input type="text" class="form-control" name="location" value="北京市朝阳区望京SOHO"> <span class="help-block">点击地图更新位置信息</span>
								</div>
							</div>
						</div>
						<div class="col-md-2"></div>
					</div>

					<div class="row">
						<div class="col-md-12">
							<hr>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">以经纬度坐标初始化地图</label>
								<div class="controls">
									<div class="gmaps gmaps-baidu gmaps-demo-point" data-point-longitude="116.487423"
										data-point-latitude="40.001965" />
								</div>
							</div>
						</div>
						<div class="col-md-2">
							<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#initGmapsBaidu" target="_blank">Javascript用法文档</a></span>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">经度坐标</label>
								<div class="controls">
									<input type="text" class="form-control" name="longitude" value="116.487423"> <span class="help-block">点击地图更新坐标信息</span>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-10">
							<div class="form-group">
								<label class="control-label">纬度坐标</label>
								<div class="controls">
									<input type="text" class="form-control" name="latitude" value="40.001965"> <span class="help-block">点击地图更新坐标信息</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<script type="text/javascript">
                    $(function() {
                        $(".gmaps-demo-location").on("clickMapPoint", function(event, geocoderResult) {
                            var $el = $(this);
                            $el.closest("form").find("input[name='location']").val(geocoderResult.fullAddress);
                        })

                        $(".gmaps-demo-point").on("clickMapPoint", function(event, geocoderResult) {
                            var $el = $(this);
                            $el.closest("form").find("input[name='longitude']").val(geocoderResult.point.lng);
                            $el.closest("form").find("input[name='latitude']").val(geocoderResult.point.lat);
                        })
                    });
                </script>
			</form:form>
		</div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> TODO
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body"></div>
	</div>

	<div class="portlet gren">
		<div class="portlet-title">
			<div class="caption">
				<i class="fa fa-reorder"></i> 无限滚动表格组件
			</div>
			<div class="tools">
				<a class="collapse" href="javascript:;"></a><a class="remove" href="javascript:;"></a>
			</div>
		</div>
		<div class="portlet-body">
			<div class="row">
				<div class="col-md-10">
					<div class="row search-form-default">
						<div class="col-md-12">
							<form method="get" class="form-inline form-validation form-search-init"
								data-div-search="#table-infinite-scroll-doc-demo > tbody" action="${ctx}/docs/mock/infinite-scroll?rows=3">
								<div class="form-group">
									<div class="controls controls-clearfix">
										<input type="text" name="search['CN_code']" class="form-control input-large" placeholder="名称...">
									</div>
								</div>
								<div class="form-group search-group-btn">
									<button class="btn green" type="submmit">
										<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
									</button>
									<button class="btn default" type="reset">
										<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
									</button>
								</div>
							</form>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<table class="table table-infinite-scroll" id="table-infinite-scroll-doc-demo">
								<thead>
									<tr>
										<th>权限代码</th>
										<th>生成时间</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="col-md-2">
					<span class="help-block"><a href="${ctx}/docs/jsdoc/global.html#bindTableInfiniteScroll" target="_blank">Javascript用法文档</a></span>
				</div>
			</div>


		</div>
	</div>
</div>