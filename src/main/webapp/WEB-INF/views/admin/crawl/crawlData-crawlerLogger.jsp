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
	<div class="form-horizontal form-bordered form-label-stripped form-validation control-label-lg">
		<div class="form-actions">
			<button type="button" class="btn default btn-post-url" data-confirm="确认 强制停止爬虫运行？"
				data-url="${ctx}/admin/crawl/crawl-data/crawler/shutdown">强制停止爬虫运行</button>
			<button id="toggler-crawl-logger-scroll" data-toggle="button" class="btn btn-default active pull-right" type="button">自动滚动开关</button>
			<button data-toggle="button" class="btn btn-default pull-right" type="button"
				onclick="$('#crawl-logger').toggleClass('crawl-logger-error')">
				异常日志
				<span id="crawl-logger-error-badge" class="badge badge-danger">0</span>
			</button>
		</div>
	</div>
	<div id="scroller-crawl-logger" class="scroller" data-height="stretch">
		<pre id="crawl-logger" class=""></pre>
	</div>
	<script type="text/javascript">
        (function() {
            if (typeof (EventSource) !== "undefined") {
                var streamUrl = "${crawlAppenderNettyContextURL}/stream";
                var source = new EventSource(streamUrl);
                var $scrollerCrawlLogger = $("#scroller-crawl-logger");
                var $crawlLogger = $("#crawl-logger");
                var $toggleLogger = $("#toggler-crawl-logger-scroll");
                var $errorBadge = $("#crawl-logger-error-badge");

                var printMessage = function(event, error) {
                    var pclass = "crawl-logger-line-info";
                    if (error) {
                        pclass = "crawl-logger-line-error";
                        $errorBadge.html(Number($errorBadge.html()) + 1);
                    }
                    $crawlLogger.append("<p class='"+pclass+"'>" + event.data + "</p>");
                    if ($toggleLogger.is(".active")) {
                        var scrollTo_int = $scrollerCrawlLogger.prop('scrollHeight') + 'px';
                        $scrollerCrawlLogger.slimScroll({
                            scrollTo : scrollTo_int
                        });
                    }
                }

                source.addEventListener("DEBUG", function(event) {
                    printMessage(event);
                });
                source.addEventListener("INFO", function(event) {
                    printMessage(event);
                });
                source.addEventListener("ERROR", function(event) {
                    printMessage(event, true);
                });

            } else {
                alert("Your browser does not support SSE, hence web-logback will not work properly");
            }

        })();
    </script>
</body>
</html>