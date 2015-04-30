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
				data-grid-search="#grid-aqbx-qrcode-batch-index">
				<div class="form-group">
					
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
			<table id="grid-aqbx-qrcode-batch-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-qrcode-batch-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/qrcode-batch/list',
		        colModel : [ {
		            label : '批次编号',
		            name : 'id'                         
		        }, {
		            label : '计划打印日期',
		            name : 'planPrintDate',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '实际打印日期',
		            name : 'finalPrintDate',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '创建二维码总数',
		            name : 'totalCount',
		            width : 60,
		            editable: true,
		            align : 'right'
		        }, {
		            label : '备注',
		            name : 'note',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }],
		        postData: {
		           "search['FETCH_createUser']" : "INNER"
		        },
		        editurl : WEB_ROOT + '/admin/aqbx/qrcode-batch/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/qrcode-batch/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/qrcode-batch/edit-tabs'
		    });
		});
    </script>
</body>
</html>
