<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>重置密码</title>
</head>
<body>
<div class="form">
    <form id="password-forget-form" class="form-horizontal form-bordered form-bordered-blank"
          method="post" data-validation='true'
          action="/pub/account/password/reset">
        <input type="hidden" name="authType" value="admin">
        <input type="hidden" name="code" value="${param.code}">
        <input type="hidden" name="email" value="${param.email}">
        <h3>重置密码</h3>
        <div class="form-body">
            <div class="form-group">
                <div class="controls controls-clearfix">
                    <p class="form-control-static">
                        当前正在通过邮箱 <span style="color: red">${param.email}</span> 找回重置密码操作，请下方输入此邮箱对应的登录账号，系统将进行邮箱和账号匹配校验以保护账户安全性.
                    </p>
                    <p class="form-control-static">
                        如果忘记此邮箱对应登录账号，请联系管理员协助处理.
                    </p>
                </div>
            </div>
            <div class="form-group">
                <div class="controls controls-clearfix">
                    <input type="text" class="form-control" name="uid" value="" required="true"
                           placeholder="为账户安全校验，请输入邮箱对应的登录账号" data-input-icon="fa-user"/>
                </div>
            </div>
            <div class="form-group">
                <div class="controls controls-clearfix">
                    <input type="password" name="newpasswd" class="form-control" required="true"
                           placeholder="输入新密码" data-input-icon="fa-lock"/>
                </div>
            </div>
            <div class="form-group">
                <div class="controls controls-clearfix">
                    <input type="password" name="cfmpasswd" class="form-control" required="true"
                           data-rule-equalToByName="newpasswd" placeholder="确认新密码" data-input-icon="fa-lock"/>
                </div>
            </div>
        </div>
        <div class="form-actions right">
            <a class="btn default" href="admin/login">返回登录界面</a>
            <button class="btn green" type="submit">提交</button>
        </div>
        <script src="assets/pages/admin/scripts/password-reset.js"></script>
    </form>
</div>
</body>
</html>
