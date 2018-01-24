<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<form:form class="form-horizontal form-bordered form-label-stripped"
           data-validation='true' data-post-reload-grid="false"
           action="admin/auth/role/privileges">
    <input type="hidden" name="id" value="${param.id}"/>
    <c:if test="${true!=readonly}">
        <div class="form-actions">
            <button class="btn blue" type="submit">
                <i class="fa fa-check"></i> 保存
            </button>
            <button class="btn default" type="button" data-dismiss="modal">取消</button>
        </div>
    </c:if>
    <div class="row">
        <div class="col-md-12">
            <div class="portlet-body" id="admin-auth-role-privilege-list"></div>
        </div>
    </div>
    <c:if test="${true!=readonly}">
        <div class="form-actions right">
            <button class="btn blue" type="submit">
                <i class="fa fa-check"></i> 保存
            </button>
            <button class="btn default" type="button" data-dismiss="modal">取消</button>
        </div>
    </c:if>
    <script type="text/javascript">
        jQuery(document).ready(function () {
            var r2s = "${r2PrivilegeIds}".split(",");
            $("#admin-auth-role-privilege-list").ajaxJsonUrl("/admin/auth/privilege/list", function (data) {
                var $list = $(this);
                var index = 0;
                $.each(data, function (i, item) {
                    var codes = item.code.split(":");
                    var $row = $('<div class="row"></div>').appendTo($list);

                    $.each(codes, function (j, code) {
                        //计算是否初始化选中
                        var checked = "";
                        if (j == codes.length - 1 && Util.isArrayContainElement(r2s, item.id)) {
                            checked = " checked ";
                        }
                        //叶子节点才设置name属性并回传post提交
                        var checkbox = '<input type="checkbox" ' + (j == codes.length - 1 ? 'name="privilegeIds"' : '') + checked + ' style="margin-right: 10px" value="' + item.id + '">';
                        var el = "<label class='" + (checked != "" ? "label label-info" : "") + "'>" + checkbox + (code) + "</label>";
                        var $item = $('<div class="col-md-3 col-item"><div style="margin:5px;border-top: 1px solid #ddd;">' + el + '</div></div>').appendTo($row);
                        $item.attr("data-index", j);
                        $item.attr("data-text", code);

                        //计算当前节点的路径
                        var path = [];
                        for (var x = 0; x <= j; x++) {
                            path.push(codes[x]);
                        }
                        $item.attr("data-path", path.join(":"));
                    })
                })

                //计算隐藏同组重复项
                $list.find(" > .row").each(function (i, row) {
                    var $row = $(this);
                    var $prev = $row.prev(".row");
                    if ($prev.size() > 0) {
                        $row.find("> [data-index]:not(:last)").each(function (j, item) {
                            var $item = $(this);
                            var idx = $item.attr("data-index");
                            if ($prev.find("[data-index='" + idx + "']").attr("data-path") == $item.attr("data-path")) {
                                $item.html("");
                                $item.css("border-top", "0px");
                            }
                        })
                    }
                })

                $list.on("click", 'input[type="checkbox"]:not([name])', function () {
                    var $el = $(this);
                    var path = $el.closest("[data-path]").attr("data-path");
                    $list.find('[data-path^="' + path + ':"] input[type="checkbox"]').attr("checked", $el.is(":checked"));
                })
            });

        });
    </script>
</form:form>