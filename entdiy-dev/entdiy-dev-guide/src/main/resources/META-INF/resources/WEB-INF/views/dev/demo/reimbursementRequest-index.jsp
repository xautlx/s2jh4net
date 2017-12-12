<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-demo-reimbursement-request-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_']" class="form-control input-xlarge" placeholder="...">
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
	<div class="row">
		<div class="col-md-12">
			<table id="grid-demo-reimbursement-request-index"></table>
		</div>
	</div>

	<script type="text/javascript">
		$(function() {
		    $("#grid-demo-reimbursement-request-index").data("gridOptions", {
		        url : '/dev/demo/reimbursement-request/list',
		        colModel : [ {
		            label : '流水号',
		            name : 'id',
		            hidden : true
		        }, {
		            label : '登录账号对象',
		            name : 'user',
		            width : 200,
		            align : 'center',
		            editable: true
		        }, {
		            label : '挂账部门',
		            name : 'department',
		            width : 200,
		            align : 'center',
		            editable: true
		        }, {
		            label : '提交时间',
		            name : 'submitTime',
		            width : 150,
		            align : 'center',
		            editable: true
		        } ],
		        postData: {
		           "search['FETCH_user']" : "INNER",
		           "search['FETCH_department']" : "INNER"
		        },
		        editurl : '/dev/demo/reimbursement-request/edit',
		        editrulesurl : '/admin/util/validate?clazz=${clazz}',
		        delurl : '/dev/demo/reimbursement-request/delete',
		        fullediturl : '/dev/demo/reimbursement-request/edit-tabs'
		    });
		});
    </script>
</body>
</html>
