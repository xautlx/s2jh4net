<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="tabbable tabbable-custom">
    <ul class="nav nav-tabs" data-active="${r"${param._tab_active}"}">
        <li class="tools pull-right"><a href="javascript:;" class="btn default reload"><i class="fa fa-refresh"></i></a></li>
        <li><a data-toggle="tab" href="/admin${model_path}/${entity_name_field_line}/edit?id=${r"${id}"}">基本信息</a></li>
        <li data-disabled="${r"${entity.isNew()}"}"><a data-toggle="tab" href="/admin${model_path}/${entity_name_field_line}/other?id=${r"${id}"}">其他管理</a></li>
    </ul>
</div>