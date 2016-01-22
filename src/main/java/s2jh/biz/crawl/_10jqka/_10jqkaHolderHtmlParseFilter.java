package s2jh.biz.crawl._10jqka;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class _10jqkaHolderHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockpage.10jqka.com.cn/[0-9]+/holder/?$";
    }

    @Override
    protected String getSiteName(String url) {
        return "www.xsbcc.com";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://stockpage.10jqka.com.cn/", "/holder");
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        Node node = selectSingleNode(doc, "//tr[TH='总股本(万股)']");
        if (node != null) {
            String key = getXPathValue(node, "./th");
            String value = getXPathValue(node, "./td[1]");
            putKeyValue(parsedDBObject, key, Double.parseDouble(value));
            return parsedDBObject;
        } else {
            return null;
        }
    }
}
