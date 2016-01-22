package s2jh.biz.crawl.yz;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class YzCityHtmlParseFilter extends YzBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dianping.com/[0-9a-zA-Z]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        Node navNode = selectSingleNode(doc, "//*[@id='index-nav']");
        if (navNode != null) {
            Node targetNode = selectSingleNode(navNode, ".//A[contains(text(),'孕产护理')]");
            if (targetNode != null) {
                String targetUrl = getNodeAttribute(targetNode, "href");
                targetUrl = "http://www.dianping.com" + StringUtils.substringBeforeLast(targetUrl, "/") + "/g2784";
                webPage.addOutlink(targetUrl);
            }
        }
        return null;
    }

}
