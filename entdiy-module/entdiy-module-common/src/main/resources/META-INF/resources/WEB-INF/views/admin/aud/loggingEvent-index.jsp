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
                            <input type="text" name="search[CN_loggerName_OR_formattedMessage]" class="form-control input-large"
                                   placeholder="日志名称 , 消息内容...">
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
	               url : "/admin/aud/logging-event/list",
                   colModel : [ {
                       label : "流水号",
                       name : "id",
                       hidden : true
                   }, {
                       label : "状态",
                       name : "state",
                       formatter : "select",
                       searchoptions : {
                           valueJson : ${stateJson}
                       },
                       width : 80,
                       align : "center",
                       editable : true
                   }, {
                       label : "等级",
                       name : "levelString",
                       width : 50,
                       align : "center"
                   }, {
                       label : "时间",
                       name : "timestampDate",
                       index : "timestmp",
                       formatter : "timestamp"
                   }, {
                       label : "日志名称",
                       name : "loggerName",
                       width : 200,
                       align : "left"
                   }, {
                       label : "日志信息",
                       name : "formattedMessage",
                       width : 300,
                       align : "left"
                   }, {
                       label : "触发类",
                       name : "callerClass",
                       width : 300,
                       align : "left"
                   }, {
                       label : "触发方法",
                       name : "callerMethod",
                       width : 150,
                       align : "center"
                   }, {
                       label : "触发行数",
                       name : "callerLine",
                       width : 40,
                       align : "center"
                   } ],
                   addable : false,
                   fullediturl : "/admin/aud/logging-event/edit"
                }'>
            </table>
        </div>
    </div>
</div>