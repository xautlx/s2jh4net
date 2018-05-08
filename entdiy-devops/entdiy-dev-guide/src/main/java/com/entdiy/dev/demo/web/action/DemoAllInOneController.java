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
import com.entdiy.auth.entity.Department;
import com.entdiy.auth.entity.Privilege;
import com.entdiy.auth.service.AccountService;
import com.entdiy.auth.service.DepartmentService;
import com.entdiy.auth.service.PrivilegeService;
import com.entdiy.auth.service.UserService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.cons.GlobalConstant;
import com.entdiy.core.entity.BaseNativeEntity;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.entdiy.dev.demo.entity.DemoProduct;
import com.entdiy.dev.demo.entity.DemoReimbursementRequest;
import com.entdiy.dev.demo.service.DemoProductService;
import com.entdiy.dev.demo.service.DemoReimbursementRequestService;
import com.entdiy.security.DefaultAuthUserDetails;
import com.entdiy.sys.entity.AttachmentFile;
import com.entdiy.sys.service.AttachmentFileService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.util.ClassUtils;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/dev/demo/all-in-one")
public class DemoAllInOneController {

    private final static Logger logger = LoggerFactory.getLogger(DemoAllInOneController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DemoProductService productService;

    @Autowired
    private DemoReimbursementRequestService reimbursementRequestService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    @MenuData("演示样例:UI组件集合")
    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "dev/demo/allInOne-index";
    }

    @RequiresRoles(DefaultAuthUserDetails.ROLE_MGMT_USER)
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String detail(HttpServletRequest request, Model model) {
        Map<String, String> clazzMapping = Maps.newHashMap();
        //搜索所有entity对象，并自动进行自增初始化值设置
        ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
        scan.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        Set<BeanDefinition> beanDefinitions = scan.findCandidateComponents("**.entity.**");
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Class<?> entityClass = ClassUtils.forName(beanDefinition.getBeanClassName());
            Audited audited = entityClass.getAnnotation(Audited.class);
            if (audited != null) {
                MetaData metaData = entityClass.getAnnotation(MetaData.class);
                if (metaData != null) {
                    clazzMapping.put(entityClass.getName(), metaData.value());
                } else {
                    clazzMapping.put(entityClass.getName(), entityClass.getName());
                }
            }
        }
        model.addAttribute("clazzMapping", clazzMapping);

        //典型的select下拉option数据项
        Map<Long, String> multiSelectOptions = Maps.newLinkedHashMap();
        multiSelectOptions.put(1L, "选项AAA");
        multiSelectOptions.put(2L, "中文BBB");
        multiSelectOptions.put(3L, "选项CCC");
        multiSelectOptions.put(4L, "元素DDD");
        model.addAttribute("multiSelectOptions", multiSelectOptions);

        //Tag类型的select下拉option数据项
        String[] multiSelectTags = new String[]{"标签AAA", "关键字BB", "选项CCC", "分类DDD"};
        model.addAttribute("multiSelectTags", multiSelectTags);

        MockEntity entity = new MockEntity();
        entity.setSelectedId(2L);
        entity.setSelectedIds(new Long[]{2L, 3L});
        entity.setTextContent("分类DDD");
        entity.setSplitText("标签AAA,选项CCC");
        entity.setDepartment(departmentService.findByCode("YF02").get());
        entity.setDepartments(Lists.newArrayList(departmentService.findByCode("YF02").get(),
                departmentService.findByCode("SC03").get()));
        {
            List<DemoProduct> products = productService.findAll(1L, 2L, 3L, 4L);
            if (CollectionUtils.isNotEmpty(products)) {
                DemoProduct product = products.get(0);
                attachmentFileService.injectToSourceEntity(product, "mainImage", "introImages");
                entity.setOneImage(product.getMainImage());
                entity.setMultiImages(product.getIntroImages());
            }
        }

        {
            List<DemoProduct> products = productService.findAll(5L, 6L, 6L, 7L);
            if (CollectionUtils.isNotEmpty(products)) {
                DemoProduct product = products.get(0);
                attachmentFileService.injectToSourceEntity(product, "mainImage", "introImages");
                entity.setImagePath(product.getMainImage().getAccessUrl());
                entity.setImagePaths(product.getIntroImages().stream().map(one -> one.getAccessUrl()).collect(Collectors.joining(",")));
            }
        }

        {
            List<DemoReimbursementRequest> items = reimbursementRequestService.findAll(1L, 2L, 3L, 4L);
            if (CollectionUtils.isNotEmpty(items)) {
                DemoReimbursementRequest item = items.get(0);
                attachmentFileService.injectToSourceEntity(item, "receiptAttachmentFiles");
                entity.setMultiFiles(item.getReceiptAttachmentFiles());
            }
        }

        model.addAttribute("entity", entity);

        //构造用于remote类型select元素的初始化显示option数据项
        Map<Long, String> initSelectOption = Maps.newLinkedHashMap();
        initSelectOption.put(entity.getId(), entity.getTextContent());
        model.addAttribute("initSelectOption", initSelectOption);

        //二维码组件使用
        model.addAttribute("webContextFullUrl", ServletUtils.getRequestFullContextURL(request));

        model.addAttribute("user", userService.findByAccount(accountService.findByUsername(Account.AuthTypeEnum.admin, "manager")));

        return "dev/demo/allInOne-detail";
    }

    @RequestMapping(value = "/show-form-data", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult showFormData(HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        StringBuilder sb = new StringBuilder("服务器接收到的表单数据列表：");
        sb.append("<ol>");
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String[] values = request.getParameterValues(name);
            sb.append("<li><b>" + name + ":</b>" + StringUtils.join(values, ",") + "</li>");
        }
        sb.append("</ol>");
        return OperationResult.buildSuccessResult(sb.toString());
    }

    @RequestMapping(value = "/docs/mock/tags", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> tagsData(Model model, @RequestParam("q") String q) {
        List<Map<String, Object>> items = Lists.newArrayList();
        for (int i = 0, length = new Double((5 + Math.random() * 10)).intValue(); i < length; i++) {
            Map<String, Object> item = Maps.newHashMap();
            String txt = q + "模拟选项" + i;
            item.put("id", txt);
            item.put("text", txt);
            items.add(item);
        }
        return items;
    }

    @RequestMapping(value = "/ui-feature/dropdownselect", method = RequestMethod.GET)
    public String uiFeatureDropdownselect(Model model) {
        return "docs/ui-feature-dropdownselect";
    }

    public Map<Long, Object> mockRemoteSelectOptions(Model model, @RequestParam("code") String code) {
        Map<Long, Object> data = Maps.newLinkedHashMap();
        for (long i = 0; i < 10; i++) {
            data.put(i, code + "选项" + i);
        }
        return data;
    }

    @RequestMapping(value = "/infinite-scroll-items", method = RequestMethod.GET)
    public Object repayLogData(HttpServletRequest request, Model model) {
        GroupPropertyFilter groupPropertyFilter = GroupPropertyFilter.buildFromHttpRequest(Privilege.class, request);
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        model.addAttribute("pageData", privilegeService.findByPage(groupPropertyFilter, pageable));
        return "dev/demo/allInOne-infiniteScrollItems";
    }

    @RequestMapping(value = "/btn-post", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult btnPost(Model model) {
        return OperationResult.buildSuccessResult("模拟POST数据处理成功");
    }

    @MetaData("模拟表单校验Confirm")
    @RequestMapping(value = "/validation-confirm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult validationConfirm(HttpServletRequest request, Model model,
                                             @RequestParam("quantity") Integer quantity,
                                             @RequestParam(value = GlobalConstant.PARAM_KEY_USER_CONFIRMED, required = false) Boolean userConfirmed) {
        //先进行常规的must数据校验

        //检测本次提交表单没有用户已confirm确认标识，则进行相关预警校验检查
        if (userConfirmed == null || userConfirmed.equals(Boolean.FALSE)) {
            if (quantity > 100) {
                return OperationResult.buildConfirmResult("库存余量不足");
            }
        }

        //所有数据校验通过，调用业务处理逻辑

        return OperationResult.buildSuccessResult("模拟POST数据处理成功");
    }

    @GetMapping(value = "/validation-remote")
    @ResponseBody
    public String validationRemote(@RequestParam("startDate") LocalDate startDate) {
        //模拟业务校验：检查当前发货计划，发现只有10天以后可以安排发货，因此校验startDate必须是当前10天以后的日期
        LocalDate validStartDate = LocalDate.now().plusDays(10);
        if (startDate.isBefore(validStartDate)) {
            return "最早发货日期需在 " + validStartDate + " 之后";
        }
        return String.valueOf(true);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @Access(AccessType.FIELD)
    public static class MockEntity extends BaseNativeEntity {
        private static final long serialVersionUID = -9187040717483523936L;

        private Department department;

        private List<Department> departments;

        private Long selectedId;

        private Long[] selectedIds;

        private LocalDateTime saleDate = DateUtils.currentDateTime();

        private LocalDateTime publishTime = DateUtils.currentDateTime();

        private Date searchDate;

        private String textContent;

        private String htmlContent;

        private String splitText;

        private String[] emptyTexts;

        private String[] emptyTexts2;

        private String[] emptyTexts3;

        private String[] emptyTexts4;

        private AttachmentFile oneFile;

        private List<AttachmentFile> multiFiles;

        private String imagePath;

        private String imagePaths;

        private AttachmentFile oneImage;

        private List<AttachmentFile> multiImages;

        private BigDecimal quantity;

        private Boolean expired;


        private List<MockItemEntity> mockItemEntites;

        public String[] getSplitTexts() {
            return StringUtils.split(splitText, ",");
        }

        public void setSplitTexts(String[] splitTexts) {
            this.splitText = StringUtils.join(splitTexts, ",");
        }

        public Long[] getDepartmentIds() {
            return departments.stream().map(Department::getId).toArray(Long[]::new);
        }

        public List<MockItemEntity> getMockItemEntitesForDynamicTable() {
            if (CollectionUtils.isEmpty(mockItemEntites)) {
                List<MockItemEntity> items = Lists.newArrayList();
                items.add(new MockItemEntity());
                return items;
            } else {
                return mockItemEntites;
            }
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @Access(AccessType.FIELD)
    public static class MockItemEntity extends BaseNativeEntity {

        private static final long serialVersionUID = -2048009679981043819L;

        private MockEntity mockEntity;

        private Department department;

        private LocalDateTime saleDate = DateUtils.currentDateTime();

        private String textContent;

        private String imagePath;

        @NotNull
        private BigDecimal quantity;

        @Override
        public String toString() {
            return "MockItemEntity [department=" + department + ", saleDate=" + saleDate + ", textContent=" + textContent + ", imagePath="
                    + imagePath + "]";
        }
    }

}

