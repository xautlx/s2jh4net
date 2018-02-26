<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="note note-danger">
    <p>以下统计功能主要用于展示业务典型的分组统计层级展示效果，统计纬度或逻辑并无实际意义和合理性。实际开发需按照业务需求合理定义统计纬度和层次。</p>
</div>
<div class="tabbable tabbable-custom">
    <ul class="nav nav-tabs" data-active="${param._tab_active}">
        <li class="tools pull-right"><a href="javascript:;" class="btn default reload"><i class="fa fa-refresh"></i></a></li>
        <li><a data-toggle="tab" href="/dev/demo/reimbursement-request/stat/submit-date">按日按人统计</a></li>
        <li><a data-toggle="tab" href="/dev/demo/reimbursement-request/stat/department">按部门按人统计</a></li>
        <li><a data-toggle="tab" href="/dev/demo/reimbursement-request/stat/use-type">挂账类型</a></li>
    </ul>
</div>