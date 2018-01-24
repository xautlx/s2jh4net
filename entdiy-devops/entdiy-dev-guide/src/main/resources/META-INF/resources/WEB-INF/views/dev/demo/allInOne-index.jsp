<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="note note-info">
    <ul>
        <li>UI示例组件对应源码请直接参考对应代码：ui-feature-items.jsp和DocumentController</li>
        <li>点击元素后方的链接可查看组件的具体用法Javascript注释文档</li>
        <li>鼠标双击portlet标题区域可以切换单个展开或收拢：
            <a class="btn btn-xs blue" onclick="$('#docs-demo-list a.expand').click()">
                展开全部
            </a>
            <a class="btn btn-xs blue" onclick="$('#docs-demo-list a.collapse').click()">
                收拢全部
            </a>
        </li>
        <li>以弹出界面展现当前样例，以便验证不同组件在弹窗界面显示效果和兼容性：
            <a class="btn btn-xs blue" href="javascript:;" data-toggle="modal-ajaxify"
               data-url="/dev/demo/all-in-one/detail" data-width="90%" onclick="$('#docs-demo-list').remove()">
                弹窗展示
            </a>
        </li>
    </ul>
</div>

<!---->
<div id="docs-demo-list" data-toggle="ajaxify" data-url="/dev/demo/all-in-one/detail"></div>
