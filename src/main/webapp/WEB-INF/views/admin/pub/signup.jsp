<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账号注册</title>
</head>
<body>
	<form class="form-horizontal form-bordered form-label-stripped form-validation" action="${ctx}/admin/signup"
		method="post" data-editrulesurl="false">
		<div class="form-body">
			<div class="row" data-equal-height="false">
				<div class="col-md-6">
					<p>请填写如下必须的注册信息：</p>
					<div class="form-group">
						<label class="control-label">登录账号</label>
						<div class="controls">
							<div class="input-icon">
								<i class="fa fa-user"></i> <input class="form-control" type="text" name="signinid" required="true" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">输入登录密码</label>
						<div class="controls">
							<div class="input-icon">
								<i class="fa fa-lock"></i> <input class="form-control" type="password" autocomplete="off" name="password"
									required="true" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">再次输入密码</label>
						<div class="controls">
							<div class="input-icon">
								<i class="fa fa-check"></i> <input class="form-control" type="password" autocomplete="off" name="rpassword"
									required="true" data-rule-equalToByName="password" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">注册邮箱</label>
						<div class="controls">
							<div class="input-icon">
								<i class="fa fa-envelope"></i> <input class="form-control" type="text" placeholder="请填写真实有效邮箱地址，可用于邮件通知、找回密码等功能"
									name="email" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">验证码</label>
						<div class="controls">
							<div class="input-group">
								<div class="input-icon">
									<i class="fa fa-qrcode"></i> <input class="form-control captcha-text" type="text" autocomplete="off"
										placeholder="验证码...看不清可点击图片可刷新" name="captcha" required="true" />
								</div>
								<span class="input-group-btn" style="cursor: pointer;"> <img alt="验证码" height="34px" class="captcha-img"
									src="${ctx}/assets/img/captcha_placeholder.jpg" title="看不清？点击刷新" />
								</span>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<p>以下为选填的注册信息：</p>
					<div class="form-group">
						<label class="control-label">联系信息</label>
						<div class="controls">
							<textarea rows="4" class="form-control" name="contactInfo"
								placeholder="可自由填写申请人的姓名、电话、邮件、聊天账号等信息，用于系统管理员在需要时联系到您进行资料确认"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">备注说明</label>
						<div class="controls">
							<textarea rows="4" class="form-control" name="remarkInfo"
								placeholder="提供相关备注说明信息，如账号类型，需要访问的 功能列表等，有助于管理员快速有效的进行账号设定"></textarea>
						</div>
					</div>
					<%-- 
											<p>上传注册相关资料附件：</p>
											<div class="row fileupload-buttonbar">
												<div>
													<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>Add
															files...</span> <!-- The file input field used as target for the file upload widget --> <input type="file"
														name="attachments" id="fileupload">
													</span>
													<!-- The loading indicator is shown during file processing -->
													<span class="fileupload-loading"></span>
												</div>
												<div class="files" id="files"></div>
											</div>
											--%>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12" style="padding-top: 10px">
					<label> <input type="checkbox" name="tnc" checked="checked" required="true" /> 同意遵守本系统相关访问和使用协议!
					</label>
					<div id="register_tnc_error"></div>
				</div>
			</div>
			<div class="note note-info" style="margin-bottom: 0px">
				<p>提交注册请求后，需要等待系统管理员人工审核授权，在此期间无法访问系统！</p>
			</div>
		</div>

		<div class="form-actions right">
			<button class="btn green" type="submit" data-post-dismiss="modal">提交</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
		</div>
	</form>
</body>
</html>
