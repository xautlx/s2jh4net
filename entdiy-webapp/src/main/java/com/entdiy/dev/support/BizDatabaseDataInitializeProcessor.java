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
package com.entdiy.dev.support;

import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.RoleService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.dev.BizConstants;
import com.entdiy.support.data.ControllerMetaDataPostProcessor;
import com.entdiy.sys.entity.ConfigProperty;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.service.AttachmentFileStoreService;
import com.entdiy.sys.service.ConfigPropertyService;
import com.entdiy.sys.service.DataDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数据初始化处理器
 */
public class BizDatabaseDataInitializeProcessor extends AbstractDatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BizDatabaseDataInitializeProcessor.class);

    /** 标识优先执行依赖，以正常获取准备好的数据 */
    @Autowired
    private ControllerMetaDataPostProcessor controllerMetaDataPostProcessor;

    @Autowired
    private AttachmentFileStoreService attachmentFileStoreService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private UserService userService;


    @Override
    public void initializeInternal() throws Exception {
        logger.info("Running " + this.getClass().getName());

        //系统参数初始化
        if (configPropertyService.findByPropKey(GlobalConstant.CFG_SYSTEM_EMAILS) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(GlobalConstant.CFG_SYSTEM_EMAILS);
            entity.setPropName("系统通知邮件列表");
            entity.setSimpleValue("xautlx@hotmail.com");
            entity.setPropDescn("可逗号分隔多个");
            configPropertyService.save(entity);
        }


        //数据字典项初始化
        if (dataDictService.findByRootPrimaryKey(BizConstants.DataDict_Feedback_Type) == null) {
            DataDict entity = new DataDict();
            entity.setPrimaryKey(BizConstants.DataDict_Feedback_Type);
            entity.setPrimaryValue("APP意见或建议反馈类型");
            entity.setParent(dataDictService.findRoot());
            dataDictService.save(entity);

            DataDict item = new DataDict();
            item.setPrimaryKey("app_bug");
            item.setPrimaryValue("APP功能问题或异常");
            item.setSecondaryValue("APP使用过程出现任何问题或异常，请大致描述操作的过程以及错误现象等信息，我们将及时予以问题排查优化");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("app_improvement");
            item.setPrimaryValue("APP体验优化或新功能建议");
            item.setSecondaryValue("APP使用过程中欢迎随时向我们提出体验优化或新功能建议，我们将不断优化改进力争为您带来更佳的使用体验");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("others_feedback");
            item.setPrimaryValue("其他意见或建议");
            item.setSecondaryValue("畅所欲言，填写其他意见或建议，我们将耐心倾听");
            item.setParent(entity);
            dataDictService.save(item);
        }
    }
}
