<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<div class="row search-form-default">
    <div class="col-md-12">
        <form action="${ctx}/admin/profile/notify-message-list?readed=${param.readed}" method="get"
              class="form-inline form-validation form-search-init control-label-sm"
              data-div-search=".profile-notify-message-List">
            <div class="btn-group">
                <a class="btn btn-default ${empty param.readed?'active':''}"
                   href='#/admin/profile/notify-message' data-path='公告信息列表'>全部</a><a
                    class="btn btn-default ${param.readed=='no'?'active':''}"
                    href='#/admin/profile/notify-message?readed=no' data-path='未读公告信息列表'>未读</a><a
                    class="btn btn-default ${param.readed=='yes'?'active':''}"
                    href='#/admin/profile/notify-message?readed=yes' data-path='已读公告信息列表'>已读</a>
            </div>
            <div class="form-group search-group-btn hide">
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

<div class="profile-notify-message-List"></div>
</body>
</html>