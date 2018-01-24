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
package com.entdiy.support.web;

import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.service.NotifyMessageService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class SupportNotifyMessageController {

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserService userService;

    @MetaData("用户未读公告数目")
    @RequestMapping(value = "/admin/notify-message/count", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult notifyMessageCount(HttpServletRequest request) {
        User user = userService.findCurrentAuthUser();
        String platform = request.getParameter("platform");
        if (StringUtils.isBlank(platform)) {
            platform = NotifyMessage.NotifyMessagePlatformEnum.web_admin.name();
        }
        return OperationResult.buildSuccessResult(notifyMessageService.findCountToRead(user, platform));
    }

    @MenuData("个人信息:公告消息")
    @RequestMapping(value = "/admin/profile/notify-message", method = RequestMethod.GET)
    public String notifyMessageIndex() {
        return "admin/profile/notifyMessage-index";
    }

    @MetaData("公告消息列表")
    @RequestMapping(value = "/admin/profile/notify-message-list", method = RequestMethod.GET)
    public String notifyMessageList(HttpServletRequest request, Model model) {
        User user = userService.findCurrentAuthUser();
        List<NotifyMessage> notifyMessages = null;
        String readed = request.getParameter("readed");
        if (StringUtils.isBlank(readed)) {
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web_admin", null);
        } else {
            notifyMessages = notifyMessageService.findStatedEffectiveMessages(user, "web_admin",
                    BooleanUtils.toBoolean(request.getParameter("readed")));
        }
        model.addAttribute("notifyMessages", notifyMessages);
        return "admin/profile/notifyMessage-list";
    }

    @MetaData("公告消息读取")
    @RequestMapping(value = "/admin/profile/notify-message-view/{messageId}", method = RequestMethod.GET)
    public String notifyMessageView(@PathVariable("messageId") Long messageId, Model model) {
        User user = userService.findCurrentAuthUser();
        NotifyMessage notifyMessage = notifyMessageService.findOne(messageId);
        notifyMessageService.processUserRead(notifyMessage, user);
        model.addAttribute("notifyMessage", notifyMessage);
        return "admin/profile/notifyMessage-view";
    }
}
