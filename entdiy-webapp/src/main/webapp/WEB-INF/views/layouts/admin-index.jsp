<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="systemName" value="<%=com.entdiy.core.web.AppContextHolder.getSystemName()%>"/>
<c:set var="buildVersion" value="<%=com.entdiy.core.web.AppContextHolder.getBuildVersion()%>"/>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>${systemName}</title>
    <base href="${applicationScope.ctx}/admin"/>
    <%@include file="/WEB-INF/views/layouts/include-header.jsp" %>

    <link href="assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/bootstrap-switch/css/bootstrap-switch.css" rel="stylesheet" type="text/css"/>
    <link href="assets/apps/plugins/kindeditor/themes/default/default.css" rel="stylesheet" type="text/css">
    <link href="assets/global/plugins/jquery-multi-select/css/multi-select.css" rel="stylesheet" type="text/css"/>
    <link href="assets/apps/plugins/free-jqgrid/css/ui.jqgrid.min.css" rel="stylesheet" type="text/css">
    <link href="assets/apps/plugins/free-jqgrid/plugins/css/ui.multiselect.min.css" rel="stylesheet" type="text/css">
    <link href="assets/apps/plugins/tooltipster/css/tooltipster.css" rel="stylesheet" type="text/css"/>
    <link href="assets/apps/plugins/tooltipster/css/themes/tooltipster-punk.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/bootstrap-toastr/toastr.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/global/plugins/jcrop/css/jquery.Jcrop.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/apps/plugins/jquery-ztree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
    <link href="assets/apps/plugins/jquery-ztree/css/awesomeStyle/font-awesome-zTree.css" rel="stylesheet" type="text/css">

    <link href="assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/layouts/layout/css/themes/light.min.css" rel="stylesheet" type="text/css" id="style_color"/>

    <link href="assets/apps/css/admin.css?_=${buildVersion}" rel="stylesheet" type="text/css"/>

    <link rel="shortcut icon" href="assets/pages/img/favicon.ico"/>
    <sitemesh:write property='head'/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-sidebar-closed-hide-logo page-content-white">
<div class="page-wrapper">
    <!-- BEGIN HEADER -->
    <div class="page-header navbar navbar-static-top">
        <!-- BEGIN TOP NAVIGATION BAR -->
        <div class="page-header-inner ">
            <!-- BEGIN LOGO -->
            <div class="page-logo">
                <a href="admin" style="text-decoration:none">
                    <span style="float: left">
                       <img src="assets/pages/img/logo.png" style="height: 30px;margin: 10px"/>
                    </span>
                    <span style="color: white;font-size: 25px;display:inline-block;padding-top: 5px">${systemName}</span>
                </a>
            </div>
            <!-- END LOGO -->
            <!-- BEGIN RESPONSIVE MENU TOGGLER -->
            <a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse"
               data-target=".navbar-collapse">
                <span></span>
            </a>
            <!-- END RESPONSIVE MENU TOGGLER -->
            <!-- BEGIN TOP NAVIGATION MENU -->
            <div class="top-menu">
                <ul class="nav navbar-nav pull-right" style="margin-right: 0px">
                    <!-- BEGIN NOTIFICATION DROPDOWN -->
                    <li class="dropdown dropdown-extended" id="header_notification_bar">
                        <a href="#/admin/profile/notify-message" class="dropdown-toggle"
                           data-toggle="dropdown" data-hover="dropdown"
                           data-close-others="true"
                           data-path="公告信息列表">
                            <i class="fa fa-bullhorn"></i>
                            <span class="badge badge-default"
                                  style="display: none"></span>
                        </a>
                        <ul class="dropdown-menu extended notification">
                            <li class="message-info"></li>
                        </ul>
                    </li>
                    <!-- END NOTIFICATION DROPDOWN -->

                    <!-- BEGIN INBOX DROPDOWN -->
                    <li class="dropdown dropdown-extended" id="header_inbox_bar">
                        <a href="#/admin/profile/account-message" class="dropdown-toggle"
                           data-toggle="dropdown" data-hover="dropdown"
                           data-close-others="true"
                           data-path="个人消息列表">
                            <i class="fa fa-envelope"></i>
                            <span class="badge badge-default" style="display: none"></span>
                        </a>
                        <ul class="dropdown-menu extended inbox">
                            <li class="message-info"></li>
                        </ul>
                    </li>
                    <!-- END INBOX DROPDOWN -->

                    <!-- BEGIN USER LOGIN DROPDOWN -->
                    <li class="dropdown dropdown-user">
                        <a href="javascripts:;"
                           class="dropdown-toggle"
                           data-toggle="dropdown"
                           data-hover="dropdown"
                           data-close-others="true">
                            <span class="username">
                                <shiro:principal property="nickname"/>
                            </span>
                            <i class="fa fa-angle-down"></i> </a>
                        <ul class="dropdown-menu dropdown-menu-default">
                            <li><a href="#/admin/profile/edit" data-path="个人配置"><i class="fa fa-user"></i> 个人配置</a></li>
                            <li><a href="javascript:;" data-url="/admin/profile/password"
                                   data-toggle="modal-ajaxify"
                                   data-width="500px"
                                   title="修改密码"><i
                                    class="fa fa-key"></i> 修改密码</a></li>
                            <li class="divider"></li>
                            <li>
                                <a id="a-logout" class="mt-sweetalert" href="javascript:;"><i
                                        class="fa fa-sign-out"></i> 注销登录</a>
                            </li>
                        </ul>
                    </li>
                    <!-- END USER LOGIN DROPDOWN -->
                </ul>
            </div>
            <!-- END TOP NAVIGATION MENU -->
        </div>
        <!-- END TOP NAVIGATION BAR -->
    </div>
    <!-- END HEADER -->
    <div class="clearfix"></div>
    <!-- BEGIN CONTAINER -->
    <div class="page-container">
        <!-- BEGIN SIDEBAR -->
        <div class="page-sidebar-wrapper">
            <div class="page-sidebar navbar-collapse collapse">
                <!-- BEGIN SIDEBAR MENU -->
                <ul class="page-sidebar-menu  page-header-fixed" data-keep-expanded="true" data-auto-scroll="false"
                    data-slide-speed="200" style="padding-top: 0px">
                    <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                    <li class="sidebar-toggler-wrapper hide">
                        <div class="sidebar-toggler">
                            <span></span>
                        </div>
                    </li>
                    <!-- END SIDEBAR TOGGLER BUTTON -->
                    <li class="sidebar-search-wrapper">
                        <form class="sidebar-search " method="POST" style="margin: 12px 5px;min-height: 20px">
                            <div class="menu-toggler sidebar-toggler" style="float:left;margin: 0px;padding: 3px 8px">
                                <span></span>
                            </div>
                            <div class="input-group" style="margin-left: 40px">
                                <input type="text" name="search" class="form-control" placeholder="菜单项快速查询过滤..."
                                       value=""
                                       title="试试输入菜单名称拼音首字母"/>
                                <span class="input-group-btn">
                                            <a href="javascript:;" class="btn">
                                                <i class="icon-magnifier form-control"></i>
                                            </a>
                                        </span>

                            </div>

                        </form>
                        <!-- END RESPONSIVE QUICK SEARCH FORM -->
                    </li>
                </ul>
                <!-- END SIDEBAR MENU -->
            </div>
        </div>
        <!-- END SIDEBAR -->

        <!-- BEGIN CONTENT -->
        <div class="page-content-wrapper">
            <div class="page-content">
                <!-- BEGIN PAGE BAR -->
                <div class="page-bar" id="layout-nav">
                    <ul class="page-breadcrumb">
                        <li><a class="btn-dashboard" href="#/admin/dashboard"><i class="fa fa-home"></i> 首页</a></li>
                    </ul>
                    <div class="page-toolbar">
                        <div class="btn-group pull-right">
                            <button data-close-others="true" data-delay="1000" data-toggle="dropdown"
                                    class="btn default dropdown-toggle"
                                    type="button">
                                <span><i class="fa fa-reorder"></i> 访问列表</span> <i class="fa fa-angle-down"></i>
                            </button>
                            <ul role="menu" class="dropdown-menu">
                            </ul>
                            <button class="btn default" type="button"
                                    onclick="$(this).prev('ul.dropdown-menu').find('li.active span.badge').click()">
                                <i class="fa fa-times"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <!-- END PAGE BAR -->

                <div class="tab-content form">
                    <div id="tab_content_dashboard"></div>
                </div>
            </div>
        </div>
        <!-- BEGIN CONTENT -->
    </div>
    <!-- END CONTAINER -->

    <!-- BEGIN FOOTER -->
    <div class="page-footer">
        <div class="page-footer-inner">
            &copy; EntDIY.com 2017 All Rights Reserved.
            <span>V${buildVersion}</span>
        </div>
        <div class="scroll-to-top">
            <i class="icon-arrow-up"></i>
        </div>
    </div>
    <!-- END FOOTER -->
</div>
<!-- END FOOTER -->

<%@include file="/WEB-INF/views/layouts/include-footer.jsp" %>

<script src="assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>

<!--
    <script src="assets/global/plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
    <link href="assets/global/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css" />
-->

<script src="assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/tooltipster/js/jquery.tooltipster.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/jquery-ztree/js/jquery.ztree.all.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/kindeditor/kindeditor-all-min.js" type="text/javascript"></script>
<script src="assets/global/plugins/jquery-multi-select/js/jquery.multi-select.js" type="text/javascript"></script>
<script src="assets/apps/plugins/jquery.quicksearch.js" type="text/javascript"></script>
<script src="assets/apps/plugins/jquery.address/jquery.address-1.5.min.js" type="text/javascript"></script>
<script src="assets/global/plugins/jcrop/js/jquery.Jcrop.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/jquery.qrcode.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/html2canvas/html2canvas.min.js" type="text/javascript"></script>
<script src="assets/apps/plugins/canvas2image.js" type="text/javascript"></script>
<script src="assets/global/plugins/bootstrap-toastr/toastr.min.js" type="text/javascript"></script>

<script src="assets/apps/plugins/free-jqgrid/i18n/grid.locale-cn.js" type="text/javascript"></script>
<script src="assets/apps/plugins/free-jqgrid/jquery.jqgrid.src.js" type="text/javascript"></script>
<script src="assets/apps/plugins/free-jqgrid/plugins/min/jquery.jqgrid.showhidecolumnmenu.js" type="text/javascript"></script>
<script src="assets/apps/plugins/free-jqgrid/plugins/min/ui.multiselect.js" type="text/javascript"></script>

<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="assets/layouts/layout/scripts/layout.min.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

<script src="assets/apps/plugins/JSPinyin.js" type="text/javascript"></script>
<script src="assets/apps/scripts/component/dynamic-edit-table.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/double-multi-select.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/image-uploader.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/file-uploader.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/data-grid.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/textarea-htmleditor.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/dropdown-tree.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/component/nav-tree.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/apps/scripts/function/data-profile-param.js?_=${buildVersion}" type="text/javascript"></script>
<script src="assets/pages/admin/scripts/index.js?_=${buildVersion}" type="text/javascript"></script>

<script>
    $(function () {
        // console.profile('Profile Sttart');
        Util.init();
        Global.init();
        AdminIndex.init();

        App.unblockUI();
        //console.profileEnd();
    });
</script>

<!-- 特殊处理外部API请求加载，避免在离线和网速慢情况访问页面显示停滞 -->
<script type="text/javascript">
    function baiduMapLoadCallback() {
        var script = document.createElement("script");
        script.src = "assets/apps/scripts/component/gmaps-baidu.js?_=${buildVersion}";
        document.body.appendChild(script);
    }

    window.onload = function () {
        var script = document.createElement("script");
        script.src = "//api.map.baidu.com/api?v=2.0&ak=${baiduMapAppkey}&callback=baiduMapLoadCallback";
        document.body.appendChild(script);
    };
</script>

<script>
    //百度统计代码，做一个基础的框架演示站点访问数据统计，实际项目开发可以移除
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?5f64653382c3bd79af3c297bbafa5a6a";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
</script>
</body>
<!-- END BODY -->
</html>