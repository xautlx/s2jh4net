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
				data-grid-search="#grid-sys-notify-message-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_title_OR_htmlContent']" class="form-control input-large"
							placeholder="标题，内容...">
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
			<table id="grid-sys-notify-message-index"></table>
		</div>
	</div>

	<script type="text/javascript">
        $(function() {
            $("#grid-sys-notify-message-index").data("gridOptions", {
                url : WEB_ROOT + '/admin/sys/notify-message/list',
                colModel : [ {
                    label : '阅读人数',
                    name : 'readUserCount',
                    width : 60,
                    fixed : true,
                    align : 'center'
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
                    label : '平台设置',
                    name : 'platform',
                    index : 'platform',
                    width : 60,
                }, {
                    label : '消息目标列表',
                    name : 'audienceTags',
                    index : 'audienceTags',
                    width : 60,
                }, {
                    label : '消息目标组合',
                    name : 'audienceAndTags',
                    index : 'audienceAndTags',
                    width : 60,
                }, {
                    label : '用户标识列表',
                    name : 'audienceAlias',
                    index : 'audienceAlias',
                    width : 60,
                }, {
                    label : '排序号',
                    name : 'orderRank',
                    width : 60,
                    sorttype : 'number',
                    editable : true,
                    align : 'center'
                }, {
                    label : '生效时间',
                    name : 'publishTime',
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
                    label : '已读人数',
                    name : 'readUserCount',
                    width : 40,
                    align : 'center'
                } ],
                editurl : WEB_ROOT + '/admin/sys/notify-message/edit',
                inlineNav : {
                    add : false
                },
                multiselect : false,
                editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
                fullediturl : WEB_ROOT + "/admin/sys/notify-message/edit",
                delurl : WEB_ROOT + '/admin/sys/notify-message/delete',
                subGrid : true,
                subGridRowExpanded : function(subgrid_id, row_id) {
                    Grid.initSubGrid(subgrid_id, row_id, {
                        url : WEB_ROOT + "/admin/sys/notify-message/read-list?search['EQ_notifyMessage.id']=" + row_id,
                        colNames : [ '阅读用户', '首次阅读时间', '最后阅读时间', '总计阅读次数' ],
                        colModel : [ {
                            name : 'readUserLabel',
                            width : 150
                        }, {
                            name : 'firstReadTime',
                            formatter : 'timestamp'
                        }, {
                            name : 'lastReadTime',
                            formatter : 'timestamp'
                        }, {
                            name : 'readTotalCount',
                            align : 'center'
                        } ]
                    });
                }
            });
        });
    </script>
</body>
</html>