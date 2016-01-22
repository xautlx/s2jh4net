package s2jh.biz.crawl.huangye88;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class HuangYe88SearchHtmlParseFilter extends HuangYe88BaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取除了“首页”的所有分类
        NodeList nodes = selectNodeList(doc, "//DIV[@class='ad_list']/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                webPage.addOutlink(href);
            }
        }
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://b2b.huangye88.com/qiye/wangzhan/?$";
    }
}
