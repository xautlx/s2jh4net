package s2jh.biz.crawl.robotchina;

import lab.s2jh.core.crawl.AbstractHtmlParseFilter;
import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class RobotChinaProductHtmlParseFilter extends AbstractHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.robot-china.com/sell/show-[^\\.]+.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取产品信息
        NodeList nodes = selectNodeList(doc, "//UL[@class='xieceprodutu']/li");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node n = nodes.item(i);
                String content = n.getTextContent();
                //中文冒号分隔
                String[] nodeTexts = StringUtils.split(content, "：");
                //容错处理，英文冒号分隔
                if (nodeTexts.length <= 1) {
                    nodeTexts = StringUtils.split(content, ":");
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
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.robot-china.com/sell/show-"), ".html");
    }

    @Override
    protected String getSiteName(String url) {
        return "robot-china-sell";
    }

}
