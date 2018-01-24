<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="note note-info">
    <h4 class="block">提示说明</h4>
    <p>Dashboard内容需要根据项目业务需求定制设计开发</p>
    <p>Your code goes here...</p>
</div>
<c:if test="${devMode}">
    <!-- 开发模式运行，加载开发指南页面，请确保maven定义了dev依赖组件，否则抛出404 -->
    <div class="portlet light bordered" style="padding: 0px">
        <div class="portlet-title" style="padding: 5px">
            <div class="caption">
                <i class="fa fa-bug font-grey-gallery"></i>
                <span class="caption-subject bold font-grey-gallery uppercase"> 开发指南 </span>
                <span class="caption-helper">以下内容只有在开发模式(dev.mode=true)才会显示，供开发人员参考</span>
            </div>
            <div class="tools">
                <a href="" class="collapse"> </a>
                <a href="" class="remove"> </a>
            </div>
        </div>
        <div class="portlet-body">
            <div data-toggle="ajaxify" data-url="/dev/dashboard"></div>
        </div>
    </div>
</c:if>