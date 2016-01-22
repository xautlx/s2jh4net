package s2jh.biz.crawl.liansuo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class LianSuoDetailHtmlParseFilter extends LianSuoBaseHtmlParseFilter {

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) {

        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);

        //获取加盟信息
        NodeList nodes = selectNodeList(doc, "//DIV[@class='row02 clearfix']/p");
        if (nodes != null && nodes.getLength() > 0) {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                String content = node.getTextContent();
                String[] contents = StringUtils.split(content, "\n\t\t\t\t");
                for (String str : contents) {
                    //中文冒号分隔
                    String[] nodeTexts = StringUtils.split(str, "：");
                    //容错处理，英文冒号分隔
                    if (nodeTexts.length <= 1) {
                        nodeTexts = StringUtils.split(str, ":");
                    }
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }
                }
            }
        }

        //获取联系方式
        NodeList nodeList = selectNodeList(doc, "//DIV[@class='G_hy_lxfs']//li");
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                String nodeText = nodeList.item(i).getTextContent();
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

        if ((nodeList != null && nodeList.getLength() > 0) || (nodes != null && nodes.getLength() > 0)) {
            return parsedDBObject;
        } else {
            return null;
        }
    }

    @Override
    public String getUrlFilterRegex() {
        return "^http://[^\\.]*.liansuo.com/lxfs.html$";
    }
}
