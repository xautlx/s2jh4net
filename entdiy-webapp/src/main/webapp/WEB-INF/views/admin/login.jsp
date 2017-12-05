<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <title>管理登录</title>
</head>
<body class="login">
<div class="row">
    <div class="col-md-1">
    </div>
    <div class="col-md-10">
        <!-- BEGIN LOGIN FORM -->
        <form id="login-form" class="login-form" action="${ctx}/admin/login" method="post">
            <%-- 管理端登录标识 --%>
            <input type="hidden" name="source" value="A"/>
            <h3 class="form-title" style="color: #666666">系统登录</h3>
            <div class="form-group">
                <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                <label class="control-label visible-ie8 visible-ie9">登录账号</label>
                <input class="form-control placeholder-no-fix" type="text"
                       placeholder="登录账号" name="username" value="${auth_username_value}"
                       data-input-icon="fa-user" required="true" data-msg-required="请填写登录账号"/>
            </div>
            <div class="form-group">
                <label class="control-label visible-ie8 visible-ie9">登录密码</label>
                <input class="form-control placeholder-no-fix" type="password"
                       placeholder="登录密码" name="password" data-input-icon="fa-lock"
                       required="true" data-msg-required="请填写登录密码"/>
            </div>
            <c:if test="${auth_captcha_required!=null}">
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
                <label> <input type="checkbox" name="rememberMe" checked="true" value="true"/> 记住我，下次自动登录
                </label>
                <button type="submit" class="btn blue pull-right">
                    登录 <i class="m-icon-swapright m-icon-white"></i>
                </button>
            </div>
            <hr style="margin: 0px"/>
            <div class="forget-password">
                <c:if test="${casSupport}">
                    <div class="row">
                        <div class="col-md-12">
                            <p class="pull-right">
                                <a href='<s:property value="casRedirectUrl"/>'>单点登录</a>
                            </p>
                        </div>
                    </div>
                </c:if>
                <div class="row">
                    <div class="col-md-12">
                        <p class="pull-right">
                            <span>忘记密码?</span>
                            <a href="${ctx}/admin/pub/password/forget" title="找回密码">
                                找回密码
                            </a>
                            <%--暂时屏蔽注册功能实现
                            <c:if test="${mgmtSignupEnabled}">
                                <span>&nbsp; &nbsp;&nbsp; &nbsp; 没有账号?</span>
                                <a href="${ctx}/admin/pub/signup" title="自助注册">
                                    自助注册
                                </a>
                            </c:if>
                            --%>
                        </p>
                    </div>
                </div>
            </div>
        </form>
        <!-- END LOGIN FORM -->

        <c:if test="${devMode}">
            <div class="form-info">
                <p id="devModeTips" style="padding: 10px">
                    <b> 开发/测试/演示登录快速入口:
                        <a href="javascript:void(0)" onclick="setupDevUser('admin','admin123')">
                            admin/admin123
                        </a>
                    </b>
                </p>
                <script type="text/javascript">
                    var $form = $("#login-form");

                    function setupDevUser(user, password) {
                        $("input[name='username']", $form).val(user);
                        $("input[name='password']", $form).val(password);
                        $form.submit();
                    }
                    jQuery(document).ready(function () {
                        $("#devModeTips").pulsate({
                            color: "#bf1c56",
                            repeat: 10
                        });
                    });
                </script>
            </div>
        </c:if>
    </div>
</div>
<script src="${ctx}/assets/pages/admin/scripts/login.js"></script>
</body>
</html>