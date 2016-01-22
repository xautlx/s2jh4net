package s2jh.biz.crawl.jdypgxw;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class JdypgxwProductHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取列表中的公司链接
        NodeList nodes = selectNodeList(doc, "//UL[@class='pr-widget-list']/li/DIV[@class='pro-offer-company']/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getNodeAttribute(node, "href");
                if (!href.endsWith("/")) {
                    href += "/";
                }
                //注入联系方式
                webPage.addOutlink(href + "contact/");
                //注入公司简介
                webPage.addOutlink(href + "introduce/");
            }
        }

        //获取下一页
        Node node = selectSingleNode(doc, "//DIV[@class='pages']/a[@title = '下一页']");
        if (node != null) {
            String href = getNodeAttribute(node, "href");
            if (StringUtils.isNotEmpty(href) && href.endsWith("html")) {
                webPage.addOutlink(href);
            }
        }
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.jdypgxw.com/tradeinfo/\\d+/list\\d+.html$";
    }

    @Override
    public String getSiteName(String url) {
        return "www.jdypgxw.com";
    }
}
