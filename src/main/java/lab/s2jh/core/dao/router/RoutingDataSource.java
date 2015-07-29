package lab.s2jh.core.dao.router;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在方法上注解数据源名称，实现数据源的动态切换
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface RoutingDataSource {
    /**
     * 数据源（集合）名称
     */
    String value();
}
