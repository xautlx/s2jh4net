<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜单配置</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-sys-menu-index">
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
			<table id="grid-sys-menu-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-sys-menu-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/sys/menu/list',
                colModel : [ {
                    label : '名称',
                    name : 'name',
                    width : 150,
                    editable : true,
                    editoptions : {
                        spellto : 'code'
                    },
                    align : 'left'
                }, {
                    label : '图标',
                    name : 'style',
                    editable : true,
                    width : 80,
                    align : 'center',
                    formatter : function(cellValue, options, rowdata, action) {
                        if (cellValue) {
                            return '<i class="fa ' + cellValue + '" icon="' + cellValue + '"></i>';
                        } else {
                            return ''
                        }
                    },
                    unformat : function(cellValue, options, cell) {
                        return $('i', cell).attr('icon');
                    }
                }, {
                    label : '菜单URL',
                    name : 'url',
                    width : 200,
                    align : 'left'
                }, {
                    label : '展开',
                    name : 'initOpen',
                    editable : true,
                    formatter : "checkbox"
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
                        defaultValue : 1000
                    },
                    sorttype : 'number'
                }, {
                    label : '备注说明',
                    name : 'description',
                    width : 200,
                    hidden : true,
                    editable : true,
                    edittype : 'textarea'
                } ],
                multiselect : false,
                subGrid : true,
                gridDnD : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
                },
                editurl : WEB_ROOT + '/admin/sys/menu/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                delurl : WEB_ROOT + '/admin/sys/menu/delete'
            });
        });
    </script>
</body>
</html>