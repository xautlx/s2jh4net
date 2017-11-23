<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>通知消息列表</title>
</head>
<body>
	<div class="row dataTables_wrapper">
		<div class="col-md-12">
			<table class="table table-striped table-hover table-bordered dataTable table-sorting">
				<thead>
					<tr>
						<th>消息标题</th>
						<th>消息摘要</th>
						<th data-sorting-name="createdDate">发布时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="item" items="${notifyMessages}">
						<tr>
							<td><a href="${ctx}/admin/profile/notify-message-view/${item.id}?readed=${item.readed}"
									data-toggle="modal-ajaxify" title="查看消息">
									<c:if test="${!item.readed}">
										<i class="fa fa-envelope-o"></i>
									</c:if>
									${item.title}
								</a></td>
							<td>${item.messageAbstract}</td>
							<td><fmt:formatDate value="${item.publishTime}" type="both" pattern="yyyy-MM-dd mm:HH:ss" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>