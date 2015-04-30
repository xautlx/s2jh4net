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
				data-grid-search="#grid-aqbx-partner-resource-index">
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
			<table id="grid-aqbx-partner-resource-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-partner-resource-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/partner-resource/list',
		        colModel : [ {
		            label : '流水号',
		            name : 'id',
		            hidden : true                          
		        }, 
		        {
		            label : '商户名称',
		            name : 'name',
		            width : 60,
		            editable: false,
		            align : 'left'
		        },
		        {
		            label : '所在城市',
		            name : 'city',
		            width : 30,
		            editable: false,
		            align : 'center'
		        }
		        ,
		        {
		            label : '百度返回UID',
		            name : 'uid',
		            width : 60,
		            editable: false,
		            align : 'left'
		        }, {
		            label : '搜索关键字',
		            name : 'keyword',
		            width : 50,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '位置：经度坐标',
		            name : 'longitude',
		            width : 30,
		            editable: true,
		            align : 'right'
		        }, {
		            label : '位置：纬度坐标',
		            name : 'latitude',
		            width : 30,
		            editable: true,
		            align : 'right'
		        } ],
		        editurl : WEB_ROOT + '/admin//aqbx/partner-resource/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/partner-resource/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/partner-resource/edit-tabs'
		    });
		});
    </script>
</body>
</html>
