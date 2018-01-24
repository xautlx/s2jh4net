package com.entdiy.core.test;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Spring的支持依赖注入的JUnit4 集成测试基类.
 * 
 * 子类需要定义applicationContext文件的位置,如:
 * @ContextConfiguration(locations = { "/applicationContext-test.xml" })
 *
 */
@ActiveProfiles("test")
public abstract class SpringContextTestCase extends AbstractJUnit4SpringContextTests {
}
