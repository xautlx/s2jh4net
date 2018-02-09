<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="tabbable tabbable-custom">
    <ul class="nav nav-tabs" data-active="${param._tab_active}">
        <li class="tools pull-right"><a href="javascript:;" class="btn default reload"><i class="fa fa-refresh"></i></a></li>
        <li><a data-toggle="tab" href="/admin/auth/user/edit?id=${id}">基本信息</a></li>
        <li data-disabled="${entity.isNew()}"><a data-toggle="tab" href="/admin/auth/user/roles?id=${id}">角色关联</a></li>
        <li data-disabled="${entity.isNew()}"><a data-toggle="tab" href="/admin/auth/user/privileges?id=${id}">权限汇总</a></li>
        <li data-disabled="${entity.isNew()}"><a data-toggle="tab" href="/admin/auth/user/menus?id=${id}">菜单汇总</a></li>
        <li data-disabled="${entity.isNew()}"><a data-toggle="tab" href="/admin/auth/account/logon-data?id=${entity.account.id}">登录记录</a></li>
    </ul>
</div>