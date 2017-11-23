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
<%@include file="/WEB-INF/views/layouts/include-header.jsp"%>
<link href="${ctx}/assets/apps/css/login.css" rel="stylesheet" type="text/css" />
<link rel="shortcut icon" href="${ctx}/assets/apps/img/favicon.ico" />
<sitemesh:write property='head' />
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="login">
    <!-- BEGIN LOGIN -->
    <div class="row hidden-xs hidden-sm" style="margin: 0px">
        <div class="col-md-12" style="margin: 0px">
            <div style="padding: 30px"></div>
        </div>
    </div>
    <div class="form-group" style="margin: 0px">
        <div class="col-md-2"></div>
        <div class="col-md-8" style="padding: 0px">
            <div class="content" style="width: 100%;">
                <div class="row">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="col-md-2">
                                <img src="${ctx}/assets/apps/img/logo.png" width="100px" />
                            </div>
                            <div class="col-md-8">
                                <h2 style="color: #555555">${applicationScope.cfg.cfg_system_title}-管理平台</h2>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <sitemesh:write property='body' />

                <!-- BEGIN COPYRIGHT -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="copyright pull-right">
                            <span title="${buildVersion}|<%= request.getLocalAddr()  %>:<%=request.getLocalPort()%>]"
                                style="display: inline-block;">
                                &copy; 2015 - 2017
                                <%=request.getServerName()%></span>
                            <c:if test="${cfg.dev_mode}">
                                <span>V${buildVersion} [${buildTimetamp}]</span>
                            </c:if>
                        </div>
                    </div>
                </div>
                <!-- END COPYRIGHT -->
            </div>
        </div>
        <div class="col-md-2"></div>
    </div>

    <%@include file="/WEB-INF/views/layouts/include-footer.jsp"%>


    <!-- BEGIN PAGE LEVEL SCRIPTS -->
    <script src="${ctx}/assets/apps/scripts/admin-global.js?_=${build_version}"></script>
    <script src="${ctx}/assets/apps/scripts/form-validation.js?_=${build_version}"></script>
    <script src="${ctx}/assets/apps/scripts/page.js?_=${build_version}"></script>
    <script src="${ctx}/assets/apps/scripts/admin-page.js?_=${build_version}"></script>

    <script>
        $(function() {

            // console.profile('Profile Sttart');

            App.init();
            Util.init();
            Global.init();
            AdminGlobal.init();
            FormValidation.init();
            FormValidation.initAjax();

            App.unblockUI($("body"));

            //console.profileEnd();
        });
    </script>

    <script src="${ctx}/assets/apps/scripts/login.js"></script>
    <!-- END PAGE LEVEL SCRIPTS -->
</body>
<!-- END BODY -->
</html>