package lab.s2jh.module.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.pagination.GroupPropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.core.pagination.PropertyFilter.MatchType;
import lab.s2jh.core.service.BaseService;
import lab.s2jh.core.web.BaseController;
import lab.s2jh.core.web.view.OperationResult;
import lab.s2jh.module.sys.entity.DataDict;
import lab.s2jh.module.sys.service.DataDictService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/sys/data-dict")
public class DataDictController extends BaseController<DataDict, Long> {

    @Autowired
    private DataDictService dataDictService;

    @Override
    protected BaseService<DataDict, Long> getEntityService() {
        return dataDictService;
    }

    @Override
    protected DataDict buildBindingEntity() {
        return new DataDict();
    }

    @MenuData("配置管理:系统管理:数据字典")
    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "admin/sys/dataDict-index";
    }

    @Override
    protected void appendFilterProperty(GroupPropertyFilter groupPropertyFilter) {
        if (groupPropertyFilter.isEmptySearch()) {
            groupPropertyFilter.forceAnd(new PropertyFilter(MatchType.NU, "parent", true));
        }
        super.appendFilterProperty(groupPropertyFilter);
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Page<DataDict> findByPage(HttpServletRequest request) {
        return super.findByPage(DataDict.class, request);
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editSave(@ModelAttribute("entity") DataDict entity, Model model) {
        return super.editSave(entity);
    }

    @RequiresPermissions("配置管理:系统管理:数据字典")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult delete(HttpServletRequest request, @ModelAttribute("entity") DataDict entity) {
        //基于提交请求参数判断用户是否已经进行确认过，如果否则进行后续的业务逻辑校验及反馈提示，如果已确认则直接通过
        if (postNotConfirmedByUser(request)) {
            List<DataDict> children = entity.getChildren();
            //反馈信息待用户进行confirm确认，如果用户确认OK则自动再次发起请求
            if (CollectionUtils.isNotEmpty(children)) {
                return OperationResult.buildConfirmResult("如果删除当前项目将递归删除所有子项");
            }
        }
        return super.delete(entity);
    }

    @RequiresUser
    @ModelAttribute
    public void prepareModel(Model model, @RequestParam(value = "id", required = false) Long id) {
        super.initPrepareModel(model, id);
    }
}
