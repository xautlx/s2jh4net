package s2jh.biz.crawl.chinamedevice;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class ChinaMedeviceDetailHtmlParseFilter extends ChinaMedeviceBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.chinamedevice.cn/company/[0-9]{1,}/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node linkNode = selectSingleNode(df, "//DIV[@class='qiye_pro']");
        if (linkNode != null) {
            NodeList liNodeList = selectNodeList(linkNode, "./ul/li");
            if (liNodeList != null && liNodeList.getLength() > 0) {
                for (int i = 0; i < liNodeList.getLength(); i++) {
                    String tdText = StringUtils.remove(liNodeList.item(i).getTextContent(), "&nbsp;").replace("若您发现该公司信息有误，可点此提交正确信息。", "");
                    String[] nodeTexts = StringUtils.split(tdText, "：");
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }
                }
            }
        } else {
            linkNode = selectSingleNode(df, "//UL[contains(LI,'联系方式')]");
            NodeList liNodeList = selectNodeList(linkNode, "./li");
            if (liNodeList != null && liNodeList.getLength() > 0) {
                for (int i = 0; i < liNodeList.getLength(); i++) {
                    String tdText = StringUtils.remove(liNodeList.item(i).getTextContent(), "&nbsp;").replace("若您发现该公司信息有误，可点此提交正确信息。", "");
                    String[] nodeTexts = StringUtils.split(tdText, "：");
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
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.chinamedevice.cn/company/"), "/");
    }

}
