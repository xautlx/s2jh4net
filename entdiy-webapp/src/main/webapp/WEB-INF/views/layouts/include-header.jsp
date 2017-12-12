<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
<meta content="" name="description"/>
<meta content="" name="author"/>
<meta name="MobileOptimized" content="320">

<!-- BEGIN GLOBAL MANDATORY STYLES -->
<link href="${ctx}/assets/apps/fonts/font.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/assets/global/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/assets/global/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/assets/global/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
<!-- END GLOBAL MANDATORY STYLES -->

<script src="${ctx}/assets/global/plugins/jquery.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery-migrate.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/apps/scripts/config.js?_=${buildVersion}" type="text/javascript"></script>
<script type="text/javascript">
    var WEB_ROOT = "${ctx}";
    Config.setContextPath("${ctx}");
    Config.setFileUploadUrl("${fileUploadUrl}");
</script>

<!-- BEGIN PAGE LEVEL STYLES -->
<link href="${ctx}/assets/global/plugins/icheck/skins/all.css" rel="stylesheet" type="text/css"/>

<link href="${ctx}/assets/global/plugins/datatables/datatables.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/assets/global/plugins/datatables/plugins/bootstrap/datatables.bootstrap.css" rel="stylesheet"
      type="text/css"/>

<script type="text/javascript" src="${ctx}/assets/global/plugins/select2/js/select2.full.min.js"></script>
<script type="text/javascript" src="${ctx}/assets/global/plugins/select2/js/i18n/zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/select2/css/select2.min.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/select2/css/select2-bootstrap.min.css"/>
<script type="text/javascript" src="${ctx}/assets/apps/plugins/select2-to-tree/select2totree.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/select2-to-tree/select2totree.css"/>

<link href="${ctx}/assets/global/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet"/>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/jquery-ui/jquery-ui.min.css">
<link href="${ctx}/assets/global/plugins/jquery-file-upload/css/jquery.fileupload-ui.css" rel="stylesheet"/>

<link href="${ctx}/assets/global/plugins/bootstrap-modal/css/bootstrap-modal-bs3patch.css" rel="stylesheet" type="text/css" />
<link href="${ctx}/assets/global/plugins/bootstrap-modal/css/bootstrap-modal.css" rel="stylesheet" type="text/css" />

<!-- END PAGE LEVEL SCRIPTS -->

<!-- BEGIN THEME GLOBAL STYLES -->
<link href="${ctx}/assets/global/css/components.min.css" rel="stylesheet" id="style_components" type="text/css"/>
<link href="${ctx}/assets/global/css/plugins.min.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/assets/apps/css/global.css?_=${buildVersion}" rel="stylesheet" type="text/css"/>
<!-- END THEME GLOBAL STYLES -->