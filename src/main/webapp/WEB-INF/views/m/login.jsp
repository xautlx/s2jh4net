<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.LockedAccountException "%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>移动应用登录</title>
</head>
<body>
	<div data-role="page" id="pageLogin">
		<div data-role="header">
			<a href="#" data-rel="back" class="ui-btn-left ui-btn  ui-btn-icon-notext ui-icon-back"></a>
			<h1>访问登录</h1>
		</div>
		<div role="main" class="ui-content">
			<form class="form-login" action="${ctx}/m/login" method="post" data-ajax="false">
				<%--移动端登录标识 --%>
				<input type="hidden" name="source" value="M" /> <label for="username">登录账号</label> <input type="text"
					name="username" id="username" value=""> <label for="password">登录密码</label> <input type="password"
					name="password" id="password" value="">
				<fieldset data-role="controlgroup">
					<input type="checkbox" name="rememberMe" id="rememberMe"> <label for="rememberMe">记住我，下次自动登录</label>
				</fieldset>
				<c:if test="${error!=null}">
					<div align='center' class='alert alert-danger'>${error}</div>
				</c:if>
				<button type="submit">登录</button>
			</form>
			<p>
				<a href="begin-password-reset.html">Can't access your account?</a>
			</p>
		</div>

		<script type="text/javascript">
            $(document).on("pageinit", "#pageLogin", function() {
                var $page = $(this);
                $('.form-login', $page).validate({
                    rules : {
                        username : {
                            required : true
                        },
                        password : {
                            required : true
                        }
                    },
                    messages : {
                        username : {
                            required : "请输入电子邮件地址"
                        },
                        password : {
                            required : "请输入登录密码"
                        }
                    },
                    submitHandler : function(form) {
                        form.submit();
                        return false;
                    }
                });
            });
        </script>
	</div>
</body>
</html>