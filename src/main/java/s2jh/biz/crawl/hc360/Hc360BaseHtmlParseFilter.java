package s2jh.biz.crawl.hc360;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;

public abstract class Hc360BaseHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getSiteName(String url) {
        return "b2b.hc360.com";
    }

}
