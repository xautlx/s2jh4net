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
<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link href="${ctx}/assets/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap-datepicker/css/datepicker.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap-datetimepicker/css/datetimepicker.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-multi-select/css/multi-select.css" />
<link href="${ctx}/assets/plugins/fullcalendar/fullcalendar.css" rel="stylesheet" />
<link href="${ctx}/assets/plugins/jqvmap/jqvmap/jqvmap.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.css" rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGIN STYLES -->

<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/data-tables/DT_bootstrap.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/assets/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css" />
<link rel="stylesheet" type="text/css"
	href="${ctx}/assets/plugins/bootstrap-switch/static/stylesheets/bootstrap-switch-metro.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" />

<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-ui/redmond/jquery-ui-1.10.3.custom.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-jqgrid/plugins/ui.multiselect.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-jqgrid/css/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/admin/app/bootstrap-jqgrid.css" />

<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/tooltipster/css/tooltipster.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/tooltipster/css/themes/tooltipster-light.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/tooltipster/css/themes/tooltipster-noir.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/tooltipster/css/themes/tooltipster-punk.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/tooltipster/css/themes/tooltipster-shadow.css" />

<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/select2/select2_metro.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-ztree/css/zTreeStyle/zTreeStyle.css">
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/kindeditor/themes/default/default.css">

<!-- BEGIN THEME STYLES -->
<link href="${ctx}/assets/admin/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/pages/tasks.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/assets/admin/css/pages/search.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/app/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<link href="${ctx}/assets/admin/app/custom.css" rel="stylesheet" type="text/css" />

<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
<script type="text/javascript">
    var WEB_ROOT = "${ctx}";
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
					rel='address:/admin/profile/notify-message|消息列表'"> <i class="fa fa-warning"></i> <span class="badge"
						style="display: none"></span>
				</a>
					<ul class="dropdown-menu extended notification">
						<li id="messageInfo"></li>
					</ul></li>
				<!-- END NOTIFICATION DROPDOWN -->
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
		<c:if test="${cfg.dev_mode}">
			<div class="footer-tools" style="margin-left: 10px; margin-right: 10px">
				<span class="dev-debug" onclick="$('#debug-info').toggle()">DEBUG</span>
			</div>
		</c:if>
	</div>
	<c:if test="${cfg.dev_mode}">
		<div id="debug-info" style="display: none; padding: 15px">
			Debug Info:
			<shiro:user>登录类型/账号: <shiro:principal property="authType" />/<shiro:principal property="authUid" />, 昵称: <shiro:principal
					property="nickName" />
			</shiro:user>
		</div>
	</c:if>
	<!-- END FOOTER -->
	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
    <script src="${ctx}/assets/plugins/respond.min.js"></script>
    <script src="${ctx}/assets/plugins/excanvas.min.js"></script> 
    <![endif]-->

	<script src="${ctx}/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="${ctx}/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js"
		type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.js"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->
	<script src="${ctx}/assets/plugins/jquery.pulsate.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-daterangepicker/moment.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"
		type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"
		type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
	<!-- IMPORTANT! fullcalendar depends on jquery-ui-1.10.3.custom.min.js for drag & drop support -->
	<script src="${ctx}/assets/plugins/fullcalendar/lib/moment.min.js"></script>
	<script src="${ctx}/assets/plugins/fullcalendar/fullcalendar.min.js"></script>
	<script src="${ctx}/assets/plugins/fullcalendar/lang/zh-cn.js"></script>
	<script src="${ctx}/assets/plugins/jquery.sparkline.min.js" type="text/javascript"></script>
	<!-- END PAGE LEVEL PLUGINS -->

	<script type="text/javascript" src="${ctx}/assets/plugins/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/localization/messages_zh.js"></script>
	<script src="${ctx}/assets/plugins/bootstrap-switch/static/js/bootstrap-switch.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js" type="text/javascript"></script>
	<script type="text/javascript"
		src="${ctx}/assets/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.min.js"></script>

	<!-- The basic File Upload plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>
	<!-- The File Upload processing plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-process.js"></script>
	<!-- The File Upload image preview & resize plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-image.js"></script>
	<!-- The File Upload audio preview plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-audio.js"></script>
	<!-- The File Upload video preview plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-video.js"></script>
	<!-- The File Upload validation plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-validate.js"></script>
	<!-- The File Upload user interface plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload-ui.js"></script>
	<!-- The main application script -->
	<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
	<!--[if (gte IE 8)&(lt IE 10)]>
    <script src="${ctx}/assets/plugins/jquery-file-upload/js/cors/jquery.xdr-transport.js"></script>
    <![endif]-->
	<!-- END:File Upload Plugin JS files-->

	<script type="text/javascript" src="${ctx}/assets/plugins/tooltipster/js/jquery.tooltipster.min.js"></script>

	<script src="${ctx}/assets/plugins/JSPinyin.js"></script>

	<script src="${ctx}/assets/plugins/flot/jquery.flot.js"></script>
	<script src="${ctx}/assets/plugins/flot/jquery.flot.pie.js"></script>
	<script src="${ctx}/assets/plugins/flot/jquery.flot.stack.js"></script>
	<script src="${ctx}/assets/plugins/flot/jquery.flot.crosshair.js"></script>
	<script src="${ctx}/assets/plugins/flot/jquery.flot.time.js"></script>

	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script src="${ctx}/assets/admin/scripts/app.js" type="text/javascript"></script>
	<script src="${ctx}/assets/admin/scripts/index.js" type="text/javascript"></script>
	<script src="${ctx}/assets/admin/scripts/tasks.js" type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<script src="${ctx}/assets/plugins/jquery-jqgrid/plugins/ui.multiselect.js"></script>
	<script src="${ctx}/assets/plugins/jquery-jqgrid/js/i18n/grid.locale-cn.js"></script>
	<script src="${ctx}/assets/plugins/jquery-jqgrid/js/jquery.jqGrid.src.js"></script>

	<script type="text/javascript" src="${ctx}/assets/plugins/select2/select2.min.js"></script>
	<script src="${ctx}/assets/plugins/jquery-ztree/js/jquery.ztree.all-3.5.js"></script>
	<script src="${ctx}/assets/plugins/kindeditor/kindeditor-ext.js"></script>

	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-multi-select/js/jquery.multi-select.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-multi-select/js/jquery.quicksearch.js"></script>

	<script src="${ctx}/assets/plugins/jquery.address/jquery.address-1.5.min.js"></script>

	<script src="${ctx}/assets/admin/app/util.js"></script>
	<script src="${ctx}/assets/w/app/global.js"></script>
	<script src="${ctx}/assets/admin/app/global.js"></script>
	<script src="${ctx}/assets/admin/app/grid.js"></script>
	<script src="${ctx}/assets/w/app/form-validation.js"></script>
	<script src="${ctx}/assets/w/app/page.js"></script>
	<script src="${ctx}/assets/admin/app/page.js"></script>

	<script>
        $(function() {

            // console.profile('Profile Sttart');

            App.init();
            Util.init();
            Global.init();
            AdminGlobal.init();
            FormValidation.init();

            App.unblockUI($("body"));

            //console.profileEnd();
        });
    </script>
	<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>