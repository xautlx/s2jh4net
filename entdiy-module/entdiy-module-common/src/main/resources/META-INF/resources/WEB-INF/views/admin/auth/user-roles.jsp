<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:choose>
    <c:when test="${root}">
        <div class="row">
            <div class="col-md-12">
                <div class="note note-info">
                    <p>系统超级账号，无需角色配置，默认拥有所有角色权限</p>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true' data-post-reload-grid="false"
                   action="admin/auth/user/edit">
            <form:hidden path="id"/>
            <form:hidden path="version"/>
            <div class="form-actions">
                <button class="btn green" type="submit">保存</button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
            <div class="form-body">
                <div class="row" data-equal-height="false">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">关联角色</label>
                            <div class="controls">
                                <form:select path="roleIds" items="${roles}" itemValue="id" itemLabel="name"
                                             class="form-control"
                                             data-toggle="double-multi-select" data-height="350px"
                                             data-selectable-header="可选角色列表" data-selection-header="已选角色列表"/>
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
    </c:otherwise>
</c:choose>