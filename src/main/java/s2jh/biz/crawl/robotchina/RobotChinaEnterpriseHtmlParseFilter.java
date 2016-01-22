package s2jh.biz.crawl.robotchina;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class RobotChinaEnterpriseHtmlParseFilter extends RobotChinaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://[^\\.]+.robot-china.com/$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取联系方式
        Node contact = selectSingleNode(doc, "//*[@id='menu']//a[contains(SPAN,'联系方式')]");
        if (contact != null) {
            String href = getNodeAttribute(contact, "href");
            webPage.addOutlink(href);
        }

        //获取产品信息 
        Node sell = selectSingleNode(doc, "//*[@id='menu']//a[contains(SPAN,'产品')]");
        if (sell != null) {
            String href = getNodeAttribute(sell, "href");
            webPage.addOutlink(href);
        }

        //获取公司介绍
        Node introduce = selectSingleNode(doc, "//*[@id='menu']//a[contains(SPAN,'公司介绍')]");
        if (introduce != null) {
            String href = getNodeAttribute(introduce, "href");
            webPage.addOutlink(href);
        }

        return null;
    }
}
