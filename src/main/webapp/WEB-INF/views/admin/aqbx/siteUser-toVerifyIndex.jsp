<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>审核处理</title>
</head>
<body>
	<div class="row search-form-default">
		<div class="col-md-12">
			<form action="${ctx}/admin/p2p/verify/to-verify-list" method="get"
				class="form-inline form-validation form-search form-search-init control-label-sm"
				data-div-search=".site-user-verify-List">
				<div class="pull-right">统计信息</div>
				<div class="form-group">
					<div class="controls controls-clearfix">
						<input type="text" name="search['CN_user.authUid_OR_trueName_OR_idCardNo']" class="form-control input-large"
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
	<div class="site-user-verify-List"></div>
</body>
</html>