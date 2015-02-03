package lab.s2jh.module.sys.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import lab.s2jh.core.dao.jpa.BaseDao;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.util.Exceptions;
import lab.s2jh.module.sys.dao.MenuDao;
import lab.s2jh.module.sys.entity.Menu;
import lab.s2jh.module.sys.vo.NavMenuVO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.aop.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
@Transactional
public class MenuService extends BaseService<Menu, Long> {

    @Autowired
    private MenuDao menuDao;

    @Override
    protected BaseDao<Menu, Long> getEntityDao() {
        return menuDao;
    }

    @Transactional(readOnly = true)
    public List<Menu> findAllCached() {
        return menuDao.findAllCached();
    }

    @Transactional(readOnly = true)
    public List<NavMenuVO> findAvailableNavMenuVOs() {
        List<Menu> allMenus = findAllCached();
        Set<String> disabledMenuPaths = Sets.newHashSet();
        for (Menu menu : allMenus) {
            if (Boolean.TRUE.equals(menu.getDisabled())) {
                disabledMenuPaths.add(menu.getPath());
            }
        }
        List<Menu> availableMenus = Lists.newArrayList();
        for (Menu menu : allMenus) {
            if (!Boolean.TRUE.equals(menu.getDisabled())) {
                String path = menu.getPath();
                boolean disabled = false;
                if (CollectionUtils.isNotEmpty(disabledMenuPaths)) {
                    for (String disabledMenuPath : disabledMenuPaths) {
                        if (path.startsWith(disabledMenuPath)) {
                            disabled = true;
                            break;
                        }
                    }
                }
                if (!disabled) {
                    availableMenus.add(menu);
                }
            }
        }

        List<NavMenuVO> navMenuVOs = Lists.newArrayList();
        for (Menu menu : availableMenus) {
            NavMenuVO vo = new NavMenuVO();
            navMenuVOs.add(vo);
            vo.setPath(menu.getPath());
            vo.setStyle(menu.getStyle());
            vo.setUrl(menu.getUrl());
            vo.setInitOpen(menu.getInitOpen());

            //基于记录的Controller类和方法信息构造MethodInvocation,用于后续调用shiro的拦截器进行访问权限比对
            if (StringUtils.isNotBlank(menu.getControllerMethod())) {

                try {
                    final Class<?> clazz = ClassUtils.getClass(menu.getControllerClass());
                    Method[] methods = clazz.getMethods();
                    for (final Method method : methods) {
                        if (method.getName().equals(menu.getControllerMethod())) {
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            if (rm.method() == null || rm.method().length == 0
                                    || ArrayUtils.contains(rm.method(), RequestMethod.GET)) {
                                vo.setMethodInvocation(new MethodInvocation() {

                                    @Override
                                    public Object proceed() throws Throwable {
                                        return null;
                                    }

                                    @Override
                                    public Object getThis() {
                                        try {
                                            return clazz.newInstance();
                                        } catch (Exception e) {
                                            Exceptions.unchecked(e);
                                        }
                                        return null;
                                    }

                                    @Override
                                    public Method getMethod() {
                                        return method;
                                    }

                                    @Override
                                    public Object[] getArguments() {
                                        return null;
                                    }
                                });
                                break;
                            }
                        }
                    }

                } catch (Exception e) {
                    Exceptions.unchecked(e);
                }
            }
        }

        return navMenuVOs;
    }
}
