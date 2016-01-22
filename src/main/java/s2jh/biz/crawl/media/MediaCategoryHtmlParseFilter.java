package s2jh.biz.crawl.media;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.mongodb.DBObject;

public class MediaCategoryHtmlParseFilter extends MediaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://media.mediavalue.com.cn/(paper|magazine|tv|net|radio|out)/$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        Node parentNode = selectSingleNode(doc, "//*[@id='f_pager_next']");
        if (parentNode != null) {
            Node node = parentNode.getLastChild();
            NamedNodeMap atrributes = node.getAttributes();
            Node attrNode = atrributes.getNamedItem("href");
            if (attrNode != null) {
                String pageUrl = attrNode.getTextContent();
                pageUrl = StringUtils.substringBefore(pageUrl, ".html");

                String prefix = pageUrl.substring(0, 16);
                String totalPage = pageUrl.substring(16);

                if (StringUtils.isNotBlank(totalPage)) {
                    for (int i = 1; i <= Integer.parseInt(totalPage); i++) {
                        webPage.addOutlink(url + prefix + i + ".html");
                    }
                }
            }
        }

        return null;
    }

    public static void main(String args[]) {
        String pageUrl = "l-000000-110000-22.html";
        pageUrl = StringUtils.substringBefore(pageUrl, ".html");

        String prefix = pageUrl.substring(0, 16);
        String totalPage = pageUrl.substring(16);
        System.out.println(prefix + "   " + totalPage);
    }
}
