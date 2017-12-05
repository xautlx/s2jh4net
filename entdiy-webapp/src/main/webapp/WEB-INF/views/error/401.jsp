<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>401 - 未登录验证</title>
</head>

<body>
	<h2>401 - 未登录验证</h2>
	<p>
		<a href="<c:url value="/login"/>">登录</a>
	</p>
</body>
</html>
