<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->
<!-- BEGIN PAGE LEVEL STYLES -->
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/select2/select2_metro.css" />
<link href="${ctx}/assets/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/jquery-ui/redmond/jquery-ui-1.10.3.custom.min.css">
<link href="${ctx}/assets/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.css" />
<!-- END PAGE LEVEL SCRIPTS -->
<!-- BEGIN THEME STYLES -->

<link href="${ctx}/assets/fonts/font.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/assets/admin/css/pages/login.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/app/custom.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>

<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
<sitemesh:write property='head' />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
	<!-- BEGIN LOGIN -->
	<div class="clearfix" style="padding: 50px">
		<div class="content" style="width: 100%; max-width: 800px">
			<div class="row">
				<div class="col-md-12">
					<div class="row">
						<div class="col-md-4">
							<img src="${ctx}/assets/img/logo.png" />
						</div>
						<div class="col-md-8">
							<h2 style="color: #555555">${applicationScope.cfg.cfg_system_title}</h2>
						</div>
					</div>
				</div>
			</div>
			<hr />
			<div class="row">
				<div class="col-md-12" style="text-align: right;">
					<a href="${ctx}/w" target="_blank">前端Web站点</a> <a href="${ctx}/m" target="_blank">HTML5移动站点</a>
				</div>
			</div>
			<sitemesh:write property='body' />

			<!-- BEGIN COPYRIGHT -->
			<div class="row">
				<div class="col-md-12">
					<div class="copyright pull-right">
						<span title="${buildVersion}|<%= request.getLocalAddr()  %>:<%=request.getLocalPort()%>]"
							style="display: inline-block; width: 200px">2014 &copy; <%=request.getServerName()%></span>
					</div>
				</div>
			</div>
			<!-- END COPYRIGHT -->
		</div>
	</div>
	<!-- END LOGIN -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
	<script type="text/javascript">
        var WEB_ROOT = "${ctx}";
    </script>
	<!-- BEGIN CORE PLUGINS -->
	<!--[if lt IE 9]>
	<script src="${ctx}/assets/plugins/respond.min.js"></script>
	<script src="${ctx}/assets/plugins/excanvas.min.js"></script> 
	<![endif]-->
	<script src="${ctx}/assets/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.blockui.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.cookie.min.js" type="text/javascript"></script>
	<!-- END CORE PLUGINS -->
	<!-- BEGIN PAGE LEVEL PLUGINS -->

	<script src="${ctx}/assets/admin/scripts/app.js" type="text/javascript"></script>

	<script src="${ctx}/assets/plugins/jquery-validation/dist/jquery.validate.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/localization/messages_zh.js"></script>
	<script src="${ctx}/assets/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery-ui/jquery-ui-1.10.3.custom.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/assets/plugins/jquery.pulsate.min.js"></script>
	<script src="${ctx}/assets/plugins/bootstrap-toastr/toastr.min.js"></script>
	<!-- The basic File Upload plugin -->
	<script src="${ctx}/assets/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>

	<script src="${ctx}/assets/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.form.js"></script>
	<!-- END PAGE LEVEL PLUGINS -->

	<script src="${ctx}/assets/admin/app/util.js"></script>
	<script src="${ctx}/assets/w/app/global.js"></script>
	<script src="${ctx}/assets/admin/app/global.js"></script>
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

	<script src="${ctx}/assets/admin/app/login.js"></script>
	<!-- END PAGE LEVEL SCRIPTS -->
</body>
<!-- END BODY -->
</html>