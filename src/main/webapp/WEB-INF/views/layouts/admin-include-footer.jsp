<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
<script src="${ctx}/assets/plugins/bootstrap-datepicker/js/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
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
<script src="${ctx}/assets/plugins/uploadify/jquery.uploadify.min.js"></script>

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

<script src="${ctx}/assets/plugins/echarts/dist/echarts-all.js"></script>

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
<script src="${ctx}/assets/plugins/jcrop/js/jquery.Jcrop.min.js"></script>

<script src="${ctx}/assets/admin/app/util.js"></script>
<script src="${ctx}/assets/w/app/global.js"></script>
<script src="${ctx}/assets/admin/app/global.js"></script>
<script src="${ctx}/assets/admin/app/grid.js"></script>
<script src="${ctx}/assets/admin/app/dynamic-table.js"></script>
<script src="${ctx}/assets/w/app/form-validation.js"></script>
<script src="${ctx}/assets/w/app/page.js"></script>
<script src="${ctx}/assets/admin/app/page.js"></script>
<script src="${ctx}/assets/w/app/biz.js"></script>

<script>
    $(function() {

        // console.profile('Profile Sttart');

        App.init();
        Util.init();
        Global.init();
        AdminGlobal.init();
        FormValidation.init();

        Biz.init();

        KindEditor.options.uploadJson = '${ctx}/w/image/upload/kind-editor.json;JSESSIONID=${pageContext.session.id}'

        App.unblockUI($("body"));

        //console.profileEnd();
    });
</script>