<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<form:form class="form-horizontal form-bordered form-label-stripped" method="post"
		   modelAttribute="entity" data-validation='${validationRules}'
		   action="admin/sys/notify-message/edit">
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<div class="form-actions">
		<button class="btn green" type="submit">保存</button>
		<button class="btn default" type="button" data-dismiss="modal">取消</button>
	</div>
	<div class="form-body">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label class="control-label">标题</label>
					<div class="controls">
						<form:input path="title" class="form-control" />
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">消息内容</label>
			<div class="controls">
				<form:textarea path="message" class="form-control" data-htmleditor="kindeditor" data-height="300px" />
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">生效时间</label>
					<div class="controls">
						<form:input path="publishTime" class="form-control" data-picker="date-time" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">过期时间</label>
					<div class="controls">
						<form:input path="expireTime" class="form-control" data-picker="date-time" />
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">排序号</label>
					<div class="controls">
						<form:input path="orderRank" class="form-control" />
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="form-group">
					<label class="control-label">类型</label>
					<div class="controls">
						<form:select path="type" class="form-control" items="${messageTypeMap }" />
					</div>
				</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">显示平台设置</label>
			<div class="controls">
				<form:checkboxes items="${platformMap}" path="platformSplit" />
				<span class="help-block">未勾选表示默认全部</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">APP弹出提示内容</label>
			<div class="controls">
				<form:input path="notification" class="form-control" />
				<span class="help-block">给诸如Android，iOS等移动设备提供系统通知消息的内容</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">用户标识列表</label>
			<div class="controls">
				<form:input path="audienceAlias" class="form-control" data-select2-type="tags"
							data-url="admin/auth/user/tags" />
				<span class="help-block">定向发送给特定用户列表，输入用户信息提示选取</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">消息目标属性AND交集</label>
			<div class="controls">
				<form:input path="audienceAndTags" class="form-control" data-select2-type="tags" />
				<span class="help-block">
						用标签来进行大规模的设备属性、用户属性分群，各元素之间为AND取交集。<br>没有值=全部，其他为数据字典项逗号分隔组合，如：student, school_01
					</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label">消息目标属性OR并集</label>
			<div class="controls">
				<form:input path="audienceTags" class="form-control" data-select2-type="tags" />
				<span class="help-block">
						用标签来进行大规模的设备属性、用户属性分群，各元素之间为OR取并集。<br>没有值=全部，其他为数据字典项逗号分隔组合，如：student, teacher
					</span>
			</div>
		</div>
	</div>
	<div class="form-actions right">
		<button class="btn green" type="submit">保存</button>
		<button class="btn default" type="button" data-dismiss="modal">取消</button>
	</div>
</form:form>