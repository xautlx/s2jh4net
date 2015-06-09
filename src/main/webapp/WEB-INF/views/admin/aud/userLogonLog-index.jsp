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
				data-grid-search="#grid-aud-user-logon-log-index">
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_authGuid_OR_authUid_OR_logonYearMonthDay']" class="form-control input-large" placeholder="账号全局唯一标识 , 账号类型所对应唯一标识 , 便于按日汇总统计的冗余属性...">
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
			<table id="grid-aud-user-logon-log-index"></table>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function() {
		    $("#grid-aud-user-logon-log-index").data("gridOptions", {
		        url : WEB_ROOT + '/admin/aud/user-logon-log/list',
		        colModel : [ {
		            label : '流水号',
		            name : 'id',
		            hidden : true                          
		        }, {
		            label : '账号全局唯一标识',
		            name : 'authGuid',
		            width : 64,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '账号类型所对应唯一标识',
		            name : 'authUid',
		            width : 64,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '账号类型',
		            name : 'authType',
		            formatter : 'select',
                    searchoptions : {
                        valueJsonString : '<tags:json value="${authTypeMap}"/>'
                    },
		            width : 80,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '便于按日汇总统计的冗余属性',
		            name : 'logonYearMonthDay',
		            width : 255,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '登录时间',
		            name : 'logonTime',
		            width : 150,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '登出时间',
		            name : 'logoutTime',
		            width : 150,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : '登录时长',
		            name : 'logonTimeLength',
		            editable: true                                                                   
		        }, {
		            label : '登录次数',
		            name : 'logonTimes',
		            formatter: 'integer',
		            editable: true                                                                   
		        }, {
		            label : 'userAgent',
		            name : 'userAgent',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'xforwardFor',
		            name : 'xforwardFor',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'localAddr',
		            name : 'localAddr',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'localName',
		            name : 'localName',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'localPort',
		            name : 'localPort',
		            formatter: 'integer',
		            editable: true                                                                   
		        }, {
		            label : 'remoteAddr',
		            name : 'remoteAddr',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'remoteHost',
		            name : 'remoteHost',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'remotePort',
		            name : 'remotePort',
		            formatter: 'integer',
		            editable: true                                                                   
		        }, {
		            label : 'serverIP',
		            name : 'serverIP',
		            width : 200,
		            align : 'center',
		            editable: true                                                                   
		        }, {
		            label : 'Session编号',
		            name : 'httpSessionId',
		            width : 128,
		            align : 'center',
		            editable: true                                                                   
		        } ],
		        editurl : WEB_ROOT + '/admin//aud/user-logon-log/edit',
		        editrulesurl : WEB_ROOT + '/admin/util/validate?clazz=${clazz}',
		        delurl : WEB_ROOT + '/admin/aud/user-logon-log/delete',
		        fullediturl : WEB_ROOT + '/admin/aud/user-logon-log/edit-tabs'
		    });
		});
    </script>
</body>
</html>
