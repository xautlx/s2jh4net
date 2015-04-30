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
				data-grid-search="#grid-aqbx-product-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_product.name']" class="form-control input-large" placeholder="代码，名称...">
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
			<table id="grid-aqbx-product-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-aqbx-product-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aqbx/product/list',
                colModel : [ {
                    label : '商品编号',
                    name : 'product.id',
                    width : 200,
                    align : 'left'
                }, {
                    label : '商品SKU编号',
                    name : 'id',
                    width : 200,
                    align : 'left'
                }, {
                    label : '商品名称',
                    name : 'product.name',
                    width : 200,
                    align : 'left'
                }, {
                    label : 'SKU价格',
                    name : 'price',
                    width : 200,
                    align : 'left'
                }, {
                    label : '佣金比例',
                    name : 'commissionRate',
                    width : 200,
                    align : 'left'
                }, {
                    label : '商品状态',
                    name : 'null',
                    width : 200,
                    align : 'left'
                }],
                editurl : WEB_ROOT + '/admin/aqbx/product/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        fullediturl : WEB_ROOT + '/admin/aqbx/product/edit-tabs'
            });
        });
    </script>
</body>
</html>
