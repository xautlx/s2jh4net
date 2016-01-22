package s2jh.biz.crawl.jdypgxw;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class JdypgxwStartHtmlParseFilter extends AbstractHtmlParseFilter {

    private static final String[] CATEGORY = { "酒店客房用品", "酒店厨房设备", "酒店清洁设备", "食品饮料" };

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        for (String str : CATEGORY) {
            Node category = selectSingleNode(doc, "//DIV[@class='pullDown']/ul/li[A='" + str + "']/a");
            if (category != null) {
                String href = getNodeAttribute(category, "href");
                webPage.addOutlink(href + "list1.html");
            }
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.jdypgxw.com/?$";
    }
}
