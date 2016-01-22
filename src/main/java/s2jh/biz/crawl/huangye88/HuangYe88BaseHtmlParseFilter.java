package s2jh.biz.crawl.huangye88;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;

public abstract class HuangYe88BaseHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getSiteName(String url) {
        return "b2b.huangye88.com";
    }
}
