<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${r"${pageContext.request.contextPath}"}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
		action="${r"${ctx}"}/admin${model_path}/${entity_name_field_line}/edit" method="post" modelAttribute="entity"
		data-editrulesurl="${r"${ctx}"}/admin/util/validate?clazz=${r"${clazz}"}">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<div class="form-actions">
		    <#if model_editable>
			<button class="btn blue" type="submit" data-grid-reload="#grid-${full_entity_name_field}-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-${full_entity_name_field}-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
			<#else>
			<button class="btn default" type="button" data-dismiss="modal">关闭</button>
			</#if>
		</div>
		<div class="form-body">
        <#list entityFields as entityField>       
	        <div class="row">
	            <div class="col-md-6">
					<div class="form-group">
						<label class="control-label">${entityField.title}</label>
						<div class="controls">
						<#if entityField.edit>
			                <#if entityField.fieldType=='Boolean'>
			                <form:input path="${entityField.fieldName}" class="form-control"/>
			                <#elseif entityField.fieldType=='Entity'>
			                <form:hidden path="${entityField.fieldName}.id" class="form-control" data-select2-type="remote"
								data-url="${r"ctx"}/admin/path/to/list" data-display="${r"${"}${entityField.fieldName}${r".display"}"
								data-query="search['CN_abc_OR_xyz_OR_abc.xyz']" />			                
			                <form:input path="${entityField.fieldName}.id" class="form-control"/>			                
			                <#elseif entityField.enumField>
			                <form:input path="${entityField.fieldName}" class="form-control"/>
			                <#elseif entityField.fieldType=='Date'>
			                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datepicker"/>    
			                <#elseif entityField.fieldType=='Timestamp'>
			                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datetimepicker"/>                                               
			                <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
			                <form:textarea path="${entityField.fieldName}" rows="3" class="form-control"/>                                             
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
										data-query="search['CN_abc_OR_xyz_OR_abc.xyz']" />			                
					                <#elseif entityField.enumField>
					                <form:input path="${entityField.fieldName}" class="form-control"/>
					                <#elseif entityField.fieldType=='Date'>
					                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datepicker"/>   
					                <#elseif entityField.fieldType=='Timestamp'>
					                <form:input path="${entityField.fieldName}" class="form-control" data-toggle="datetimepicker"/>     					                                                                  
					                <#elseif (entityField.fieldType=='String' && entityField.listWidth gt 255)>
					                <form:textarea path="${entityField.fieldName}" rows="3" class="form-control"/>                                             
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
			<button class="btn blue" type="submit" data-grid-reload="#grid-${full_entity_name_field}-index">
				<i class="fa fa-check"></i> 保存
			</button>
			<button class="btn green" type="submit" data-grid-reload="#grid-${full_entity_name_field}-index" data-post-dismiss="modal">保存并关闭
			</button>
			<button class="btn default" type="button" data-dismiss="modal">取消</button>
			<#else>
			<button class="btn default" type="button" data-dismiss="modal">关闭</button>
			</#if>
		</div>
	</form:form>
</body>
</html>