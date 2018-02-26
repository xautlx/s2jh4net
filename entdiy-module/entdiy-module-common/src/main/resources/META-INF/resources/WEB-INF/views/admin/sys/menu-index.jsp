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
                            <input type="text" name="search[CN_name]" class="form-control input-large" placeholder="名称...">
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
                   url : "/admin/sys/menu/list",
                   colModel : [ {
                       label : "名称",
                       name : "name",
                       width : 150,
                       editable : true,
                       editoptions : {
                           spellto : "code"
                       },
                       align : "left"
                   }, {
                       label : "图标",
                       name : "style",
                       editable : true,
                       width : 80,
                       align : "center"
                   }, {
                       label : "菜单URL",
                       name : "url",
                       width : 200,
                       align : "left"
                   }, {
                       label : "展开",
                       name : "initOpen",
                       editable : true,
                       formatter : "checkbox"
                   }, {
                       label : "禁用",
                       name : "disabled",
                       editable : true,
                       formatter : "checkbox"
                   }, {
                       label : "备注说明",
                       name : "description",
                       width : 200,
                       hidden : true,
                       editable : true,
                       edittype : "textarea"
                   }],
                   multiselect: false,
                   addable: false,
                   subGridRowExpandedKey: "parent.id",
                   editurl : "/admin/sys/menu/edit",
                   delurl : "/admin/sys/menu/delete"
                }'>
            </table>
        </div>
    </div>
</div>