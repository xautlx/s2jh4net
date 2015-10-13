<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户账号</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-auth-user-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_authUid_OR_nickName_OR_email_OR_mobile']" class="form-control input-large"
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
	<div class="row">
		<div class="col-md-12">
			<table id="grid-auth-user-index"></table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#grid-auth-user-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/auth/user/list',
                colModel : [ {
                    label : '账号标识',
                    name : 'authUid',
                    editable : true,
                    editoptions : {
                        updatable : false
                    },
                    align : 'center',
                    width : 120
                }, {
                    label : '账号类型',
                    name : 'authType',
                    editable : true,
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${authTypeMap}"/>'
                    },
                    align : 'center',
                    width : 120
                }, {
                    label : '昵称',
                    name : 'nickName',
                    editable : true,
                    width : 120
                }, {
                    label : '电子邮件',
                    name : 'email',
                    editable : true,
                    width : 200
                }, {
                    label : '移动电话',
                    name : 'mobile',
                    editable : true,
                    width : 100
                }, {
                    label : '管理授权',
                    name : 'mgmtGranted',
                    editable : true,
                    formatter : "checkbox"
                }, {
                    label : '启用',
                    name : 'accountNonLocked',
                    editable : true,
                    formatter : "checkbox"
                }, {
                    label : '账号失效日期',
                    name : 'accountExpireTime',
                    editable : true,
                    sorttype : 'date'
                }, {
                    label : '密码失效日期',
                    name : 'credentialsExpireTime',
                    editable : true,
                    sorttype : 'date'
                }, {
                    name : 'department.id',
                    hidden : true,
                    hidedlg : true,
                    editable : true
                }, {
                    label : '所属部门',
                    name : 'department.pathDisplay',
                    index : 'department.code_OR_department.title',
                    width : 120,
                    sortable : false,
                    hidden : true,
                    editable : true,
                    align : 'left',
                    editoptions : {
                        dataInit : function(elem, opt) {
                            $(elem).treeSelect({
                                url : WEB_ROOT + "/admin/auth/department/select"
                            });
                        }
                    }
                }, {
                    label : '注册时间',
                    name : 'userExt.signupTime',
                    sorttype : 'date'
                }, {
                    label : '最后登录时间',
                    name : 'userExt.lastLogonTime',
                    sorttype : 'date'
                }, {
                    label : '总计登录次数',
                    name : 'userExt.logonTimes',
                    align : 'center',
                    width : 80
                }, {
                    label : '最近认证失败时间',
                    name : 'userExt.lastLogonFailureTime',
                    sorttype : 'date'
                }, {
                    label : '最近认证失败次数',
                    name : 'logonFailureTimes',
                    align : 'center',
                    width : 80
                } ],
                editurl : WEB_ROOT + '/admin/auth/user/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                fullediturl : WEB_ROOT + '/admin/auth/user/edit-tabs',
                delurl : WEB_ROOT + '/admin/auth/user/delete'
            });
        });
    </script>
</body>
</html>