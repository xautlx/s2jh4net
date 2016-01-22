package s2jh.biz.crawl.biyixia;

import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class BiyixiaSearchHtmlParseFilter extends BiyixiaBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        //TODO 先简单注入25个页面，后期优化为动态从AJAX获取
        for (int i = 1; i <= 25; i++) {
            webPage.addOutlink("http://self-media.biyixia.com/node/" + i);
        }
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://self-media.biyixia.com/?$";
    }
}
