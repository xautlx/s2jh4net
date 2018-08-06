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
package com.entdiy.dev;

import com.entdiy.core.web.AppContextHolder;
import com.entdiy.security.DefaultAuthUserDetails;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
public class BizAdminController {

    private final static Logger logger = LoggerFactory.getLogger(BizAdminController.class);

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/admin/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
        model.addAttribute("devMode", AppContextHolder.isDevMode());
        return "admin/dashboard";
    }
}

