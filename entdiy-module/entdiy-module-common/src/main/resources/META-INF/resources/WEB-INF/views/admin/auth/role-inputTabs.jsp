<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="tabbable tabbable-custom">
    <ul class="nav nav-tabs" data-active="${param._tab_active}">
        <li class="tools pull-right">
            <a href="javascript:;" class="btn default reload"><i class="fa fa-refresh"></i></a>
        </li>
        <li><a data-toggle="tab" href="admin/auth/role/edit?id=${id}">基本信息</a></li>
        <li data-disabled="${entity.isNew()}">
            <a data-toggle="tab" href="admin/auth/role/privileges?id=${id}">权限关联</a>
        </li>
    </ul>
</div>
