package s2jh.biz.crawl.chinamedevice;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class ChinaMedeviceStartHtmlParseFilter extends ChinaMedeviceBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.chinamedevice.cn/company/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        NodeList nodeList = selectNodeList(df, "//*[@id='list_left']/dl/dd[1]/div[2]/ul/li");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String categoryUrl = getXPathAttribute(nodeList.item(i), "./a", "href");
                webPage.addOutlink(categoryUrl);
            }
        }

        return null;
    }

}
