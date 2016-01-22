package s2jh.biz.crawl.expo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class ExpoSearchHtmlParseFilter extends ExpoBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.expo-china.com/web/exhi/exhi_search.aspx\\?Province=0&Industry=-1&Keywords=&page=[0-9]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList liNodeList = selectNodeList(doc, "//*[@id='home_up2']/div[2]/div[2]/ul/li");
        if (liNodeList != null && liNodeList.getLength() > 0) {
            for (int i = 0; i < liNodeList.getLength(); i++) {
                Node liNode = liNodeList.item(i);
                String expoUrl = getXPathAttribute(liNode, "./div[1]/h3/a", "href");
                if (StringUtils.isNotBlank(expoUrl)) {
                    webPage.addOutlink(expoUrl);
                }
            }

            //注入下一页
            Node nextPageNode = selectSingleNode(doc, "//*[@id='ctl00_MainPageHolder_webPage']/a[contains(text(),'下一页')]");
            if (nextPageNode != null) {
                String nextPageUrl = getNodeAttribute(nextPageNode, "href");
                if (StringUtils.isNotBlank(nextPageUrl)) {
                    webPage.addOutlink("http://www.expo-china.com/web/exhi/" + nextPageUrl);
                }
            }
        }
        return null;
    }
}
