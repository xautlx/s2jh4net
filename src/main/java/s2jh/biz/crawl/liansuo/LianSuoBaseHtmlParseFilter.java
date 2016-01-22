package s2jh.biz.crawl.liansuo;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;

public abstract class LianSuoBaseHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getSiteName(String url) {
        return "www.liansuo.com";
    }
}
