package s2jh.biz.crawl.hc360;

import java.net.URLDecoder;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Hc360SearchHtmlParseFilter extends Hc360BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://s.hc360.com/\\?w=.+?&mc=enterprise(&ee=[0-9]{1,})?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node parentNode = selectSingleNode(df, "//DIV[@class='cont-left']");
        NodeList divNodeList = selectNodeList(parentNode, "./div");
        if (divNodeList != null && divNodeList.getLength() > 0) {
            String s = StringUtils.substringBetween(url, "http://s.hc360.com/?w=", "&mc=enterprise");
            String category = URLDecoder.decode(s, "gb2312");
            String siteName = getSiteName(url);
            for (int i = 0; i < divNodeList.getLength(); i++) {
                Node divNode = divNodeList.item(i);
                DBObject outlinkParsedDBObject = new BasicDBObject();
                String companyUrl = getXPathAttribute(divNode, ".//h3/a[1]", "href");
                String companyName = getXPathValue(divNode, ".//h3/a[1]");
                putKeyValue(outlinkParsedDBObject, "_产品分类", category);
                putKeyValue(outlinkParsedDBObject, "公司名称", companyName);
                if (StringUtils.isNotBlank(companyUrl)) {
                    webPage.addOutlink(companyUrl, null, null, siteName, StringUtils.substringBetween(companyUrl, "http://", ".b2b.hc360.com"),
                            outlinkParsedDBObject);
                }
            }

            //注入懒加载请求
            webPage.addOutlink(url + "&af=3");

            //注入下一页
            String nextPageUrl = getXPathAttribute(df, "//A[contains(text(),'下一页')]", "href");
            if (StringUtils.isNotBlank(nextPageUrl)) {
                webPage.addOutlink(nextPageUrl);
            }
        }
        return null;
    }
}
