<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运行流程实例</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-bpm-process-instance-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="businessKey" class="form-control input-large" placeholder="业务编码">
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="processDefinitionKey" class="form-control input-large" placeholder="流程编码">
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
			<table id="grid-bpm-process-instance-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-bpm-process-instance-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/bpm/process-instance/list',
                colModel : [ {
                    label : '流程实例序号',
                    name : 'executionEntityId',
                    align : 'center',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/bpm/process-instance/view',
                        title : '流程实例详情'
                    },
                    width : 80
                }, {
                    label : '流程发起人',
                    name : 'startUserId',
                    align : 'center',
                    width : 80
                }, {
                    label : '业务编码',
                    name : 'businessKey',
                    width : 150
                }, {
                    label : '流程编码',
                    name : 'processDefinitionKey',
                    width : 150
                }, {
                    label : '流程名称',
                    name : 'processDefinitionName',
                    width : 150
                }, {
                    label : '当前活动',
                    name : 'activityNames',
                    width : 150
                } ],
                filterToolbar : false,
                cmTemplate : {
                    sortable : false
                },
                operations : function(items) {
                    var $grid = $(this);
                    var $select = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;"><i class="fa fa-trash-o"></i> 强制结束流程实例</a></li>');
                    $select.children("a").bind("click", function(e) {
                        var ids = $grid.getAtLeastOneSelectedItem();
                        if (ids) {
                            $grid.ajaxPostURL({
                                url : WEB_ROOT + "/bpm/process-instance!forceTerminal",
                                success : function(response) {
                                    $.each(ids, function(i, item) {
                                        var item = $.trim(item);
                                        var $tr = $grid.find("tr.jqgrow[id='" + item + "']");
                                        if (response.data && response.data[item]) {
                                            var msg = response.data[item];
                                            $tr.pulsate({
                                                color : "#bf1c56",
                                                repeat : 3
                                            });
                                        } else {
                                            $grid.jqGrid("delRowData", item);
                                        }
                                    });
                                },
                                confirmMsg : "确认强制结束流程实例吗？",
                                data : {
                                    ids : ids.join(",")
                                }
                            })
                        }
                    });
                    items.push($select);
                }
            });
        });
    </script>
</body>
</html>