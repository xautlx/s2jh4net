<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<form:form class="form-horizontal form-bordered form-label-stripped form-validation"
           action="/dev/demo/reimbursement-request/edit" method="post" modelAttribute="entity"
           data-editrulesurl="/admin/util/validate?clazz=${clazz}">
    <form:hidden path="id"/>
    <form:hidden path="version"/>
    <div class="form-actions">
        <button class="btn blue" type="submit" data-grid-reload="#grid-demo-reimbursement-request-index">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <h3 class="form-section">抬头信息</h3>
        <div class="row">
            <div class="col-md-4">
                <div class="form-group">
                    <label class="control-label">挂账部门</label>
                    <div class="controls">
                        <form:input path="department.id" class="form-control"/>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div class="form-group">
                    <label class="control-label">报销摘要说明</label>
                    <div class="controls">
                        <form:input path="abstractExplain" class="form-control"/>
                    </div>
                </div>
            </div>
        </div>
        <h3 class="form-section">行项信息</h3>
        <div class="row">
            <div class="col-md-12">
                <table class="table table-striped" data-dynamic-edit-table="true" data-rule-minlength="1">
                    <thead>
                    <tr>
                        <th>费用发生起始日期<span class="required">*</span></th>
                        <th>结束日期<span data-tooltips="留空表示与起始日期相同，单日费用"/></th>
                        <th>记账类型<span class="required">*</span></th>
                        <th>票据张数<span class="required">*</span></th>
                        <th>金额小计<span class="required">*</span></th>
                        <th>摘要说明</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${entity.reimbursementRequestItems}" varStatus="status">
                        <tr>
                            <form:hidden class="dynamic-row-operation"
                                         path="reimbursementRequestItems[${status.index}].extraAttributes['operation']"/>
                            <form:hidden class="dynamic-row-id"
                                         path="reimbursementRequestItems[${status.index}].reimbursementRequest.id"/>
                            <td><form:input path="reimbursementRequestItems[${status.index}].startDate"
                                            class="form-control" data-picker="date"
                                            required="true"
                                            data-rule-dateLT="reimbursementRequestItems[${status.index}].endDate"/></td>
                            <td><form:input path="reimbursementRequestItems[${status.index}].endDate"
                                            class="form-control"
                                            data-picker="date"
                                            data-rule-dateGT="reimbursementRequestItems[${status.index}].startDate"/></td>
                            <td><form:input path="reimbursementRequestItems[${status.index}].useType"
                                            class="form-control"
                                            required="true"/></td>
                            <td><form:input path="reimbursementRequestItems[${status.index}].invoiceCount"
                                            class="form-control"
                                            required="true" data-rule-digits="true"/></td>
                            <td><form:input path="reimbursementRequestItems[${status.index}].invoiceAmount"
                                            class="form-control"
                                            required="true" data-rule-number="true"/></td>
                            <td><form:input path="reimbursementRequestItems[${status.index}].useExplain"
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
</body>
</html>