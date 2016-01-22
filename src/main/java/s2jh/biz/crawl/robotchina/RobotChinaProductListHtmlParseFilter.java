package s2jh.biz.crawl.robotchina;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class RobotChinaProductListHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://(?!www).*.robot-china.com/sell.*$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取产品列表信息
        NodeList nodes = selectNodeList(doc, "//DIV[@class='thumb']/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                String href = getXPathAttribute(nodes.item(i), "./", "href");
                webPage.addOutlink(href);
            }
        }

        //获取下一页信息
        Node node = selectSingleNode(doc, "//DIV[@class='pages']/a[@class='next']");
        if (node != null) {
            String href = getXPathAttribute(node, "./", "href");
            if (!href.endsWith("robot-china.com/sell/page-1.shtml")) {
                webPage.addOutlink(href);
            }
        }

        return null;

    }

}
