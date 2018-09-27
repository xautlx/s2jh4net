<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init" data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text"
                                   name="search[CN_controllerClassName_OR_controllerMethodName_OR_controllerClassLabel_OR_controllerMethodLabel_OR_operationUserName]"
                                   class="form-control input-xlarge" placeholder="处理类 , 处理方法 , 操作账号标识...">
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
	               url : "/admin/aud/revision-entity/user/list",
                   colModel : [ {
                       label : "版本号",
                       name : "rev",
                       width : 60,
                       align : "center"
                   }, {
                       label : "记录时间",
                       name : "revstmp",
                       formatter : "timestamp"
                   }, {
                       label : "操作对象",
                       name : "entityClassName",
                       width : 100,
                       align : "center"
                   }, {
                       label : "处理类",
                       name : "controllerClassDisplay",
                       index : "controllerClassName_OR_controllerClassLabel",
                       width : 250,
                       align : "left"
                   }, {
                       label : "处理方法",
                       name : "controllerMethodDisplay",
                       index : "controllerMethodName_OR_controllerMethodLabel",
                       width : 100,
                       align : "center"
                   }, {
                       label : "操作账号标识",
                       name : "operationUserName",
                       align : "center",
                       width : 80
                   }],
                   sortname: "rev",
                   navButtons: [{
                       caption: "数据查看/对比",
                       buttonicon: "fa-indent",
                       onClickButton: function (rowids) {
                           var that = this;
                           var rowdatas = that.getSelectedRowdatas();
                           var url = "/admin/aud/revision-entity/compare";

                           var entityClassName = null;
                           var revs = [];
                           $.each(rowdatas, function(i, rowdata) {
                               revs.push(rowdata["rev"]);
                               if (entityClassName == null) {
                                   entityClassName = rowdata["entityClassName"];
                               } else {
                                   if (rowdata["entityClassName"] != entityClassName) {
                                       entityClassName = false;
                                       return false;
                                   }
                               }
                           });

                           if (entityClassName) {
                               url += "?clazz=" + entityClassName;
                               url += "&revs=" + revs.join(",");

                               that.modalShow(url, "版本数据查看/对比");
                           } else {
                               Global.notify("error", "选取行项数据类型不一致");
                           }
                       },
                       operationRows: "multiple",
                       showOnToolbar: true,
                       showOnToolbarText: true
                   }]
                }'>
            </table>
        </div>
    </div>
</div>