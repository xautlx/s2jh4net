<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/sys/config-property/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">代码</label>
                    <div class="controls">
                        <form:input path="propKey" class="form-control"/>
                        <div class="help-block">代码一般会在程序中引用，请勿随意变更代码</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">名称</label>
                    <div class="controls">
                        <form:input path="propName" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">简单属性值</label>
                    <div class="controls">
                        <form:input path="simpleValue" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">HTML属性值</label>
                    <div class="controls">
                        <form:textarea path="htmlValue" class="form-control" data-htmleditor="kindeditor" data-height="400px"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">参数属性用法说明</label>
                    <div class="controls">
                        <form:textarea path="propDescn" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>