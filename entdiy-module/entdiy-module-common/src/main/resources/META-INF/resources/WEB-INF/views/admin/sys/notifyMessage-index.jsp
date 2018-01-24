<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="row">
        <div class="col-md-12">
            <div class="search-group-form">
                <form method="get" class="form-inline form-search-init"
                      data-validation='true'>
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_title_OR_message]" class="form-control input-large"
                                   placeholder="标题，内容...">
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
                   url: "/admin/sys/notify-message/list",
                   colModel: [{
                       label: "阅读人数",
                       name: "readUserCount",
                       formatter: "integer"
                   }, {
                       label: "标题",
                       name: "title",
                       editable: true,
                       align: "left"
                   }, {
                       label: "消息摘要",
                       name: "messageAbstract",
                       index: "externalLink_OR_htmlContent",
                       align: "left"
                   }, {
                       label: "平台设置",
                       name: "platform",
                       hidden: true,
                       width: 60,
                   }, {
                       label: "消息目标列表",
                       name: "audienceTags",
                       hidden: true,
                       width: 60,
                   }, {
                       label: "消息目标组合",
                       name: "audienceAndTags",
                       hidden: true,
                       width: 60,
                   }, {
                       label: "用户标识列表",
                       name: "audienceAlias",
                       hidden: true,
                       width: 60,
                   }, {
                       label: "排序号",
                       name: "orderRank",
                       width: 60,
                       formatter: "integer",
                       editable: true,
                       align: "center"
                   }, {
                       label: "生效时间",
                       name: "publishTime",
                       formatter: "timestamp",
                       editable: true
                   }, {
                       label: "到期时间",
                       name: "expireTime",
                       formatter: "timestamp",
                       editable: true
                   }],
                   inlineNav: {
                       add: false
                   },
                   multiselect: true,
                   editurl: "/admin/sys/notify-message/edit",
                   fullediturl: "/admin/sys/notify-message/edit",
                   delurl: "/admin/sys/notify-message/delete",
                   subGrid: true,
                   subGridRowExpanded: function (subgridDivId, rowid) {
                       $(this).extDataGrid("initSubGrid", subgridDivId, rowid, {
                           url: "/admin/sys/notify-message/read-list?search[EQ_notifyMessage.id]=" + rowid,
                           colModel: [{
                               label: "阅读用户",
                               name: "readUser.display",
                               index: "readUser.id",
                               width: 150
                           }, {
                               label: "首次阅读时间",
                               name: "firstReadTime",
                               formatter: "timestamp"
                           }, {
                               label: "最后阅读时间",
                               name: "lastReadTime",
                               formatter: "timestamp"
                           }, {
                               label: "总计阅读次数",
                               name: "readTotalCount",
                               formatter: "integer",
                               align: "center"
                           }],
                           inlineNav: false
                       });
                   }
                }'>
            </table>
        </div>
    </div>
</div>