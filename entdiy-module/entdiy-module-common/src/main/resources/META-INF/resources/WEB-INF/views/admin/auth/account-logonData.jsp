<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="form-horizontal form-bordered form-label-stripped">
    <div class="form-body">
        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label">总计认证失败次数</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.logonFailureTimes}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">最近认证失败时间</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.lastLogonFailureTime}</p>
                    </div>
                </div>

            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="control-label">总计认证成功次数</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.logonSuccessTimes}</p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">最近认证成功时间</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.lastLogonSuccessTime}</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">最近认证失败次数</label>
                    <div class="controls">
                        <p class="form-control-static">${entity.lastFailureTimes}</p>
                        <span class="help-block">如果此数字较大，说明此账号在不断尝试登录认证，建议联系对应用户了解情况以排查系统被恶意攻击的可能性</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>