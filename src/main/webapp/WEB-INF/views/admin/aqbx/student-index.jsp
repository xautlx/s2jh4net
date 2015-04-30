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
				data-grid-search="#grid-aqbx-student-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_name']" class="form-control input-large" placeholder="代码，名称...">
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
			<table id="grid-aqbx-student-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-student-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/student/list',
		        colModel : [ {
		            label : '学生编号',
		            name : 'id',
		            hidden : true                          
		        }, {
		            label : '姓名',
		            name : 'trueName',
		            width : 255,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '性别',
		            name : 'sex',
		            width : 60,
		            formatter : 'select',
                    searchoptions : {
                        valueJsonString : '${sexJson}'
                    },
		            editable: true,
		            align : 'right'
		        }, {
		            label : '所属学校',
		            name : 'schoolName',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '所属专业',
		            name : 'majorName',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '身份证',
		            name : 'idCardNo',
		            width : 255,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '学历',
		            name : 'educationLevel',
		            width : 60,
		            formatter : 'select',
                    searchoptions : {
                        valueJsonString : '${educationLevelJson}'
                    },
		            editable: true,
		            align : 'right'
		        }, {
		            label : '手机号码',
		            name : 'mobile',
		            width : 255,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '电子邮箱',
		            name : 'email',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }],
		        postData: {
		           "search['FETCH_studentExt']" : "LEFT"
		        },
		        editurl : WEB_ROOT + '/admin/aqbx/student/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/student/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/student/edit-tabs'
		    });
		});
    </script>
</body>
</html>
