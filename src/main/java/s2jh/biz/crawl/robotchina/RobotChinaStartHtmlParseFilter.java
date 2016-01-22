package s2jh.biz.crawl.robotchina;

import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class RobotChinaStartHtmlParseFilter extends RobotChinaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.robot-china.com/company/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String firstPageURL = "http://www.robot-china.com/company/search-htm-page-1-vip-0.html";
        webPage.addOutlink(firstPageURL, true);
        return null;
    }
}
