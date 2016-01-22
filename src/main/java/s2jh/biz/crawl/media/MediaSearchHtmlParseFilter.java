package s2jh.biz.crawl.media;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class MediaSearchHtmlParseFilter extends MediaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://media.mediavalue.com.cn/(paper|magazine|tv|net|radio|out)/l-[0-9]{0,}-[0-9]{0,}-[0-9]{0,}.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//DIV[@class='wzjj01']/UL/LI");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node liNode = nodes.item(i);
                String href = getXPathAttribute(liNode, "./A", "href");
                webPage.addOutlink(href);
            }
        }
        return null;
    }

}
