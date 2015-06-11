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
				data-grid-search="#grid-aud-revision-entity-user-index">
				<input type="hidden" name="search['NN_authUid']" value="true" />
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_controllerClassLabel_OR_controllerMethodLabel_OR_authUid']"
							class="form-control input-xlarge" placeholder="处理类 , 处理方法 , 操作账号标识...">
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
			<table id="grid-aud-revision-entity-user-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-aud-revision-entity-user-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aud/revision-entity/user/list',
                colModel : [ {
                    label : '版本号',
                    name : 'rev',
                    width : 60,
                    align : 'center'
                }, {
                    label : '记录时间',
                    name : 'revstmp',
                    formatter : 'timestamp'
                }, {
                    label : '处理类',
                    name : 'controllerClassLabel',
                    width : 100,
                    align : 'center'
                }, {
                    label : '处理方法',
                    name : 'controllerMethodLabel',
                    width : 100,
                    align : 'center'
                }, {
                    label : '操作账号标识',
                    name : 'authUid',
                    align : 'center',
                    width : 80
                }, {
                    label : '操作账号类型',
                    name : 'authType',
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${authTypeMap}"/>'
                    },
                    align : 'center',
                    width : 60
                }, {
                    label : '类名称',
                    name : 'controllerClassName',
                    width : 200,
                    align : 'left'
                }, {
                    label : '方法名称',
                    name : 'controllerMethodName',
                    width : 100,
                    align : 'center'
                }, {
                    label : '方法类型',
                    name : 'controllerMethodType',
                    width : 60,
                    align : 'center'
                }, {
                    label : 'entityClassName',
                    name : 'entityClassName',
                    width : 60,
                    hidden : true,
                    align : 'center'
                } ],
                sortname : 'rev',
                operations : function(itemArray) {
                    var $select = $('<li data-position="single" data-toolbar="show"><a href="javascript:;"><i class="fa fa-circle-o"></i> 单数据查看</a></li>');
                    $select.children("a").bind("click", function(e) {
                        e.preventDefault();
                        var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                        var selectedRows = $grid.jqGrid('getGridParam', 'selarrrow');
                        if (selectedRows.length != 1) {
                            alert("请选取单个行项");
                            return false;
                        }

                        var rowdata = $grid.jqGrid("getRowData", selectedRows);
                        var url = WEB_ROOT + '/admin/aud/revision-entity/compare';
                        url += "?clazz=" + rowdata['entityClassName'];
                        url += "&revs=" + rowdata['rev'];

                        $select.popupDialog({
                            url : url,
                            title : "历史版本数据查看"
                        })
                    });
                    itemArray.push($select);

                    var $revisionsComparet = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-indent"></i> 双数据对比</a></li>');
                    $revisionsComparet.children("a").bind("click", function(e) {
                        e.preventDefault();
                        var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                        var selectedRows = $grid.jqGrid('getGridParam', 'selarrrow');
                        if (selectedRows.length != 2) {
                            alert("请选取两个比较行项");
                            return false;
                        }
                        var row0 = $grid.getRowData(selectedRows[0])['revisionEntity.rev'];
                        var row1 = $grid.getRowData(selectedRows[1])['revisionEntity.rev'];
                        var revLeft, revRight;
                        if (row0 > row1) {
                            revLeft = row1;
                            revRight = row0;
                        } else {
                            revLeft = row0;
                            revRight = row1;
                        }
                        var url = WEB_ROOT + '<s:property value="#request.revisionComparePath"/>';
                        url += "?id=<s:property value='#parameters.id'/>";
                        url += "&revLeft=" + revLeft;
                        url += "&revRight=" + revRight;

                        $select.popupDialog({
                            url : url,
                            title : "历史版本数据对比"
                        })
                    });
                    itemArray.push($revisionsComparet);
                }
            });
        });
    </script>
</body>
</html>
