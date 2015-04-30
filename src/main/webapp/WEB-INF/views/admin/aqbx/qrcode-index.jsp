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
				data-grid-search="#grid-aqbx-qrcode-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_uid']" class="form-control input-large" placeholder="编号">
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
			<table id="grid-aqbx-qrcode-index"></table>
		</div>
	</div>


	<script type="text/javascript">
        $(function() {
            $("#grid-aqbx-qrcode-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aqbx/qrcode/list',
                colModel : [ {
                    label : '流水号',
                    name : 'id',
                    hidden : true
                }, {
                    label : '二维码编号',
                    name : 'uid',
                    formatter : 'showlink',
                    formatoptions : {
                        baseLinkUrl : WEB_ROOT + '/admin/aqbx/qrcode/preview',
                        title : '二维码预览'
                    },
                    width : 250,
                    align : 'left'
                }, {
                    label : '内容',
                    name : 'content',
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '批量数量',
                    name : 'printCount',
                    index : 'printCount',
                    width : 200,
                    width : 200,
                    editable : true,
                    align : 'left'
                }, {
                    label : '创建时间',
                    name : 'createdDate',
                    width : 150,
                    formatter : 'timestamp',
                    align : 'center'
                }, {
                    label : '启用时间',
                    name : 'enableDate',
                    width : 150,
                    formatter : 'timestamp',
                    editable : true,
                    align : 'center'
                }, {
                    label : '推广人',
                    name : 'bindSiteUser.nickName',
                    index : 'siteUser',
                    width : 200,
                    width : 200,
                    editable : true,
                    align : 'left'
                } ],
                postData : {
                    "search['FETCH_bindSiteUser']" : "LEFT"
                },
                editurl : WEB_ROOT + '/admin/aqbx/qrcode/edit',
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                delurl : WEB_ROOT + '/admin/aqbx/qrcode/delete',
                fullediturl : WEB_ROOT + '/admin/aqbx/qrcode/edit-tabs',
                addable : false,
                operations : function(items) {
                    var $grid = $(this);
                    var $batch = $('<li data-toolbar="show" data-text="show"><a href="/aqbx/admin/aqbx/qrcode/batch" data-toggle="modal-ajaxify"> 批次创建</a></li>');
                    items.push($batch);
                }
            });
        });
    </script>
</body>
</html>
