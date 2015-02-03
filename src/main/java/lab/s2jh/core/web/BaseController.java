package lab.s2jh.core.web;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.entity.PersistableEntity;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.EntityProcessCallbackHandler.EntityProcessCallbackException;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class BaseController<T extends PersistableEntity<ID>, ID extends Serializable> {

    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /** 子类指定泛型对应的实体Service接口对象 */
    abstract protected BaseService<T, ID> getEntityService();

    abstract protected T buildBindingEntity();

    protected Page<T> findByPage(Class<T> clazz, HttpServletRequest request) {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        GroupPropertyFilter groupFilter = GroupPropertyFilter.buildFromHttpRequest(clazz, request);
        appendFilterProperty(groupFilter);
        return getEntityService().findByPage(groupFilter, pageable);
    }

    protected OperationResult editSave(T entity) {
        getEntityService().save(entity);
        Map<String, Object> result = Maps.newHashMap();
        result.put("id", entity.getId());
        return OperationResult.buildSuccessResult("数据保存处理完成", result);
    }

    protected OperationResult delete(T entity) {
        return delete(entity.getId(), null);
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
                    break;
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
                return OperationResult.buildWarningResult("删除操作已处理. 成功:" + (entities.size() - rejectSize) + "条"
                        + ",失败:" + rejectSize + "条", errorMessageMap);
            }
        }
    }

    protected T initPrepareModel(Model model, ID id) {
        T entity = null;
        if (id == null) {
            entity = buildBindingEntity();
        } else {
            entity = getEntityService().findOne(id);
            model.addAttribute("id", id);
        }
        model.addAttribute("clazz", ServletUtils.buildValidateId(entity.getClass()));
        model.addAttribute("entity", entity);
        return entity;
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
     * 一般用于把没有分页的集合数据转换组装为对应的Page对象，传递到前端Grid组件以统一的JSON结构数据显示
     * @param list 泛型集合数据
     * @return 转换封装的Page分页结构对象
     */
    protected <S> Page<S> buildPageResultFromList(List<S> list) {
        Page<S> page = new PageImpl<S>(list);
        return page;
    }

    protected String writeValueAsJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return "{\"ERROR\":\"ERROR\"}";
        }
    }

    /**
     * 对于一些复杂处理逻辑需要基于提交数据服务器校验后有提示警告信息需要用户二次确认
     * 判断当前表单是否已被用户confirm确认OK
     */
    protected boolean postNotConfirmedByUser(HttpServletRequest request) {
        return !BooleanUtils.toBoolean(request.getParameter("_serverValidationConfirmed_"));
    }
}
