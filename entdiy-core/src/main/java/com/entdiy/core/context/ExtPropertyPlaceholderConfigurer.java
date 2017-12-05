package com.entdiy.core.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.realm.AuthorizingRealm;
import org.hibernate.annotations.Cache;
import org.hibernate.cache.internal.StandardQueryCache;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.persistence.Entity;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 扩展标准的PropertyPlaceholderConfigurer把属性文件中的配置参数信息放入全局Map变量便于其他接口访问key-value配置数据
 */
public class ExtPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static Logger logger = LoggerFactory.getLogger(ExtPropertyPlaceholderConfigurer.class);

    private static Map<String, String> ctxPropertiesMap = new HashMap<>();

    private static String basePackages;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        logger.info("Putting PropertyPlaceholder {}  datas into cache...", props.size());
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            //logger.debug(" - key={}, value={}", keyStr, value);
            ctxPropertiesMap.put(keyStr, value);

            if (keyStr.startsWith("env.") || keyStr.startsWith("env_")) {
                System.setProperty(keyStr, value);
            }
        }

        //云服务器环境一般不支持组播通讯，RMI集群需要peerDiscovery=manual方式
        //通过动态取实体对象信息拼装组播手动通知地址信息字符串
        String rmiServers = props.getProperty("env_ehcache_rmi_servers");
        if (StringUtils.isNotBlank(rmiServers)) {

            String rmiPort = props.getProperty("env_ehcache_rmi_port");
            if (StringUtils.isBlank(rmiPort)) {
                rmiPort = "40001";
            }
            System.setProperty("env_ehcache_rmi_port", rmiPort);
            logger.info("Set env_ehcache_rmi_port={}", rmiPort);

            List<String> rmiUrls = Lists.newArrayList();
            String[] rmiServerSplits = rmiServers.split(",");

            //取当前主机名，后续添加RMI Manual列表把当前主机排除掉，否则会导致当前缓存同时也被remove清空导致缓存数据访问丢失
            String hostName = null;
            try {
                hostName = InetAddress.getLocalHost().getHostName().trim();
            } catch (UnknownHostException e) {
                logger.warn(e.getMessage(), e);
            }

            //对象扫描包集合
            String[] packages = StringUtils.split(basePackages, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

            //实体对象缓存定义
            {
                Set<BeanDefinition> cacheEntityBeanDefinitions = Sets.newHashSet();
                ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
                scan.addIncludeFilter(new AnnotationTypeFilter(Cache.class));
                scan.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
                for (String pkg : packages) {
                    cacheEntityBeanDefinitions.addAll(scan.findCandidateComponents(pkg));
                }
                for (BeanDefinition beanDefinition : cacheEntityBeanDefinitions) {
                    String className = beanDefinition.getBeanClassName();
                    for (String rmiServer : rmiServerSplits) {
                        rmiServer = rmiServer.trim();
                        rmiServer = rmiServer + ":" + rmiPort;
                        if (hostName != null && !rmiServer.startsWith(hostName)) {
                            rmiUrls.add("//" + rmiServer + "/" + className);
                        }
                    }
                }
            }

            //其他特定对象缓存定义
            {
                Set<BeanDefinition> authorizingRealmBeanDefinitions = Sets.newHashSet();
                ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
                scan.addIncludeFilter(new AssignableTypeFilter(AuthorizingRealm.class));
                for (String pkg : packages) {
                    authorizingRealmBeanDefinitions.addAll(scan.findCandidateComponents(pkg));
                }
                for (String rmiServer : rmiServerSplits) {
                    rmiServer = rmiServer.trim();
                    rmiServer = rmiServer + ":" + rmiPort;
                    if (hostName != null && !rmiServer.startsWith(hostName)) {
                        //for Hibernate Query Cache
                        rmiUrls.add("//" + rmiServer + "/" + UpdateTimestampsCache.class.getName());
                        rmiUrls.add("//" + rmiServer + "/" + StandardQueryCache.class.getName());

                        //for Shiro authorizationCache
                        if (CollectionUtils.isNotEmpty(authorizingRealmBeanDefinitions)) {
                            for (BeanDefinition beanDefinition : authorizingRealmBeanDefinitions) {
                                String name = beanDefinition.getBeanClassName();
                                rmiUrls.add("//" + rmiServer + "/" + name + ".authorizationCache");
                                rmiUrls.add("//" + rmiServer + "/" + name + ".authorizationCache.1");
                            }
                        }
                    }
                }
            }
            String rmiUrl = StringUtils.join(rmiUrls, "|");
            System.setProperty("env_ehcache_rmi_rmiUrls", rmiUrl);
            logger.info("Set env_ehcache_rmi_rmiUrls={}", rmiUrl);
        }
    }

    public Map<String, String> getPropertiesMap() {
        return ctxPropertiesMap;
    }

    public String getProperty(String name) {
        return ctxPropertiesMap.get(name);
    }

    public void setBasePackages(String basePackages) {
        ExtPropertyPlaceholderConfigurer.basePackages = basePackages;
    }

    public static String getBasePackages() {
        return basePackages;
    }
}
