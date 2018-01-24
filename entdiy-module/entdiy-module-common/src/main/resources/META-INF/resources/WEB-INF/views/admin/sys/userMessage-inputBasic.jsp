<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='${validationRules}'
           action="admin/sys/user-message/edit">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-body">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">消息内容</label>
                    <div class="controls">
                        <form:textarea path="message" class="form-control" data-htmleditor="kindeditor"
                                       data-height="300px" readonly="true"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form:form>