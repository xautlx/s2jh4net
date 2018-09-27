<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="account" data-validation='${validationRules}'
           action="admin/profile/edit">
    <form:hidden path="id" disabled="true"/>
    <div class="form-actions">
        <button class="btn blue" type="submit"><i class="fa fa-check"></i> 保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="form-group">
            <label class="control-label">登录账号</label>
            <div class="controls">
                <p class="form-control-static">${account.display}</p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">移动电话</label>
            <div class="controls">
                <form:input path="mobile" class="form-control" data-rule-mobile="true"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">电子邮件</label>
            <div class="controls">
                <form:input path="email" class="form-control"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">注册时间</label>
            <div class="controls">
                <p class="form-control-static">
                        ${account.signupTimeFormatted}
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">密码到期日期</label>
            <div class="controls">
                <p class="form-control-static">
                        ${account.accountExpireDateFormatted}
                    <a title="修改密码" class="btn btn-xs" data-width="500px" data-toggle="modal-ajaxify"
                       href="javascript:;" data-url="admin/profile/password">修改密码</a>
                    <span class="help-block">出于安全考虑系统要求用户在设定的固定周期后更新密码，密码到期之前一段时间会提醒用户设置新密码</span>
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">最近登录时间</label>
            <div class="controls">
                <p class="form-control-static">
                        ${account.lastLogonSuccessTimeFormatted}
                </p>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">微信绑定</label>
            <div class="controls">
                <p class="form-control-static">
                <ul>
                    <c:forEach items="${oauthAccounts}" var="oauthAccount">
                        <li>${oauthAccount.bindTime} 已绑定 ${oauthAccount.nickname}
                            <button class="btn blue btn-post-url" type="button"
                                    data-url="wx/admin-unbind?openid=${oauthAccount.oauthOpenId}" data-confirm="确认解除微信绑定？">解绑
                            </button>
                        </li>
                    </c:forEach>
                </ul>
                <a target="_blank" class="btn blue" href="wx/goto-admin-bind">添加绑定当前微信</a>
                </p>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn blue" type="submit"><i class="fa fa-check"></i> 保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>