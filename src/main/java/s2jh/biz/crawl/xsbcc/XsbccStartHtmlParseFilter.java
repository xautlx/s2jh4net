package s2jh.biz.crawl.xsbcc;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import com.mongodb.DBObject;

public class XsbccStartHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.xsbcc.com/company.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String firstPageURL = "http://www.xsbcc.com/common/company.htm?r=0.1807033922486062&pn=1&ru=&uid=&hy=&addr=&quan=&tm1=&tm2=&se=&tp=0&tz=&gao=&phoneemail=13124705728&od=txtno%20desc";
        webPage.addOutlink(firstPageURL, true);
        return null;
    }

}
