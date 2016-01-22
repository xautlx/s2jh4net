package s2jh.biz.crawl.jm51;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Jm51StartHtmlParseFilter extends Jm51BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://paihang.51jiameng.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList categoryList = selectNodeList(doc, "//*[@id='rank_list']/dl");
        if (categoryList != null && categoryList.getLength() > 0) {
            for (int i = 0; i < categoryList.getLength(); i++) {
                Node categoryNode = categoryList.item(i);
                String categoryUrl = getXPathAttribute(categoryNode, "./dt/a", "href");
                webPage.addOutlink("http://paihang.51jiameng.com/" + categoryUrl);
            }
        }
        return null;
    }

}
