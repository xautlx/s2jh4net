<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@page import="com.entdiy.core.exception.BaseRuntimeException,org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>系统异常</title>
    <link href="${applicationScope.ctx}/assets/pages/css/error.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<%
    Throwable ex = null;
    if (exception != null)
        ex = exception;
    if (request.getAttribute("javax.servlet.error.exception") != null)
        ex = (Throwable) request.getAttribute("javax.servlet.error.exception");    //记录日志
    Logger logger = LoggerFactory.getLogger("500.jsp");
    String exceptionCode = BaseRuntimeException.buildExceptionCode();
    String errorTitle = exceptionCode + ": ";
    String errorMessage = errorTitle + "系统运行错误，请联系管理员！";
    logger.error(errorMessage, ex);
%>
<div class="row">
    <div class="col-md-12 page-500">
        <div class=" number">500</div>
        <div class=" details">
            <h3>
                <%=errorMessage%>
            </h3>
            <p>
                请尝试刷新页面或重新登录后再次操作。如果问题依然请将上述错误信息反馈给系统管理员。<br> <br>
            </p>
        </div>
    </div>
</div>
</body>
</html>