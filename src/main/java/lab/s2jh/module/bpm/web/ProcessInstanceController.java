package lab.s2jh.module.bpm.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.annotation.MenuData;
import lab.s2jh.core.pagination.PropertyFilter;
import lab.s2jh.module.bpm.service.ActivitiService;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@Controller
@RequestMapping(value = "/admin/bpm/process-instance")
public class ProcessInstanceController {

    protected static Logger logger = LoggerFactory.getLogger(ProcessInstanceController.class);

    @Autowired(required = false)
    private RepositoryService repositoryService;

    @Autowired(required = false)
    private RuntimeService runtimeService;

    @Autowired(required = false)
    private HistoryService historyService;

    @Autowired
    protected ActivitiService activitiService;

    @MenuData("配置管理:流程管理:流程运行实例")
    @RequiresPermissions("配置管理:流程管理:流程运行实例")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "admin/bpm/processInstance-index";
    }

    @RequiresPermissions("配置管理:流程管理:流程运行实例")
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Model model, String id) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        ProcessInstance processInstance = processInstanceQuery.processInstanceId(id).singleResult();
        model.addAttribute("processInstance", processInstance);
        return "admin/bpm/processInstance-view";
    }

    public Map<String, String> getProcessDefinitions() {
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.list();
        Map<String, String> datas = Maps.newHashMap();
        for (ProcessDefinition processDefinition : processDefinitions) {
            datas.put(processDefinition.getKey(), processDefinition.getName());
        }
        return datas;
    }

    @RequiresPermissions("配置管理:流程管理:流程运行实例")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object findByPageRunning(HttpServletRequest request) {
        Pageable pageable = PropertyFilter.buildPageableFromHttpRequest(request);
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        String searchBusinessKey = request.getParameter("businessKey");
        if (StringUtils.isNotBlank(searchBusinessKey)) {
            processInstanceQuery.processInstanceBusinessKey(searchBusinessKey);
        }
        String processDefinitionKey = request.getParameter("processDefinitionKey");
        if (StringUtils.isNotBlank(processDefinitionKey)) {
            processInstanceQuery.processDefinitionKey(processDefinitionKey);
        }
        List<ProcessInstance> processInstances = processInstanceQuery.orderByProcessInstanceId().asc()
                .listPage(pageable.getOffset(), pageable.getPageSize());
        List<Map<String, Object>> datas = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(processInstances)) {
            Set<String> processInstanceIds = Sets.newHashSet();
            for (ProcessInstance pi : processInstances) {
                processInstanceIds.add(pi.getId());
            }
            List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceIds(processInstanceIds).list();
            for (ProcessInstance processInstance : processInstances) {
                ExecutionEntity executionEntity = (ExecutionEntity) processInstance;
                Map<String, Object> data = Maps.newHashMap();
                String businessKey = executionEntity.getBusinessKey();
                ProcessDefinition pd = repositoryService.getProcessDefinition(executionEntity.getProcessDefinitionId());
                for (HistoricProcessInstance hpi : historicProcessInstances) {
                    if (hpi.getId().equals(processInstance.getId())) {
                        data.put("startUserId", hpi.getStartUserId());
                        break;
                    }
                }
                data.put("id", executionEntity.getId());
                data.put("executionEntityId", executionEntity.getId());
                data.put("businessKey", businessKey);
                data.put("processDefinitionKey", pd.getKey());
                data.put("processDefinitionName", pd.getName());
                data.put("activityNames", activitiService.findActiveTaskNames(businessKey));
                datas.add(data);
            }
        }
        return new PageImpl(datas, pageable, processInstanceQuery.count());
    }

    //    public HttpHeaders forceTerminal() {
    //        //删除失败的id和对应消息以Map结构返回，可用于前端批量显示错误提示和计算表格组件更新删除行项
    //        Map<String, String> errorMessageMap = Maps.newLinkedHashMap();
    //
    //        String[] ids = getParameterIds();
    //        for (String id : ids) {
    //            String msg = "Terminal processInstance[" + id + "]  by user " + AuthContextHolder.getAuthUserPin();
    //            logger.debug(msg);
    //            activitiService.deleteProcessInstanceByProcessInstanceId(id, msg);
    //        }
    //        int rejectSize = errorMessageMap.size();
    //        if (rejectSize == 0) {
    //            setModel(OperationResult.buildSuccessResult("强制结束流程实例选取记录:" + ids.length + "条"));
    //        } else {
    //            if (rejectSize == ids.length) {
    //                setModel(OperationResult.buildFailureResult("强制结束流程实例操作失败", errorMessageMap));
    //            } else {
    //                setModel(OperationResult.buildWarningResult("强制结束流程实例操作已处理. 成功:" + (ids.length - rejectSize) + "条" + ",失败:" + rejectSize + "条",
    //                        errorMessageMap));
    //            }
    //        }
    //        return buildDefaultHttpHeaders();
    //    }
}
