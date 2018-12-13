<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
           modelAttribute="entity" data-validation='true'
           action="admin/sys/biz-remark-log/edit">
    <input type="hidden" name="bizEntityClass" value="${param.bizEntityClass}">
    <input type="hidden" name="bizEntityId" value="${param.bizEntityId}">
    <div class="form-actions">
        <c:if test="${entity.isNew()}">
            <button class="btn green" type="submit">保存</button>
        </c:if>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">备注内容</label>
                    <div class="controls">
                        <form:textarea path="remarkLog" class="form-control" rows="10"/>
                        <div class="help-block">特别提示：备注内容一旦提交后不可修改，请仔细填写提交</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <c:if test="${entity.isNew()}">
            <button class="btn green" type="submit">保存</button>
        </c:if>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>