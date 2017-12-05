<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>${systemTitle}</title>
    <%@include file="/WEB-INF/views/layouts/include-header.jsp" %>
    <link href="${ctx}/assets/global/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet"
          type="text/css"/>

    <link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/select2/css/select2.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/select2/css/select2-bootstrap.min.css"/>

    <!-- BEGIN THEME LAYOUT STYLES -->
    <link href="${ctx}/assets/layouts/layout/css/layout.min.css" rel="stylesheet" type="text/css"/>
    <link href="${ctx}/assets/layouts/layout/css/themes/light.min.css" rel="stylesheet" type="text/css"
          id="style_color"/>
    <!-- END THEME LAYOUT STYLES -->

    <link href="${ctx}/assets/apps/css/admin.css?_=${buildVersion}" rel="stylesheet" type="text/css"/>

    <script type="text/javascript">
        var READ_FILE_URL_PREFIX = "${readFileUrlPrefix}";
        var DASHBOARD_URI = "/admin/dashboard";
    </script>

    <sitemesh:write property='head'/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-header-fixed page-sidebar-closed-hide-logo page-content-white">
<div class="page-wrapper">
    <!-- BEGIN HEADER -->
    <div class="page-header navbar navbar-fixed-top">
        <!-- BEGIN TOP NAVIGATION BAR -->
        <div class="page-header-inner ">
            <!-- BEGIN LOGO -->
            <div class="page-logo">
                <a href="${ctx}/admin" style="text-decoration:none">
                    <span style="float: left">
                       <img src="${ctx}/assets/apps/img/logo.png" style="height: 30px;margin: 10px"/>
                    </span>
                    <span style="color: white;font-size: 25px;display:inline-block;padding-top: 5px">${systemTitle}</span>
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
                        <a href="#/admin/profile/user-message" class="dropdown-toggle"
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
                                <shiro:principal property="authUid"/>
                            </span>
                            <i class="fa fa-angle-down"></i> </a>
                        <ul class="dropdown-menu dropdown-menu-default">
                            <li><a href="#/admin/profile/edit" data-path="个人配置"><i class="fa fa-user"></i> 个人配置</a></li>
                            <li><a href="javascript:;" data-url="${ctx}/admin/profile/password"
                                   data-toggle="modal-ajaxify"
                                   data-modal-size="500px"
                                   title="修改密码"><i
                                    class="fa fa-key"></i> 修改密码</a></li>
                            <li class="divider"></li>
                            <li>
                                <a id="a-logout" class="mt-sweetalert" href="javascript:;"><i class="fa fa-sign-out"></i> 注销登录</a>
                            </li>
                        </ul>
                    </li>
                    <!-- END USER LOGIN DROPDOWN -->

                    <!-- BEGIN QUICK SIDEBAR TOGGLER -->
                    <!-- DOC: Apply "dropdown-dark" class after below "dropdown-extended" to change the dropdown styte -->
                    <li class="dropdown dropdown-extended hidden-xs hidden-sm" id="theme-toggler"
                        style="padding-left: 0px">
                        <a href="javascript:;" class="dropdown-toggle" style="padding-right: 10px">
                            <i class="icon-settings"></i>
                        </a>
                    </li>
                    <!-- END QUICK SIDEBAR TOGGLER -->
                </ul>
            </div>
            <!-- END TOP NAVIGATION MENU -->
        </div>
        <!-- END TOP NAVIGATION BAR -->

        <!-- BEGIN THEME PANEL -->
        <div class="theme-panel hidden-xs hidden-sm">
            <div class="toggler-close"></div>
            <div class="theme-options">
                <div class="theme-option theme-colors clearfix">
                    <span> THEME COLOR </span>
                    <ul>
                        <li class="color-default current tooltips" data-style="default" data-container="body"
                            data-original-title="Default"></li>
                        <li class="color-darkblue tooltips" data-style="darkblue" data-container="body"
                            data-original-title="Dark Blue"></li>
                        <li class="color-blue tooltips" data-style="blue" data-container="body"
                            data-original-title="Blue"></li>
                        <li class="color-grey tooltips" data-style="grey" data-container="body"
                            data-original-title="Grey"></li>
                        <li class="color-light tooltips" data-style="light" data-container="body"
                            data-original-title="Light"></li>
                        <li class="color-light2 tooltips" data-style="light2" data-container="body" data-html="true"
                            data-original-title="Light 2"></li>
                    </ul>
                </div>
                <div class="theme-option">
                    <span> Theme Style </span>
                    <select class="layout-style-option form-control input-sm">
                        <option value="square" selected="selected">Square corners</option>
                        <option value="rounded">Rounded corners</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Layout </span>
                    <select class="layout-option form-control input-sm">
                        <option value="fluid" selected="selected">Fluid</option>
                        <option value="boxed">Boxed</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Header </span>
                    <select class="page-header-option form-control input-sm">
                        <option value="fixed" selected="selected">Fixed</option>
                        <option value="default">Default</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Top Menu Dropdown</span>
                    <select class="page-header-top-dropdown-style-option form-control input-sm">
                        <option value="light" selected="selected">Light</option>
                        <option value="dark">Dark</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Sidebar Mode</span>
                    <select class="sidebar-option form-control input-sm">
                        <option value="fixed">Fixed</option>
                        <option value="default" selected="selected">Default</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Sidebar Menu </span>
                    <select class="sidebar-menu-option form-control input-sm">
                        <option value="accordion" selected="selected">Accordion</option>
                        <option value="hover">Hover</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Sidebar Style </span>
                    <select class="sidebar-style-option form-control input-sm">
                        <option value="default" selected="selected">Default</option>
                        <option value="light">Light</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Sidebar Position </span>
                    <select class="sidebar-pos-option form-control input-sm">
                        <option value="left" selected="selected">Left</option>
                        <option value="right">Right</option>
                    </select>
                </div>
                <div class="theme-option">
                    <span> Footer </span>
                    <select class="page-footer-option form-control input-sm">
                        <option value="fixed">Fixed</option>
                        <option value="default" selected="selected">Default</option>
                    </select>
                </div>
            </div>
        </div>
        <!-- END THEME PANEL -->
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
                    <!-- DOC: To remove the sidebar toggler from the sidebar you just need to completely remove the below "sidebar-toggler-wrapper" LI element -->
                    <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
                    <li class="sidebar-toggler-wrapper hide">
                        <div class="sidebar-toggler">
                            <span></span>
                        </div>
                    </li>
                    <!-- END SIDEBAR TOGGLER BUTTON -->
                    <!-- DOC: To remove the search box from the sidebar you just need to completely remove the below "sidebar-search-wrapper" LI element -->
                    <li class="sidebar-search-wrapper">
                        <!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
                        <!-- DOC: Apply "sidebar-search-bordered" class the below search form to have bordered search box -->
                        <!-- DOC: Apply "sidebar-search-bordered sidebar-search-solid" class the below search form to have bordered & solid search box -->
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
                        <li><a class="btn-dashboard" href="#/dashboard"><i class="fa fa-home"></i> 首页 </a></li>
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
            &copy; S2JH4Net 2014 - 2017
            <c:if test="${devMode}">
                <span>V${buildVersion}</span>
            </c:if>
        </div>
        <div class="scroll-to-top">
            <i class="icon-arrow-up"></i>
        </div>
    </div>
    <!-- END FOOTER -->

    <!-- BEGIN MISC -->
    <div class="hide">
        <form class="form-horizontal" id="singleFileUploadForm" enctype="multipart/form-data" method="post">
            <input type="file" name="fileUpload"/>
            <button type="submit" class="btn">提交</button>
        </form>
    </div>

    <!-- BEGIN FileUpload FORM -->
    <div class="modal fade" id="fileupload-dialog" tabindex="-1" role="basic" aria-hidden="true">
        <div class="modal-dialog modal-wide">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
                    <h4 class="modal-title">文件上传</h4>
                </div>
                <div class="modal-body form">
                    <form id="fileupload" enctype="multipart/form-data" method="POST">
                        <input type="hidden" name="attachmentName" value="attachments"/>
                        <div class="row fileupload-buttonbar">
                            <div class="col-lg-7">
                                <!-- The fileinput-button span is used to style the file input field as button -->
                                <span class="btn green fileinput-button"> <i
                                        class="fa fa-plus"></i> <span>添加文件...</span> <input
                                        type="file" multiple="" name="files">
									</span>
                                <button class="btn blue start" type="submit">
                                    <i class="fa fa-upload"></i> <span>开始上传</span>
                                </button>
                                <button class="btn yellow cancel" type="reset">
                                    <i class="fa fa-ban"></i> <span>取消上传</span>
                                </button>
                                <!-- The loading indicator is shown during file processing -->
                                <span class="fileupload-loading"></span>
                            </div>
                            <!-- The global progress information -->
                            <div class="col-lg-5 fileupload-progress fade">
                                <!-- The global progress bar -->
                                <div aria-valuemax="100" aria-valuemin="0" role="progressbar"
                                     class="progress progress-striped active">
                                    <div style="width: 0%;" class="progress-bar progress-bar-success"></div>
                                </div>
                                <!-- The extended global progress information -->
                                <div class="progress-extended">&nbsp;</div>
                            </div>
                        </div>
                        <table class="table table-striped clearfix" role="presentation">
                            <tbody class="files"></tbody>
                        </table>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn default" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn blue btn-add">添加</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- END FileUpload FORM -->

    <!-- END MISC -->
</div>
<!-- END FOOTER -->

<%@include file="/WEB-INF/views/layouts/include-footer.jsp" %>

<script src="${ctx}/assets/global/plugins/bootstrap-hover-dropdown/bootstrap-hover-dropdown.min.js"
        type="text/javascript"></script>

<script src="${ctx}/assets/global/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>
<link href="${ctx}/assets/global/plugins/bootstrap-daterangepicker/daterangepicker.min.css" rel="stylesheet"
      type="text/css"/>

<script src="${ctx}/assets/global/plugins/bootstrap-datepicker/js/bootstrap-datepicker.js"
        type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-datepicker/locales/bootstrap-datepicker.zh-CN.min.js"
        type="text/javascript"></script>
<link href="${ctx}/assets/global/plugins/bootstrap-datepicker/css/bootstrap-datepicker3.min.css" rel="stylesheet"
      type="text/css"/>

<script src="${ctx}/assets/global/plugins/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"
        type="text/javascript"></script>
<script src="${ctx}/assets/global/plugins/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"
        type="text/javascript"></script>
<link href="${ctx}/assets/global/plugins/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
      type="text/css"/>

<!--
    <script src="${ctx}/assets/global/plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
    <link href="${ctx}/assets/global/plugins/gritter/css/jquery.gritter.css" rel="stylesheet" type="text/css" />
-->

<!-- END PAGE LEVEL PLUGINS -->
<script src="${ctx}/assets/global/plugins/bootstrap-switch/js/bootstrap-switch.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/bootstrap-switch/css/bootstrap-switch.css"/>

<script type="text/javascript"
        src="${ctx}/assets/global/plugins/bootstrap-contextmenu/bootstrap-contextmenu.js"></script>

<script type="text/javascript"
        src="${ctx}/assets/global/plugins/bootstrap-editable/bootstrap-editable/js/bootstrap-editable.min.js"></script>
<link rel="stylesheet" type="text/css"
      href="${ctx}/assets/global/plugins/bootstrap-editable/bootstrap-editable/css/bootstrap-editable.css"/>

<!-- The basic File Upload plugin -->
<script src="${ctx}/assets/apps/plugins/uploadify/jquery.uploadify.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/uploadify/uploadify.css"/>
<!-- The main application script -->
<!-- The XDomainRequest Transport is included for cross-domain file deletion for IE 8 and IE 9 -->
<!--[if (gte IE 8)&(lt IE 10)]>
<script src="${ctx}/assets/apps/plugins/jquery-file-upload/js/cors/jquery.xdr-transport.js"></script>
<![endif]-->
<!-- END:File Upload Plugin JS files-->

<script type="text/javascript" src="${ctx}/assets/apps/plugins/tooltipster/js/jquery.tooltipster.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/tooltipster/css/tooltipster.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/tooltipster/css/themes/tooltipster-punk.css"/>

<script src="${ctx}/assets/apps/plugins/JSPinyin.js"></script>

<script src="${ctx}/assets/apps/plugins/echarts/dist/echarts-all.js"></script>

<link rel="stylesheet" href="${ctx}/assets/apps/plugins/free-jqgrid/css/ui.jqgrid.min.css">
<script src="${ctx}/assets/apps/plugins/free-jqgrid/i18n/grid.locale-cn.js"></script>
<script src="${ctx}/assets/apps/plugins/free-jqgrid/jquery.jqgrid.src.js"></script>
<script src="${ctx}/assets/apps/plugins/free-jqgrid/plugins/min/jquery.jqgrid.showhidecolumnmenu.js"></script>
<script src="${ctx}/assets/apps/plugins/free-jqgrid/plugins/min/jquery.contextmenu-ui.js"></script>
<script src="${ctx}/assets/apps/plugins/free-jqgrid/plugins/min/jquery.createcontexmenufromnavigatorbuttons.js"></script>
<script src="${ctx}/assets/apps/plugins/free-jqgrid/plugins/min/ui.multiselect.js"></script>
<link rel="stylesheet" href="${ctx}/assets/apps/plugins/free-jqgrid/plugins/css/ui.multiselect.min.css">

<script src="${ctx}/assets/apps/plugins/jquery-ztree/js/jquery.ztree.all-3.5.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/jquery-ztree/css/zTreeStyle/zTreeStyle.css">

<script src="${ctx}/assets/apps/plugins/kindeditor/kindeditor-all.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/assets/apps/plugins/kindeditor/themes/default/default.css">

<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/jquery-multi-select/css/multi-select.css"/>
<script type="text/javascript"
        src="${ctx}/assets/global/plugins/jquery-multi-select/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="${ctx}/assets/apps/plugins/jquery.quicksearch.js"></script>

<script src="${ctx}/assets/apps/plugins/jquery.address/jquery.address-1.5.min.js"></script>

<script src="${ctx}/assets/global/plugins/jcrop/js/jquery.Jcrop.min.js"></script>
<link href="${ctx}/assets/global/plugins/jcrop/css/jquery.Jcrop.min.css" rel="stylesheet"/>

<script src="${ctx}/assets/apps/plugins/jquery.qrcode.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/apps/plugins/html2canvas/html2canvas.min.js" type="text/javascript"></script>
<script src="${ctx}/assets/apps/plugins/canvas2image.js" type="text/javascript"></script>

<link rel="stylesheet" type="text/css" href="${ctx}/assets/global/plugins/bootstrap-toastr/toastr.min.css"/>
<script src="${ctx}/assets/global/plugins/bootstrap-toastr/toastr.min.js"></script>

<script src="//api.map.baidu.com/api?v=2.0&ak=${baiduMapAppkey}" type="text/javascript"></script>

<!-- BEGIN THEME LAYOUT SCRIPTS -->
<script src="${ctx}/assets/layouts/layout/scripts/layout.min.js" type="text/javascript"></script>
<!-- END THEME LAYOUT SCRIPTS -->

<script src="${ctx}/assets/apps/scripts/component/dynamic-table.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/double-multi-select.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/component/image-uploader.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/component/file-uploader.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/apps/scripts/component/grid.js" ?_=${buildVersion}></script>
<script src="${ctx}/assets/apps/scripts/component/gmaps-baidu.js" ?_=${buildVersion}></script>

<script src="${ctx}/assets/apps/scripts/function/data-profile-param.js?_=${buildVersion}"></script>

<script src="${ctx}/assets/pages/admin/scripts/index.js?_=${buildVersion}"></script>
<script src="${ctx}/assets/apps/scripts/admin-theme.js?_=${buildVersion}"></script>


<script>
    $(function () {

        // console.profile('Profile Sttart');


        //KindEditor文件操作涉及到Flash集成，因此需要在upload URL追加JSESSIONID参数以进行登录用户标识传递
        KindEditor.options.uploadJson = '${ctx}/w/image/upload/kind-editor.json;JSESSIONID=${pageContext.session.id}'

        //App.init();
        Util.init();
        Global.init();
        AdminIndex.init();

        App.unblockUI();

        //console.profileEnd();
    });
</script>

</body>
<!-- END BODY -->
</html>