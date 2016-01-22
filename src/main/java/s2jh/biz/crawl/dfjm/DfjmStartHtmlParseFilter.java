package s2jh.biz.crawl.dfjm;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class DfjmStartHtmlParseFilter extends DfjmBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dfjmw.cn/28cnjmlsd/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList categoryList = selectNodeList(doc, "//*[@id='hotlinkOp']/div[3]/ul/li");
        if (categoryList != null && categoryList.getLength() > 0) {
            for (int i = 0; i < categoryList.getLength(); i++) {
                Node categoryNode = categoryList.item(i);
                String categoryUrl = getXPathAttribute(categoryNode, "./a", "href");
                if (StringUtils.isNotBlank(categoryUrl)) {
                    if (categoryUrl.contains("sid")) {
                        webPage.addOutlink(categoryUrl);
                    }
                }
            }
        }
        return null;
    }
}
