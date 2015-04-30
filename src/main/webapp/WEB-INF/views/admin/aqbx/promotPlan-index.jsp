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
				data-grid-search="#grid-aqbx-promot-plan-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_title_OR_title']" class="form-control input-large" placeholder="方案标题，营销商品...">
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
			<table id="grid-aqbx-promot-plan-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-aqbx-promot-plan-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aqbx/promot-plan/list',
                colModel : [ {
                    label : '方案标题',
                    name : 'title',
                    width : 200,
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '商品',
                    name : 'product.display',
                    index : 'product.name_OR_product.title',
                    width : 200,
                    editable : false,
                    align : 'left'
                }, {
                    label : '方案内容描述',
                    name : 'description',
                    width : 255,
                    editable : true,
                    align : 'left'
                }, {
                    label : '失效标识',
                    name : 'expired',
                    width : 150,
                    formatter : 'checkbox',
                    editable : true,
                    align : 'center'
                } ],
                postData : {
                    "search['FETCH_product']" : "LEFT"
                },
                editurl : WEB_ROOT + '/admin//aqbx/promot-plan/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                delurl : WEB_ROOT + '/admin/aqbx/promot-plan/delete',
                fullediturl : WEB_ROOT + '/admin/aqbx/promot-plan/edit-tabs'
            });
        });
    </script>
</body>
</html>
