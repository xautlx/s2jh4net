package s2jh.biz.crawl.hc360;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Hc360IntroduceHtmlParseFilter extends Hc360BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://[0-9a-zA-Z]{1,}.b2b.hc360.com/shop/show.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node detailInfoNode = selectSingleNode(df, "//*[@id='detailInfoDiv']");
        if (detailInfoNode != null) {
            NodeList trNodeList = selectNodeList(detailInfoNode, ".//DIV[@class='detailsinfo']/table/tbody/tr");
            if (trNodeList != null && trNodeList.getLength() > 0) {
                for (int j = 0; j < trNodeList.getLength(); j++) {
                    Node trNode = trNodeList.item(j);
                    NodeList tdNodeList = selectNodeList(trNode, "./td");
                    if (tdNodeList != null && tdNodeList.getLength() > 0) {
                        if (tdNodeList.getLength() % 2 == 0) {
                            for (int z = 0; z < tdNodeList.getLength(); z = z + 2) {
                                putKeyValue(parsedDBObject, tdNodeList.item(z).getTextContent().replaceAll("\\s*", "").replace("：", ""), tdNodeList
                                        .item(z + 1).getTextContent().replaceAll("\\s*", "").replace("&nbsp;", ""));
                            }
                        }
                    }
                }
            }
            return parsedDBObject;
        }
        return null;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://", ".b2b.hc360.com/shop/show.html");
    }
}
