<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init"
                      data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_authUid_OR_email_OR_mobile]"
                                   class="form-control input-large"
                                   placeholder="登录账号，昵称，电子邮件，移动电话...">
                        </div>
                    </div>
                    <div class="form-group search-group-btn">
                        <button class="btn green" type="submmit">
                            <i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
                        </button>
                        <button class="btn default" type="reset">
                            <i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="search-group-target"
                   grid-options='{
                   url: "/admin/auth/account/list",
                   colModel: [{
                       label: "账号类型",
                       name: "authType",
                       editable: false,
                       editoptions: {
                           updatable: false
                       },
                       formatter: "select",
                       "searchoptions": {
                           "valueJson": ${authTypeJson}
                       },
                       align: "center",
                       width: 80
                   }, {
                       label: "账号标识",
                       name: "authUid",
                       editable: false,
                       editoptions: {
                           updatable: false
                       },
                       align: "center",
                       width: 120
                   }, {
                       label: "电子邮件",
                       name: "email",
                       editable: true,
                       width: 200
                   }, {
                       label: "移动电话",
                       name: "mobile",
                       editable: true,
                       width: 100
                   }, {
                       label: "启用",
                       name: "accountNonLocked",
                       editable: true,
                       formatter: "checkbox"
                   }, {
                       label: "账号失效日期",
                       name: "accountExpireTime",
                       formatter: "date",
                       editable: true
                   }, {
                       label: "密码失效日期",
                       name: "credentialsExpireTime",
                       formatter: "date",
                       editable: true
                   }, {
                       label: "注册时间",
                       name: "signupTime",
                       formatter: "timestamp"
                   }, {
                       label: "最后登录时间",
                       name: "lastLogonTime",
                       formatter: "timestamp"
                   }, {
                       label: "总计登录次数",
                       name: "logonTimes",
                       formatter: "integer"
                   }, {
                       label: "最近认证失败时间",
                       name: "lastLogonFailureTime",
                       formatter: "timestamp",
                       hidden: true
                   }, {
                       label: "最近认证失败次数",
                       name: "logonFailureTimes",
                       formatter: "integer"
                   }],
                   addable: false,
                   multiselect: false,
                   editurl: "/admin/auth/account/edit",
                   editvalidation: ${validationRules},
                   fullediturl: "/admin/auth/account/edit-tabs"
                }'>
            </table>
        </div>
    </div>
</div>