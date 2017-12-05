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
				data-grid-search="#grid-auth-signup-user-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_authUid_OR_trueNameOR_nickName_OR_email_OR_mobile']"
							class="form-control input-large" placeholder="登录账号，姓名，昵称，电子邮件...">
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<select name="search['NU_auditTime']" class="form-control input-small" placeholder="审核状态">
							<option value="true" selected="selected">待审核</option>
							<option value="false">已审核</option>
						</select>
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
			<table id="grid-auth-signup-user-index"></table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#grid-auth-signup-user-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/auth/signup-user/list',
                colModel : [ {
                    label : '登录账号',
                    name : 'authUid',
                    editable : true,
                    editoptions : {
                        updatable : false
                    },
                    align : 'center',
                    width : 120
                }, {
                    label : '姓名',
                    name : 'trueName',
                    editable : true,
                    align : 'center',
                    width : 120
                }, {
                    label : '昵称',
                    name : 'nickName',
                    editable : true,
                    align : 'center',
                    width : 120
                }, {
                    label : '电子邮件',
                    name : 'email',
                    editable : true,
                    align : 'center',
                    width : 200
                }, {
                    label : '移动电话',
                    name : 'mobile',
                    editable : true,
                    align : 'center',
                    width : 100
                }, {
                    label : '注册时间',
                    name : 'signupTime',
                    formatter : 'timestamp'
                }, {
                    label : '审核时间',
                    name : 'auditTime',
                    formatter : 'timestamp'
                } ],
                fullediturl : WEB_ROOT + '/admin/auth/signup-user/audit',
                delurl : WEB_ROOT + '/admin/auth/signup-user/delete'
            });
        });
    </script>
</body>
</html>