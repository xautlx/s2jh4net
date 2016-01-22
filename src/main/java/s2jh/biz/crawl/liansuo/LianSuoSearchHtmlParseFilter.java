package s2jh.biz.crawl.liansuo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class LianSuoSearchHtmlParseFilter extends LianSuoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取餐饮分类
        Node node = selectSingleNode(doc, "//DIV[@class='lsrw_l']//A[1]");
        String href = getXPathAttribute(node, "./", "href");
        webPage.addOutlink(href);

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.liansuo.com/?$";
    }
}
