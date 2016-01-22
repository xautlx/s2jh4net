package s2jh.biz.crawl.liansuo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class LianSuoTopListHtmlParseFilter extends LianSuoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取排行榜列表链接
        NodeList nodes = selectNodeList(doc, "//DIV[@class='phb_list_rgcon']//h2/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                if (!"http://www.liansuo.com/top-1-0-1.html".equals(href)) {
                    webPage.addOutlink(href);
                }
            }
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.liansuo.com/top-1-0-1.html$";
    }
}
