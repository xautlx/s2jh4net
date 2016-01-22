package s2jh.biz.crawl.robotchina;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;

public abstract class RobotChinaBaseHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getSiteName(String url) {
        return "www.robot-china.com";
    }
}
