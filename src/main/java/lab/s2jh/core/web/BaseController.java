package lab.s2jh.core.web;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.exception.WebException;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.web.EntityProcessCallbackHandler.EntityProcessCallbackException;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class BaseController<T extends PersistableEntity<ID>, ID extends Serializable> {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /** 子类指定泛型对应的实体Service接口对象 */
    abstract protected BaseService<T, ID> getEntityService();

    /** 实体泛型对应的Class定义 */
    protected Class<T> entityClass;

    /** 主键泛型对应的Class定义 */
    protected Class<ID> entityIdClass;

    /**
     * 初始化构造方法，计算相关泛型对象
     */
    @SuppressWarnings("unchecked")
    public BaseController() {
        super();
        // 通过反射取得Entity的Class.
        try {
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
     * @return ID泛型对象实例
     */
    public ID getId(HttpServletRequest request) {
        return getId(request, "id");
    }

    /**
     * 将指定参数转换为ID泛型对应的主键变量实例
     * 另外，页面也会以Struts标签获取显示当前操作对象的ID值
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

    protected Page<T> findByPage(Class<T> clazz, HttpServletRequest request, EntityProcessCallbackHandler<T> handler) {
        //RoutingDataSourceAdvice.setSlaveDatasource();
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(clazz, request);
        appendFilterProperty(groupFilter);
        Page<T> pageData = getEntityService().findByPage(groupFilter, pageable);
        if (handler != null) {
            List<T> content = pageData.getContent();
            for (T entity : content) {
                try {
                    handler.processEntity(entity);
                } catch (EntityProcessCallbackException e) {
                    throw new WebException("entity process callback error", e);
                }
            }
        }
        return pageData;
    }

    protected Page<T> findByPage(Class<T> clazz, HttpServletRequest request) {
        return findByPage(clazz, request, null);
    }

    protected OperationResult editSave(T entity) {
        getEntityService().save(entity);
        Map<String, Object> result = Maps.newHashMap();
        result.put("id", entity.getId());
        return OperationResult.buildSuccessResult("数据保存处理完成", result);
    }

    protected OperationResult delete(ID... ids) {
        Assert.notNull(ids);
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
                try {
                    handler.processEntity(entity);
                } catch (EntityProcessCallbackException e) {
                    msg = e.getMessage();
                }
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
                logger.warn("entity delete failure", e);
                errorMessageMap.put(entity.getId(), e.getMessage());
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
            if (request.getMethod().equalsIgnoreCase("POST")) {
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
        model.addAttribute("clazz", entityClass.getName());
        model.addAttribute("entity", entity);
        return entity;
    }

    /**
     * 如果子类需要一对多关联对象批量处理，则在子类返回定制的detach对象
     * @param id
     * @return
     */
    protected T buildDetachedBindingEntity(ID id) {
        return null;
    }

    /**
     * 为了防止用户恶意传入数据修改不可访问的属性，采用白名单机制，只有在该方法中定义的属性才会进行自动绑定
     * 请记住把所有表单元素涉及到属性添加到此方法的setAllowedFields列表中，否则会出现页面数据没有正确保存到数据库
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat(DateUtils.DEFAULT_DATE_FORMAT), true));
        //binder.setAllowedFields("nick", "gender", "name", "idCardNo", "studentExt.dormitory");
    }

    /**
     * 子类额外追加过滤限制条件的入口方法，一般基于当前登录用户强制追加过滤条件
     * 注意：凡是基于当前登录用户进行的控制参数，一定不要通过页面请求参数方式传递，存在用户篡改请求数据访问非法数据的风险
     * 因此一定要在Controller层面通过覆写此回调函数或自己的业务方法中强制追加过滤条件
     * @param filters 已基于Request组装好查询条件的集合对象
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
