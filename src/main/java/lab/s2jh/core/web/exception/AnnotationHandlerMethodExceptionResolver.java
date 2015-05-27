package lab.s2jh.core.web.exception;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lab.s2jh.core.exception.BaseRuntimeException;
import lab.s2jh.core.exception.DuplicateTokenException;
import lab.s2jh.core.exception.ValidationException;
import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.web.util.ServletUtils;
import lab.s2jh.core.web.view.OperationResult;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.google.common.collect.Maps;

/**
 * 全局的异常解析处理器
 * 注入contentNegotiationManager，判断根据不同请求类型构造对应的数据格式响应，如JSON或JSP页面
 */
public class AnnotationHandlerMethodExceptionResolver implements HandlerExceptionResolver, Ordered {

    private static final Logger logger = LoggerFactory.getLogger("lab.s2jh.errors");

    private ContentNegotiationManager contentNegotiationManager;

    public int getOrder() {
        //优先执行
        return Integer.MIN_VALUE;
    }

    /**
     * <p>注入contentNegotiationManager，判断根据不同请求类型构造对应的数据格式响应，如JSON或JSP页面<p>
     * <p>根据不同异常类型，做一定的错误消息友好转义处理，区分控制不同异常是否需要进行logger日志记录<p>
     * <p>logger记录时把相关请求数据基于MDC方式记录下来，以便问题排查<p>
     */
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object aHandler, Exception e) {

        String errorMessage = null;
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof HttpRequestMethodNotSupportedException) {
            //HTTP请求方式不对
            errorMessage = e.getMessage();
            httpStatus = HttpStatus.BAD_REQUEST;

            //此时还未到Controller方法，无法基于ResponseBody注解判断响应类型，则基于contentNegotiationManager进行判断
            try {
                //先处理特定类型相应
                ServletWebRequest webRequest = new ServletWebRequest(request);
                List<MediaType> mediaTypes = contentNegotiationManager.resolveMediaTypes(webRequest);
                for (MediaType mediaType : mediaTypes) {
                    //JSON类型请求响应
                    if (mediaType.equals(MediaType.APPLICATION_JSON)) {
                        ModelAndView mv = new ModelAndView();
                        MappingJackson2JsonView view = new MappingJackson2JsonView();
                        Map<String, Object> attributes = Maps.newHashMap();
                        attributes.put("type", OperationResult.OPERATION_RESULT_TYPE.failure);
                        attributes.put("code", OperationResult.FAILURE);
                        attributes.put("message", errorMessage);
                        attributes.put("exception", e.getMessage());
                        view.setAttributesMap(attributes);
                        mv.setView(view);
                        return mv;
                    }
                }
            } catch (HttpMediaTypeNotAcceptableException e1) {
                logger.error(e1.getMessage(), e1);
            }

        } else if (e instanceof UnauthenticatedException) {
            //未登录访问，转向登录界面
            errorMessage = "访问需要登录";
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof UnauthorizedException) {
            //访问受限或无权限访问，转向403提示页面
            errorMessage = "访问未授权";
            httpStatus = HttpStatus.FORBIDDEN;
        } else {

            //构建和记录友好和详细的错误信息及消息
            //生成一个异常流水号，追加到错误消息上显示到前端用户，用户反馈问题时给出此流水号给运维或开发人员快速定位对应具体异常细节
            String rand = DateFormatUtils.format(new java.util.Date(), "yyMMddHHmmss") + RandomStringUtils.randomNumeric(3);
            //标记有些校验类型异常无需调用logger对象写入日志
            boolean skipLog = false;
            String errorTitle = "ERR" + rand + ": ";
            errorMessage = errorTitle + "系统运行错误，请联系管理员！";

            //先判断明确子类异常，优先匹配后则终止其他判断
            boolean continueProcess = true;
            if (continueProcess) {
                DuplicateTokenException ex = parseSpecException(e, DuplicateTokenException.class);
                if (ex != null) {
                    continueProcess = false;
                    errorMessage = "请勿重复提交表单";
                    skipLog = true;
                }
            }

            if (continueProcess) {
                //业务校验失败异常，直接反馈校验提示信息即可
                ValidationException ex = parseSpecException(e, ValidationException.class);
                if (ex != null) {
                    continueProcess = false;
                    httpStatus = HttpStatus.BAD_REQUEST;
                    errorMessage = e.getMessage();
                    skipLog = true;
                }
            }

            if (continueProcess) {
                //框架定义的基类运行异常
                BaseRuntimeException ex = parseSpecException(e, BaseRuntimeException.class);
                if (ex != null) {
                    continueProcess = false;
                    errorMessage = errorTitle + e.getMessage();
                }
            }

            if (continueProcess) {
                //对一些数据库异常进行友好转义处理，以便前端用户可以理解
                SQLException ex = parseSpecException(e, SQLException.class);
                if (ex != null) {
                    continueProcess = false;
                    String sqlMessage = ex.getMessage();
                    if (sqlMessage != null && (sqlMessage.indexOf("FK") > -1 || sqlMessage.startsWith("ORA-02292"))) {
                        errorMessage = "该数据已被关联使用：" + sqlMessage;
                        skipLog = true;
                    } else if (sqlMessage != null
                            && (sqlMessage.indexOf("Duplicate") > -1 || sqlMessage.indexOf("UNIQUE") > -1 || sqlMessage.startsWith("ORA-02292"))) {
                        errorMessage = "违反唯一性约束：" + sqlMessage;
                        skipLog = true;
                    }
                }
            }

            if (!skipLog) {
                //记录登录用户信息
                String userId = AuthContextHolder.getAuthUserDisplay();
                if (StringUtils.isNotBlank(userId)) {
                    MDC.put("AUTH_USER", userId);
                }
                //记录时间
                MDC.put("LOG_DATETIME", DateUtils.formatTimeNow());

                //以logger的MDC模式记录组装的字符串信息
                MDC.put("WEB_DATA", ServletUtils.buildRequestInfoToString(request, true));

                logger.error(errorMessage, e);

                MDC.clear();
            }else{
                logger.debug(errorMessage, e);
            }
        }

        //设置http status错误代码，如jqGrid等组件是基于此代码来标识请求处理成功与否
        response.setStatus(httpStatus.value());
        //其余按照标准的error-page处理
        request.setAttribute("javax.servlet.error.message", errorMessage);

        boolean json = false;
        if (aHandler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) aHandler;
            Method method = handlerMethod.getMethod();
            ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
            if (responseBody != null) {
                json = true;
            }
        }

        if (json) {
            ModelAndView mv = new ModelAndView();
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            Map<String, Object> attributes = Maps.newHashMap();
            attributes.put("type", OperationResult.OPERATION_RESULT_TYPE.failure);
            attributes.put("code", OperationResult.FAILURE);
            attributes.put("message", errorMessage);
            attributes.put("exception", e.getMessage());
            view.setAttributesMap(attributes);
            mv.setView(view);
            return mv;
        } else {
            if (httpStatus.equals(HttpStatus.UNAUTHORIZED)) {
                //记录当前请求信息，登录完成后直接转向登录之前URL
                WebUtils.saveRequest(request);
                String view = null;
                String path = request.getServletPath();
                if (path.startsWith("/admin")) {
                    view = "admin/login";
                } else if (path.startsWith("/m")) {
                    view = "m/login";
                } else {
                    view = "w/login";
                }
                return new ModelAndView("redirect:" + view);
            }
        }

        return new ModelAndView("error/" + httpStatus.value());
    }

    /**
     * 取当前异常及递归其root case示例，判定是否特定异常类型的示例或子类示例
     * 如果是则直接返回强制类型转换后的异常示例，否则返回null
     */
    @SuppressWarnings("unchecked")
    private <X> X parseSpecException(Exception e, Class<X> clazz) {
        if (clazz.isAssignableFrom(e.getClass())) {
            return (X) e;
        }
        Throwable cause = e.getCause();
        while (cause != null) {
            if (clazz.isAssignableFrom(cause.getClass())) {
                return (X) cause;
            }
            cause = cause.getCause();
        }
        return null;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }
}