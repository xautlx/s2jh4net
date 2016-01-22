package s2jh.biz.crawl.jm51;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;

import com.mongodb.DBObject;

public class Jm51SpecialHtmlParseFilter extends Jm51BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.51jiameng.com/[a-zA-Z0-9]{1,}/[a-zA-Z0-9]{1,}/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        if (url.contains("shop_no")) {
            return null;
        }
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        String storeUrl = getXPathAttribute(doc, "/html/body/div[1]/div/div[5]/div[2]/ul/li[2]/a", "href").trim();
        if (StringUtils.isNotBlank(storeUrl)) {
            String storeNo = StringUtils.substringAfter(storeUrl, "http://so.51jiameng.com/store.asp?shop_no=");
            if (StringUtils.isNotBlank(storeNo)) {
                webPage.addOutlink("http://www.51jiameng.com/jiameng/store.asp?shop_no=" + storeNo);
            }
        }
        return null;
    }
}
