<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-capital-account-trade-index">
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
			<table id="grid-capital-account-trade-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-capital-account-trade-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/p2p/capital-account-trade/list',
                colModel : [ {
                    label : '交易时间TODO',
                    name : 'requestTime',
                    formatter : 'timestamp',
                    editable : true
                },{
                    label : '资金流出账户',
                    name : 'sourceCapitalAccount.id',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/site-user/edit',
                        idValue : 'sourceCapitalAccount.siteUser.id',
                        title : '用户详情'
                    },
                    width : 100,
                    editable : true
                }, {
                    label : '资金流入账户',
                    name : 'targetCapitalAccount.id',
                    width : 100,
                    editable : true
                }, {
                    label : '交易内容',
                    name : 'requestNote',
                    editable : true,
                    align : 'left',
                    width : 250
                },{
                    label : '交易金额',
                    name : 'dealAmount',
                    width : 100,
                    formatter : 'currency',
                    editable : true
                }, {
                    label : '冻结金额',
                    name : 'TODO',
                    align : 'center',
                    formatter : 'currency',
                    width : 120
                },{
                    label : '相关借款',
                    name : 'borrowInApply.id',
                    width : 100,
                    editable : true
                },{
                    label : '相关债权转让',
                    name : 'borrowInvestTransferApply.id',
                    width : 100,
                    editable : true
                } ],
                multiselect : false,
                subGrid : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initSubGrid(subgrid_id, row_id, {
                        url : WEB_ROOT + "/admin/p2p/capital-account-trade/log-list?search['EQ_capitalAccountTrade.id']=" + row_id,
                        colNames : [ '交易明细项金额', '交易子项类型TODO' ],
                        colModel : [ {
                            name : 'dealAmount',
                            formatter : 'currency',
                            width : 150
                        }, {
                            name : 'tradeSubType',
                            align : 'center'
                        } ]
                    });
                }
            });
        });
    </script>
</body>
</html>