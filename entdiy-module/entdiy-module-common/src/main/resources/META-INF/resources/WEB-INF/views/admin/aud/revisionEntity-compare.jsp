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
	<div class="row">
		<div class="col-md-12">
			<div class="note note-info">
				<label> <input type="checkbox" id="showJustDiffChk" /> 只显示有差异数据
				</label>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<table class="table table-condensed  table-advance table-hover" id="revCompareTable">
				<thead>
					<tr>
						<th>属性</th>
						<c:forEach items="${entityRevisions}" var="item">
							<th>${item.revisionEntity.rev}版本数据<small>${item.revisionEntity.revstmp}</small></th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${revEntityProperties}" var="item">
						<tr>
							<td class="success">${item.name}</td>
							<c:forEach items="${item.values}" var="value">
								<td class="property-value">${value}</td>
							</c:forEach>
						</tr>

					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#revCompareTable tbody tr").each(function() {

                var $tr = $(this);
                var tempValue = null;
                $tr.find(".property-value").each(function() {
                    if (tempValue == null) {
                        tempValue = $.trim($(this).html());
                    } else {
                        if (tempValue != $.trim($(this).html())) {
                            $tr.attr("diff", true);
                            $tr.find(".property-value").addClass("warning");
                            return false;
                        }
                    }
                })
            });

            $("#showJustDiffChk").click(function() {
                var tbl = $("#revCompareTable");
                var trs = tbl.find("tbody tr");
                if (this.checked) {
                    trs.each(function() {
                        if ($(this).attr("diff")) {
                            $(this).show();
                        } else {
                            $(this).hide();
                        }
                    });
                } else {
                    trs.each(function() {
                        $(this).show();
                    });
                }
            });
        });
    </script>
</body>
</html>