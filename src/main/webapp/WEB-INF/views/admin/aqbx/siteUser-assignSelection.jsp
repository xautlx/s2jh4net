<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<form class="form-validation" action="${ctx}/admin/p2p/verify/assign" method="post" data-editrulesurl="false">
		<input type="hidden" name="ids" value="${param.ids}" />
		<div class="row">
			<div class="col-md-12">
				<table class="table table-striped table-hover table-bordered">
					<thead>
						<tr>
							<th style="text-align: center">选择</th>
							<th style="text-align: center">分配用户</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${users}">
							<tr>
								<td align="center"><input type="radio" name="assignTo" value="${item.id}" required="true" /></td>
								<td align="center">${item.display}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="form-actions right">
			<button class="btn blue" type="submit" data-grid-reload="#grid-siteuser-assign-verify-index" data-post-dismiss="modal">
				<i class="fa fa-check"></i> 确认分配
			</button>
		</div>
	</form>
</body>
</html>