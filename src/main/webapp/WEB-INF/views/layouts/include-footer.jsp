<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!--[if lt IE 9]>
<script src="${ctx}/assets/global/plugins/respond.min.js"></script>
<script src="${ctx}/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<!--[if lt IE 9]>
<script src="${ctx}/assets/global/plugins/respond.min.js"></script>
<script src="${ctx}/assets/global/plugins/excanvas.min.js"></script>
<![endif]-->
<script src="${ctx}/assets/global/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery.blockui.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/js.cookie.min.js" type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/assets/global/plugins/jquery-validation/js/localization/messages_zh.js"></script>
<script src="${ctx}/assets/global/plugins/bootbox/bootbox.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/assets/global/plugins/jquery.pulsate.min.js"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-toastr/toastr.min.js"></script>
<!-- The basic File Upload plugin -->
<script src="${ctx}/assets/global/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/moment.min.js"></script>
<script src="${ctx}/assets/apps/plugins/jquery.form.js"></script>


<!-- END CORE PLUGINS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${ctx}/assets/global/scripts/app.js?_=${build_version}" type="text/javascript"></script>
<script type="text/javascript">
    var WEB_ROOT = "${ctx}";
    App.setAssetsPath("${ctx}/assets/")
</script>
<script src="${ctx}/assets/apps/scripts/util.js?_=${build_version}"></script>
<script src="${ctx}/assets/apps/scripts/global.js?_=${build_version}"></script>
<script type="text/javascript">
    Global.setContextPath("${ctx}")
</script>
<!-- END PAGE LEVEL PLUGINS -->
