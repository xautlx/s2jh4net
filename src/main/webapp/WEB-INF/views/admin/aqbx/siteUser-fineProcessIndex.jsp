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
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-fine-process-index">
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
			<table id="grid-fine-process-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-fine-process-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/p2p/fine-process/list',
                colModel : [ {
                    label : '借款人',
                    name : 'siteUser.display',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/site-user/edit',
                        title : '用户详情',
                        idValue : 'siteUser.id'
                    },
                    align : 'center',
                    width : 100
                }, {
                    label : '上次处理时间',
                    name : 'lastFineProcessDate',
                    formatter : 'timestamp'
                }, {
                    label : '处理次数',
                    name : 'totalFineProcessQuantity',
                    formatter : 'integer'
                }, {
                    label : '当前逾期款项',
                    name : 'TODO',
                    formatter : 'number'
                }, {
                    label : '当前最高逾期时长',
                    name : 'TODO',
                    formatter : 'number'
                }, {
                    label : '逾期金额',
                    name : 'TODO',
                    formatter : 'number'
                }, {
                    label : '罚息合计',
                    name : 'totalFineInterestAmount',
                    formatter : 'number'
                }, {
                    label : '逾期管理费合计',
                    name : 'TODO',
                    formatter : 'number'
                }, {
                    label : '累计逾期次数',
                    name : 'totalFineInterestQuantity',
                    formatter : 'integer'
                } ],
                multiselect : false,
                fullediturl : WEB_ROOT + '/admin/p2p/fine-process/edit',
                addable : false
            });
        });
    </script>
</body>
</html>