package s2jh.biz.crawl.yibiao;

import lab.s2jh.module.crawl.vo.WebPage;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YibiaoSearchHtmlParseFilter extends YibiaoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取“首页”的所有公司
        NodeList nodes = selectNodeList(doc, "//DIV[@class='list-view']//h3/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                webPage.addOutlink(href);
            }
        }

        //注入下一页
        webPage.addOutlink("http://www.21yibiao.com/company/index-htm-page-2.html");
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.21yibiao.com/company/?$";
    }
}
