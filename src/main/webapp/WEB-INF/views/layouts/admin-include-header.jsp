<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<script src="${ctx}/assets/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- END GLOBAL MANDATORY STYLES -->

<!-- BEGIN THEME STYLES -->
<link href="${ctx}/assets/admin/css/style-metronic.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/style-responsive.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/plugins.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/pages/tasks.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/admin/css/themes/light.css" rel="stylesheet" type="text/css" id="style_color" />
<link href="${ctx}/assets/admin/css/pages/search.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/plugins/data-tables/DT_bootstrap.css" rel="stylesheet" type="text/css" />
<!-- END THEME STYLES -->

<link rel="shortcut icon" href="${ctx}/assets/img/favicon.ico" />
<script type="text/javascript">
    var WEB_ROOT = "${ctx}";
    var READ_FILE_URL_PREFIX = "${readFileUrlPrefix}";
</script>