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
package com.entdiy.support.web;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.annotation.AuthAccount;
import com.entdiy.sys.entity.AccountMessage;
import com.entdiy.sys.service.AccountMessageService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SupportAccountMessageController {

    @Autowired
    private AccountMessageService accountMessageService;

    @Autowired
    private UserService userService;

    @MenuData("个人信息:个人消息")
    @RequestMapping(value = "/admin/profile/account-message", method = RequestMethod.GET)
    public String accountMessageIndex() {
        return "admin/profile/accountMessage-index";
    }


    @MetaData("用户未读消息数目")
    @RequestMapping(value = "/admin/account-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult accountMessageCount(@AuthAccount Account account) {
        return OperationResult.buildSuccessResult(accountMessageService.findCountToRead(account));
    }

    @MetaData("个人消息列表")
    @RequestMapping(value = "/admin/profile/account-message-list", method = RequestMethod.GET)
    public String accountMessageList(@AuthAccount Account account,
                                  @ModelPropertyFilter(AccountMessage.class) GroupPropertyFilter filter,
                                  @ModelPageableRequest Pageable pageable,
                                  HttpServletRequest request, Model model) {
        filter.append(new PropertyFilter(PropertyFilter.MatchType.EQ, "targetAccount", account));
        String readed = request.getParameter("readed");
        if (StringUtils.isNotBlank(readed)) {
            if (BooleanUtils.toBoolean(request.getParameter("readed"))) {
                filter.append(new PropertyFilter(PropertyFilter.MatchType.NN, "firstReadTime", Boolean.TRUE));
            } else {
                filter.append(new PropertyFilter(PropertyFilter.MatchType.NU, "firstReadTime", Boolean.TRUE));
            }
        }
        JsonPage<AccountMessage> pageData = accountMessageService.findByPage(filter, pageable);
        model.addAttribute("pageData", pageData);
        return "admin/profile/accountMessage-list";
    }

    @MetaData("个人消息读取")
    @RequestMapping(value = "/admin/profile/account-message-view/{messageId}", method = RequestMethod.GET)
    public String accountMessageView(@AuthAccount Account account, @PathVariable("messageId") Long messageId, Model model) {

        accountMessageService.findOptionalOne(messageId).ifPresent(one -> {
            accountMessageService.processUserRead(one);
            model.addAttribute("accountMessage", one);
        });
        return "admin/profile/accountMessage-view";
    }
}
