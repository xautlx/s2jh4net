<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-site-user-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_code_OR_name']" class="form-control input-large" placeholder="代码，名称...">
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
			<table id="grid-site-user-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-site-user-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/p2p/site-user/list',
                colModel : [ {
                    label : '用户ID',
                    name : 'id',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/site-user/edit',
                        title : '用户详情'
                    },
                    width : 100
                },{
                    label : '昵称',
                    name : 'user.nickName',
                    width : 100,
                    align : 'center',
                    editable : true
                }, {
                    label : '真实姓名',
                    name : 'trueName',
                    width : 100,
                    align : 'center',
                    editable : true
                }, {
                    label : '用户类型',
                    name : 'userType',
                    editable : true,
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '${userTypeJson}'
                    },
                    align : 'center',
                    width : 120
                },{
                    label : '信用分数',
                    name : 'creditScore',
                    width : 100,
                    align : 'center',
                    editable : true
                }, {
                    label : '信用等级',
                    name : 'creditLevel',
                    align : 'center',
                    width : 120
                },{
                    label : '信用额度',
                    name : 'creditTotalAmount',
                    formatter : 'currency',
                    width : 100,
                    editable : true
                },{
                    label : '信用余额',
                    name : 'TODO',
                    formatter : 'currency',
                    width : 100,
                    editable : true
                },{
                    label : '债权总额',
                    name : 'TODO',
                    formatter : 'currency',
                    width : 100,
                    editable : true
                },{
                    label : '待还本金额',
                    name : 'TODO',
                    formatter : 'currency',
                    width : 100,
                    editable : true
                },{
                    label : '逾期次数',
                    name : 'TODO',
                    formatter : 'currency',
                    width : 100,
                    editable : true
                },{
                    label : '注册时间',
                    name : 'user.signupTime',
                    formatter : 'timestamp',
                    width : 100,
                    editable : true
                } ],
                multiselect : false
            });
        });
    </script>
</body>
</html>