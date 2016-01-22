package s2jh.biz.crawl.med;

import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class MedStartHtmlParseFilter extends MedBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.3618med.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        webPage.addOutlink("http://www.3618med.com/company/companylist/comtype_23_1_20.html");
        return null;
    }
}
