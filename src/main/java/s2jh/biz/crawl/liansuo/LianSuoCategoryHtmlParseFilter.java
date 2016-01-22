package s2jh.biz.crawl.liansuo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class LianSuoCategoryHtmlParseFilter extends LianSuoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        if (url.startsWith("http://www.liansuo.com")) {
            return null;
        }

        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取排行榜链接
        Node node = selectSingleNode(doc, "//A[contains(DIV,'加盟店排行榜')]");
        String href = getXPathAttribute(node, "./", "href");
        webPage.addOutlink(href);

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://[^\\.]*.liansuo.com/?$";
    }
}
