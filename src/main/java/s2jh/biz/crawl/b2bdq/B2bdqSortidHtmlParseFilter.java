package s2jh.biz.crawl.b2bdq;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.filter.WebSiteContactInfoStartHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class B2bdqSortidHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.b2bdq.com/category.aspx\\?sortid=[0-9]+$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        String category = getXPathValue(doc, "//div[@id='content']//div[@class='tal-text b f_l']");
        if (StringUtils.isNotBlank(category)) {
            category = StringUtils.substringAfterLast(category, "->").trim();
        }
        NodeList liNodeList = selectNodeList(doc, "//div[@id='content']//div[@class='box_ttt']/table//td/a");
        if (liNodeList != null && liNodeList.getLength() > 0) {
            for (int i = 0; i < liNodeList.getLength(); i++) {
                Node liNode = liNodeList.item(i);
                String outlink = getNodeAttribute(liNode, "href");

                DBObject outlinkParsedDBObject = new BasicDBObject();
                putKeyValue(outlinkParsedDBObject, "站点名称", getNodeText(liNode));
                putKeyValue(outlinkParsedDBObject, "站点类别", category);

                webPage.addOutlink(outlink, WebSiteContactInfoStartHtmlParseFilter.class, getNodeText(liNode), getSiteName(url), outlink,
                        outlinkParsedDBObject);
            }
        }
        return null;
    }

}
