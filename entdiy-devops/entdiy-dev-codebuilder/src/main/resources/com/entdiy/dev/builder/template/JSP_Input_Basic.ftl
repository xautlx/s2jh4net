<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${r"${validationRules}"}'
           action="/admin${convert_model_path}/${entity_name_field_line}/edit">
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
                        <form:radiobuttons path="${entityField.fieldName}" class="form-control" required="true"
                                           items="${r"${"}applicationScope.cons.booleanLabelMap${r"}"}"/>
                        <#elseif entityField.fieldType=='Entity'>
                        <form:select path="${entityField.fieldName}.id" class="form-control"
                                     data-url="admin/biz/${entityField.fieldName}/list"
                                     data-term-query="search[CN_code_OR_name]"
                                     multiple="false">
                            <form:option value="${r"${entity."}${entityField.fieldName}${r".id}"}" label="${r"${entity."}${entityField.fieldName}${r".display}"}"/>
                        </form:select>
                        <#elseif (entityField.enumField || entityField.fieldType=="DataDict")>
                        <form:select path="${entityField.fieldName}" class="form-control"
                                     items="${r"${"}${entityField.fieldName}${r"Map}"}" multiple="false"/>
                        <#elseif entityField.fieldType=='AttachmentImage'>
                        <form:hidden path="${entityField.fieldName}" class="form-control"
                                     data-imageuploader="single" data-width="120px" data-height="120px"/>
                        <#elseif entityField.fieldType=='AttachmentImageList'>
                        <form:hidden path="${entityField.fieldName}" class="form-control"
                                     data-imageuploader="multiple" data-width="120px" data-height="120px"/>
                        <#elseif entityField.fieldType=='AttachmentFile'>
                        <ul class="list-group" data-fileuploader="${entityField.fieldName}" data-multiple="false">
                            <li class="list-group-item">
                                <form:hidden path="${entityField.fieldName}.id"/>
                                <form:hidden path="${entityField.fieldName}.accessUrl"/>
                                <form:hidden path="${entityField.fieldName}.fileRealName"/>
                                <form:hidden path="${entityField.fieldName}.fileLength"/>
                            </li>
                        </ul>
                        <#elseif entityField.fieldType=='AttachmentFileList'>
                        <ul class="list-group" data-fileuploader="${entityField.fieldName}" data-multiple="true">
                            <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r"}"}" varStatus="status">
                                <li class="list-group-item">
                                    <form:hidden path="${entityField.fieldName}[${r"${status.index}"}].id"/>
                                    <form:hidden path="${entityField.fieldName}[${r"${status.index}"}].accessUrl"/>
                                    <form:hidden path="${entityField.fieldName}[${r"${status.index}"}].fileRealName"/>
                                    <form:hidden path="${entityField.fieldName}[${r"${status.index}"}].fileLength"/>
                                </li>
                            </c:forEach>
                        </ul>
                        <#elseif entityField.fieldType=='LocalDate'>
                        <form:input path="${entityField.fieldName}" class="form-control" data-picker="date"/>
                        <#elseif entityField.fieldType=='LocalDateTime'>
                        <form:input path="${entityField.fieldName}" class="form-control" data-picker="date-time"/>
                        <#elseif entityField.fieldType=='Instant'>
                        <form:input path="${entityField.fieldName}" class="form-control" data-picker="date-time"/>
                        <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 1024)>
                        <form:textarea path="${entityField.fieldName}" class="form-control"
                                       data-htmleditor="kindeditor" rows="3" data-height="400"/>
                        <#elseif entityField.fieldType=='LocalizedLabel'>
                        <ul class="list-group" data-multilocale="true">
                            <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                <li class="list-group-item">
                                    <input type="text" class="form-control" name="${entityField.fieldName}${r".${item.key}"}"
                                           value="${r"${item.value}"}" title="${r"${item.name}"}" data-rule-required="${r"${item.required}"}"/>
                                </li>
                            </c:forEach>
                        </ul>
                        <#elseif entityField.fieldType=='LocalizedText'>
                        <ul class="list-group" data-multilocale="true">
                            <c:forEach var="item" items="${r"${entity."}${entityField.fieldName}${r".items}"}" varStatus="status">
                                <li class="list-group-item">
                                    <textarea rows="3" class="form-control" name="${entityField.fieldName}${r".${item.key}"}"
                                              title="${r"${item.name}"}" data-rule-required="${r"${item.required}"}">
                                        ${r"${item.value}"}
                                    </textarea>
                                </li>
                            </c:forEach>
                        </ul>
                        <#else>
                        <form:input path="${entityField.fieldName}" class="form-control"/>
                        </#if>
                    <#else>
                        <p class="form-control-static">${r"${entity."}${entityField.fieldName}${r"}"}${entityField.fieldType}</p>
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