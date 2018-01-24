<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/auth/role/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="form-group">
            <label class="control-label">代码</label>
            <div class="controls">
                <form:input path="code"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">名称</label>
            <div class="controls">
                <form:input path="name"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">禁用</label>
            <div class="controls">
                <form:radiobuttons path="disabled" items="${applicationScope.cons.booleanLabelMap}"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">描述</label>
            <div class="controls">
                <form:textarea path="description" rows="3"/>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>