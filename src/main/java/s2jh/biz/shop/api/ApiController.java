package s2jh.biz.shop.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lab.s2jh.core.exception.BaseRuntimeException;
import lab.s2jh.core.exception.ValidationException;
import lab.s2jh.core.service.GlobalConfigService;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private DynamicConfigService dynamicConfigService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model) {
        return "api/index";
    }

    @RequestMapping(value = "/html5/router", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object apiForHtml5Web(@RequestParam(value = "method", required = false) String method,
            HttpServletRequest request) {
        return apiForMobileApp(method, request);
    }

    /**
     * 接口含义说明: 移动端应用接口访问入口
     * 调用请求路径：/api/app/router
     * 
     * 全局输入参数列表：
     *  - method 必须参数, 指定调用接口的方法名称
     *  - debug 可选参数，true/false：标识是否开启debug模式调用，影响服务端日志输出和响应信息内容
     *  - verbose 可选参数，true/false：在debug模式下，是否输出更多细节信息
     *  - 其他参数根据各业务接口定义需要传入
     *  
     * 全局输出参数列表：
     *  - 根据各业务接口定义输出JSON结构数据
     * 
     * @return ModelMap Map结构数据最后转换为JSON形式返回
     */
    @RequestMapping(value = "/app/router", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object apiForMobileApp(@RequestParam(value = "method", required = false) String method,
            HttpServletRequest request) {
        try {
            if (StringUtils.isBlank(method)) {
                return buildFailureResponse(ErrorCodeMessage.E110);
            }
            try {
                return MethodUtils.invokeMethod(this, method, request);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return buildFailureResponse(ErrorCodeMessage.E111.format(method));
            }
        } catch (IllegalArgumentException iae) {
            return buildFailureResponse(ErrorCodeMessage.E222.format(iae.getMessage()));
        } catch (Exception e) {
            if (e instanceof BaseRuntimeException) {
                BaseRuntimeException ex = (BaseRuntimeException) e;
                String errorCode = ex.getErrorCode();
                if (StringUtils.isBlank(errorCode)) {
                    errorCode = ErrorCodeMessage.E999.name();
                }
                return buildFailureResponse(errorCode, ex.getMessage());
            }
            return buildFailureResponse(ErrorCodeMessage.E999.format(e.getMessage()));
        }
    }

    /**
     * 接口含义说明: 检查确认接口访问正常
     * 调用方法名称：ping
     * 
     * 业务输入参数列表：
     *  - 无
     *  
     * 业务输出参数列表：
     *  - result PONG：标识接口服务正常
     *  - buildVersion 当前应用部署版本
     *  - serverTime 当前服务器时间
     *  
     * @param request HttpServletRequest请求
     * @return ModelMap Map结构数据最后转换为JSON形式返回
     */
    public ModelMap ping(HttpServletRequest request) {
        ModelMap response = buildSuccessResponse();
        response.put("result", "PONG");
        response.put("buildVersion", GlobalConfigService.getBuildVersion());
        response.put("serverTime", DateUtils.formatTimeNow());
        return response;
    }

    private Map<String, Object> buildFailureResponse(ErrorCodeMessage error) {
        return buildFailureResponse(error.name(), error.getMessage());
    }

    private ModelMap buildFailureResponse(String code, String message) {
        logger.warn("error_code: {}, error_message: {}", code, message);
        ModelMap response = new ModelMap();
        response.addAttribute("code", code);
        response.addAttribute("message", message);
        return response;
    }

    private ModelMap buildSuccessResponse() {
        return buildSuccessResponse(null);
    }

    private ModelMap buildSuccessResponse(ModelMap modelMap) {
        ModelMap response = new ModelMap();
        if (modelMap != null) {
            response.addAllAttributes(modelMap);
            Assert.isTrue(!modelMap.containsKey("code"), "业务返回数据不能含有code属性");
        }
        response.addAttribute("code", "S000");
        return response;
    }

    protected String getRequiredParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtils.isBlank(value)) {
            throw new ValidationException(ErrorCodeMessage.E112.name(), ErrorCodeMessage.E112.format(name).getMessage());
        }
        return value;
    }

    protected String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }
}
