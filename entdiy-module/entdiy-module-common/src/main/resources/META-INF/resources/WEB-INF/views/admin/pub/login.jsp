<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理登录</title>
    <base href="${applicationScope.ctx}/admin"/>
</head>
<body class="login">
<div class="row">
    <div class="col-md-1">
    </div>
    <div class="col-md-10">
        <!-- BEGIN LOGIN FORM -->
        <form id="login-form" class="login-form" action="admin/login" method="post">
            <%-- 管理端登录标识 --%>
            <input type="hidden" name="authType" value="admin"/>
            <h3 class="form-title" style="color: #666666">系统登录</h3>
            <div class="form-group">
                <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                <label class="control-label visible-ie8 visible-ie9">登录账号</label>
                <input class="form-control placeholder-no-fix" type="text"
                       placeholder="登录账号" name="username" value="${lastUserName}"
                       data-input-icon="fa-user" required="true" data-msg-required="请填写登录账号"/>
            </div>
            <div class="form-group">
                <label class="control-label visible-ie8 visible-ie9">登录密码</label>
                <input class="form-control placeholder-no-fix" type="password"
                       placeholder="登录密码" name="password" data-input-icon="fa-lock"
                       required="true" data-msg-required="请填写登录密码"/>
            </div>
            <c:if test="${captchaRequired!=null}">
                <div class="form-group">
                    <label class="control-label visible-ie8 visible-ie9">验证码</label>
                    <input class="form-control" data-toggle="captcha-code" data-input-icon="fa-qrcode"
                           type="text" name="captcha" required="true"/>
                </div>
            </c:if>
            <c:if test="${error!=null}">
                <div align='center' class='alert alert-danger'>${error}</div>
            </c:if>
            <div class="form-actions">
                <label>
                    <!--
                   <input type="checkbox" name="rememberMe" checked="true" value="true"/> 记住我，下次自动登录
                    -->
                </label>
                <button type="submit" class="btn blue pull-right">
                    登录 <i class="m-icon-swapright m-icon-white"></i>
                </button>
            </div>
            <hr style="margin: 0px"/>
            <div class="forget-password">
                <div class="row">
                    <div class="col-md-12">
                        <p class="pull-right">
                            <span>忘记密码?</span>
                            <a href="admin/pub/password/forget" title="找回密码">
                                找回密码
                            </a>
                        </p>
                    </div>
                </div>
                <c:if test="${weiXinOAuthUrl!=null}">
                    <div class="row">
                        <div class="col-md-12">
                            <p class="pull-right">
                                <a href='${weiXinOAuthUrl}'>微信登录</a>
                            </p>
                        </div>
                    </div>
                </c:if>
            </div>
        </form>
        <!-- END LOGIN FORM -->
        <c:if test="${noneProductionMode}">
            <jsp:include page="login-dev.jsp"/>
        </c:if>
    </div>
</div>
<script src="assets/pages/admin/scripts/login.js"></script>
</body>
</html>