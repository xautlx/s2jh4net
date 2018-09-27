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
                            <input type="text" name="search[BT_submitTime]"
                                   class="form-control" data-picker="date-range"
                                   data-input-icon="false"
                                   placeholder="提交时间段...">
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
                   "url" : "/dev/demo/reimbursement-request/stat/department/list",
                   "colModel" : [{
                       "label" : "部门ID",
                       "name" : "department.id",
                       "hidden" : true
                   }, {
                       "label" : "挂账部门",
                       "name" : "department.display",
                       "index" : "department.code_OR_department.name",
                       "width" : 100
                   }, {
                       "label" : "报销单数量",
                       "name" : "cnt",
                       "formatter": "integer"
                   }, {
                       "label" : "单笔最小报销金额",
                       "name" : "minTotalInvoiceAmount",
                       "formatter": "number"
                   }, {
                       "label" : "单笔最大报销金额",
                       "name" : "maxTotalInvoiceAmount",
                       "formatter": "number"
                   }, {
                       "label" : "报销总金额",
                       "name" : "totalInvoiceAmount",
                       "formatter": "number"
                   }],
                   sortname: "department.id",
                   sortorder: "desc",
                   multiselect: false,
                   subGrid: true,
                   subGridRowExpanded: function (subgridDivId, rowid) {
                       var rowdata=$(this).extDataGrid("getSelectedRowdata",rowid);

                       $(this).extDataGrid("initSubGrid", subgridDivId, rowid, {
                           url: "/dev/demo/reimbursement-request/stat/submit-user/list?search[EQ_submitDate]=" + rowdata.submitDate,
                           "colModel" : [ {
                               "label" : "用户ID",
                               "name" : "user.id",
                               "hidden" : true
                           }, {
                               "label" : "申请用户",
                               "name" : "user.display",
                               "index": "user.account.authUid",
                               "width" : 100,
                               "align" : "center",
                               "editable": false
                           }, {
                               "label" : "报销单数量",
                               "name" : "cnt",
                               "formatter": "integer"
                           }, {
                               "label" : "单笔最小报销金额",
                               "name" : "minTotalInvoiceAmount",
                               "formatter": "number"
                           }, {
                               "label" : "单笔最大报销金额",
                               "name" : "maxTotalInvoiceAmount",
                               "formatter": "number"
                           }, {
                               "label" : "报销总金额",
                               "name" : "totalInvoiceAmount",
                               "formatter": "number"
                           }],
                           multiselect: false,
                           subGrid: true,
                           subGridRowExpanded: function (subgridDivId, rowid) {
                               var rowdata2=$(this).extDataGrid("getSelectedRowdata",rowid);
                               console.log(rowdata2)
                               $(this).extDataGrid("initSubGrid", subgridDivId, rowid, {
                                   url: "/dev/demo/reimbursement-request/list?search[EQ_submitDate]=" + rowdata.submitDate +"&search[EQ_user.id]=" + rowdata2["user.id"],
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
                                   multiselect: false,
                                   inlineNav: false
                               });
                           }
                       });
                   }
                }'>
            </table>
        </div>
    </div>
</div>
