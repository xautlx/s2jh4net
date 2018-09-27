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
                            <input type="text" name="search[CN_targets_OR_title_OR_message]" class="form-control input-large"
                                   placeholder="消息接受者 , 标题 , 消息内容...">
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
	               url: "/admin/aud/send-message-log/list",
                   colModel: [{
                       label: "流水号",
                       name: "id",
                       hidden: true
                   }, {
                       label: "消息接受者",
                       name: "targets",
                       width: 100,
                       align: "center",
                       editable: true
                   }, {
                       label: "标题",
                       name: "title",
                       width: 200,
                       align: "left",
                       editable: true
                   }, {
                       label: "消息摘要",
                       index: "message",
                       name: "messageAbstract",
                       width: 300,
                       align: "left"
                   }, {
                       label: "消息类型",
                       name: "messageType",
                       formatter: "select",
                       searchoptions: {
                           valueJson: ${messageTypeJson}
                       },
                       width: 80,
                       align: "center",
                       editable: true
                   }, {
                       label: "发送时间",
                       name: "sendTime",
                       formatter: "timestamp"
                   }],
                   multiselect: false,
                   addable: false,
                   viewurl: "/admin/aud/send-message-log/view"
                }'>
            </table>
        </div>
    </div>
</div>