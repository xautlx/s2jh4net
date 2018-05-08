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
package com.entdiy.aud.web;

import com.entdiy.aud.envers.EntityRevision;
import com.entdiy.aud.envers.ExtDefaultRevisionEntity;
import com.entdiy.aud.service.RevisionEntityService;
import com.entdiy.core.annotation.MenuData;
import com.entdiy.core.annotation.MetaData;
import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.entity.BaseEntity;
import com.entdiy.core.exception.WebException;
import com.entdiy.core.pagination.ExtPageRequest;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.JsonPage;
import com.entdiy.core.web.BaseController;
import com.entdiy.core.web.annotation.ModelPageableRequest;
import com.entdiy.core.web.annotation.ModelPropertyFilter;
import com.entdiy.core.web.json.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.ClassUtils;
import org.hibernate.envers.Audited;
import org.hibernate.proxy.pojo.javassist.JavassistLazyInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin/aud/revision-entity")
public class RevisionEntityController extends BaseController<ExtDefaultRevisionEntity, Long> {

    private final static Logger logger = LoggerFactory.getLogger(RevisionEntityController.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RevisionEntityService revisionEntityService;

    @MenuData("配置管理:系统记录:业务操作记录")
    @RequiresPermissions("配置管理:系统记录:业务操作记录")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String revisionEntityUserIndex(Model model) throws Exception {
        return "admin/aud/revisionEntity-userIndex";
    }

    @RequiresPermissions("配置管理:系统记录:业务操作记录")
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    @ResponseBody
    @JsonView(JsonViews.Admin.class)
    public JsonPage<ExtDefaultRevisionEntity> findByPage(
            @ModelPropertyFilter(ExtDefaultRevisionEntity.class) GroupPropertyFilter filter,
            @ModelPageableRequest Pageable pageable) {
        return revisionEntityService.findByPage(filter, pageable);
    }

    /**
     * 版本数据主界面页面转向
     */
    @MenuData("配置管理:系统记录:数据变更记录")
    @RequiresPermissions("配置管理:系统记录:数据变更记录")
    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String revisionEntityDataIndex(Model model) {
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

        return "admin/aud/revisionEntity-dataIndex";
    }

    @MetaData(value = "版本对象属性列表")
    @RequestMapping(value = "/properties", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> revisionEntityProperties(HttpServletRequest request) {
        String clazz = request.getParameter("clazz");
        Class<?> entityClass = ClassUtils.forName(clazz);
        Map<String, String> properties = Maps.newLinkedHashMap();
        Map<Field, String> fields = getRevisionFields(entityClass);
        for (Map.Entry<Field, String> me : fields.entrySet()) {
            properties.put(me.getKey().getName(), me.getValue());
        }
        return properties;
    }

    @MetaData(value = "版本数据列表")
    @RequiresPermissions("配置管理:系统记录:数据变更记录")
    @RequestMapping(value = "/data/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<EntityRevision> revisionList(HttpServletRequest request) {
        String clazz = request.getParameter("clazz");
        Class<?> entityClass = ClassUtils.forName(clazz);

        String property = request.getParameter("property");
        Boolean hasChanged = null;
        String changed = request.getParameter("changed");
        if (StringUtils.isNotBlank(changed)) {
            hasChanged = BooleanUtils.toBooleanObject(changed);
        }

        String id = request.getParameter("id");
        List<EntityRevision> entityRevisions = revisionEntityService.findEntityRevisions(entityClass, NumberUtils.isDigits(id) ? Long.valueOf(id)
                : id, property, hasChanged);
        for (EntityRevision entityRevision : entityRevisions) {
            ExtDefaultRevisionEntity revEntity = entityRevision.getRevisionEntity();
            revEntity.setEntityClassName(clazz);
            revEntity.addExtraAttribute("entityId", id);
        }

        return ExtPageRequest.buildPageResultFromList(entityRevisions);
    }

    /**
     * 用于版本属性下拉列表集合
     *
     * @return
     */
    public Map<Field, String> getRevisionFields(final Class<?> entityClass) {
        Map<Field, String> revisionFields = Maps.newLinkedHashMap();
        Class<?> loopClass = entityClass;
        do {
            for (Field field : loopClass.getDeclaredFields()) {
                MetaData metaData = field.getAnnotation(MetaData.class);
                if (metaData != null && metaData.comparable()) {
                    revisionFields.put(field, metaData != null ? metaData.value() : field.getName().toUpperCase());
                }
            }
            loopClass = loopClass.getSuperclass();
        } while (!(loopClass.equals(BaseEntity.class) || loopClass.equals(Object.class)));

        return revisionFields;
    }

    /**
     * Revision版本数据对比显示
     */
    @MetaData(value = "版本数据对比")
    @RequiresPermissions("配置管理:系统记录:数据变更记录")
    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public String revisionCompare(Model model, HttpServletRequest request, @RequestParam("clazz") String clazz,
                                  @RequestParam(value = "entityId", required = false) Long entityId, @RequestParam("revs") Long[] revs) {
        Class<?> entityClass = ClassUtils.forName(request.getParameter("clazz"));

        //获取对应版本数组历史数据对象集合
        List<EntityRevision> entityRevisions = revisionEntityService.findEntityRevisions(entityClass, entityId, revs);

        List<Map<String, Object>> revEntityProperties = Lists.newArrayList();
        for (Map.Entry<Field, String> me : getRevisionFields(entityClass).entrySet()) {
            Field field = me.getKey();
            Map<String, Object> revEntityProperty = Maps.newHashMap();
            revEntityProperty.put("name", me.getValue());

            List<String> values = Lists.newArrayList();
            for (EntityRevision entityRevision : entityRevisions) {
                try {
                    Object value = FieldUtils.readField(entityRevision.getEntity(), field.getName(), true);
                    String valueDisplay = convertPropertyDisplay(entityRevision.getEntity(), field, value);
                    values.add(valueDisplay);
                } catch (IllegalAccessException e) {
                    throw new WebException(e.getMessage(), e);
                }
            }
            revEntityProperty.put("values", values);

            revEntityProperties.add(revEntityProperty);
        }

        model.addAttribute("entityRevisions", entityRevisions);
        model.addAttribute("revEntityProperties", revEntityProperties);
        model.addAttribute("pageRequestUid", System.currentTimeMillis());
        return "admin/aud/revisionEntity-compare";
    }

    /**
     * 将对象Value对象转换为显示字符串，子类可根据需要覆写此方法输出定制格式字符串
     *
     * @param entity 版本数据实体对象
     * @param field  版本字段属性
     * @param value  版本属性数据值
     * @return 格式化后处理的字符串
     */
    private String convertPropertyDisplay(Object entity, Field field, Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof AbstractPersistableEntity) {
            @SuppressWarnings("rawtypes")
            AbstractPersistableEntity persistableEntity = (AbstractPersistableEntity) value;
            String label = "N/A";
            try {
                label = persistableEntity.getDisplay();
            } catch (EntityNotFoundException e) {
                //Hibernate Envers默认始终查询对应Audit版本数据，有可能关联对象之前没有Audit记录，从而会导致Envers抛出未找到数据异常
                //此处做Hack处理：如果没有找到关联Audit记录，则查询关联主对象记录
                try {
                    //从Hibernate AOP增强对象反查对应实体对象数据
                    JavassistLazyInitializer jli = (JavassistLazyInitializer) FieldUtils.readDeclaredField(value, "handler", true);
                    Class entityClass = jli.getPersistentClass();
                    Serializable id = jli.getIdentifier();
                    Object obj = entityManager.find(entityClass, id);
                    AbstractPersistableEntity auditTargetEntity = (AbstractPersistableEntity) obj;
                    label = auditTargetEntity.getDisplay();
                } catch (IllegalAccessException iae) {
                    logger.warn(e.getMessage());
                }
            }
            return label;
        }
        return String.valueOf(value);
    }

}
