package s2jh.biz.crawl.hc360;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Hc360StartHtmlParseFilter extends Hc360BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.hc360.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node parentNode = selectSingleNode(df, "//DIV[@class='item_marketplace_L' and contains(H2,'工程机械')]");
        if (parentNode != null) {
            NodeList nodeList = selectNodeList(parentNode, ".//dd/a");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String searchUrl = getNodeAttribute(nodeList.item(i), "href").replace("seller", "enterprise");
                    webPage.addOutlink(searchUrl);
                }
            }
        }
        return null;
    }

}
