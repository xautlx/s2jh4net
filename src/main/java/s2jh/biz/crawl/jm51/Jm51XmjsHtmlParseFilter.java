package s2jh.biz.crawl.jm51;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Jm51XmjsHtmlParseFilter extends Jm51BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.51jiameng.com/jiameng/store.asp\\?shop_no=[0-9]{1,}$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        Node parentNode = selectSingleNode(doc, "//*[@class='main_content_base']/table");
        //品牌
        {
            NodeList nodeList = selectNodeList(parentNode, "./tbody/tr[1]/td/table/tbody/tr/td");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    String tdText = nodeList.item(i).getTextContent();
                    String[] nodeTexts = StringUtils.split(tdText, "：");
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }

                }
            }
        }
        //公司信息
        {
            NodeList nodeList = selectNodeList(parentNode, "./tbody/tr[2]/td/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node trNode = nodeList.item(i);
                    NodeList tdNodeList = selectNodeList(trNode, "./td");
                    if (tdNodeList != null && tdNodeList.getLength() > 0) {
                        for (int j = 0; j < tdNodeList.getLength(); j++) {
                            String tdText = tdNodeList.item(j).getTextContent();
                            String[] nodeTexts = StringUtils.split(tdText, "：");
                            if (nodeTexts.length > 1) {
                                putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                            }
                        }
                    }
                }
            }
        }
        //加盟条件
        {
            NodeList nodeList = selectNodeList(parentNode, "./tbody/tr[4]/td/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node trNode = nodeList.item(i);
                    NodeList tdNodeList = selectNodeList(trNode, "./td");
                    if (tdNodeList != null && tdNodeList.getLength() > 0) {
                        for (int j = 0; j < tdNodeList.getLength(); j++) {
                            String tdText = tdNodeList.item(j).getTextContent();
                            String[] nodeTexts = StringUtils.split(tdText, "：");
                            if (nodeTexts.length > 1) {
                                putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                            }
                        }
                    }
                }
            }
        }
        return parsedDBObject;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringAfter(url, "http://www.51jiameng.com/jiameng/store.asp?shop_no=");
    }
}
