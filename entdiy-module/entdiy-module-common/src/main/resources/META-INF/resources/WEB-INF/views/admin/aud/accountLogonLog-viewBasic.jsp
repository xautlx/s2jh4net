<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="form-horizontal form-bordered form-label-stripped">
    <div class="form-actions">
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
    <div class="form-body">
        <div class="row">
            <div class="col-md-12">
                <div class="form-group">
                    <label class="control-label">登录标识</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.account.display}
                            <a title="登录账号管理" class="btn btn-xs" data-toggle="modal-ajaxify"
                               href="javascript:;" data-url="admin/auth/account/edit-tabs?id=${entity.account.id}">
                                登录账号管理
                            </a>
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">登录时间</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.logonTime}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">登出时间</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.logoutTime}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">登录时长</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.logonTimeLengthFriendly}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">登录次数</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${lentity.ogonTimes}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">userAgent</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.userAgent}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">xforwardFor</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.xforwardFor}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">localAddr</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.localAddr}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">localName</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.localName}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">localPort</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.localPort}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">remoteAddr</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.remoteAddr}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">remoteHost</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.remoteHost}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">remotePort</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.remotePort}]
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">serverIP</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.serverIP}
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label">Session ID</label>
                    <div class="controls">
                        <p class="form-control-static">
                            ${entity.httpSessionId}
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="form-actions right">
        <button class="btn default" type="button" data-dismiss="modal">取消</button>
    </div>
</div>