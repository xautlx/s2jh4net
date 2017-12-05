<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>辅助管理功能</title>
</head>
<body>
	<div class="row">
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 缓存管理
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/cache-clear' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>为了系统运行效率，系统会基于Hibernate和Spring的Cache支持尽可能缓存数据</p>
								<p>此功能主要用于直接修改数据库数据后，通知缓存框架移除选取范围的缓存数据从而加载最新数据库数据.</p>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit">
								<i class="fa fa-check"></i> 刷新清空全部缓存数据
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 动态更新Logger日志级别
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/logger-update' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>此功能主要用于在应用运行过程中动态修改Logger日志级别从而实现在线Debug调试系统日志信息以便实时进行一些线上问题分析排查.</p>
								<p class="text-warning">在调低日志级别问题排查完毕后，最好把日志级别调整回预设较高级别以避免大量日志信息影响系统运行效率</p>
							</div>
							<div class="form-group">
								<label class="control-label">Logger Name</label>
								<div class="controls">
									<input type="text" name="loggerName" class="form-control" placeholder="root，特定package名称，特定logger名称等">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label">Logger Level</label>
								<div class="controls">
									<select name="loggerLevel">
										<option value="OFF">OFF</option>
										<option value="ERROR">ERROR</option>
										<option value="WARN">WARN</option>
										<option value="INFO">INFO</option>
										<option value="DEBUG">DEBUG</option>
										<option value="TRACE">TRACE</option>
										<option value="ALL">ALL</option>
									</select>
								</div>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit">
								<i class="fa fa-check"></i> 动态更新Logger日志级别
							</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 负载均衡验证
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="note note-info">
						<p>提供基本的HTTP Request和Session信息显示页面，用于在集群环境Failover切换不同主机检查确认服务是否正常，检查Session复制配置是否有效的实现了Session属性数据的无缝复制切换。</p>
					</div>
					<a class="btn blue" href="${ctx}/admin/util/load-balance-test" target="_blank">点击显示负载均衡信息页面</a>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> Druid数据源监控
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<div class="note note-info">
						<p>本项目采用由阿里开发维护的Druid数据库连接池。Druid能够提供强大的监控和扩展功能。Druid内置提供了一个StatViewServlet用于展示Druid的统计信息。</p>
					</div>
					<a class="btn blue" href="${ctx}/druid/" target="_blank">点击访问Druid数据源监控页面</a>
				</div>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 系统时间临时设置
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/systime/setup' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>此功能主要用于在开发测试阶段，临时“篡改”调整系统当前时间信息，以模拟系统运行在一段时间之后，然后执行定时任务等操作。</p>
								<p>调整系统时间会导致整个系统一直处于一个固定的设定时间，不会往前继续推进。因此，临时操作完成后及时“恢复系统时间”以恢复当前实时系统时间。</p>
								<p>为了避免遗忘执行手工恢复操作，在“临时调整系统时间”操作后，默认在10分钟后强制恢复为当前系统时间。</p>
							</div>
							<div class="form-group">
								<label class="control-label">指定临时系统时间</label>
								<div class="controls">
									<input type="text" name="time" class="form-control" data-picker="date-time" required="true">
								</div>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue" type="submit">
								<i class="fa fa-check"></i> 临时调整系统时间
							</button>
							<button class="btn blue btn-post-url" type="button" data-url="${ctx}/admin/util/systime/reset"
								data-confirm="false">恢复系统时间</button>
						</div>
					</form>
				</div>
			</div>
		</div>
		<div class="col-md-6">
			<div class="portlet box grey">
				<div class="portlet-title">
					<div class="caption">
						<i class="fa fa-reorder"></i> 消息服务监听器控制
					</div>
					<div class="tools">
						<a class="collapse" href="javascript:;"></a>
					</div>
				</div>
				<div class="portlet-body">
					<form class="form-horizontal form-bordered form-label-stripped form-validation"
						action='${ctx}/admin/util/systime/setup' method="post" data-editrulesurl="false">
						<div class="form-body" style="min-height: 250px">
							<div class="note note-info">
								<p>在开发dev_mode=true运行模式下，为了避免相互争夺接收消息，默认关闭消息监听服务。</p>
								<p>在开发和测试过程中，根据需要人工控制开启或关闭监听服务。</p>
							</div>
						</div>
						<div class="form-actions right">
							<button class="btn blue btn-post-url" type="button"
								data-url="${ctx}/admin/util/brokered-message/listener-state?state=startup" data-confirm="false">启动监听服务</button>
							<button class="btn blue btn-post-url" type="button"
								data-url="${ctx}/admin/util/brokered-message/listener-state?state=shutdown" data-confirm="false">关闭监听服务</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>