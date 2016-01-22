package s2jh.biz.crawl.liansuo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class LianSuoTopHtmlParseFilter extends LianSuoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取排行榜链接
        NodeList nodes = selectNodeList(doc, "//DIV[@class='MS_ph_big_bt']/DIV[@class='ph_small']/dl/dd[2]/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                if (!href.endsWith("/")) {
                    href += "/";
                }
                webPage.addOutlink(href + "lxfs.html");
            }
            //注入下一页的地址
            Node node = selectSingleNode(doc, "//DIV[@class='fenye']/A[text()='下一页']");
            String nextPageUrl = getXPathAttribute(node, "./", "href");
            if (StringUtils.isNotEmpty("nextPageUrl")) {
                webPage.addOutlink(nextPageUrl);
            }
        }

        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.liansuo.com/top-\\d+-\\d+-\\d+.html$";
    }
}
