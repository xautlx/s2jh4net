<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
				data-grid-search="#grid-aqbx-user-share-index">
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
			<table id="grid-aqbx-user-share-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-aqbx-user-share-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aqbx/user-share/list',
                colModel : [ {
                    label : '流水号',
                    name : 'id',
                    hidden : true
                }, {
                    label : '分享唯一标识',
                    name : 'shareUid',
                    width : 200,
                    align : 'left'
                }, {
                    label : '关联用户',
                    name : 'siteUser.display',
                    index : 'siteUser',
                    width : 200,
                    width : 200,
                    align : 'left'
                }, {
                    label : '商品',
                    name : 'productId',
                    width : 60,
                    align : 'right'
                }, {
                    label : '商品SKU',
                    name : 'productSKUId',
                    width : 60,
                    align : 'right'
                }, {
                    label : '分享渠道',
                    name : 'shareTarget',
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '分享链接',
                    name : 'shareUrl',
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '分享文本',
                    name : 'shareText',
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '分享创建时间',
                    name : 'shareTime',
                    width : 150,
                    formatter : 'timestamp',
                    align : 'center'
                }, {
                    label : '成功处理时间',
                    name : 'successTime',
                    width : 150,
                    formatter : 'timestamp',
                    align : 'center'
                }, {
                    label : '分享首次访问时间',
                    name : 'firstVisitTime',
                    width : 150,
                    formatter : 'timestamp',
                    align : 'center'
                }, {
                    label : '分享最近访问时间',
                    name : 'lastVisitTime',
                    width : 150,
                    formatter : 'timestamp',
                    align : 'center'
                }, {
                    label : '分享被访问统计次数',
                    name : 'visitTotalCount',
                    width : 60,
                    align : 'right'
                } ],
                postData : {
                    "search['FETCH_siteUser']" : "INNER"
                },
                editurl : WEB_ROOT + '/admin//aqbx/user-share/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                delurl : WEB_ROOT + '/admin/aqbx/user-share/delete',
                fullediturl : WEB_ROOT + '/admin/aqbx/user-share/edit-tabs'
            });
        });
    </script>
</body>
</html>
