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

import com.entdiy.auth.entity.Account;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.exception.WebException;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.util.FileUtils;
import com.entdiy.core.web.annotation.ModelEntity;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.json.JsonViews;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.security.annotation.AuthAccount;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.service.AttachmentFileService;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import com.entdiy.dev.demo.entity.DemoOrder;
import com.entdiy.dev.demo.entity.DemoSiteUser;
import com.entdiy.dev.demo.service.DemoOrderService;
import com.entdiy.dev.demo.service.DemoSiteUserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(GlobalConstant.API_MAPPING_PREFIX + "/demo")
public class DemoApiController {

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Autowired
    private DemoOrderService orderService;

    @Autowired
    private DemoSiteUserService siteUserService;

    @ModelAttribute
    @RequiresUser
    public void prepareSiteUser(Model model, @AuthAccount Account account) {
        model.addAttribute("siteUser", siteUserService.findByAccount(account));
    }

    @ApiOperation("客户订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", paramType = "query", required = false),
            @ApiImplicitParam(name = "search[NU_payTime]", value = "true=待支付订单，false=已支付订单", paramType = "query", allowableValues = "true,false", required = false)
    })
    @GetMapping("/order/list")
    @ResponseBody
    @JsonView(JsonViews.App.class)
    public OperationResult<Page<DemoOrder>> orderList(@ApiIgnore @ModelEntity("siteUser") DemoSiteUser siteUser,
                                                      @ApiIgnore @ModelPropertyFilter(DemoOrder.class) GroupPropertyFilter filter,
                                                      @ApiIgnore @ModelPageableRequest Pageable pageable) {
        filter.forceAnd(new PropertyFilter(PropertyFilter.MatchType.EQ, "siteUser", siteUser));
        return OperationResult.buildSuccessResult(orderService.findByPage(filter, pageable));
    }

    @ApiOperation("客户订单详情")
    @GetMapping("/order/detail")
    @ResponseBody
    @JsonView(JsonViews.App.class)
    public OperationResult<DemoOrder> orderDetail(Long id) {
        return OperationResult.buildSuccessResult(orderService.findOne(id).orElse(null));
    }

    @ApiOperation("客户信息")
    @GetMapping("/user/info")
    @ResponseBody
    @JsonView(JsonViews.App.class)
    public OperationResult<DemoSiteUser> userInfo(@ApiIgnore @ModelEntity("siteUser") DemoSiteUser siteUser) {
        attachmentFileService.injectToSourceEntity(siteUser, "headPhoto");
        return OperationResult.buildSuccessResult(siteUser);
    }

    @ApiOperation("客户信息修改")
    @PostMapping("/user/info/edit")
    @ResponseBody
    @JsonView(JsonViews.App.class)
    public OperationResult userInfoEdit(@ApiIgnore @ModelEntity(value = "siteUser",
            allowedFields = {"account.email", "account.mobile", "gender", "birthDay"}) DemoSiteUser siteUser) {
        siteUserService.save(siteUser);
        return OperationResult.buildSuccessResult();
    }

    @ApiOperation("客户头像修改")
    @PostMapping("/user/photo/edit")
    @ResponseBody
    @JsonView(JsonViews.App.class)
    public OperationResult userPhotoEdit(HttpServletRequest request,
                                         @ApiIgnore @ModelEntity(value = "siteUser", allowedFields = {"headPhoto"}) DemoSiteUser siteUser,
                                         @RequestParam("myfile") CommonsMultipartFile fileUpload) {

        try {
            String fileName = fileUpload.getOriginalFilename();
            long fileLength = fileUpload.getSize();
            //写入磁盘文件
            FileUtils.FileInfo fileInfo = FileUtils.writeFile(fileUpload.getInputStream(), FileUtils.SUB_DIR_FILES, fileName, fileLength);

            //创建附件记录
            AttachmentFile attachmentFile = new AttachmentFile();
            attachmentFile.setFileRealName(fileName);
            attachmentFile.setFileLength(fileLength);
            attachmentFile.setFileContentType(fileUpload.getContentType());
            attachmentFile.setRelativePath(fileInfo.getRelativePath());
            attachmentFile.setAbsolutePath(fileInfo.getAbsolutePath());
            attachmentFileService.save(attachmentFile);

            siteUser.setHeadPhoto(attachmentFile);
            //附件处理（考虑到附件就是简单的字段更新基本不会出现业务失败，即便异常也不会对主业务逻辑带来严重问题，因此放在另外事务中调用）
            attachmentFileService.saveBySourceEntity(siteUser, "headPhoto");

            Map<String, Object> retMap = Maps.newHashMap();
            retMap.put("url", attachmentFile.getAccessUrl());
            return OperationResult.buildSuccessResult(retMap);
        } catch (IOException e) {
            throw new WebException("Upload file error", e);
        }
    }
}
