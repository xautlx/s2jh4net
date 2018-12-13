<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/auth/account/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row" data-equal-height="false">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">登录账号</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.authUid}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">设置密码</label>
                    <div class="controls">
                        <input type="password" class="form-control" name="rawPassword"
                               data-rule-minlength="3" placeholder="留空表示不变更密码"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">确认密码</label>
                    <div class="controls">
                        <input type="password" class="form-control" name="cfmpassword" autocomplete="off"
                               data-rule-equalToByName="rawPassword" data-rule-minlength="3" placeholder="留空表示不变更密码"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">昵称</label>
                    <div class="controls">
                        <form:input path="nickname" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">电子邮件</label>
                    <div class="controls">
                        <form:input path="email" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">移动电话</label>
                    <div class="controls">
                        <form:input path="mobile" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">启用状态</label>
                    <div class="controls">
                        <form:radiobuttons path="accountNonLocked"
                                           items="${applicationScope.cons.booleanLabelMap}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">账号失效日期</label>
                    <div class="controls">
                        <form:input path="accountExpireDate" class="form-control" data-picker="date"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">密码失效日期</label>
                    <div class="controls">
                        <form:input path="credentialsExpireDate" class="form-control" data-picker="date"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">注册时间</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.signupTimeFormatted}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>
