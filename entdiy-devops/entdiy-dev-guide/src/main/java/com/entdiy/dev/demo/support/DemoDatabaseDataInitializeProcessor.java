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
import com.entdiy.sys.entity.AccountMessage;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.entity.DataDict;
import com.entdiy.sys.entity.NotifyMessage;
import com.entdiy.sys.service.AccountMessageService;
import com.entdiy.sys.service.AttachmentFileService;
import com.entdiy.sys.service.DataDictService;
import com.entdiy.sys.service.NotifyMessageService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import com.entdiy.dev.demo.entity.*;
import com.entdiy.dev.demo.service.DemoOrderService;
import com.entdiy.dev.demo.service.DemoProductService;
import com.entdiy.dev.demo.service.DemoReimbursementRequestService;
import com.entdiy.dev.demo.service.DemoSiteUserService;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private AccountMessageService accountMessageService;

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

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Autowired
    private DemoOrderService orderService;

    @Autowired
    private DemoSiteUserService siteUserService;

    @Override
    public void initializeInternal() throws Exception {
        logger.info("Running " + this.getClass().getName());

        if (AppContextHolder.isDemoMode() || AppContextHolder.isDevMode()) {
            LocalDateTime now = DateUtils.currentDateTime();
            String prefix = "demo";

            //演示用户数据
            PropertyFilter propertyFilter = new PropertyFilter(PropertyFilter.MatchType.BW, "account.authUid", prefix);
            List<User> users = userService.findByFilter(propertyFilter);
            List<Department> departments = departmentService.findByFilter(new PropertyFilter(PropertyFilter.MatchType.NN, "parent", true));

            if (CollectionUtils.isEmpty(users)) {
                Department rootDepartment = departmentService.findRoot();

                Department department10 = new Department();
                department10.setCode("SC00");
                department10.setName("市场部");
                department10.setParent(rootDepartment);
                departmentService.save(department10);

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

                Department department20 = new Department();
                department20.setCode("YF00");
                department20.setName("研发部");
                department20.setParent(rootDepartment);
                departmentService.save(department20);

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

                departments = departmentService.findByFilter(new PropertyFilter(PropertyFilter.MatchType.NN, "parent", true));
                {
                    int random = MockEntityUtils.randomInt(8, 20);
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

                            User user = MockEntityUtils.buildMockObject(User.class);
                            user.setAccount(account);
                            user.setDepartment(MockEntityUtils.randomCandidates(departments));
                            user.setTrueName("模拟账号" + authUid);
                            userService.saveCascadeAccount(user, "123456");
                        }
                        users.add(userService.findByAccount(account));
                    }
                }
                users = userService.findByFilter(propertyFilter);


                {
                    DataDict entity = dataDictService.findByRootPrimaryKey(GlobalConstant.DATADICT_MESSAGE_TYPE).get();

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
                }

                //初始化演示通知消息
                if (isEmptyTable(AccountMessage.class)) {
                    Account adminAccount = accountService.findByUsername(Account.AuthTypeEnum.admin, GlobalConstant.ROOT_VALUE);

                    AccountMessage entity = new AccountMessage();
                    entity.setType("normal");
                    entity.setPublishTime(MockEntityUtils.randomDateTime(10, 0));
                    entity.setTitle("演示个人消息1");
                    entity.setTargetAccount(adminAccount);
                    entity.setMessage("<p>演示定向发送个人消息1内容</p>");
                    accountMessageService.save(entity);

                    entity = new AccountMessage();
                    entity.setType("important");
                    entity.setPublishTime(MockEntityUtils.randomDateTime(10, 0));
                    entity.setTitle("演示个人消息2");
                    entity.setTargetAccount(adminAccount);
                    entity.setMessage("<p>演示定向发送个人消息2内容</p>");
                    accountMessageService.save(entity);

                    int random = MockEntityUtils.randomInt(20, 40);
                    for (int i = 0; i < random; i++) {
                        String seq = String.format("%03d", i);
                        entity = new AccountMessage();
                        entity.setType(MockEntityUtils.randomCandidates("normal", "important", "urgent"));
                        entity.setPublishTime(MockEntityUtils.randomDateTime(10, 0));
                        entity.setTitle("演示个人消息" + seq);
                        entity.setTargetAccount(MockEntityUtils.randomCandidates(users).getAccount());
                        entity.setMessage("<p>演示定向发送个人消息内容" + seq + "</p>");
                        accountMessageService.save(entity);
                    }
                }
            }


            //数据字典项初始化
            if (!dataDictService.findByRootPrimaryKey(DemoConstant.DataDict_Demo_ReimbursementRequest_UseType).isPresent()) {
                DataDict entity = new DataDict();
                entity.setPrimaryKey(DemoConstant.DataDict_Demo_ReimbursementRequest_UseType);
                entity.setPrimaryValue("报销申请:记账类型");
                entity.setParent(dataDictService.findRoot());
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

            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] imageResources = resolver.getResources("classpath*:/META-INF/resources/assets/images/*.jpg");
            Resource[] avatarResources = resolver.getResources("classpath*:/META-INF/resources/assets/avatar/*.jpg");
            Resource[] fileResources = resolver.getResources("classpath*:/META-INF/resources/dev/docs/markdown/*.md");

            if (isEmptyTable(DemoSiteUser.class)) {
                //预置普通管理账号
                Account customerAccount = new Account();
                customerAccount.setAuthType(Account.AuthTypeEnum.site);
                customerAccount.setAuthUid("customer");
                customerAccount.setDataDomain(GlobalConstant.DEFAULT_VALUE);
                customerAccount.setEmail("customer@entdiy.com");

                DemoSiteUser customer = new DemoSiteUser();
                customer.setAccount(customerAccount);
                customer.setBirthDay(MockEntityUtils.randomDateTime(30, 0).toLocalDate());
                customer.setGender(GlobalConstant.GenderEnum.S);
                siteUserService.saveCascadeAccount(customer, "123456");

                AccountMessage entity = new AccountMessage();
                entity.setType("normal");
                entity.setPublishTime(MockEntityUtils.randomDateTime(10, 0));
                entity.setTitle("前端用户演示个人消息1");
                entity.setTargetAccount(customerAccount);
                entity.setMessage("<p>前端用户演示定向发送个人消息1内容</p>");
                accountMessageService.save(entity);

                entity = new AccountMessage();
                entity.setType("important");
                entity.setPublishTime(MockEntityUtils.randomDateTime(10, 0));
                entity.setTitle("前端用户演示个人消息2");
                entity.setTargetAccount(customerAccount);
                entity.setMessage("<p>前端用户演示定向发送个人消息2内容</p>");
                accountMessageService.save(entity);

                //随机模拟用户下单
                int orderCount = MockEntityUtils.randomInt(25, 35);
                for (int j = 0; j < orderCount; j++) {
                    //构造模拟订单对象
                    DemoOrder order = new DemoOrder();
                    //模拟订单号
                    order.setOrderNo("O0000" + (10 + j));
                    order.setOrderTitle(MockEntityUtils.randomString(10, 20));
                    order.setSiteUser(customer);

                    //模拟用户在注册后随机时间下单
                    DateUtils.setCurrentDateTime(customer.getCreateDate().plusHours(MockEntityUtils.randomInt(1, 240)));
                    orderService.submitOrder(order);

                    //随机部分订单支付
                    if (MockEntityUtils.randomBoolean()) {
                        //新事务中重新查询加载对象
                        order = orderService.findOne(order.getId()).get();
                        //设置付款时间为当前订单的下单时间之后的随机1到8小时的时间点
                        LocalDateTime randomTime = order.getSubmitTime().plusHours(MockEntityUtils.randomInt(1, 8));
                        DateUtils.setCurrentDateTime(randomTime);
                        orderService.payOrder(order);
                    }
                }
            }


            if (isEmptyTable(DemoProduct.class)) {
                int random = MockEntityUtils.randomInt(15, 30);
                for (int i = 0; i < random; i++) {
                    DemoProduct product = new DemoProduct();
                    product.setCode("P0" + (10 + i));
                    product.setName("商品" + product.getCode());
                    demoProductService.save(product);

                    //附件处理
                    //创建附件记录
                    List<AttachmentFile> attachmentFiles = Lists.newArrayList();
                    int randomInt = MockEntityUtils.randomInt(2, 4);
                    for (int j = 0; j < randomInt; j++) {
                        Resource resource = MockEntityUtils.randomCandidates(imageResources);
                        File file = new File(FileUtils.getTempDirectoryPath() + File.separator + resource.getFilename());
                        FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
                        AttachmentFile attachmentFile = new AttachmentFile();
                        attachmentFile.setFileRealName(file.getName());
                        attachmentFile.setFileLength(file.length());
                        attachmentFile.setFileContentType("image/jpg");
                        attachmentFile.setRelativePath("/assets/images/" + file.getName());
                        attachmentFile.setAbsolutePath("jar:/assets/images/" + file.getName());
                        attachmentFiles.add(attachmentFile);
                    }
                    //先提前上传保存附件
                    attachmentFileService.saveAll(attachmentFiles);

                    Resource resource = MockEntityUtils.randomCandidates(imageResources);
                    File file = new File(FileUtils.getTempDirectoryPath() + File.separator + resource.getFilename());
                    FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
                    AttachmentFile attachmentFile = new AttachmentFile();
                    attachmentFile.setFileRealName(file.getName());
                    attachmentFile.setFileLength(file.length());
                    attachmentFile.setFileContentType("image/jpg");
                    attachmentFile.setRelativePath("/assets/images/" + file.getName());
                    attachmentFile.setAbsolutePath("jar:/assets/images/" + file.getName());
                    attachmentFiles.add(attachmentFile);
                    attachmentFileService.save(attachmentFile);

                    //然后和当前主对象关联
                    product.setIntroImages(attachmentFiles);
                    product.setMainImage(attachmentFile);
                    attachmentFileService.saveBySourceEntity(product, "introImages", "mainImage");
                }
            }

            if (isEmptyTable(DemoReimbursementRequest.class)) {
                List<Department> leafDepartments = departments.stream().filter(one -> !one.hasChildren()).collect(Collectors.toList());
                int random = MockEntityUtils.randomInt(40, 60);
                for (int i = 0; i < random; i++) {
                    DemoReimbursementRequest rr = MockEntityUtils.buildMockObject(DemoReimbursementRequest.class);
                    rr.setUseType(MockEntityUtils.randomCandidates("BG", "ZS", "CY"));
                    User user = MockEntityUtils.randomCandidates(users);
                    rr.setUser(user);
                    rr.setDepartment(MockEntityUtils.randomCandidates(leafDepartments));
                    //模拟用户在注册后随机时间下单
                    rr.setSubmitTime(now.minusHours(MockEntityUtils.randomInt(12, 240)).minusMinutes(MockEntityUtils.randomInt(1, 60)).minusSeconds(MockEntityUtils.randomInt(1, 60)));
                    rr.setSubmitDate(rr.getSubmitTime().toLocalDate());

                    List<DemoReimbursementRequestItem> items = MockEntityUtils.buildMockObject(DemoReimbursementRequestItem.class, 1, 3);
                    items.forEach(one -> {
                        one.setReimbursementRequest(rr);
                        one.setEndDate(null);
                        one.setUseType(MockEntityUtils.randomCandidates("BG", "ZS", "CY"));
                    });
                    rr.setTotalInvoiceAmount(items.stream().map(DemoReimbursementRequestItem::getInvoiceAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                    rr.setReimbursementRequestItems(items);

                    reimbursementRequestService.save(rr);

                    //附件处理
                    //创建附件记录
                    List<AttachmentFile> attachmentFiles = Lists.newArrayList();
                    int randomInt = MockEntityUtils.randomInt(1, 2);
                    for (int j = 0; j < randomInt; j++) {
                        {
                            Resource resource = MockEntityUtils.randomCandidates(fileResources);
                            File file = new File(FileUtils.getTempDirectoryPath() + File.separator + resource.getFilename());
                            FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
                            AttachmentFile attachmentFile = new AttachmentFile();
                            attachmentFile.setFileRealName(file.getName());
                            attachmentFile.setFileLength(file.length());
                            attachmentFile.setFileContentType("plain/txt");
                            attachmentFile.setRelativePath("/dev/docs/markdown/" + file.getName());
                            attachmentFile.setAbsolutePath("jar:/dev/docs/markdown/" + file.getName());
                            attachmentFiles.add(attachmentFile);
                        }

                        {
                            Resource resource = MockEntityUtils.randomCandidates(imageResources);
                            File file = new File(FileUtils.getTempDirectoryPath() + File.separator + resource.getFilename());
                            FileUtils.copyInputStreamToFile(resource.getInputStream(), file);
                            AttachmentFile attachmentFile = new AttachmentFile();
                            attachmentFile.setFileRealName(file.getName());
                            attachmentFile.setFileLength(file.length());
                            attachmentFile.setFileContentType("image/jpg");
                            attachmentFile.setRelativePath("/assets/images/" + file.getName());
                            attachmentFile.setAbsolutePath("jar:/assets/images/" + file.getName());
                            attachmentFiles.add(attachmentFile);
                        }
                    }
                    //先提前上传保存附件
                    attachmentFileService.saveAll(attachmentFiles);

                    //然后和当前主对象关联
                    rr.setReceiptAttachmentFiles(attachmentFiles);
                    attachmentFileService.saveBySourceEntity(rr, "receiptAttachmentFiles");
                }
            }
        }
    }

}
