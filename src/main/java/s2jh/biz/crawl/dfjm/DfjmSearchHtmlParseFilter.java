package s2jh.biz.crawl.dfjm;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class DfjmSearchHtmlParseFilter extends DfjmBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dfjmw.cn/search/\\?sid=[0-9]{1,}&Page=[0-9]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        NodeList divNodeList = selectNodeList(doc, "//*[@id='innermainbody']/div");
        if (divNodeList != null && divNodeList.getLength() > 0) {
            for (int i = 0; i < divNodeList.getLength(); i++) {
                Node divNode = divNodeList.item(i);
                String storeUrl = getXPathAttribute(divNode, "./ul/li[2]/a", "href");
                if (StringUtils.isBlank(storeUrl)) {
                    continue;
                }
                webPage.addOutlink(storeUrl);

            }
        }
        return null;
    }
}
