/**
 * 基于jqGrid的表格处理
 * @see http://free-jqgrid.github.io/examples/index.html
 * @namespace Grid
 */
//覆写showlink formatter，添加支持从行项数据组装url
$.fn.fmatter.showlink = function (cellval, opts, rowdata) {

    var op = {
        baseLinkUrl: opts.baseLinkUrl,
        showAction: opts.showAction,
        addParam: opts.addParam || "",
        target: opts.target,
        idName: opts.idName
    }, target = "", title = "", idUrl;
    if (opts.colModel !== undefined && opts.colModel.formatoptions !== undefined) {
        op = $.extend({}, op, opts.colModel.formatoptions);
    }
    if (op.target) {
        target = ' target="' + op.target + '"';
    }
    if (op.title) {
        title = ' title="' + op.title + '"';
    }
    //添加idValue属性处理以支持从特定的rowdata属性中取值作为链接的id值
    //比如链接显示一个关联对象的查看界面，则可以把“abc.id”作为idValue值
    var id = null;
    if (op.idValue == 'id') {
        id = opts.rowId;
    } else {
        id = rowdata[op.idValue];
        //兼容处理以对象方式获取数据值
        if ((id == undefined || id == '') && op.idValue.indexOf(".") > -1) {
            var loop = rowdata;
            $.each(op.idValue.split("."), function (i, item) {
                loop = loop[item];
            })
            id = loop;
        }
    }
    idUrl = op.baseLinkUrl + op.showAction + '?' + op.idName + '=' + id + op.addParam;
    if ($.fmatter.isString(cellval) || $.fmatter.isNumber(cellval)) { //add this one even if its blank string
        return "<a " + target + title + " href=\"" + idUrl + "\">" + cellval + "</a>";
    }
    return $.fn.fmatter.defaultFormat(cellval, opts);
};

var Grid = function () {

    // private functions & variables
    var gridDefaultInited = false;

    // public functions
    return {

        initGridDefault: function () {

            $.extend($.ui.multiselect, {
                locale: {
                    addAll: '全部添加',
                    removeAll: '全部移除',
                    itemsCount: '已选择项目列表'
                }
            });

            $.extend($.jgrid.ajaxOptions, {
                dataType: "json"
            });

            $.extend($.jgrid.defaults, {
                guiStyle: "bootstrap",
                iconSet: "fontAwesome",
                datatype: "json",
                loadui: false,
                loadonce: false,
                filterToolbar: {},
                ignoreCase: true,
                prmNames: {
                    npage: "npage"
                },
                jsonReader: {
                    repeatitems: false,
                    root: "content",
                    total: "totalPages",
                    records: "totalElements"
                },
                treeReader: {
                    level_field: "extraAttributes.level",
                    parent_id_field: "extraAttributes.parent",
                    leaf_field: "extraAttributes.isLeaf",
                    expanded_field: "extraAttributes.expanded",
                    loaded: "extraAttributes.loaded",
                    icon_field: "extraAttributes.icon"
                },
                autowidth: true,
                rowNum: 15,
                page: 1,
                altclass: "ui-jqgrid-evennumber",
                height: "stretch",
                viewsortcols: [true, 'vertical', true],
                mtype: "GET",
                viewrecords: true,
                rownumbers: true,
                toppager: true,
                gridview: true,
                altRows: true,
                sortable: false,
                multiboxonly: true,
                multiselect: true,
                multiSort: false,
                forceFit: false,
                shrinkToFit: true,
                sortorder: "desc",
                sortname: "id",
                ajaxSelectOptions: {
                    cache: true
                },
                loadError: function (ts, xhr, st, err) {
                    try {
                        var message = "表格数据加载处理失败,请尝试刷新或联系管理员";
                        var response = jQuery.parseJSON(ts.responseText);
                        if (response.type == "failure" || response.type == "error") {
                            message = response.message;
                        }
                        Global.notify("error", message);
                    } catch (e) {
                        Global.notify("error", "数据处理异常");
                    }
                },
                subGridOptions: {
                    reloadOnExpand: false
                }
            });

            $.extend($.jgrid.search, {
                multipleSearch: true,
                multipleGroup: true,
                width: 700,
                jqModal: false,

                searchOperators: true,
                stringResult: true,
                searchOnEnter: true,
                defaultSearch: 'bw',
                operandTitle: "点击选择查询方式",
                odata: [{
                    oper: 'eq',
                    text: '等于\u3000\u3000'
                }, {
                    oper: 'ne',
                    text: '不等\u3000\u3000'
                }, {
                    oper: 'lt',
                    text: '小于\u3000\u3000'
                }, {
                    oper: 'le',
                    text: '小于等于'
                }, {
                    oper: 'gt',
                    text: '大于\u3000\u3000'
                }, {
                    oper: 'ge',
                    text: '大于等于'
                }, {
                    oper: 'bw',
                    text: '开始于'
                }, {
                    oper: 'bn',
                    text: '不开始于'
                }, {
                    oper: 'in',
                    text: '属于\u3000\u3000'
                }, {
                    oper: 'ni',
                    text: '不属于'
                }, {
                    oper: 'ew',
                    text: '结束于'
                }, {
                    oper: 'en',
                    text: '不结束于'
                }, {
                    oper: 'cn',
                    text: '包含\u3000\u3000'
                }, {
                    oper: 'nc',
                    text: '不包含'
                }, {
                    oper: 'nu',
                    text: '不存在'
                }, {
                    oper: 'nn',
                    text: '存在'
                }, {
                    oper: 'bt',
                    text: '介于'
                }],
                operands: {
                    "eq": "=",
                    "ne": "!",
                    "lt": "<",
                    "le": "<=",
                    "gt": ">",
                    "ge": ">=",
                    "bw": "^",
                    "bn": "!^",
                    "in": "=",
                    "ni": "!=",
                    "ew": "|",
                    "en": "!@",
                    "cn": "~",
                    "nc": "!~",
                    "nu": "#",
                    "nn": "!#",
                    "bt": "~~"
                }
            });

            $.extend($.jgrid.del, {
                serializeDelData: function (postd) {
                    //把选取的多个id值转换参数名为ids以便和服务器端匹配处理
                    postd['ids'] = postd['id'];
                    postd['id'] = '';
                    return postd;
                },
                errorTextFormat: function (data) {
                    var response = jQuery.parseJSON(data.responseText);
                    return response.message;
                },
                //删除处理回调
                afterComplete: function (data) {
                    var ret = new Array();
                    var response = jQuery.parseJSON(data.responseText);
                    if (response.type == "success") {
                        top.$.publishMessage(response.message);
                        ret[0] = true;
                    } else {
                        top.$.publishError(response.message);
                        ret[0] = false;
                    }
                    return ret;
                },
                ajaxDelOptions: {
                    dataType: 'json'
                }
            });

            $.jgrid.extend({
                bindKeys: function (settings) {
                    var o = $.extend({
                        upKey: true,
                        downKey: true,
                        onEnter: null,
                        onSpace: null,
                        onLeftKey: null,
                        onRightKey: null,
                        scrollingRows: true
                    }, settings || {});
                    return this.each(function () {
                        var $t = this;
                        if (!$('body').is('[role]')) {
                            $('body').attr('role', 'application');
                        }
                        $t.p.scrollrows = o.scrollingRows;
                        $($t).keydown(function (event) {
                            var target = $($t).find('tr[tabindex=0]')[0], id, r, mind, expanded = $t.p.treeReader.expanded_field;
                            //check for arrow keys
                            if (target) {
                                mind = $t.p._index[$.jgrid.stripPref($t.p.idPrefix, target.id)];
                                if (event.keyCode === 37 || event.keyCode === 38 || event.keyCode === 39 || event.keyCode === 40) {
                                    // up key
                                    if (event.keyCode === 38 && o.upKey) {
                                        r = target.previousSibling;
                                        id = "";
                                        if (r) {
                                            if ($(r).is(":hidden")) {
                                                while (r) {
                                                    r = r.previousSibling;
                                                    if (!$(r).is(":hidden") && $(r).hasClass('jqgrow')) {
                                                        id = r.id;
                                                        break;
                                                    }
                                                }
                                            } else {
                                                id = r.id;
                                            }
                                        }
                                        $($t).jqGrid('setSelection', id, true, event);
                                        event.preventDefault();
                                    }
                                    //if key is down arrow
                                    if (event.keyCode === 40 && o.downKey) {
                                        r = target.nextSibling;
                                        id = "";
                                        if (r) {
                                            if ($(r).is(":hidden")) {
                                                while (r) {
                                                    r = r.nextSibling;
                                                    if (!$(r).is(":hidden") && $(r).hasClass('jqgrow')) {
                                                        id = r.id;
                                                        break;
                                                    }
                                                }
                                            } else {
                                                id = r.id;
                                            }
                                        }
                                        $($t).jqGrid('setSelection', id, true, event);
                                        event.preventDefault();
                                    }
                                    // left
                                    if (event.keyCode === 37) {
                                        if ($t.p.treeGrid && $t.p.data[mind][expanded]) {
                                            $(target).find("div.treeclick").trigger('click');
                                        }
                                        $($t).triggerHandler("jqGridKeyLeft", [$t.p.selrow]);
                                        if ($.isFunction(o.onLeftKey)) {
                                            o.onLeftKey.call($t, $t.p.selrow);
                                        }
                                    }
                                    // right
                                    if (event.keyCode === 39) {
                                        if ($t.p.treeGrid && !$t.p.data[mind][expanded]) {
                                            $(target).find("div.treeclick").trigger('click');
                                        }
                                        $($t).triggerHandler("jqGridKeyRight", [$t.p.selrow]);
                                        if ($.isFunction(o.onRightKey)) {
                                            o.onRightKey.call($t, $t.p.selrow);
                                        }
                                    }
                                }
                                //check if enter was pressed on a grid or treegrid node
                                else if (event.keyCode === 13) {
                                    var ta = event.target;
                                    //对TEXTAREA特殊判断回车只换行不提交
                                    if (ta.tagName === 'TEXTAREA') {
                                        return true;
                                    }
                                    event.stopPropagation();
                                    $($t).triggerHandler("jqGridKeyEnter", [$t.p.selrow]);
                                    if ($.isFunction(o.onEnter)) {
                                        o.onEnter.call($t, $t.p.selrow);
                                    }
                                } else if (event.keyCode === 32) {
                                    event.stopPropagation();
                                    $($t).triggerHandler("jqGridKeySpace", [$t.p.selrow]);
                                    if ($.isFunction(o.onSpace)) {
                                        o.onSpace.call($t, $t.p.selrow);
                                    }
                                } else if (event.keyCode === 27) {//ESC
                                    event.stopPropagation();
                                    $($t).jqGrid("restoreRow", $t.p.selrow, o.afterrestorefunc);
                                    $($t).jqGrid('showAddEditButtons')
                                }
                            }
                        });
                    });
                },

                /**
                 * 刷新Grid组件
                 * @alias jqgrid-refresh
                 * @example $grid.refresh()
                 */
                refresh: function () {
                    this.each(function () {
                        var $t = this;
                        if (!$t.grid) {
                            return;
                        }
                        $($t).jqGrid('setGridParam', {
                            datatype: "json"
                        }).trigger("reloadGrid");
                    })
                },

                //按照提供参数查询刷新
                search: function (params) {
                    this.each(function () {
                        var $t = this;
                        if (!$t.grid) {
                            return;
                        }
                        var url = $($t).jqGrid('getGridParam', 'url');
                        for (var key in params) {
                            url = Util.AddOrReplaceUrlParameter(url, key, params[key]);
                        }
                        $($t).jqGrid('setGridParam', {
                            url: url,
                            page: 1
                        }).trigger("reloadGrid");
                    })
                },

                //把‘当前’分页面的数据组装成一个长文本然后传给后端程序切分处理转换为Excel数据文件下载
                exportExcelLocal: function (params) {
                    this.each(function () {
                        var $t = this;
                        if (!$t.grid) {
                            return;
                        }

                        if (!confirm("确认导出当前页面 " + $t.p.caption + " 数据为Excel下载文件？")) {
                            return;
                        }

                        //循环每行每列按照tab切分组装长字符串
                        var mya = new Array();
                        // Get All IDs
                        mya = $($t).getDataIDs();
                        var colModel = $t.p.colModel;
                        var colNames = $t.p.colNames;
                        var html = "";
                        //循环拼接表头字段名称
                        for (k = 0; k < colNames.length; k++) {
                            // output each Column as tab delimited
                            var cm = colModel[k];
                            if (cm.hidedlg || cm.hidden || cm.disableExport) {
                                continue;
                            }
                            html = html + colNames[k] + "\t";
                        }
                        // Output header with end of line
                        html = html + "\n";
                        for (i = 0; i < mya.length; i++) {
                            // get each row
                            data = $($t).getRowData(mya[i]);
                            for (j = 0; j < colNames.length; j++) {
                                // output each Row as tab delimited
                                var cm = colModel[j];
                                if (cm.hidedlg || cm.hidden || cm.disableExport) {
                                    continue;
                                }
                                var realData = data[cm.name];
                                var selectValues = null;
                                //如果是特定的mapping类型数据，如true=是，false=否，把原始数据转换为映射后的字面数据
                                if (cm.searchoptions && cm.searchoptions.value) {
                                    selectValues = cm.searchoptions.value;
                                } else if (cm.editoptions && cm.editoptions.value) {
                                    selectValues = cm.editoptions.value;
                                }
                                if (selectValues) {
                                    realData = selectValues[realData];
                                }
                                //对于链接形式的数据只取text内容
                                if (realData.indexOf("<") > -1 && realData.indexOf(">") > -1) {
                                    realData = $(realData).text();
                                }
                                if (realData == '') {
                                    realData = data[cm.name];
                                }
                                if (realData == 'null' || realData == null) {
                                    realData = '';
                                }
                                //移除无意义的空格转义字符
                                realData = realData.replace(/\&nbsp;/g, "");
                                html = html + realData + "\t";
                            }
                            // output each row with end of line
                            html = html + "\n";

                        }
                        // end of line at the end
                        html = html + "\n";
                        // alert(html);
                        //把组装好的字符串以post参数提交表单请求服务器组装为Excel下载文件
                        var form = $('<form method="post" target = "_blank" action="' + WEB_ROOT + '/admin/util/grid/export"></form>').appendTo($("body"));
                        var dataInput = $('<input type="hidden" name="exportDatas"/>').appendTo(form);
                        var fileName = $('<input type="hidden" name="fileName"/>').appendTo(form);
                        fileName.val("export-data.xls");
                        dataInput.val(html);
                        form.submit();
                        form.remove();
                    })
                },

                //更新表格中数组输入组件的下标索引值 主要用于如主子表结构的子表数据的索引下标处理
                refreshRowIndex: function () {
                    var target = $(this);
                    $.each($(target).jqGrid('getDataIDs'), function (i, n) {
                        $(target).find("#" + n).find("input,select").each(function () {
                            var oldName = $(this).attr("name");
                            $(this).attr("name", oldName.substring(0, oldName.indexOf("[") + 1) + i + oldName.substring(oldName.indexOf("]"), oldName.length));
                        });
                    });
                },

                /**
                 *
                 * 获取Grid至少选择一项,如果没有选择则Alert提示
                 * @alias jqgrid-getAtLeastOneSelectedItem
                 * @param includeSubGird 是否包含子表格数据项，默认false
                 * @returns 选取行项的ID数组
                 *
                 * @example $grid.getAtLeastOneSelectedItem()
                 */
                getAtLeastOneSelectedItem: function (includeSubGird) {
                    var target = $(this);
                    var checkedRows = [];
                    //先多选模式获取，如果没有取到数据在单选模式获取
                    var selectedRows = jQuery(target).jqGrid('getGridParam', 'selarrrow');
                    if (selectedRows.length > 0) {
                        for (var x = 0; x < selectedRows.length; x++) {
                            var isDisabled = $("#jqg_" + jQuery(target).attr("id") + "_" + selectedRows[x]).is(':disabled');
                            if (!isDisabled) {
                                checkedRows.push(selectedRows[x]);
                            }
                        }
                    } else {
                        var singleselect = jQuery(target).jqGrid('getGridParam', 'selrow');
                        if (singleselect) {
                            checkedRows.push(singleselect);
                        }
                    }

                    // 处理SubGrid
                    if (includeSubGird) {
                        jQuery(target).find("table.jqsubgrid").each(function () {
                            var subselectedRows = $(this).jqGrid('getGridParam', 'selarrrow');
                            for (var x = 0; x < subselectedRows.length; x++) {
                                var isDisabled = $("#jqg_" + jQuery(this).attr("id") + "_" + selectedRows[x]).is(':disabled');
                                if (!isDisabled) {
                                    checkedRows.push(subselectedRows[x]);
                                }
                            }
                        });
                    }

                    if (checkedRows.length == 0) {
                        Global.notify("warning", "请至少选择一条行项目！");
                        return false;
                    } else {
                        return checkedRows;
                    }
                },

                /**
                 *
                 * 获取Grid唯一选择行项,如果没有选择或多选则Alert提示
                 * @alias jqgrid-getOnlyOneSelectedItem
                 * @param requireAlert 必选警告标识，如果为true则会给出提示信息，为false则直接不提示直接返回false标识
                 * @returns 已选取行项的ID值
                 *
                 * @example $grid.getOnlyOneSelectedItem()
                 */
                getOnlyOneSelectedItem: function (requireAlert) {
                    var target = $(this);
                    var checkedRows = [];
                    var selectedRows = jQuery(target).jqGrid('getGridParam', 'selarrrow');
                    if (selectedRows.length > 0) {
                        for (var x = 0; x < selectedRows.length; x++) {
                            var isDisabled = $("#jqg_" + jQuery(target).attr("id") + "_" + selectedRows[x]).is(':disabled');
                            if (!isDisabled) {
                                checkedRows.push(selectedRows[x]);
                            }
                        }
                    } else {
                        var singleselect = jQuery(target).jqGrid('getGridParam', 'selrow');
                        if (singleselect) {
                            checkedRows.push(singleselect);
                        }
                    }

                    if (checkedRows.length == 0) {
                        if (requireAlert) {
                            Global.notify("error", "请选取操作项目");
                        }
                        return false;
                    } else {
                        if (checkedRows.length > 1) {
                            Global.notify("error", "只能选择一条操作项目");
                            return false;
                        }
                        return checkedRows[0];
                    }
                },

                /**
                 * 获取Grid已选择selarrrow多行项数组rowdata数据
                 * @alias jqgrid-getSelectedRowdatas
                 * @returns 已选行项rowdata数组
                 *
                 * @example $grid.getSelectedRowdatas()
                 */
                getSelectedRowdatas: function () {
                    var $grid = $(this);
                    var rowdatas = [];
                    var selarrrow = $grid.jqGrid('getGridParam', 'selarrrow');
                    if (selarrrow) {
                        $.each(selarrrow, function (i, rowid) {
                            var rowdata = $grid.jqGrid('getRowData', rowid);
                            rowdata.id = rowid;
                            rowdatas.push(rowdata);
                        })
                    } else {
                        var selrow = $grid.jqGrid('getGridParam', 'selrow');
                        if (selrow) {
                            var rowdata = $grid.jqGrid('getRowData', selrow);
                            rowdata.id = selrow;
                            rowdatas.push(rowdata);
                        }
                    }
                    return rowdatas;
                },

                /**
                 * 获取Grid已选择selrow单行项rowdata数据
                 * @alias jqgrid-getSelectedRowdata
                 * @returns 已选行项rowdata
                 *
                 * @example $grid.getSelectedRowdata()
                 */
                getSelectedRowdata: function () {
                    var $grid = $(this);
                    var selrow = $grid.jqGrid('getGridParam', 'selrow');
                    if (selrow) {
                        return $grid.jqGrid('getRowData', selrow);
                    }
                },

                //获取批量动态表格数据有save调用的脏数据行项
                //为了优化处理，框架添加行项脏数据标记，只有新数据或做了变更的数据才会组装提交到post参数
                getDirtyRowdatas: function () {
                    var $grid = $(this);

                    var dirtyRowdatas = [];
                    var colModel = $grid.jqGrid('getGridParam', 'colModel');
                    var editableCols = [];
                    $.each(colModel, function (i, cm) {
                        //把可编辑列添加到处理集合中
                        if (cm.editable) {
                            editableCols.push(cm.name);
                        }
                    });
                    var ids = $grid.jqGrid("getDataIDs");
                    var startNewIndex = 0;
                    $.each(ids, function (i, id) {
                        //如果id参数前缀为克隆操作设置的“-”或为空白，则累加数组索引下标
                        //计算出当前表格如果新增行项的起始下标
                        if (!Util.startWith(id, "-") && id != "") {
                            startNewIndex++;
                        }
                    });

                    //判断当前是否克隆数据操作
                    var clone = BooleanUtil.toBoolean($grid.attr("data-clone"));

                    $.each(ids, function (i, id) {
                        var rowdata = $grid.jqGrid("getRowData", id);
                        //判断当前行项是否脏数据：克隆的行项或变更了数据的行项
                        if (BooleanUtil.toBoolean(rowdata["extraAttributes.dirtyRow"])) {
                            if (Util.startWith(id, "-")) {
                                id = "";
                            }
                            var newrow = {
                                id: id
                            };
                            //收集每行可编辑列数据集合
                            $.each(editableCols, function (i, cname) {
                                newrow[cname] = rowdata[cname];
                            });
                            //_arrayIndex数组下标，对于编辑对象则初始化加载记录对应值;如果是新增行项则按照算法赋予顺序索引号
                            newrow["_arrayIndex"] = rowdata["_arrayIndex"];
                            //特定的operation操作标识，一般用于标记行项删除
                            //在ExtParametersInterceptor.java做对象移除统一处理
                            if (rowdata["extraAttributes.operation"]) {
                                newrow["extraAttributes.operation"] = rowdata["extraAttributes.operation"];
                            }
                            dirtyRowdatas.push(newrow);
                        }
                    });

                    //按照指定的前缀参数处理批量行项集合
                    var batchEntitiesPrefix = $grid.jqGrid("getGridParam", "batchEntitiesPrefix");
                    if (batchEntitiesPrefix) {
                        var revertRowdatas = {};
                        $.each(dirtyRowdatas, function (i, rowdata) {
                            //判断如果有_arrayIndex说明是编辑行项，否则是新增对象累加数组下标
                            var _arrayIndex = rowdata["_arrayIndex"];
                            delete rowdata["_arrayIndex"];
                            if (_arrayIndex == undefined || _arrayIndex == '') {
                                _arrayIndex = startNewIndex;
                                startNewIndex++;
                            }
                            //把基本的表格行项数据对象转换为struts后端能理解的数组下标结构
                            $.each(rowdata, function (k, v) {
                                revertRowdatas[batchEntitiesPrefix + "[" + _arrayIndex + "]." + k] = v;
                            });
                        });
                        return revertRowdatas;
                    }

                    return dirtyRowdatas;
                },

                //插入新行项对象并返回生成的行项标识
                insertNewRowdata: function (newdata) {
                    var $grid = $(this);

                    //当前表格第一条空行项
                    var firtEmptyRow = null;
                    var ids = $grid.jqGrid("getDataIDs");
                    $.each(ids, function (i, id) {
                        var rowdata = $grid.jqGrid("getRowData", id);
                        //当前行项没有脏数据标记则作为第一个空行项
                        if (!BooleanUtil.toBoolean(rowdata["extraAttributes.dirtyRow"])) {
                            firtEmptyRow = id;
                            return false;
                        }
                    });
                    //计算一个负数作为新增行项的id标识
                    var newid = -Math.floor(new Date().getTime() + Math.random() * 100 + 100);
                    //标记当前新增行项为脏数据
                    newdata["extraAttributes.dirtyRow"] = true;
                    if (firtEmptyRow) {
                        //如果有空行项，则添加到空行项位置之前
                        $grid.jqGrid('addRowData', newid, newdata, 'before', firtEmptyRow);
                    } else {
                        //否则添加最后一行
                        $grid.jqGrid('addRowData', newid, newdata, 'last');
                    }
                    return newid;
                },

                /**
                 * 快速修改设置正在编辑行项的各表单元素值
                 * @alias jqgrid-setEditingRowdata
                 * @param rowdata json结构数据，不用完整的行项数据可以是部分name和value值
                 * @param forceOverwrite 是否强制覆盖已有值,true=直接覆盖，false=如果已有值则跳过赋值
                 */
                setEditingRowdata: function (rowdata, forceOverwrite) {
                    var $grid = $(this);
                    var $rowContainer = $grid.find("tbody");
                    for (var key in rowdata) {
                        //按照json结构的key属性查找name属性匹配的表单元素
                        var el = "input[name='" + key + "'],select[name='" + key + "'],textarea[name='" + key + "']";
                        var $el = $rowContainer.find(el);
                        if (forceOverwrite == false) {
                            //如果值不为空，且不允许强制覆盖则跳过
                            if ($.trim($el.val()) != '') {
                                continue;
                            }
                        }
                        var val = rowdata[key];
                        $el.val(val).attr("title", val);
                        //如果是select组件则调用select2刷新
                        if ($el.is("select")) {
                            $el.select2({
                                openOnEnter: false,
                                placeholder: "请选择...",
                                //支持拼音匹配
                                matcher: function (term, text) {
                                    var mod = Pinyin.getCamelChars(text) + "";
                                    var tf1 = mod.toUpperCase().indexOf(term.toUpperCase()) == 0;
                                    var tf2 = text.toUpperCase().indexOf(term.toUpperCase()) == 0;
                                    return (tf1 || tf2);
                                }
                            });
                        }
                    }
                },

                /**
                 * 获取正在编辑行项的各表单元素值
                 * @alias jqgrid-getEditingRowdata
                 * @returns 各编辑元素内容组装的JSON结构数据
                 */
                getEditingRowdata: function () {
                    var $grid = $(this);
                    var $rowContainer = $grid.find("tbody");
                    var rowdata = {};
                    var elems = "input,select,textarea";
                    $rowContainer.find(elems).each(function () {
                        var $el = $(this);
                        //alert($el.attr("name") + ":" + $el.val());
                        rowdata[$el.attr("name")] = $el.val();
                    })
                    return rowdata;
                },

                /**
                 * 检测表格当前是否处于编辑模式
                 * @alias jqgrid-isEditingMode
                 * @returns 布尔值
                 */
                isEditingMode: function (msg) {
                    var $grid = $(this);
                    var editingRow = $grid.find('tr[editable="1"]');
                    if (editingRow.size() > 0) {
                        if (msg == undefined) {
                            return true;
                        }
                        if (msg === true) {
                            alert("请先保存或取消正在编辑的表格数据行项后再操作");
                        } else {
                            alert(msg);
                        }
                        return true;
                    }
                    return false;
                },

                /**
                 * 列求和帮助方法，添加小数位保留处理，默认两位
                 * @alias jqgrid-sumColumn
                 * @param col 求和列属性名称
                 * @param decimalPlaces 保留小数位数
                 * @returns 求和结果数字
                 */
                sumColumn: function (col, decimalPlaces) {
                    var $grid = $(this);
                    if (decimalPlaces == undefined) {
                        decimalPlaces = 2;
                    }
                    var nData = $grid.jqGrid('getCol', col, false, 'sum');
                    var nDecimal = Math.pow(10, decimalPlaces);
                    return Math.round(nData * nDecimal) / nDecimal;
                },

                //获取grid缓存的由关联的查询form提交的查询参数值
                //对需要记录的表单元素添加class: grid-param-data
                getDataFromBindSeachForm: function (name) {
                    var $grid = $(this);
                    var bindSearchFormData = $grid.jqGrid('getGridParam', 'bindSearchFormData');
                    var val = bindSearchFormData[name];
                    return val;
                }
            });

            gridDefaultInited = true;
        },

        //初始化特定区域下面的data-grid标记的jqGrid元素
        initAjax: function ($container) {
            // initialize here something.

            if ($container == undefined) {
                $container = $("body");
            }

            $('table[data-grid="table"],table[data-grid="items"]', $container).each(function () {
                Grid.initGrid($(this));
            });
        },

        /**
         * 初始化指定grid元素，默认取元素的gridOptions数据值，options为可选覆盖参数
         *
         * 表格属性参数：
         * @property url 发起AJAX数据请求的URL
         * @property colModel 列属性参数对象，详见随后参数说明
         * @property postData 固定追加的查询参数JSON结构数据对象
         * @property editurl inline编辑模式POST提交请求的处理url地址，没有此参数则工具条屏蔽相关编辑操作按钮
         * @property editrulesurl inline编辑模式自动表单校验规则请求url，一般固定为：WEB_ROOT + '/admin/util/validate?clazz=$&#123;clazz}'
         * @property delurl inline删除操作请求url，没有此参数则工具条屏蔽相关删除操作按钮
         * @property multiselect 是否显示多选复选框列，默认true
         * @perperty gridDnD 如果为true，开启鼠标拖放移动行项功能，一般用于类似菜单父子结构数据的管理操作
         * @perperty subGrid 如果为true，行项左侧出现一个展开的加号图标点击触发执行subGridRowExpanded加载子表格
         * @perperty addable 是否允许添加操作，默认为true，如果为false则不会显示相关的添加操作按钮图标
         * @perperty sortname 排序列名称，默认为id
         * @perperty sortorder 排序顺序，默认为desc
         *
         * 表格列属性参数：
         * @property label 列标题label
         * @property name 从JSON响应数据取值的属性名称
         * @property index 点击排序或查询回传服务器的属性名称，可选，默认值等于name值
         * @property width 列宽度，如果是自动百分比模式会取所有宽度按照百分比换算
         * @property editable 列单元格是否支持inline编辑，editurl有值才有意义
         * @property align 单元格数据对齐模式：left|right|center，基本原则是等长或简短内容center居中；金额类型一般right对齐；其余不等长文本一般left对齐
         * @property formatter 单元格类型，用于合理的控制显示格式和排序等特性，指定不同formatter格式一般会同步自动设定相关的width，align等属性，因此一般建议合理设置。<br>
         * 可选值说明：
         *     <pre>
         *      currency=货币类型数据，逗号千分位分隔符，默认两位小数 ，自动设定width=80和align=right
         *      percentage=百分比类型数据，value为小数乘以100转换为%格式，自动设定width=50和align=right
         *      integer=整数类型，自动设定width=60和align=center
         *      number=数字类型，整数或浮点数 ，自动设定width=60和align=right
         *      date=ISO年月日格式，自动设定width=120和align=center
         *      timestamp=ISO年月日时分秒格式，自动设定width=150和align=center
         *      showlink=以AJAX对话框模式请求URL，需自行合理设置width属性，子参数示意：
         *          formatoptions.baseLinkUrl : WEB_ROOT + '/admin/user/view',
         *          formatoptions.title : WEB_ROOT + '/admin/user/view',
         *      checkbox=复选框，一般用于boolean类型 ，自动设定width=60和align=center，子参数示意：
         *          searchoptions.valueJsonString : '&#123;"true":"正常","false":"中止"}'
         *      select=下拉选项框，一般用于枚举类型属性，子参数示意：
         *          searchoptions.value : function()&#123;
         *               //返回key-value结构对象数组
         *          }
         *          searchoptions.valueJsonString : '&lt;tags:json value="$&#123;educationLevel}"/>'
         *      </pre>
         * @property formatter inline编辑模式元素类型，默认会按照formatter自动设定合理类型，其余一般为text=单行文本， textarea=多行文本
         *
         */
        initGrid: function (grid, options) {

            //判断是否进行jqGrid全局初始化
            if (!gridDefaultInited) {
                Grid.initGridDefault();
            }

            var $grid = $(grid);
            //判断是否已初始化过
            if ($grid.hasClass("ui-jqgrid-btable")) {
                return;
            }

            //如果没有定义id则基于当前时间戳生成一个id属性值
            if ($grid.attr("id") == undefined) {
                $grid.attr("id", "grid_" + new Date().getTime());
            }
            var gridID = $grid.attr("id");

            //取表格基本设置参数
            if (options == undefined && $grid.data("gridOptions") == undefined) {
                alert("Grid options undefined: class=" + $grid.attr("class"));
                return;
            }
            //基于基本参数和传入参数合并得到用户设定参数对象
            var userOptions = $.extend(true, {}, $grid.data("gridOptions"), options);

            //表格对象实体名字，一般用于构造相关提示信息
            var gridEntityName = userOptions.entityName;

            //alert("url: " + gridOptions.url);
            //表格类型：
            //  items=指主子表界面的子表表格，相关操作都是基于当前表格本地数据处理，然后连同主表对象表达批量提交数据; 
            //  其他主表格就是直接的ajax数据操作模式
            var gridType = $grid.attr("data-grid");
            //分页条对象id
            var pagerId = null;
            //基于当前表格id组装定义右键菜单对象的id
            var contextMenuId = gridID + "-context-menu-container";
            //容纳下拉或右键菜单的UL对象。表格的左侧下拉菜单和右键菜单，本质上调用同一菜单列表对象
            var $menuUL = null;

            //子表编辑模式默认不支持多选
            var multiselect = (gridType == 'items' ? false : true);
            //子表编辑模式默认不支持过滤工具条
            var filterToolbar = (gridType == 'items' ? false : true);

            //基于组件全局参数，默认参数和指定参数合并得到表格参数对象
            var gridOptions = $.extend(true, {}, $.jgrid.defaults, {
                //默认设置各数字类型元素默认值为空白字符串
                formatter: {
                    integer: {
                        defaultValue: ''
                    },
                    number: {
                        decimalSeparator: ".",
                        thousandsSeparator: ",",
                        decimalPlaces: 2,
                        defaultValue: ''
                    },
                    currency: {
                        decimalSeparator: ".",
                        thousandsSeparator: ",",
                        decimalPlaces: 2,
                        defaultValue: ''
                    }
                },
                cmTemplate: {
                    //子表编辑模式默认不支持排序
                    sortable: gridType == 'items' ? false : true
                },
                viewsortcols: gridType == 'items' ? [true, 'vertical', false] : [true, 'vertical', true],
                altRows: gridType == 'items' ? false : true,
                hoverrows: gridType == 'items' ? false : true,
                pgbuttons: gridType == 'items' ? false : true,
                pginput: gridType == 'items' ? false : true,
                rowList: gridType == 'items' ? [] : [10, 15, 20, 50, 100, 200, 500, 1000, 2000],
                inlineNav: {
                    //默认设置inline编辑的增删改查功能是否启用
                    add: userOptions.editurl || gridType == 'items' ? true : false,
                    edit: userOptions.editurl || gridType == 'items' ? true : false,
                    del: userOptions.delurl || gridType == 'items' ? true : false,
                    restoreAfterSelect: gridType == 'items' ? false : true,
                    //inline新增操作默认参数
                    addParams: {
                        addRowParams: {
                            extraparam: {},
                            restoreAfterError: false,
                            beforeSaveRow: function (rowid) {
                                if (gridOptions.beforeInlineSaveRow) {
                                    gridOptions.beforeInlineSaveRow.call($grid, rowid);
                                }
                            },
                            aftersavefunc: function (rowid, xhr) {

                                //子表本地新增数据保存处理
                                if (gridOptions.editurl == "clientArray") {
                                    $grid.jqGrid('resetSelection');
                                    if (gridOptions.afterInlineSaveRow) {
                                        gridOptions.afterInlineSaveRow.call($grid, rowid);
                                    }
                                    //新增完毕后稍等片刻触发新增按钮点击，实现连续新增数据的效果
                                    setTimeout(function () {
                                        $("#" + pagerId).find(".ui-pg-div span.ui-icon-plus").click();
                                    }, 200);
                                    return;
                                }
                                var response = jQuery.parseJSON(xhr.responseText);
                                if (response.type == "success" || response.type == "warning") {
                                    Global.notify(response.type, response.message);
                                    var newid = response.data.id;

                                    $grid.jqGrid('refresh');

                                    //以返回的实体对象id更新行项自动生成的id属性
                                    //$grid.find("#" + rowid).attr("id", newid);
                                    //$grid.jqGrid('resetSelection');
                                    //选中当前新增项目，用于后续连续新增复制数据
                                    //$grid.jqGrid('setSelection', newid);
                                    if (gridOptions.afterInlineSaveRow) {
                                        gridOptions.afterInlineSaveRow.call($grid, rowid);
                                    }
                                    //新增完毕后稍等片刻触发新增按钮点击，实现连续新增数据的效果
                                    setTimeout(function () {
                                        $("#" + gridID + "_iladd").find("span.fa").click();
                                    }, 200);
                                } else if (response.type == "failure" || response.type == "error") {
                                    Global.notify("error", response.message);
                                } else {
                                    Global.notify("error", "数据处理异常，请联系管理员");
                                }
                            },
                            errorfunc: function (rowid, xhr) {
                                var response = jQuery.parseJSON(xhr.responseText);
                                alert(response.message);
                                Global.notify("error", response.message);
                            }
                        }
                    },
                    //inline编辑操作默认参数
                    editParams: {
                        restoreAfterError: false,
                        beforeSaveRow: function (rowid) {
                            if (gridOptions.beforeInlineSaveRow) {
                                gridOptions.beforeInlineSaveRow.call($grid, rowid);
                            }
                        },
                        oneditfunc: function (rowid) {
                            //iCol记录最近选取所在列，在进入行项编辑模式时自动取上次编辑所在列获取焦点
                            //使用户操作具有更好的连续性体验
                            var iCol = $grid.jqGrid('getGridParam', 'iCol');
                            var cm = $grid.jqGrid('getGridParam', 'colModel')[iCol];
                            var $tr = $grid.find("tr#" + rowid);
                            var $td = $tr.find("> td:eq(" + iCol + ")");
                            var $el = $td.find("input:visible:first");
                            if ($el.size() > 0 && $el.attr("readonly") == undefined) {
                                //如果有之前定位列对应行可见输入元素，则把定位列元素获取焦点
                                setTimeout(function () {
                                    $el.focus();
                                }, 200);
                            } else {
                                //如果没有，则取第一个可见编辑元素获取焦点
                                $tr.find("input:visible:enabled:first").focus();
                            }
                        },
                        aftersavefunc: function (rowid, xhr) {
                            var success = true;
                            if (gridOptions.editurl != "clientArray") {
                                //如果是非子表本地数据保存模式，即AJAX提交数据，全局的显示提示信息
                                var response = jQuery.parseJSON(xhr.responseText);
                                if (response.type == "success" || response.type == "warning") {
                                    Global.notify(response.type, response.message);
                                } else if (response.type == "failure" || response.type == "error") {
                                    Global.notify("error", response.message);
                                    success = false;
                                } else {
                                    Global.notify("error", "数据处理异常，请联系管理员");
                                    success = false;
                                }
                            }
                            if (success) {
                                //行项保存成功后，回调afterInlineSaveRow方法，比如用于基于保存的行项数据刷新汇总数据
                                if (gridOptions.afterInlineSaveRow) {
                                    gridOptions.afterInlineSaveRow.call($grid, rowid);
                                }
                                if (gridOptions.editurl != "clientArray") {
                                    //如果是主表编辑模式，定位下一行项并选中触发编辑，实现连续编辑
                                    var $ntr = $grid.find("tr.jqgrow[id='" + rowid + "']").next("tr");
                                    if ($ntr.size() > 0) {
                                        var nid = $ntr.attr("id");
                                        $grid.jqGrid('resetSelection');
                                        $grid.jqGrid('setSelection', nid);
                                        //稍作延时处理触发新行项进入自动进入编辑状态
                                        setTimeout(function () {
                                            $("#" + pagerId).find(".ui-pg-div span.ui-icon-pencil").click();
                                        }, 200);
                                    }
                                }
                            }
                        },
                        errorfunc: function (rowid, xhr) {
                            if (xhr.status == 404) {
                                Global.notify("error", xhr.status + ": 请求地址未找到");
                            } else {
                                var response = jQuery.parseJSON(xhr.responseText);
                                Global.notify("error", response.message);
                            }
                        }
                    }
                },
                inlineEditing: {
                    serializeSaveData: function (data) {
                        //如果是新增数据，则强制把id参数置空以便服务端处理
                        if (data.oper === 'add') {
                            data.id = '';
                        }
                        data["extraAttributes.dirtyRow"] = true;
                        return data;
                    }
                },
                //控制是否显示快速过滤工具条
                filterToolbar: filterToolbar,
                //是否多选模式
                multiselect: multiselect,
                //是否启用右键菜单
                contextMenu: true,
                //是否启用列选择排序操作
                columnChooser: true,
                //是否启用本页数据导出Excel操作
                exportExcelLocal: true,
                loadBeforeSend: function () {
                    //发送请求之前加载blockUI效果
                    // App.blockUI({
                    //     target: $grid.closest(".ui-jqgrid"),
                    //     animate: true,
                    //     overlayColor: 'none'
                    // });
                },
                subGridBeforeExpand: function () {
                    //设置子表格高度自动扩展，使其可以自动显示全部子表格数据不用出现垂直滚动条
                    var $bdiv = $grid.closest(".ui-jqgrid-bdiv");
                    $bdiv.css({
                        "height": "auto"
                    });
                },
                beforeProcessing: function (data) {
                    //为克隆数据设置负数id属性，以便grid组件识别各行项，同时提交时id置空以新增数据提交
                    if (data && data.content) {
                        var cnt = 1000;
                        $.each(data.content, function (i, item) {
                            if (item.extraAttributes && item.extraAttributes.dirtyRow) {
                                item.id = -(cnt++);
                            }
                        });
                        //特殊处理BaseService.findByGroupAggregate接口暂时没法获取总记录数而直接返回一个很大数字
                        //如果是汇总查询则不显示总记录数
                        if (data.totalElements >= (2147473647 - 10000)) {
                            $grid.jqGrid('setGridParam', {
                                recordtext: "{0} - {1}\u3000"
                            })
                        }
                    }
                },
                gridComplete: function () {

                },
                loadComplete: function (data) {
                    //移除遮罩
                    //App.unblockUI($grid.closest(".ui-jqgrid"));

                    //没有数据，直接返回
                    if (data == undefined) {
                        return;
                    }

                    //默认要求所有表格返回数据都以JSON分页格式，所以在Controller层面确保构造返回的是分页格式数据
                    if (data.total == undefined && data.totalElements == undefined) {
                        alert("表格数据格式不正确");
                        return;
                    }
                    if (data && data.content) {
                        //基于已有行项数据设置_arrayIndex属性值
                        //此值和Struts返回的序列化JSON数据顺序一致从而实现数据提交到数组对象的一一对应
                        $.each(data.content, function (i, item) {
                            $grid.setRowData(item.id, {
                                '_arrayIndex': i
                            })
                        });
                        //特殊处理BaseService.findByGroupAggregate接口暂时没法获取总记录数而直接返回一个很大数字
                        //如果是汇总查询则禁用“最后一页”按钮, 不显示总页数
                        if (data.totalElements >= (2147473647 - 10000)) {
                            $grid.closest(".ui-jqgrid").find(".ui-pg-table td[id^='last_']").addClass("ui-state-disabled");
                            $grid.closest(".ui-jqgrid").find(".ui-pg-table .ui-pg-input").each(function () {
                                $(this).parent().html($(this));
                            });
                        }
                    }

                    //如果是子表模式并且允许添加行项，则默认初始化几个行项免得表格界面太空
                    if (gridType == 'items' && gridOptions.inlineNav.add != false) {
                        for (var i = 1; i <= 3; i++) {
                            $grid.addRowData(-i, {});
                        }
                    }

                    //为每个行项构造右键菜单
                    if (globalContextMenuOption == 'enable' && gridOptions.contextMenu && $menuUL.find("li").length > 0) {
                        $grid.find("tr.jqgrow").each(function () {
                            $(this).contextmenu({
                                target: "#" + contextMenuId,
                                onItem: function (e, item) {
                                    var idx = $(item).attr("role-idx");
                                    $menuUL.find('a[role-idx="' + idx + '"]').click();
                                    return true;
                                }
                            })
                        })
                    }

                    //指定列进行默认的sum汇总显示，参数为数组形式的列名
                    if (gridOptions.footerrow) {
                        if (gridOptions.footerLocalDataColumn) {
                            $.each(gridOptions.footerLocalDataColumn, function (i, n) {
                                var sum = $grid.jqGrid('sumColumn', n);
                                var ob = [];
                                ob[n] = sum;
                                $grid.footerData('set', ob);
                            });
                        } else {
                            $.each(gridOptions.colModel, function (i, col) {
                                if (col.footersum != undefined && col.footersum == false) {
                                    return;
                                }
                                if (col.formatter == 'integer' || col.formatter == 'currency' || col.formatter == 'number') {
                                    var sum = $grid.jqGrid('sumColumn', col.name);
                                    var ob = [];
                                    ob[col.name] = sum;
                                    $grid.footerData('set', ob);
                                }
                            })
                        }
                    }

                    //默认选中指定行
                    if ($grid.attr("data-selected")) {
                        $grid.jqGrid("setSelection", $grid.attr("data-selected"), false);
                    }

                    //回调用户指定的加载完成事件
                    var userLoadComplete = userOptions.userLoadComplete;
                    if (userLoadComplete) {
                        userLoadComplete.call($grid, data);
                    }
                    // Handle Hower Dropdowns
                    //TODO $('[data-hover="dropdown"]', $grid.closest(".ui-jqgrid")).dropdownHover();
                },
                //行项选中之前事件
                beforeSelectRow: function (id) {
                    if (gridOptions.inlineNav.restoreAfterSelect == false) {
                        //在选中把之前选择的数据触发保存处理
                        var previd = $grid.jqGrid('getGridParam', 'selrow');
                        var editable = $grid.find("tr#" + previd).attr("editable");
                        if (previd && previd != id && editable == "1") {
                            $("#" + pagerId).find(".ui-pg-div span.ui-icon-disk").click();
                            return false;
                        }
                    }
                    return true;
                },
                //行项选中事件
                onSelectRow: function (id, stat, e) {
                    $grid.find("tr.jqgrow").attr("tabindex", -1);
                    $grid.find("tr.jqgrow[id='" + id + "']").attr("tabindex", 0);
                    //如果是子表模式直接进入编辑状态
                    if (gridType == "items") {
                        $("#" + pagerId).find(".ui-pg-div span.ui-icon-pencil").click();
                    }
                },
                //单元格选中事件
                onCellSelect: function (rowid, iCol) {
                    //记录当前单元格列号，用于后续连续编辑进入下一行定位默认获取焦点的列元素
                    $grid.jqGrid('setGridParam', {
                        'iCol': iCol
                    });
                },
                //鼠标双击事件
                ondblClickRow: function (rowid, iRow, iCol, e) {
                    //如果有页面编辑按钮，则双击触发页面编辑显示
                    var $fulledit = $("#" + pagerId).find("i.fa-edit").parent("a");
                    if ($fulledit.size() > 0) {
                        $fulledit.click();
                    } else {
                        //子表模式单击就已进入编辑模式，所以双击不做处理
                        if (gridType != "items") {
                            var $pencil = $("#" + pagerId).find(".ui-pg-div span.ui-icon-pencil");
                            //如果没有页面编辑按钮并且是主表模式，则触发inline行项编辑模式
                            if ($pencil.size() > 0) {
                                $pencil.click();
                            } else {
                                //如果也没有inline编辑按钮，则触发查看按钮事件
                                $("#" + pagerId).find("i.fa-credit-card").parent("a").click();
                            }
                        }
                    }
                    e.stopPropagation();
                },

                editcol: 'display'
            }, userOptions);

            //假如url参数是函数形式动态返回则以函数调用形式获取url参数值
            if ($.isFunction(gridOptions.url)) {
                gridOptions.url = gridOptions.url.call($grid);
            }
            //假如参数中没有定义url，则从元素的data-url属性中获取
            if (gridOptions.url == undefined) {
                gridOptions.url = $grid.attr("data-url");
            }
            //如果没有url参数，则设置本地数据处理模式
            if (gridOptions.url == undefined) {
                gridOptions.datatype = "local";
            } else {
                gridOptions.url = Util.smartParseURL(gridOptions.url);
            }
            //智能处理url context前缀
            if (gridOptions.editurl) {
                gridOptions.editurl = Util.smartParseURL(gridOptions.editurl);
            }
            if (gridOptions.editrulesurl) {
                gridOptions.editrulesurl = Util.smartParseURL(gridOptions.editrulesurl);
            }
            if (gridOptions.fullediturl) {
                gridOptions.fullediturl = Util.smartParseURL(gridOptions.fullediturl);
            }
            if (gridOptions.delurl) {
                gridOptions.delurl = Util.smartParseURL(gridOptions.delurl);
            }

            //如果元素i定义了data-readonly属性则默认关闭inline编辑按钮选项
            if (BooleanUtil.toBoolean($grid.attr("data-readonly"))) {
                gridOptions.inlineNav.add = false;
                gridOptions.inlineNav.edit = false;
                gridOptions.inlineNav.del = false;
            }

            //如果没有设定pager分页条对象则基于grid的id组装设置
            //并且动态生成分页条容器div元素添加到表格对象之后
            if (gridOptions.pager == undefined || gridOptions.pager) {
                pagerId = gridID + "_pager";
                $("<div id='" + pagerId + "'/>").insertAfter($grid);
                gridOptions.pager = "#" + pagerId;
            } else {
                //如果定义pager=false没有分页条，则把toppager也默认设定为false
                gridOptions.toppager = false;
            }

            //如果有toppager，则设置其对应元素表达式
            if (gridOptions.toppager) {
                gridOptions.toppager = "#" + gridID + "_toppager";
            }

            //如果是treeGrid模式，则默认不显示行项顺序号
            if (gridOptions.treeGrid) {
                gridOptions.rownumbers = false;
            }

            //子表模式设置相关编辑参数为本地数组存取
            if (userOptions.editurl == undefined && gridType == "items") {
                gridOptions.editurl = "clientArray";
            }
            if (userOptions.delurl == undefined && gridType == "items") {
                gridOptions.delurl = "clientArray";
            }
            //设置单元格提交模式
            if (gridOptions.editurl == 'clientArray') {
                gridOptions.cellsubmit = gridOptions.editurl;
            } else {
                gridOptions.cellurl = gridOptions.editurl;
            }

            //计算各列width之和
            var userTotalWidth = 0;
            //标识是否有冻结列
            var userFrozen = false;

            //缺省的查询参数
            var defaultSearchOptions = [];

            //列name集合，用于后续判断一些默认列是否存在
            var colModelNames = [];
            $.each(gridOptions.colModel, function (i, col) {
                //记录name列名
                colModelNames.push(col.name);
            })
            if ($.inArray("id", colModelNames)) {
                gridOptions.colModel.push({
                    label: "流水号",
                    name: 'id',
                    width: 50,
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("流水号");
                }
            }
            if ($.inArray("createdBy", colModelNames)) {
                gridOptions.colModel.push({
                    label: "创建者",
                    name: 'createdBy',
                    width: 50,
                    align: 'center',
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("创建者");
                }
            }
            if ($.inArray("createdDate", colModelNames)) {
                gridOptions.colModel.push({
                    label: "创建时间",
                    name: 'createdDate',
                    formatter: 'timestamp',
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("创建时间");
                }
            }
            if ($.inArray("lastModifiedBy", colModelNames)) {
                gridOptions.colModel.push({
                    label: "最后更新者",
                    name: 'lastModifiedBy',
                    width: 50,
                    align: 'center',
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("最后更新者");
                }
            }
            if ($.inArray("lastModifiedDate", colModelNames)) {
                gridOptions.colModel.push({
                    label: "最后更新时间",
                    name: 'lastModifiedDate',
                    formatter: 'timestamp',
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("最后更新时间");
                }
            }

            //默认追加display列
            gridOptions.colModel.push({
                label: "标题",
                name: 'display',
                hidden: true,
                search: false,
                hidedlg: true
            });
            if (gridOptions.colNames) {
                gridOptions.colNames.push("标题");
            }

            $.each(gridOptions.colModel, function (i, col) {

                if (col.frozen) {
                    userFrozen = true;
                }

                //默认设定的列参数
                col = $.extend(true, {}, {
                    editoptions: {
                        rows: 1
                    },
                    searchoptions: {
                        //filterToolbar查询输入元素右侧的清除按钮
                        clearSearch: false,
                        //隐藏列是否出现在查询属性列表
                        searchhidden: true,
                        defaultValue: '',
                        //构造ajax请求类型的下拉框数据组装
                        buildSelect: function (json) {
                            if (json == null) {
                                json = data;
                            }
                            var html = "<select>";
                            html += "<option value=''></option>";
                            for (var key in json) {
                                key = key + '';
                                html += ("<option value='" + key + "'>" + json[key] + "</option>");
                            }
                            html += "</select>";
                            return html;
                        }
                    }
                }, col);

                //开发阶段暂未明确实现标记TODO的列，前端统一显示TODO以提示修正
                if (col.name.toLowerCase() == 'todo') {
                    col.formatter = function (cellValue, options, rowdata, action) {
                        return "TODO";
                    }
                }

                //根据列的responsive定义动态控制列的显示hidden与否
                //responsive值规则同bootstrap的响应式定义值类似
                if (col.responsive) {
                    if (col.hidden == undefined) {
                        var windowWidth = $(window).width();
                        var responsive = col.responsive;
                        if (responsive == 'sm') {
                            if (windowWidth < 768) {
                                col.hidden = true;
                            }
                        } else if (responsive == 'md') {
                            if (windowWidth < 992) {
                                col.hidden = true;
                            }
                        } else if (responsive == 'lg') {
                            if (windowWidth < 1200) {
                                col.hidden = true;
                            }
                        }
                    }
                }

                //货币类型格式默认参数
                if (col.formatter == 'currency') {
                    col = $.extend({}, {
                        width: 80,
                        align: 'right'
                    }, col);
                    col.formatoptions = $.extend({}, col.formatoptions, {
                        decimalSeparator: ".",
                        //千分位分隔符
                        thousandsSeparator: ",",
                        //默认保留显示两位小数
                        decimalPlaces: 2,
                        prefix: "",
                        defaultValue: ""
                    });
                    //默认查询选项定义
                    col.searchoptions = $.extend({}, col.searchoptions, {
                        sopt: ['eq', 'ne', 'ge', 'le', 'gt', 'lt']
                    });
                }

                //百分比类型格式，用于利润率类似的百分比格式显示
                //原始小数值转换为%符号格式显示
                if (col.formatter == 'percentage') {
                    col = $.extend(true, {
                        width: 50,
                        align: 'right'
                    }, col);
                    col.formatter = function (cellValue, options, rowdata, action) {
                        if (cellValue) {
                            return Math.round(cellValue * 10000) / 100 + "%"
                        } else {
                            return cellValue
                        }
                    }
                }

                //日期类型
                if (col.stype == 'date' || col.sorttype == 'date' || col.formatter == 'date' || col.formatter == 'timestamp' || col.formatter == 'shortTimestamp') {
                    if (col.formatter == 'timestamp') {
                        //时间类型设置特定格式参数
                        col = $.extend(true, {
                            width: 150,
                            fixed: true,
                            align: 'center',
                            formatoptions: {
                                srcformat: 'Y-m-d H:i:s',
                                newformat: 'Y-m-d H:i:s'
                            }
                        }, col);
                        //jqGrid没有timestamp类型概念，设定特定参数后，最终再转换为jqGrid只支持的date类型
                        col.formatter = 'date';
                    } else if (col.formatter == 'shortTimestamp') {
                        //时间类型设置特定格式参数
                        col = $.extend(true, {
                            width: 150,
                            fixed: true,
                            align: 'center',
                            formatoptions: {
                                srcformat: 'Y-m-d H:i',
                                newformat: 'Y-m-d H:i'
                            }
                        }, col);
                        //jqGrid没有timestamp类型概念，设定特定参数后，最终再转换为jqGrid只支持的date类型
                        col.formatter = 'date';
                    } else {
                        col = $.extend(true, {
                            width: 120,
                            fixed: true,
                            align: 'center',
                            formatoptions: {
                                newformat: 'Y-m-d'
                            }
                        }, col);
                    }

                    //查询参数设定
                    col.searchoptions = $.extend({}, col.searchoptions, {
                        //默认区间段查询模式，详见PropertyFilter.java
                        sopt: ['bt', 'eq', 'ne', 'ge', 'le', 'gt', 'lt'],
                        dataInit: function (elem) {
                            var $elem = $(elem);
                            //对输入元素调用daterangepicker选取查询区间段，传递给服务器做between查询
                            $(elem).daterangepicker($.extend(true, $.fn.daterangepicker.defaults, col.searchoptions.daterangepicker), function (start, end) {
                                //选取完成后元素获取焦点，以便用户直接回车触发查询
                                $(elem).focus();
                                $grid.trigger("triggerToolbar");
                            });
                            //取消默认的获取焦点触发弹出日期的事件，只保留点击事件弹出组件
                            $(elem).off("focus");
                        }
                    });

                    //编辑参数设定
                    col.editoptions = $.extend(col.editoptions, {
                        dataInit: function (elem) {
                            if (col.editoptions.time) {
                                //如果是时间类型则构造日期时间选取组件
                                $(elem).datetimepicker({
                                    language: 'zh-CN',
                                    autoclose: true,
                                    todayBtn: true,
                                    minuteStep: 10,
                                    format: 'yyyy-mm-dd hh:ii'
                                });
                            } else {
                                //否则构造普通的日期选取组件
                                $(elem).datepicker({
                                    language: 'zh-CN',
                                    autoclose: true,
                                    todayBtn: true,
                                    format: 'yyyy-mm-dd'
                                });
                            }
                        }
                    });
                }

                //详见本文件中对于showlink的扩展实现定义
                if (col.formatter == 'showlink') {
                    col = $.extend(true, {
                        formatoptions: {
                            //添加idValue属性处理以支持从特定的rowdata属性中取值作为链接的id值
                            //比如链接显示一个关联对象的查看界面，则可以把“abc.id”作为idValue值
                            idValue: 'id',
                            //默认以AJAX弹出创建显示url界面信息，一般最常用的点击链接信息查看界面
                            target: 'modal-ajaxify'
                        }
                    }, col);
                }

                //整数类型格式化定义
                if (col.formatter == 'integer') {
                    col = $.extend(true, {
                        width: 60,
                        align: 'center',
                        formatoptions: {
                            defaultValue: "",
                            thousandsSeparator: ""
                        },
                        searchoptions: {
                            sopt: ['eq', 'ne', 'ge', 'le', 'gt', 'lt']
                        }
                    }, col);
                }

                //带小数的数字类型格式定义
                if (col.formatter == 'number') {
                    col.sorttype = col.formatter;
                    col.edittype = col.formatter;
                    col = $.extend(true, {
                        width: 60,
                        align: 'right',
                        formatoptions: {
                            defaultValue: ""
                        },
                        searchoptions: {
                            sopt: ['eq', 'ne', 'ge', 'le', 'gt', 'lt']
                        }
                    }, col);
                }

                //id属性特定参数设定
                if (col.name == 'id') {
                    col = $.extend(true, {
                        width: 80,
                        align: 'center',
                        title: false,
                        formatter: function (cellValue, options, rowdata, action) {
                            //如果id数据太长如uuid类型，则裁剪显示部分避免占用太宽位置
                            //同时实现一个点击展开效果以便用户在必要时候可以点击查看完整数据
                            if (cellValue && cellValue.length > 10) {
                                var len = cellValue.length;
                                var display = cellValue.substring(len - 5, len);
                                return "<span data='" + cellValue + "' onclick='$(this).html($(this).attr(\"data\"))'>..." + display + "</span>";
                            } else {
                                return cellValue;
                            }
                        },
                        //默认冻结此列
                        frozen: true
                    }, col);
                    col.searchoptions = $.extend(true, col.searchoptions, {
                        sopt: ['eq', 'ne', 'ge', 'le', 'gt', 'lt']
                    });
                }

                if (col.formatter == 'checkbox') {
                    col.edittype = col.formatter;
                    //checkbox类型查询元素以select下拉框显示”是“，”否“选取项
                    col = $.extend(true, {
                        width: 60,
                        align: 'center',
                        formatter: "checkbox",
                        stype: 'select'
                    }, col);
                    col.searchoptions.value = {
                        "": "",
                        "true": "是",
                        "false": "否"
                    };
                    col.searchoptions.sopt = ['eq', 'ne'];
                    //编辑元素以true/false来控制显示和数据提交值
                    col.editoptions.value = "true:false";
                }

                if (col.edittype == undefined || col.edittype == "text" || col.edittype == "select" || col.edittype == "textarea") {
                    //取用户给定的dataInit函数，然后在此基础上包裹一层全局的元素初始化处理
                    var userDataInit = col.editoptions.dataInit;
                    col.editoptions = $.extend(col.editoptions, {
                        dataInit: function (elem) {
                            var $elem = $(elem);
                            //移除jqGrid给的editable class定义，改成bootstrap的form-control以便页面整体编辑元素显示效果一致性
                            $elem.removeClass("editable").addClass("form-control").attr("autocomplete", "off").css({
                                width: "100%"
                            });
                            //调用用户给定的初始化逻辑
                            if (userDataInit) {
                                userDataInit.call(this, elem);
                            }

                            //假如设定元素创建后就不可更新
                            if (col.editoptions.updatable == false) {
                                var rowdata = $grid.jqGrid('getSelectedRowdata');
                                if (rowdata && rowdata.id) {
                                    //如果是id有值即已有数据，则直接把编辑元素disabled
                                    $elem.attr("disabled", true);
                                } else {
                                    //如果是新建数据，则设定默认的提示信息
                                    if (!$elem.attr("placeholder")) {
                                        $elem.attr("placeholder", "创建后不可修改");
                                        $elem.attr("title", "创建后不可修改");
                                    }
                                }
                            }
                            if ($elem.is("input[type='text']")) {
                                //对于文本输入元素，焦点离开是自动把输入内容trim一下
                                //常用于粘贴输入时去掉头尾多余的空格
                                $elem.blur(function () {
                                    $elem.val($.trim($elem.val()));
                                })
                            }
                            //select类型元素构造为select2组件，提供更强大的选取支持功能
                            if ($elem.is("select")) {
                                $elem.select2({
                                    openOnEnter: false,
                                    placeholder: "请选择...",
                                    matcher: function (term, text) {
                                        //支持拼音匹配输入，直接把元素值内容转换成拼音进行匹配
                                        var mod = Pinyin.getCamelChars(text) + "";
                                        var tf1 = mod.toUpperCase().indexOf(term.toUpperCase()) == 0;
                                        var tf2 = text.toUpperCase().indexOf(term.toUpperCase()) == 0;
                                        return (tf1 || tf2);
                                    }
                                });
                            }

                            //如果元素定义了spellto编辑属性，则当前元素值变更后把当前元素值同步转换对应拼音值去更新spellto指定元素
                            //主要用于一些输入名称对应自动以拼音作为对应代码属性值，当然对应代码属性输入元素可以再基于生成的拼音修改数据
                            if (col.editoptions.spellto) {
                                $elem.change(function () {
                                    var data = {};
                                    //调用拼音组件生成对应拼音值
                                    data[col.editoptions.spellto] = Pinyin.getCamelChars($.trim($elem.val()));
                                    $grid.jqGrid("setEditingRowdata", data);
                                });
                            }
                        }
                    });
                }

                //下拉框类型元素
                if (col.formatter == 'select') {
                    if (col.searchoptions.sopt == undefined) {
                        col.searchoptions.sopt = ['eq', 'ne'];
                    }

                    if (col.edittype == undefined) {
                        col.edittype = 'select';
                    }
                    if (col.stype == undefined) {
                        col.stype = 'select';
                    }

                    //如果编辑value属性是方法，则调用方法并把返回值再赋值给vlaue属性
                    if (typeof col.searchoptions.value === 'function') {
                        col.searchoptions.value = col.searchoptions.value.call($grid);
                    }

                    //如果编辑value属性是方法，则调用方法并把返回值再赋值给vlaue属性
                    if (col.searchoptions.valueJsonString) {
                        col.searchoptions.value = {
                            "": ""
                        };
                        var options = $.parseJSON(col.searchoptions.valueJsonString);
                        for (var key in options) {
                            col.searchoptions.value[key] = options[key];
                        }
                    }

                    col.editoptions = $.extend(true, {
                        value: col.searchoptions.value
                    }, col.editoptions);
                }

                //计算表格元素总宽度，用于后面控制是默认收缩显示还是扩展显示
                if (!col.hidden) {
                    if (col.width) {
                        userTotalWidth += col.width;
                    } else {
                        userTotalWidth += 300;
                    }
                }

                if (col.hasOwnProperty('searchoptions')) {
                    var searchOptions = col.searchoptions;
                    //计算如果定义searchoptions.defaultValue值，则把对应的值和列标识组装追加到查询参数对象，用于后续的初始化参数过滤查询
                    if (searchOptions.hasOwnProperty('defaultValue') && searchOptions.defaultValue != '') {
                        var field = col.index;
                        if (field == undefined) {
                            field = col.name;
                        }
                        defaultSearchOptions[defaultSearchOptions.length++] = {
                            "field": field,
                            //默认取第一个查询规则
                            "op": col.searchoptions.sopt[0],
                            "data": searchOptions.defaultValue
                        };
                    }
                }

                if (col.searchoptions.sopt == undefined) {
                    //默认支持的查询类型
                    col.searchoptions.sopt = ['cn', 'bw', 'bn', 'eq', 'ne', 'nc', 'ew', 'en'];
                }

                //根据formatter转换定义index属性
                if (col.index == undefined) {
                    if (col.formatter == 'date' || col.formatter == 'timestamp') {
                        col.index = col.name + "@Date";
                    } else if (col.formatter == 'checkbox') {
                        col.index = col.name + "@Boolean";
                    } else if (col.formatter == 'integer' || col.formatter == 'currency' || col.formatter == 'number' || col.formatter == 'percentage') {
                        col.index = col.name + "@Number";
                    }
                }

                gridOptions.colModel[i] = col;
            });

            //如果是从表格模式
            if (gridType == 'items') {
                //追加记录当前行项是否为脏数据的列属性定义
                gridOptions.colModel.push({
                    name: 'extraAttributes.dirtyRow',
                    hidden: true,
                    hidedlg: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("extraAttributes.dirtyRow");
                }

                //追加用于记录当前行项对应索引下标的属性列
                gridOptions.colModel.push({
                    name: '_arrayIndex',
                    hidedlg: true,
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("_arrayIndex");
                }

                //追加用于记录当前行项数据操作标识，主要是remove标识，新增和编辑可以通过有无id来判断无需考虑此属性
                gridOptions.colModel.push({
                    name: 'extraAttributes.operation',
                    hidedlg: true,
                    hidden: true
                });
                if (gridOptions.colNames) {
                    gridOptions.colNames.push("extraAttributes.operation");
                }
            }

            //取全局的表格收缩或扩展显示模式参数
            var globalGridShrinkOption = $('.theme-panel .grid-shrink-option').val();
            if (globalGridShrinkOption == "true") {
                gridOptions.shrinkToFit = true;
            } else {
                //如果计算总计的表格宽度大于父元素宽度，则设置以扩展模式显示，出现横向滚动条
                if (Number(userTotalWidth) > Number($grid.parent().width())) {
                    $.each(gridOptions.colModel, function (i, col) {
                        if (!col.hidden) {
                            if (col.width == undefined) {
                                col.width = 300;
                            }
                        }
                    });
                    gridOptions.shrinkToFit = false;
                }
            }

            //控制表格是否自动扩展以铺满页面其他元素高度累加后剩余空白高度
            var needAutoStretch = false;
            //假如不是子表格，也不是从表格模式
            if ($grid.closest(".ui-subgrid").size() == 0 && gridType != 'items') {
                //如果用户未制定高度模式，则设置为自动铺满模式
                if (gridOptions.height == undefined || gridOptions.height == "stretch") {
                    needAutoStretch = true;
                    //直接把高度参数设置为0不显示表格，后续再根据计算空白区域高度更新设置高度并显示
                    gridOptions.height = 0;
                }
            }

            //快速过滤工具条
            if (gridOptions.filterToolbar) {
                if (gridOptions.postData == undefined) {
                    gridOptions.postData = {};
                }
                var postData = gridOptions.postData;

                //组装默认查询参数赋值给表格postData参数以发起参数初始化查询
                var filters = {};

                // check if post data already has filters
                if (postData.hasOwnProperty('filters')) {
                    filters = JSON.parse(postData.filters);
                }

                var rules = [];

                // check if filtes already has rules
                if (filters.hasOwnProperty('rules')) {
                    rules = filters.rules;
                }

                // loop trough each default search option and add the search option if the filter rule doesnt exists
                $.each(defaultSearchOptions, function (defaultSearchOptionindex, defaultSearchOption) {
                    var ruleExists = false;
                    $.each(rules, function (index, rule) {
                        if (defaultSearchOption.field == rule.field) {
                            ruleExists = true;
                            return;
                        }
                    });

                    if (ruleExists == false) {
                        rules.push(defaultSearchOption);
                    }
                });

                if (rules.length > 0) {
                    filters.groupOp = 'AND';
                    filters.rules = rules;

                    // set search = true
                    postData._search = true;
                    postData.filters = JSON.stringify(filters);
                }
            }

            if (gridOptions.jqPivot) {
                //如果有透视图表参数，则以透视图表方式构造jqGrid组件
                var jqPivot = gridOptions.jqPivot;
                delete gridOptions.jqPivot;
                var url = gridOptions.url;
                gridOptions = {
                    multiselect: false,
                    pager: gridOptions.pager,
                    shrinkToFit: false
                };
                $grid.jqGrid('jqPivot', url, jqPivot, gridOptions, {
                    reader: "content"
                });
                return;
            } else {
                //常规方式构造jqGrid组件
                $grid.jqGrid(gridOptions);
            }

            if (gridOptions.filterToolbar) {
                //如果有快速过滤工具条，则初始化工具条
                $grid.jqGrid('filterToolbar', gridOptions.filterToolbar);

                //取到行号显示表格头
                var $rn = $("#jqgh_" + gridID + "_rn");
                var show = '<a href="javascript:;" title="显示快速查询"><span class="ui-icon ui-icon-carat-1-s"></span></a>';
                var hide = '<a href="javascript:;" title="隐藏快速查询"><span class="ui-icon ui-icon-carat-1-n"></span></a>';
                if ($grid.is(".ui-jqgrid-subgrid") || gridOptions.filterToolbar == 'hidden') {
                    //如果是子表格或设置隐藏，则行号表格头提供一个显示按钮图标，同时默认收起工具条
                    $rn.html(show);
                    $grid[0].toggleToolbar();
                } else {
                    //行号表格头提供一个隐藏按钮图标
                    $rn.html(hide);
                }

                //图标点击事件处理
                $rn.on("click", ".ui-icon-carat-1-s", function () {
                    $rn.html(hide);
                    $grid[0].toggleToolbar();
                });
                $rn.on("click", ".ui-icon-carat-1-n", function () {
                    $rn.html(show);
                    $grid[0].toggleToolbar();
                });
            }

            //jqGrid setGroupHeaders处理
            if (gridOptions.setGroupHeaders) {
                $grid.jqGrid('setGroupHeaders', $.extend(true, {
                    useColSpanStyle: true
                }, gridOptions.setGroupHeaders));
            }

            //事件处理
            $grid.bindKeys({
                upKey: false,
                downKey: false,
                //回车事件处理
                onEnter: function (id) {
                    if (id == undefined) {
                        return;
                    }
                    $grid.find("tr.jqgrow").attr("tabindex", -1);
                    var $tr = $grid.find("tr.jqgrow[id='" + id + "']");
                    $tr.attr("tabindex", 0);
                    if (gridOptions.editurl) {
                        //如果正在编辑模式，则保存;如果不是则开启编辑模式
                        if ($tr.attr("editable") == "1") {
                            $("#" + gridID + "_ilsave").find("span.fa").click();
                        } else {
                            $("#" + gridID + "_iledit").find("span.fa").click();
                        }
                        return false;
                    }
                }
            });

            //如果有pager区域
            if (gridOptions.pager || gridOptions.toppager) {

                var $pager = $(gridOptions.pager);

                //基于视窗大小设定是否显示refresh刷新图标按钮
                var refresh = Util.notSmallViewport();
                if (refresh) {
                    //如果是从表格模式，则不显示按钮
                    refresh = (gridType == 'items' ? false : true)
                }

                //基于视窗大小设定是否显示search高级查询图标按钮
                var search = Util.notSmallViewport();
                if (search) {
                    //如果是从表格模式，则不显示按钮
                    search = (gridType == 'items' ? false : true)
                }

                //添加分隔条
                $grid.jqGrid('navSeparatorAdd', undefined, {
                    position: 'last'
                });
                //导航区域处理     
                $grid.jqGrid('navGrid', gridOptions.pager, {
                    //grid自带的表单编辑模式，置为false不支持
                    edit: false,
                    add: false,
                    del: false,
                    refresh: refresh,
                    search: refresh,
                    cloneToTop: true
                });

                //Drag&Drop功能支持
                //主要用在父子结构类型数据托放调整父子关系
                if (gridOptions.gridDnD) {
                    var gridDnD = $.extend(true, {
                        dropbyname: true,
                        beforedrop: function (event, ui, data) {
                            data.id = $(ui.draggable).attr("id");
                            return data;
                        },
                        autoid: function (rowdata) {
                            return rowdata.id;
                        },
                        "drop_opts": {
                            "activeClass": "ui-state-active",
                            "hoverClass": "ui-state-hover",
                            "greedy": true
                        },
                        ondrop: function (event, ui, rowdata) {
                            var $targetGrid = $("#" + this.id);
                            var $pp = $targetGrid.closest(".ui-subgrid");
                            var ppid = '';
                            if ($pp.size() > 0) {
                                ppid = $pp.prev(".jqgrow").attr("id");
                            }
                            var rowid = $(ui.draggable).attr("id");
                            var postdata = {};
                            var parent = $targetGrid.jqGrid('getGridParam', 'parent');
                            var editurl = $targetGrid.jqGrid('getGridParam', 'editurl');
                            if (editurl) {
                                postdata[parent] = ppid;
                                postdata['id'] = rowid;
                                $targetGrid.ajaxPostURL({
                                    url: editurl,
                                    success: function () {
                                        return true;
                                    },
                                    confirmMsg: false,
                                    data: postdata
                                });
                            }
                        }
                    }, gridOptions.gridDnD);

                    var dndButtonHVOptions = {
                        caption: false,
                        buttonicon: 'fa-arrows',
                        position: "first",
                        title: "开启拖放移动模式",
                        onClickButton: function () {
                            var $allGrid = null;
                            if ($grid.closest(".ui-subgrid").size() > 0) {
                                $topGrid = $grid.parent().closest(".ui-jqgrid-btable:not(.ui-jqgrid-subgrid)");
                                $allGrid = $topGrid.parent().find(".ui-jqgrid-btable");
                            } else {
                                $allGrid = $grid.parent().find(".ui-jqgrid-btable");
                            }
                            var allGridIds = [];
                            $allGrid.each(function (i, item) {
                                allGridIds.push("#" + $(this).attr("id"));
                            })

                            //颠倒顺序使其子表格向上逐步正确的draggable初始化
                            var reverseGridIds = allGridIds.reverse()
                            $.each(reverseGridIds, function (i, id) {
                                var connectWithIds = $.map(allGridIds, function (n) {
                                    return n != id ? n : null;
                                })
                                var $cur = $(id);
                                if (connectWithIds.length > 0) {
                                    var opts = $.extend({
                                        connectWith: connectWithIds.join(",")
                                    }, gridDnD);
                                    $cur.jqGrid('gridDnD', opts);
                                }
                                if (!$cur.hasClass("ui-jqgrid-dndtable")) {
                                    $cur.addClass("ui-jqgrid-dndtable");
                                }
                            })
                        }
                    };

                    $grid.jqGrid('navButtonAdd', dndButtonHVOptions);
                }

                //inline edit编辑功能支持
                if (gridOptions.pager && (gridOptions.inlineNav.add || gridOptions.inlineNav.edit || gridOptions.inlineNav.del) && gridOptions.inlineNav != false) {
                    $grid.jqGrid('navSeparatorAdd', undefined, {
                        position: 'last'
                    });

                    $grid.jqGrid('inlineNav', gridOptions.inlineNav);
                }

                //处理显示业务操作功能列表
                {
                    // 定制操作按钮，参考： http://www.trirand.com/jqgridwiki/doku.php?id=wiki:custom_buttons
                    var navButtons = gridOptions.navButtons;
                    if (navButtons == undefined || navButtons == false) {
                        navButtons = [];
                    } else if (!Array.isArray(navButtons)) {
                        navButtons = [navButtons];
                    }

                    //表格行项inline编辑操作按钮
                    if (gridOptions.editurl) {
                        navButtons.push({
                            caption: "表格新增数据",
                            buttonicon: "fa-plus",
                            onClickButton: function (rowid) {
                                $("#" + gridID + "_iladd").find("span.fa").click();
                            },
                            title: "直接在表格组件内进行行项新增数据",
                            operationRows: 0,
                            showOnToolbar: false,
                            showOnDropdown: true
                        })

                        navButtons.push({
                            caption: "表格编辑数据",
                            buttonicon: "fa-pencil",
                            onClickButton: function (rowid) {
                                $("#" + gridID + "_iledit").find("span.fa").click();
                            },
                            title: "直接在表格组件内进行行项编辑数据",
                            operationRows: 1,
                            showOnToolbar: false,
                            showOnDropdown: true
                        })
                    }

                    //列显示和排序处理
                    if (gridOptions.columnChooser) {
                        //直接表头右键列表选取显示行项
                        // https://stackoverflow.com/questions/29365758/how-to-change-column-name-in-column-chooser-pop-up-in-jqgrid/29374787#29374787
                        $grid.jqGrid("showHideColumnMenu");

                        navButtons.push({
                            //添加一个添加数据操作按钮图标
                            caption: "设定显示列和顺序",
                            buttonicon: "fa-th-list",
                            onClickButton: function () {
                                var gwdth = $grid.jqGrid("getGridParam", "width");
                                $grid.jqGrid('columnChooser', {
                                    width: 470,
                                    done: function (perm) {
                                        if (perm) {
                                            // "OK" button are clicked
                                            this.jqGrid("remapColumns", perm, true);
                                            // the grid width is probably
                                            // changed co we can get new width
                                            // and adjust the width of other
                                            // elements on the page
                                            $grid.jqGrid("setGridWidth", gwdth, false);
                                        } else {
                                            // we can do some action in case of
                                            // "Cancel" button clicked
                                        }
                                    }
                                });
                            },
                            operationRows: 9,
                            showOnToolbar: false
                        })
                    }

                    //基于本地组装数据传递给服务端生成Excel下载数据的功能处理
                    //如果是从表格模式，则不显示按钮
                    if (gridOptions.exportExcelLocal && gridType != 'items') {
                        navButtons.push({
                            caption: "导出当前显示数据",
                            buttonicon: "fa-file-excel-o",
                            onClickButton: function () {
                                $grid.jqGrid('exportExcelLocal', gridOptions.exportExcelLocal);
                            },
                            operationRows: 9,
                            showOnToolbar: false
                        })
                    }

                    //收缩显示模式图标按钮，目前有局限只能收缩但是不能恢复
                    navButtons.push({
                        caption: "收缩显示模式",
                        buttonicon: "fa-list-alt",
                        onClickButton: function () {
                            var gridwidth = $grid.jqGrid("getGridParam", "width");
                            $grid.jqGrid('destroyFrozenColumns');
                            $grid.jqGrid("setGridWidth", gridwidth, true);
                        },
                        operationRows: 9,
                        showOnToolbar: false
                    })

                    //如果有viewurl参数，则提供一个查看详情的操作按钮
                    if (gridOptions.viewurl) {
                        navButtons.push({
                            caption: "查看详情",
                            buttonicon: "fa-edit",
                            onClickButton: function (rowid) {
                                //计算到编辑界面tab标题区域显示的数据标识信息
                                var editcol = "TBD";
                                var rowdata = $grid.jqGrid("getRowData", rowid);
                                if (gridOptions.editcol) {
                                    //如果指定了特定显示列，则取对应列数据
                                    editcol = rowdata[gridOptions.editcol];
                                    //如果是包含链接标识，则只取text文本部分作为标题显示
                                    if (editcol.indexOf("<") > -1 && editcol.indexOf(">") > -1) {
                                        editcol = $(editcol).text();
                                    }
                                } else {
                                    //如果未指定，则以id主键数据
                                    editcol = rowdata['id'];
                                    if (editcol.indexOf("<span") > -1) {
                                        editcol = $(editcol).text();
                                    }
                                }

                                //显示信息长度处理，避免由于内容信息太长导致显示tab的标题信息过长
                                //var len = editcol.length;
                                //if (len > 8) {
                                //    editcol = "..." + editcol.substring(len - 5, len);
                                //}
                                //全局的id参数处理，把id值追加到url对应的id参数上
                                var url = Util.AddOrReplaceUrlParameter(gridOptions.viewurl, "id", rowid);

                                $view.popupDialog({
                                    title: '查看: ' + editcol,
                                    url: url
                                });
                            },
                            operationRows: 1
                        })
                    }

                    //简单数据直接用inline edit在表格完成编辑，对于复杂对象编辑需要转向一个详细编辑界面
                    if (gridOptions.fullediturl) {
                        //如果允许新增数据，则添加一个添加数据操作按钮图标
                        if (gridOptions.addable == undefined || gridOptions.addable != false) {
                            navButtons.push({
                                //添加一个添加数据操作按钮图标
                                caption: "表单新增数据",
                                buttonicon: "fa-plus-square-o",
                                onClickButton: function () {
                                    $.AdminLTE.appendTabPane(gridOptions.fullediturl, "新增" + gridEntityName);
                                },
                                operationRows: 0,
                                showOnToolbar: true
                            })

                            /**  TODO 复杂度挺高，暂时屏蔽
                             navButtons.push({
                                //添加按钮的同时，提供一个clone克隆复制功能方便用户可以快速基于已有克隆创建新数据
                                caption: "表单克隆复制",
                                buttonicon: "fa-copy",
                                onClickButton: function (rowid) {
                                    //如果有cloneurl参数则取之，否则以fullediturl并替换id参数及追加clone标识参数作为克隆请求url
                                    var cloneurl = gridOptions.cloneurl ? gridOptions.cloneurl : gridOptions.fullediturl;
                                    var url = Util.AddOrReplaceUrlParameter(cloneurl, "id", rowid);
                                    url = url + ("&clone=true");
                                    $.AdminLTE.appendTabPane(url, "克隆" + gridEntityName);
                                },
                                title: "基于已有数据克隆复制创建新数据",
                                operationRows: 1,
                                showOnToolbar: false,
                                showOnDropdown: true
                            });
                             */
                        }

                        //详情编辑功能
                        navButtons.push({
                            caption: "表单编辑数据",
                            buttonicon: "fa-edit",
                            onClickButton: function (rowid) {
                                var rownum = $grid.find("tr#" + rowid + " > td.jqgrid-rownum").html();
                                var url = Util.AddOrReplaceUrlParameter(gridOptions.fullediturl, "id", rowid);
                                $.AdminLTE.appendTabPane(url, "编辑" + gridEntityName + " " + rownum);
                            },
                            title: "可双击行项进行编辑数据",
                            badge: "双击",
                            operationRows: 1,
                            showOnToolbar: true,
                            showOnDropdown: true
                        })
                    }

                    if (gridOptions.delurl) {
                        navButtons.push({
                            caption: "删除选择数据",
                            buttonicon: "fa-trash-o",
                            onClickButton: function (ids) {
                                //主子表的子表本地数据处理
                                if (gridOptions.delurl == undefined || gridOptions.delurl == 'clientArray') {
                                    $.each(ids, function (i, id) {
                                        if (Util.startWith(id, '-')) {
                                            //如果以负数开头，说明是当前页面操作新插入的行项，则直接删除行项dom
                                            $grid.jqGrid("delRowData", id);
                                        } else {
                                            //如果是已有数据，则需要做删除标记以便提交到服务端做数据库删除
                                            var $tr = $grid.find("#" + id);
                                            var rowdata = $grid.jqGrid("getRowData", id);
                                            //alert(Util.objectToString(rowdata));
                                            for (var key in rowdata) {
                                                if (key == 'id' || Util.endWith(key, '.id') || key == '_arrayIndex') {

                                                } else if (key == 'extraAttributes.dirtyRow') {
                                                    //脏数据标记，以便提交post组装数据
                                                    rowdata[key] = true;
                                                } else if (key == 'extraAttributes.operation') {
                                                    //remove标记设定，在ExtParametersInterceptor.java全局处理
                                                    rowdata[key] = 'remove';
                                                } else {
                                                    //置空其他属性，减少不必要的数据post
                                                    rowdata[key] = '';
                                                }
                                            }
                                            //alert(Util.objectToString(rowdata));
                                            //设置删除数据，然后把行项隐藏，后续作为脏数据提交
                                            $grid.jqGrid("setRowData", id, rowdata);
                                            $tr.hide();
                                        }
                                    })
                                } else {
                                    //主表的行项数据ajax批量选取删除
                                    if (ids) {
                                        var idKey = "id";
                                        //如果是多选则变更参数名为ids
                                        if (gridOptions.multiselect) {
                                            idKey = "ids";
                                        }
                                        var url = Util.AddOrReplaceUrlParameter(gridOptions.delurl, idKey, ids.join(","));
                                        $grid.ajaxPostURL({
                                            url: url,
                                            success: function (response) {
                                                $.each(ids, function (i, item) {
                                                    var item = $.trim(item);
                                                    if (response.data && response.data[item]) {
                                                        //服务器会回传删除失败的行项数据，找到对应行项添加删除提示
                                                        var $tr = $grid.find("tr.jqgrow[id='" + item + "']");
                                                        var msg = response.data[item];
                                                        $tr.pulsate({
                                                            color: "#bf1c56",
                                                            repeat: 3
                                                        });
                                                    } else {
                                                        //如果服务器处理成功则移除grid对应行项
                                                        $grid.jqGrid("delRowData", item);
                                                    }
                                                });
                                            },
                                            confirmMsg: "确认批量删除所选记录吗？"
                                        })
                                    }
                                }
                            },
                            title: "勾选并批量删除数据",
                            operationRows: 2,
                            showOnToolbar: true,
                            showOnDropdown: true
                        })
                    }

                    var showOnToolbarSeparator = false;
                    $.each(navButtons, function (i, item) {
                        item = $.extend(
                            //叠加默认属性值
                            {
                                buttonicon: "fa-edit",
                                onClickButton: function ($g) {

                                },
                                position: "last",
                                cursor: "pointer",
                                operationRows: 0,
                                showOnToolbar: false,
                                showOnDropdown: true
                            }, item);

                        //取按钮名称作为title提示信息
                        if (!item.title) {
                            item.title = item.caption;
                        }

                        var userOnClickButton = item.onClickButton;
                        item.onClickButton = function (options) {
                            var $grid = $(this);
                            if (item.operationRows == 1) {
                                //单选模式，则校验必须刚好选取一项，并返回选中rowid给业务逻辑处理
                                var rowid = $grid.getOnlyOneSelectedItem(true);
                                if (rowid) {
                                    userOnClickButton.call($grid, rowid, options);
                                }
                            } else if (item.operationRows > 1 && item.operationRows < 9) {
                                //多选模式，则校验必须至少选取一项，并返回选中rowid集合给业务逻辑处理
                                var rowids = $grid.getAtLeastOneSelectedItem(true);
                                if (rowids) {
                                    userOnClickButton.call($grid, rowids, options);
                                }
                            } else {
                                userOnClickButton.call($grid, options);
                            }
                        }

                        //属性叠加后的元素更新回数组对象
                        navButtons[i] = item;

                        if (item.showOnToolbar) {
                            if (showOnToolbarSeparator == false) {
                                showOnToolbarSeparator = true;
                                $grid.jqGrid('navSeparatorAdd', undefined, {
                                    position: 'last'
                                });
                            }

                            //把caption设置为false避免在按钮区域显示文本
                            $grid.jqGrid('navButtonAdd', $.extend({}, item, {caption: false}));
                        }
                    });

                    //下拉功能菜单处理
                    {
                        //如果下拉按钮选项，则添加分隔条
                        $grid.jqGrid('navSeparatorAdd', undefined, {
                            position: 'first'
                        });

                        var html = [];
                        html.push('<div class="btn-group btn-group-more-option" style="margin-left: 3px;margin-right: 3px;">');
                        html.push('<button type="button" class="btn btn-xs default dropdown-toggle" data-toggle="dropdown">');
                        html.push('更多 <i class="fa fa-angle-down"></i>');
                        html.push('</button>');
                        html.push('<ul class="dropdown dropdown-menu" role="menu">');
                        html.push('</ul>');
                        html.push('</div>');

                        //下拉操作菜单列表图标按钮
                        $grid.jqGrid('navButtonAdd', {
                            caption: "",
                            buttonicon: 'fa-more-option',
                            position: "first",
                            title: "更多操作"
                        });


                        //通过jqGrid navButtonAdd添加的按钮控制会导致无法弹出dropdown选项，因此用自定义的下拉按钮替换Grid组件添加的按钮
                        var $gridRoot = $grid.closest("div.ui-jqgrid");
                        var $moreOption = $gridRoot.find(".fa-more-option").parent().parent();
                        $moreOption.replaceWith(html.join(""));

                        //下方的分页区域按钮添加dropup控制，向上方弹出选项
                        var $pagerMoreOption = $gridRoot.find(".ui-jqgrid-pager .btn-group-more-option");
                        $pagerMoreOption.addClass("dropup");

                        //利用js中的sort方法对navButtons按照operationRows属性排序：0无需选取，1单条选取，2多条选取，9工具操作
                        navButtons.sort(function (a, b) {
                            return a.operationRows - b.operationRows
                        });
                        var $ul = $gridRoot.find(".btn-group-more-option").find("ul");
                        $.each(navButtons, function (i, item) {
                            if (item.showOnDropdown) {
                                var html = '<i class="fa ' + item.buttonicon + '"></i> ' + item.caption;
                                if (item.badge) {
                                    html = html + '<span class="label label-default pull-right"> ' + item.badge + ' </span>';
                                }
                                var $li = $('<li data-type="' + item.operationRows + '"><a tabindex="-1" href="javascript:;">' + html + '</a> </li>');
                                $li.children("a").bind("click", function () {
                                    item.onClickButton.call($grid);
                                });
                                $li.appendTo($ul);
                            }
                        });
                        var noneCount = $ul.find('li[data-type="0"]').size(), singleCount = $ul.find('li[data-type="1"]').size();
                        var multipleCount = $ul.find('li[data-type="2"]').size(), toolCount = $ul.find('li[data-type="9"]').size();
                        ;
                        if (noneCount > 0 && (singleCount > 0 || multipleCount > 0)) {
                            $ul.find('li[data-type="0"]:last').after('<li class="divider"> </li>');
                        }
                        if (singleCount > 0 && (multipleCount > 0 || toolCount > 0)) {
                            $ul.find('li[data-type="1"]:last').after('<li class="divider"> </li>');
                        }
                        if (multipleCount > 0 && toolCount > 0) {
                            $ul.find('li[data-type="2"]:last').after('<li class="divider"> </li>');
                        }
                    }
                }


                //基于工具条区域的下拉操作列表克隆复制方式处理右键菜单
                //取全局的右键菜单控制参数，控制是显示业务右键菜单项还是浏览器自己的右键菜单
                var globalContextMenuOption = $('.theme-panel .context-menu-option').val();
                //表格本身的contextMenu参数也可控制是否生成右键菜单项
                if (globalContextMenuOption == 'enable' && gridOptions.contextMenu && $menuUL.find("li").length > 0) {
                    //生成对应右键菜单容器div元素追加到body，然后在loadComplete事件中基于此元素添加相应的contextmenu插件事件处理
                    var $contextMenu = $('<div id="' + contextMenuId + '" class="context-menu"></div>');
                    $menuUL.clone().appendTo($contextMenu);
                    $("body").append($contextMenu);
                    //取消表格组件默认的右键菜单事件绑定
                    $grid.unbind("contextmenu");
                }
            }

            //处理提示信息显示
            var cm = gridOptions.colModel;
            for (var i = 0; i < cm.length; i++) {
                var c = cm[i];
                //如果元素本身定义tooltips参数，则处理其显示效果
                if (c.tooltips) {
                    var $tip = $('<span class="glyphicon glyphicon-exclamation-sign tooltipster"  title="' + c.tooltips + '"></span>');
                    var name = c.index ? c.index : c.name;
                    var $sortable = $(".ui-jqgrid-sortable[id*='" + name + "']", $labels);
                    if ($sortable.size() > 0) {
                        $sortable.prepend($tip);
                        $tip.tooltipster({
                            contentAsHTML: true,
                            offsetY: 5,
                            theme: 'tooltipster-punk'
                        })
                    }
                }
            }

            //editrulesurl编辑规则校验url规则请求参数，如果没有定义则基于editurl转换组装
            //如果是无需进行表单校验的inline edit编辑功能，可以设置为false避免不必要的ajax请求
            //否则始终会自动发起指定或组装的ajax请求
            var editrulesurl = gridOptions.editrulesurl;
            if (editrulesurl) {
                var $labels = $("#gbox_" + grid.attr("id") + "  .ui-jqgrid-labels");
                //动态调用通用的AJAX请求获取对象编辑表单校验规则
                $labels.ajaxJsonUrl(editrulesurl, function (json) {
                    //处理Class元数据
                    var classMetaData = json._classMeta;
                    gridEntityName = classMetaData.entityName;
                    delete json._classMeta;

                    var cm = $grid.jqGrid('getGridParam', 'colModel');
                    for (var key in json) {
                        for (var i = 0; i < cm.length; i++) {
                            var c = cm[i];

                            if ((c.index && c.index == key) || (c.name && c.name == key)) {
                                //绑定验证规则
                                c.editrules = $.extend(json[key] || {}, c.editrules || {});
                                if (c.editrules.required == undefined) {
                                    c.editrules.required = false;
                                }
                                //alert(JSON.stringify( gridOptions.colModel));
                                //移除Grid不支持的校验项
                                delete c.editrules.timestamp;
                                delete c.editrules.shortTimestamp;

                                //动态处理提示信息显示
                                if (c.editrules.tooltips && c.tooltips == undefined) {
                                    var $tip = $('<span class="glyphicon glyphicon-exclamation-sign tooltipster"  title="' + c.editrules.tooltips + '"></span>');
                                    var $sortable = $(".ui-jqgrid-sortable[id*='" + key + "']", $labels);
                                    if ($sortable.size() > 0) {
                                        $sortable.prepend($tip);
                                        $tip.tooltipster({
                                            contentAsHTML: true,
                                            offsetY: 5,
                                            theme: 'tooltipster-punk'
                                        })
                                    }
                                    delete c.editrules.tooltips;
                                }
                                break;
                            }
                        }
                    }
                });
            }

            // 根据所在父级元素高度计算表格组件合理的高度，使组件刚好撑满父元素
            if (needAutoStretch) {
                var gbox = $("#gbox_" + grid.attr("id"));
                var decreaseHeight = 0;
                //定义需要计算高度的其他元素列表
                var els = "div.ui-jqgrid-titlebar,div.ui-jqgrid-hdiv,div.ui-jqgrid-pager,div.ui-jqgrid-toppager,div.ui-jqgrid-sdiv";
                gbox.find(els).filter(":visible").each(function () {
                    decreaseHeight += $(this).outerHeight();
                });
                //下方预留一点空隙
                decreaseHeight = decreaseHeight + 50;
                //表格高度=窗口高度-表格top位置偏移-其余高度
                var newheight = $(window).height() - $grid.closest(".ui-jqgrid").offset().top - decreaseHeight;
                //如果高度太小设置一个合理最小高度，避免表格高度太小导致显示不全
                if (newheight < 300) {
                    newheight = 300;
                }
                //更新高度
                $grid.setGridHeight(newheight, true);
            }

            //刷新表格宽度使其自适应页面宽度
            Grid.refreshWidth();

            //如果有冻结列定义，则调用表格列冻结处理
            if (userFrozen) {
                $grid.jqGrid('setFrozenColumns');
            }

            //表格resize支持，主要实现可以拖动放大显示区域
            $grid.jqGrid('gridResize', {
                minWidth: 500,
                minHeight: 100
            });

            //对表格下边框添加双击事件：自动更新高度到适合所有行项数据显示
            $grid.closest(".ui-jqgrid").find(".ui-resizable-s").dblclick(function () {
                var height = $grid.jqGrid("getGridParam", "height");
                $grid.jqGrid("setGridHeight", $grid.height() + 17);
            }).attr("title", "鼠标双击可自动扩展显示区域");

        },

        // 根据表格所属父元素宽度，刷新表格宽度。主要用于窗口resize事件后回调以实现表格宽度自适应
        refreshWidth: function () {
            $("table.ui-jqgrid-btable:visible").each(function () {
                var $grid = $(this);
                var gridwidth = $grid.jqGrid("getGridParam", "width");
                var newwidth = $grid.closest("div.ui-jqgrid").parent("div").width();
                if (gridwidth != newwidth) {
                    //$grid.jqGrid("remapColumns", true, true);
                    //var shrinkToFit = newwidth > gridwidth;
                    $grid.jqGrid("setGridWidth", newwidth);
                    //如果有GroupHeader则重建以便根据新宽度重新设置表头否则会出现显示错位
                    var groupHeaders = $(this).jqGrid("getGridParam", "groupHeader");
                    if (groupHeaders) {
                        $grid.jqGrid('destroyGroupHeader');
                        $grid.jqGrid('setGroupHeaders', groupHeaders);
                    }
                }
            });
        },

        /**
         * 树形父子结构数据递归初始化子表格，在主表格的参数对象基础上添加父子关联参数作为子表格初始化参数对象，一般在subGrid=true时，在subGridRowExpanded事件方法中调用
         *
         * @param subgrid_id 直接传入subGridRowExpanded事件方法中对应参数
         * @param row_id 直接传入subGridRowExpanded事件方法中对应参数
         * @param parent 将当期父行项的id作为参数传入parent指定的参数作为查询子集合的查询条件名称，例如parent.id，在当前主Grid的url参数基础上添加search['EQ_" + parent + "']=(行项id值)作为子表格查询url
         * @param clearPostData 是否初始化清空参数对象中的postData参数，避免父对象的参数干扰
         *
         * @example  Grid.initRecursiveSubGrid(subgrid_id, row_id, "parent.id");
         */
        initRecursiveSubGrid: function (subgrid_id, row_id, parent, clearPostData) {
            //动态构造子表格table元素
            var $subTable = $("<table data-grid='table' class='ui-jqgrid-subgrid'/>").appendTo($("#" + subgrid_id));
            //取父表格的参数对象作为子表格参数基础对象
            var parentGridOptions = $subTable.closest("table.ui-jqgrid-btable").data("gridOptions");
            //取父表格的url参数并追加parent参数作为子表格的url参数
            parentGridOptions.url = Util.AddOrReplaceUrlParameter(parentGridOptions.url, "search['EQ_" + parent + "']", row_id);
            //取父表格的fullediturl参数并追加parent参数作为子表格的fullediturl参数
            if (parentGridOptions.fullediturl) {
                parentGridOptions.fullediturl = Util.AddOrReplaceUrlParameter(parentGridOptions.fullediturl, parent, row_id);
            }
            //初始化子表格的inline编辑参数对象
            parentGridOptions.inlineNav = $.extend(true, {
                addParams: {
                    addRowParams: {
                        extraparam: {}
                    }
                }
            }, parentGridOptions.inlineNav);
            //追加当前父对象主键参数名及对应值，用于子表格数据提交自动附加所属父对象信息
            parentGridOptions.inlineNav.addParams.addRowParams.extraparam[parent] = row_id;
            //设置当前子表格的参数parent为其父对象
            parentGridOptions.parent = parent;
            //是否初始化清空参数对象中的postData参数，避免父对象的参数干扰
            if (clearPostData) {
                parentGridOptions.postData = {};
            }
            $subTable.data("gridOptions", parentGridOptions);
            //构造子表格
            Grid.initGrid($subTable);

            //找到对应的顶级父表格对象
            var $topGrid = $("#" + subgrid_id).parent().closest(".ui-jqgrid-btable:not(.ui-jqgrid-subgrid)");
            //如果副对象启用了托放支持，并且当前正处于托放模式，则触发当前子表格的托放支持
            if (parentGridOptions.gridDnD && $topGrid.hasClass("ui-jqgrid-dndtable")) {
                $("#" + subgrid_id).find(".ui-icon-arrow-4:first").click();
            }
        },

        /**
         * 初始化普通子表格，一般在subGrid=true时，在subGridRowExpanded事件方法中调用
         *
         * @param subgrid_id 直接传入subGridRowExpanded事件方法中对应参数
         * @param row_id 直接传入subGridRowExpanded事件方法中对应参数
         * @param options 传入用于子表格初始化的jqGrid结构参数对象
         *
         * @example
         * <pre>
         subGrid : true,
         subGridRowExpanded : function(subgrid_id, row_id) {
                Grid.initSubGrid(subgrid_id, row_id, {
                    url : WEB_ROOT + "/admin/sys/notify-message/read-list?search['EQ_notifyMessage.id']=" + row_id,
                    colNames : [ '阅读用户', '首次阅读时间', '最后阅读时间', '总计阅读次数' ],
                    colModel : [ {
                        name : 'readUserLabel',
                        width : 150
                    }, {
                        name : 'firstReadTime',
                        formatter : 'timestamp'
                    }, {
                        name : 'lastReadTime',
                        formatter : 'timestamp'
                    }, {
                        name : 'readTotalCount',
                        align : 'center'
                    } ]
                });
            }
         * </pre>
         */
        initSubGrid: function (subgrid_id, row_id, options) {
            var $subTable = $("<table data-grid='table' class='ui-jqgrid-subgrid'/>").appendTo($("#" + subgrid_id));
            $subTable.data("gridOptions", options);
            Grid.initGrid($subTable);
        }
    };


}();

$(document).on('after.ajax.page.show', function (event) {
    var expr = 'table[data-grid="table"],table[data-grid="items"]';
    $(expr, event.container).each(function () {
        var $target = $(this)
        //稍微延迟构造，使其能正确获取到父容器的宽度值
        setTimeout(function () {
            Grid.initGrid($target);
        }, 100)
    })
})

new ResizeSensor($('.content-wrapper'), function () {
    //刷新表格宽度使其自适应页面宽度
    Grid.refreshWidth();
});
