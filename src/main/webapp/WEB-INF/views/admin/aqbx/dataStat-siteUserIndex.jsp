<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<body>
	<div class="form-horizontal form-bordered form-label-stripped">
		<div class="portlet gren">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i> 总体统计
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="row">
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">注册用户数</label>
							<div class="controls">
								<p class="form-control-static">${datas.countAll}</p>
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">创建托管账户用户数</label>
							<div class="controls">
								<p class="form-control-static">${datas.countAccount}</p>
							</div>
						</div>
					</div>
					<div class="col-md-4">
						<div class="form-group">
							<label class="control-label">认证用户数</label>
							<div class="controls">
								<p class="form-control-static">${datas.countVerified}</p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="portlet gren">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i> 认证用户构成
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="row">
					<div class="col-md-4">
						<div id="chart_sex" class="chart" data-chart="echarts" style="height: 300px"></div>
					</div>
					<div class="col-md-4">
						<div id="chart_educationLevel" class="chart" data-chart="echarts" style="height: 300px"></div>
					</div>
					<div class="col-md-4">
						<div id="chart_enrollmentDate" class="chart" data-chart="echarts" style="height: 300px"></div>
					</div>
				</div>
			</div>
		</div>
		<div class="portlet gren">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-reorder"></i> 分时统计
				</div>
				<div class="tools">
					<a class="collapse" href="javascript:;"></a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="row search-form-default">
					<div class="col-md-12">
						<form action="${ctx}/admin/p2p/data-stat/site-user/timely" method="get"
							class="form-inline form-validation form-search form-search-init form-search-auto"
							data-div-search="#data-stat-site-user-timely">
							<div class="form-group">
								<label class="control-label">统计时间段</label>
								<div class="controls">
									<input class="form-control input-large" name="activeFromTo" type="text" data-toggle="daterangepicker"
										data-date-scope="beforeNow">
								</div>
							</div>
							<div class="form-group search-group-btn hide">
								<button class="btn green" type="submmit">
									<i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
								</button>
								<button class="btn default" type="reset">
									<i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
								</button>
							</div>
						</form>
					</div>
				</div>
				<div id="data-stat-site-user-timely"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
        $(function() {

            $("#chart_sex").data("echartsOptions", {
                title : {
                    text : '性别构成'
                },
                xAxis : [ {
                    type : 'value'
                } ],
                yAxis : [ {
                    type : 'category',
                    data : [ '男', '女' ]
                } ],
                series : [ {
                    type : 'bar',
                    itemStyle : {
                        normal : {
                            label : {
                                show : true,
                                position : 'right',
                                formatter : '{c}'
                            }
                        }
                    },
                    data : [ "${datas.groupCountSex.sex1==null?0:datas.groupCountSex.sex1.cnt}", "${datas.groupCountSex.sex2==null?0:datas.groupCountSex.sex2.cnt}" ]
                } ]
            });

            $("#chart_educationLevel").data(
                    "echartsOptions",
                    {
                        title : {
                            text : '学历构成'
                        },
                        xAxis : [ {
                            type : 'value'
                        } ],
                        yAxis : [ {
                            type : 'category',
                            data : [ '本科', '专科', '硕士', '博士' ]
                        } ],
                        series : [ {
                            type : 'bar',
                            itemStyle : {
                                normal : {
                                    label : {
                                        show : true,
                                        position : 'right',
                                        formatter : '{c}'
                                    }
                                }
                            },
                            data : [ "${datas.groupCountEducationLevel.DZ==null?0:datas.groupCountEducationLevel.DZ.cnt}",
                                    "${datas.groupCountEducationLevel.BK==null?0:datas.groupCountEducationLevel.BK.cnt}",
                                    "${datas.groupCountEducationLevel.YJS==null?0:datas.groupCountEducationLevel.YJS.cnt}",
                                    "${datas.groupCountEducationLevel.BS==null?0:datas.groupCountEducationLevel.BS.cnt}" ]
                        } ]
                    });

            $("#chart_enrollmentDate").data("echartsOptions", {
                title : {
                    text : '入学年Top5'
                },
                xAxis : [ {
                    type : 'value'
                } ],
                yAxis : [ {
                    type : 'category',
                    data : (function() {
                        return $.map($.parseJSON('<tags:json value="${datas.groupCountEnrollmentDate}"/>'), function(item) {
                            return item.enrollmentDate;
                        });
                    })()
                } ],
                series : [ {
                    type : 'bar',
                    itemStyle : {
                        normal : {
                            label : {
                                show : true,
                                position : 'right',
                                formatter : '{c}'
                            }
                        }
                    },
                    data : (function() {
                        return $.map($.parseJSON('<tags:json value="${datas.groupCountEnrollmentDate}"/>'), function(item) {
                            return item.cnt;
                        });
                    })()
                } ]
            });
        })
    </script>
</body>
</html>