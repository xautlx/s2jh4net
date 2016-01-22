package s2jh.biz.crawl.chinamedevice;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ChinaMedeviceSearchHtmlParseFilter extends ChinaMedeviceBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.chinamedevice.cn/company/[0-9]{1,}/1/[0-9]{1,}.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        String categoryName = getXPathValue(df, "//*[@id='list_left']/dl/dt/a[3]");
        String categoryId = StringUtils.substringBetween(url, "http://www.chinamedevice.cn/company/", "/1/");
        DBObject outlinkParsedDBObject = new BasicDBObject();
        putKeyValue(outlinkParsedDBObject, "类别名称", categoryName);
        putKeyValue(outlinkParsedDBObject, "类别编号", categoryId);
        NodeList nodeList = selectNodeList(df, "//dd[@class='cont']");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String companyUrl = getXPathAttribute(nodeList.item(i), "./ul/li[2]/h1/a", "href");
                webPage.addOutlink(companyUrl, null, null, getSiteName(url),
                        StringUtils.substringBefore(StringUtils.substringAfter(companyUrl, "http://www.chinamedevice.cn/company/"), "/"),
                        outlinkParsedDBObject);
            }

            String pager = StringUtils.substringBetween(url, "/1/", ".html");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;
            webPage.addOutlink(StringUtils.substringBeforeLast(url, "/") + "/" + nextPage + ".html");
        }

        return null;
    }

}
