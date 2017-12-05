<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通知消息管理</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form method="get" class="form-inline form-validation form-search-init control-label-sm"
				data-grid-search="#grid-sys-user-message-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_title_OR_message']" class="form-control input-large" placeholder="标题，内容...">
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
			<table id="grid-sys-user-message-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-sys-user-message-index").data("gridOptions", {
                url : WEB_ROOT + "/admin/sys/user-message/list?search['EQ_targetUser']=${param.siteUserId}",
                colModel : [ {
                    label : '目标用户',
                    name : 'targetUser.display',
                    editable : true,
                    width : 100,
                    align : 'left'
                }, {
                    label : '标题',
                    name : 'title',
                    editable : true,
                    align : 'left'
                }, {
                    label : '消息摘要',
                    name : 'messageAbstract',
                    index : 'externalLink_OR_htmlContent',
                    align : 'left'
                }, {
                    label : '发布时间',
                    name : 'createdDate',
                    formatter : 'timestamp',
                    editable : true,
                    editoptions : {
                        time : true
                    },
                    formatoptions : {
                        srcformat : 'Y-m-d H:i',
                        newformat : 'Y-m-d H:i'
                    },
                    align : 'center'
                }, {
                    label : '到期时间',
                    name : 'expireTime',
                    formatter : 'timestamp',
                    editable : true,
                    editoptions : {
                        time : true
                    },
                    formatoptions : {
                        srcformat : 'Y-m-d H:i',
                        newformat : 'Y-m-d H:i'
                    },
                    align : 'center'
                }, {
                    label : '总计阅读次数',
                    name : 'readTotalCount',
                    width : 60,
                    fixed : true,
                    align : 'center'
                }, {
                    label : '首次阅读时间',
                    name : 'firstReadTime',
                    formatter : 'timestamp'
                }, {
                    label : '最后阅读时间',
                    name : 'lastReadTime',
                    formatter : 'timestamp'
                } ],
                multiselect : false,
                addable : false,
                fullediturl : WEB_ROOT + "/admin/sys/user-message/edit",
            });
        });
    </script>
</body>
</html>