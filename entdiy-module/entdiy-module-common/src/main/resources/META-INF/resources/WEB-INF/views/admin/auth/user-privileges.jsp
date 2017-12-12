<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="note note-info">
		<p>
			提示说明：以下列表显示系统所有权限数据项，并基于用户关联角色合并计算用户关联的权限列表，当前用户拥有的权限项高亮显示为
			<label class='label label-success'>已关联权限</label>
			示意效果。
		</p>
	</div>
	<div class="form-horizontal form-bordered form-label-stripped">
		<div class="row">
			<div class="col-md-12">
				<div class="portlet-body" id="admin-auth-user-privilege-list"></div>
			</div>
		</div>
		<script type="text/javascript">
            jQuery(document).ready(function() {
                var r2s = "${r2PrivilegeIds}".split(",");
                $("#admin-auth-user-privilege-list").ajaxJsonUrl("/admin/auth/privilege/list", function(data) {
                    var $list = $(this);
                    var index = 0;
                    $.each(data, function(i, item) {
                        var codes = item.code.split(":");
                        var $row = $('<div class="row"></div>').appendTo($list);

                        $.each(codes, function(j, code) {
                            //计算是否初始化选中
                            var checked = (j == codes.length - 1 && Util.isArrayContainElement(r2s, item.id));
                            var el = "<label class='" + (checked ? "label label-success" : "text-muted") + "'>" + (code) + "</label>";
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
                    $list.find(" > .row").each(function(i, row) {
                        var $row = $(this);
                        var $prev = $row.prev(".row");
                        if ($prev.size() > 0) {
                            $row.find("> [data-index]:not(:last)").each(function(j, item) {
                                var $item = $(this);
                                var idx = $item.attr("data-index");
                                if ($prev.find("[data-index='" + idx + "']").attr("data-path") == $item.attr("data-path")) {
                                    $item.html("");
                                    $item.css("border-top", "0px");
                                }
                            })
                        }
                    })

                    $list.on("click", 'input[type="checkbox"]:not([name])', function() {
                        var $el = $(this);
                        var path = $el.closest("[data-path]").attr("data-path");
                        $list.find('[data-path^="' + path + ':"] input[type="checkbox"]').attr("checked", $el.is(":checked"));
                    })
                });

            });
        </script>
	</div>
</body>
</html>
