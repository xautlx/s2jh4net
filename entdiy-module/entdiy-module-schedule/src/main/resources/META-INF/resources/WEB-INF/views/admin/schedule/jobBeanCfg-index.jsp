<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="search-group">
    <div class="search-group-form">
        <div class="row">
            <div class="col-md-12">
                <form method="get" class="form-inline form-search-init"
                      data-validation="true">
                    <div class="form-group">
                        <div class="controls controls-clearfix">
                            <input type="text" name="search[CN_jobClass_OR_description]" class="form-control input-large"
                                   placeholder="类名，描述...">
                        </div>
                    </div>
                    <div class="form-group search-group-btn">
                        <button class="btn green" type="submit">
                            <i class="m-icon-swapright m-icon-white"></i>&nbsp; 查&nbsp;询
                        </button>
                        <button class="btn default" type="reset">
                            <i class="fa fa-undo"></i>&nbsp; 重&nbsp;置
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="search-group-target"
                   grid-options='{
	               url: "/admin/schedule/job-bean-cfg/list",
                   colModel: [{
                       label: "任务类全名",
                       name: "jobClass",
                       width: 200,
                       editable: true,
                       editoptions: {
                           placeholder: "新添加计划任务不会立刻安排作业,需要重启应用服务器才能生效"
                       }
                   }, {
                       label: "CRON表达式",
                       name: "cronExpression",
                       width: 100,
                       editable: true,
                       align: "right"
                   }, {
                       label: "自动初始运行",
                       name: "autoStartup",
                       formatter: "checkbox",
                       editable: true,
                       editoptions: {
                           title: "是否随应用启动之后自动部署运行任务，禁用后需要手工启动任务运行"
                       }
                   }, {
                       label: "启用历史记录",
                       name: "logRunHist",
                       formatter: "checkbox",
                       editable: true,
                       editoptions: {
                           title: "关键任务建议启用历史记录，对非关键任务且运行频率较高考虑性能因素建议关闭"
                       }
                   }, {
                       label: "集群运行模式",
                       name: "runWithinCluster",
                       formatter: "checkbox",
                       editable: true,
                       editoptions: {
                           title: "变更运行模式会移除当前计划任务,并需要重启应用服务器才能生效"
                       }
                   }, {
                       label: "名称描述",
                       name: "description",
                       editable: true,
                       edittype: "textarea",
                       width: 100,
                       align: "left"
                   }],
                   editurl: "/admin/schedule/job-bean-cfg/edit"
                }'>
            </table>
        </div>
    </div>
</div>