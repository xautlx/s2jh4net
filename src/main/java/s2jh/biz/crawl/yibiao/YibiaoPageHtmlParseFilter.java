package s2jh.biz.crawl.yibiao;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YibiaoPageHtmlParseFilter extends YibiaoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取列表公司
        NodeList nodes = selectNodeList(doc, "//DIV[@class='list-view']//h3/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                webPage.addOutlink(href);
            }

            //注入下一页
            int pageNum = Integer.parseInt(StringUtils.substringAfter(StringUtils.substringBefore(url, ".html"),
                    "http://www.21yibiao.com/company/index-htm-page-"));
            String nextPage = "http://www.21yibiao.com/company/index-htm-page-" + ++pageNum + ".html";

            webPage.addOutlink(nextPage);
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.21yibiao.com/company/index-htm-page-\\d+.html$";
    }
}
