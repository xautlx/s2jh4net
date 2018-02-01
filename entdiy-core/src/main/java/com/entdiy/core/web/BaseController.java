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
package com.entdiy.core.web;

import com.entdiy.core.entity.AbstractPersistableEntity;
import com.entdiy.core.exception.BaseRuntimeException;
import com.entdiy.core.exception.WebException;
import com.entdiy.core.pagination.GroupPropertyFilter;
import com.entdiy.core.pagination.PropertyFilter;
import com.entdiy.core.service.BaseService;
import com.entdiy.core.util.DateUtils;
import com.entdiy.core.web.json.LocalDateSerializer;
import com.entdiy.core.web.json.LocalDateTimeSerializer;
import com.entdiy.core.web.util.ServletUtils;
import com.entdiy.core.web.view.OperationResult;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public abstract class BaseController<T extends AbstractPersistableEntity<ID>, ID extends Serializable> {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 子类指定泛型对应的实体Service接口对象
     */
    abstract protected BaseService<T, ID> getEntityService();

    /**
     * 实体泛型对应的Class定义
     */
    protected Class<T> entityClass;

    /**
     * 主键泛型对应的Class定义
     */
    protected Class<ID> entityIdClass;

    /**
     * 实体对象校验规则字符串
     */
    private String entityValidationRules;

    /**
     * 初始化构造方法，计算相关泛型对象
     */
    @PostConstruct
    public void init() {
        logger.debug("Invoking init method for {}", this.getClass());
        try {
            // 通过反射取得Entity的Class.
            Object genericClz = getClass().getGenericSuperclass();
            if (genericClz instanceof ParameterizedType) {
                entityClass = (Class<T>) ((ParameterizedType) genericClz).getActualTypeArguments()[0];
                entityIdClass = (Class<ID>) ((ParameterizedType) genericClz).getActualTypeArguments()[1];
            }
        } catch (Exception e) {
            throw new WebException(e.getMessage(), e);
        }
    }

    /**
     * 将id=123格式的字符串id参数转换为ID泛型对应的主键变量实例
     * 另外，页面也会以Struts标签获取显示当前操作对象的ID值
     *
     * @return ID泛型对象实例
     */
    public ID getId(HttpServletRequest request) {
        return getId(request, "id");
    }

    /**
     * 将指定参数转换为ID泛型对应的主键变量实例
     * 另外，页面也会以Struts标签获取显示当前操作对象的ID值
     *
     * @return ID泛型对象实例
     */
    @SuppressWarnings("unchecked")
    public ID getId(HttpServletRequest request, String paramName) {
        String entityId = request.getParameter(paramName);
        //jqGrid inline edit新增数据传入id=负数标识 
        if (StringUtils.isBlank(entityId) || entityId.startsWith("-")) {
            return null;
        }
        if (String.class.isAssignableFrom(entityIdClass)) {
            return (ID) entityId;
        } else if (Long.class.isAssignableFrom(entityIdClass)) {
            return (ID) (Long.valueOf(entityId));
        } else {
            throw new IllegalStateException("Undefine entity ID class: " + entityIdClass);
        }
    }

    protected Page<T> findByPage(Class<T> clazz, HttpServletRequest request, PropertyFilter... propertyFilters) {
        //RoutingDataSourceAdvice.setSlaveDatasource();
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(clazz, request);
        appendFilterProperty(groupFilter);
        if (propertyFilters != null && propertyFilters.length > 0) {
            for (PropertyFilter propertyFilter : propertyFilters) {
                groupFilter.forceAnd(propertyFilter);
            }
        }
        Page<T> pageData = getEntityService().findByPage(groupFilter, pageable);
        return pageData;
    }

    protected OperationResult editSave(T entity) {
        getEntityService().save(entity);
        Map<String, Object> result = Maps.newHashMap();
        result.put("id", entity.getId());
        return OperationResult.buildSuccessResult("数据保存处理完成", result);
    }

    protected OperationResult delete(ID... ids) {
        Assert.notNull(ids, "ids参数不能为空");
        return delete(ids, null);
    }

    protected OperationResult delete(ID[] ids, EntityProcessCallbackHandler<T> handler) {
        //删除失败的id和对应消息以Map结构返回，可用于前端批量显示错误提示和计算表格组件更新删除行项
        Map<ID, String> errorMessageMap = Maps.newLinkedHashMap();

        Set<T> enableDeleteEntities = Sets.newHashSet();
        Collection<T> entities = getEntityService().findAll(ids);
        for (T entity : entities) {
            String msg = null;
            //回调接口调用，比如以内部类方式传入对象是否可删除的检测逻辑
            if (handler != null) {
                msg = handler.processEntity(entity);
            }
            if (StringUtils.isBlank(msg)) {
                enableDeleteEntities.add(entity);
            } else {
                errorMessageMap.put(entity.getId(), msg);
            }
        }
        //对于批量删除,循环每个对象调用Service接口删除,则各对象删除事务分离
        //这样可以方便某些对象删除失败不影响其他对象删除
        //如果业务逻辑需要确保批量对象删除在同一个事务则请子类覆写调用Service的批量删除接口
        for (T entity : enableDeleteEntities) {
            try {
                getEntityService().delete(entity);
            } catch (Exception e) {
                String errorMessage = null;
                if (e instanceof DataIntegrityViolationException) {
                    Throwable cause = e;
                    while (cause != null) {
                        String sqlMessage = cause.getMessage();
                        if (sqlMessage.indexOf("FK") > -1) {
                            //提取外键名称，方便用户反馈问题是排查告知具体关联数据逻辑问题
                            //for MySQL
                            errorMessage = "该数据已被关联使用：FK" + StringUtils.substringBetween(sqlMessage, "FK", "`");
                            break;
                            //外键约束异常，不做logger日志
                        }
                        cause = cause.getCause();
                    }
                }
                if (errorMessage == null) {
                    //构建和记录友好和详细的错误信息及消息
                    //生成一个异常流水号，追加到错误消息上显示到前端用户，用户反馈问题时给出此流水号给运维或开发人员快速定位对应具体异常细节
                    String exceptionCode = BaseRuntimeException.buildExceptionCode();
                    errorMessage = exceptionCode + ": 数据删除操作失败，请联系管理员处理！";
                    logger.error(errorMessage, e);
                }
                errorMessageMap.put(entity.getId(), errorMessage);
            }
        }
        int rejectSize = errorMessageMap.size();
        if (rejectSize == 0) {
            return OperationResult.buildSuccessResult("成功删除所选选取记录:" + entities.size() + "条");
        } else {
            if (rejectSize == entities.size()) {
                return OperationResult.buildFailureResult("所有选取记录删除操作失败", errorMessageMap);
            } else {
                return OperationResult.buildWarningResult("删除操作已处理. 成功:" + (entities.size() - rejectSize) + "条" + ",失败:" + rejectSize + "条",
                        errorMessageMap);
            }
        }
    }

    protected T initPrepareModel(HttpServletRequest request, Model model, ID id) {
        T entity = null;
        if (id != null && StringUtils.isNotBlank(id.toString())) {
            //如果是以POST方式请求数据，则获取Detach状态的对象，其他则保留Session方式以便获取Lazy属性
            if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
                entity = buildDetachedBindingEntity(id);
            }
            //如果子类没有给出detach的对象，则依然采用非detach模式查询返回对象
            if (entity == null) {
                entity = getEntityService().findOne(id);
            }
            model.addAttribute("id", id);
        }
        if (entity == null) {
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                throw new WebException(e.getMessage(), e);
            }
        }
        model.addAttribute("entity", entity);

        //对于GET类型请求，追加校验规则JSON字符串属性
        if (HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
            if (AppContextHolder.isDevMode()) {
                // 开发模式则每次计算以便修改注解后及时生效
                model.addAttribute("validationRules", ServletUtils.buildValidateRules(entityClass));
            } else {
                if (entityValidationRules == null) {
                    entityValidationRules = ServletUtils.buildValidateRules(entityClass);
                }
                // 生成运行直接取当前类静态缓存值
                model.addAttribute("validationRules", entityValidationRules);
            }
        }

        return entity;
    }

    protected T fetchEntityFromModel(Model model) {
        return (T) model.asMap().get("entity");
    }

    /**
     * 如果编辑提交数据涉及到一对一或一对多关联对象更新处理，则需要返回Detached的对象实例，否则会遇到关联对象主键修改异常
     *
     * @param id 实体主键
     * @return Detached的对象实例
     */
    protected T buildDetachedBindingEntity(ID id) {
        return getEntityService().findDetachedOne(id);
    }

    /**
     * 为了防止用户恶意传入数据修改不可访问的属性，采用白名单机制，只有在该方法中定义的属性才会进行自动绑定
     * 请记住把所有表单元素涉及到属性添加到此方法的setAllowedFields列表中，否则会出现页面数据没有正确保存到数据库
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        //自定义类型属性转换，动态处理日期、时间等不同格式数据转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(DateUtils.parseDate(text));
                }
            }

            @Override
            public String getAsText() {
                Date value = (Date) getValue();
                return (value != null ? DateUtils.formatDate(value) : "");
            }
        });
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(LocalDate.parse(text, LocalDateSerializer.LOCAL_DATE_FORMATTER));
                }
            }

            @Override
            public String getAsText() {
                LocalDate value = (LocalDate) getValue();
                return (value != null ? value.format(LocalDateSerializer.LOCAL_DATE_FORMATTER) : "");
            }
        });
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (StringUtils.isBlank(text)) {
                    // Treat empty String as null value.
                    setValue(null);
                } else {
                    setValue(LocalDateTime.parse(text, LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER));
                }
            }

            @Override
            public String getAsText() {
                LocalDateTime value = (LocalDateTime) getValue();
                return (value != null ? value.format(LocalDateTimeSerializer.LOCAL_DATE_TIME_FORMATTER) : "");
            }
        });

        // Converts empty strings into null when a form is submitted
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));


        //binder.setAllowedFields("nick", "gender", "name", "idCardNo", "studentExt.dormitory");
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 注意：凡是基于当前登录用户进行的控制参数，一定不要通过页面请求参数方式传递，存在用户篡改请求数据访问非法数据的风险
     * 因此一定要在Controller层面通过覆写此回调函数或自己的业务方法中强制追加过滤条件
     *
     * @param groupPropertyFilter 已基于Request组装好查询条件的集合对象
     */
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {

    }

    /**
     * 对于一些复杂处理逻辑需要基于提交数据服务器校验后有提示警告信息需要用户二次确认
     * 判断当前表单是否已被用户confirm确认OK
     */
    protected boolean postNotConfirmedByUser(HttpServletRequest request) {
        return !BooleanUtils.toBoolean(request.getParameter("_serverValidationConfirmed_"));
    }
}
