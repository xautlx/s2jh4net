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
				data-grid-search="#grid-aqbx-supplier-index">
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
			<table id="grid-aqbx-supplier-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-supplier-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/supplier/list',
		        colModel : [ {
		            label : '工商编号',
		            name : 'id',
		            hidden : true                          
		        }, {
		            label : '供商名称',
		            name : 'name',
		            width : 255,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '供商Logo',
		            name : 'logo',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '供商官网',
		            name : 'webSite',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '供商联系人',
		            name : 'contacter',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '供商联系人电话',
		            name : 'contacterTel',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '客服QQ',
		            name : 'ustomerServiceQQ',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '客服电话',
		            name : 'customerServiceTel',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '客服邮箱',
		            name : 'email',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '备注',
		            name : 'note',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '操作员',
		            name : 'createUser.nickName',
		            width : 60,
		            editable: true,
		            align : 'right'
		        } ],
		        editurl : WEB_ROOT + '/admin/aqbx/supplier/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/supplier/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/supplier/edit-tabs'
		    });
		});
    </script>
</body>
</html>
