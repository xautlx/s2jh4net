<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped" data-validation='${validationRules}'
           action="/dev/demo/reimbursement-request/edit" method="post" modelAttribute="entity">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn blue" type="submit" data-grid-reload="#grid-demo-reimbursement-request-index">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="reset">
            <i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <h3 class="form-section">抬头信息</h3>
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label">挂账部门</label>
                    <div class="controls">
                        <form:select id="" path="department.id" class="form-control"
                                     data-toggle="dropdown-tree"
                                     data-url="/admin/auth/department/tree"
                                     data-fetch-all="true"
                                     multiple="false">
                            <form:option value="${entity.department.id}" label="${entity.department.display}"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label">报销摘要说明</label>
                    <div class="controls">
                        <form:input path="abstractExplain" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">报销凭证</label>
                    <div class="controls">
                        <ul class="list-group" data-fileuploader="receiptAttachmentFiles" data-multiple="true">
                            <c:forEach var="item" items="${entity.receiptAttachmentFiles}" varStatus="status">
                                <li class="list-group-item">
                                    <form:hidden id="" path="receiptAttachmentFiles[${status.index}].id"/>
                                    <form:hidden id="" path="receiptAttachmentFiles[${status.index}].accessUrl"/>
                                    <form:hidden id="" path="receiptAttachmentFiles[${status.index}].fileRealName"/>
                                    <form:hidden id="" path="receiptAttachmentFiles[${status.index}].fileLength"/>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <h3 class="form-section">行项信息</h3>
        <div class="row">
            <div class="col-md-12">
                <table class="table table-striped" data-dynamic-edit-table="true" data-rule-minlength="1"
                       data-validation='${subValidationRules}'>
                    <thead>
                    <tr>
                        <th>费用发生起始日期</th>
                        <th>结束日期</th>
                        <th>记账类型</th>
                        <th>票据张数</th>
                        <th>金额小计</th>
                        <th>摘要说明</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${entity.reimbursementRequestItems}" varStatus="status">
                        <tr>
                            <form:hidden id="" class="dynamic-row-id"
                                         path="reimbursementRequestItems[${status.index}].id"/>
                            <td><form:input id="" path="reimbursementRequestItems[${status.index}].startDate"
                                            class="form-control"
                                            data-rule-dateLT="reimbursementRequestItems[${status.index}].endDate"/></td>
                            <td><form:input id="" path="reimbursementRequestItems[${status.index}].endDate"
                                            class="form-control"
                                            data-rule-dateGT="reimbursementRequestItems[${status.index}].startDate"/></td>
                            <td><form:select id="" path="reimbursementRequestItems[${status.index}].useType"
                                             items="${useTypeMap}" class="form-control"
                                             cssStyle="min-width: 150px"/></td>
                            <td><form:input id="" path="reimbursementRequestItems[${status.index}].invoiceCount"
                                            class="form-control"/></td>
                            <td><form:input id="" path="reimbursementRequestItems[${status.index}].invoiceAmount"
                                            class="form-control"/></td>
                            <td><form:input id="" path="reimbursementRequestItems[${status.index}].useExplain"
                                            class="form-control"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn blue" type="submit" data-grid-reload="#grid-demo-reimbursement-request-index">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form:form>