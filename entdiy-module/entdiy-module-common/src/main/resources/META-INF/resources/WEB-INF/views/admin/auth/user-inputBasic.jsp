<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户账号基本信息</title>
</head>
<body>
<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
           action="${ctx}/admin/auth/user/edit" method="post" modelAttribute="entity"
           data-editrulesurl="${ctx}/admin/util/validate?clazz=${clazz}">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn blue" type="submit" data-grid-reload="#grid-auth-user-index">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row" data-equal-height="false">
            <div class="col-md-5">
                <div class="form-group">
                    <label class="control-label">账号类型</label>
                    <div class="controls">
                        <p class="form-control-static">${authTypeMap[entity.authType]}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">登录账号</label>
                    <div class="controls">
                        <form:input path="authUid" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">账号昵称</label>
                    <div class="controls">
                        <form:input path="nickName" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">设置密码</label>
                    <div class="controls">
                        <input type="password" class="form-control" name="rawPassword" data-rule-minlength="3"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">确认密码</label>
                    <div class="controls">
                        <input type="password" class="form-control" name="cfmpassword" autocomplete="off"
                               data-rule-equalToByName="rawPassword" data-rule-minlength="3"/>
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
                    <label class="control-label">管理授权</label>
                    <div class="controls controls-radiobuttons">
                        <form:radiobuttons path="mgmtGranted" items="${applicationScope.cons.booleanLabelMap}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">启用状态</label>
                    <div class="controls controls-radiobuttons">
                        <form:radiobuttons path="accountNonLocked" items="${applicationScope.cons.booleanLabelMap}"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">账号失效日期</label>
                    <div class="controls">
                        <form:input path="accountExpireTime" class="form-control" data-picker="date"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">密码失效日期</label>
                    <div class="controls">
                        <form:input path="credentialsExpireTime" class="form-control" data-picker="date"/>
                    </div>
                </div>

            </div>
            <div class="col-md-7">
                <h3 class="form-section">关联角色</h3>
                <div class="form-group">
                    <form:select path="selectedRoleIds" items="${roles}" itemValue="id" itemLabel="name"
                                 class="form-control"
                                 data-toggle="double-multi-select" data-height="450px"
                                 data-selectable-header="可选角色列表" data-selection-header="已选角色列表"/>

                </div>
                <c:if test="${notNew}">
                    <div class="form-group">
                        <label class="control-label">注册时间</label>
                        <div class="controls">
                            <p class="form-control-static">${entity.signupTime}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">最后登录时间</label>
                        <div class="controls">
                            <p class="form-control-static">${entity.lastLogonTime}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">总计登录次数</label>
                        <div class="controls">
                            <p class="form-control-static">${entity.logonTimes}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">最近认证失败次数</label>
                        <div class="controls">
                            <p class="form-control-static">${entity.logonFailureTimes}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label">最近认证失败时间</label>
                        <div class="controls">
                            <p class="form-control-static">${entity.lastLogonFailureTime}</p>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn blue" type="submit" data-grid-reload="#grid-auth-user-index">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>
</body>
</html>
