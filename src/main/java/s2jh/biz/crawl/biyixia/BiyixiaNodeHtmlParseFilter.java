package s2jh.biz.crawl.biyixia;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class BiyixiaNodeHtmlParseFilter extends BiyixiaBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);
        Node contact = selectSingleNode(df, "//DIV[@id='group_blog_contact']");
        if (contact == null) {
            return null;
        }

        //企业信息
        {
            NodeList nodes = selectNodeList(df, "//DIV[@class='row info']/DIV[@class='clearfix']");
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String key = getXPathValue(node, "./DIV[@class='col-xs-3']");
                    String value = null;

                    Node aNode = selectSingleNode(node, "./DIV[@class='col-xs-9']/ul/li/a");
                    if (aNode != null) {
                        value = aNode.getAttributes().getNamedItem("href").getTextContent();
                    }
                    if (value == null) {
                        value = getXPathValue(node, "./DIV[@class='col-xs-9']");
                    }
                    putKeyValue(parsedDBObject, key, value);
                }
            }
        }

        //联系信息
        {
            NodeList nodes = selectNodeList(df, "//DIV[@id='group_blog_contact']//DIV[@class='t_row']");
            if (nodes != null && nodes.getLength() > 0) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    String key = getXPathValue(node, "./DIV[contains(@class, 'key')]");
                    String value = null;

                    Node aNode = selectSingleNode(node, "./DIV[contains(@class, 'no_l_b')]/ul/li/a");
                    if (aNode != null) {
                        value = aNode.getAttributes().getNamedItem("href").getTextContent();
                    }
                    if (value == null) {
                        value = getXPathValue(node, "./DIV[contains(@class, 'no_l_b')]");
                    }
                    putKeyValue(parsedDBObject, key, value);
                }
            }
        }

        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://self-media.biyixia.com/node/[0-9]*$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringAfter(url, "http://self-media.biyixia.com/node/");
    }
}
