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
                   url: "/admin/sys/config-property/list",
                   colModel: [{
                       label: "代码",
                       name: "propKey",
                       width: 120,
                       editable: true,
                       align: "left"
                   }, {
                       label: "名称",
                       name: "propName",
                       width: 100,
                       editable: true,
                   }, {
                       label: "简单属性值",
                       name: "simpleValue",
                       width: 80,
                       editable: true
                   }, {
                       label: "参数属性用法说明",
                       name: "propDescn",
                       sortable: false,
                       editable: true,
                       width: 200,
                       edittype: "textarea"
                   }],
                   editurl: "/admin/sys/config-property/edit",
                   fullediturl: "/admin/sys/config-property/edit-tabs",
                   delurl: "/admin/sys/config-property/delete"
                }'>
            </table>
        </div>
    </div>
</div>