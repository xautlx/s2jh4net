<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运行流程实例</title>
</head>
<body>
	<div class="row">
		<div class="col-md-6"></div>
		<div class="col-md-6">
			<img src="${ctx}/admin/bpm/diagram?processInstanceId=${processInstance.id}" />
		</div>
	</div>
</body>
</html>