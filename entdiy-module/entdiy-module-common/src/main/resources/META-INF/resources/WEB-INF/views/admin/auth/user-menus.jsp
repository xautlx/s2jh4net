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
		<p>提示说明：用户的可见菜单列表是基于用户配置的关联角色，以及角色与权限的关联关系，自动匹配计算出来的。只需设置好用户和角色关联，角色和权限关联即可。如果需要调整菜单显示顺序，可以访问菜单配置功能变更设置相对排序号即可。</p>
	</div>
	<div class="row">
		<div class="col-md-6">
			<ul class="breadcrumb">
				<li class="active">用户菜单列表</li>
			</ul>
			<ul id="userMenuTree_${id}" class="ztree"></ul>
		</div>
		<div class="col-md-6">
			<ul class="breadcrumb">
				<li class="active">所有菜单列表</li>
			</ul>
			<ul id="allMenuTree_${id}" class="ztree"></ul>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {

            $.getJSON("${ctx}/admin/auth/user/menus/data?id=${id}", function(data) {
                $.fn.zTree.init($("#userMenuTree_${id}"), {
                    callback : {
                        onClick : function(event, treeId, treeNode) {
                            event.stopPropagation();
                            event.preventDefault();
                            return false;
                        }
                    },
                    data : {
                        simpleData : {
                            enable : true
                        }
                    }
                }, data);
            });

            $.getJSON("${ctx}/admin/sys/menu/data", function(data) {
                $.fn.zTree.init($("#allMenuTree_${id}"), {
                    callback : {
                        onClick : function(event, treeId, treeNode) {
                            event.stopPropagation();
                            event.preventDefault();
                            return false;
                        }
                    },
                    data : {
                        simpleData : {
                            enable : true
                        }
                    }
                }, data);
            });
        });
    </script>
</body>
</html>
