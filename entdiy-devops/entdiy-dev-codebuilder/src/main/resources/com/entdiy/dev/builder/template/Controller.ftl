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
package ${root_package}.web.admin;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.core.web.json.JsonViews;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ${root_package}.entity.${entity_name};
import ${root_package}.service.${entity_name}Service;

import com.fasterxml.jackson.annotation.JsonView;

@MetaData("${model_title}管理")
@Controller
@RequestMapping(value = "/admin${model_path}/${entity_name_field_line}")
public class ${entity_name}Controller extends BaseController<${entity_name},${id_type}> {

    @Autowired
    private ${entity_name}Service ${entity_name_uncapitalize}Service;

    @MenuData("${model_title}管理")
    @RequiresPermissions("${model_title}")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity ${entity_name} entity) {
        return "admin${model_path}/${entity_name_uncapitalize}-index";
    }   
    
    @RequiresPermissions("${model_title}管理")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public Page<${entity_name}> findByPage(@ModelPropertyFilter(${entity_name}.class) GroupPropertyFilter filter,
                                           @ModelPageableRequest Pageable pageable) {
        return ${entity_name_uncapitalize}Service.findByPage(filter,pageable);
    }
    
    @RequestMapping(value = "/edit-tabs", method = RequestMethod.GET)
    public String editTabs(@ModelEntity ${entity_name} entity) {
        return "admin${model_path}/${entity_name_uncapitalize}-inputTabs";
    }

    @RequiresPermissions("${model_title}编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(@ModelEntity ${entity_name} entity) {
        return "admin${model_path}/${entity_name_uncapitalize}-inputBasic";
    }

    @RequiresPermissions("${model_title}编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity ${entity_name} entity) {
        return super.editSave(${entity_name_uncapitalize}Service, entity);
    }

    @RequiresPermissions("${model_title}删除")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(${entity_name_uncapitalize}Service, ids);
    }
}