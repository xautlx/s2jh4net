<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运行记录详情</title>
</head>
<body>
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
</body>
</html>