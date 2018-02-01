/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 *
 * Site: https://www.entdiy.com, E-Mail: xautlx@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.dev.demo.web.action;

import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.JsonUtils;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.dev.demo.entity.DemoReimbursementRequest;
import com.entdiy.dev.demo.entity.DemoReimbursementRequestItem;
import com.entdiy.dev.demo.service.DemoReimbursementRequestService;
import com.entdiy.dev.demo.support.DemoConstant;
import com.entdiy.sys.service.DataDictService;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@MetaData("报销申请管理")
@Controller
@RequestMapping(value = "/dev/demo/reimbursement-request")
public class DemoReimbursementRequestController extends BaseController<DemoReimbursementRequest, Long> {

    @Autowired
    private UserService userService;

    @Autowired
    private DemoReimbursementRequestService reimbursementRequestService;

    @Autowired
    private DataDictService dataDictService;

    @Override
    protected BaseService<DemoReimbursementRequest, Long> getEntityService() {
        return reimbursementRequestService;
    }

    @ModelAttribute
    public void prepareModel(HttpServletRequest request, Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(request, model, id);
    }

    /**
     * 如果编辑提交数据涉及到一对一或一对多关联对象更新处理，则需要返回Detached的对象实例，否则会遇到关联对象主键修改异常
     *
     * @param id 实体主键
     * @return Detached的对象实例
     */
    @Override
    protected DemoReimbursementRequest buildDetachedBindingEntity(Long id) {
        return reimbursementRequestService.findDetachedOne(id, "reimbursementRequestItems");
    }

    @MenuData("演示样例:报销申请")
    @RequiresPermissions("演示样例:报销申请")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("useTypeJson",
                JsonUtils.writeValueAsString(dataDictService.findMapDataByRootPrimaryKey(DemoConstant.DataDict_Demo_ReimbursementRequest_UseType)));
        return "dev/demo/reimbursementRequest-index";
    }

    @RequiresPermissions("演示样例:报销申请")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<DemoReimbursementRequest> findByPage(HttpServletRequest request) {
        return super.findByPage(DemoReimbursementRequest.class, request);
    }

    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(HttpServletRequest request) {
        return "dev/demo/reimbursementRequest-inputTabs";
    }

    @RequiresPermissions("演示样例:报销申请")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(Model model) {
        model.addAttribute("subValidationRules", ServletUtils.buildValidateRules(DemoReimbursementRequestItem.class));
        DemoReimbursementRequest entity = fetchEntityFromModel(model);
        if (entity.isNew()) {
            //默认取当前登录用户所属部门，用户可编辑修改
            entity.setDepartment(userService.findCurrentAuthUser().getDepartment());
        }

        //模板记录初始化及属性设置
        DemoReimbursementRequestItem newItemTemplate = new DemoReimbursementRequestItem();
        newItemTemplate.setStartDate(DateUtils.currentDateTime().toLocalDate());
        //将追加模板记录添加到集合用于前端循环显示
        List<DemoReimbursementRequestItem> reimbursementRequestItems = entity.getReimbursementRequestItems();
        if (CollectionUtils.isEmpty(reimbursementRequestItems)) {
            reimbursementRequestItems = Lists.newArrayList();
            entity.setReimbursementRequestItems(reimbursementRequestItems);
        }
        reimbursementRequestItems.add(newItemTemplate);

        model.addAttribute("useTypeMap",
                dataDictService.findMapDataByRootPrimaryKey(DemoConstant.DataDict_Demo_ReimbursementRequest_UseType));

        return "dev/demo/reimbursementRequest-inputBasic";
    }

    @RequiresPermissions("演示样例:报销申请")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") DemoReimbursementRequest entity, Model model) {
        if (entity.isNew()) {
            entity.setUser(userService.findCurrentAuthUser());
        } else {
            Optional.ofNullable(entity.getReimbursementRequestItems()).ifPresent(items -> items.removeIf(item -> {
                if (item.getId() == null) {
                    //新增对象设置当前主对象关联
                    item.setReimbursementRequest(entity);
                } else if (item.getId() < 0) {
                    //如果id为负值标识为待删除元素，并且重置id为正值，以便hibernate后续删除元素
                    item.setId(-item.getId());
                    return true;
                }
                //新增或更新则保留元素
                return false;
            }));
        }
        return super.editSave(entity);
    }

    @RequiresPermissions("演示样例:报销申请")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    @Override
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(ids);
    }
}