package lab.s2jh.module.bpm.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.exception.WebException;
import lab.s2jh.module.bpm.service.ActivitiService;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/admin/bpm")
public class ActivitiController {

    protected static Logger logger = LoggerFactory.getLogger(ActivitiController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    protected ActivitiService activitiService;

    /**
     * 流程运行图显示响应
     * 此处采用的宽松的流程图访问控制，如果业务需要限制流程图的访问需要添加相应的控制逻辑
     */
    @RequestMapping(value = "/diagram", method = RequestMethod.GET)
    @ResponseBody
    public void processInstanceImage(HttpServletRequest request, HttpServletResponse response) {
        InputStream imageStream = null;
        String bizKey = request.getParameter("bizKey");
        if (StringUtils.isNotBlank(bizKey)) {
            imageStream = activitiService.buildProcessImageByBizKey(bizKey);
        } else {
            String processInstanceId = request.getParameter("processInstanceId");
            imageStream = activitiService.buildProcessImageByProcessInstanceId(processInstanceId);
        }

        if (imageStream == null) {
            return;
        }

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        ServletOutputStream out;
        try {
            out = response.getOutputStream();
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                out.write(b, 0, len);
            }
            imageStream.close();
            out.close();
        } catch (IOException e) {
            logger.error("Output process image error", e);
            throw new WebException("流程运行图处理异常", e);
        }
    }
}
