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
				data-grid-search="#grid-aqbx-partner-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_name']" class="form-control input-large" placeholder="名称">
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
			<table id="grid-aqbx-partner-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aqbx-partner-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aqbx/partner/list',
		        colModel : [ {
		            label : '商铺编号',
		            name : 'id',
		            width : 64
		        }, {
		            label : '商铺名称',
		            name : 'name',
		            width : 100,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '商铺地址',
		            name : 'address',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '联系人姓名',
		            name : 'linkman',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '联系人电话',
		            name : 'phone',
		            width : 200,
		            editable: true,
		            align : 'left'
		        }, {
		            label : '状态',
		            name : 'cooperationState',
		            formatter : 'select',
                    searchoptions : {
                        valueJsonString : '${cooperationStateJson}'
                    },
		            width : 80,
		            editable: true,
		            align : 'center'
		        }, {
		            label : '签约日期',
		            name : 'dealDate',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        }, {
		            label : '注册日期',
		            name : 'createDate',
		            width : 150,
		            formatter: 'timestamp',
		            editable: true,
		            align : 'center'
		        } ],
		        postData: {
		           "search['FETCH_siteUser']" : "INNER"
		        },
		        editurl : WEB_ROOT + '/admin/aqbx/partner/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aqbx/partner/delete',
		        fullediturl : WEB_ROOT + '/admin/aqbx/partner/edit-tabs'
		    });
		});
    </script>
</body>
</html>
