package s2jh.biz.crawl.cy8;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Cy8SearchHtmlParseFilter extends Cy8BaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取除了“首页”的所有分类
        NodeList nodes = selectNodeList(doc, "//DIV[@class='nav']//LI");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./A", "href");
                if (href.startsWith("/")) {
                    webPage.addOutlink("http://www.cy8.com.cn" + href);
                }
            }
        }

        //获取首页加盟导航的加盟店链接
        NodeList nodeList = selectNodeList(doc, "//DIV[@class='kcjm_dh']//LI");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String href = getXPathAttribute(node, "./A", "href");
                webPage.addOutlink(href);
            }
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.cy8.com.cn/$";
    }
}
