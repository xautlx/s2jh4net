package s2jh.biz.crawl.med;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;

public abstract class MedBaseHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getSiteName(String url) {
        return "www.3618med.com";
    }
}
