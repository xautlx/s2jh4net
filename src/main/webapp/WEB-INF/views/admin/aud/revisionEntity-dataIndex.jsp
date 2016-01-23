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
			<form method="get" class="form-inline form-validation form-search control-label-sm"
				data-grid-search="#grid-aud-data-revision-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<select name="clazz" class="form-control input-large" required="true" placeholder="记录数据对象"
							data-cascade-name="property" data-cascade-url="${ctx}/admin/aud/revision-entity/properties">
							<c:forEach items="${clazzMapping}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="id" class="form-control input-small" required="true" placeholder="数据主键">
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<select name="property" class="form-control input-medium" placeholder="选取变更属性">
						</select>
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<select name="changed" class="form-control input-medium" placeholder="选取变更状态">
							<option value="true">有变更</option>
							<option value="false">无变更</option>
						</select>
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
			<table id="grid-aud-data-revision-index"></table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#grid-aud-data-revision-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aud/revision-entity/data/list',
                colModel : [ {
                    label : '版本号',
                    name : 'revisionEntity.rev',
                    width : 60,
                    align : 'center'
                }, {
                    label : '记录时间',
                    name : 'revisionEntity.revstmp',
                    formatter : 'timestamp'
                }, {
                    label : '处理类',
                    name : 'revisionEntity.controllerClassDisplay',
                    index : 'revisionEntity.controllerClassName_OR_revisionEntity.controllerClassLabel',
                    width : 250,
                    align : 'left'
                }, {
                    label : '处理方法',
                    name : 'revisionEntity.controllerMethodDisplay',
                    index : 'revisionEntity.controllerMethodName_OR_revisionEntity.controllerMethodLabel',
                    width : 100,
                    align : 'center'
                }, {
                    label : '操作账号标识',
                    name : 'revisionEntity.authUid',
                    align : 'center',
                    width : 80
                }, {
                    label : '操作账号类型',
                    name : 'revisionEntity.authType',
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${authTypeMap}"/>'
                    },
                    hidden : true,
                    align : 'center',
                    width : 60
                }, {
                    label : '变更类型',
                    name : 'revisionType',
                    width : 60,
                    align : 'center'
                }, {
                    label : 'entityClassName',
                    name : 'revisionEntity.entityClassName',
                    width : 60,
                    hidden : true,
                    align : 'center'
                }, {
                    label : 'entityId',
                    name : 'revisionEntity.extraAttributes.entityId',
                    width : 60,
                    hidden : true,
                    align : 'center'
                } ],
                operations : function(itemArray) {
                    var $revisionsComparet = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-indent"></i> 数据查看/对比</a></li>');
                    $revisionsComparet.children("a").bind("click", function(e) {
                        e.preventDefault();
                        var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                        var selectRows = $grid.getAtLeastOneSelectedItem();
                        if (selectRows) {
                            var rowdatas = $grid.getSelectedRowdatas();
                            var url = WEB_ROOT + '/admin/aud/revision-entity/compare';

                            var entityClassName = null;
                            var revs = [];
                            $.each(rowdatas, function(i, rowdata) {
                                revs.push(rowdata['revisionEntity.rev']);
                                if (entityClassName == null) {
                                    entityClassName = rowdata['revisionEntity.entityClassName'];
                                } else {
                                    if (rowdata['revisionEntity.entityClassName'] != entityClassName) {
                                        entityClassName = false;
                                        return false;
                                    }
                                }
                            });

                            if (entityClassName) {
                                url += "?clazz=" + entityClassName;
                                url += "&entityId=" + rowdatas[0]['revisionEntity.extraAttributes.entityId'];
                                url += "&revs=" + revs.join(",");

                                $grid.popupDialog({
                                    url : url,
                                    title : "历史版本数据对比"
                                })
                            } else {
                                Global.notify("error", "选取行项数据类型不一致");
                            }
                        }
                    });
                    itemArray.push($revisionsComparet);
                }
            });
        });
    </script>
</body>
</html>
