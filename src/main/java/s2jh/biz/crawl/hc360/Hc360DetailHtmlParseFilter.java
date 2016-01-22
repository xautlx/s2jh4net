package s2jh.biz.crawl.hc360;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class Hc360DetailHtmlParseFilter extends Hc360BaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://[0-9a-zA-Z]{1,}.b2b.hc360.com/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        DocumentFragment df = parse(pageText);

        Node navNode = selectSingleNode(df, "//*[@id='module_mainNav']");
        if (navNode == null) {
            navNode = selectSingleNode(df, "//*[@class='mainnav']");
        }
        if (navNode != null) {
            String introduceUrl = getXPathAttribute(navNode, ".//A[contains(text(),'公司介绍') or contains(text(),'公司档案')]", "href");
            if (StringUtils.isNotBlank(introduceUrl)) {
                webPage.addOutlink(introduceUrl);
            }

            String contactUrl = getXPathAttribute(navNode, ".//A[contains(text(),'联系我们') or contains(text(),'联系方式')]", "href");
            if (StringUtils.isNotBlank(contactUrl)) {
                webPage.addOutlink(contactUrl);
            }
            return null;
        } else {
            //联系我们
            Node contactNode = selectSingleNode(df, "//DIV[contains(H2,'联系我们')]");
            if (contactNode != null) {
                NodeList contactNodeList = selectNodeList(contactNode, "/div/ul/li");
                if (contactNodeList != null && contactNodeList.getLength() > 0) {
                    for (int i = 0; i < contactNodeList.getLength(); i++) {
                        String tdText = contactNodeList.item(i).getTextContent();
                        String[] nodeTexts = StringUtils.split(tdText, "：");
                        if (nodeTexts.length > 1) {
                            putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                        }
                    }
                }

                NodeList detailNodeList = selectNodeList(df, "//DIV[@class='tableCon']/DIV");
                if (detailNodeList != null && detailNodeList.getLength() > 0) {
                    for (int i = 0; i < detailNodeList.getLength(); i++) {
                        Node detailNode = detailNodeList.item(i);
                        NodeList trNodeList = selectNodeList(detailNode, "./table/tbody/tr");
                        if (trNodeList != null && trNodeList.getLength() > 0) {
                            for (int j = 0; j < trNodeList.getLength(); j++) {
                                Node trNode = trNodeList.item(j);
                                NodeList tdNodeList = selectNodeList(trNode, "./td");
                                if (tdNodeList != null && tdNodeList.getLength() > 0) {
                                    if (tdNodeList.getLength() % 2 == 0) {
                                        for (int z = 0; z < tdNodeList.getLength(); z = z + 2) {
                                            putKeyValue(parsedDBObject, tdNodeList.item(z).getTextContent().replaceAll("\\s*", "").replace("：", ""),
                                                    tdNodeList.item(z + 1).getTextContent().replaceAll("\\s*", "").replace("&nbsp;", ""));
                                        }
                                    }
                                }
                            }
                        } else {
                            NodeList liNodeList = selectNodeList(detailNode, "./ul/li");
                            if (liNodeList != null && liNodeList.getLength() > 0) {
                                for (int j = 0; j < liNodeList.getLength(); j++) {
                                    String tdText = liNodeList.item(j).getTextContent();
                                    String[] nodeTexts = StringUtils.split(tdText, "：");
                                    if (nodeTexts.length > 1) {
                                        putKeyValue(parsedDBObject, nodeTexts[0].trim(), nodeTexts[1].trim());
                                    }
                                }
                            }
                        }
                    }
                }
                return parsedDBObject;
            }
        }
        return null;
    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBetween(url, "http://", ".b2b.hc360.com");
    }
}
