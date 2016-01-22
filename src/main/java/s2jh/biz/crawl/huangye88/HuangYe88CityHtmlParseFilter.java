package s2jh.biz.crawl.huangye88;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class HuangYe88CityHtmlParseFilter extends HuangYe88BaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取列表公司链接
        NodeList nodes = selectNodeList(doc, "//*[@id='jubao']/dl/dt/h4/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getXPathAttribute(node, "./", "href");
                webPage.addOutlink(href);
            }
        }

        //获取下一页
        Node node = selectSingleNode(doc, "//div[@class='page_tag Baidu_paging_indicator']/a[text()='下一页']");
        String href = getXPathAttribute(node, "./", "href");
        if (StringUtils.isNotEmpty(href)) {
            webPage.addOutlink(href);
        }
        return null;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://b2b.huangye88.com/(?!qiye)[^/]*/wangzhan.*";
    }
}
