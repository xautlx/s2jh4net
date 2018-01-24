<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<div class="row dataTables_wrapper">
    <div class="col-md-12">
        <table class="table table-striped table-hover table-bordered dataTable table-sorting">
            <thead>
            <tr>
                <th>消息标题</th>
                <th>消息摘要</th>
                <th data-sorting-name="publishTime">发布时间</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${pageData.content}">
                <tr>
                    <td><a href="javascript:;"
                           data-url="admin/profile/user-message-view/${item.id}?readed=${item.lastReadTime!=null}"
                           data-toggle="modal-ajaxify" title="查看消息">
                        <c:if test="${item.lastReadTime==null}">
                            <i class="fa fa-envelope-o"></i>
                        </c:if> ${item.title}
                    </a></td>
                    <td>${item.messageAbstract}</td>
                    <td>${item.publishTimeFormatted}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <!-- TODO
        <tags:pagination page="${pageData}"/>
        -->
    </div>
</div>