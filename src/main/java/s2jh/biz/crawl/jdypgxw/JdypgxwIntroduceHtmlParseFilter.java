package s2jh.biz.crawl.jdypgxw;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class JdypgxwIntroduceHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        //获取公司档案信息
        NodeList nodes = selectNodeList(doc, "//*[@id='main']/div[5]/div/table/tbody/tr");
        if (nodes == null || nodes.getLength() < 1) {
            nodes = selectNodeList(doc, "//*[@id='spdv_16627']/div/div[2]/div[4]/div/table/tbody/tr");
        }

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
            return parsedDBObject;
        } else {
            return null;
        }
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://[^\\.]+.jdypgxw.com/introduce/?$";
    }

    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://", ".jdypgxw.com/introduce");
    }

    @Override
    public String getSiteName(String url) {
        return "www.jdypgxw.com";
    }
}
