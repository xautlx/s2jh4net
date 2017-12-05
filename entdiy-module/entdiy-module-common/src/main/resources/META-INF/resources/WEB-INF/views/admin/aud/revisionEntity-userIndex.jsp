<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div class="row search-form-default">
    <div class="col-md-12">
        <form method="get" class="form-inline form-validation form-search form-search-init control-label-sm"
              data-grid-search="#grid-aud-revision-entity-user-index">
            <input type="hidden" name="search['NN_authUid']" value="true"/>
            <div class="form-group">
                <div class="controls controls-clearfix">
                    <input type="text" name="search['CN_controllerClassLabel_OR_controllerMethodLabel_OR_authUid']"
                           class="form-control input-xlarge" placeholder="处理类 , 处理方法 , 操作账号标识...">
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
<div class="row">
    <div class="col-md-12">
        <table id="grid-aud-revision-entity-user-index"></table>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        $("#grid-aud-revision-entity-user-index").data("gridOptions", {
            url: WEB_ROOT + '/admin/aud/revision-entity/user/list',
            colModel: [{
                label: '版本号',
                name: 'rev',
                width: 60,
                align: 'center'
            }, {
                label: '记录时间',
                name: 'revstmp',
                formatter: 'timestamp'
            }, {
                label: '处理类',
                name: 'controllerClassDisplay',
                index: 'controllerClassName_OR_controllerClassLabel',
                width: 250,
                align: 'left'
            }, {
                label: '处理方法',
                name: 'controllerMethodDisplay',
                index: 'controllerMethodName_OR_controllerMethodLabel',
                width: 150,
                align: 'center'
            }, {
                label: '操作账号标识',
                name: 'authUid',
                align: 'center',
                width: 80
            }, {
                label: '操作账号类型',
                name: 'authType',
                formatter: 'select',
                searchoptions: {
                    valueJsonString: '<tags:json value="${authTypeMap}"/>'
                },
                hidden: true,
                align: 'center',
                width: 60
            }, {
                label: '方法类型',
                name: 'controllerMethodType',
                width: 60,
                align: 'center'
            }, {
                label: 'entityClassName',
                name: 'entityClassName',
                width: 60,
                hidden: true,
                align: 'center'
            }],
            sortname: 'rev',
            navButtons: [{
                caption: "数据查看/对比",
                buttonicon: "fa-indent",
                onClickButton: function (rowids) {
                    var $grid = $(this);
                    var $grid = $(this).closest(".ui-jqgrid").find(".ui-jqgrid-btable:first");
                    var selectRows = $grid.getAtLeastOneSelectedItem();
                    if (selectRows) {
                        var rowdatas = $grid.getSelectedRowdatas();
                        var url = WEB_ROOT + '/admin/aud/revision-entity/compare';

                        var entityClassName = null;
                        var revs = [];
                        $.each(rowdatas, function (i, rowdata) {
                            revs.push(rowdata['rev']);
                            if (entityClassName == null) {
                                entityClassName = rowdata['entityClassName'];
                            } else {
                                if (rowdata['entityClassName'] != entityClassName) {
                                    entityClassName = false;
                                    return false;
                                }
                            }
                        });

                        if (entityClassName) {
                            url += "?clazz=" + entityClassName;
                            url += "&revs=" + revs.join(",");

                            $grid.popupDialog({
                                url: url,
                                title: "历史版本数据对比"
                            })
                        } else {
                            Global.notify("error", "选取行项数据类型不一致");
                        }
                    }
                },
                operationRows: 'multiple',
                showOnToolbar: true,
                showOnToolbarText: true
            }]
        });
    });
</script>
</body>
</html>
