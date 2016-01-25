package s2jh.biz.shop.support.data;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import lab.s2jh.core.data.BaseDatabaseDataInitialize;
import lab.s2jh.core.util.DateUtils;
import lab.s2jh.core.util.MockEntityUtils;
import lab.s2jh.module.auth.entity.User;
import lab.s2jh.module.auth.service.RoleService;
import lab.s2jh.module.auth.service.UserService;
import lab.s2jh.module.schedule.service.JobBeanCfgService;
import lab.s2jh.module.sys.entity.ConfigProperty;
import lab.s2jh.module.sys.service.ConfigPropertyService;
import lab.s2jh.module.sys.service.DataDictService;
import lab.s2jh.support.service.DynamicConfigService;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import s2jh.biz.shop.cons.BizConstant;
import s2jh.biz.shop.entity.SiteUser;
import s2jh.biz.shop.service.SiteUserService;

import com.google.common.collect.Lists;

/**
 * 业务数据初始化处理器
 */
@Component
public class BizDatabaseDataInitialize extends BaseDatabaseDataInitialize {

    private final static Logger logger = LoggerFactory.getLogger(BizDatabaseDataInitialize.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ConfigPropertyService configPropertyService;

    @Autowired
    private DataDictService dataDictService;

    @Autowired
    private JobBeanCfgService jobBeanCfgService;

    @Autowired
    private SiteUserService siteUserService;

    @Override
    public void initializeInternal() {

        if (configPropertyService.findByPropKey(BizConstant.CFG_HTML_FAQ) == null) {
            ConfigProperty entity = new ConfigProperty();
            entity.setPropKey(BizConstant.CFG_HTML_FAQ);
            entity.setPropName("常见问题文案");
            entity.setHtmlValue(getStringFromTextFile("faq.txt"));
            configPropertyService.save(entity);
        }

        //开发模式创建一些测试数据
        if (DynamicConfigService.isDemoMode()) {

            logger.debug("Prepare data for DEMO mode...");

            //获取一些随机图片集合数据
            List<String> randomImages = Lists.newArrayList();
            URL url = BizDatabaseDataInitialize.class.getResource("images");
            String fileName = url.getFile();
            Collection<File> files = FileUtils.listFiles(new File(fileName), null, false);
            for (File file : files) {
                randomImages.add("/files/mock/" + file.getName());
            }

            //如果为空表则初始化模拟数据
            if (isEmptyTable(SiteUser.class)) {
                //随机一定数量数据
                int cnt = MockEntityUtils.randomInt(10, 20);
                for (int i = 0; i < cnt; i++) {
                    //随机当前系统时间
                    DateUtils.setCurrentDate(MockEntityUtils.randomDate(90, -7));

                    String seq = String.format("%03d", i);
                    User user = new User();
                    user.setAuthUid("test" + seq);
                    user.setTrueName("测试账号" + seq);
                    userService.save(user, "123456");

                    SiteUser siteUser = MockEntityUtils.buildMockObject(SiteUser.class);
                    siteUser.setUser(user);
                    //随机头像
                    siteUser.setHeadPhoto(MockEntityUtils.randomCandidates(randomImages));
                    siteUserService.save(siteUser);
                }
            }
            //提交当前事务数据，以模拟实际情况中分步骤创建业务数据
            commitAndResumeTransaction();
        }
    }
}
