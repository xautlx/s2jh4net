<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>通知消息列表</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form action="${ctx}/admin/profile/notify-message-list?search_toRead=${param.toRead}" method="get"
				class="form-inline form-validation form-search form-search-init control-label-sm"
				data-div-search=".profile-notify-message-List">
				<div class="btn-group pull-right">
					<a class="btn btn-default ${empty param.toRead?'active':''}" href="${ctx}/admin/profile/notify-message">全部</a> <a
						class="btn btn-default ${not empty param.toRead?'active':''}" href="${ctx}/admin/profile/notify-message?toRead=1">未读</a>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search_title" class="form-control input-medium" placeholder="消息标题...">
					</div>
				</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search_htmlContent" class="form-control input-large" placeholder="消息内容...">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label">发布日期</label>
					<div class="controls">
						<input type="text" name="search_publishTime" class="form-control input-small date-picker">
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

	<div class="profile-notify-message-List"></div>
</body>
</html>