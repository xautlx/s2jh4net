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
			<form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
				data-grid-search="#grid-aud-send-message-log-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_targets_OR_title_OR_message']" class="form-control input-large"
							placeholder="消息接受者 , 标题 , 消息内容...">
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
			<table id="grid-aud-send-message-log-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-aud-send-message-log-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/aud/send-message-log/list',
                colModel : [ {
                    label : '流水号',
                    name : 'id',
                    hidden : true
                }, {
                    label : '消息接受者',
                    name : 'targets',
                    width : 100,
                    align : 'center',
                    editable : true
                }, {
                    label : '标题',
                    name : 'title',
                    width : 200,
                    align : 'left',
                    editable : true
                }, {
                    label : '消息摘要',
                    index : 'message',
                    name : 'messageAbstract',
                    width : 300,
                    align : 'left'
                }, {
                    label : '消息类型',
                    name : 'messageType',
                    formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${messageTypeMap}"/>'
                    },
                    width : 80,
                    align : 'center',
                    editable : true
                }, {
                    label : '发送时间',
                    name : 'sendTime',
                    width : 150,
                    align : 'center',
                    formatter : 'timestamp',
                    editable : true
                } ],
                multiselect : false,
                addable : false,
                fullediturl : WEB_ROOT + '/admin/aud/send-message-log/edit-tabs'
            });
        });
    </script>
</body>
</html>
