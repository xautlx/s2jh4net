<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>借款列表</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-schedule-job-run-hist-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_jobName_OR_nodeId']" class="form-control input-large" placeholder="任务名称、主机...">
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
			<table id="grid-schedule-job-run-hist-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-schedule-job-run-hist-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/schedule/job-run-hist/list',
                colNames : [ '流水号', '主机节点', '任务名称', '触发时间', '下次触发时间', '触发次数', '异常标识', '执行结果', '创建时间', '版本号' ],
                colModel : [ {
                    name : 'id',
                    align : 'center',
                    width : 50
                }, {
                    name : 'nodeId',
                    width : 150,
                    align : 'center'
                }, {
                    name : 'jobName',
                    width : 200,
                    align : 'left'
                }, {
                    name : 'previousFireTime',
                    fixed : true,
                    sorttype : 'date',
                    align : 'center'
                }, {
                    name : 'nextFireTime',
                    fixed : true,
                    sorttype : 'date',
                    align : 'center'
                }, {
                    name : 'refireCount',
                    width : 60,
                    fixed : true,
                    hidden : true,
                    align : 'right'
                }, {
                    name : 'exceptionFlag',
                    fixed : true,
                    formatter : 'checkbox',
                    align : 'center'
                }, {
                    name : 'result',
                    width : 100,
                    align : 'left'
                }, {
                    name : 'createdDate',
                    width : 120,
                    fixed : true,
                    hidden : true,
                    align : 'center'
                }, {
                    name : 'version',
                    hidden : true,
                    hidedlg : true
                } ],
                multiselect : false,
                viewurl : WEB_ROOT + '/admin/schedule/job-run-hist/view',
                addable : false
            });
        });
    </script>
</body>
</html>