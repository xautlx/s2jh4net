package s2jh.biz.crawl.expo;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class ExpoDetailHtmlParseFilter extends ExpoBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://www.expo-china.com/exhibition-[0-9]{1,}.html$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment doc = parse(pageText);
        //会展名称
        putKeyValue(parsedDBObject, "会展名称", getXPathValue(doc, "//*[@id='frmExhibition']/div[4]/div[1]/div[1]/div[2]/div"));

        {
            NodeList liNodeList = selectNodeList(doc, "//*[@id='tab_zhanhui_1']/ul/li");
            if (liNodeList != null && liNodeList.getLength() > 0) {
                for (int i = 0; i < liNodeList.getLength(); i++) {
                    String tdText = liNodeList.item(i).getTextContent();
                    String[] nodeTexts = StringUtils.split(tdText, "：");
                    if (nodeTexts.length > 1) {
                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                    }

                }
            }
        }

        {
            NodeList pNodeList = selectNodeList(doc, "//*[@id='frmExhibition']/div[4]/div[3]/div[2]/div[3]/div[2]/div[2]/p");
            if (pNodeList != null && pNodeList.getLength() > 0) {
                for (int i = 0; i < pNodeList.getLength(); i++) {
                    String pText = pNodeList.item(i).getTextContent();
                    String[] nodeTexts = StringUtils.split(pText, "：");
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
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://www.expo-china.com/exhibition-"), ".html");
    }

}
