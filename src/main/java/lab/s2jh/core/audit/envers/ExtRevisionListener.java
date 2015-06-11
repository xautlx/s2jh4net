package lab.s2jh.core.audit.envers;

import java.util.Map;

import lab.s2jh.core.security.AuthContextHolder;
import lab.s2jh.core.security.AuthUserDetails;

import org.hibernate.envers.RevisionListener;

import com.google.common.collect.Maps;

/**
 * 扩展默认的RevisionListener，额外追加记录登录用户信息
 * @see http://docs.jboss.org/hibernate/orm/4.2/devguide/en-US/html/ch15.html
 */
public class ExtRevisionListener implements RevisionListener {

    /** 以ThreadLocal机制把Web层相关审计属性值带入Envers监听器 */
    private static final ThreadLocal<Map<String, String>> operationDataContainer = new ThreadLocal<Map<String, String>>();

    public final static String entityClassName = "entityClassName";
    public final static String controllerClassName = "controllerClassName";
    public final static String controllerClassLabel = "controllerClassLabel";
    public final static String controllerMethodName = "controllerMethodName";
    public final static String controllerMethodLabel = "controllerMethodLabel";
    public final static String controllerMethodType = "controllerMethodType";
    public final static String requestMappingUri = "requestMappingUri";

    public static void setKeyValue(Map<String, String> datas) {
        Map<String, String> operationData = operationDataContainer.get();
        if (operationData == null) {
            operationData = Maps.newHashMap();
            operationDataContainer.set(operationData);
        }
        operationData.putAll(datas);
    }

    @Override
    public void newRevision(Object revisionEntity) {
        ExtDefaultRevisionEntity revEntity = (ExtDefaultRevisionEntity) revisionEntity;
        AuthUserDetails authUserDetails = AuthContextHolder.getAuthUserDetails();
        if (authUserDetails != null) {
            revEntity.setAuthGuid(authUserDetails.getAuthGuid());
            revEntity.setAuthUid(authUserDetails.getAuthUid());
            revEntity.setAuthType(authUserDetails.getAuthType());
        }
        Map<String, String> operationData = operationDataContainer.get();
        if (operationData != null) {
            revEntity.setControllerClassName(operationData.get(controllerClassName));
            revEntity.setControllerClassLabel(operationData.get(controllerClassLabel));
            revEntity.setControllerMethodName(operationData.get(controllerMethodName));
            revEntity.setControllerMethodLabel(operationData.get(controllerMethodLabel));
            revEntity.setControllerMethodType(operationData.get(controllerMethodType));
            revEntity.setRequestMappingUri(operationData.get(requestMappingUri));
            revEntity.setEntityClassName(operationData.get(entityClassName));
        }
    }
}
