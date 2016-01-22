package s2jh.biz.crawl.sanban18;

import java.util.Map;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.core.util.JsonUtils;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Sanban18ProfileHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.sanban18.com/stock/[0-9]+/profile.html$";
    }

    @Override
    protected String getSiteName(String url) {
        return "www.xsbcc.com";
    }
    
    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://www.sanban18.com/stock/", "/profile.html");
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        String jsonData = "{" + StringUtils.substringBefore(StringUtils.substringAfter(pageText, "var gsgk = eval({"), "});") + "}";
        Map<String, Object> mapData = JsonUtils.readValue(jsonData);

        String content = StringUtils.substringAfterLast(StringUtils.substringBefore(pageText, "$(\"#middle_id\").html(html);"), "var html =");
        DocumentFragment doc = parse(content);
        NodeList nodes = selectNodeList(doc, "//tr");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                String text = n.getTextContent();
                //中文冒号分隔
                String[] nodeTexts = StringUtils.split(text, "：");
                //容错处理，英文冒号分隔
                if (nodeTexts.length <= 1) {
                    nodeTexts = StringUtils.split(text, ":");
                }
                if (nodeTexts.length > 1) {
                    String key = StringUtils.substringBetween(nodeTexts[1], "gsgk['", "']").trim();
                    putKeyValue(parsedDBObject, nodeTexts[0].trim(), mapData.get(key));
                }
            }
            return parsedDBObject;
        } else {
            return null;
        }
    }
}
