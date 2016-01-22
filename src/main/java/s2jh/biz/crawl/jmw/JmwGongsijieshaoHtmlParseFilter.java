package s2jh.biz.crawl.jmw;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class JmwGongsijieshaoHtmlParseFilter extends JmwBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        {
            NodeList nodes = selectNodeList(df, "//DIV[@class='projects_cont']/UL/LI");
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
            }
        }

        return parsedDBObject;
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.jmw.com.cn/xm[0-9]*/gongsijieshao/$";
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "jmw.com.cn/xm"), "/");
    }
}
