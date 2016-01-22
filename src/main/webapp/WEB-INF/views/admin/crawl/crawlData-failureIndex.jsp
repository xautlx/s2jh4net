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
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-crawl-data-failure-data-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_URL']" class="form-control input-large" placeholder="URL...">
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
			<table id="grid-crawl-data-failure-data-index"></table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#grid-crawl-data-failure-data-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/crawl/crawl-data/failure-data-list',
                colModel : [ {
                    label : 'URL',
                    name : 'URL',
                    width : 150,
                    align : 'left'
                }, {
                    label : '抓取失败次数',
                    name : 'fetchFailureTimes',
                    formatter : 'integer'
                }, {
                    label : '最后执行抓取时间',
                    name : 'fetchTouchTime',
                    formatter : 'timestamp'                    
                }, {
                    label : '最后成功抓取时间',
                    name : 'fetchTime',
                    formatter : 'timestamp'
                }, {
                    label : '抓取响应状态码',
                    name : 'httpStatus',
                    formatter : 'integer'
                }, {
                    label : '解析失败次数',
                    name : 'parseFailureTimes',
                    formatter : 'integer'
                }, {
                    label : '解析时间',
                    name : 'parseTime',
                    formatter : 'timestamp'
                } ],
                multiselect : false,
                addable : false,
                fullediturl : WEB_ROOT + '/admin/crawl/crawl-data/failure-detail'
            });
        });
    </script>
</body>
</html>
