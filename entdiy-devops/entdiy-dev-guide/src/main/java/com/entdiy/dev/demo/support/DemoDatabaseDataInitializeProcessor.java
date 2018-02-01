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
package com.entdiy.dev.demo.support;

import com.entdiy.auth.entity.Account;
import com.entdiy.auth.entity.Department;
import com.entdiy.auth.entity.User;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.data.AbstractDatabaseDataInitializeProcessor;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.util.MockEntityUtils;
import com.entdiy.core.web.AppContextHolder;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.entdiy.dev.demo.entity.DemoProduct;
import com.entdiy.dev.demo.entity.DemoReimbursementRequest;
import com.entdiy.dev.demo.service.DemoProductService;
import com.entdiy.dev.demo.service.DemoReimbursementRequestService;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.entity.UserMessage;
import com.entdiy.sys.service.DataDictService;
import com.entdiy.sys.service.NotifyMessageService;
import com.entdiy.sys.service.UserMessageService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 演示数据初始化处理器
 */
@Component
@Order
public class DemoDatabaseDataInitializeProcessor extends AbstractDatabaseDataInitializeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDatabaseDataInitializeProcessor.class);

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private NotifyMessageService notifyMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DepartmentService departmentService;


    @Autowired
    private DemoReimbursementRequestService reimbursementRequestService;

    @Autowired
    private DemoProductService demoProductService;

    @Override
    public void initializeInternal() {
        logger.info("Running " + this.getClass().getName());

        if (AppContextHolder.isDemoMode() || AppContextHolder.isDevMode()) {
            LocalDateTime now = DateUtils.currentDateTime();
            String prefix = "demo";

            //演示用户数据
            PropertyFilter propertyFilter = new PropertyFilter(PropertyFilter.MatchType.BW, "account.authUid", prefix);
            List<User> users = userService.findByFilter(propertyFilter);
            List<Department> departments = departmentService.findByFilter(new PropertyFilter(PropertyFilter.MatchType.NN, "parent", true));

            if (CollectionUtils.isEmpty(users)) {
                Department rootDepartment = new Department();
                rootDepartment.setCode("ROOT");
                rootDepartment.setName("总部");
                departmentService.save(rootDepartment);
                entityManager.flush();


                Department department10 = new Department();
                department10.setCode("SC00");
                department10.setName("市场部");
                department10.setParent(rootDepartment);
                departmentService.save(department10);
                entityManager.flush();

                Department department11 = new Department();
                department11.setCode("SC01");
                department11.setName("市场一部");
                department11.setParent(department10);
                departmentService.save(department11);

                Department department12 = new Department();
                department12.setCode("SC02");
                department12.setName("市场二部");
                department12.setParent(department10);
                departmentService.save(department12);

                Department department13 = new Department();
                department13.setCode("SC03");
                department13.setName("市场三部");
                department13.setParent(department10);
                departmentService.save(department13);
                entityManager.flush();

                Department department20 = new Department();
                department20.setCode("YF00");
                department20.setName("研发部");
                department20.setParent(rootDepartment);
                departmentService.save(department20);
                entityManager.flush();

                Department department21 = new Department();
                department21.setCode("YF01");
                department21.setName("研发一部");
                department21.setParent(department20);
                departmentService.save(department21);

                Department department22 = new Department();
                department22.setCode("YF02");
                department22.setName("研发二部");
                department22.setParent(department20);
                departmentService.save(department22);
                entityManager.flush();

                departments = departmentService.findByFilter(new PropertyFilter(PropertyFilter.MatchType.NN, "parent", true));
                {
                    int random = MockEntityUtils.randomInt(20, 40);
                    for (int i = 0; i < random; i++) {
                        String seq = String.format("%03d", i);
                        //基于当前循环流水号作为模拟数据账号
                        String authUid = prefix + seq;
                        Account account = accountService.findByUsername(Account.AuthTypeEnum.admin, authUid);
                        if (account == null) {
                            //随机用户注册日期: 当前系统日期之前若干天
                            DateUtils.setCurrentDateTime(MockEntityUtils.randomDateTime(90, -7));

                            //构造随机属性值填充用户对象。一般随机属性生成后，需要对一些特定业务属性特殊设置。
                            account = MockEntityUtils.buildMockObject(Account.class);
                            account.setAuthType(Account.AuthTypeEnum.admin);
                            account.setDataDomain(GlobalConstant.DEFAULT_VALUE);
                            account.setAuthUid(authUid);
                            account.setMobile("100123456" + (10 + i));
                            //对email属性设置有效格式的值，否则无法通过实体上定义的@Email注解验证
                            account.setEmail(account.getAuthUid() + "@entdiy.com");
                            //调用业务接口进行模拟数据保存
                            accountService.save(account, "123456");

                            User user = MockEntityUtils.buildMockObject(User.class);
                            user.setAccount(account);
                            user.setDepartment(MockEntityUtils.randomCandidates(departments));
                            user.setTrueName("模拟账号" + authUid);
                            userService.save(user);

                            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                            entityManager.flush();
                        }
                        users.add(userService.findByAccount(account));
                    }
                }
                //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                entityManager.flush();
                users = userService.findByFilter(propertyFilter);


                {
                    DataDict entity = dataDictService.findByProperty("primaryKey", GlobalConstant.DATADICT_MESSAGE_TYPE);

                    DataDict item = new DataDict();
                    item.setPrimaryKey("normal");
                    item.setPrimaryValue("一般通知");
                    item.setSecondaryValue("#32CFC4");
                    item.setParent(entity);
                    dataDictService.save(item);

                    item = new DataDict();
                    item.setPrimaryKey("important");
                    item.setPrimaryValue("重要通知");
                    item.setSecondaryValue("#FF645D");
                    item.setParent(entity);
                    dataDictService.save(item);

                    item = new DataDict();
                    item.setPrimaryKey("urgent");
                    item.setPrimaryValue("紧急通知");
                    item.setSecondaryValue("#FF0000");
                    item.setParent(entity);
                    dataDictService.save(item);

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();
                }

                //初始化演示通知消息
                if (isEmptyTable(NotifyMessage.class)) {
                    NotifyMessage entity = new NotifyMessage();
                    entity.setEffective(true);
                    entity.setType("normal");
                    entity.setTitle("欢迎访问" + AppContextHolder.getSystemName());
                    entity.setPublishTime(now);
                    entity.setMessage("<p>系统初始化时间：" + now.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER) + "</p>");
                    notifyMessageService.save(entity);

                    //默认root用户读取当前公告消息
                    notifyMessageService.processUserRead(entity, userService.findByAccount(accountService.findByUsername(Account.AuthTypeEnum.admin, "root")));

                    entity = new NotifyMessage();
                    entity.setEffective(true);
                    entity.setType("important");
                    entity.setTitle("版本更新通知");
                    entity.setPublishTime(now.plusDays(MockEntityUtils.randomInt(1, 3)));
                    entity.setMessage("<p>整体重构项目Maven结构，模块化拆分，使定制开发能按需所取</p><p>UI基础框架版本从 Metronic 1.4.5 升级到 4.7.5</p>");
                    notifyMessageService.save(entity);

                    entity = new NotifyMessage();
                    entity.setEffective(true);
                    entity.setType("urgent");
                    entity.setTitle("系统更新维护通知");
                    entity.setPublishTime(now.plusDays(MockEntityUtils.randomInt(4, 7)));
                    entity.setMessage("<p>计划在XX进行系统迁移升级，届时本系统不可用，预计一小时迁移完成恢复使用</p>");
                    notifyMessageService.save(entity);

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();
                }

                //初始化演示通知消息
                if (isEmptyTable(UserMessage.class)) {
                    Account adminAccount = accountService.findByUsername(Account.AuthTypeEnum.admin, GlobalConstant.ROOT_VALUE);
                    User admin = userService.findByAccount(adminAccount);

                    UserMessage entity = new UserMessage();
                    entity.setType("normal");
                    entity.setPublishTime(DateUtils.currentDateTime());
                    entity.setTitle("演示个人消息1");
                    entity.setTargetUser(admin);
                    entity.setMessage("<p>演示定向发送个人消息1内容</p>");
                    userMessageService.save(entity);

                    entity = new UserMessage();
                    entity.setType("important");
                    entity.setPublishTime(DateUtils.currentDateTime());
                    entity.setTitle("演示个人消息2");
                    entity.setTargetUser(admin);
                    entity.setMessage("<p>演示定向发送个人消息2内容</p>");
                    userMessageService.save(entity);
                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();

                    int random = MockEntityUtils.randomInt(20, 40);
                    for (int i = 0; i < random; i++) {
                        String seq = String.format("%03d", i);
                        entity = new UserMessage();
                        entity.setType(MockEntityUtils.randomCandidates("normal", "important", "urgent"));
                        entity.setPublishTime(DateUtils.currentDateTime());
                        entity.setTitle("演示个人消息" + seq);
                        entity.setTargetUser(MockEntityUtils.randomCandidates(users));
                        entity.setMessage("<p>演示定向发送个人消息内容" + seq + "</p>");
                        userMessageService.save(entity);
                    }

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();
                }
            }


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
            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
            entityManager.flush();


            if (isEmptyTable(DemoProduct.class)) {
                int random = MockEntityUtils.randomInt(20, 40);
                for (int i = 0; i < random; i++) {
                    DemoProduct product = new DemoProduct();
                    product.setIntroImages(Lists.newArrayList(MockEntityUtils.buildMockObject(AttachmentFile.class, 3, 6)));
                    demoProductService.save(product);

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();
                }
            }
            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
            entityManager.flush();

            if (isEmptyTable(DemoReimbursementRequest.class)) {
                int random = MockEntityUtils.randomInt(20, 40);
                for (int i = 0; i < random; i++) {
                    DemoReimbursementRequest rr = MockEntityUtils.buildMockObject(DemoReimbursementRequest.class);
                    User user = MockEntityUtils.randomCandidates(users);
                    rr.setUser(user);
                    rr.setDepartment(MockEntityUtils.randomCandidates(departments));
                    //模拟用户在注册后随机时间下单
                    rr.setSubmitTime(user.getAccount().getSignupTime().plusHours(MockEntityUtils.randomInt(1, 240)));
                    reimbursementRequestService.save(rr);

                    //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
                    entityManager.flush();
                }
            }
        }
    }

}
