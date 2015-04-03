<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
	<div class="row">
		<div class="col-md-10">
			<div id="chart_timelyActive" class="chart" data-chart="echarts" style="height: 300px"></div>
		</div>
		<div class="col-md-2">
			<div class="well">
				<div class="row">
					<div class="col-md-12">
						平均日活跃用户：
						<fmt:formatNumber value="${datas.groupTimelyActivePerDay}" pattern="#,##0" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						平均周活跃用户：
						<fmt:formatNumber value="${datas.groupTimelyActivePerDay*7}" pattern="#,##0.#" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						平均月活跃用户：
						<fmt:formatNumber value="${datas.groupTimelyActivePerDay*30}" pattern="#,##0.#" />
					</div>
				</div>
			</div>
			<div class="well">
				<div class="row">
					<div class="col-md-12">
						新增用户数：
						<fmt:formatNumber value="${datas.groupTimelySignupTotal}" pattern="#,##0" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						日均新增用户数：
						<fmt:formatNumber value="${datas.groupTimelySignupPerDay*7}" pattern="#,##0.#" />
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						月均新增用户数：
						<fmt:formatNumber value="${datas.groupTimelySignupPerDay*30}" pattern="#,##0.#" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {
            $("#chart_timelyActive").data("echartsOptions", {
                legend : {
                    data : [ '活跃用户数变化趋势', '新增用户数变化趋势' ]
                },
                calculable : true,
                xAxis : [ {
                    type : 'time'
                } ],
                yAxis : [ {
                    type : 'value'
                } ],
                tooltip : {
                    trigger : 'item',
                    formatter : function(params) {
                        var date = new Date(params.value[0]);
                        data = date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                        return data + ' : ' + params.value[1];
                    }
                },
                series : [ {
                    name : '活跃用户数变化趋势',
                    type : 'line',
                    showAllSymbol : true,
                    symbolSize : function(value) {
                        return value[1];
                    },
                    data : (function() {
                        var data = [];
                        $.each($.parseJSON('<tags:json value="${datas.groupTimelyActive}"/>'), function(i, item) {
                            data.push([ moment(item.logonYearMonthDay, "YYYY-MM-DD").toDate(), item.cnt ]);
                        });
                        return data;
                    })()
                }, {
                    name : '新增用户数变化趋势',
                    type : 'line',
                    showAllSymbol : true,
                    symbolSize : function(value) {
                        return value[1];
                    },
                    data : (function() {
                        var data = [];
                        $.each($.parseJSON('<tags:json value="${datas.groupTimelySignup}"/>'), function(i, item) {
                            data.push([ moment(item.signupTime, "YYYY-MM-DD").toDate(), item.cnt + 2 ]);
                        });
                        return data;
                    })()
                } ]
            });
        })
    </script>
</body>
</html>