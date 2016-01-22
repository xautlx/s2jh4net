<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<form:form class="form-horizontal form-bordered form-label-stripped form-validation control-label-lg"
		action="${ctx}/admin/crawl/crawl-data/crawler/startup" modelAttribute="crawlConfig" method="post"
		data-editrulesurl="false" id="crawlForm">
		<div class="form-actions">
			<button class="btn blue" type="submit">
				<i class="fa fa-check"></i> 开始爬虫采集
			</button>
			<button type="button" class="btn default btn-post-url" data-confirm="确认 强制停止爬虫运行？"
				data-url="${ctx}/admin/crawl/crawl-data/crawler/shutdown">强制停止爬虫运行</button>
		</div>
		<div class="form-body">
			<div class="row">
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">强制重新抓取页面内容</label>
						<div class="controls controls-radiobuttons">
							<form:radiobuttons path="forceRefetch" items="${applicationScope.cons.booleanLabelMap}" class="form-control" />
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">强制重新解析页面内容</label>
						<div class="controls controls-radiobuttons">
							<form:radiobuttons path="forceReparse" items="${applicationScope.cons.booleanLabelMap}" class="form-control" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="control-label">并发抓取线程数</label>
						<div class="controls">
							<form:input path="threadNum" class="form-control" required="true" data-rule-min="1" data-rule-max="100" />
							<span class="help-block">对于速度快，没有反爬虫的站点可以根据机器性能设置较大；反正则设置较小一些</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">抓取访问最小间隔(秒)</label>
						<div class="controls">
							<form:input path="fetchMinInterval" class="form-control" required="true" data-rule-max="300" />
							<span class="help-block">有些站点做了一定反爬虫控制，如限制用户请求间隔不得太快，通过合理设置此参数来规避站点封锁</span>
						</div>
					</div>
				</div>
			</div>

			<div class="row" data-equal-height="false">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">种子URL</label>
						<div class="controls">
							<textarea name="urls" rows="6" class="form-control" />
							<span class="help-block">请填写符合下列正则表达式的URL列表，一行一个URL；留空表示把所有非200成功状态页面重新抓取解析</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">有效URL正则列表</label>
						<div class="controls">
							<c:forEach items="${crawlParseFilters}" var="item">
								<c:if test="${item.urlFilterRegex!=null}">
									<p class="form-control-static" title="${item.getClass().name}">${item.urlFilterRegex}</p>
								</c:if>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>

	<script type="text/javascript">
        $("#crawlForm").on("form-submit-success", function() {
            var $this = $(this);
            $this.closest(".tabbable").find(">.nav .tab-crawl-logger").click();
        })
    </script>
</body>
</html>