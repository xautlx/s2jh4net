package lab.s2jh.core.context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lab.s2jh.core.security.BearerTokenRealm;
import lab.s2jh.core.security.ShiroJdbcRealm;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cache.internal.StandardQueryCache;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 扩展标准的PropertyPlaceholderConfigurer把属性文件中的配置参数信息放入全局Map变量便于其他接口访问key-value配置数据
 */
public class ExtPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private static Logger logger = LoggerFactory.getLogger(ExtPropertyPlaceholderConfigurer.class);

    private static Map<String, String> ctxPropertiesMap;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<String, String>();
        logger.info("Putting PropertyPlaceholder {}  datas into cache...", props.size());
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            logger.debug(" - {}={}", key, value);
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

            Set<BeanDefinition> beanDefinitions = Sets.newHashSet();
            ClassPathScanningCandidateComponentProvider scan = new ClassPathScanningCandidateComponentProvider(false);
            scan.addIncludeFilter(new AnnotationTypeFilter(org.hibernate.annotations.Cache.class));
            beanDefinitions.addAll(scan.findCandidateComponents("lab.s2jh.**.entity"));
            beanDefinitions.addAll(scan.findCandidateComponents("s2jh.biz.**.entity"));

            //取当前主机名，后续添加RMI Manual列表把当前主机排除掉，否则会导致当前缓存同时也被remove清空导致缓存数据访问丢失
            String hostName = null;
            try {
                hostName = InetAddress.getLocalHost().getHostName().trim();
            } catch (UnknownHostException e) {
                logger.warn(e.getMessage(), e);
            }

            for (BeanDefinition beanDefinition : beanDefinitions) {
                String className = beanDefinition.getBeanClassName();
                for (String rmiServer : rmiServerSplits) {
                    rmiServer = rmiServer.trim();
                    rmiServer = rmiServer + ":" + rmiPort;
                    if (hostName != null && !rmiServer.startsWith(hostName)) {
                        rmiUrls.add("//" + rmiServer + "/" + className);
                    }
                }
            }
            for (String rmiServer : rmiServerSplits) {
                rmiServer = rmiServer.trim();
                rmiServer = rmiServer + ":" + rmiPort;
                if (hostName != null && !rmiServer.startsWith(hostName)) {
                    //for Hibernate Query Cache
                    rmiUrls.add("//" + rmiServer + "/" + UpdateTimestampsCache.class.getName());
                    rmiUrls.add("//" + rmiServer + "/" + StandardQueryCache.class.getName());

                    //for Shiro authorizationCache
                    rmiUrls.add("//" + rmiServer + "/" + ShiroJdbcRealm.class.getName() + ".authorizationCache");
                    rmiUrls.add("//" + rmiServer + "/" + ShiroJdbcRealm.class.getName() + ".authorizationCache.1");
                    rmiUrls.add("//" + rmiServer + "/" + BearerTokenRealm.class.getName() + ".authorizationCache");
                    rmiUrls.add("//" + rmiServer + "/" + BearerTokenRealm.class.getName() + ".authorizationCache.1");
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
}
