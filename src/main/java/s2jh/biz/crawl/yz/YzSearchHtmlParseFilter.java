package s2jh.biz.crawl.yz;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YzSearchHtmlParseFilter extends YzBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "http://www.dianping.com/search/category/[0-9]{1,}/70/g2784(p[0-9]{1,})?";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodeList = selectNodeList(doc, "//*[@id='J_boxList']/ul/li");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node liNode = nodeList.item(i);
                String href = getXPathAttribute(liNode, "./a", "href");
                webPage.addOutlink("http://www.dianping.com" + href);
            }

            String pager = StringUtils.substringAfter(url, "g2784p");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;

            String prefix = StringUtils.substringBefore(url, "g2784");
            String nextPageUrl = prefix + "g2784p" + nextPage;
            webPage.addOutlink(nextPageUrl);
        }
        return null;
    }
}
