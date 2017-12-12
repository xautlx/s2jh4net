<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<div class="alert alert-block alert-info fade in">
    <p>根据业务需要展示复杂的操作界面，并最终把选取结果回传给触发输入元素.</p>
    <p>
        <button class="btn blue" type="button" id="btnDropdownSelect" data-val="点击数据">模拟页面元素点击</button>
    </p>
</div>

<script type="text/javascript">
    $(function () {
        $("#btnDropdownSelect").click(function () {
            var $el = $(this);
            //获取所属弹出容器对象
            var $container = $el.closest(".container-dropdownselect");
            //获取弹出触发元素对象
            var $element = $container.data("element");
            //数据处理
            $element.val($el.attr("data-val"));
            //关闭容器界面显示
            $container.trigger("close");
        });
    });
</script>
</body>
</html>