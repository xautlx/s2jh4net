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
                            <input type="text" name="search[CN_trueName_OR_account.authUid]"
                                   class="form-control input-large"
                                   placeholder="姓名，登录账号...">
                        </div>
                    </div>
                    <div class="form-group search-group-btn">
                        <button class="btn green" type="submit">
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
                    url: "/admin/auth/user/list",
                    colModel: [{
                        label: "数据域",
                        name: "account.dataDomain",
                        editable: false,
                        align: "center",
                        width: 120
                    }, {
                        label: "登录账号",
                        name: "account.authUid",
                        editable: false,
                        align: "center",
                        width: 120
                    }, {
                        label: "姓名",
                        name: "trueName",
                        editable: true,
                        width: 120
                    }, {
                        label: "所属部门",
                        name: "department.display",
                        index: "department.code_OR_department.name",
                        width: 120,
                        editable: false
                    }, {
                        label: "电子邮件",
                        name: "account.email",
                        editable: false,
                        width: 200
                    }, {
                        label: "移动电话",
                        name: "account.mobile",
                        editable: false,
                        width: 100
                    }, {
                        label: "注册时间",
                        name: "account.signupTime",
                        formatter: "timestamp"
                    }],
                    editurl: "/admin/auth/user/edit",
                    fullediturl: "/admin/auth/user/edit-tabs",
                    delurl: "/admin/auth/user/delete"
                }'>
            </table>
        </div>
    </div>
</div>