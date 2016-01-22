package s2jh.biz.crawl.yibiao;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class YibiaoIntroduceHtmlParseFilter extends YibiaoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        //获取公司档案信息
        NodeList nodes = selectNodeList(doc, "//td[@id='main']/div[5]/div/table/tbody/tr");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String key1 = getXPathValue(node, "./td[1]");
                String value1 = getXPathValue(node, "./td[2]");
                putKeyValue(parsedDBObject, StringUtils.substringBeforeLast(key1, "："), value1);
                if (StringUtils.isNotEmpty(getXPathValue(node, "./td[3]"))) {
                    String key2 = getXPathValue(node, "./td[3]");
                    String value2 = getXPathValue(node, "./td[4]");
                    putKeyValue(parsedDBObject, StringUtils.substringBeforeLast(key2, "："), value2);
                }
            }
        }

        //获取联系方式
        NodeList nodeList = selectNodeList(doc, "//*[@id='side']//div[@class='side_body']/ul[contains(LI,'联系人：')]/li");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String nodeText = nodeList.item(i).getTextContent().replaceAll("\\n", "").replace(" ", "");
                //中文冒号分隔
                String[] nodeTexts = StringUtils.split(nodeText, "：");
                //容错处理，英文冒号分隔
                if (nodeTexts.length <= 1) {
                    nodeTexts = StringUtils.split(nodeText, ":");
                }
                if (nodeTexts.length > 1 && StringUtils.isNotBlank(nodeTexts[1].trim())) {
                    putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                }
            }
        }

        if ((nodes != null && nodes.getLength() > 0) || (nodeList != null && nodeList.getLength() > 0)) {
            return parsedDBObject;
        } else {
            return null;
        }
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.21yibiao.com/com/\\w+/introduce/?$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.21yibiao.com/com/"), "/introduce");
    }
}
