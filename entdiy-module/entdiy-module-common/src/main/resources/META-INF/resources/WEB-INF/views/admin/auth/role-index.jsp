<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="search-group">
	<div class="search-group-form">
		<div class="row">
			<div class="col-md-12">
				<form method="get" class="form-inline form-search-init"
					  data-validation="true">
					<div class="form-group">
						<div class="controls controls-clearfix">
                            <input type="text" name="search[CN_code_OR_name]" class="form-control input-large" placeholder="代码，名称...">
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
	</div>
	<div class="row">
		<div class="col-md-12">
			<table class="search-group-target"
				   grid-options='{
                    url : "/admin/auth/role/list",
                    colModel : [ {
                    	label : "代码",
                    	name : "code",
                    	width : 100,
                    	editoptions : {
                    		defaultValue : "ROLE_"
                    	},
                    	editable : true
                    }, {
                    	label : "名称",
                    	name : "name",
                    	width : 100,
                    	editable : true
                    }, {
                    	label : "禁用",
                    	name : "disabled",
                    	width : 60,
                    	editable : true,
                    	formatter : "checkbox"
                    }, {
                    	label : "描述",
                    	name : "description",
                    	sortable : false,
                    	editable : true,
                    	width : 200,
                    	edittype : "textarea",
                    	align : "left"
                    } ],
                   editurl : "/admin/auth/role/edit",
                   fullediturl : "/admin/auth/role/edit-tabs",
                   delurl : "/admin/auth/role/delete",
                   editvalidation: ${validationRules}
                }'>
			</table>
		</div>
	</div>
</div>