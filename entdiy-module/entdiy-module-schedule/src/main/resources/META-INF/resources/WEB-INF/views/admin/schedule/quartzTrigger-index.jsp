<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-md-12">
        <table data-toggle="grid"
               grid-options='{
               url: "/admin/schedule/quartz-trigger/list",
               colModel: [{
                   label: "任务名称",
                   name: "jobName",
                   width: 240,
                   align: "left"
               }, {
                   label: "CRON表达式",
                   name: "cronExpression",
                   width: 100,
                   align: "right"
               }, {
                   label: "当前状态",
                   name: "stateLabel",
                   width: 60,
                   align: "center"
               }, {
                   label: "上次触发时间",
                   name: "previousFireTime",
                   sorttype: "date",
                   align: "center"
               }, {
                   label: "下次触发时间",
                   name: "nextFireTime",
                   sorttype: "date",
                   align: "center"
               }, {
                   label: "集群运行模式",
                   name: "runWithinCluster",
                   formatter: "checkbox"
               }],
               rowNum: -1,
               loadonce: true,
               addable: false,
               loadonce: true,
               navButtons: [{
                   caption: "启动",
                   buttonicon: "fa-play",
                   onClickButton: function (rowids) {
                       var that = this;
                       that.$element.ajaxPostURL({
                           url: "/admin/schedule/quartz-trigger/state",
                           success: function () {
                               that.refresh();
                           },
                           confirmMsg: "确认 启动 所选任务？",
                           data: {
                               ids: rowids.join(","),
                               state: "resume"
                           }
                       })
                   },
                   operationRows: "multiple",
                   showOnToolbar: true,
                   showOnToolbarText: true
               }, {
                   caption: "暂停",
                   buttonicon: "fa-pause",
                   onClickButton: function (rowids) {
                       var that = this;
                       that.$element.ajaxPostURL({
                           url: "/admin/schedule/quartz-trigger/state",
                           success: function () {
                               that.refresh();
                           },
                           confirmMsg: "确认 启动 所选任务？",
                           data: {
                               ids: rowids.join(","),
                               state: "pause"
                           }
                       })
                   },
                   operationRows: "multiple",
                   showOnToolbar: true,
                   showOnToolbarText: true
               }, {
                   caption: "立即执行",
                   buttonicon: "fa-bolt",
                   onClickButton: function (rowids) {
                       var that = this;
                       that.$element.ajaxPostURL({
                           url: "/admin/schedule/quartz-trigger/run",
                           success: function () {
                               that.refresh();
                           },
                           confirmMsg: "确认 立即执行 所选任务？",
                           data: {
                               ids: rowids.join(",")
                           }
                       })
                   },
                   operationRows: "multiple",
                   showOnToolbar: true,
                   showOnToolbarText: true
               }]
            }'>
        </table>
    </div>
</div>