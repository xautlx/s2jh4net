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
				data-grid-search="#grid-aud-logging-event-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_loggerName_OR_formattedMessage']" class="form-control input-large"
							placeholder="日志名称 , 消息内容...">
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
			<table id="grid-aud-logging-event-index"></table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#grid-aud-logging-event-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aud/logging-event/list',
                colModel : [ {
                    label : '流水号',
                    name : 'id',
                    hidden : true
                }, {
                    label : '时间',
                    name : 'timestampDate',
                    index : 'timestmp',
                    formatter : 'timestamp',
                    editable : true
                }, {
                    label : '日志名称',
                    name : 'loggerName',
                    width : 200,
                    align : 'center',
                    editable : true
                }, {
                    label : '等级',
                    name : 'levelString',
                    width : 50,
                    align : 'center',
                    editable : true
                }, {
                    label : '日志信息',
                    name : 'formattedMessage',
                    width : 300,
                    align : 'left',
                    editable : true
                }, {
                    label : '触发类',
                    name : 'callerClass',
                    width : 300,
                    align : 'left',
                    editable : true
                }, {
                    label : '触发方法',
                    name : 'callerMethod',
                    width : 150,
                    align : 'center',
                    editable : true
                }, {
                    label : '触发行数',
                    name : 'callerLine',
                    width : 40,
                    align : 'center',
                    editable : true
                }, {
                    label : '状态',
                    name : 'state',
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${stateMap}"/>'
                    },
                    width : 80,
                    align : 'center',
                    editable : true
                } ],
                addable : false,
                fullediturl : WEB_ROOT + '/admin/aud/logging-event/edit'
            });
        });
    </script>
</body>
</html>
