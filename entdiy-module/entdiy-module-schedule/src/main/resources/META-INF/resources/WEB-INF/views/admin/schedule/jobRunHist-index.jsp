<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init"
                      data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_jobName_OR_nodeId]" class="form-control input-large"
                                   placeholder="任务名称、主机...">
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
	               url: "/admin/schedule/job-run-hist/list",
                   colModel: [{
                       label: "主机节点",
                       name: "nodeId",
                       width: 150,
                       align: "center"
                   }, {
                       label: "任务名称",
                       name: "jobName",
                       width: 250,
                       align: "left"
                   }, {
                       label: "触发时间",
                       name: "previousFireTime",
                       formatter: "timestamp"
                   }, {
                       label: "下次触发时间",
                       name: "nextFireTime",
                       formatter: "timestamp"
                   }, {
                       label: "触发次数",
                       name: "refireCount",
                       formatter: "integer",
                       hidden: true
                   }, {
                       label: "异常标识",
                       name: "exceptionFlag",
                       fixed: true,
                       formatter: "checkbox",
                       align: "center"
                   }, {
                       label: "执行结果",
                       name: "result",
                       width: 100,
                       align: "left"
                   }],
                   multiselect: false,
                   viewurl: "/admin/schedule/job-run-hist/view"
                }'>
            </table>
        </div>
    </div>
</div>