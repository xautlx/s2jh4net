<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

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

<script src="${ctx}/assets/global/plugins/icheck/icheck.min.js" type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/jquery-validation/js/jquery.validate.min.js" type="text/javascript"></script>
<script type="text/javascript"
        src="${ctx}/assets/global/plugins/jquery-validation/js/localization/messages_zh.js"></script>
<script src="${ctx}/assets/global/plugins/backstretch/jquery.backstretch.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/jquery-ui/jquery-ui.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/assets/global/plugins/jquery.pulsate.min.js"></script>

<!-- The basic File Upload plugin -->
<script src="${ctx}/assets/global/plugins/jquery-file-upload/js/jquery.fileupload.js"></script>

<script src="${ctx}/assets/global/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-maxlength/bootstrap-maxlength.min.js"
        type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/moment.min.js"></script>
<script src="${ctx}/assets/apps/plugins/jquery.form.js"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-modal/js/bootstrap-modal.js" type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-modal/js/bootstrap-modalmanager.js" type="text/javascript"></script>

<link href="${ctx}/assets/global/plugins/bootstrap-sweetalert/sweetalert.css" rel="stylesheet" type="text/css" />
<script src="${ctx}/assets/global/plugins/bootstrap-sweetalert/sweetalert.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->

<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${ctx}/assets/apps/scripts/app.js?_=${buildVersion}" type="text/javascript"></script>
<script type="text/javascript">
    App.setAssetsPath("${ctx}/assets/")
</script>

<script src="${ctx}/assets/apps/scripts/util.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/global.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/function/misc.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/component/select.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/date-picker.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/date-time-picker.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/date-range-picker.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/slimscroll-panel.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/qrcode.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/table-ajax-sorting.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/form/validator-additional-method.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/form/form-validation.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/form/form-misc.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/component/table-infinite-scroll.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/ajax-bootstrap-tabs.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/ajax-bootstrap-modal.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/checkbox-radio-group.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/function/image-captcha-code.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/function/popup-captcha-code.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/function/mobile-sms-code.js?_=${buildVersion}"></script>
<!-- END PAGE LEVEL PLUGINS -->


