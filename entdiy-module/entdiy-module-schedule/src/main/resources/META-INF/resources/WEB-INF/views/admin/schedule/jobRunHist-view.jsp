<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="form-horizontal form-bordered form-label-stripped">
    <div class="form-group">
        <label class="control-label">执行结果</label>
        <div class="controls">
            <pre>${entity.result}</pre>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label">异常标识</label>
        <div class="controls">
            <p class="form-control-static">
                ${entity.exceptionFlag}
            </p>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label">异常日志</label>
        <div class="controls">
            <pre>${entity.exceptionStack}</pre>
        </div>
    </div>
</div>