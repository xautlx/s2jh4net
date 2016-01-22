package s2jh.biz.crawl.robotchina;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class RobotChinaContactHtmlParseFilter extends RobotChinaBaseHtmlParseFilter {

    @Override
    public String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://"), ".robot-china.com/contact");
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://.*.robot-china.com/contact/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        NodeList nodes = selectNodeList(doc, "//*[@id='main']/div[3]/div/table/tbody/tr");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String name = StringUtils.substringBeforeLast(getXPathValue(node, "./td[1]"), "：");
                Node img = selectSingleNode(node, ".//IMG");
                if (img != null) {
                    String src = getNodeAttribute(img, "src");
                    if (src.startsWith("http://www.robot-china.com/extend/image.php")) {
                        if ("电子邮件".equals(name)) {
                            String ocrResult = processOCR(url, src, false);
                            if (StringUtils.isNotBlank(ocrResult)) {
                                putKeyValue(parsedDBObject, name, ocrResult);
                            }
                        } else {
                            String ocrResult = processOCR(url, src, true);
                            if (StringUtils.isNotBlank(ocrResult)) {
                                putKeyValue(parsedDBObject, name, ocrResult);
                            }
                        }
                    }
                } else {
                    String value = getXPathValue(node, "./td[2]");
                    if ("公司网址".equals(name)) {
                        Node site = selectSingleNode(node, ".//A[1]");
                        if (site != null) {
                            putKeyValue(parsedDBObject, name, getNodeText(site));
                        }
                    } else if (!"即时通讯".equals(name) && !"在线状态".equals(name)) {
                        putKeyValue(parsedDBObject, name, value);
                    }
                }

            }
            return parsedDBObject;
        } else {
            return null;
        }
    }

}
