<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>Mobile</title>
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/plugins/jquery.mobile/jquery.mobile-1.4.5.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/jquery-mobile-iscrollview/lib/jquery.mobile.iscrollview.css" rel="stylesheet"
	type="text/css" />
<link href="${ctx}/assets/plugins/jquery-mobile-iscrollview/lib/jquery.mobile.iscrollview-pull.css" rel="stylesheet"
	type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/plugins/jquery.mobile/jquery.mobile-1.4.5.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/dist/jquery.validate.js"></script>
<script type="text/javascript" src="${ctx}/assets/plugins/jquery-validation/localization/messages_zh.js"></script>

<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>
	<sitemesh:write property='body' />

	<script type="text/javascript">
        $(document).on("iscroll_init", function() {
            $.extend($.mobile.iscrollview.prototype.options, {
                pullDownResetText : "向下拉动刷新内容...",
                pullDownPulledText : "释放刷新...",
                pullDownLoadingText : "正在加载...",
                pullUpResetText : "向上拉动刷新内容...",
                pullUpPulledText : "释放刷新...",
                pullUpLoadingText : "正在加载...",
            });
        });
    </script>

	<script src="${ctx}/assets/plugins/jquery-mobile-iscrollview/lib/iscroll.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery-mobile-iscrollview/lib/jquery.mobile.iscrollview.js" type="text/javascript"></script>
	<script src="${ctx}/assets/plugins/jquery.mobile.toast/jquery.mobile.toast.min.js" type="text/javascript"></script>
</body>
<!-- END BODY -->
</html>