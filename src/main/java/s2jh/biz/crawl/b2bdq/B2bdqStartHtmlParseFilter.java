package s2jh.biz.crawl.b2bdq;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class B2bdqStartHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.b2bdq.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList liNodeList = selectNodeList(doc, "//DIV[@id='content']//DIV[@class='catalogue']//DIV[@class='box']/table/tbody/tr");
        if (liNodeList != null && liNodeList.getLength() > 0) {
            for (int i = 0; i < liNodeList.getLength(); i++) {
                Node liNode = liNodeList.item(i);
                String outlink = getXPathAttribute(liNode, "./td[3]/a", "href");
                webPage.addOutlink(outlink);
            }
        }
        return null;
    }

}
