/*!
 * Select2-to-Tree 1.1.1
 * https://github.com/xautlx/select2-to-tree fork from https://github.com/clivezhg/select2-to-tree
 * Add AJAX data source support
 */
(function ($) {
    $.fn.select2ToTree = function (options) {
        var $this = $(this);
        var opts = $.extend({}, options);
        var treeData = opts.treeData;
        var dataUrl = $this.data("tree-url") || treeData.dataUrl;
        var dataParent = $this.data("tree-parent") || treeData.dataParent;

        opts._templateResult = opts.templateResult;
        opts.templateResult = function (data, container) {
            var label = data.text;
            if (typeof opts._templateResult === "function") {
                label = opts._templateResult(data, container);
            }
            var $iteme = $("<span class='item-label'></span>").append(label);
            if (data.element) {
                $(container).addClass("non-leaf");
                var $ele = $(data.element);
                var children = $ele.data("children");
                var level = $ele.data("level");
                if (children == -1) {
                    var $col = $('<span class="expand-collapse"></span>');
                    $col.on('mouseup', function (evt) {
                        var url = Util.AddOrReplaceUrlParameter(dataUrl, dataParent, $ele.val());
                        alert('url=' + url);
                        $this.ajaxJsonUrl(url, function (data) {
                            var dataArr = data.content;
                            $ele.attr("data-children", dataArr.length);
                            for (var i = 0; i < dataArr.length; i++) {
                                var data = dataArr[i] || {};
                                var $opt = $("<option></option>");
                                $opt.text(data[treeData.labelFld || "text"]);
                                var val = data[treeData.valFld || "id"];
                                $opt.val(val);
                                $opt.attr("data-val", val);
                                $opt.attr("data-level", level + 1);
                                $opt.attr("data-children", -1);
                                $this.append($opt);
                            }
                            $this.trigger('change');
                        });
                    })
                    return $.merge($col, $iteme);
                }
            }
            return $iteme;
        };


        $this.ajaxJsonUrl(dataUrl, function (data) {
            var dataArr = data.content;
            for (var i = 0; i < dataArr.length; i++) {
                var data = dataArr[i] || {};
                var $opt = $("<option></option>");
                $opt.text(data[treeData.labelFld || "text"]);
                var val = data[treeData.valFld || "id"];
                $opt.val(val);
                $opt.attr("data-val", val);
                $opt.attr("data-level", 1);
                $opt.attr("data-children", -1);
                $this.append($opt);
            }

            var s2inst = $this.select2(opts);
            var s2data = s2inst.data("select2");
            var $dropdown = s2data.$dropdown;
            $dropdown.addClass("s2-to-tree");
        })
    };

    function buildSelect(treeData, $el, curLevel, pup) {
        function buildOptions(dataUrl, dataArr, curLevel, pup) {
            var $pup = null;
            if (pup) {
                $pup = $el.children("option[value=='" + pup + "']");
            }
            for (var i = 0; i < dataArr.length; i++) {
                var data = dataArr[i] || {};
                var $opt = $("<option></option>");
                $opt.text(data[treeData.labelFld || "text"]);
                $opt.val(data[treeData.valFld || "id"]);
                if ($opt.val() === "") {
                    $opt.prop("disabled", true);
                    $opt.val(getUniqueValue());
                }
                $opt.addClass("l" + curLevel);
                $opt.attr("data-level", curLevel);
                if ($pup) {
                    $opt.attr("data-pup", pup);
                    $pup.after($opt);
                } else {
                    $el.append($opt);
                }

                $opt.addClass("non-leaf");
            }
        }


        if (dataUrl) {

        } else {
            buildOptions(dataUrl, dataArr, curLevel, pup);
        }

        if (treeData.dftVal) $el.val(treeData.dftVal);
    }

    var uniqueIdx = 1;

    function getUniqueValue() {
        return "autoUniqueVal_" + uniqueIdx++;
    }

    function toggleSubOptions(target) {
        $(target.parentNode).toggleClass("opened");
        showHideSub(target.parentNode);
    }

    function showHideSub(ele) {
        var curEle = ele;
        var $el = $(curEle).closest(".select2-container").data("element");
        var $options = $(ele).parent(".select2-results__options");
        var shouldShow = true;
        do {
            var pup = ($(curEle).attr("data-pup") || "").replace(/'/g, "\\'");
            curEle = null;
            if (pup) {
                var pupEle = $options.find(".select2-results__option[data-val='" + pup + "']");
                if (pupEle.length > 0) {
                    if (!pupEle.eq(0).hasClass("opened")) { // hide current node if any parent node is collapsed
                        $(ele).removeClass("showme");
                        shouldShow = false;
                        break;
                    }
                    curEle = pupEle[0];
                }
            }
        } while (curEle);
        if (shouldShow) $(ele).addClass("showme");

        var val = ($(ele).attr("data-val") || "").replace(/'/g, "\\'");
        $options.find(".select2-results__option[data-pup='" + val + "']").each(function () {
            showHideSub(this);
        });
    }
})(jQuery);
