<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<title>管理登录</title>
</head>
<body>
	<div class="row">
		<div class="col-md-6">
			<!-- BEGIN LOGIN FORM -->
			<form id="login-form" class="login-form" action="${ctx}/admin/login" method="post">
				<%--管理端登录标识 --%>
				<input type="hidden" name="source" value="A" />
				<h3 class="form-title" style="color: #666666">系统登录</h3>
				<div class="form-group">
					<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
					<label class="control-label visible-ie8 visible-ie9">登录账号</label>
					<div class="input-icon">
						<i class="fa fa-user"></i> <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
							placeholder="登录账号" name="username" value="${auth_username_value}" required="true" data-msg-required="请填写登录账号" />
					</div>
				</div>
				<div class="form-group">
					<label class="control-label visible-ie8 visible-ie9">登录密码</label>
					<div class="input-icon">
						<i class="fa fa-lock"></i> <input class="form-control placeholder-no-fix" type="password" autocomplete="off"
							placeholder="登录密码" name="password" required="true" data-msg-required="请填写登录密码" />
					</div>
				</div>
				<c:if test="${auth_captcha_required!=null}">
					<div class="form-group">
						<label class="control-label visible-ie8 visible-ie9">验证码</label>
						<div class="input-group">
							<div class="input-icon">
								<i class="fa fa-qrcode"></i> <input class="form-control captcha-text" type="text" autocomplete="off"
									placeholder="验证码...看不清可点击图片可刷新" name="captcha" required="true" data-msg-required="请填写验证码" />
							</div>
							<span class="input-group-btn" style="cursor: pointer;"> <img alt="验证码" class="captcha-img"
								src="${ctx}/assets/img/captcha_placeholder.jpg" title="看不清？点击刷新" />
							</span>
						</div>
					</div>
				</c:if>
				<c:if test="${error!=null}">
					<div align='center' class='alert alert-danger'>${error}</div>
				</c:if>
				<div class="form-actions">
					<label> <input type="checkbox" name="rememberMe" checked="true" value="true" /> 记住我，下次自动登录
					</label>
					<button type="submit" class="btn blue pull-right">
						登录 <i class="m-icon-swapright m-icon-white"></i>
					</button>
				</div>
				<div class="forget-password">
					<div class="row">
						<div class="col-md-3">
							<c:if test="${casSupport}">
								<p>
									<a href='<s:property value="casRedirectUrl"/>'>单点登录</a>
								</p>
							</c:if>
						</div>
						<div class="col-md-9">
							<p class="pull-right">
								忘记密码? <a href="${ctx}/admin/password/forget" data-toggle="modal-ajaxify" title="找回密码" data-modal-size="550px">找回密码</a>
								<c:if test="${mgmtSignupEnabled}">
                                &nbsp; &nbsp;&nbsp; &nbsp; 没有账号? <a href="${ctx}/admin/signup"
										data-toggle="modal-ajaxify" title="自助注册">自助注册</a>
								</c:if>
							</p>
						</div>
					</div>
				</div>
			</form>
			<!-- END LOGIN FORM -->
		</div>
		<div class="col-md-1"></div>
		<div class="col-md-5">
			<div class="form-info" style="height: 270px; margin-top: 50px">
				<h4>访问提示</h4>
				<p>建议使用最新版本Firefox或Chrome浏览器访问应用以避免不必要的浏览器兼容性问题。</p>
				<c:if test="${cfg.dev_mode}">
					<p id="devModeTips" style="padding: 10px">
						<b> 开发/测试/演示登录快速入口: <br /> <br /> <a href="javascript:void(0)" onclick="setupDevUser('admin','admin123')">admin超级管理员(admin/admin123)</a>
						</b>
					</p>
					<script type="text/javascript">
                        var $form = $("#login-form");

                        $("input[name='username']", $form).val("admin");
                        $("input[name='password']", $form).val("admin123");

                        function setupDevUser(user, password) {
                            $("input[name='username']", $form).val(user);
                            $("input[name='password']", $form).val(password);
                            $form.submit();
                        }
                        jQuery(document).ready(function() {
                            $("#devModeTips").pulsate({
                                color : "#bf1c56",
                                repeat : 10
                            });
                        });
                    </script>
				</c:if>
			</div>
		</div>
	</div>

	<script src="${ctx}/assets/admin/app/login.js"></script>
</body>
</html>