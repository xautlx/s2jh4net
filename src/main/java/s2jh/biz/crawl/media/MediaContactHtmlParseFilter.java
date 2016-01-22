package s2jh.biz.crawl.media;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class MediaContactHtmlParseFilter extends MediaBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://media.mediavalue.com.cn/index/[0-9]{0,}.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        //媒体名片
        {
            NodeList nodes = selectNodeList(df, "//DIV[@class='m_mtmp']/UL/LI");
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
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://media.mediavalue.com.cn/index/"), ".html");
    }

}
