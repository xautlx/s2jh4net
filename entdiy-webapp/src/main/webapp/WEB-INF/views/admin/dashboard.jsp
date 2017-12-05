<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Dashboard</title>
</head>
<body>
<div class="note note-info">
    <h4 class="block">提示说明</h4>
    <p>Dashboard内容需要根据项目业务需求定制设计开发</p>
    <p>Your code goes here...</p>
</div>
<c:if test="${devMode}">
    <!-- 开发模式运行，加载开发指南页面，请确保maven定义了dev依赖组件，否则抛出404 -->
    <!-- BEGIN Portlet PORTLET-->
    <div class="portlet light bordered" style="padding: 0px">
        <div class="portlet-title" style="padding: 5px">
            <div class="caption">
                <i class="fa fa-bug font-grey-gallery"></i>
                <span class="caption-subject bold font-grey-gallery uppercase"> 开发指南 </span>
                <span class="caption-helper">以下内容只有在开发模式(dev_mode=true)才会显示，供开发人员参考</span>
            </div>
            <div class="tools">
                <a href="" class="collapse"> </a>
                <a href="" class="remove"> </a>
            </div>
        </div>
        <div class="portlet-body">
            <div data-toggle="ajaxify" data-url="${ctx}/dev/dashboard"></div>
        </div>
    </div>
    <!-- END GRID PORTLET-->
</c:if>
</body>
</html>
