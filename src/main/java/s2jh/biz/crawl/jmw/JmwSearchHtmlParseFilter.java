package s2jh.biz.crawl.jmw;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class JmwSearchHtmlParseFilter extends JmwBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//DIV[@class='div_listeach']");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, ".//DIV[@class='div_img']/A", "href");
                webPage.addOutlink(href);
            }
            String pager = StringUtils.substringBefore(StringUtils.substringAfter(url, "http://search.jmw.com.cn/"), ".html");
            if (StringUtils.isBlank(pager)) {
                pager = "1";
            }
            int nextPage = Integer.valueOf(pager) + 1;
            webPage.addOutlink("http://search.jmw.com.cn/" + nextPage + ".html");
        }
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://search.jmw.com.cn/?([0-9]{0,}.html)?$";
    }
}
