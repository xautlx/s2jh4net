<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${r"${validationRules}"}'
           action="/admin${model_path}/${entity_name_field_line}/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <#if model_editable>
        <button class="btn green" type="submit">保存</button>
        </#if>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
    <#list entityFields as entityField>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">${entityField.title}</label>
                    <div class="controls">
                    <#if entityField.edit>
                        <#if entityField.fieldType=='Boolean'>
                        <form:input path="${entityField.fieldName}" class="form-control"/>
                        <#elseif entityField.fieldType=='Entity'>
                        <form:hidden path="${entityField.fieldName}.id" class="form-control" data-select2-type="remote"
                                     data-url="${r"ctx"}/admin/path/to/list" data-display="${r"${"}${entityField.fieldName}${r".display"}"
                                     data-query="search[CN_abc_OR_xyz_OR_abc.xyz]" />
                        <form:input path="${entityField.fieldName}.id" class="form-control"/>
                        <#elseif entityField.enumField>
                        <form:input path="${entityField.fieldName}" class="form-control"/>
                        <#elseif entityField.fieldType=='Date'>
                        <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datepicker"/>
                        <#elseif entityField.fieldType=='Timestamp'>
                        <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datetimepicker"/>
                        <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
                        <form:textarea path="${entityField.fieldName}" rows="3" class="form-control"/>
                        <#elseif entityField.fieldType=='LocalizedLabel'>
                        <ul class="list-group" data-multilocale="true">
                            <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                <li class="list-group-item">
                                    <input type="text" class="form-control" name="${entityField.fieldName}${r".${item.key}"}" value="${r"${item.value}"}" title="${r"${item.name}"}"/>
                                </li>
                            </c:forEach>
                        </ul>
                        <#elseif entityField.fieldType=='LocalizedText'>
                        <ul class="list-group" data-multilocale="true">
                            <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                <li class="list-group-item">
                                    <textarea rows="3" class="form-control" name="${entityField.fieldName}${r".${item.key}"}" value="${r"${item.value}"}" title="${r"${item.name}"}"/>
                                </li>
                            </c:forEach>
                        </ul>
                        <#else>
                        <form:input path="${entityField.fieldName}" class="form-control"/>
                        </#if>
                    <#else>
                        <c:choose>
                            <c:when test="${r"${entity.notNew}"}">
                                <p class="form-control-static">${r"${entity."}${entityField.fieldName}${r"}"}</p>
                            </c:when>
                            <c:otherwise>
                                <#if entityField.fieldType=='Boolean'>
                                <form:input path="${entityField.fieldName}" class="form-control"/>
                                <#elseif entityField.fieldType=='Entity'>
                                <form:hidden path="${entityField.fieldName}.id" class="form-control" data-select2-type="remote"
                                             data-url="${r"ctx"}/admin/path/to/list" data-display="${r"${"}${entityField.fieldName}${r".display"}"
                                             data-query="search[CN_abc_OR_xyz_OR_abc.xyz]" />
                                <#elseif entityField.enumField>
                                <form:input path="${entityField.fieldName}" class="form-control"/>
                                <#elseif entityField.fieldType=='Date'>
                                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datepicker"/>
                                <#elseif entityField.fieldType=='Timestamp'>
                                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datetimepicker"/>
                                <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
                                <form:textarea path="${entityField.fieldName}" rows="3" class="form-control"/>
                                <#elseif entityField.fieldType=='LocalizedLabel'>
                                <ul class="list-group" data-multilocale="true">
                                    <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                        <li class="list-group-item">
                                            <input type="text" class="form-control" name="${entityField.fieldName}${r".${item.key}"}" value="${r"${item.value}"}" title="${r"${item.name}"}"/>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <#elseif entityField.fieldType=='LocalizedText'>
                                <ul class="list-group" data-multilocale="true">
                                    <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                        <li class="list-group-item">
                                            <textarea rows="3" class="form-control" name="${entityField.fieldName}${r".${item.key}"}" value="${r"${item.value}"}" title="${r"${item.name}"}"/>
                                        </li>
                                    </c:forEach>
                                </ul>
                                <#else>
                                <form:input path="${entityField.fieldName}" class="form-control"/>
                                </#if>
                            </c:otherwise>
                        </c:choose>
                    </#if>
                    </div>
                </div>
            </div>
        </div>
    </#list>
    </div>
    <div class="form-actions right">
        <#if model_editable>
        <button class="btn green" type="submit">保存</button>
        </#if>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>