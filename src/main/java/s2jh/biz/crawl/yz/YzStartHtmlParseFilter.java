package s2jh.biz.crawl.yz;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YzStartHtmlParseFilter extends YzBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dianping.com/citylist/citylist\\?citypage=1$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //直辖市、港澳台
        {
            NodeList nodeList = selectNodeList(doc, "//DIV[@class='terms']/A");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    String cityUrl = getNodeAttribute(node, "href");
                    webPage.addOutlink("http://www.dianping.com" + cityUrl);
                }
            }
        }
        //省市
        {
            NodeList nodeList = selectNodeList(doc, "//DL[@class='terms']/DD/A");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    String cityUrl = getNodeAttribute(node, "href");
                    webPage.addOutlink("http://www.dianping.com" + cityUrl);
                }
            }
        }
        return null;
    }
}
