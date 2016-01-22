package lab.s2jh.module.crawl.filter;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.Outlink;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class WebSiteContactInfoStartHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node node = selectSingleNode(df, "//A[contains(text(),'联系我们')]");
        if (node == null) {
            node = selectSingleNode(df, "//A[contains(text(),'联系方式')]");
        }
        if (node != null) {
            Node hrefAttr = node.getAttributes().getNamedItem("href");
            if (hrefAttr != null) {
                String href = hrefAttr.getTextContent();
                Outlink outlink = webPage.addOutlink(href, WebSiteContactInfoDetailHtmlParseFilter.class);
                if (outlink != null) {
                    parsedDBObject.put("联系我们URL", outlink.getUrl());
                }
            }
        }
        String title = webPage.getTitle();
        if (StringUtils.isBlank(title)) {
            title = getXPathValue(df, "//TITLE");
            title = StringUtils.substringBefore(title, "-");
        }
        parsedDBObject.put("站点名称", title);
        parsedDBObject.put("站点链接", url);
        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return null;
    }
}
