<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title><sitemesh:write property='title' /> : ${applicationScope.cfg.cfg_system_title}</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/jquery-ui/redmond/jquery-ui-1.10.3.custom.min.css" rel="stylesheet" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/data-tables/DT_bootstrap.css" />
<link href="${ctx}/assets/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet" />
<link rel="stylesheet" href="${ctx}/assets/plugins/revolution_slider/css/rs-style.css" media="screen">
<link rel="stylesheet" href="${ctx}/assets/plugins/revolution_slider/rs-plugin/css/settings.css" media="screen">
<link href="${ctx}/assets/plugins/bxslider/jquery.bxslider.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.css" />
<link href="${ctx}/assets/plugins/bootstrap-datepicker/css/datepicker.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/select2/select2_metro.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/data-tables/DT_bootstrap.css" />
<link href="${ctx}/assets/plugins/bootstrap-daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
<!-- END PAGE LEVEL PLUGIN STYLES -->

<!-- BEGIN THEME STYLES -->
<link href="${ctx}/assets/w/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/w/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/w/css/themes/blue.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/assets/w/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/w/app/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="<sitemesh:write property='body.class' />" style="<sitemesh:write property='body.style' />">

	<!-- BEGIN STYLE CUSTOMIZER -->
	<div class="color-panel hidden-sm">
		<div class="color-mode-icons icon-color"></div>
		<div class="color-mode-icons icon-color-close"></div>
		<div class="color-mode">
			<p>THEME COLOR</p>
			<ul class="inline">
				<li class="color-blue current color-default" data-style="blue"></li>
				<li class="color-red" data-style="red"></li>
				<li class="color-green" data-style="green"></li>
				<li class="color-orange" data-style="orange"></li>
			</ul>
			<label> <span>Header</span> <select class="header-option form-control input-small">
					<option value="default" selected>Default</option>
					<option value="fixed">Fixed</option>
			</select>
			</label>
		</div>
	</div>
	<!-- END BEGIN STYLE CUSTOMIZER -->


	<!-- BEGIN HEADER -->
	<div class="header navbar navbar-default navbar-static-top">
		<!-- BEGIN TOP BAR -->
		<div class="front-topbar">
			<div class="container">
				<div class="row">
					<div class="col-md-6 col-sm-6">
						<ul class="list-unstyle inline">
							<li>QQ: <span><a
									href="http://wpa.qq.com/msgrd?v=3&uin=2414521719&site=http://my.oschina.net/s2jh&menu=yes">2414521719</a></span></li>
							<li class="sep"><span>|</span></li>
							<li><i class="fa fa-envelope-o topbar-info-icon top-2"></i>Email: <span><a
									href="mailto:s2jh-dev@hotmail.com">s2jh-dev@hotmail.com</a></span></li>
							<li class="sep"><span>|</span></li>
							<li><span><a href="${ctx}/admin" target="_blank">管理后台系统</a></span></li>
							<li class="sep"><span>|</span></li>
							<li><span><a href="${ctx}/m" target="_blank">HTML5移动站点</a></span></li>
						</ul>
					</div>
					<div class="col-md-6 col-sm-6 login-reg-links">
						<ul class="list-unstyled inline">
							<shiro:guest>
								<li><a href="${ctx}/w/login">登录</a></li>
								<li class="sep"><span>|</span></li>
								<li><a href="${ctx}/w/signup">注册</a></li>
							</shiro:guest>
							<shiro:user>
								<li><a href="${ctx}/w/user"><shiro:principal property="nickName" /> ( <shiro:principal
											property="authUid" /> )</a></li>
								<li class="sep"><span>|</span></li>
								<li><a href="javascript:;" rel="address:/w/message/list">我的消息<span id="message-count-alert"></span></a></li>
								<li class="sep"><span>|</span></li>
								<li><a href="${ctx}/w/logout" onclick="return confirm('确认注销登录吗?')">注销登录</a></li>
							</shiro:user>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<!-- END TOP BAR -->
		<div class="container">
			<div class="navbar-header">
				<!-- BEGIN RESPONSIVE MENU TOGGLER -->
				<button class="navbar-toggle btn navbar-btn" data-toggle="collapse" data-target=".navbar-collapse">
					<span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<!-- END RESPONSIVE MENU TOGGLER -->
				<!-- BEGIN LOGO (you can use logo image instead of text)-->
				<a class="navbar-brand logo-v1" href="${ctx}/w" style="padding-top: 0px; padding-bottom: 0px"> <img
					src="${ctx}/assets/img/logo.png" id="logoimg" alt="" style="height: 60px">
				</a>
				<!-- END LOGO -->
			</div>

			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<li><a href="${ctx}/w" data-nav-id="index">首页</a></li>
					<li class="menu-search"><span class="sep"></span> <i class="fa fa-search search-btn"></i>

						<div class="search-box">
							<form action="#">
								<div class="input-group input-large">
									<input class="form-control" type="text" placeholder="Search"> <span class="input-group-btn">
										<button type="submit" class="btn theme-btn">Go</button>
									</span>
								</div>
							</form>
						</div></li>
				</ul>
			</div>
			<!-- BEGIN TOP NAVIGATION MENU -->
		</div>
	</div>
	<!-- END HEADER -->

	<!-- BEGIN PAGE CONTAINER -->
	<div class="page-container">
		<sitemesh:write property='body' />
	</div>
	<!-- END PAGE CONTAINER -->

	<!-- BEGIN FOOTER -->
	<div class="copyright">
		<div class="container">
			<div class="row">
				<div class="col-md-8">
					<p>
						<span class="margin-right-10">2015 © S2JH. ALL Rights Reserved.</span> <a
							href="http://git.oschina.net/xautlx/s2jh" target="_blank">关于S2JH</a> | <a
							href="http://git.oschina.net/xautlx/s2jh4net" target="_blank">关于S2JH4Net</a> | <a
							href="mailto:s2jh-dev@hotmail.com">联系作者</a>
					</p>
				</div>
				<div class="col-md-4">
					<p>
						<span class="margin-right-10">构建版本：</span>
					</p>
				</div>
			</div>
		</div>
	</div>
	<!-- END FOOTER -->
	<!-- Load javascripts at bottom, this will reduce page load time -->
	<script type="text/javascript">
        var WEB_ROOT = "${ctx}";
    </script>
	<!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) -->
	<!--[if lt IE 9]>
    <script src="${ctx}/assets/plugins/respond.min.js"></script>  
    <![endif]-->

	<script src="${ctx}/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<!-- IMPORTANT! Load jquery-ui-1.10.3.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
	<script src="${ctx}/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/hover-dropdown.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/back-to-top.js"></script>
	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) -->
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery.form.js"></script>
	<script src="${ctx}/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/localization/messages_zh.js"></script>
	<script src="${ctx}/assets/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.js"></script>
	<script src="${ctx}/assets/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/fancybox/source/jquery.fancybox.pack.js"></script>
	<script type="text/javascript"
		src="${ctx}/assets/plugins/revolution_slider/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
	<script type="text/javascript"
		src="${ctx}/assets/plugins/revolution_slider/rs-plugin/js/jquery.themepunch.revolution.min.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/bxslider/jquery.bxslider.min.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-knob/js/jquery.knob.js"></script>
	<script src="${ctx}/assets/plugins/bootstrap-daterangepicker/moment.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js"
		type="text/javascript"></script>

	<script type="text/javascript" src="${ctx}/assets/plugins/select2/select2.min.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/data-tables/jquery.dataTables.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/data-tables/DT_bootstrap.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-mousewheel/jquery.mousewheel.min.js"></script>
	<script src="${ctx}/assets/plugins/jquery.address/jquery.address-1.5.min.js"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery.cookie.min.js"></script>

	<script src="${ctx}/assets/plugins/JSPinyin.js"></script>

	<script src="${ctx}/assets/w/scripts/app.js"></script>

	<script src="${ctx}/assets/admin/app/util.js" type="text/javascript"></script>
	<script src="${ctx}/assets/w/app/global.js"></script>
	<script src="${ctx}/assets/w/app/form-validation.js" type="text/javascript"></script>
	<script src="${ctx}/assets/w/app/page.js"></script>
	<script src="${ctx}/assets/w/app/biz.js"></script>

	<script type="text/javascript">
        jQuery(document).ready(function() {

            App.init();
            App.initBxSlider();

            Util.init();
            Global.init();
            FormValidation.init();
            FormValidation.initAjax();

            Page.initAjaxBeforeShow();
            Page.initAjaxAfterShow();

            Biz.init();

        });
    </script>
	<!-- END PAGE LEVEL JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>