package s2jh.biz.crawl._31yj;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class _31yjCompanyHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.31yj.com/show/[^/]+/company.html$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://www.31yj.com/show/", "/company.html");
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取联系方式的链接
        NodeList nodes = selectNodeList(doc, "//div[@class='content']/div[6]//ul");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                String nodeText = nodes.item(i).getTextContent();
                //中文冒号分隔
                String[] nodeTexts = StringUtils.split(nodeText, "：");
                //容错处理，英文冒号分隔
                if (nodeTexts.length <= 1) {
                    nodeTexts = StringUtils.split(nodeText, ":");
                }
                if (nodeTexts.length > 1) {
                    putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                }
            }
            return parsedDBObject;
        } else {
            return null;
        }
    }
}
