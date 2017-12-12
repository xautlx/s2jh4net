<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
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
				data-grid-search="#grid-demo-reimbursement-request-item-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_useType']" class="form-control input-xlarge" placeholder="报销类目...">
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
			<table id="grid-demo-reimbursement-request-item-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-demo-reimbursement-request-item-index").data("gridOptions", {
		        url : '/admin/demo/reimbursement-request-item/list',
		        colModel : [ {
		            label : '流水号',
		            name : 'id',
		            hidden : true                          
		        }, {
		            label : '报销申请主对象',
		            name : 'reimbursementRequest',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '费用发生起始日期',
		            name : 'startDate',
		            width : 150,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '费用发生结束日期',
		            name : 'endDate',
		            width : 150,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '报销类目',
		            name : 'useType',
		            width : 128,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '票据张数',
		            name : 'invoiceCount',
		            formatter: 'integer',
		            editable: true                                                                   
		        }, {
		            label : '票据小计金额',
		            name : 'invoiceAmount',
		            formatter: 'number',
		            editable: true                                                                   
		        } ],
		        postData: {
		           "search['FETCH_reimbursementRequest']" : "INNER"
		        },
		        editurl : '/admin//demo/reimbursement-request-item/edit',
		        editrulesurl : '/admin/util/validate?clazz=${clazz}',
		        delurl : '/admin/demo/reimbursement-request-item/delete',
		        fullediturl : '/admin/demo/reimbursement-request-item/edit-tabs'
		    });
		});
    </script>
</body>
</html>
