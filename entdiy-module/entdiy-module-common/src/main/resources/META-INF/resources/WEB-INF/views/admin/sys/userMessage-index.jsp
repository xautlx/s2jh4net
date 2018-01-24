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
                            <input type="text" name="search[CN_propKey_OR_propName_OR_simpleValue]"
                                   class="form-control input-large"
                                   placeholder="代码，名称，参数值...">
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
                   url: "/admin/sys/user-message/list?search[EQ_targetUser]=${param.siteUserId}",
                   colModel: [{
                       label: "目标用户",
                       name: "targetUser.display",
                       name: "targetUser.id",
                       editable: true,
                       width: 100,
                       align: "left"
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
                       label: "发布时间",
                       name: "createdDate",
                       formatter: "timestamp",
                       editable: true
                   }, {
                       label: "到期时间",
                       name: "expireTime",
                       formatter: "timestamp",
                       editable: true
                   }, {
                       label: "总计阅读次数",
                       name: "readTotalCount",
                       width: 60,
                       formatter: "integer"
                   }, {
                       label: "首次阅读时间",
                       name: "firstReadTime",
                       formatter: "timestamp"
                   }, {
                       label: "最后阅读时间",
                       name: "lastReadTime",
                       formatter: "timestamp"
                   }],
                   multiselect: false,
                   inlineNav: false,
                   addable: false,
                   fullediturl: "/admin/sys/user-message/edit"
                }'>
            </table>
        </div>
    </div>
</div>