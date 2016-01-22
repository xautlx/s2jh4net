package s2jh.biz.crawl.dfjm;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;

import com.mongodb.DBObject;

public class DfjmPageHtmlParseFilter extends DfjmBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dfjmw.cn/search/\\?sid=[0-9]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        String totalNumStr = getXPathValue(doc, "//*[@id='innermainbody']/div[22]/strong");
        if (StringUtils.isNotBlank(totalNumStr)) {
            int totalNum = Integer.parseInt(totalNumStr);
            if (totalNum > 0) {
                int totalPage = totalNum % 20 == 0 ? totalNum / 20 : (totalNum / 20 + 1);
                for (int i = 1; i <= totalPage; i++) {
                    webPage.addOutlink(url + "&Page=" + i);
                }
            }
        }
        return null;
    }
}
