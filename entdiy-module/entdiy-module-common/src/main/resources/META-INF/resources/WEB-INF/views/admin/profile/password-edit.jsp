<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<form class="form-horizontal form-bordered form-label-stripped" method="post"
      action="/account/profile/password" data-validation='true'>
    <div class="form-body">
        <div class="form-group">
            <label class="control-label">输入原密码</label>
            <div class="controls">
                <input type="password" name="oldpasswd" class="form-control" required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">输入新密码</label>
            <div class="controls">
                <input type="password" name="newpasswd" class="form-control" required="true">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label">确认新密码</label>
            <div class="controls">
                <input type="password" name="cfmpasswd" class="form-control" required="true"
                       data-rule-equalToByName="newpasswd">
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn blue" type="submit" data-post-dismiss="modal">
            <i class="fa fa-check"></i> 保存
        </button>
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</form>