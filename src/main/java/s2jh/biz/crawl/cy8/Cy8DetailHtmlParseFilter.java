package s2jh.biz.crawl.cy8;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Cy8DetailHtmlParseFilter extends Cy8BaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//DIV[@class='pp_intro']//LI");
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

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.cy8.com.cn/[a-z]+/\\d+$";
    }
}
