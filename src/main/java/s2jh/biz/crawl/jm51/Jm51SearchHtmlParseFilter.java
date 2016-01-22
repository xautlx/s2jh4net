package s2jh.biz.crawl.jm51;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Jm51SearchHtmlParseFilter extends Jm51BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://paihang.51jiameng.com/paihang.asp\\?cl=[0-9]{1,}(&page=[0-9]{1,})?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodeList = selectNodeList(doc, "/html/body/div/div/div[4]/div[2]/div[2]/table/tbody[1]/tr");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node storeNode = nodeList.item(i);
                String storeName = getXPathValue(storeNode, "./td[2]/a");
                if (StringUtils.isBlank(storeName)) {
                    return null;
                }
                String storeUrl = getXPathAttribute(storeNode, "./td[2]/a", "href");
                webPage.addOutlink(storeUrl);
            }

            String pager = StringUtils.substringAfter(url, "&page=");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;

            String prefix = StringUtils.substringBefore(url, "&page=");
            if (StringUtils.isBlank(prefix)) {
                prefix = url;
            }
            String nextPageUrl = prefix + "&page=" + nextPage;
            webPage.addOutlink(nextPageUrl);
        }

        return null;
    }

}
