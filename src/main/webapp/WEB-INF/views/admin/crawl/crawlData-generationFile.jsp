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
		action="${ctx}/admin/crawl/crawl-data/generation-file/generate" modelAttribute="crawlConfig" method="post"
		data-editrulesurl="false" id="crawlForm">
		<div class="form-actions">
			<button class="btn blue" type="submit" id="submitId">
				<i class="fa fa-check"></i> 重新生成文件
			</button>
		</div>
		<div class="form-body">
			<div class="row" data-equal-height="false">
				<div class="col-md-12">
					<div class="form-group">
						<label class="control-label">站点分组</label>
						<div class="controls">
							<select name="bizSiteName" id="bizSiteName" class="form-control">
								<c:forEach items="${allSiteNameList}" var="item">
									<option value="${item }">${item }</option>
								</c:forEach>
							</select>
							<span class="help-block">请选择需要生成文件的站点分组，如果不选择，则所有站点分组均生成文件，需要等待较长时间</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label">已生成的文件列表</label>
						<div class="controls">
							<table class="table table-hover">
								<thead>
									<tr>
										<th>文件名称</th>
										<th>文件大小</th>
										<th>最后更新时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${fileList}" var="item">
										<tr>
											<td><a href="${ctx }/admin/crawl/crawl-data/generation-file/download?fileName=${item.fileName}">${item.fileName }</a></td>
											<td>${item.fileSize }</td>
											<td>${item.lastModified }</td>
											<td><a href="javascript:;" onclick="generateFile('${item.fileName}');">重新生成</a></td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form:form>
	<script type="text/javascript">
        function generateFile(fileName) {
            fileName = fileName.substring(0, fileName.length - 4);

            $("#bizSiteName").val(fileName);
            $("#submitId").submit();
        }
    </script>
</body>
</html>