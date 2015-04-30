<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-aqbx-assignment-index">
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
			<table id="grid-aqbx-assignment-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-assignment-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/assignment/list',
		        colModel : [ {
		            label : '任务编号',
		            name : 'id'                         
		        }, {
		            label : '任务名称',
		            name : 'name',
		            width : 255,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '报名开始时间',
		            name : 'startApplyTime',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '报名结束时间',
		            name : 'endApplyTime',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '任务开始时间',
		            name : 'startTime',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '任务结束时间',
		            name : 'endTime',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '创建时间',
		            name : 'createTime',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        } ],
		        postData: {
		           "search['FETCH_createUser']" : "INNER"
		        },
		        editurl : WEB_ROOT + '/admin/aqbx/assignment/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/assignment/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/assignment/edit-tabs'
		    });
		});
    </script>
</body>
</html>
