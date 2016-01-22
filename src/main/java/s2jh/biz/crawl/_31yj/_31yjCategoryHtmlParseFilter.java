package s2jh.biz.crawl._31yj;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class _31yjCategoryHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.31yj.com/company/\\d+-\\d+.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取联系方式的链接
        NodeList nodes = selectNodeList(doc, "//*[@id='pics']/div/div[1]/div[2]/a");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String href = getNodeAttribute(node, "href");
                webPage.addOutlink(href);
            }

            //注入下一页
            int pageNum = Integer.parseInt(StringUtils.substringBetween(url, "-", ".html"));
            pageNum++;
            String nextPage = StringUtils.substringBefore(url, "-") + "-" + pageNum + ".html";
            webPage.addOutlink(nextPage);
        }

        return null;
    }
}
