package lab.s2jh.module.sys.service.test;

import java.util.List;

import lab.s2jh.core.test.SpringTransactionalTestCase;
import lab.s2jh.module.sys.service.MenuService;
import lab.s2jh.module.sys.vo.NavMenuVO;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends SpringTransactionalTestCase {

    @Autowired
    private MenuService menuService;

    @Test
    public void findAvailableNavMenuVOs() {
        List<NavMenuVO> navMenuVOs = menuService.findAvailableNavMenuVOs();
        logger.debug("navMenuVOs size={}", navMenuVOs.size());
        for (NavMenuVO navMenuVO : navMenuVOs) {
            logger.debug(" - {}", navMenuVO.getMethodInvocation());
        }
    }
}
