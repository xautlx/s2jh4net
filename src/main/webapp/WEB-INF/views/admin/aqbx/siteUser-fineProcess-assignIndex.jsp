<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>逾期处理</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-siteuser-fineprocess-assign-index">
				<div class="pull-right">
					<div class="form-group">
						<button class="btn blue" type="button">设置自动分配规则</button>
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_siteUser.user.authUid_OR_siteUser.trueName_OR_siteUser.idCardNo']" class="form-control input-large"
							placeholder="账号，姓名，身份证号...">
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
			<table id="grid-siteuser-fineprocess-assign-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-siteuser-fineprocess-assign-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/p2p/fine-process/assign-list',
                colModel : [ {
                    label : '借款人',
                    name : 'siteUser.display',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/p2p/site-user/edit',
                        idValue : 'siteUser.id',
                        title : '借款人详情'
                    },
                    align : 'center',
                    width : 100
                }, {
                    label : '分配状态',
                    name : 'assignStatus',
                    align : 'center',
                    width : 200
                }, {
                    label : '处理者',
                    name : 'assignFineOperator.nickName',
                    align : 'center',
                    width : 100
                }, {
                    label : '信用分',
                    name : 'siteUser.creditScore',
                    align : 'center',
                    formatter : 'integer',
                    width : 100
                }, {
                    label : '当前逾期数',
                    name : 'TODO',
                    align : 'center',
                    width : 100
                }, {
                    label : '当前最高逾期时长',
                    name : 'TODO',
                    align : 'center',
                    width : 100
                }, {
                    label : '逾期金额',
                    name : 'TODO',
                    align : 'center',
                    width : 100
                }, {
                    label : '罚息合计',
                    name : 'totalFineInterestAmount',
                    formatter : 'number',
                    align : 'center',
                    width : 100
                }, {
                    label : '累计逾期次数',
                    name : 'totalFineInterestQuantity',
                    align : 'center',
                    formatter : 'integer',
                    width : 100
                } ],
                operations : function(items) {
                    var $grid = $(this);
                    var $select = $('<li data-position="multi" data-toolbar="show"><a href="javascript:;">分配至...</a></li>');
                    $select.children("a").bind("click", function(e) {
                        var ids = $grid.getAtLeastOneSelectedItem();
                        if (ids) {
                            $(this).popupDialog({
                                url : WEB_ROOT + '/admin/p2p/fine-process/assign-selection?ids=' + ids,
                                title : '分配逾期任务',
                                size : '500px'
                            });
                        }
                    });
                    items.push($select);
                }
            });
        });
    </script>
</body>
</html>