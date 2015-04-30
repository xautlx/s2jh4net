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
				data-grid-search="#grid-aqbx-order-contract-index">
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
			<table id="grid-aqbx-order-contract-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-order-contract-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/order-contract/list',
		        colModel : [ {
		            label : '协议编号',
		            name : 'id'                          
		        }, {
		            label : '保单编号',
		            name : 'insuranceNo',
		            width : 64,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '订单编号',
		            name : 'order.id',
		            index : 'order',
		            width : 200,
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '供货商名称',
		            name : 'order.supplierName',
		            index : 'order',
		            width : 200,
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '收货人姓名',
		            name : 'order.consignee',
		            index : 'order',
		            width : 200,
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '收货人联系方式',
		            name : 'order.consigneeTel',
		            index : 'order',
		            width : 200,
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '协议状态',
		            name : 'contractState',
		            formatter : 'select',
		            searchoptions : {
		            	valueJsonString : '${contractStateJson}'
		            },
		            width : 80,
		            editable: true,
		            align : 'center'
		        } ],
		        postData: {
		           "search['FETCH_order']" : "INNER"
		        },
		        editurl : WEB_ROOT + '/admin/aqbx/order-contract/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/order-contract/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/order-contract/edit-tabs'
		    });
		});
    </script>
</body>
</html>
