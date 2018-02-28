<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/sys/data-dict/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <c:if test="${param['parent.id']!=null}">
        <input type="hidden" name="parent.id" value="${param['parent.id']}"/>
    </c:if>
    <div class="form-actions">
        <button class="btn green" type="submit">保存</button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">主标识</label>
                    <div class="controls">
                        <form:input path="primaryKey" class="form-control"/>
                        <div class="help-block">代码一般会在程序中引用，请勿随意变更代码</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">次标识</label>
                    <div class="controls">
                        <form:input path="secondaryKey" class="form-control"/>
                        <div class="help-block">代码一般会在程序中引用，请勿随意变更代码</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">主要数据</label>
                    <div class="controls">
                        <form:input path="primaryValue" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">次要数据</label>
                    <div class="controls">
                        <form:input path="secondaryValue" class="form-control"/>
                        <div class="help-block">在主要数据基础上可以再额外维护一个次要数据，在业务逻辑中根据需要定制使用</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">HTML属性值</label>
                    <div class="controls">
                        <form:textarea path="richTextValue" class="form-control" data-htmleditor="kindeditor" data-height="400px"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">禁用标识</label>
                    <div class="controls">
                        <form:radiobuttons path="disabled" items="${applicationScope.cons.booleanLabelMap}"/>
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
