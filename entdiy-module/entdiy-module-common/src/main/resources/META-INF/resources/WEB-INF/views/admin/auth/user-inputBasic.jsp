<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/auth/user/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row" data-equal-height="false">
            <div class="col-md-12">
                <c:choose>
                    <c:when test="${entity.isNew()}">
                        <form:hidden path="account.authType" value="admin"/>
                        <div class="form-group">
                            <label class="control-label">登录账号</label>
                            <div class="controls">
                                <form:input path="account.authUid" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">数据域</label>
                            <div class="controls">
                                <form:input path="account.dataDomain" class="form-control"/>
                                <span class="help-block">用于数据访问控制，请仔细填写或保留默认值，创建之后不可变更</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">设置密码</label>
                            <div class="controls">
                                <input type="password" class="form-control" name="rawPassword"
                                       data-rule-minlength="3"/>
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
                                <form:input path="account.email" class="form-control"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">移动电话</label>
                            <div class="controls">
                                <form:input path="account.mobile" class="form-control"/>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-group">
                            <label class="control-label">登录账号</label>
                            <div class="controls">
                                <p class="form-control-static">${entity.account.authUid}
                                    <a title="登录账号管理" class="btn btn-xs" data-toggle="modal-ajaxify"
                                       href="javascript:;" data-url="admin/auth/account/edit-tabs?id=${entity.account.id}">
                                        登录账号管理
                                    </a>
                                </p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">数据域</label>
                            <div class="controls">
                                <p class="form-control-static">${entity.account.dataDomain}</p>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="form-group">
                    <label class="control-label">姓名</label>
                    <div class="controls">
                        <form:input path="trueName" class="form-control"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">所属部门</label>
                    <div class="controls">
                        <form:select path="department.id" class="form-control"
                                     data-toggle="dropdown-tree"
                                     data-url="/admin/auth/department/tree"
                                     data-fetch-all="true"
                                     data-only-child-select="false"
                                     multiple="false">
                            <form:option value="${entity.department.id}" label="${entity.department.display}"/>
                        </form:select>
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