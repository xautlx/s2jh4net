package s2jh.biz.crawl.hexun;

import lab.s2jh.module.crawl.vo.WebPage;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mongodb.DBObject;

public class HeXunStockFinanceHtmlParseFilter extends HeXunStockBaseHtmlParseFilter {

    @Override
    public String getUrlFilterRegex() {
        return "^http://stockdata.stock.hexun.com/2009_zxcwzb_[0-9]{6}.shtml/?$";
    }

    @Override
    public DBObject filterInternal(String url, WebPage webPage, DBObject parsedDBObject) throws Exception {
        String pageText = webPage.getPageText();
        pageText = StringUtils.replace(pageText, "<span id=\"lbl\">", "<tbody>");
        pageText = StringUtils.replace(pageText, "</tr></span>", "</tr></tbody>");
        DocumentFragment df = parse(pageText);

        //财务数据
        {
            NodeList nodeList = selectNodeList(df, "//*[@id='zaiyaocontent']/table/tbody/tr");
            if (nodeList != null && nodeList.getLength() > 0) {
                Node titleNode = nodeList.item(0);
                NodeList titleChildNodes = titleNode.getChildNodes();
                String date1 = titleChildNodes.item(1).getTextContent().replaceAll("\\.", "").trim();
                String date2 = titleChildNodes.item(2).getTextContent().replaceAll("\\.", "").trim();
                String date3 = titleChildNodes.item(3).getTextContent().replaceAll("\\.", "").trim();

                boolean add1 = date1.compareTo("2011") > 0;
                boolean add2 = date2.compareTo("2011") > 0;
                boolean add3 = date3.compareTo("2011") > 0;
                int nodeListSize = nodeList.getLength();
                for (int i = 1; i < nodeListSize; i++) {
                    Node node = nodeList.item(i);
                    NodeList childNodes = node.getChildNodes();
                    //转换数字
                    if (i + 2 < nodeListSize) {
                        Double double1 = string2double(childNodes.item(1).getTextContent());
                        Double double2 = string2double(childNodes.item(2).getTextContent());
                        Double double3 = string2double(childNodes.item(3).getTextContent());

                        if (add1 && double1 != null) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date1, double1);
                        }
                        if (add2 && double2 != null) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date2, double2);
                        }
                        if (add3 && double3 != null) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date3, double3);
                        }
                    } else {
                        if (add1) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date1, childNodes.item(1).getTextContent());
                        }
                        if (add2) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date2, childNodes.item(2).getTextContent());
                        }
                        if (add3) {
                            putKeyValue(parsedDBObject, childNodes.item(0).getTextContent() + date3, childNodes.item(3).getTextContent());
                        }
                    }
                }
            }
        }

        return parsedDBObject;
    }

    private Double string2double(String value) {
        try {
            return Double.parseDouble(value.replaceAll(",", ""));
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    protected String getPrimaryKey(String url) {
        return StringUtils.substringBefore(StringUtils.substringAfter(url, "http://stockdata.stock.hexun.com/2009_zxcwzb_"), ".shtml");
    }
}
