package s2jh.biz.crawl.hc360;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Hc360ContactHtmlParseFilter extends Hc360BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://[0-9a-zA-Z]{1,}.b2b.hc360.com/shop/company.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node contactboxNode = selectSingleNode(df, "//DIV[@class='contactbox']");
        if (contactboxNode != null) {
            NodeList liNodeList = selectNodeList(contactboxNode, "//*[@id='popLoginShow']/li");
            if (liNodeList != null && liNodeList.getLength() > 0) {
                for (int i = 0; i < liNodeList.getLength(); i++) {
                    String tdText = liNodeList.item(i).getTextContent().replace("&nbsp;", "");
                    String[] nodeTexts = StringUtils.split(tdText, "：");
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }
                }
            }

            liNodeList = selectNodeList(contactboxNode, "./table/tbody/tr[2]/td/ul[1]/li");
            if (liNodeList != null && liNodeList.getLength() > 0) {
                for (int i = 0; i < liNodeList.getLength(); i++) {
                    String tdText = liNodeList.item(i).getTextContent().replace("&nbsp;", "");
                    String[] nodeTexts = StringUtils.split(tdText, "：");
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }
                }
                Node fristNode = liNodeList.item(0);
                if (fristNode != null) {
                    String linkman = getXPathValue(fristNode, "./a/span");
                    String title = getXPathValue(fristNode, "./span[1]");
                    if (StringUtils.isNotBlank(linkman)) {
                        putKeyValue(parsedDBObject, "联系人", linkman);
                    }
                    if (StringUtils.isNotBlank(title)) {
                        putKeyValue(parsedDBObject, "职务", title);
                    }
                }
            }

            return parsedDBObject;
        }

        return null;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://", ".b2b.hc360.com/shop/company.html");
    }
}
