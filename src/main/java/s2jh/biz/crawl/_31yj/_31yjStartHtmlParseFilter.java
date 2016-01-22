package s2jh.biz.crawl._31yj;

import java.util.Set;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Sets;
import com.mongodb.DBObject;

public class _31yjStartHtmlParseFilter extends AbstractHtmlParseFilter {

    private final static Set<String> whiteList = Sets.newHashSet(new String[] { "制剂机械", "原料药机械", "包装机械", "饮片机械" });

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.31yj.com/company/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//div[@class='content mt10']/div[@class='fl']/div[@class='title borderadd']");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                for (String str : whiteList) {
                    if (node.getTextContent().contains(str)) {
                        String href = getXPathAttribute(node, ".//a[contains(text(),'更多')]", "href");
                        webPage.addOutlink(href);
                    }
                }
            }
        }
        return null;
    }
}
