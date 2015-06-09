<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title><sitemesh:write property='title' /> : ${applicationScope.cfg.cfg_system_title}</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">

<%@include file="/WEB-INF/views/layouts/admin-include-header.jsp"%>
<script type="text/javascript">
    var DASHBOARD_URI = "/admin/dashboard";
</script>

<sitemesh:write property='head' />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed">
	<!-- BEGIN HEADER -->
	<div class="header navbar navbar-inverse navbar-fixed-top">
		<!-- BEGIN TOP NAVIGATION BAR -->
		<div class="header-inner">
			<!-- BEGIN LOGO -->
			<a class="navbar-brand" href="${ctx}/admin" style="width: 500px; padding-top: 10px; padding-left: 10px"><font
				color="white" size="+2">${applicationScope.cfg.cfg_system_title}</font></a>
			<!-- END LOGO -->
			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse"> <img
				src="${ctx}/assets/admin/img/menu-toggler.png" alt="" />
			</a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<ul class="nav navbar-nav pull-right">
				<!-- BEGIN NOTIFICATION DROPDOWN -->
				<li class="dropdown" id="header_notification_bar"><a href="javascript:;" class="dropdown-toggle"
					data-toggle="dropdown" data-hover="dropdown" data-close-others="true"
					rel="address:/admin/profile/notify-message|公告信息列表"> <i class="fa fa-warning"></i> <span class="badge"
						style="display: none"></span>
				</a>
					<ul class="dropdown-menu extended notification">
						<li class="message-info"></li>
					</ul></li>
				<!-- END NOTIFICATION DROPDOWN -->

				<!-- BEGIN INBOX DROPDOWN -->
				<li class="dropdown" id="header_inbox_bar"><a href="javascript:;" class="dropdown-toggle"
					data-toggle="dropdown" data-hover="dropdown" data-close-others="true"
					rel="address:/admin/profile/user-message|个人消息列表"> <i class="fa fa-envelope"></i> <span class="badge"
						style="display: none"></span>
				</a>
					<ul class="dropdown-menu extended inbox">
						<li class="message-info"></li>
					</ul></li>
				<!-- END INBOX DROPDOWN -->

				<!-- BEGIN USER LOGIN DROPDOWN -->
				<li class="dropdown user" style="padding-top: 5px; margin-right: 25px"><a href="javascripts:;"
					rel="address:/admin/profile/edit|个人配置" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown"
					data-close-others="true"><span class="username"><shiro:principal property="nickName" />(<shiro:principal
								property="authUid" />)</span> <i class="fa fa-angle-down"></i> </a>
					<ul class="dropdown-menu">
						<li><a href="${ctx}/w" target="_blank"><i class="fa fa-windows"></i> 前端Web站点</a></li>
						<li><a href="${ctx}/m" target="_blank"><i class="fa fa-html5"></i> HTML5移动站点</a></li>
						<li class="divider"></li>
						<li><a href="javascript:;" id="trigger_fullscreen"><i class="fa fa-move"></i> 全屏显示</a></li>
						<li><a href="${ctx}/admin/profile/password" data-toggle="modal-ajaxify" data-modal-size="600px" title="修改密码"><i
								class="fa fa-key"></i> 修改密码</a></li>
						<li class="divider"></li>
						<li><a id="a-logout" href="javascript:;"><i class="fa fa-sign-out"></i> 注销登录</a></li>
					</ul></li>
				<!-- END USER LOGIN DROPDOWN -->
			</ul>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END TOP NAVIGATION BAR -->

		<!-- BEGIN STYLE CUSTOMIZER -->
		<div class="theme-panel hidden-xs hidden-sm pull-right" style="margin-top: -3px; position: absolute; right: 0px">
			<div class="toggler"></div>
			<div class="toggler-close"></div>
			<div class="theme-options">
				<div class="theme-option theme-colors clearfix">
					<span>颜色样式</span>
					<ul>
						<li class="color-black current color-default" data-style="default"></li>
						<li class="color-blue" data-style="blue"></li>
						<li class="color-brown" data-style="brown"></li>
						<li class="color-purple" data-style="purple"></li>
						<li class="color-grey" data-style="grey"></li>
						<li class="color-white color-light" data-style="light"></li>
					</ul>
				</div>
				<div class="theme-option">
					<span>页面布局</span> <select class="layout-option form-control input-small">
						<option value="fluid" selected="selected">扩展</option>
						<option value="boxed">收缩</option>
					</select>
				</div>
				<div class="theme-option">
					<span>页面头部</span> <select class="header-option form-control input-small">
						<option value="fixed" selected="selected">固定</option>
						<option value="default">自动</option>
					</select>
				</div>
				<div class="theme-option">
					<span>侧边菜单</span> <select class="sidebar-option form-control input-small">
						<option value="fixed">浮动</option>
						<option value="default" selected="selected">自动</option>
					</select>
				</div>
				<div class="theme-option">
					<span>页面底部</span> <select class="footer-option form-control input-small">
						<option value="fixed">固定</option>
						<option value="default" selected="selected">自动</option>
					</select>
				</div>
				<div class="theme-option">
					<span>右键菜单</span> <select class="context-menu-option form-control input-small">
						<option value="enable" selected="selected">启用</option>
						<option value="disable">禁用</option>
					</select>
				</div>
				<div class="theme-option">
					<span>表格布局</span> <select class="grid-shrink-option form-control input-small">
						<option value="auto">自动</option>
						<option value="true" selected="selected">收缩</option>
					</select>
				</div>
			</div>
		</div>
		<!-- END BEGIN STYLE CUSTOMIZER -->
	</div>
	<!-- END HEADER -->
	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">
		<!-- BEGIN SIDEBAR -->
		<div class="page-sidebar navbar-collapse collapse">
			<!-- BEGIN SIDEBAR MENU -->
			<ul class="page-sidebar-menu">
				<li>
					<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
					<div class="sidebar-toggler hidden-phone" style="margin-top: 5px; margin-bottom: 5px"></div> <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
				</li>
				<li>
					<!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
					<div class="sidebar-search">
						<div class="form-container">
							<div class="input-box">
								<a href="javascript:;" class="remove"></a> <input type="text" name="search" placeholder="菜单项快速查询过滤..." value=""
									title="试试输入菜单名称拼音首字母" /> <input type="button" class="submit" value=" " />
							</div>
						</div>
					</div> <!-- END RESPONSIVE QUICK SEARCH FORM -->
				</li>
			</ul>
			<!-- END SIDEBAR MENU -->
		</div>
		<!-- END SIDEBAR -->

		<!-- BEGIN PAGE -->
		<div class="page-content">
			<div class="row" style="margin-left: 0px; margin-right: 0px">
				<div class="col-md-12" style="padding-left: 0px; padding-right: 0px">
					<ul class="page-breadcrumb breadcrumb" id="layout-nav" style="margin-top: 0px; margin-bottom: 5px;">
						<li class="btn-group" style="right: 0px;">
							<button data-close-others="true" data-delay="1000" data-toggle="dropdown" class="btn default dropdown-toggle"
								type="button">
								<span><i class="fa fa-reorder"></i> 访问列表</span> <i class="fa fa-angle-down"></i>
							</button>
							<ul role="menu" class="dropdown-menu">
							</ul>
							<button class="btn default btn-close-active" type="button">
								<i class="fa fa-times"></i>
							</button>
						</li>
						<li><a class="btn-dashboard" href="#dashboard"><i class="fa fa-home"></i> 首页 </a></li>
					</ul>
					<div class="tab-content">
						<div id="tab_content_dashboard"></div>
					</div>
				</div>
			</div>
		</div>
		<!-- BEGIN PAGE -->
	</div>
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<div class="footer">
		<div class="footer-inner">&copy; S2JH4Net 2014</div>
		<div class="footer-tools">
			<span class="go-top"> <i class="fa fa-angle-up"></i>
			</span>
		</div>
	</div>
	<!-- END FOOTER -->


	<div class="hide">
		<form class="form-horizontal" action="${ctx}/w/file/upload/single" id="singleFileUploadForm"
			enctype="multipart/form-data" method="post">
			<input type="file" name="fileUpload" />
			<button type="submit" class="btn">提交</button>
		</form>
	</div>

	<!-- BEGIN FileUpload FORM -->
	<div class="modal fade" id="fileupload-dialog" tabindex="-1" role="basic" aria-hidden="true">
		<div class="modal-dialog modal-wide">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
					<h4 class="modal-title">文件上传</h4>
				</div>
				<div class="modal-body">
					<form id="fileupload" enctype="multipart/form-data" method="POST">
						<input type="hidden" name="attachmentName" value="attachments" />
						<div class="row fileupload-buttonbar">
							<div class="col-lg-7">
								<!-- The fileinput-button span is used to style the file input field as button -->
								<span class="btn green fileinput-button"> <i class="fa fa-plus"></i> <span>添加文件...</span> <input
									type="file" multiple="" name="files">
								</span>
								<button class="btn blue start" type="submit">
									<i class="fa fa-upload"></i> <span>开始上传</span>
								</button>
								<button class="btn yellow cancel" type="reset">
									<i class="fa fa-ban"></i> <span>取消上传</span>
								</button>
								<!-- The loading indicator is shown during file processing -->
								<span class="fileupload-loading"></span>
							</div>
							<!-- The global progress information -->
							<div class="col-lg-5 fileupload-progress fade">
								<!-- The global progress bar -->
								<div aria-valuemax="100" aria-valuemin="0" role="progressbar" class="progress progress-striped active">
									<div style="width: 0%;" class="progress-bar progress-bar-success"></div>
								</div>
								<!-- The extended global progress information -->
								<div class="progress-extended">&nbsp;</div>
							</div>
						</div>
						<table class="table table-striped clearfix" role="presentation">
							<tbody class="files"></tbody>
						</table>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn default" data-dismiss="modal">取消</button>
					<button type="submit" class="btn blue btn-add">添加</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- END FileUpload FORM -->

	<button type="button" class="btn " id="btn-profile-param" title="点击收藏记忆当前表单元素数据" style="display: none">
		<i class="fa fa-heart-o"></i>
	</button>

	<%@include file="/WEB-INF/views/layouts/admin-include-footer.jsp"%>
</body>
<!-- END BODY -->
</html>