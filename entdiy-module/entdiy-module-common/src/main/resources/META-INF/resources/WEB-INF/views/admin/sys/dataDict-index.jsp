<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据字典配置</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-sys-data-dict-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_name']" class="form-control input-large" placeholder="名称...">
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
			<table id="grid-sys-data-dict-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-sys-data-dict-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/sys/data-dict/list',
                colModel : [ {
                    label : '主要数据',
                    name : 'primaryValue',
                    editable : true,
                    editoptions : {
                        spellto : 'primaryKey'
                    },
                    width : 150
                }, {
                    label : '主标识',
                    name : 'primaryKey',
                    editable : true,
                    width : 100
                }, {
                    label : '次标识',
                    name : 'secondaryKey',
                    editable : true,
                    width : 50
                }, {
                    label : '次要数据',
                    name : 'secondaryValue',
                    hidden : true,
                    editable : true,
                    width : 50
                }, {
                    label : '禁用',
                    name : 'disabled',
                    editable : true,
                    formatter : "checkbox"
                }, {
                    label : '排序号',
                    name : 'orderRank',
                    width : 60,
                    editable : true,
                    editoptions : {
                        defaultValue : 100
                    },
                    formatter : 'integer'
                }, {
                    label : '大文本数据',
                    name : 'richTextValue',
                    width : 200,
                    hidden : true,
                    editable : true,
                    edittype : 'textarea'
                } ],
                sortorder : "desc",
                sortname : 'orderRank',
                multiselect : false,
                subGrid : true,
                gridDnD : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
                },
                editurl : WEB_ROOT + '/admin/sys/data-dict/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                fullediturl : WEB_ROOT + "/admin/sys/data-dict/edit",
                delurl : WEB_ROOT + '/admin/sys/data-dict/delete'
            });
        });
    </script>
</body>
</html>