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
                            <input type="text" name="search[CN_remarkLog]"
                                   class="form-control input-large"
                                   placeholder="备注内容...">
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
                   url: "/admin/sys/biz-remark-log/list?search[EQ_bizEntityClass]=${param.bizEntityClass}&search[EQ_bizEntityId]=${param.bizEntityId}",
                   colModel: [{
                       label: "备注内容",
                       name: "remarkLog",
                       align: "left"
                   }, {
                       label: "提交时间",
                       name: "submitTime",
                       formatter: "timestamp",
                       editable: true
                   }],
                   multiselect: false,
                   fullediturl: "/admin/sys/biz-remark-log/edit?bizEntityClass=${param.bizEntityClass}&bizEntityId=${param.bizEntityId}"
                }'>
            </table>
        </div>
    </div>
</div>