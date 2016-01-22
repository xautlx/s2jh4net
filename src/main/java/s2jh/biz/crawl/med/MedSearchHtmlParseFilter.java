package s2jh.biz.crawl.med;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class MedSearchHtmlParseFilter extends MedBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "http://www.3618med.com/company/companylist/comtype_23_[0-9]{0,}_20.html";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//DIV[@class='comlist cmplist_cmplist_2015']/UL/LI");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node liNode = nodes.item(i);
                String href = getXPathAttribute(liNode, ".//SPAN[@class='comdeil']/A", "href");
                if (StringUtils.isNotBlank(href)) {
                    if (!href.startsWith("http")) {
                        webPage.addOutlink(StringUtils.replace("http://www.3618med.com" + href, "companyinfo", "introduce"));
                    } else {
                        webPage.addOutlink(href + "/sub/zh-CN/aboutus.html");
                    }
                }
            }
            String pager = StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.3618med.com/company/companylist/comtype_23_"),
                    "_20.html");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;
            webPage.addOutlink("http://www.3618med.com/company/companylist/comtype_23_" + nextPage + "_20.html");
        }
        return null;
    }
}
