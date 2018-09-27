<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init"
                      data-validation='true'>
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_name]" class="form-control input-large" placeholder="名称...">
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
                   url: "/admin/sys/data-dict/list",
                   colModel: [{
                       label: "主要数据",
                       name: "primaryValue",
                       editable: true,
                       editoptions: {
                           spellto: "primaryKey"
                       },
                       width: 150
                   }, {
                       label: "主标识",
                       name: "primaryKey",
                       editable: true,
                       width: 100
                   }, {
                       label: "次标识",
                       name: "secondaryKey",
                       editable: true,
                       width: 50
                   }, {
                       label: "次要数据",
                       name: "secondaryValue",
                       hidden: true,
                       editable: true,
                       width: 50
                   }, {
                       label: "禁用",
                       name: "disabled",
                       editable: true,
                       formatter: "checkbox"
                   }, {
                       label: "大文本数据",
                       name: "richTextValue",
                       width: 200,
                       hidden: true,
                       editable: true,
                       edittype: "textarea"
                   }],
                   subGridRowExpandedKey: "parent.id",
                   editurl: "/admin/sys/data-dict/edit",
                   fullediturl: "/admin/sys/data-dict/edit",
                   delurl: "/admin/sys/data-dict/delete"
                }'>
            </table>
        </div>
    </div>
</div>