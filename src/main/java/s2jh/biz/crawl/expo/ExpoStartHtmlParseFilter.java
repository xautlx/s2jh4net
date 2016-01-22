package s2jh.biz.crawl.expo;

import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class ExpoStartHtmlParseFilter extends ExpoBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.expo-china.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        webPage.addOutlink("http://www.expo-china.com/web/exhi/exhi_search.aspx?Province=0&Industry=-1&Keywords=&page=1");
        return null;
    }
}
