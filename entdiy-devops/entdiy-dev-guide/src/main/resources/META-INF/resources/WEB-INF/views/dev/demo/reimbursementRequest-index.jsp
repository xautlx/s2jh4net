<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="row">
        <div class="col-md-12">
            <div class="search-group-form">
                <form method="get" class="form-inline control-label-sm form-search-init"
                      data-validation='true'>
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text"
                                   name="search[CN_user.account.authUid_OR_department.name]"
                                   class="form-control input-large"
                                   placeholder="登录账号，部门名称...">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <select name="search[IN_department.id]"
                                    class="form-control input-medium"
                                    data-toggle="dropdown-tree"
                                    data-placeholder="选择部门..."
                                    data-url="/admin/auth/department/tree"
                                    data-fetch-all="true"
                                    data-parent-name="search[EQ_parent.id]"
                                    multiple="true">
                            </select>
                        </div>
                    </div>
                    <div class="form-group search-group-btn">
                        <button class="btn green" type="submmit">
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
                   "url" : "/dev/demo/reimbursement-request/list",
                   "colModel" : [ {
                       "label" : "流水号",
                       "name" : "id",
                       "hidden" : true
                   }, {
                       "label" : "申请用户",
                       "name" : "user.display",
                       "index": "user.account.authUid",
                       "width" : 100,
                       "align" : "center",
                       "editable": false
                   }, {
                       "label" : "挂账部门",
                       "name" : "department.display",
                       "index" : "department.code_OR_department.name",
                       "width" : 100,
                       "align" : "center",
                       "editable": false
                   }, {
                       "label" : "提交时间",
                       "name" : "submitTime",
                       "formatter": "timestamp",
                       "editable": true
                   }, {
                       "label" : "挂账类型",
                       "name" : "useType",
                       "formatter": "select",
                       "searchoptions": {
                           "valueJson": ${useTypeJson}
                       },
                       "width" : 100,
                       "align" : "center",
                       "editable": true
                   }, {
                       "label" : "摘要说明",
                       "name" : "abstractExplain",
                       "width" : 200,
                       "editable": true
                   }, {
                       "label" : "报销总金额",
                       "name" : "totalInvoiceAmount",
                       "formatter": "number"
                   }, {
                       "label" : "审批完结",
                       "name" : "auditComplete",
                       "formatter": "checkbox",
                       "editable": true
                   } ],
                   "postData": {
                      "search[FETCH_user]" : "INNER",
                      "search[FETCH_user.account]" : "INNER.INNER",
                      "search[FETCH_department]" : "INNER"
                   },
                   "validation":${validationRules},
                   "editurl" : "/dev/demo/reimbursement-request/edit",
                   "delurl" : "/dev/demo/reimbursement-request/delete",
                   "fullediturl" : "/dev/demo/reimbursement-request/edit-tabs"
                }'>
            </table>
        </div>
    </div>
</div>
