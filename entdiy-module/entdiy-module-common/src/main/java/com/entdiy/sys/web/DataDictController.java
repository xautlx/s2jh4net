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
package com.entdiy.sys.web;

import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.pagination.PropertyFilter.MatchType;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.service.DataDictService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/sys/data-dict")
public class DataDictController extends BaseController<DataDict, Long> {

    @Autowired
    private DataDictService dataDictService;

    @MenuData("配置管理:系统管理:数据字典")
    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@ModelEntity DataDict entity, Model model) {
        return "admin/sys/dataDict-index";
    }


    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonPage<DataDict> findByPage(@ModelPropertyFilter(DataDict.class) GroupPropertyFilter filter,
                                         @ModelPageableRequest Pageable pageable) {
        //如果没有业务查询参数，则限制只查询业务根节点数据
        if (filter.isEmptySearch()) {
            filter.forceAnd(new PropertyFilter(MatchType.EQ, "depth", 1));
        }
        return dataDictService.findByPage(filter, pageable);
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String editShow(@ModelEntity DataDict entity) {
        return "admin/sys/dataDict-inputBasic";
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelEntity DataDict entity, Model model) {
        return super.editSave(dataDictService, entity);
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(@RequestParam("ids") Long... ids) {
        return super.delete(dataDictService, ids);
    }

    @MetaData(value = "级联子数据集合")
    @RequestMapping(value = "/children", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> children(HttpServletRequest request, @RequestParam(value = "id") Long id) {
        return dataDictService.findMapDataById(id);
    }
}
