package s2jh.biz.crawl.media;

import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class MediaStartHtmlParseFilter extends MediaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://media.mediavalue.com.cn/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        webPage.addOutlink("http://media.mediavalue.com.cn/paper/");
        webPage.addOutlink("http://media.mediavalue.com.cn/magazine/");
        webPage.addOutlink("http://media.mediavalue.com.cn/tv/");
        webPage.addOutlink("http://media.mediavalue.com.cn/net/");
        webPage.addOutlink("http://media.mediavalue.com.cn/radio/");
        webPage.addOutlink("http://media.mediavalue.com.cn/out/");
        return null;
    }

}
