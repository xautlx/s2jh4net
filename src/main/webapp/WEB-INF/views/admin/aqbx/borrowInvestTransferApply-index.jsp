<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>债权转让列表</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-borrow-invest-transfer-index">
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
			<table id="grid-borrow-invest-transfer-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-borrow-invest-transfer-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/p2p/borrow-invest-transfer/list',
                colModel : [ {
                    label : '转让ID',
                    name : 'id',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/borrow-invest-transfer/edit',
                        title : '债权转让详情'
                    },
                    width : 100
                }, {
                    label : '出让人',
                    name : 'investUser.id',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/site-user/edit',
                        idValue : 'investUser.id',
                        title : '出让人详情'
                    },
                    width : 100
                }, {
                    label : '借款ID',
                    name : 'borrowInApply.id',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/borrow-in-apply/edit',
                        idValue : 'borrowInApply.id',
                        title : '借款详情'
                    },
                    width : 100,
                    editable : true
                }, {
                    label : '累计出让份额',
                    name : 'applyQuantity',
                    width : 100,
                    editable : true
                }, {
                    label : '剩余份额',
                    name : 'transferRemainQuantity',
                    width : 100,
                    editable : true
                }, {
                    label : '当前每份价值TODO',
                    name : 'singleRemainCostAmount',
                    formatter : 'currency',
                    width : 120
                }, {
                    label : '当前转让系数',
                    name : 'transferRate',
                    formatter : 'percentage',
                    align : 'center',
                    width : 120
                }, {
                    label : '当前每份价格TODO',
                    name : 'singleTransferCostAmount',
                    formatter : 'currency',
                    width : 120
                } ],
                multiselect : false
            });
        });
    </script>
</body>
</html>