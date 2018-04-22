<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 基础组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form id="" class="form-horizontal form-bordered form-label-stripped"
                   method="post" modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">普通文本输入元素/输入提示</label>
                            <div class="controls">
                                <input type="text" name="txt1" class="form-control" data-tooltips="[输入提示说明内容]"
                                       data-tooltips-position="bottom" maxlength="20"/>
                                <span class="help-block">辅助说明文本区块</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtFormValidation.html#tooltips" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">必填性校验输入元素</label>
                            <div class="controls">
                                <input type="text" name="txt2" class="form-control" required/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtFormValidation.html#required" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row" data-equal-height="true">
                    <div class="col-md-5">
                        <div class="form-group">
                            <label class="control-label">单选按钮组</label>
                            <div class="controls">
                                <form:radiobuttons id="" path="expired" class="form-control" required="true"
                                                   items="${applicationScope.cons.booleanLabelMap}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">复选按钮组</label>
                            <div class="controls">
                                <form:checkboxes id="" path="splitTexts" class="form-control" required="true"
                                                 items="${multiSelectTags}"
                                                 data-rule-minlength="2" data-msg-minlength="最少选取 {0} 项"
                                                 data-rule-maxlength="3" data-msg-maxlength="最多选取 {0} 项"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtCheckboxRadioGroup.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">表单数据显示</label>
                            <div class="controls">
                                <p class="form-control-static">为了对齐效果给显示文本内容添加p.form-control-static包裹</p>
                                <span class="help-block"></span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg"></div>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>
<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 收藏记忆组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>对于一些表单元素，用户希望能方便设定自己的默认值，这样每次访问表单页面则默认初始化为用户设定值。</p>
                    <p>设定好值，然后按照左侧按钮指示操作保存数据，然后再刷新页面则会显示收藏设定值。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDataProfileParam.html" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
        <form:form id="" class="form-horizontal form-bordered form-label-stripped"
                   method="post" modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row" data-equal-height="true">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">普通输入框</label>
                            <div class="controls">
                                <input type="text" name="txtProfile" class="form-control"
                                       data-profile-param="mock-profile1"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">单选组</label>
                            <div class="controls">
                                <form:radiobuttons id="" path="expired" class="form-control"
                                                   items="${applicationScope.cons.booleanLabelMap}"
                                                   data-profile-param="mock-profile2"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">复选按钮组</label>
                            <div class="controls">
                                <form:checkboxes id="" path="emptyTexts" class="form-control"
                                                 items="${multiSelectTags}" data-profile-param="mock-profile3"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">下拉单选</label>
                            <div class="controls">
                                <form:select id="" path="emptyTexts2" items="${multiSelectTags}" multiple="false"
                                             required="true" class="form-control" data-profile-param="mock-profile4"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label">下拉多选</label>
                            <div class="controls">
                                <form:select id="" path="emptyTexts3" items="${multiSelectTags}"
                                             class="form-control" multiple="true"
                                             data-profile-param="mock-profile5"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 表单控制及校验
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/validation-confirm">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">发货开始日期</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="startDate" required="true"
                                       data-rule-dateLT="endDate" data-tooltips="开始日期应早于结束日期"
                                       data-picker="date">
                                <span class="help-block">日期前后关系关联校验</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">发货结束日期</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="endDate" required="true"
                                       data-rule-dateGT="startDate" data-tooltips="结束日期应晚于开始日期"
                                       data-picker="date" data-today-btn="false">
                                <span class="help-block">日期前后关系关联校验</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label">计划发货量</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="quantity" required="true"
                                       data-rule-digits="true">
                                <span class="help-block">模拟规则：大于100提交表单触发confirm确认提示，小于100通过</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <p class="form-control-static">当前页面相关表单和框架已有基础功能包含一系列表单控制处理演示，可直接参考各功能点设计实现</p>
                        <p class="form-control-static">点击相关链接可查阅主要技术点文说明文档:</p>
                        <span class="help-block">
                            <a href="dev/docs/markdown/290.表单控制.md#get" target="_blank">列表界面的GET查询表单</a>
                        </span>
                        <span class="help-block">
                            <a href="dev/docs/markdown/290.表单控制.md#post" target="_blank">编辑界面的POST提交表单</a>
                        </span>
                        <span class="help-block">
                            <a href="dev/docs/markdown/290.表单控制.md#jsr303" target="_blank">集成服务端和客户端的Form/Data Validation的设计处理</a>
                        </span>
                        <span class="help-block">
                            <a href="dev/docs/markdown/290.表单控制.md#rules"
                               target="_blank">基于JQuery Validation及扩展的校验语法规则定义</a>
                        </span>
                        <span class="help-block">
                            <a href="dev/docs/markdown/290.表单控制.md#confirm"
                               target="_blank">Form表单数据的提交Confirm确认处理</a>
                        </span>
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 演示服务端确认校验
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 扩展组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form id="" class="form-horizontal form-bordered form-label-stripped"
                   method="post" modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">手机号码</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="mobile" required="true"
                                       data-toggle="mobile-sms-code">
                                <span class="help-block">敏感操作，系统自动弹出图片验证码进行人机校验，以防恶意攻击导致短信费用损失</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtMobileSmsCode.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">手机验证码</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="code" required="true">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <hr>
                        <div class="note note-danger">
                            <p>为了交互体验的友好，框架扩展实现输入4位验证码后立即触发AJAX异步校验验证码有效性，如果校验失败立即给出提示信息，同时在表单提交业务处理时再进行验证码销毁处理</p>
                            <p>按照JCaptch组件的设计原则，一个用户会话只能对应一个有效的验证码，因此如下界面同时显示多个验证码只是为了不同类型组件展示，实际业务不应该出现同一个页面多个验证码输入</p>
                            <p>因此在本页面如果同时显示了多个验证码，则需要分别点击刷新验证码后，在对应输入框输入验证码体验实时校验效果，否则会出现看到验证码即使输入正确也提示失败</p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">图片验证码（普通输入框）</label>
                            <div class="controls">
                                <input class="form-control" data-toggle="captcha-code"
                                       type="text" name="captcha" required="true"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageCaptchaCode.html#normal" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">图片验证码（美化图标样式）</label>
                            <div class="controls">
                                <input class="form-control" data-toggle="captcha-code"
                                       type="text" name="captcha" required="true" data-input-icon="fa-qrcode"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageCaptchaCode.html#inputIcon" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <hr>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">弹窗组件(缓存显示)</label>
                            <div class="controls">
                                <a title="修改密码" class="btn" data-width="500px" data-toggle="modal-ajaxify"
                                   href="javascript:;" data-url="admin/profile/password">修改密码</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtAjaxBootstrapModal.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">弹窗组件(每次刷新)</label>
                            <div class="controls">
                                <a title="修改密码" class="btn" data-width="500px" data-toggle="modal-ajaxify"
                                   href="javascript:;" data-url="admin/profile/password"
                                   data-force-reload="true">修改密码</a>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">按钮发起POST请求</label>
                            <div class="controls">
                                <button class="btn blue btn-post-url" type="button"
                                        data-url="dev/demo/all-in-one/btn-post" data-confirm="确认提交请求？">模拟按钮POST请求
                                </button>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 扩展AJAX支持Tab组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"后台用户管理"编辑界面，实际操作可直接访问“配置管理”->"权限管理"->"后台用户管理"菜单功能体验。</p>
                    <p>点击保存会直接调用"后台用户管理"数据存储逻辑，提交的数据可在对应功能界面查看。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtAjaxBootstrapTabs.html" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>

        <div data-toggle="ajaxify" data-url="admin/auth/user/edit-tabs?id=${user.id}"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 下拉、选取等处理组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form id="" class="form-horizontal form-bordered form-label-stripped control-label-lg"
                   method="post" modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <!--
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">以Dropdown形式弹出AJAX加载DIV区域</label>
                            <div class="controls">
                                <form:input id="" path="textContent" class="form-control" data-toggle="dropdown-select"
                                            data-url="dev/docs/ui-feature/dropdownselect"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
							<span class="help-block">
								<a href="dev/docs/jsdoc/ExtDropdownSelect.html" target="_blank"
                                   class="btn btn-icon-only green btn-dev-demo-info">
                                        <i class="fa fa-info"></i>
                                </a>
							</span>
                    </div>
                </div>
                -->
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2单选组件</label>
                            <div class="controls">
                                <form:select id="" path="selectedId" items="${multiSelectOptions}" multiple="false"
                                             class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtSelect.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2单选并可自由输入组件</label>
                            <div class="controls">
                                <form:select id="" path="textContent" items="${multiSelectTags}" multiple="false"
                                             data-tags="true" class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2多选组件</label>
                            <div class="controls">
                                <form:select id="" path="selectedIds" items="${multiSelectOptions}" multiple="true"
                                             class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2多选并限制数目组件</label>
                            <div class="controls">
                                <form:select id="" path="selectedIds" items="${multiSelectOptions}" multiple="true"
                                             data-maximum-selection-length="3" class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2多选并可自由输入组件</label>
                            <div class="controls">
                                <form:select id="" path="splitTexts" items="${multiSelectTags}"
                                             multiple="true" data-tags="true" class="form-control"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2 AJAX查询(输入SC示例)单选组件</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-url="admin/auth/department/list"
                                             data-term-query="search[CN_code_OR_name]"
                                             multiple="false">
                                    <form:option value="${entity.department.id}" label="${entity.department.name}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2 AJAX查询(输入SC示例)多选组件</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-url="admin/auth/department/list"
                                             data-term-query="search[CN_code_OR_name]"
                                             multiple="true">
                                    <form:option value="${entity.department.id}" label="${entity.department.name}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">基于Select2 AJAX查询(输入SC示例)组合示例</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-url="admin/auth/department/list"
                                             data-term-query="search[CN_code_OR_name]"
                                             data-term-rows="2"
                                             data-item-display="name"
                                             data-item-label="name"
                                             multiple="true">
                                    <form:option value="${entity.department.id}" label="${entity.department.name}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-11">
                    <div class="form-group">
                        <label class="control-label">基于multiSelect封装的左右互选组件</label>
                        <div class="controls">
                            <form:select id="" path="selectedIds" items="${multiSelectOptions}" class="form-control"
                                         data-toggle="double-multi-select" data-height="300px"/>
                        </div>
                    </div>
                </div>
                <div class="col-md-1 visible-md visible-lg">
                    <span class="help-block">
                        <a href="dev/docs/jsdoc/ExtDoubleMultiSelect.html" target="_blank"
                           class="btn btn-icon-only green btn-dev-demo-info">
                            <i class="fa fa-info"></i>
                        </a>
                    </span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <hr>
                </div>
            </div>
            <div class="row">
                <div class="col-md-11">
                    <div class="form-group">
                        <label class="control-label">下拉级联组件-触发元素</label>
                        <div class="controls">
                            <select name="clazz" class="form-control" required="true" placeholder="记录数据对象"
                                    data-cascade-name="property"
                                    data-cascade-url="admin/aud/revision-entity/properties">
                                <c:forEach items="${clazzMapping}" var="item">
                                    <option value="${item.key}">${item.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-md-1 visible-md visible-lg">
                    <span class="help-block">
                        <a href="dev/docs/jsdoc/ExtSelect.html" target="_blank"
                           class="btn btn-icon-only green btn-dev-demo-info">
                            <i class="fa fa-info"></i>
                        </a>
                    </span>
                </div>
            </div>
            <div class="row">
                <div class="col-md-11">
                    <div class="form-group">
                        <label class="control-label">下拉级联组件-目标元素</label>
                        <div class="controls">
                            <select name="property" class="form-control" placeholder="选取变更属性">
                            </select>
                        </div>
                    </div>
                </div>
                <div class="col-md-1 visible-md visible-lg"></div>
            </div>

            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> Tree树形相关组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form id="" class="form-horizontal form-bordered form-label-stripped control-label-lg"
                   method="post" modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">树形导航-初次加载所有节点</label>
                            <div class="controls">
                                <ul data-toggle="nav-tree" data-url="/admin/auth/department/tree"
                                    data-fetch-all="true" data-parent-name="search[EQ_parent.id]"
                                    data-onclick="alert($(this).attr('node-id')+' : '+$(this).attr('node-name'))"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtNavTree.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">树形导航-点击展开逐级加载</label>
                            <div class="controls">
                                <ul data-toggle="nav-tree" data-url="/admin/auth/department/tree"
                                    data-parent-name="search[EQ_parent.id]"
                                    data-only-child-select="false"
                                    data-onclick="alert($(this).attr('node-id')+' : '+$(this).attr('node-name'))"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单选-无初始值-初次加载所有节点</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-fetch-all="true"
                                             data-parent-name="search[EQ_parent.id]"
                                             multiple="false">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtDropdownTree.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单选-无初始值-点击展开逐级加载</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-parent-name="search[EQ_parent.id]"
                                             data-query-name="search[CN_code_OR_name]"
                                             multiple="false">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单选-有初始值-初次加载所有节点</label>
                            <div class="controls">
                                <form:select path="department.id" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-fetch-all="true"
                                             data-parent-name="search[EQ_parent.id]"
                                             multiple="false">
                                    <form:option value="${entity.department.id}" label="${entity.department.display}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单选-有初始值-点击展开逐级加载</label>
                            <div class="controls">
                                <form:select id="" path="department.id" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-parent-name="search[EQ_parent.id]"
                                             data-query-name="search[CN_code_OR_name]"
                                             multiple="false">
                                    <form:option value="${entity.department.id}" label="${entity.department.display}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多选-无初始值-初次加载所有节点</label>
                            <div class="controls">
                                <form:select id="" path="departmentIds" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-fetch-all="true"
                                             data-parent-name="search[EQ_parent.id]"
                                             multiple="true">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多选-无初始值-点击展开逐级加载</label>
                            <div class="controls">
                                <form:select id="" path="departmentIds" class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-parent-name="search[EQ_parent.id]"
                                             data-query-name="search[CN_code_OR_name]"
                                             multiple="true">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多选-有初始值-初次加载所有节点</label>
                            <div class="controls">
                                <form:select id="" path="departmentIds" items="${entity.departments}"
                                             itemValue="id" itemLabel="display"
                                             class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-fetch-all="true"
                                             multiple="true">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多选-有初始值-点击展开逐级加载</label>
                            <div class="controls">
                                <form:select id="" path="departmentIds" items="${entity.departments}"
                                             itemValue="id" itemLabel="display"
                                             class="form-control"
                                             data-toggle="dropdown-tree"
                                             data-url="/admin/auth/department/tree"
                                             data-parent-name="search[EQ_parent.id]"
                                             data-query-name="search[CN_code_OR_name]"
                                             multiple="true">
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 日期、时间等处理组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">年月日 日期选取组件</label>
                            <div class="controls">
                                <form:input id="" path="saleDate" class="form-control" data-picker="date"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtDatePicker.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">年月日-时分 选取组件</label>
                            <div class="controls">
                                <form:input id="" path="publishTime" class="form-control" data-picker="date-time"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtDateTimePicker.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">时间段 选取组件</label>
                            <div class="controls">
                                <form:input id="" path="searchDate" class="form-control" data-picker="date-range"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtDateRangePicker.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> Text、HTML等处理组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">Textarea文本框可输入字符数显示和校验</label>
                            <div class="controls">
                                <form:textarea id="" path="textContent" class="form-control" maxlength="100"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">

                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">将Textarea转换为HTML可视化编辑器</label>
                            <div class="controls">
                                <form:textarea id="" path="htmlContent" class="form-control"
                                               data-htmleditor='kindeditor'
                                               data-height="400"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtTextareaHtmleditor.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">美化的滚动面板</label>
                            <div class="controls">
                                <div class="scroller" style="height:200px;" data-always-visible="1"
                                     data-rail-visible1="1">
                                    <div style="height: 110px; background-color: #eee"></div>
                                    <div style="height: 120px; background-color: #ddd"></div>
                                    <div style="height: 130px; background-color: #ccc"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtSlimscrollPanel.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 文件、图片等处理组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <form:hidden id="" path="id"/>
            <form:hidden id="" path="version"/>
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单文件上传</label>
                            <div class="controls">
                                <ul class="list-group" data-fileuploader="oneFile" data-multiple="false">
                                    <li class="list-group-item">
                                        <form:hidden id="" path="oneFile.id"/>
                                        <form:hidden id="" path="oneFile.accessUrl"/>
                                        <form:hidden id="" path="oneFile.fileRealName"/>
                                        <form:hidden id="" path="oneFile.fileLength"/>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtFileUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多文件上传</label>
                            <div class="controls">
                                <ul class="list-group" data-fileuploader="multiFiles" data-multiple="true">
                                    <c:forEach var="item" items="${entity.multiFiles}" varStatus="status">
                                        <li class="list-group-item">
                                            <form:hidden id="" path="multiFiles[${status.index}].id"/>
                                            <form:hidden id="" path="multiFiles[${status.index}].accessUrl"/>
                                            <form:hidden id="" path="multiFiles[${status.index}].fileRealName"/>
                                            <form:hidden id="" path="multiFiles[${status.index}].fileLength"/>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtFileUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单图片上传(单一文本)</label>
                            <div class="controls">
                                <form:hidden id="" path="imagePath" class="form-control" data-imageuploader="single"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多图片上传(逗号文本)</label>
                            <div class="controls">
                                <form:hidden id="" path="imagePaths" class="form-control" data-imageuploader="multiple"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">单图片上传(单一对象)</label>
                            <div class="controls">
                                <form:select id="" path="oneImage"
                                             class="form-control" data-imageuploader="single">
                                    <form:option value="${entity.oneImage.id}" label="${entity.oneImage.accessUrl}"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">多图片上传(集合对象)</label>
                            <div class="controls">
                                <form:select id="" path="multiImages" items="${entity.multiImages}" itemLabel="accessUrl" itemValue="id"
                                             class="form-control" data-imageuploader="multiple"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtImageUploader.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">二维码组件</label>
                            <div class="controls">
                                <div data-qrcode="${webContextFullUrl}/admin"
                                     data-qrcode-icon="assets/pages/img/favicon.ico"
                                     data-qrcode-header="欢迎访问"
                                     data-qrcode-footer="扫码查看详情"
                                     data-foreground="#FF0000"/>
                                <span class="help-block">点击二维码自动转换为图片下载，方便线下使用</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtQrcode.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button class="btn blue" type="submit">
                    <i class="fa fa-check"></i> 提交并显示表单数据
                </button>
                <button class="btn default" type="button" data-dismiss="modal">取消</button>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 动态表格等处理组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"报销申请"编辑界面，实际操作可直接访问“演示样例”->"报销申请"菜单功能体验。</p>
                    <p>点击保存会直接调用"报销申请"数据存储逻辑，提交的数据可在对应功能界面查看。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDynamicEditTable.html" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>

        <div data-toggle="ajaxify" data-url="dev/demo/reimbursement-request/edit"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 行项数据编辑及弹窗界面编辑数据Grid组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"登录账户管理"主界面，完整功能可访问“配置管理”->“权限管理”->"登录账户管理"菜单功能体验。</p>
                    <p>直接双击可编辑列（如"移动电话"列），进入行项数据编辑模式，输入修改数据回车保存，自动进入下一行并定位上一编辑列，实现连续快速更新多行同列数据。</p>
                    <p>直接双击不可编辑列（如"账号标识"列），进入弹窗编辑模式，修改提交数据后，自动计算处理弹窗的刷新或关闭处理，并自动刷新表格数据。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDataGrid.html" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
        <div data-toggle="ajaxify" data-url="admin/auth/account"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 父子递归和拖放操作Grid组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"部门管理"主界面，完整功能可访问“配置管理”->“权限管理”->"部门管理"菜单功能体验。</p>
                    <p>展开各节点后，点击表头 <i class="fa fa-arrows"></i> 图标开启拖放操作模式，然后即可进行各子表格行项拖放更新操作。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDataGrid.html#recursiveSubGrid" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
        <div data-toggle="ajaxify" data-url="admin/auth/department"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 主子数据展现Grid组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"公告管理"主界面，完整功能可访问“配置管理”->“系统管理”->"公告管理"菜单功能体验。</p>
                    <p>可通过主界面右上角读取公告，然后再返回来刷新表格查看最新阅读记录子表格数据。</p>
                    <p>展开节点，显示子表格数据，主子表格可各自独立分页查询操作等。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDataGrid.html#initSubGrid" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
        <div data-toggle="ajaxify" data-url="admin/sys/notify-message"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 为Grid组件添加定制业务功能操作
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="note note-danger">
                    <p>当前界面直接调用的"任务实时控制"主界面，完整功能可访问“配置管理”->“计划任务管理”->"任务实时控制"菜单功能体验。</p>
                    <p>点击"立即执行"业务按钮触发所选任务立即执行，执行任务记录结果可访问“配置管理”->“计划任务管理”->"任务运行记录"菜单功能查看。</p>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtDataGrid.html#navButtons" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
        <div data-toggle="ajaxify" data-url="admin/schedule/quartz-trigger"></div>
    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 百度地图
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <form:form class="form-horizontal form-bordered form-label-stripped" method="post"
                   modelAttribute="entity" data-validation='true'
                   action="dev/demo/all-in-one/show-form-data">
            <div class="form-body">
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">空白信息初始化地图</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="address1" data-gmaps="address"
                                       value="">
                                <span class="help-block">更新文本框或点击地图，双向联动更新</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                            <a href="dev/docs/jsdoc/ExtGmapsBaidu.html" target="_blank"
                               class="btn btn-icon-only green btn-dev-demo-info">
                                <i class="fa fa-info"></i>
                            </a>
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">以地址信息初始化地图</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="address2" data-gmaps="address"
                                       value="重庆市解放碑">
                                <span class="help-block">更新文本框或点击地图，双向联动更新</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-1 visible-md visible-lg">
                        <span class="help-block">
                        </span>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-11">
                        <div class="form-group">
                            <label class="control-label">以经纬度坐标初始化地图</label>
                            <div class="controls">
                                <input type="text" class="form-control" name="point" data-gmaps="point"
                                       value="106.476257,29.572587"
                                       data-tooltips="输入逗号分隔经纬度数字可直接移动地图点；输入其他文本则表示地址搜索功能">
                                <span class="help-block">更新文本框(逗号分隔经纬度或地址文本)或点击地图，双向联动更新</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <span class="help-block">
                        </span>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>

<div class="portlet gren hide">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> TODO
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">

    </div>
</div>

<div class="portlet gren">
    <div class="portlet-title">
        <div class="caption">
            <i class="fa fa-reorder"></i> 无限滚动表格组件
        </div>
        <div class="tools">
            <a class="collapse" href="javascript:;"></a>
            <a class="remove" href="javascript:;"></a>
        </div>
    </div>
    <div class="portlet-body">
        <div class="row">
            <div class="col-md-11">
                <div class="row">
                    <div class="col-md-12">
                        <table class="table table-infinite-scroll">
                            <thead>
                            <tr>
                                <th>权限代码</th>
                                <th>生成时间</th>
                            </tr>
                            </thead>
                            <tbody data-url="dev/demo/all-in-one/infinite-scroll-items?rows=3">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="col-md-1 visible-md visible-lg">
                <span class="help-block">
                    <a href="dev/docs/jsdoc/ExtTableInfiniteScroll.html" target="_blank"
                       class="btn btn-icon-only green btn-dev-demo-info">
                        <i class="fa fa-info"></i>
                    </a>
                </span>
            </div>
        </div>
    </div>
</div>