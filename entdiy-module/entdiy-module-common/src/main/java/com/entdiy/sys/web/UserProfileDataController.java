/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
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

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.UserProfileData;
import com.entdiy.sys.service.UserProfileDataService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/sys/user-profile-data")
public class UserProfileDataController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileDataService userProfileDataService;

    @RequiresUser
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(HttpServletRequest request) {
        String[] codes = request.getParameter("codes").split(",");
        User user = userService.findCurrentAuthUser();
        for (String code : codes) {
            UserProfileData entity = userProfileDataService.findByUserAndCode(user, code);
            if (entity == null) {
                entity = new UserProfileData();
                entity.setUser(user);
            }
            entity.setCode(code);
            String[] values = request.getParameterValues(code);
            entity.setValue(StringUtils.join(values, ","));
            userProfileDataService.save(entity);
        }
        return OperationResult.buildSuccessResult("参数默认值设定成功");
    }

    @RequiresUser
    @RequestMapping(value = "/params", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> params() {
        User user = userService.findCurrentAuthUser();
        Map<String, String> datas = Maps.newHashMap();
        List<UserProfileData> items = userProfileDataService.findByUser(user);
        for (UserProfileData item : items) {
            datas.put(item.getCode(), item.getValue());
        }
        return datas;
    }
}
