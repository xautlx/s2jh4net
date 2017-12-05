<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限管理</title>
</head>
<body>
	<div class="note note-info">
		<p>提示说明：系统权限数据列表基于开发配置代码自动生成，无需手工维护。</p>
	</div>
	<div id="admin-auth-privilege-list"></div>
	<script type="text/javascript">
        jQuery(document).ready(function() {
            $("#admin-auth-privilege-list").ajaxJsonUrl(WEB_ROOT + "/admin/auth/privilege/list", function(data) {
                var $list = $(this);
                var index = 0;
                $.each(data, function(i, item) {
                    var codes = item.code.split(":");
                    var $row = $('<div class="row"></div>').appendTo($list);
                    $.each(codes, function(j, code) {
                        $row.append('<div class="col-md-3" data-index="'+j+'" data-text="'+code+'" ><div style="margin:5px;border-top: 1px solid #ddd;">' + code + '</div></div>');
                    })
                })

                $list.find(" > .row").each(function(i, row) {
                    var $row = $(this);
                    var $prev = $row.prev(".row");
                    if ($prev.size() > 0) {
                        $row.find("> [data-index]:not(:last)").each(function(j, item) {
                            var $item = $(this);
                            var idx = $item.attr("data-index");
                            if ($prev.find("[data-index='" + idx + "']").attr("data-text") == $item.attr("data-text")) {
                                $item.html("");
                                $item.css("border-top", "0px");
                            }
                        })
                    }
                })
            });

        });
    </script>
</body>
</html>