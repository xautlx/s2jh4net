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
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-auth-department-index">
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
			<table id="grid-auth-department-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-auth-department-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/auth/department/list',
                colModel : [ {
                    label : '名称',
                    name : 'name',
                    width : 100,
                    editable : true,
                    align : 'left'
                }, {
                    label : '代码',
                    name : 'code',
                    width : 100,
                    editable : true,
                    editoptions : {
                        placeholder : '下级节点代码必须以父节点代码作为前缀',
                    },
                    align : 'left'
                }, {
                    label : '层次路径',
                    name : 'pathDisplay',
                    width : 200
                } ],
                multiselect : false,
                subGrid : true,
                gridDnD : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
                },
                editurl : WEB_ROOT + '/admin/auth/department/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                delurl : WEB_ROOT + '/admin/auth/department/delete'
            });
        });
    </script>
</body>
</html>