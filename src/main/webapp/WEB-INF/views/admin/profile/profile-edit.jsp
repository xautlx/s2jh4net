<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>个人配置</title>
</head>
<body>
	<div class="row">
		<%-- 
		<div class="col-md-6">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 界面布局默认设置
					</div>
					<div class="tools">
						<a class="expand" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="note note-info">
						<p>以下设置参数将用于管理界面右上角界面布局参数的初始化设置，具体参数设置效果可以通过右上角操作体验。</p>
					</div>
					<form:form class="form-horizontal form-bordered form-label-stripped form-validation" method="post"
						action="${ctx}/admin/profile/layout" modelAttribute="user" data-editrulesurl="false">
						<div class="form-body">
							<div class="form-group">
								<label class="control-label">页面布局</label>
								<div class="controls">
									<form:select path="extraAttributes['_layout_page']" class="form-control">
										<form:option value="fluid">扩展</form:option>
										<form:option value="boxed">收缩</form:option>
									</form:select>
									<span class="help-block">扩展：页面宽度自动扩展显示；收缩：按照一个特定宽度显示</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">页面头部</label>
								<div class="controls">
									<form:select path="extraAttributes['_layout_header']" class="form-control">
										<form:option value="fixed">固定</form:option>
										<form:option value="default">自动</form:option>
									</form:select>
									<span class="help-block">固定：滚动时始终显示头部；自动：根据滚动区域自动显示头部</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">页面底部</label>
								<div class="controls">
									<form:select path="extraAttributes['_layout_footer']" class="form-control">
										<form:option value="fixed">固定</form:option>
										<form:option value="default">自动</form:option>
									</form:select>
									<span class="help-block">固定：滚动时始终显示底部；自动：根据滚动区域自动显示底部</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">右键菜单</label>
								<div class="controls">
									<form:select path="extraAttributes['_layout_context_menu']" class="form-control">
										<form:option value="enable">启用</form:option>
										<form:option value="disable">禁用</form:option>
									</form:select>
									<span class="help-block">启用：特定区域显示业务定制的鼠标右键菜单；<br />禁用：全局关闭如表格组件的右键功能，显示浏览器标准右键菜单
									</span>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">表格布局</label>
								<div class="controls">
									<form:select path="extraAttributes['_layout_grid_shrink']" class="form-control">
										<form:option value="auto">自动</form:option>
										<form:option value="true">收缩</form:option>
									</form:select>
									<span class="help-block">自动：根据表格组件列数和宽度动态控制显示模式，为了避免数据显示太拥挤可能会自动出现横向滚动条；<br />收缩：始终以收缩方式显示表格组件，不会出现横向滚动条，但是可能数据显示会比较拥挤
									</span>
								</div>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit" data-post-dismiss="modal">
								<i class="fa fa-check"></i> 保存
							</button>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		--%>
		<div class="col-md-6">
			<div class="portlet box blue">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 信息记录
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation" method="post" action="false"
						data-editrulesurl="false">
						<div class="form-body">
							<div class="form-group">
								<label class="control-label">登录账号</label>
								<div class="controls">
									<p class="form-control-static">${user.authUid}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">移动电话</label>
								<div class="controls">
									<p class="form-control-static">${user.mobile}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">电子邮件</label>
								<div class="controls">
									<p class="form-control-static">${user.email}</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">注册时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${user.userExt.signupTime}" type="both" />
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">密码到期日期</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${user.credentialsExpireTime}" type="date" />
										<span class="help-block">出于安全考虑系统要求用户在设定的固定周期后更新密码，密码到期后登录系统会提示用户设置新密码</span>
									</p>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">最近登录时间</label>
								<div class="controls">
									<p class="form-control-static">
										<fmt:formatDate value="${user.userExt.lastLogonTime}" type="both" />
									</p>
								</div>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>