/**
 * Copyright © 2015 - 2017 EntDIY JavaEE Development Framework
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.entdiy.dev.demo.support;

import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.core.util.DateUtils;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.service.DataDictService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 演示数据初始化处理器
 */
@Component
public class DemoDatabaseDataInitializeProcessor extends AbstractDatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseDataInitializeProcessor.class);

    @Autowired
    private DataDictService dataDictService;

    @Override
    public void initializeInternal() {

        logger.info("Running " + this.getClass().getName());
        Date now = DateUtils.currentDate();

        //数据字典项初始化
        if (dataDictService.findByProperty("primaryKey", DemoConstant.DataDict_Demo_ReimbursementRequest_UseType) == null) {
            DataDict entity = new DataDict();
            entity.setPrimaryKey(DemoConstant.DataDict_Demo_ReimbursementRequest_UseType);
            entity.setPrimaryValue("报销申请:记账类型");
            dataDictService.save(entity);

            DataDict item = new DataDict();
            item.setPrimaryKey("BG");
            item.setPrimaryValue("办公用品");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("ZS");
            item.setPrimaryValue("住宿费");
            item.setParent(entity);
            dataDictService.save(item);

            item = new DataDict();
            item.setPrimaryKey("CY");
            item.setPrimaryValue("餐饮招待");
            item.setParent(entity);
            dataDictService.save(item);
        }

    }

}
