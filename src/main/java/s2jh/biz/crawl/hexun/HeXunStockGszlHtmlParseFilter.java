package s2jh.biz.crawl.hexun;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

/**
 * 和讯股票数据解析: 公司资料
 */
public class HeXunStockGszlHtmlParseFilter extends HeXunStockBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        //基础信息
        {
            NodeList nodeList = selectNodeList(df, "/html/body/div[5]/div[8]/div[1]/div[1]/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    putKeyValue(parsedDBObject, childNodes.item(1).getTextContent(), childNodes.item(3).getTextContent());
                }
            }
        }

        //证券信息
        {
            NodeList nodeList = selectNodeList(df, "/html/body/div[5]/div[8]/div[2]/div[1]/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    putKeyValue(parsedDBObject, childNodes.item(1).getTextContent(), childNodes.item(3).getTextContent());
                }
            }
        }

        //工商信息
        {
            NodeList nodeList = selectNodeList(df, "/html/body/div[5]/div[8]/div[1]/div[2]/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    putKeyValue(parsedDBObject, childNodes.item(1).getTextContent(), childNodes.item(3).getTextContent());
                }
            }
        }

        //联系方式
        {
            NodeList nodeList = selectNodeList(df, "/html/body/div[5]/div[8]/div[2]/div[2]/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    putKeyValue(parsedDBObject, childNodes.item(1).getTextContent(), childNodes.item(3).getTextContent());
                }
            }
        }

        //经营范围
        putKeyValue(parsedDBObject, "经营范围", getXPathValue(df, "/html/body/div[5]/div[8]/div[1]/div[3]/p"));

        //公司简介
        putKeyValue(parsedDBObject, "公司简介", getXPathValue(df, "/html/body/div[5]/div[8]/div[2]/div[3]/p"));

        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockdata.stock.hexun.com/gszl/[a-zA-z0-9]{6,}.shtml$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://stockdata.stock.hexun.com/gszl/"), ".shtml").replaceAll("\\D", "");
    }
}
