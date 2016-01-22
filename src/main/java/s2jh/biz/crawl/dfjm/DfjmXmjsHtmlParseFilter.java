package s2jh.biz.crawl.dfjm;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class DfjmXmjsHtmlParseFilter extends DfjmBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.dfjmw.cn/search/item/\\?id=[0-9]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        NodeList trNodeList = selectNodeList(doc, "//*[@id='innermainbody']/div[3]/div[2]/table/tbody/tr");
        if (trNodeList != null && trNodeList.getLength() > 0) {
            for (int i = 0; i < trNodeList.getLength(); i++) {
                Node trNode = trNodeList.item(i);
                NodeList tdNodeList = selectNodeList(trNode, "./td");
                if (tdNodeList != null && tdNodeList.getLength() > 0) {
                    for (int j = 0; j < tdNodeList.getLength(); j = j + 2) {
                        String key = StringUtils.remove(tdNodeList.item(j).getTextContent(), "&nbsp;");
                        if (StringUtils.isBlank(key)) {
                            continue;
                        }
                        String value = StringUtils.remove(tdNodeList.item(j + 1).getTextContent(), "&nbsp;");
                        putKeyValue(parsedDBObject, key, value);
                    }
                }
            }
        }
        return parsedDBObject;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringAfter(url, "http://www.dfjmw.cn/search/item/?id=");
    }
}
