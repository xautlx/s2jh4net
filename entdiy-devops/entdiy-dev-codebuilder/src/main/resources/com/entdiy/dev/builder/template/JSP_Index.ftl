<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init"
                      data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_${searchOrFieldNames}]"
                                   class="form-control input-large"
                                   placeholder="${searchOrFieldPlaceholders}...">
                        </div>
                    </div>
                    <div class="form-group search-group-btn">
                        <button class="btn green" type="submit">
                            <i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
                        </button>
                        <button class="btn default" type="reset">
                            <i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="search-group-target"
                   grid-options='{
                   url: "/admin${convert_model_path}/${entity_name_field_line}/list",
                   colModel: [{
                       label: "流水号",
                       name: "id",
                       hidden: true
                   <#list entityFields as entityField>
                   <#if entityField.list>
                       <#if (entityField.enumField || entityField.fieldType=="DataDict")>
                   }, {
                       label: "${entityField.title}",
                       name: "${entityField.fieldName}",
                       formatter: "select",
                       width: ${entityField.listWidth},
                       searchoptions: {
                           valueJson: ${r"${"}${entityField.fieldName}Json${r"}"}
                       },
                       <#elseif entityField.fieldType=="Entity">
                   }, {
                       label: "${entityField.title}",
                       name: "${entityField.fieldName}.display",
                       index: "${entityField.fieldName}",
                       width: ${entityField.listWidth},
                       <#elseif entityField.fieldType=="LocalizedLabel">
                   }, {
                       label: "${entityField.title}",
                       name: "${entityField.fieldName}.zhCN",
                       index: "${entityField.fieldName}.zhCN",
                       <#else>
                   }, {
                       label: "${entityField.title}",
                       name: "${entityField.fieldName}",
                       </#if>
                       <#if entityField.listHidden>
                       hidden: true,
                       </#if>
                       <#if entityField.fieldType=="Boolean">
                       formatter: "checkbox",
                       </#if>
                       <#if entityField.fieldType=="LocalDate">
                       formatter: "date",
                       </#if>
                       <#if entityField.fieldType=="AttachmentImage">
                       formatter: "image",
                       </#if>
                       <#if entityField.fieldType=="LocalDateTime">
                       formatter: "timestamp",
                       </#if>
                       <#if entityField.fieldType=="Instant">
                       formatter: "timestamp",
                       </#if>
                       <#if entityField.fieldType=="Integer">
                       formatter: "integer",
                       </#if>
                       <#if entityField.fieldType=="BigDecimal">
                       formatter: "number",
                       </#if>
                       <#if entityField.fieldType=="String">
                       width: ${entityField.listWidth},
                       </#if>
                       align: "${entityField.listAlign}",
                       editable: true
                   </#if>
                   </#list>
                   }],
                   <#if fetchJoinFields?exists>
                   postData: {
                   <#list fetchJoinFields?keys as key>
                           "search[FETCH_${key}]": "${fetchJoinFields[key]}"<#if (key_has_next)>,</#if>
                   </#list>
                   },
                   </#if>
                   editurl: "/admin${convert_model_path}/${entity_name_field_line}/edit",
                   editvalidation: ${r"${validationRules}"},
                   delurl: "/admin${convert_model_path}/${entity_name_field_line}/delete",
                   fullediturl: "/admin${convert_model_path}/${entity_name_field_line}/edit-tabs"
                }'>
            </table>
        </div>
    </div>
</div>